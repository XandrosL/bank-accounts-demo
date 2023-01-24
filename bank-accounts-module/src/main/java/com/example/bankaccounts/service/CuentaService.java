package com.example.bankaccounts.service;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.example.bankaccounts.dto.ClienteDTO;
import com.example.bankaccounts.exception.ResourceNotFoundException;
import com.example.bankaccounts.jpa.entity.Cuenta;
import com.example.bankaccounts.jpa.repository.CuentaRepository;
import com.example.bankaccounts.utils.BankAccountsUtils;

@Service
public class CuentaService {

	private static final String CUENTA_INVALIDA = "Cuenta inválida";
	private static final String CUENTA_NO_ENCONTRADA_O_ID_INVALIDO = "Cuenta no encontrada o ID inválido";
	private static final String CUENTA_NO_ENCONTRADA_O_NUMERO_DE_CUENTA_INVALIDO = "Cuenta no encontrada o numero de cuenta inválido";
	private static final String CLIENTE_INVALIDO = "Cliente Inválido";
	private static final String CLIENTE_EN_ESTADO_INACTIVO = "Cliente en estado Inactivo";

	@Autowired
	private CuentaRepository repository;

	@Autowired
	private RestTemplate restTemplate;

	@Value("${users-module.clients.url}")
	private String clientesUrl;

	public Page<Cuenta> findAll(int page, int size) {
		return findAll(page, size, Sort.unsorted());
	}

	public Page<Cuenta> findAll(int page, int size, Sort sort) {
		return repository.findAll(PageRequest.of(page, size, sort));
	}

	public Iterable<Cuenta> findAllByClienteId(Long clienteId) {
		return repository.findAllByClienteId(clienteId);
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
		validateClienteEstado(cuenta);
		return repository.save(cuenta);
	}

	public Cuenta replaceById(Long cuentaId, Cuenta cuenta) {
		if (cuenta == null) {
			throw new IllegalArgumentException(CUENTA_INVALIDA);
		}
		cuenta.setCuentaId(findById(cuentaId).getCuentaId());
		validateClienteEstado(cuenta);
		return repository.save(cuenta);
	}

	public Cuenta replaceByNumeroCuenta(Integer numeroCuenta, Cuenta cuenta) {
		if (cuenta == null) {
			throw new IllegalArgumentException(CUENTA_INVALIDA);
		}
		cuenta.setCuentaId(findByNumeroCuenta(numeroCuenta).getCuentaId());
		validateClienteEstado(cuenta);
		return repository.save(cuenta);
	}

	public Cuenta modifyById(Long cuentaId, Cuenta cuenta) {
		if (cuenta == null) {
			throw new IllegalArgumentException(CUENTA_INVALIDA);
		}
		cuenta.setCuentaId(null);
		Cuenta oldCuenta = findById(cuentaId);
		BankAccountsUtils.copyNonNullValues(cuenta, oldCuenta);
		validateClienteEstado(oldCuenta);
		return repository.save(oldCuenta);
	}

	public Cuenta modifyByNumeroCuenta(Integer numeroCuenta, Cuenta cuenta) {
		if (cuenta == null) {
			throw new IllegalArgumentException(CUENTA_INVALIDA);
		}
		cuenta.setCuentaId(null);
		Cuenta oldCuenta = findByNumeroCuenta(numeroCuenta);
		BankAccountsUtils.copyNonNullValues(cuenta, oldCuenta);
		validateClienteEstado(oldCuenta);
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

	private ClienteDTO getCliente(Cuenta cuenta) {
		URI clientesUri = UriComponentsBuilder.fromHttpUrl(clientesUrl).path("/{clienteId}")
				.buildAndExpand(cuenta.getClienteId()).toUri();
		ResponseEntity<ClienteDTO> response = restTemplate.getForEntity(clientesUri, ClienteDTO.class);
		if (response.getStatusCode().is2xxSuccessful()) {
			return response.getBody();
		} else {
			throw new RestClientException("No fue posible hallar el cliente");
		}
	}

	private void validateClienteEstado(Cuenta cuenta) {
		ClienteDTO cliente = getCliente(cuenta);
		if (cliente == null || cliente.getClienteId() <= 0) {
			throw new IllegalArgumentException(CLIENTE_INVALIDO);
		} else if (Boolean.FALSE.equals(cliente.getEstado())) {
			throw new IllegalArgumentException(CLIENTE_EN_ESTADO_INACTIVO);
		}
	}
}