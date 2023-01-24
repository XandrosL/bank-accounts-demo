package com.example.users.controller;

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

import com.example.users.jpa.entity.Cliente;
import com.example.users.jpa.entity.Persona.Genero;
import com.example.users.service.ClienteService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(ClienteController.class)
class ClienteControllerTest {

	@Autowired
	private MockMvc mvc;

	@MockBean
	private ClienteService service;

	@Autowired
	private ObjectMapper objectMapper;

	private Cliente cliente;

//	@BeforeAll
//	static void setUpBeforeClass() throws Exception {
//	}
//
//	@AfterAll
//	static void tearDownAfterClass() throws Exception {
//	}

	@BeforeEach
	void setUp() throws Exception {
		cliente = new Cliente();
		cliente.setClienteId(1L);
		cliente.setContraseña("123456");
		cliente.setDireccion("Sector La Mañosca, Quito");
		cliente.setEdad(30);
		cliente.setEstado(true);
		cliente.setGenero(Genero.M);
		cliente.setIdentificacion(123456789L);
		cliente.setNombre("Cliente Prueba");
		cliente.setTelefono(999999999);
	}

//	@AfterEach
//	void tearDown() throws Exception {
//	}

	@Test
	void testGetClientes() throws Exception {
		List<Cliente> list = new ArrayList<>();
		list.add(cliente);
		Page<Cliente> page = new PageImpl<>(list);
		Mockito.when(service.findAll(Mockito.anyInt(), Mockito.anyInt())).thenReturn(page);

		mvc.perform(MockMvcRequestBuilders.get("/clientes").accept(MediaType.APPLICATION_JSON)).andDo(print())
				.andExpect(status().isOk()).andExpect(MockMvcResultMatchers.jsonPath("$.[*]").exists())
				.andExpect(MockMvcResultMatchers.jsonPath("$.[*].clienteId").isNotEmpty())
				.andExpect(MockMvcResultMatchers.jsonPath("$.[*].nombre").value("Cliente Prueba"));
	}

//	@Test
//	void testGetClienteById() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	void testGetClienteByIdentificacion() {
//		fail("Not yet implemented");
//	}

	@Test
	void testPostCliente() throws Exception {
		cliente.setNombre("Cliente Prueba Post");
		cliente.setIdentificacion(222222222L);
		Mockito.when(service.create(Mockito.any(Cliente.class))).thenReturn(cliente);
		RequestBuilder request = MockMvcRequestBuilders.post("/clientes").accept(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(cliente)).contentType(MediaType.APPLICATION_JSON);

		MvcResult result = mvc.perform(request).andExpect(status().isCreated())
				.andExpect(header().string("location", containsString("/clientes/"))).andReturn();

		Cliente clienteResponse = objectMapper.readValue(result.getResponse().getContentAsString(), Cliente.class);
		Assert.isTrue(clienteResponse.getNombre() != null, "La respuesta debe contener el cliente creado");
	}

//	@Test
//	void testPutClienteById() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	void testPutClienteByIdentificacion() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	void testPatchClienteById() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	void testPatchClienteByIdentificacion() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	void testDeleteClienteById() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	void testDeleteClienteByIdentificacion() {
//		fail("Not yet implemented");
//	}
}