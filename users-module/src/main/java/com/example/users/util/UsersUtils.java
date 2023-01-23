package com.example.users.util;

import java.beans.PropertyDescriptor;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

public class UsersUtils {

	private UsersUtils() {
		throw new IllegalStateException("Utility class");
	}

	public static <T> void copyNonNullValues(T source, T target) {
		final Set<String> ignoredProperties = new HashSet<>();
		ignoredProperties.addAll(getPropertyNamesWithNullValue(source));
		BeanUtils.copyProperties(source, target, ignoredProperties.toArray(new String[] {}));
	}

	private static Set<String> getPropertyNamesWithNullValue(Object source) {
		final BeanWrapper sourceBeanWrapper = new BeanWrapperImpl(source);
		final PropertyDescriptor[] propertyDescriptors = sourceBeanWrapper.getPropertyDescriptors();
		final Set<String> emptyNames = new HashSet<>();

		for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
			// Check if value of this property is null then add it to the collection
			Object propertyValue = sourceBeanWrapper.getPropertyValue(propertyDescriptor.getName());
			if (propertyValue != null) {
				continue;
			}
			emptyNames.add(propertyDescriptor.getName());
		}
		return emptyNames;
	}
}