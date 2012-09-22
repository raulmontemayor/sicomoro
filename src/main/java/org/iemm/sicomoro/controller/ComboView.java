package org.iemm.sicomoro.controller;

/**
 * Una clase pojo con solo dos atributos value y text, para llenar los select
 * via json
 * 
 * @author raul
 * 
 */
public class ComboView {

	private String value;
	private String text;
	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}
	/**
	 * @param value the value to set
	 */
	public void setValue(String value) {
		this.value = value;
	}
	/**
	 * @return the text
	 */
	public String getText() {
		return text;
	}
	/**
	 * @param text the text to set
	 */
	public void setText(String text) {
		this.text = text;
	}
	
}
