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
import com.example.bankaccounts.exception.ForbiddenOperationException;
import com.example.bankaccounts.exception.ResourceNotFoundException;
import com.example.bankaccounts.jpa.entity.Cuenta;
import com.example.bankaccounts.jpa.repository.CuentaRepository;
import com.example.bankaccounts.utils.BankAccountsUtils;

@Service
public class CuentaService {

	private static final String CUENTA_INVALIDA = "Cuenta inválida";
	private static final String CUENTA_NO_ENCONTRADA_O_ID_INVALIDO = "Cuenta no encontrada o ID inválido";
	private static final String CUENTA_NO_ENCONTRADA_O_NUMERO_DE_CUENTA_INVALIDO = "Cuenta no encontrada o numero de cuenta inválido";
	private static final String NO_SE_PERMITE_ALTERAR_UNA_CUENTA_CON_MOVIMIENTOS = "No se permite alterar una cuenta con movimientos";
	private static final String CLIENTE_INVALIDO = "Cliente Inválido";
	private static final String CLIENTE_EN_ESTADO_INACTIVO = "Cliente en estado Inactivo";
	private static final String NO_FUE_POSIBLE_HALLAR_EL_CLIENTE_DE_LA_CUENTA = "No fue posible hallar el cliente de la cuenta";

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

	public Cuenta findByNumeroCuenta(Long numeroCuenta) {
		return repository.findByNumeroCuenta(numeroCuenta)
				.orElseThrow(() -> new ResourceNotFoundException(CUENTA_NO_ENCONTRADA_O_NUMERO_DE_CUENTA_INVALIDO));
	}

	public Cuenta create(Cuenta cuenta) {
		if (cuenta == null) {
			throw new IllegalArgumentException(CUENTA_INVALIDA);
		}
		setDefaultValues(cuenta);
		validateClienteEstado(cuenta);
		return repository.save(cuenta);
	}

	public Cuenta replaceById(Long cuentaId, Cuenta cuenta) {
		if (cuenta == null) {
			throw new IllegalArgumentException(CUENTA_INVALIDA);
		}
		Cuenta oldCuenta = findById(cuentaId);
		validateExistingMovimientos(oldCuenta);
		setDefaultValues(cuenta, oldCuenta.getCuentaId());
		validateClienteEstado(cuenta);
		return repository.save(cuenta);
	}

	public Cuenta replaceByNumeroCuenta(Long numeroCuenta, Cuenta cuenta) {
		if (cuenta == null) {
			throw new IllegalArgumentException(CUENTA_INVALIDA);
		}
		Cuenta oldCuenta = findByNumeroCuenta(numeroCuenta);
		validateExistingMovimientos(oldCuenta);
		setDefaultValues(cuenta, oldCuenta.getCuentaId());
		validateClienteEstado(cuenta);
		return repository.save(cuenta);
	}

	public Cuenta modifyById(Long cuentaId, Cuenta cuenta) {
		if (cuenta == null) {
			throw new IllegalArgumentException(CUENTA_INVALIDA);
		}
		cuenta.setCuentaId(null);
		Cuenta oldCuenta = findById(cuentaId);
		validateExistingMovimientos(oldCuenta);
		BankAccountsUtils.copyNonNullValues(cuenta, oldCuenta);
		validateClienteEstado(oldCuenta);
		return repository.save(oldCuenta);
	}

	public Cuenta modifyByNumeroCuenta(Long numeroCuenta, Cuenta cuenta) {
		if (cuenta == null) {
			throw new IllegalArgumentException(CUENTA_INVALIDA);
		}
		cuenta.setCuentaId(null);
		Cuenta oldCuenta = findByNumeroCuenta(numeroCuenta);
		validateExistingMovimientos(oldCuenta);
		BankAccountsUtils.copyNonNullValues(cuenta, oldCuenta);
		validateClienteEstado(oldCuenta);
		return repository.save(oldCuenta);
	}

	public void deleteById(Long cuentaId) {
		validateExistingMovimientos(findById(cuentaId));
		repository.deleteById(cuentaId);
	}

	public void deleteByNumeroCuenta(Long numeroCuenta) {
		validateExistingMovimientos(findByNumeroCuenta(numeroCuenta));
		repository.deleteByNumeroCuenta(numeroCuenta);
	}

	private void setDefaultValues(Cuenta cuenta) {
		setDefaultValues(cuenta, null);
	}

	private void setDefaultValues(Cuenta cuenta, Long cuentaId) {
		cuenta.setCuentaId(cuentaId);
		if (cuenta.getEstado() == null) {
			cuenta.setEstado(false);
		}
		if (cuenta.getSaldoInicial() == null) {
			cuenta.setSaldoInicial(0.0);
		}
	}

	private ClienteDTO getCliente(Cuenta cuenta) {
		URI clientesUri = UriComponentsBuilder.fromHttpUrl(clientesUrl).path("/{clienteId}")
				.buildAndExpand(cuenta.getClienteId()).toUri();
		ResponseEntity<ClienteDTO> response = restTemplate.getForEntity(clientesUri, ClienteDTO.class);
		if (response.getStatusCode().is2xxSuccessful()) {
			return response.getBody();
		} else {
			throw new RestClientException(NO_FUE_POSIBLE_HALLAR_EL_CLIENTE_DE_LA_CUENTA);
		}
	}

	private void validateExistingMovimientos(Cuenta oldCuenta) {
		if (oldCuenta.getMovimientos() != null && !oldCuenta.getMovimientos().isEmpty()) {
			throw new ForbiddenOperationException(NO_SE_PERMITE_ALTERAR_UNA_CUENTA_CON_MOVIMIENTOS);
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