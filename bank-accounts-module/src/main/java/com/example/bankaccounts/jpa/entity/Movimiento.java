package com.example.bankaccounts.jpa.entity;

import java.sql.Date;
import java.util.Arrays;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;

@Entity
public class Movimiento {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@JsonProperty(index = 0)
	private Long movimientoId;

	@JsonIgnore
	@JoinColumn(name = "cuenta_id", insertable = false, updatable = false)
	@ManyToOne(targetEntity = Cuenta.class)
	private Cuenta cuenta;

	@NotNull(message = "El campo CuentaId es obligatorio")
	@Column(name = "cuenta_id", nullable = false)
	private Long cuentaId;

	@Column(nullable = false)
	private Date fecha;
	@NotNull(message = "El campo TipoMovimiento es obligatorio")
	@Column(nullable = false)
	private TipoMovimiento tipoMovimiento;
	@NotNull(message = "El campo Valor es obligatorio")
	@Column(nullable = false)
	private Double valor;
	@Column(nullable = false)
	private Double saldo;

	public enum TipoMovimiento {
		D("Deposito"), R("Retiro");

		private final String descripcion;

		TipoMovimiento(String descripcion) {
			this.descripcion = descripcion;
		}

		public String getDescripcion() {
			return descripcion;
		}

		public static TipoMovimiento getByDescripcion(String descripcion) {
			return Arrays.stream(values()).filter(value -> value.descripcion.equals(descripcion)).findFirst()
					.orElseThrow(() -> new IllegalArgumentException("Tipo de movimiento no válido"));
		}
	}

	public Long getMovimientoId() {
		return movimientoId;
	}

	public void setMovimientoId(Long movimientoId) {
		this.movimientoId = movimientoId;
	}

	public Cuenta getCuenta() {
		return cuenta;
	}

	public void setCuenta(Cuenta cuenta) {
		this.cuenta = cuenta;
	}

	public Long getCuentaId() {
		return cuentaId;
	}

	public void setCuentaId(Long cuentaId) {
		this.cuentaId = cuentaId;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public TipoMovimiento getTipoMovimiento() {
		return tipoMovimiento;
	}

	public void setTipoMovimiento(TipoMovimiento tipoMovimiento) {
		this.tipoMovimiento = tipoMovimiento;
	}

	public Double getValor() {
		return valor;
	}

	public void setValor(Double valor) {
		this.valor = valor;
	}

	public Double getSaldo() {
		return saldo;
	}

	public void setSaldo(Double saldo) {
		this.saldo = saldo;
	}
}