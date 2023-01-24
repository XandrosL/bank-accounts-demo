package com.example.bankaccounts.jpa;

import com.example.bankaccounts.jpa.entity.Cuenta.TipoCuenta;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class TipoCuentaConverter implements AttributeConverter<TipoCuenta, String> {

	@Override
	public String convertToDatabaseColumn(TipoCuenta tipoCuenta) {
		if (tipoCuenta == null) {
			return null;
		}
		return tipoCuenta.name();
	}

	@Override
	public TipoCuenta convertToEntityAttribute(String dbData) {
		if (dbData == null) {
			return null;
		}
		return Enum.valueOf(TipoCuenta.class, dbData);
	}
}