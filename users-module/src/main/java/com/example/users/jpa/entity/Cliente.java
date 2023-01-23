package com.example.users.jpa.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;

@Entity
public class Cliente extends Persona {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@JsonProperty(index = 0)
	private Long clienteId;
	@NotBlank(message = "El campo Contraseña es obligatorio")
	@Column(nullable = false)
	private String contraseña;
	@Column(nullable = false)
	private Boolean estado;

	public Long getClienteId() {
		return clienteId;
	}

	public void setClienteId(Long clienteId) {
		this.clienteId = clienteId;
	}

	public String getContraseña() {
		return contraseña;
	}

	public void setContraseña(String contraseña) {
		this.contraseña = contraseña;
	}

	public Boolean getEstado() {
		return estado;
	}

	public void setEstado(Boolean estado) {
		this.estado = estado;
	}
}