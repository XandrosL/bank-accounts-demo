package com.example.bankaccounts.controller;

import static org.hamcrest.CoreMatchers.containsString;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.util.Assert;

import com.example.bankaccounts.jpa.entity.Movimiento;
import com.example.bankaccounts.jpa.entity.Movimiento.TipoMovimiento;
import com.example.bankaccounts.service.MovimientoService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(MovimientoController.class)
class MovimientoControllerTest {

	@Autowired
	private MockMvc mvc;

	@MockBean
	private MovimientoService service;

	@Autowired
	private ObjectMapper objectMapper;

	private Movimiento movimiento;

//	@BeforeAll
//	static void setUpBeforeClass() throws Exception {
//	}
//
//	@AfterAll
//	static void tearDownAfterClass() throws Exception {
//	}
//
	@BeforeEach
	void setUp() throws Exception {
		movimiento = new Movimiento();
		movimiento.setCuentaId(1L);
		movimiento.setFecha(new Date(System.currentTimeMillis()));
		movimiento.setMovimientoId(1L);
		movimiento.setSaldo(1000.0);
		movimiento.setTipoMovimiento(TipoMovimiento.D);
		movimiento.setValor(1000.0);
	}

//
//	@AfterEach
//	void tearDown() throws Exception {
//	}
//
	@Test
	void testGetMovimientos() throws Exception {
		List<Movimiento> list = new ArrayList<>();
		list.add(movimiento);
		Page<Movimiento> page = new PageImpl<>(list);
		Mockito.when(service.findAll(Mockito.anyInt(), Mockito.anyInt())).thenReturn(page);

		MvcResult result = mvc.perform(MockMvcRequestBuilders.get("/movimientos").accept(MediaType.APPLICATION_JSON))
				.andDo(print()).andExpect(status().isOk()).andExpect(MockMvcResultMatchers.jsonPath("$.[*]").exists())
				.andExpect(MockMvcResultMatchers.jsonPath("$.[*].movimientoId").isNotEmpty()).andReturn();

		Movimiento[] movimientoResponse = objectMapper.readValue(result.getResponse().getContentAsString(),
				Movimiento[].class);
		Assert.isTrue(movimientoResponse[0].getValor().equals(movimiento.getValor()),
				"La respuesta debe contener el movimiento dado");
	}

//
//	@Test
//	void testGetMovimientoById() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	void testGetMovimientosByNumeroCuenta() {
//		fail("Not yet implemented");
//	}
//
	@Test
	void testPostMovimiento() throws Exception {
		movimiento.setCuentaId(2L);
		movimiento.setFecha(new Date(System.currentTimeMillis()));
		movimiento.setMovimientoId(2L);
		movimiento.setSaldo(7000.0);
		movimiento.setTipoMovimiento(TipoMovimiento.D);
		movimiento.setValor(2000.0);
		Mockito.when(service.create(Mockito.any(Movimiento.class))).thenReturn(movimiento);
		RequestBuilder request = MockMvcRequestBuilders.post("/movimientos").accept(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(movimiento)).contentType(MediaType.APPLICATION_JSON);

		MvcResult result = mvc.perform(request).andExpect(status().isCreated())
				.andExpect(header().string("location", containsString("/movimientos/"))).andReturn();

		Movimiento movimientoResponse = objectMapper.readValue(result.getResponse().getContentAsString(),
				Movimiento.class);
		Assert.isTrue(movimientoResponse.getMovimientoId().equals(movimiento.getMovimientoId()),
				"La respuesta debe contener el movimiento creado");
	}
//
//	@Test
//	void testPutMovimientoById() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	void testPatchMovimientoById() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	void testDeleteMovimientoById() {
//		fail("Not yet implemented");
//	}

}
