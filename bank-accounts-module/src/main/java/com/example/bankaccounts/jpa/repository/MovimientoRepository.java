package com.example.bankaccounts.jpa.repository;

import java.sql.Date;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.bankaccounts.jpa.entity.Movimiento;

public interface MovimientoRepository extends JpaRepository<Movimiento, Long> {

	public Page<Movimiento> findAllByCuentaId(Long cuentaId, Pageable pageable);

	public Iterable<Movimiento> findAllByCuentaIdAndByFechaBetween(Long cuentaId, Date startDate, Date endDate);

	public boolean existsByCuentaId(Long cuentaId);

	public void deleteAllByCuentaId(Long cuentaId);

	public Optional<Movimiento> findFirstByCuentaId(Long cuentaId, Sort sort);

	public Optional<Movimiento> findFirstByCuentaIdAndMovimientoIdNot(Long cuentaId, Long movimientoId, Sort sort);
}