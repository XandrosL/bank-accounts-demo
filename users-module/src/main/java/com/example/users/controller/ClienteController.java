package com.example.users.controller;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.example.users.jpa.entity.Cliente;
import com.example.users.service.ClienteService;

import jakarta.validation.Valid;

@RestController
@RequestMapping(path = "/clientes")
public class ClienteController {

	@Autowired
	private ClienteService service;

	@GetMapping
	public ResponseEntity<Iterable<Cliente>> getClientes(@RequestParam(defaultValue = "0") Integer page,
			@RequestParam(defaultValue = "20") Integer size, @RequestParam(defaultValue = "false") Boolean details) {

		Page<Cliente> clientes = service.findAll(page, size);
		return (Boolean.TRUE.equals(details) ? new ResponseEntity<>(clientes, HttpStatus.OK)
				: new ResponseEntity<>(clientes.getContent(), HttpStatus.OK));
	}

	@GetMapping("/{clienteId}")
	public ResponseEntity<Cliente> getClienteById(@PathVariable("clienteId") Long clienteId) {
		return new ResponseEntity<>(service.findById(clienteId), HttpStatus.OK);
	}

	@GetMapping("/identificacion/{identificacion}")
	public ResponseEntity<Cliente> getClienteByIdentificacion(@PathVariable("identificacion") Integer identificacion) {
		return new ResponseEntity<>(service.findByIdentificacion(identificacion), HttpStatus.OK);
	}

	@PostMapping
	public ResponseEntity<Cliente> postCliente(@Valid @RequestBody(required = false) Cliente cliente) {
		Cliente createdCliente = service.create(cliente);
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{clienteId}")
				.buildAndExpand(createdCliente.getClienteId()).toUri();
		return ResponseEntity.created(location).body(createdCliente);
	}

	@PutMapping("/{clienteId}")
	public ResponseEntity<Cliente> putClienteById(@PathVariable("clienteId") Long clienteId,
			@Valid @RequestBody Cliente cliente) {
		return new ResponseEntity<>(service.replaceById(clienteId, cliente), HttpStatus.OK);
	}

	@PutMapping("/identificacion/{identificacion}")
	public ResponseEntity<Cliente> putClienteByIdentificacion(@PathVariable("identificacion") Integer clienteId,
			@Valid @RequestBody Cliente cliente) {
		return new ResponseEntity<>(service.replaceByIdentificacion(clienteId, cliente), HttpStatus.OK);
	}

	@PatchMapping("/{clienteId}")
	public ResponseEntity<Cliente> patchClienteById(@PathVariable("clienteId") Long clienteId,
			@RequestBody Cliente cliente) {
		return new ResponseEntity<>(service.modifyById(clienteId, cliente), HttpStatus.OK);
	}

	@PatchMapping("/identificacion/{identificacion}")
	public ResponseEntity<Cliente> patchClienteByIdentificacion(@PathVariable("identificacion") Integer identificacion,
			@RequestBody Cliente cliente) {
		return new ResponseEntity<>(service.modifyByIdentificacion(identificacion, cliente), HttpStatus.OK);
	}

	@DeleteMapping("/{clienteId}")
	public ResponseEntity<Cliente> deleteClienteById(@PathVariable("clienteId") Long clienteId) {
		service.deleteById(clienteId);
		return ResponseEntity.noContent().build();
	}

	@DeleteMapping("/identificacion/{identificacion}")
	public ResponseEntity<Cliente> deleteClienteByIdentificacion(
			@PathVariable("identificacion") Integer identificacion) {
		service.deleteByIdentificacion(identificacion);
		return ResponseEntity.noContent().build();
	}
}