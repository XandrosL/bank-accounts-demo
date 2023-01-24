package com.example.users.dto;

import java.io.Serializable;
import java.util.Arrays;

public class CuentaDTO implements Serializable {

	private static final long serialVersionUID = -6770028656245919558L;

	private Long cuentaId;
	private Long clienteId;
	private Integer numeroCuenta;
	private TipoCuenta tipoCuenta;
	private Double saldoInicial;
	private Boolean estado;

	public enum TipoCuenta {
		A("Ahorros"), C("Corriente");

		private final String descripcion;

		TipoCuenta(String descripcion) {
			this.descripcion = descripcion;
		}

		public String getDescripcion() {
			return descripcion;
		}

		public static TipoCuenta getByDescripcion(String descripcion) {
			return Arrays.stream(values()).filter(value -> value.descripcion.equals(descripcion)).findFirst()
					.orElseThrow(() -> new IllegalArgumentException("Tipo de cuenta no válido"));
		}
	}

	public Long getCuentaId() {
		return cuentaId;
	}

	public void setCuentaId(Long cuentaId) {
		this.cuentaId = cuentaId;
	}

	public Long getClienteId() {
		return clienteId;
	}

	public void setClienteId(Long clienteId) {
		this.clienteId = clienteId;
	}

	public Integer getNumeroCuenta() {
		return numeroCuenta;
	}

	public void setNumeroCuenta(Integer numeroCuenta) {
		this.numeroCuenta = numeroCuenta;
	}

	public TipoCuenta getTipoCuenta() {
		return tipoCuenta;
	}

	public void setTipoCuenta(TipoCuenta tipoCuenta) {
		this.tipoCuenta = tipoCuenta;
	}

	public Double getSaldoInicial() {
		return saldoInicial;
	}

	public void setSaldoInicial(Double saldoInicial) {
		this.saldoInicial = saldoInicial;
	}

	public Boolean getEstado() {
		return estado;
	}

	public void setEstado(Boolean estado) {
		this.estado = estado;
	}
}