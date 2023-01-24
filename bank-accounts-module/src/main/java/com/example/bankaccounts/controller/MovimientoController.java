package com.example.bankaccounts.controller;

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

import com.example.bankaccounts.jpa.entity.Movimiento;
import com.example.bankaccounts.service.MovimientoService;

import jakarta.validation.Valid;

@RestController
@RequestMapping(path = "/movimientos")
public class MovimientoController {

	@Autowired
	private MovimientoService service;

	@GetMapping
	public ResponseEntity<Iterable<Movimiento>> getMovimientos(@RequestParam(defaultValue = "0") Integer page,
			@RequestParam(defaultValue = "20") Integer size, @RequestParam(defaultValue = "false") Boolean details) {

		Page<Movimiento> movimientos = service.findAll(page, size);
		return (Boolean.TRUE.equals(details) ? new ResponseEntity<>(movimientos, HttpStatus.OK)
				: new ResponseEntity<>(movimientos.getContent(), HttpStatus.OK));
	}

	@GetMapping("/{movimientoId}")
	public ResponseEntity<Movimiento> getMovimientoById(@PathVariable("movimientoId") Long movimientoId) {
		return new ResponseEntity<>(service.findById(movimientoId), HttpStatus.OK);
	}

	@GetMapping("/numero-cuenta/{numeroCuenta}")
	public ResponseEntity<Iterable<Movimiento>> getMovimientosByNumeroCuenta(
			@PathVariable("numeroCuenta") Integer numeroCuenta, @RequestParam(defaultValue = "0") Integer page,
			@RequestParam(defaultValue = "20") Integer size, @RequestParam(defaultValue = "false") Boolean details) {

		Page<Movimiento> movimientos = service.findAllByNumeroCuenta(numeroCuenta, page, size);
		return (Boolean.TRUE.equals(details) ? new ResponseEntity<>(movimientos, HttpStatus.OK)
				: new ResponseEntity<>(movimientos.getContent(), HttpStatus.OK));
	}

	@PostMapping
	public ResponseEntity<Movimiento> postMovimiento(@Valid @RequestBody Movimiento movimiento) {
		Movimiento createdMovimiento = service.create(movimiento);
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{movimientoId}")
				.buildAndExpand(createdMovimiento.getMovimientoId()).toUri();
		return ResponseEntity.created(location).body(createdMovimiento);
	}

	@PutMapping("/{movimientoId}")
	public ResponseEntity<Movimiento> putMovimientoById(@PathVariable("movimientoId") Long movimientoId,
			@Valid @RequestBody Movimiento movimiento) {
		return new ResponseEntity<>(service.replaceById(movimientoId, movimiento), HttpStatus.OK);
	}

	@PatchMapping("/{movimientoId}")
	public ResponseEntity<Movimiento> patchMovimientoById(@PathVariable("movimientoId") Long movimientoId,
			@RequestBody Movimiento movimiento) {
		return new ResponseEntity<>(service.modifyById(movimientoId, movimiento), HttpStatus.OK);
	}

	@DeleteMapping("/{movimientoId}")
	public ResponseEntity<Movimiento> deleteMovimientoById(@PathVariable("movimientoId") Long movimientoId) {
		service.deleteById(movimientoId);
		return ResponseEntity.noContent().build();
	}
}