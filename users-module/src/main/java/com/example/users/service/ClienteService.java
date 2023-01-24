package com.example.users.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.users.exception.ResourceNotFoundException;
import com.example.users.jpa.entity.Cliente;
import com.example.users.jpa.repository.ClienteRepository;
import com.example.users.util.UsersUtils;

@Service
public class ClienteService {

	private static final String CLIENTE_NO_ENCONTRADO_O_ID_INVALIDO = "Cliente no encontrado o ID inválido";
	private static final String CLIENTE_NO_ENCONTRADO_O_IDENTIFICACION_INVALIDA = "Cliente no encontrado o identificación inválida";
	private static final String CLIENTE_INVALIDO = "Cliente inválido";

	@Autowired
	private ClienteRepository repository;

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

	public Cliente findByIdentificacion(Integer identificacion) {
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

	public Cliente replaceByIdentificacion(Integer identificacion, Cliente cliente) {
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

	public Cliente modifyByIdentificacion(Integer identificacion, Cliente cliente) {
		if (cliente == null) {
			throw new IllegalArgumentException(CLIENTE_INVALIDO);
		}
		cliente.setClienteId(null);
		Cliente oldCliente = findByIdentificacion(identificacion);
		UsersUtils.copyNonNullValues(cliente, oldCliente);
		return repository.save(oldCliente);
	}

	public void deleteById(Long clienteId) {
		if (!repository.existsById(clienteId)) {
			throw new ResourceNotFoundException(CLIENTE_NO_ENCONTRADO_O_ID_INVALIDO);
		}
		repository.deleteById(clienteId);
	}

	public void deleteByIdentificacion(Integer identificacion) {
		if (repository.existsByIdentificacion(identificacion)) {
			throw new ResourceNotFoundException(CLIENTE_NO_ENCONTRADO_O_IDENTIFICACION_INVALIDA);
		}
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
}