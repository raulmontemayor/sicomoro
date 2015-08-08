package org.iemm.sicomoro.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.Validate;
import org.iemm.sicomoro.db.client.MovementCutMapper;
import org.iemm.sicomoro.db.dao.Movement;
import org.iemm.sicomoro.db.dao.MovementCut;
import org.iemm.sicomoro.db.dao.MovementCutExample;
import org.iemm.sicomoro.exception.BussinesLogicException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
@Service
public class MovementCutService {
	
	@Autowired
	private MovementCutMapper movementCutMapper;
	
	@Autowired
	private MovementService movementService;

	/**
	 * Regresa el ultimo corte de movimientos, entiendase ultimo como el que
	 * tenga la fecha de corte mas grande.<br/> Se creo por que para poder validar la
	 * fecha de un nuevo movimiento, necesito saber cual es el ultimo corte.
	 * 
	 * @return
	 */
	public MovementCut getLastMovementCut() {
		final MovementCutExample example = new MovementCutExample();
		example.setOrderByClause("cutDate desc");
		final List<MovementCut> movements = movementCutMapper.selectByExample(example);
		if(CollectionUtils.isEmpty(movements)) {
			return null;
		} else {
			//FIXME esto se puede mejorar, a que el query solo regrese el ultimo resultado
			return movements.get(0);
		}
	}
	
	public int createCut(Date cutDate, String description) throws BussinesLogicException {
		// Validaciones
		Validate.notNull(cutDate);
		final List<Movement> movements = movementService.getMovementsWithoutCut();
		if(CollectionUtils.isEmpty(movements)) {
			throw new BussinesLogicException("movementcut.nomovements");
		}
		final MovementCut lastMovementCut = getLastMovementCut();
		if(lastMovementCut != null && lastMovementCut.getCutDate().before(cutDate)) {
			throw new BussinesLogicException("movementcut.invaliddate");
		}
		
		
		// Crear movimiento
		final MovementCut movementCut = new MovementCut();
		movementCut.setCutDate(cutDate);
		movementCut.setDescription(description);
		// TODO quitar estas columnas
		movementCut.setCurrentBalance(BigDecimal.ZERO);
		movementCut.setPreviousBalance(BigDecimal.ZERO);
		final Date actualDate = new Date();
		movementCut.setUpdateDate(actualDate);
		movementCut.setCreateDate(actualDate);
		
		final int idMovementCut = movementCutMapper.insert(movementCut);
		// Marcar los movimientos con su corte
		for(Movement movement: movements) {
			movement.setIdMovementCut(idMovementCut);
			movementService.update(movement);
		}

		return idMovementCut;
	}

	/**
	 * @return the movementCutMapper
	 */
	public MovementCutMapper getMovementCutMapper() {
		return movementCutMapper;
	}

	/**
	 * @param movementCutMapper the movementCutMapper to set
	 */
	public void setMovementCutMapper(MovementCutMapper movementCutMapper) {
		this.movementCutMapper = movementCutMapper;
	}

	public MovementService getMovementService() {
		return movementService;
	}

	public void setMovementService(MovementService movementService) {
		this.movementService = movementService;
	}

}
