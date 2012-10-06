package org.iemm.sicomoro.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.iemm.sicomoro.db.client.MovementMapper;
import org.iemm.sicomoro.db.client.MovementTypeMapper;
import org.iemm.sicomoro.db.dao.Movement;
import org.iemm.sicomoro.db.dao.MovementCut;
import org.iemm.sicomoro.db.dao.MovementType;
import org.iemm.sicomoro.db.dao.MovementTypeExample;
import org.iemm.sicomoro.exception.BussinesLogicException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MovementService {

	@Autowired
	private MovementMapper movementMapper;

	@Autowired
	private MovementTypeMapper movementTypeMapper;
	
	@Autowired
	private MovementCutService movementCutService;

	public enum MovementTypeE {
		UP("Alta"), DOWN("Baja"), TITHE("Diezmo")  ;
		MovementTypeE(String name) {
			this.name = name;
		}
		private String name;
	}

	/**
	 * Inserta un movimiento
	 * @param amount
	 * @param movementType
	 * @return
	 * @throws BussinesLogicException 
	 */
	public int createMovement( BigDecimal amount, Integer idContributor, Date movementDate, String description, MovementTypeE movementType) throws BussinesLogicException {
		final Movement movement = new Movement();
		
		// Si es diezmo validar que idContributor no sea null
		if(MovementTypeE.TITHE.equals(movementType)) {
			if(idContributor == null){
				throw new BussinesLogicException("movement.error.titheContributor");
			} else {
				movement.setIdContributor(idContributor);
			}
		}
		final MovementCut lastMovementCut = movementCutService.getLastMovementCut();
		if (lastMovementCut != null) {
			if (lastMovementCut.getCutDate().after(movementDate)) {
				throw new BussinesLogicException("movement.error.date");
			}
		}
		movement.setMovementDate(movementDate);
		movement.setIdMovementType(getMovementTypeId(movementType));
		movement.setAmount(amount);
		movement.setDescription(description);
		final Date actual = new Date();
		movement.setCreateDate(actual);
		movement.setUpdateDate(actual);
		return movementMapper.insert(movement);
	}

	/**
	 * Regresa el id del tipo de movimiento
	 * @param movementType
	 * @return
	 */
	public Integer getMovementTypeId(MovementTypeE movementType) {
		final MovementTypeExample example = new MovementTypeExample();
		example.createCriteria().andNameEqualTo(movementType.name);
		List<MovementType> lstMovementType = movementTypeMapper.selectByExample(example);
		return lstMovementType.get(0).getIdMovementType();
	}
	
	public Movement find(Integer idMovement){
		return movementMapper.selectByPrimaryKey(idMovement);
	}

	/**
	 * @return the movementMapper
	 */
	public MovementMapper getMovementMapper() {
		return movementMapper;
	}

	/**
	 * @param movementMapper
	 *            the movementMapper to set
	 */
	public void setMovementMapper(MovementMapper movementMapper) {
		this.movementMapper = movementMapper;
	}

	/**
	 * @return the movementTypeMapper
	 */
	public MovementTypeMapper getMovementTypeMapper() {
		return movementTypeMapper;
	}

	/**
	 * @param movementTypeMapper
	 *            the movementTypeMapper to set
	 */
	public void setMovementTypeMapper(MovementTypeMapper movementTypeMapper) {
		this.movementTypeMapper = movementTypeMapper;
	}

	/**
	 * @return the movementCutService
	 */
	public MovementCutService getMovementCutService() {
		return movementCutService;
	}

	/**
	 * @param movementCutService the movementCutService to set
	 */
	public void setMovementCutService(MovementCutService movementCutService) {
		this.movementCutService = movementCutService;
	}

}
