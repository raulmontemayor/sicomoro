package org.iemm.sicomoro.controller;

import org.iemm.sicomoro.db.dao.MovementCut;

public class MovementCutForm {
	
	public MovementCutForm(){
		this.movementCut = new MovementCut();
	}
	
	private MovementCut movementCut;

	/**
	 * @return the movement
	 */
	public MovementCut getMovementCut() {
		return movementCut;
	}
	/**
	 * @param movement the movement to set
	 */
	public void setMovementCut(MovementCut movementCut) {
		this.movementCut = movementCut;
	}

}
