package org.lo.d.minecraft.littlemaid;

import net.minecraft.entity.Entity;
import net.minecraft.src.LMM_EntityLittleMaid;
import net.minecraftforge.common.Configuration;

import org.lo.d.commons.bytecode.ExtendedClassSupport;
import org.lo.d.commons.bytecode.ExtendedClassSupport.GenerateClassName;
import org.lo.d.commons.reflections.ReflectionSupport;
import org.lo.d.minecraft.littlemaid.entity.BaseEntityLittleMaidEx;
import org.lo.d.minecraft.littlemaid.entity.EntityLittleMaidEx;

import com.google.common.collect.Lists;

import cpw.mods.fml.common.registry.EntityRegistry;

public class GeneratedMaidEntityClassProvidor implements ReflectionSupport.Worker {

	private final Configuration config;

	private static final String CONFIG_CATEGORY_NAME = "generate classes";

	public GeneratedMaidEntityClassProvidor(Configuration config) {
		this.config = config;
		config.addCustomCategoryComment(CONFIG_CATEGORY_NAME, "メイドさんクラスを拡張したクラスを更に拡張したクラスを生成します");
	}

	@Override
	public void work(Class<?> cls) {

		try {
			if (!ClassGenerateMaidChanger.instance.canGenarate(cls)) {
				return;
			}

			@SuppressWarnings("unchecked")
			Class<? extends LMM_EntityLittleMaid> maidClass = (Class<? extends LMM_EntityLittleMaid>) cls;

			GenerateClassName generateClassName = new ExtendedClassSupport.GenerateClassName(
					BaseEntityLittleMaidEx.class, LMM_EntityLittleMaid.class, maidClass, EntityLittleMaidEx.class);
			boolean genarate = config.get(CONFIG_CATEGORY_NAME,
					"generate." + generateClassName.getNewSuperClassName().getFqn().getName(), true,
					generateClassName.getNewGenarateClassName().getPath().getName()).getBoolean(true);
			if (!genarate) {
				return;
			}

			Class<?> c = ExtendedClassSupport.loadAndGenerateNewExtendedClass(BaseEntityLittleMaidEx.class,
					LMM_EntityLittleMaid.class, maidClass, EntityLittleMaidEx.class, Lists.<Class<?>> newArrayList());
			@SuppressWarnings("unchecked")
			Class<? extends Entity> genClass = (Class<? extends Entity>) c;
			System.out.println();
			System.out.println();
			System.out.println();
			System.out.println();
			System.out.println();
			System.out.println(c);
			System.out.println();
			System.out.println();
			System.out.println();
			System.out.println();
			System.out.println();
			int entityId = EntityRegistry.findGlobalUniqueEntityId();
			EntityRegistry.registerModEntity(genClass, genClass.getSimpleName(), entityId, LMMExtension.instance, 80,
					3, true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}