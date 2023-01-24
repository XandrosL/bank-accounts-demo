package com.example.bankaccounts.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
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
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.util.Assert;

import com.example.bankaccounts.dto.ReporteDTO;
import com.example.bankaccounts.service.ReporteService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(ReporteController.class)
class ReporteControllerTest {

	@Autowired
	private MockMvc mvc;

	@MockBean
	private ReporteService service;

	@Autowired
	private ObjectMapper objectMapper;

	private ReporteDTO reporteDTO;

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
		reporteDTO = new ReporteDTO();
		reporteDTO.setCliente("Prueba");
		reporteDTO.setEstado(true);
		reporteDTO.setFecha(new Date(System.currentTimeMillis()));
		reporteDTO.setMovimiento(-1000.0);
		reporteDTO.setNumeroCuenta(123456L);
		reporteDTO.setSaldoDisponible(1000.0);
		reporteDTO.setSaldoInicial(2000.0);
		reporteDTO.setTipo("Ahorros");
	}

//
//	@AfterEach
//	void tearDown() throws Exception {
//	}
//
	@Test
	void testGetEstadoDeCuenta() throws Exception {
		List<ReporteDTO> list = new ArrayList<>();
		list.add(reporteDTO);
		Mockito.when(service.getEstadoDeCuenta(Mockito.anyLong(), Mockito.any(Date.class), Mockito.any(Date.class)))
				.thenReturn(list);

		MvcResult result = mvc
				.perform(MockMvcRequestBuilders.get("/reportes").param("clienteId", "1")
						.param("fechaInicio", "2023-01-01").param("fechaFin", "2021-01-31")
						.accept(MediaType.APPLICATION_JSON))
				.andDo(print()).andExpect(status().isOk()).andExpect(MockMvcResultMatchers.jsonPath("$.[*]").exists())
				.andExpect(MockMvcResultMatchers.jsonPath("$.[*].cliente").isNotEmpty()).andReturn();

		ReporteDTO[] reporteResponse = objectMapper.readValue(result.getResponse().getContentAsString(),
				ReporteDTO[].class);
		Assert.isTrue(reporteResponse[0].getNumeroCuenta().equals(reporteDTO.getNumeroCuenta()),
				"La respuesta debe contener el registro dado");
	}
}
