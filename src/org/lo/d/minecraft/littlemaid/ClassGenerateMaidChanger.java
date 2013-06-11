package org.lo.d.minecraft.littlemaid;

import java.lang.reflect.Constructor;

import net.minecraft.src.LMM_EntityLittleMaid;

import org.lo.d.commons.bytecode.ExtendedClassSupport;
import org.lo.d.commons.bytecode.ExtendedClassSupport.GenerateClassName;
import org.lo.d.minecraft.littlemaid.entity.AntiEntityLittleMaidEx;
import org.lo.d.minecraft.littlemaid.entity.BaseEntityLittleMaidEx;
import org.lo.d.minecraft.littlemaid.entity.EntityLittleMaidEx;

@LMMExChanger
public class ClassGenerateMaidChanger {

	@LMMExChanger.Instance
	public static final ClassGenerateMaidChanger instance = new ClassGenerateMaidChanger();

	public boolean canGenarate(Class<?> maidClass) {
		if (LMM_EntityLittleMaid.class == maidClass || BaseEntityLittleMaidEx.class == maidClass
				|| !LMM_EntityLittleMaid.class.isAssignableFrom(maidClass)) {
			return false;
		}
		if (EntityLittleMaidEx.class.isAssignableFrom(maidClass)) {
			return false;
		}
		AntiEntityLittleMaidEx antiAnno = maidClass.getAnnotation(AntiEntityLittleMaidEx.class);
		if (antiAnno != null) {
			return false;
		}
		return true;
	}

	@LMMExChanger.Change
	private LMM_EntityLittleMaid change(LMM_EntityLittleMaid maid) {
		Class<?> maidClass = maid.getClass();
		final GenerateClassName generateClassName = getGenerateMaidClassName(maidClass);
		Class<?> n;
		try {
			n = Class.forName(generateClassName.getNewGenarateClassName().getFqn().getName());
			Constructor<?> constructor = n.getConstructor(new Class<?>[] { LMM_EntityLittleMaid.class });
			LMM_EntityLittleMaid newMaid = (LMM_EntityLittleMaid) constructor.newInstance(maid);
			return newMaid;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return maid;
	}

	private GenerateClassName getGenerateMaidClassName(Class<?> maidClass) {
		final GenerateClassName generateClassName = new ExtendedClassSupport.GenerateClassName(
				BaseEntityLittleMaidEx.class, LMM_EntityLittleMaid.class, maidClass, EntityLittleMaidEx.class);
		return generateClassName;
	}

	@LMMExChanger.Validate
	private int validate(LMM_EntityLittleMaid maid) {
		Class<?> maidClass = maid.getClass();
		if (canGenarate(maidClass)) {
			return 0;
		}

		final GenerateClassName generateClassName = getGenerateMaidClassName(maidClass);
		try {
			Class.forName(generateClassName.getNewGenarateClassName().getFqn().getName());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return 0;
		}
		return 20;
	}

}
