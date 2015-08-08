package org.iemm.sicomoro.db.dto;

import java.math.BigDecimal;
import java.util.Date;

public class MovementDTO {

	private Date date;
	private String type;
	private String category;
	private BigDecimal amount;
	private String notes;
	private boolean invoice;

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}
	
	

	public boolean isInvoice() {
		return invoice;
	}

	public void setInvoice(boolean invoice) {
		this.invoice = invoice;
	}

	@Override
	public String toString() {
		return "[" + category + ": " + amount + "]";
	}

}
