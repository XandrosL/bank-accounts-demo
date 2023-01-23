package com.example.users.jpa.entity;

import java.util.Arrays;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.validation.constraints.NotBlank;

@MappedSuperclass
public class Persona {
	@NotBlank(message = "El campo Nombre es obligatorio")
	@Column(nullable = false)
	private String nombre;
	@NotBlank(message = "El campo Genero es obligatorio")
	@Column(nullable = false)
	private Genero genero;
	@NotBlank(message = "El campo Edad es obligatorio")
	@Column(nullable = false)
	private Integer edad;
	@NotBlank(message = "El campo ContIdentificación es obligatorio")
	@Column(nullable = false)
	private Integer identificacion;
	@NotBlank(message = "El campo Dirección es obligatorio")
	@Column(nullable = false)
	private String direccion;
	@NotBlank(message = "El campo Telefono es obligatorio")
	@Column(nullable = false)
	private Integer telefono;

	public enum Genero {
		M("Masculino"), F("Femenino"), O("Otro");

		private final String descripcion;

		Genero(String descripcion) {
			this.descripcion = descripcion;
		}

		public String getDescripcion() {
			return descripcion;
		}

		public static Genero getByDescripcion(String descripcion) {
			return Arrays.stream(values()).filter(value -> value.descripcion.equals(descripcion)).findFirst()
					.orElseThrow(() -> new IllegalArgumentException("Género no válido"));
		}
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Genero getGenero() {
		return genero;
	}

	public void setGenero(Genero genero) {
		this.genero = genero;
	}

	public Integer getEdad() {
		return edad;
	}

	public void setEdad(Integer edad) {
		this.edad = edad;
	}

	public Integer getIdentificacion() {
		return identificacion;
	}

	public void setIdentificacion(Integer identificacion) {
		this.identificacion = identificacion;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public Integer getTelefono() {
		return telefono;
	}

	public void setTelefono(Integer telefono) {
		this.telefono = telefono;
	}
}