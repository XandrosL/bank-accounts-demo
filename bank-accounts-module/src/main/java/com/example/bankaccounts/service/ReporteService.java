package com.example.bankaccounts.service;

import java.net.URI;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.example.bankaccounts.dto.ClienteDTO;
import com.example.bankaccounts.dto.ReporteDTO;
import com.example.bankaccounts.jpa.entity.Cuenta;
import com.example.bankaccounts.jpa.entity.Movimiento;
import com.example.bankaccounts.jpa.entity.Movimiento.TipoMovimiento;

@Service
public class ReporteService {

	@Autowired
	private CuentaService cuentaService;
	@Autowired
	private MovimientoService movimientoService;
	@Autowired
	private RestTemplate restTemplate;

	@Value("${users-module.clients.url}")
	private String clientesUrl;

	public List<ReporteDTO> getEstadoDeCuenta(Long clienteId, Date fechaInicio, Date fechaFin) {
		List<ReporteDTO> results = new ArrayList<>();

		URI clientesUri = UriComponentsBuilder.fromHttpUrl(clientesUrl).path("/{clienteId}").buildAndExpand(clienteId)
				.toUri();
		ResponseEntity<ClienteDTO> response = restTemplate.getForEntity(clientesUri, ClienteDTO.class);
		ClienteDTO cliente = response.getBody();
		if (!response.getStatusCode().is2xxSuccessful() || cliente == null) {
			throw new RestClientException("No fue posible hallar el cliente");
		}

		Iterable<Cuenta> cuentas = cuentaService.findAllByClienteId(cliente.getClienteId());
		cuentas.forEach(cuenta -> {
			Iterable<Movimiento> movimientos = movimientoService.findAllByCuentaIdAndFechaBetween(cuenta.getCuentaId(),
					fechaInicio, fechaFin);
			movimientos.forEach(movimiento -> {
				ReporteDTO reporteDTO = new ReporteDTO();
				reporteDTO.setFecha(movimiento.getFecha());
				reporteDTO.setCliente(cliente.getNombre());
				reporteDTO.setNumeroCuenta(cuenta.getNumeroCuenta());
				reporteDTO.setTipo(cuenta.getTipoCuenta().getDescripcion());
				reporteDTO.setSaldoInicial(cuenta.getSaldoInicial());
				reporteDTO.setEstado(cuenta.getEstado());
				reporteDTO.setMovimiento(TipoMovimiento.D.equals(movimiento.getTipoMovimiento()) ? movimiento.getValor()
						: -1 * movimiento.getValor());
				reporteDTO.setSaldoDisponible(movimiento.getSaldo());
				results.add(reporteDTO);
			});
		});
		results.sort((reporteDTO1, reporteDTO2) -> reporteDTO1.getFecha().compareTo(reporteDTO2.getFecha()));
		return results;
	}
}