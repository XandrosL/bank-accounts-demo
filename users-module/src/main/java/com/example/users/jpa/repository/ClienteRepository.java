package com.example.users.jpa.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.users.jpa.entity.Cliente;

public interface ClienteRepository extends JpaRepository<Cliente, Integer> {

	public Optional<Cliente> findByIdentificacion(Integer identificacion);

	public boolean existsByIdentificacion(Integer identificacion);

	public void deleteByIdentificacion(Integer identificacion);
}