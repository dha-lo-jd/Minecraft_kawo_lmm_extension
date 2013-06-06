package org.lo.d.minecraft.littlemaid;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.minecraft.entity.Entity;
import net.minecraft.src.LMM_EntityLittleMaid;

import org.lo.d.commons.bytecode.ExtendedClassSupport;
import org.lo.d.commons.reflections.ReflectionSupport;
import org.lo.d.minecraft.littlemaid.entity.BaseEntityLittleMaidEx;
import org.lo.d.minecraft.littlemaid.entity.EntityLittleMaidEx;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import cpw.mods.fml.common.registry.EntityRegistry;

public class LMMExMaidChangeManager {

	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.TYPE)
	public @interface LMMExChanger {
		@Retention(RetentionPolicy.RUNTIME)
		@Target(ElementType.METHOD)
		public @interface Change {
		}

		@Retention(RetentionPolicy.RUNTIME)
		@Target(ElementType.FIELD)
		public @interface Instance {
		}

		@Retention(RetentionPolicy.RUNTIME)
		@Target(ElementType.METHOD)
		public @interface Validate {
		}
	}

	public interface MaidChanger {
		LMM_EntityLittleMaid change(LMM_EntityLittleMaid maid);

		int validate(LMM_EntityLittleMaid maid);
	}

	private static class AnnotatedEntryMaidChanger implements MaidChanger {
		private final Method changeMethod;
		private final Method validateMethod;
		private final Object changerInstance;

		private AnnotatedEntryMaidChanger(Method changeMethod, Method validateMethod, Object changerInstance) {
			this.changeMethod = changeMethod;
			this.validateMethod = validateMethod;
			this.changerInstance = changerInstance;
		}

		@Override
		public LMM_EntityLittleMaid change(LMM_EntityLittleMaid maid) {
			try {
				return (LMM_EntityLittleMaid) changeMethod.invoke(changerInstance, maid);
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				return null;
			}
		}

		@Override
		public int validate(LMM_EntityLittleMaid maid) {
			try {
				return (int) validateMethod.invoke(changerInstance, maid);
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				return 0;
			}
		}
	}

	private static class DefaultMaidChanger implements MaidChanger {
		@Override
		public LMM_EntityLittleMaid change(LMM_EntityLittleMaid maid) {
			return new BaseEntityLittleMaidEx(maid);
		}

		@Override
		public int validate(LMM_EntityLittleMaid maid) {
			return LMM_EntityLittleMaid.class.equals(maid.getClass()) ? 1 : 0;
		}
	}

	public static void main(String[] args) {
		new LMMExMaidChangeManager().initialize();
	}

	private Set<MaidChanger> registedChangers = Sets.newHashSet();

	private Map<Class<?>, MaidChanger> cachedChangers = Maps.newHashMap();

	public LMM_EntityLittleMaid changeMaidToEx(LMM_EntityLittleMaid maid) {
		if (!maid.worldObj.isRemote) {
			LMM_EntityLittleMaid littleMaidEx;
			MaidChanger changer = getPrimaryChanger(maid);
			if (changer != null) {
				littleMaidEx = changer.change(maid);
				littleMaidEx.setMaidWait(false);
				littleMaidEx.rotationYawHead = maid.rotationYawHead;
				maid.worldObj.spawnEntityInWorld(littleMaidEx);
				littleMaidEx.getNextEquipItem();
				maid = littleMaidEx;
			}
		}
		return maid;
	}

	public void initialize() {
		registMaidChanger(new DefaultMaidChanger());

		List<Class<? extends LMM_EntityLittleMaid>> maidClasses = ReflectionSupport.getClasses("",
				new ReflectionSupport.ClassFilterWorker<List<Class<? extends LMM_EntityLittleMaid>>>() {

					@Override
					public List<Class<? extends LMM_EntityLittleMaid>> createResultObjectInstsnce() {
						return Lists.newArrayList();
					}

					@Override
					public void work(Class<?> cls, List<Class<? extends LMM_EntityLittleMaid>> resultObject) {
						if (LMM_EntityLittleMaid.class == cls || BaseEntityLittleMaidEx.class == cls
								|| !LMM_EntityLittleMaid.class.isAssignableFrom(cls)) {
							return;
						}

						try {
							@SuppressWarnings("unchecked")
							Class<? extends LMM_EntityLittleMaid> maidClass = (Class<? extends LMM_EntityLittleMaid>) cls;
							resultObject.add(maidClass);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
		for (Class<? extends LMM_EntityLittleMaid> maidClass : maidClasses) {

			try {
				Class<?> c = ExtendedClassSupport.loadAndGenerateNewExtendedClass(BaseEntityLittleMaidEx.class,
						LMM_EntityLittleMaid.class,
						maidClass, EntityLittleMaidEx.class, Lists.<Class<?>> newArrayList());
				@SuppressWarnings("unchecked")
				Class<? extends Entity> genClass = (Class<? extends Entity>) c;
				int entityId = EntityRegistry.findGlobalUniqueEntityId();
				EntityRegistry.registerModEntity(genClass, genClass.getSimpleName(), entityId, LMMExtension.instance,
						80, 3, true);
				EntityRegistry.registerGlobalEntityID(genClass, genClass.getSimpleName(), entityId);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		List<MaidChanger> changers = ReflectionSupport.getClasses("",
				new ReflectionSupport.ClassFilterWorker<List<MaidChanger>>() {
					@Override
					public List<MaidChanger> createResultObjectInstsnce() {
						return Lists.newArrayList();
					}

					@Override
					public void work(Class<?> cls, List<MaidChanger> resultObject) {
						LMMExChanger changerAnno = cls.getAnnotation(LMMExChanger.class);
						if (changerAnno == null) {
							return;
						}

						Object targetInstance = null;
						for (Field field : cls.getDeclaredFields()) {
							if (!Modifier.isStatic(field.getModifiers())) {
								continue;
							}

							LMMExChanger.Instance anno = field.getAnnotation(LMMExChanger.Instance.class);
							if (anno == null) {
								continue;
							}

							try {
								field.setAccessible(true);
								targetInstance = field.get(null);
								break;
							} catch (IllegalArgumentException | IllegalAccessException e) {
								e.printStackTrace();
							}
						}

						if (targetInstance == null) {
							return;
						}

						Method changeMethod = null;
						Method validateMethod = null;
						for (Method method : ReflectionSupport.getAllRecursiveMethods(cls)) {
							Class<?>[] paramTypes = method.getParameterTypes();
							if (paramTypes.length != 1 || !LMM_EntityLittleMaid.class.isAssignableFrom(paramTypes[0])) {
								continue;
							}

							LMMExChanger.Change changeMethodAnno = method.getAnnotation(LMMExChanger.Change.class);
							if (changeMethodAnno != null) {
								Class<?> returnType = method.getReturnType();
								if (returnType != null && LMM_EntityLittleMaid.class.isAssignableFrom(returnType)) {
									method.setAccessible(true);
									changeMethod = method;
								}
							}

							LMMExChanger.Validate validateMethodAnno = method
									.getAnnotation(LMMExChanger.Validate.class);
							if (validateMethodAnno != null) {
								Class<?> returnType = method.getReturnType();
								if (returnType != null
										&& (Integer.class.isAssignableFrom(returnType) || int.class.equals(returnType))) {
									method.setAccessible(true);
									validateMethod = method;
								}
							}

						}
						if (changeMethod == null || validateMethod == null) {
							return;
						}

						AnnotatedEntryMaidChanger entryMaidChanger = new AnnotatedEntryMaidChanger(changeMethod,
								validateMethod, targetInstance);

						resultObject.add(entryMaidChanger);
					}
				});
		for (MaidChanger changer : changers) {
			registMaidChanger(changer);
		}
	}

	public boolean registMaidChanger(MaidChanger maidChanger) {
		return registedChangers.add(maidChanger);
	}

	private MaidChanger doGetPrimaryChanger(LMM_EntityLittleMaid maid) {
		int maxPriority = 0;
		MaidChanger primaryChanger = null;
		for (MaidChanger changer : registedChangers) {
			int priority = changer.validate(maid);
			if (maxPriority < priority) {
				maxPriority = priority;
				primaryChanger = changer;
			}
		}
		return primaryChanger;

	}

	private MaidChanger getPrimaryChanger(LMM_EntityLittleMaid maid) {
		Class<?> maidClass = maid.getClass();
		if (!cachedChangers.containsKey(maidClass)) {
			MaidChanger changer = doGetPrimaryChanger(maid);
			cachedChangers.put(maidClass, changer);
		}
		MaidChanger changer = cachedChangers.get(maidClass);

		return changer;

	}

}
