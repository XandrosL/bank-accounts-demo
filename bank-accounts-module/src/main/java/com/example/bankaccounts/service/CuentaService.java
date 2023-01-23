package com.example.bankaccounts.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.bankaccounts.exception.ResourceNotFoundException;
import com.example.bankaccounts.jpa.entity.Cuenta;
import com.example.bankaccounts.jpa.repository.CuentaRepository;
import com.example.bankaccounts.utils.BankAccountsUtils;

@Service
public class CuentaService {

	private static final String CUENTA_NO_ENCONTRADA_O_ID_INVALIDO = "Cuenta no encontrada o ID inválido";
	private static final String CUENTA_NO_ENCONTRADA_O_NUMERO_DE_CUENTA_INVALIDO = "Cuenta no encontrada o numero de cuenta inválido";
	private static final String CUENTA_INVALIDA = "Cuenta inválida";

	@Autowired
	private CuentaRepository repository;

	public Page<Cuenta> findAll(int page, int size) {
		return findAll(page, size, Sort.unsorted());
	}

	public Page<Cuenta> findAll(int page, int size, Sort sort) {
		return repository.findAll(PageRequest.of(page, size, sort));
	}

	public Cuenta findById(Long cuentaId) {
		return repository.findById(cuentaId)
				.orElseThrow(() -> new ResourceNotFoundException(CUENTA_NO_ENCONTRADA_O_ID_INVALIDO));
	}

	public Cuenta findByNumeroCuenta(Integer numeroCuenta) {
		return repository.findByNumeroCuenta(numeroCuenta)
				.orElseThrow(() -> new ResourceNotFoundException(CUENTA_NO_ENCONTRADA_O_NUMERO_DE_CUENTA_INVALIDO));
	}

	public Cuenta create(Cuenta cuenta) {
		if (cuenta == null) {
			throw new IllegalArgumentException(CUENTA_INVALIDA);
		}
		cuenta.setCuentaId(null);
		cuenta.setClienteId(null); // TODO
		return repository.save(cuenta);
	}

	public Cuenta replaceById(Long cuentaId, Cuenta cuenta) {
		if (cuenta == null) {
			throw new IllegalArgumentException(CUENTA_INVALIDA);
		}
		cuenta.setCuentaId(findById(cuentaId).getCuentaId());
		cuenta.setClienteId(null); // TODO
		return repository.save(cuenta);
	}

	public Cuenta replaceByNumeroCuenta(Integer numeroCuenta, Cuenta cuenta) {
		if (cuenta == null) {
			throw new IllegalArgumentException(CUENTA_INVALIDA);
		}
		cuenta.setCuentaId(findByNumeroCuenta(numeroCuenta).getCuentaId());
		cuenta.setClienteId(null); // TODO
		return repository.save(cuenta);
	}

	public Cuenta modifyById(Long cuentaId, Cuenta cuenta) {
		if (cuenta == null) {
			throw new IllegalArgumentException(CUENTA_INVALIDA);
		}
		cuenta.setCuentaId(null);
		cuenta.setClienteId(null); // TODO
		Cuenta oldCuenta = findById(cuentaId);
		BankAccountsUtils.copyNonNullValues(cuenta, oldCuenta);
		return repository.save(oldCuenta);
	}

	public Cuenta modifyByNumeroCuenta(Integer numeroCuenta, Cuenta cuenta) {
		if (cuenta == null) {
			throw new IllegalArgumentException(CUENTA_INVALIDA);
		}
		cuenta.setCuentaId(null);
		cuenta.setClienteId(null); // TODO
		Cuenta oldCuenta = findByNumeroCuenta(numeroCuenta);
		BankAccountsUtils.copyNonNullValues(cuenta, oldCuenta);
		return repository.save(oldCuenta);
	}

	public void deleteById(Long cuentaId) {
		if (!repository.existsById(cuentaId)) {
			throw new ResourceNotFoundException(CUENTA_NO_ENCONTRADA_O_ID_INVALIDO);
		}
		repository.deleteById(cuentaId);
	}

	public void deleteByNumeroCuenta(Integer numeroCuenta) {
		if (repository.existsByNumeroCuenta(numeroCuenta)) {
			throw new ResourceNotFoundException(CUENTA_NO_ENCONTRADA_O_NUMERO_DE_CUENTA_INVALIDO);
		}
		repository.deleteByNumeroCuenta(numeroCuenta);
	}
}