package org.iemm.sicomoro.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
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
		LOG.debug("--getTaxList() {}", taxMovement);
		return result;
	}

	public BigDecimal sumAmount(List<MovementDTO> listofMovement) {
		BigDecimal result = BigDecimal.ZERO;
		for (MovementDTO movementDTO : listofMovement) {
			result = result.add(movementDTO.getAmount());
		}
		return result;
	}
	
	public List<MovementDTO> getList(List<MovementDTO> listofMovement, String key) {
		final List<MovementDTO> result = new ArrayList<MovementDTO>();
		final List<Object> categories = ConfigService.getConfig()
				.getList(key, new ArrayList<String>());
		for (MovementDTO movementDTO : listofMovement) {
			if(categories.contains(movementDTO.getCategory())) {
				result.add(movementDTO);
			}
		}
		return result;
	}
	
	public List<MovementDTO> getMaintenanceList(List<MovementDTO> listofMovement) {
		return getList(listofMovement, "category.maintenance");
	}
	
	public List<MovementDTO> getConservationList(List<MovementDTO> listofMovement) {
		return getList(listofMovement, "category.conservation");
	}
	
	public List<MovementDTO> getWaterList(List<MovementDTO> listofMovement) {
		return getList(listofMovement, "category.water");
	}
	
	public List<MovementDTO> getLightList(List<MovementDTO> listofMovement) {
		return getList(listofMovement, "category.light");
	}
	
	public List<MovementDTO> getPaperList(List<MovementDTO> listofMovement) {
		final List<MovementDTO> result = getList(listofMovement, "category.paper");
		LOG.debug("--getPaperList() {}", result);
		return result;
	}
	
	public List<MovementDTO> getTransportList(List<MovementDTO> listofMovement) {
		return getList(listofMovement, "category.transport");
	}
	
	public List<MovementDTO> getFoodList(List<MovementDTO> listofMovement) {
		return getList(listofMovement, "category.food");
	}
	
	public Map<Date, List<MovementDTO>> groupByDay(
			List<MovementDTO> titheList) {
		LOG.trace(">> groupByDay()");
		final Map<Date, List<MovementDTO>> result = new LinkedHashMap<Date, List<MovementDTO>>();
		for (MovementDTO movementDTO : titheList) {
			final Date date = getNextMondayOfMonth(movementDTO.getDate());
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
	
	private Date getNextMondayOfMonth(Date originalDate) {
		final Calendar calendar = GregorianCalendar.getInstance();
		calendar.setTime(originalDate);
		final int month = calendar.get(Calendar.MONTH);
		final int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
		if (dayOfWeek != Calendar.SUNDAY) {
			int days = (Calendar.SATURDAY - dayOfWeek + 1) %7;
			calendar.add(Calendar.DAY_OF_YEAR, days);
			if (calendar.get(Calendar.MONTH) != month) {
				calendar.add(Calendar.DAY_OF_YEAR, -7);
			}
		}	
		return calendar.getTime();
	}
	
	
	public BigDecimal getLastTotal(Date theDateOfCut) {
		final Calendar calendar = GregorianCalendar.getInstance();
		calendar.setTime(theDateOfCut);
		calendar.set(GregorianCalendar.DAY_OF_MONTH, -1);
		final String lastMonth = calendar.get(Calendar.YEAR) + "" + (calendar.get(Calendar.MONTH) + 1);
		return ConfigService.getConfig().getBigDecimal("last.total." + lastMonth, BigDecimal.ZERO);
	}
	
	public void setLastTotal(Date theDateOfCut, BigDecimal lastTotal) {
		final Calendar calendar = GregorianCalendar.getInstance();
		calendar.setTime(theDateOfCut);
		final String month = calendar.get(Calendar.YEAR) + "" + (calendar.get(Calendar.MONTH) + 1);
		ConfigService.saveConfig("last.total." + month, lastTotal);
	}

	public List<MovementDTO> grouByCategory(List<MovementDTO> thiteList) {
		final List<MovementDTO> copy = new ArrayList<MovementDTO>(thiteList);
		final List<MovementDTO> result = new ArrayList<MovementDTO>();
		Map<String, MovementDTO> map = new HashMap<String, MovementDTO>();
		for (MovementDTO movementDTO : copy) {
			if(map.containsKey(movementDTO.getCategory())) {
				final BigDecimal amount = movementDTO.getAmount().add(map.get(movementDTO.getCategory()).getAmount());
				map.get(movementDTO.getCategory()).setAmount(amount);
			} else {
				map.put(movementDTO.getCategory(), movementDTO);
			}
		}
		for(MovementDTO movement: map.values()) {
			result.add(movement);
		}
		Collections.sort(result, new Comparator<MovementDTO>() {

			@Override
			public int compare(MovementDTO o1, MovementDTO o2) {
				return o1.getCategory().compareTo(o2.getCategory());
			}
		});
		
		return result;
	}
}
