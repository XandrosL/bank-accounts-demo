package com.example.bankaccounts.controller;

import static org.hamcrest.CoreMatchers.containsString;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

import com.example.bankaccounts.jpa.entity.Cuenta;
import com.example.bankaccounts.jpa.entity.Cuenta.TipoCuenta;
import com.example.bankaccounts.service.CuentaService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(CuentaController.class)
class CuentaControllerTest {

	@Autowired
	private MockMvc mvc;

	@MockBean
	private CuentaService service;

	@Autowired
	private ObjectMapper objectMapper;

	private Cuenta cuenta;

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
		cuenta = new Cuenta();
		cuenta.setClienteId(1L);
		cuenta.setCuentaId(1L);
		cuenta.setEstado(true);
		cuenta.setNumeroCuenta(123456L);
		cuenta.setSaldoInicial(1000.0);
		cuenta.setTipoCuenta(TipoCuenta.A);
	}

//
//	@AfterEach
//	void tearDown() throws Exception {
//	}
//
	@Test
	void testGetCuentas() throws Exception {
		List<Cuenta> list = new ArrayList<>();
		list.add(cuenta);
		Page<Cuenta> page = new PageImpl<>(list);
		Mockito.when(service.findAll(Mockito.anyInt(), Mockito.anyInt())).thenReturn(page);

		MvcResult result = mvc.perform(MockMvcRequestBuilders.get("/cuentas").accept(MediaType.APPLICATION_JSON))
				.andDo(print()).andExpect(status().isOk()).andExpect(MockMvcResultMatchers.jsonPath("$.[*]").exists())
				.andExpect(MockMvcResultMatchers.jsonPath("$.[*].cuentaId").isNotEmpty()).andReturn();

		Cuenta[] cuentaResponse = objectMapper.readValue(result.getResponse().getContentAsString(), Cuenta[].class);
		Assert.isTrue(cuentaResponse[0].getNumeroCuenta().equals(cuenta.getNumeroCuenta()),
				"La respuesta debe contener la cuenta dada");
	}

//
//	@Test
//	void testGetCuentasByClienteId() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	void testGetCuentaById() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	void testGetCuentaByNumeroCuenta() {
//		fail("Not yet implemented");
//	}
//
	@Test
	void testPostCuenta() throws Exception {
		cuenta.setCuentaId(2L);
		cuenta.setClienteId(10L);
		cuenta.setNumeroCuenta(434234L);
		cuenta.setSaldoInicial(5000.0);
		Mockito.when(service.create(Mockito.any(Cuenta.class))).thenReturn(cuenta);
		RequestBuilder request = MockMvcRequestBuilders.post("/cuentas").accept(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(cuenta)).contentType(MediaType.APPLICATION_JSON);

		MvcResult result = mvc.perform(request).andExpect(status().isCreated())
				.andExpect(header().string("location", containsString("/cuentas/"))).andReturn();

		Cuenta cuentaResponse = objectMapper.readValue(result.getResponse().getContentAsString(), Cuenta.class);
		Assert.isTrue(cuentaResponse.getNumeroCuenta().equals(cuenta.getNumeroCuenta()),
				"La respuesta debe contener la cuenta creada");
	}
//
//	@Test
//	void testPutCuentaById() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	void testPutCuentaByNumeroCuenta() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	void testPatchCuentaById() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	void testPatchClienteByNumeroCuenta() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	void testDeleteCuentaById() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	void testDeleteCuentaByNumeroCuenta() {
//		fail("Not yet implemented");
//	}

}
