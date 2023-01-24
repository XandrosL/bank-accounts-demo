package com.example.users.jpa.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.users.jpa.entity.Cliente;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {

	public Optional<Cliente> findByIdentificacion(Long identificacion);

	public boolean existsByIdentificacion(Long identificacion);

	public void deleteByIdentificacion(Long identificacion);
}