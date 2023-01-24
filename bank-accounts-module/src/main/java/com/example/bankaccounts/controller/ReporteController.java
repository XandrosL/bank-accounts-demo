package com.example.bankaccounts.controller;

import java.sql.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.bankaccounts.dto.ReporteDTO;
import com.example.bankaccounts.service.ReporteService;

@Controller
@RequestMapping(path = "/reportes")
public class ReporteController {

	@Autowired
	private ReporteService service;

	@GetMapping
	public ResponseEntity<List<ReporteDTO>> getEstadoDeCuenta(@RequestParam Long clienteId,
			@RequestParam Date fechaInicio, @RequestParam Date fechaFin) {
		return new ResponseEntity<>(service.getEstadoDeCuenta(clienteId, fechaInicio, fechaFin), HttpStatus.OK);
	}
}
