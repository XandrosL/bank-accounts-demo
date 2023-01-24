package com.example.bankaccounts.jpa.entity;

import java.util.Arrays;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotNull;

@Entity
public class Cuenta {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@JsonProperty(index = 0)
	private Long cuentaId;

	@NotNull(message = "El campo ClienteId es obligatorio")
	@Column(nullable = false)
	private Long clienteId;
	// TODO Generar NumeroCuenta por secuencia de BD. JPA/Hibernate no poseen
	// funcionalidad nativa, se debe especifar el DDL
	@NotNull(message = "El campo NumeroCuenta es obligatorio")
	@Column(nullable = false, unique = true)
	private Integer numeroCuenta;
	@NotNull(message = "El campo TipoCuenta es obligatorio")
	@Column(nullable = false)
	private TipoCuenta tipoCuenta;
	@Column(nullable = false)
	private Double saldoInicial;
	@Column(nullable = false)
	private Boolean estado;

	@JsonIgnore
	@OneToMany(mappedBy = "cuentaId")
	private Set<Movimiento> movimientos;

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

	public Set<Movimiento> getMovimientos() {
		return movimientos;
	}

	public void setMovimientos(Set<Movimiento> movimientos) {
		this.movimientos = movimientos;
	}
}