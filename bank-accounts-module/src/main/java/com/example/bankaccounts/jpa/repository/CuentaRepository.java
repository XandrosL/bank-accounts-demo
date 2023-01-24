package com.example.bankaccounts.jpa.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.bankaccounts.jpa.entity.Cuenta;

public interface CuentaRepository extends JpaRepository<Cuenta, Long> {

	public Iterable<Cuenta> findAllByClienteId(Long clienteId);

	public Optional<Cuenta> findByNumeroCuenta(Long numeroCuenta);

	public boolean existsByNumeroCuenta(Long numeroCuenta);

	public void deleteByNumeroCuenta(Long numeroCuenta);
}