package org.iemm.sicomoro.service;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.iemm.sicomoro.db.client.MovementCutMapper;
import org.iemm.sicomoro.db.dao.MovementCut;
import org.iemm.sicomoro.db.dao.MovementCutExample;
import org.springframework.beans.factory.annotation.Autowired;

public class MovementCutService {
	
	@Autowired
	private MovementCutMapper movementCutMapper;

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

}
