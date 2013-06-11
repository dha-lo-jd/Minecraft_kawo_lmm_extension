package org.lo.d.minecraft.littlemaid;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import net.minecraft.src.LMM_EntityLittleMaid;

import org.lo.d.commons.reflections.ReflectionSupport;
import org.lo.d.minecraft.littlemaid.LMMExMaidChangeManager.MaidChanger;

public class AnnotatedMaidChangerProvidor implements ReflectionSupport.Worker {
	public static class AnnotatedEntryMaidChanger implements MaidChanger {
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
				return maid;
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

	/**
	 *
	 */
	private final LMMExMaidChangeManager maidChangeManager;

	/**
	 * @param lmmExMaidChangeManager
	 */
	public AnnotatedMaidChangerProvidor(LMMExMaidChangeManager maidChangeManager) {
		this.maidChangeManager = maidChangeManager;
	}

	@Override
	public void work(Class<?> cls) {
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

			LMMExChanger.Validate validateMethodAnno = method.getAnnotation(LMMExChanger.Validate.class);
			if (validateMethodAnno != null) {
				Class<?> returnType = method.getReturnType();
				if (returnType != null && (Integer.class.isAssignableFrom(returnType) || int.class.equals(returnType))) {
					method.setAccessible(true);
					validateMethod = method;
				}
			}

		}
		if (changeMethod == null || validateMethod == null) {
			return;
		}

		AnnotatedEntryMaidChanger entryMaidChanger = new AnnotatedEntryMaidChanger(changeMethod, validateMethod,
				targetInstance);
		maidChangeManager.registMaidChanger(entryMaidChanger);
	}
}