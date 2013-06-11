package org.lo.d.minecraft.littlemaid;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import net.minecraftforge.common.Configuration;

import org.lo.d.commons.reflections.ReflectionSupport;

abstract class AbstractConfiguratedIdInjector<A extends Annotation> implements ReflectionSupport.Worker {
	private final String category;

	private final String keyPrefix;

	private final Configuration config;
	private final Class<? extends Annotation> typeAnnotationClass;
	private final Class<A> fieldAnnotationClass;

	protected AbstractConfiguratedIdInjector(String category, String keyPrefix, Configuration config,
			Class<? extends Annotation> typeAnnotationClass, Class<A> fieldAnnotationClass) {
		this.category = category;
		this.keyPrefix = keyPrefix;
		this.config = config;
		this.typeAnnotationClass = typeAnnotationClass;
		this.fieldAnnotationClass = fieldAnnotationClass;
	}

	@Override
	public void work(Class<?> cls) {
		Annotation configAnno = cls.getAnnotation(typeAnnotationClass);
		if (configAnno == null) {
			return;
		}

		for (Field field : cls.getFields()) {
			A idAnno = field.getAnnotation(fieldAnnotationClass);
			if (idAnno == null) {
				continue;
			}
			int modifier = field.getModifiers();
			if (Modifier.isFinal(modifier) || !Modifier.isStatic(modifier)) {
				continue;
			}

			Class<?> fieldType = field.getType();
			if (!int.class.isAssignableFrom(fieldType) && !Integer.class.isAssignableFrom(fieldType)) {
				continue;
			}

			field.setAccessible(true);
			try {
				int defaultValue = (int) field.get(null);

				String key = keyPrefix + "." + getKeyName(idAnno);
				int value = config.get(category, key, defaultValue, "defaultId = " + defaultValue).getInt();
				workToValue(idAnno, key, value, defaultValue, field);

			} catch (IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			}

		}
	}

	protected abstract String getKeyName(A annotation);

	protected void workToValue(A annotation, String key, int value, int defaultValue, Field field)
			throws IllegalArgumentException, IllegalAccessException {
		field.set(null, value);
	}
}