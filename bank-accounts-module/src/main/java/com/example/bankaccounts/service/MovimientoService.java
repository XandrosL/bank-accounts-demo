package com.example.bankaccounts.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.bankaccounts.exception.ForbiddenOperationException;
import com.example.bankaccounts.exception.ResourceNotFoundException;
import com.example.bankaccounts.jpa.entity.Movimiento;
import com.example.bankaccounts.jpa.entity.Movimiento.TipoMovimiento;
import com.example.bankaccounts.jpa.repository.MovimientoRepository;
import com.example.bankaccounts.utils.BankAccountsUtils;

@Service
public class MovimientoService {

	private static final String MOVIMIENTO_NO_ENCONTRADO_O_ID_INVALIDO = "Movimiento no encontrado o ID inválido";
	private static final String NO_HAY_MOVIMIENTOS_O_ID_DE_CUENTA_INVALIDO = "No hay movimientos o ID de cuenta inválido";
	private static final String MOVIMIENTO_INVALIDO = "Movimiento inválido";
	private static final String DEBE_SER_EL_MOVIMIENTO_MAS_RECIENTE_DE_UNA_CUENTA = "Solo se permite modificar o eliminar el movimiento más reciente de una cuenta";
	private static final String SALDO_NO_DISPONIBLE = "Saldo no disponible";

	@Autowired
	private MovimientoRepository movimientoRepository;
	@Autowired
	private CuentaService cuentaService;

	public Page<Movimiento> findAll(int page, int size) {
		return findAll(page, size, Sort.unsorted());
	}

	public Page<Movimiento> findAll(int page, int size, Sort sort) {
		return movimientoRepository.findAll(PageRequest.of(page, size, sort));
	}

	public Page<Movimiento> findAllByCuentaId(Long cuentaId, int page, int size) {
		return findAllByCuentaId(cuentaId, page, size, Sort.unsorted());
	}

	public Page<Movimiento> findAllByCuentaId(Long cuentaId, int page, int size, Sort sort) {
		return movimientoRepository.findAllByCuentaId(cuentaId, PageRequest.of(page, size, sort));
	}

	public Movimiento findById(Long movimientoId) {
		return movimientoRepository.findById(movimientoId)
				.orElseThrow(() -> new ResourceNotFoundException(MOVIMIENTO_NO_ENCONTRADO_O_ID_INVALIDO));
	}

	@Transactional
	public Movimiento create(Movimiento movimiento) {
		if (movimiento == null) {
			throw new IllegalArgumentException(MOVIMIENTO_INVALIDO);
		}
		movimiento.setMovimientoId(null);
		setCalculatedSaldo(movimiento);
		return movimientoRepository.save(movimiento);
	}

	@Transactional
	public Movimiento replaceById(Long movimientoId, Movimiento movimiento) {
		if (movimiento == null) {
			throw new IllegalArgumentException(MOVIMIENTO_INVALIDO);
		}
		Movimiento oldMovimiento = findById(movimientoId);
		Movimiento latest = findLatestByCuentaId(oldMovimiento.getCuentaId())
				.orElseThrow(() -> new ResourceNotFoundException(NO_HAY_MOVIMIENTOS_O_ID_DE_CUENTA_INVALIDO));
		if (!latest.getMovimientoId().equals(movimientoId)) {
			throw new ForbiddenOperationException(DEBE_SER_EL_MOVIMIENTO_MAS_RECIENTE_DE_UNA_CUENTA);
		}
		movimiento.setMovimientoId(oldMovimiento.getMovimientoId());
		setCalculatedSaldo(movimiento, true);
		return movimientoRepository.save(oldMovimiento);
	}

	public Movimiento modifyById(Long movimientoId, Movimiento movimiento) {
		if (movimiento == null) {
			throw new IllegalArgumentException(MOVIMIENTO_INVALIDO);
		}
		Movimiento oldMovimiento = findById(movimientoId);
		Movimiento latest = findLatestByCuentaId(oldMovimiento.getCuentaId())
				.orElseThrow(() -> new ResourceNotFoundException(NO_HAY_MOVIMIENTOS_O_ID_DE_CUENTA_INVALIDO));
		if (!latest.getMovimientoId().equals(movimientoId)) {
			throw new ForbiddenOperationException(DEBE_SER_EL_MOVIMIENTO_MAS_RECIENTE_DE_UNA_CUENTA);
		}
		movimiento.setMovimientoId(null);
		BankAccountsUtils.copyNonNullValues(movimiento, oldMovimiento);
		setCalculatedSaldo(oldMovimiento, true);
		return movimientoRepository.save(oldMovimiento);
	}

	@Transactional
	public void deleteById(Long movimientoId) {
		Movimiento movimiento = findById(movimientoId);
		Movimiento latest = findLatestByCuentaId(movimiento.getCuentaId())
				.orElseThrow(() -> new ResourceNotFoundException(NO_HAY_MOVIMIENTOS_O_ID_DE_CUENTA_INVALIDO));
		if (!latest.getMovimientoId().equals(movimientoId)) {
			throw new ForbiddenOperationException(DEBE_SER_EL_MOVIMIENTO_MAS_RECIENTE_DE_UNA_CUENTA);
		}
		movimientoRepository.deleteById(movimientoId);
	}

	public Optional<Movimiento> findLatestByCuentaId(Long cuentaId) {
		return findLatestByCuentaId(cuentaId, null);
	}

	public Optional<Movimiento> findLatestByCuentaId(Long cuentaId, Long excludedMovimientoId) {
		List<Order> orderBys = new ArrayList<>();
		orderBys.add(new Order(Sort.Direction.DESC, "Fecha"));
		orderBys.add(new Order(Sort.Direction.DESC, "MovimientoId"));
		return (excludedMovimientoId != null && excludedMovimientoId > 0
				? movimientoRepository.findFirstByCuentaIdAndMovimientoIdNot(cuentaId, excludedMovimientoId,
						Sort.by(orderBys))
				: movimientoRepository.findFirstByCuentaId(cuentaId, Sort.by(orderBys)));
	}

	private void setCalculatedSaldo(Movimiento movimiento) {
		setCalculatedSaldo(movimiento, false);
	}

	private void setCalculatedSaldo(Movimiento movimiento, boolean excludeItself) {
		Long cuentaId = movimiento.getCuentaId();
		Movimiento latest = excludeItself ? findLatestByCuentaId(cuentaId, movimiento.getMovimientoId()).orElse(null)
				: findLatestByCuentaId(cuentaId).orElse(null);
		Double saldo = (latest != null ? latest.getSaldo() : cuentaService.findById(cuentaId).getSaldoInicial());
		Double valor = movimiento.getValor();

		if (movimiento.getTipoMovimiento().equals(TipoMovimiento.D)) {
			movimiento.setSaldo(saldo + valor);
		} else if (saldo.compareTo(valor) >= 0) {
			movimiento.setSaldo(saldo - valor);
		} else {
			throw new IllegalArgumentException(SALDO_NO_DISPONIBLE);
		}
	}
}