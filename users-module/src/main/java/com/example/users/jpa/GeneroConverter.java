package com.example.users.jpa;

import com.example.users.jpa.entity.Persona.Genero;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class GeneroConverter implements AttributeConverter<Genero, String> {

	@Override
	public String convertToDatabaseColumn(Genero genero) {
		if (genero == null) {
			return null;
		}
		return genero.name();
	}

	@Override
	public Genero convertToEntityAttribute(String dbData) {
		if (dbData == null) {
			return null;
		}
		return Enum.valueOf(Genero.class, dbData);
	}
}