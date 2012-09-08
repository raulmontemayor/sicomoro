package org.iemm.sicomoro.exception;

/**
 * Excepcion de logica de negocio
 * @author raul
 *
 */
public class BussinesLogicException extends Exception {

	/** serialVersionUID */
	private static final long serialVersionUID = 3468455272580510743L;

	public BussinesLogicException(String message, Throwable cause) {
		super(message, cause);
	}

	public BussinesLogicException(String message) {
		super(message);
	}

}
