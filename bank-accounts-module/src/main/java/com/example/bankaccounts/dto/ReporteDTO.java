package com.example.bankaccounts.dto;

import java.io.Serializable;
import java.sql.Date;

public class ReporteDTO implements Serializable {

	private static final long serialVersionUID = -3467686759003572525L;

	private Date fecha;
	private String cliente;
	private Long numeroCuenta;
	private String tipo;
	private Double saldoInicial;
	private Boolean estado;
	private Double movimiento;
	private Double saldoDisponible;

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public String getCliente() {
		return cliente;
	}

	public void setCliente(String cliente) {
		this.cliente = cliente;
	}

	public Long getNumeroCuenta() {
		return numeroCuenta;
	}

	public void setNumeroCuenta(Long numeroCuenta) {
		this.numeroCuenta = numeroCuenta;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
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

	public Double getMovimiento() {
		return movimiento;
	}

	public void setMovimiento(Double movimiento) {
		this.movimiento = movimiento;
	}

	public Double getSaldoDisponible() {
		return saldoDisponible;
	}

	public void setSaldoDisponible(Double saldoDisponible) {
		this.saldoDisponible = saldoDisponible;
	}
}