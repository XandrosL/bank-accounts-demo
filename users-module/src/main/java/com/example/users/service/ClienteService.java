package com.example.users.service;

import java.net.URI;
import java.util.Arrays;
import java.util.List;

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

import com.example.users.dto.CuentaDTO;
import com.example.users.exception.ForbiddenOperationException;
import com.example.users.exception.ResourceNotFoundException;
import com.example.users.jpa.entity.Cliente;
import com.example.users.jpa.repository.ClienteRepository;
import com.example.users.util.UsersUtils;

@Service
public class ClienteService {

	private static final String CLIENTE_INVALIDO = "Cliente inválido";
	private static final String CLIENTE_NO_ENCONTRADO_O_ID_INVALIDO = "Cliente no encontrado o ID inválido";
	private static final String CLIENTE_NO_ENCONTRADO_O_IDENTIFICACION_INVALIDA = "Cliente no encontrado o identificación inválida";
	private static final String NO_FUE_POSIBLE_HALLAR_LAS_CUENTAS_DEL_CLIENTE = "No fue posible hallar las cuentas del cliente";
	private static final String NO_SE_PERMITE_ELIMINAR_UN_CLIENTE_CON_CUENTAS = "No se permite eliminar un cliente con cuentas";

	@Autowired
	private ClienteRepository repository;

	@Autowired
	private RestTemplate restTemplate;

	@Value("${bank-accounts-module.bankaccounts.url}")
	private String cuentasUrl;

	public Page<Cliente> findAll(int page, int size) {
		return findAll(page, size, Sort.unsorted());
	}

	public Page<Cliente> findAll(int page, int size, Sort sort) {
		return repository.findAll(PageRequest.of(page, size, sort));
	}

	public Cliente findById(Long clienteId) {
		return repository.findById(clienteId)
				.orElseThrow(() -> new ResourceNotFoundException(CLIENTE_NO_ENCONTRADO_O_ID_INVALIDO));
	}

	public Cliente findByIdentificacion(Long identificacion) {
		return repository.findByIdentificacion(identificacion)
				.orElseThrow(() -> new ResourceNotFoundException(CLIENTE_NO_ENCONTRADO_O_IDENTIFICACION_INVALIDA));
	}

	public Cliente create(Cliente cliente) {
		if (cliente == null) {
			throw new IllegalArgumentException(CLIENTE_INVALIDO);
		}
		setDefaultValues(cliente);
		return repository.save(cliente);
	}

	public Cliente replaceById(Long clienteId, Cliente cliente) {
		if (cliente == null) {
			throw new IllegalArgumentException(CLIENTE_INVALIDO);
		}
		setDefaultValues(cliente, findById(clienteId).getClienteId());
		return repository.save(cliente);
	}

	public Cliente replaceByIdentificacion(Long identificacion, Cliente cliente) {
		if (cliente == null) {
			throw new IllegalArgumentException(CLIENTE_INVALIDO);
		}
		setDefaultValues(cliente, findByIdentificacion(identificacion).getClienteId());
		return repository.save(cliente);
	}

	public Cliente modifyById(Long clienteId, Cliente cliente) {
		if (cliente == null) {
			throw new IllegalArgumentException(CLIENTE_INVALIDO);
		}
		cliente.setClienteId(null);
		Cliente oldCliente = findById(clienteId);
		UsersUtils.copyNonNullValues(cliente, oldCliente);
		return repository.save(oldCliente);
	}

	public Cliente modifyByIdentificacion(Long identificacion, Cliente cliente) {
		if (cliente == null) {
			throw new IllegalArgumentException(CLIENTE_INVALIDO);
		}
		cliente.setClienteId(null);
		Cliente oldCliente = findByIdentificacion(identificacion);
		UsersUtils.copyNonNullValues(cliente, oldCliente);
		return repository.save(oldCliente);
	}

	public void deleteById(Long clienteId) {
		validateExistingCuentas(getCuentas(findById(clienteId)));
		repository.deleteById(clienteId);
	}

	public void deleteByIdentificacion(Long identificacion) {
		validateExistingCuentas(getCuentas(findByIdentificacion(identificacion)));
		repository.deleteByIdentificacion(identificacion);
	}

	private void setDefaultValues(Cliente cliente) {
		setDefaultValues(cliente, null);
	}

	private void setDefaultValues(Cliente cliente, Long clienteId) {
		cliente.setClienteId(clienteId);
		if (cliente.getEstado() == null) {
			cliente.setEstado(true);
		}
	}

	private List<CuentaDTO> getCuentas(Cliente cliente) {
		URI cuentasUri = UriComponentsBuilder.fromHttpUrl(cuentasUrl).path("/cliente/{clienteId}")
				.buildAndExpand(cliente.getClienteId()).toUri();
		ResponseEntity<CuentaDTO[]> response = restTemplate.getForEntity(cuentasUri, CuentaDTO[].class);
		if (response.getStatusCode().is2xxSuccessful()) {
			return Arrays.asList(response.getBody());
		} else {
			throw new RestClientException(NO_FUE_POSIBLE_HALLAR_LAS_CUENTAS_DEL_CLIENTE);
		}
	}

	private void validateExistingCuentas(List<CuentaDTO> cuentas) {
		if (cuentas != null && !cuentas.isEmpty()) {
			throw new ForbiddenOperationException(NO_SE_PERMITE_ELIMINAR_UN_CLIENTE_CON_CUENTAS);
		}
	}
}