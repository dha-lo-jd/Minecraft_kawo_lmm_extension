package org.lo.d.minecraft.littlemaid;

import java.lang.reflect.Constructor;

import net.minecraft.src.LMM_EntityLittleMaid;

import org.lo.d.commons.bytecode.ExtendedClassSupport;
import org.lo.d.commons.bytecode.ExtendedClassSupport.GenerateClassName;
import org.lo.d.minecraft.littlemaid.LMMExMaidChangeManager.LMMExChanger;
import org.lo.d.minecraft.littlemaid.entity.BaseEntityLittleMaidEx;
import org.lo.d.minecraft.littlemaid.entity.EntityLittleMaidEx;

@LMMExChanger
public class ClassGenerateMaidChanger {

	@LMMExChanger.Instance
	private static final ClassGenerateMaidChanger instance = new ClassGenerateMaidChanger();

	@LMMExChanger.Change
	private LMM_EntityLittleMaid change(LMM_EntityLittleMaid maid) {
		Class<?> maidClass = maid.getClass();
		final GenerateClassName generateClassName = new ExtendedClassSupport.GenerateClassName(
				BaseEntityLittleMaidEx.class,
				LMM_EntityLittleMaid.class,
				maidClass, EntityLittleMaidEx.class);
		Class<?> n;
		try {
			n =
					Class.forName(generateClassName.getNewGenarateClassName().getFqn().getName());
			Constructor<?> constructor = n.getConstructor(new Class<?>[] { LMM_EntityLittleMaid.class });
			LMM_EntityLittleMaid newMaid = (LMM_EntityLittleMaid) constructor.newInstance(maid);
			return newMaid;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return maid;
	}

	@LMMExChanger.Validate
	private int validate(LMM_EntityLittleMaid maid) {
		Class<?> maidClass = maid.getClass();
		final GenerateClassName generateClassName = new ExtendedClassSupport.GenerateClassName(
				BaseEntityLittleMaidEx.class,
				LMM_EntityLittleMaid.class,
				maidClass, EntityLittleMaidEx.class);
		try {
			Class.forName(generateClassName.getNewGenarateClassName().getFqn().getName());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return 0;
		}

		return maid.getClass() != LMM_EntityLittleMaid.class && maid instanceof LMM_EntityLittleMaid ? 2 : 0;
	}

}
