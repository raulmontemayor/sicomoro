package org.iemm.sicomoro.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.iemm.sicomoro.db.dto.MovementDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MovementService {

	private Logger LOG = LoggerFactory.getLogger(MovementService.class);

	public List<MovementDTO> getIncomeList(List<MovementDTO> listofMovement) {
		final List<MovementDTO> result = new ArrayList<MovementDTO>();
		for (MovementDTO movementDTO : listofMovement) {
			if (movementDTO.getType().equals("income")) {
				result.add(movementDTO);
			}
		}
		return result;
	}

	public List<MovementDTO> getOfferingList(List<MovementDTO> listofMovement) {
		final List<MovementDTO> result = new ArrayList<MovementDTO>();
		for (MovementDTO movementDTO : listofMovement) {
			if (movementDTO.getCategory().equals("Ofrenda")) {
				result.add(movementDTO);
			}
		}
		return result;
	}

	public List<MovementDTO> getExpenseList(List<MovementDTO> listofMovement) {
		final List<MovementDTO> result = new ArrayList<MovementDTO>();
		for (MovementDTO movementDTO : listofMovement) {
			if (movementDTO.getType().equals("expense")) {
				result.add(movementDTO);
			}
		}
		return result;
	}

	public List<MovementDTO> getTitheList(List<MovementDTO> listofMovement) {
		final List<MovementDTO> result = new ArrayList<MovementDTO>();
		for (MovementDTO movementDTO : listofMovement) {
			if (!movementDTO.getCategory().equals("Ofrenda")) {
				result.add(movementDTO);
			}
		}
		return result;
	}

	public List<MovementDTO> getTaxList(List<MovementDTO> listofMovement) {
		final List<MovementDTO> result = new ArrayList<MovementDTO>();
		final BigDecimal totalAmount = sumAmount(listofMovement);
		final MovementDTO taxMovement = new MovementDTO();
		taxMovement.setDate(new Date());
		taxMovement.setAmount(totalAmount.multiply(new BigDecimal("0.23"))
				.setScale(0, RoundingMode.HALF_DOWN));
		taxMovement.setCategory("Aportación 23%");
		taxMovement.setType("expense");
		taxMovement.setInvoice(true);
		result.add(taxMovement);
		return result;
	}

	public BigDecimal sumAmount(List<MovementDTO> listofMovement) {
		BigDecimal result = BigDecimal.ZERO;
		for (MovementDTO movementDTO : listofMovement) {
			result = result.add(movementDTO.getAmount());
		}
		return result;
	}
	
	public List<MovementDTO> getMaintenanceList(List<MovementDTO> listofMovement) {
		final List<MovementDTO> result = new ArrayList<MovementDTO>();
		for (MovementDTO movementDTO : listofMovement) {
			if (movementDTO.getCategory().equals("Manutención") || movementDTO.getCategory().equals("Ayuda Transporte") || movementDTO.getCategory().equals("Amor")) {
				result.add(movementDTO);
			}
		}
		return result;
	}
	
	public List<MovementDTO> getConservationList(List<MovementDTO> listofMovement) {
		final List<MovementDTO> result = new ArrayList<MovementDTO>();
		for (MovementDTO movementDTO : listofMovement) {
			if (movementDTO.getCategory().equals("Conservación")) {
				result.add(movementDTO);
			}
		}
		return result;
	}
	
	public List<MovementDTO> getWaterList(List<MovementDTO> listofMovement) {
		final List<MovementDTO> result = new ArrayList<MovementDTO>();
		for (MovementDTO movementDTO : listofMovement) {
			if (movementDTO.getCategory().equals("Agua") || movementDTO.getCategory().equals("Agua Templo")
					|| movementDTO.getCategory().equals("Agua Casa Pastoral")) {
				result.add(movementDTO);
			}
		}
		return result;
	}
	
	public List<MovementDTO> getLightList(List<MovementDTO> listofMovement) {
		final List<MovementDTO> result = new ArrayList<MovementDTO>();
		for (MovementDTO movementDTO : listofMovement) {
			if (movementDTO.getCategory().equals("Luz") || movementDTO.getCategory().equals("Luz Templo")
					|| movementDTO.getCategory().equals("Luz Casa Pastoral")) {
				result.add(movementDTO);
			}
		}
		return result;
	}
	
	public List<MovementDTO> getPaperList(List<MovementDTO> listofMovement) {
		final List<MovementDTO> result = new ArrayList<MovementDTO>();
		for (MovementDTO movementDTO : listofMovement) {
			if (movementDTO.getCategory().equals("Copias") || movementDTO.getCategory().equals("Papelería")) {
				result.add(movementDTO);
			}
		}
		return result;
	}
	
	public List<MovementDTO> getTransportList(List<MovementDTO> listofMovement) {
		final List<MovementDTO> result = new ArrayList<MovementDTO>();
		for (MovementDTO movementDTO : listofMovement) { //TODO revisar que coincida las categorías
			if (movementDTO.getCategory().equals("Transportación")) {
				result.add(movementDTO);
			}
		}
		return result;
	}
	
	public List<MovementDTO> getFoodList(List<MovementDTO> listofMovement) {
		final List<MovementDTO> result = new ArrayList<MovementDTO>();
		for (MovementDTO movementDTO : listofMovement) { //TODO revisar que coincida las categorías
			if (movementDTO.getCategory().equals("Refrigerio")) {
				result.add(movementDTO);
			}
		}
		return result;
	}
	
	public Map<Date, List<MovementDTO>> groupByDay(
			List<MovementDTO> titheList) {
		final Map<Date, List<MovementDTO>> result = new LinkedHashMap<Date, List<MovementDTO>>();
		for (MovementDTO movementDTO : titheList) {
			final Date date = movementDTO.getDate();
			if(result.containsKey(date)) {
				result.get(date).add(movementDTO);
			} else {
				final List<MovementDTO> list = new ArrayList<MovementDTO>();
				list.add(movementDTO);
				result.put(date, list);
			}
		}
		return result;
	}
}
