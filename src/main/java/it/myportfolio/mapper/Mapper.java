package it.myportfolio.mapper;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;


import org.springframework.beans.BeanUtils;

public class Mapper {
	
    //funzione generica che  restitiusce l'entità (model) dato in input un DTO
	//Class<Entity> entityClass -> tipo di oggetto che vogliano in output
	//DTO dto oggetto dto da trasformare in entità
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

	//funzione generica che restriusce un DTO dato in input un'entità (model)
	//Class<DTO> dtoClass -> tipo di oggetto che vogliano in output
	//Entity entity entità (model) da trasformare in DTO
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

//	//funzione generica che restriusce una List DTO dato in input  un iterable di entità (model)
//	//Class<DTO> dtoClass -> tipo di oggetto che vogliano in output
//	//Iterable<Entity> entities iterable di entità (model) da trasformare in una  List di DTO
//	public static <Entity, DTO> List<DTO> toDTOList(Class<DTO> dtoClass, Iterable<Entity> entities) {
//		List<DTO> dtos = new ArrayList<>();
//		for (Entity entity : entities) {
//			DTO dto = toDTO(dtoClass, entity);
//			if (dto != null) {
//				dtos.add(dto);
//			}
//		}
//		return dtos;
//	}
}