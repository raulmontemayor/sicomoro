package org.iemm.sicomoro.controller;

import org.iemm.sicomoro.db.dao.Movement;
import org.iemm.sicomoro.service.MovementService.MovementTypeE;

public class MovementForm {
	
	public MovementForm(){
		this.movement = new Movement();
	}
	
	private Movement movement;
	private MovementTypeE movementType;
	/**
	 * @return the movement
	 */
	public Movement getMovement() {
		return movement;
	}
	/**
	 * @param movement the movement to set
	 */
	public void setMovement(Movement movement) {
		this.movement = movement;
	}
	/**
	 * @return the movementType
	 */
	public MovementTypeE getMovementType() {
		return movementType;
	}
	/**
	 * @param movementType the movementType to set
	 */
	public void setMovementType(MovementTypeE movementType) {
		this.movementType = movementType;
	}

}
