package com.example.bankaccounts.jpa;

import com.example.bankaccounts.jpa.entity.Movimiento.TipoMovimiento;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class TipoMovimientoConverter implements AttributeConverter<TipoMovimiento, String> {

	@Override
	public String convertToDatabaseColumn(TipoMovimiento tipoMovimiento) {
		if (tipoMovimiento == null) {
			return null;
		}
		return tipoMovimiento.name();
	}

	@Override
	public TipoMovimiento convertToEntityAttribute(String dbData) {
		if (dbData == null) {
			return null;
		}
		return Enum.valueOf(TipoMovimiento.class, dbData);
	}
}