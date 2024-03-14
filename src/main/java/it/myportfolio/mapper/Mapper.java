package it.myportfolio.mapper;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.springframework.beans.BeanUtils;

public class Mapper {


	public static <Entity, DTO> Entity toEntity(Class<Entity> entityClass, DTO dto) {
		Entity entity = null;
		try {
			Constructor<Entity> constructor = entityClass.getConstructor();
			entity = constructor.newInstance();
			BeanUtils.copyProperties(dto, entity);
		} catch (InstantiationException | IllegalAccessException | NoSuchMethodException
				| InvocationTargetException e) {
			e.printStackTrace(); 
		}
		return entity;
	}

	public static <Entity, DTO> DTO toDTO(Class<DTO> dtoClass, Entity entity) {
		DTO dto = null;
		try {
			Constructor<DTO> constructor = dtoClass.getConstructor();
			dto = constructor.newInstance();
			BeanUtils.copyProperties(entity, dto);
		} catch (InstantiationException | IllegalAccessException | NoSuchMethodException
				| InvocationTargetException e) {
			e.printStackTrace(); 
		}
		return dto;
	}
}