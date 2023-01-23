package com.example.bankaccounts.controller;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.example.bankaccounts.jpa.entity.Cuenta;
import com.example.bankaccounts.service.CuentaService;

@Controller
@RequestMapping(path = "/cuentas")
public class CuentaController {

	@Autowired
	private CuentaService service;

	@GetMapping
	public ResponseEntity<Iterable<Cuenta>> getCuentas(@RequestParam(defaultValue = "0") Integer page,
			@RequestParam(defaultValue = "20") Integer size, @RequestParam(defaultValue = "false") Boolean details) {

		Page<Cuenta> cuentas = service.findAll(page, size);
		return (Boolean.TRUE.equals(details) ? new ResponseEntity<>(cuentas, HttpStatus.OK)
				: new ResponseEntity<>(cuentas.getContent(), HttpStatus.OK));
	}

	@GetMapping("/{cuentaId}")
	public ResponseEntity<Cuenta> getCuentaById(@PathVariable("cuentaId") Long cuentaId) {
		return new ResponseEntity<>(service.findById(cuentaId), HttpStatus.OK);
	}

	@GetMapping("/numero-cuenta/{numeroCuenta}")
	public ResponseEntity<Cuenta> getClienteByNumeroCuenta(@PathVariable("numeroCuenta") Integer numeroCuenta) {
		return new ResponseEntity<>(service.findByNumeroCuenta(numeroCuenta), HttpStatus.OK);
	}

	@PostMapping
	public ResponseEntity<Cuenta> postCuenta(@RequestBody Cuenta cuenta) {
		Cuenta createdCuenta = service.create(cuenta);
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{cuentaId}")
				.buildAndExpand(createdCuenta.getCuentaId()).toUri();
		return ResponseEntity.created(location).body(createdCuenta);
	}

	@PutMapping("/{cuentaId}")
	public ResponseEntity<Cuenta> putCuentaById(@PathVariable("cuentaId") Long cuentaId, @RequestBody Cuenta cuenta) {
		return new ResponseEntity<>(service.replaceById(cuentaId, cuenta), HttpStatus.OK);
	}

	@PutMapping("/numero-cuenta/{numeroCuenta}")
	public ResponseEntity<Cuenta> putCuentaByNumeroCuenta(@PathVariable("numeroCuenta") Integer numeroCuenta,
			@RequestBody Cuenta cuenta) {
		return new ResponseEntity<>(service.replaceByNumeroCuenta(numeroCuenta, cuenta), HttpStatus.OK);
	}

	@PatchMapping("/{cuentaId}")
	public ResponseEntity<Cuenta> patchCuentaById(@PathVariable("cuentaId") Long cuentaId, @RequestBody Cuenta cuenta) {
		return new ResponseEntity<>(service.modifyById(cuentaId, cuenta), HttpStatus.OK);
	}

	@PatchMapping("/numero-cuenta/{numeroCuenta}")
	public ResponseEntity<Cuenta> patchClienteByNumeroCuenta(@PathVariable("numeroCuenta") Integer numeroCuenta,
			@RequestBody Cuenta cuenta) {
		return new ResponseEntity<>(service.modifyByNumeroCuenta(numeroCuenta, cuenta), HttpStatus.OK);
	}

	@DeleteMapping("/{cuentaId}")
	public ResponseEntity<Cuenta> deleteCuentaById(@PathVariable("cuentaId") Long cuentaId) {
		service.deleteById(cuentaId);
		return ResponseEntity.noContent().build();
	}

	@DeleteMapping("/numero-cuenta/{numeroCuenta}")
	public ResponseEntity<Cuenta> deleteCuentaByNumeroCuenta(@PathVariable("numeroCuenta") Integer numeroCuenta) {
		service.deleteByNumeroCuenta(numeroCuenta);
		return ResponseEntity.noContent().build();
	}
}