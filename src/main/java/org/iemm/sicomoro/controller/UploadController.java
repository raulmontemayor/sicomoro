package org.iemm.sicomoro.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;

import org.apache.commons.lang.time.DateUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.iemm.sicomoro.db.dto.MovementDTO;
import org.iemm.sicomoro.util.NumberToLetterConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/")
public class UploadController {

	private Logger LOG = LoggerFactory.getLogger(UploadController.class);

	@RequestMapping(value = "/upload", method = RequestMethod.GET)
	public String upload() {
		LOG.trace(">> upload()");
		return "/jsp/upload/upload.jsp";
	}

	@RequestMapping(value = "/upload", method = RequestMethod.POST)
	public @ResponseBody String upload(@RequestParam("name") String lastName,
			@RequestParam("file") MultipartFile file) {
		if (!file.isEmpty()) {
			try {

				final File theFile = File.createTempFile("temp", ".temp");
				file.transferTo(theFile);

				final InputStream inp = new FileInputStream(theFile);

				final Workbook wb = WorkbookFactory.create(inp);
				final Sheet sheet = wb.getSheetAt(0);
				final List<MovementDTO> listOfMovements = new ArrayList<MovementDTO>();
				final Iterator<Row> rowIterator = sheet.iterator();
				int i = 0;
				while (rowIterator.hasNext()) {
					final Row row = rowIterator.next();
					if (i != 0) {
						final Iterator<Cell> cellIterator = row.cellIterator();
						final MovementDTO movement = new MovementDTO();
						int j = 0;
						while (cellIterator.hasNext()) {
							final Cell cell = cellIterator.next();
							switch (j) {
							case 0:
								movement.setDate(getDate(cell
										.getStringCellValue()));
								break;
							case 2:
								movement.setType(cell.getStringCellValue().trim());
								break;
							case 3:
								movement.setCategory(cell.getStringCellValue().trim());
								break;
							case 4:
								movement.setAmount(new BigDecimal(cell
										.getNumericCellValue()).abs());
								break;
							case 6:
								movement.setNotes(cell.getStringCellValue().trim());
								movement.setInvoice(cell.getStringCellValue().indexOf("#invoice") != -1);
								break;
							default:
								break;
							}
							j++;
						}
						if(movement.getCategory().equals("Otros")) {
							movement.setCategory(movement.getNotes().replace("#invoice", ""));
						}
						listOfMovements.add(movement);
					}
					i++;
				}

				generateReport("".equals(lastName)? BigDecimal.ZERO : new BigDecimal(lastName), listOfMovements);

				return "yupi";

			} catch (Exception e) {
				LOG.error(e.getMessage(), e);
				return "You failed to upload " + lastName + " => " + e.getMessage();
			}
		} else {
			return "You failed to upload " + lastName
					+ " because the file was empty.";
		}
	}

	private Date getDate(String theDate) {
		final String theDay = theDate.substring(0, 2);
		final int month;
		if (theDate.indexOf("enero") != -1) {
			month = 0;
		} else if (theDate.indexOf("febrero") != -1) {
			month = 1;
		} else if (theDate.indexOf("marzo") != -1) {
			month = 2;
		} else if (theDate.indexOf("abril") != -1) {
			month = 3;
		} else if (theDate.indexOf("mayo") != -1) {
			month = 4;
		} else if (theDate.indexOf("junio") != -1) {
			month = 5;
		} else if (theDate.indexOf("julio") != -1) {
			month = 6;
		} else if (theDate.indexOf("agosto") != -1) {
			month = 7;
		} else if (theDate.indexOf("septiembre") != -1) {
			month = 8;
		} else if (theDate.indexOf("octubre") != -1) {
			month = 9;
		} else if (theDate.indexOf("noviembre") != -1) {
			month = 10;
		} else {
			month = 11;
		}
		final String theYear = theDate.substring(theDate.length() - 4,
				theDate.length());
		final Calendar calendar = GregorianCalendar.getInstance();
		DateUtils.truncate(calendar, Calendar.DATE);
		calendar.set(Calendar.YEAR, Integer.valueOf(theYear));
		calendar.set(Calendar.DATE, Integer.valueOf(theDay.trim()));
		calendar.set(Calendar.MONTH, month);
		return calendar.getTime();
	}

	private void generateReport(BigDecimal lastTotal, List<MovementDTO> listofMovement) {
		final List<MovementDTO> incomeList = getIncomeList(listofMovement);
		final BigDecimal totalIncome = sumAmount(incomeList);
		LOG.debug("generateReport() totalIncome {}", totalIncome);
		final List<MovementDTO> offeringList = getOfferingList(incomeList);

		final List<MovementDTO> titheList = getTitheList(incomeList);

		getIncomeReport(offeringList, titheList);

		final List<MovementDTO> taxList = getTaxList(incomeList);
		listofMovement.addAll(taxList);

		final List<MovementDTO> expenseList = getExpenseList(listofMovement);
		LOG.debug("--generateReport() expenseList {} ", expenseList);
		
		getExpenseReport(expenseList);
		
		getInvoiceReport(offeringList, titheList, expenseList);
		
		getNoInvoiceReport(expenseList);
		
		getMaintenanceReport(getMaintenanceList(expenseList));
		
		getCutReport(lastTotal, incomeList, expenseList);
	}






	private List<MovementDTO> getIncomeList(List<MovementDTO> listofMovement) {
		final List<MovementDTO> result = new ArrayList<MovementDTO>();
		for (MovementDTO movementDTO : listofMovement) {
			if (movementDTO.getType().equals("income")) {
				result.add(movementDTO);
			}
		}
		return result;
	}

	private List<MovementDTO> getExpenseList(List<MovementDTO> listofMovement) {
		final List<MovementDTO> result = new ArrayList<MovementDTO>();
		for (MovementDTO movementDTO : listofMovement) {
			if (movementDTO.getType().equals("expense")) {
				result.add(movementDTO);
			}
		}
		return result;
	}

	private List<MovementDTO> getOfferingList(List<MovementDTO> listofMovement) {
		final List<MovementDTO> result = new ArrayList<MovementDTO>();
		for (MovementDTO movementDTO : listofMovement) {
			if (movementDTO.getCategory().equals("Ofrenda")) {
				result.add(movementDTO);
			}
		}
		return result;
	}

	private List<MovementDTO> getTitheList(List<MovementDTO> listofMovement) {
		final List<MovementDTO> result = new ArrayList<MovementDTO>();
		for (MovementDTO movementDTO : listofMovement) {
			if (!movementDTO.getCategory().equals("Ofrenda")) {
				result.add(movementDTO);
			}
		}
		return result;
	}

	private List<MovementDTO> getTaxList(List<MovementDTO> listofMovement) {
		final List<MovementDTO> result = new ArrayList<MovementDTO>();
		final BigDecimal totalAmount = sumAmount(listofMovement);
		final MovementDTO taxMovement = new MovementDTO();
		taxMovement.setDate(new Date());
		taxMovement.setAmount(totalAmount.multiply(new BigDecimal("0.23")).setScale(0, RoundingMode.HALF_DOWN));
		taxMovement.setCategory("Aportación 23%");
		taxMovement.setType("expense");
		taxMovement.setInvoice(true);
		result.add(taxMovement);
		return result;
	}

	private BigDecimal sumAmount(List<MovementDTO> listofMovement) {
		BigDecimal result = BigDecimal.ZERO;
		for (MovementDTO movementDTO : listofMovement) {
			result = result.add(movementDTO.getAmount());
		}
		return result;
	}

	private void getIncomeReport(List<MovementDTO> offeringList,
			List<MovementDTO> titheList) {
		try {
			final JasperReport report = (JasperReport) JRLoader
					.loadObject(new File("/home/raul/ingresos.jasper"));
			final BigDecimal totalOffering = sumAmount(offeringList);
			LOG.debug("generateReport() totalOffering {}", totalOffering);

			final BigDecimal totalTithe = sumAmount(titheList);
			LOG.debug("generateReport() totalTithe {}", totalTithe);

			final Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("totalOffering", totalOffering);
			parameters.put("totalTithe", totalTithe);
			parameters.put("date", offeringList.get(0).getDate());
			parameters.put("total", totalOffering.add(totalTithe));
			parameters.put("church", "Horeb");
			parameters.put("place", "Guadalupe");

			final JasperPrint jasperPrint = JasperFillManager.fillReport(
					report, parameters);
			JasperExportManager.exportReportToPdfFile(jasperPrint, "/home/raul/ingresos.pdf");

		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		}
	}
	
	
	private void getExpenseReport(List<MovementDTO> expenseList) {
		try {
			final JasperReport report = (JasperReport) JRLoader
					.loadObject(new File("/home/raul/egresos.jasper"));
			final BigDecimal totalExpense = sumAmount(expenseList);
			LOG.debug("generateReport() totalExpense {}", totalExpense);
			
			final List<MovementDTO> maintenanceList = getMaintenanceList(expenseList);
			final BigDecimal totalMaintenance = sumAmount(maintenanceList);
			
			final List<MovementDTO> conservationList = getConservationList(expenseList);
			final BigDecimal totalConservation = sumAmount(conservationList);
			
			final List<MovementDTO> waterList = getWaterList(expenseList);
			final BigDecimal totalWater = sumAmount(waterList);

			final List<MovementDTO> lightList = getLightList(expenseList);
			final BigDecimal totalLight = sumAmount(lightList);
			
			final List<MovementDTO> paperList = getPaperList(expenseList);
			final BigDecimal totalPaper = sumAmount(paperList);
			
			final List<MovementDTO> transportList = getTransportList(expenseList);
			final BigDecimal totalTransport = sumAmount(transportList);
			
			final List<MovementDTO> foodList = getFoodList(expenseList);
			final BigDecimal totalFood = sumAmount(foodList);
			
			final List<MovementDTO> otherList = new ArrayList<MovementDTO>(expenseList);
			otherList.removeAll(maintenanceList);
			otherList.removeAll(conservationList);
			otherList.removeAll(waterList);
			otherList.removeAll(lightList);
			otherList.removeAll(paperList);
			otherList.removeAll(transportList);
			otherList.removeAll(foodList);
			final BigDecimal totalOther = sumAmount(otherList);

			final Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("totalMaintenance", totalMaintenance);
			parameters.put("totalConservation", totalConservation);
			parameters.put("totalWater", totalWater);
			parameters.put("totalLight", totalLight);
			parameters.put("totalPaper", totalPaper);
			parameters.put("totalTransport", totalTransport);
			parameters.put("totalFood", totalFood);
			parameters.put("totalOther", totalOther);
			parameters.put("church", "Horeb");
			parameters.put("place", "Xochimilco");

			
			parameters.put("date", expenseList.get(0).getDate());
			parameters.put("total", totalExpense);

			final JasperPrint jasperPrint = JasperFillManager.fillReport(
					report, parameters);
			JasperExportManager.exportReportToPdfFile(jasperPrint, "/home/raul/egresos.pdf");

		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		}
	}
	
	private void getInvoiceReport(List<MovementDTO> offeringList,
			List<MovementDTO> titheList, List<MovementDTO> expenseList) {
		try {
			final JasperReport report = (JasperReport) JRLoader
					.loadObject(new File("/home/raul/factura.jasper"));
			//final BigDecimal totalExpense = sumAmount(expenseList);
			//LOG.debug("generateReport() totalExpense {}", totalExpense);
			
			final List<MovementDTO> maintenanceList = getMaintenanceList(expenseList);
			final BigDecimal totalMaintenance = sumAmount(maintenanceList);
			
			final List<MovementDTO> conservationList = getConservationList(expenseList);
			final BigDecimal totalConservation = sumAmount(conservationList);
			
			final List<MovementDTO> waterList = getWaterList(expenseList);
			final BigDecimal totalWater = sumAmount(waterList);

			final List<MovementDTO> lightList = getLightList(expenseList);
			final BigDecimal totalLight = sumAmount(lightList);
			
			final List<MovementDTO> paperList = getPaperList(expenseList);
			final BigDecimal totalPaper = sumAmount(paperList);
			
			final List<MovementDTO> transportList = getTransportList(expenseList);
			final BigDecimal totalTransport = sumAmount(transportList);
			
			final List<MovementDTO> foodList = getFoodList(expenseList);
			final BigDecimal totalFood = sumAmount(foodList);
			
			final List<MovementDTO> expenseInvoiceList = new ArrayList<MovementDTO>();
			expenseInvoiceList.addAll(maintenanceList);
			expenseInvoiceList.addAll(conservationList);
			expenseInvoiceList.addAll(waterList);
			expenseInvoiceList.addAll(lightList);
			expenseInvoiceList.addAll(paperList);
			expenseInvoiceList.addAll(transportList);
			expenseInvoiceList.addAll(foodList);
			
			final BigDecimal totalExpense = sumAmount(expenseInvoiceList);
			final BigDecimal totalOffering = sumAmount(offeringList);
			final BigDecimal totalTithe = sumAmount(titheList);
			final BigDecimal totalIncome = totalOffering.add(totalTithe);
			




			final Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("totalMaintenance", totalMaintenance);
			parameters.put("totalConservation", totalConservation);
			parameters.put("totalWater", totalWater);
			parameters.put("totalLight", totalLight);
			parameters.put("totalPaper", totalPaper);
			parameters.put("totalTransport", totalTransport);
			parameters.put("totalFood", totalFood);
			parameters.put("totalExpense", totalExpense);
			parameters.put("totalOffering", totalOffering);
			parameters.put("totalTithe", totalTithe);
			parameters.put("totalIncome", totalIncome);
			
			final Map<Date, List<MovementDTO>> listByDayTithe = groupByDay(titheList);
			final Map<Date, List<MovementDTO>> listByDayOffering = groupByDay(offeringList);
			
			int i = 1;
			for (Map.Entry<Date, List<MovementDTO>> entry : listByDayTithe.entrySet()) {
				parameters.put(i + "MondayTithe", sumAmount(entry.getValue()));
				i++;
			}
			
			i = 1;
			for (Map.Entry<Date, List<MovementDTO>> entry : listByDayOffering.entrySet()) {
				parameters.put(i + "MondayOffering", sumAmount(entry.getValue()));
				i++;
			}

			
			parameters.put("date", expenseList.get(0).getDate());
			parameters.put("total", totalIncome.subtract(totalExpense)); 

			final JasperPrint jasperPrint = JasperFillManager.fillReport(
					report, parameters);
			JasperExportManager.exportReportToPdfFile(jasperPrint, "/home/raul/invoice.pdf");

		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		}
	}
	
	
	private void getNoInvoiceReport(List<MovementDTO> expenseList) {
		try {
			final JasperReport report = (JasperReport) JRLoader
					.loadObject(new File("/home/raul/sinfactura.jasper"));
			
			final List<MovementDTO> noInvoiceExpenseList = new ArrayList<MovementDTO>(expenseList);
			
			noInvoiceExpenseList.removeAll(getMaintenanceList(expenseList));
			noInvoiceExpenseList.removeAll(getConservationList(expenseList));
			noInvoiceExpenseList.removeAll(getWaterList(expenseList));
			noInvoiceExpenseList.removeAll(getLightList(expenseList));
			noInvoiceExpenseList.removeAll(getPaperList(expenseList));
			noInvoiceExpenseList.removeAll(getTransportList(expenseList));
			noInvoiceExpenseList.removeAll(getFoodList(expenseList));
			
			if (noInvoiceExpenseList.size() < 10) {
				int size = noInvoiceExpenseList.size();
				for (int i = 0; i < 10 - size; i++) {
					noInvoiceExpenseList.add(new MovementDTO());
				}
			}


			
			final JRBeanCollectionDataSource ds = new JRBeanCollectionDataSource(noInvoiceExpenseList);
			
			final Map<String, Object> parameters = new HashMap<String, Object>();							
			parameters.put("date", expenseList.get(0).getDate());

			final JasperPrint jasperPrint = JasperFillManager.fillReport(
					report, parameters, ds);
			JasperExportManager.exportReportToPdfFile(jasperPrint, "/home/raul/noinvoice.pdf");

		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		}
		
	}
	
	
	private void getMaintenanceReport(List<MovementDTO> maintenanceList) {
		try {
			final JasperReport report = (JasperReport) JRLoader
					.loadObject(new File("/home/raul/manutencion.jasper"));
			
			final Map<String, Object> parameters = new HashMap<String, Object>();	
			final Calendar calendar = GregorianCalendar.getInstance();
			calendar.setTime(maintenanceList.get(0).getDate());
			calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DATE));
			
			parameters.put("date", calendar.getTime());
			parameters.put("place", "Guadalupe");
			parameters.put("church", "Horeb");
			
			final BigDecimal total = sumAmount(maintenanceList);
			boolean generarOtro = false;
			if(new BigDecimal ("6000").compareTo(total) > 0) {
				parameters.put("total", total);
				parameters.put("cantidadLetra", NumberToLetterConverter.convertNumberToLetter(total.doubleValue()));

			} else {
				final BigDecimal maxAmount = new BigDecimal("6000");
				parameters.put("total", maxAmount);
				parameters.put("cantidadLetra", NumberToLetterConverter.convertNumberToLetter(maxAmount.doubleValue()));
				generarOtro = true;
			}
			parameters.put("name", "Ignacio Álvares");

			JasperPrint jasperPrint = JasperFillManager.fillReport(
					report, parameters);
			JasperExportManager.exportReportToPdfFile(jasperPrint, "/home/raul/manutencion.pdf");
			
			if(generarOtro) {
				parameters.put("name", "Gloria Adame");
				parameters.put("total", total.subtract(new BigDecimal(6000)));
				parameters.put("cantidadLetra", NumberToLetterConverter.convertNumberToLetter(total.subtract(new BigDecimal(6000)).doubleValue()));
				jasperPrint = JasperFillManager.fillReport(
						report, parameters);
				JasperExportManager.exportReportToPdfFile(jasperPrint, "/home/raul/manutencion2.pdf");
			}

		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		}
		
	}
	
	private void getCutReport(BigDecimal lastTotal, List<MovementDTO> incomeList,
			List<MovementDTO> expenseList) {
		try {
			final JasperReport report = (JasperReport) JRLoader
					.loadObject(new File("/home/raul/corte.jasper"));
			
			final Map<String, Object> parameters = new HashMap<String, Object>();	
			final Calendar calendar = GregorianCalendar.getInstance();
			calendar.setTime(incomeList.get(0).getDate());
			calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DATE));
			
			parameters.put("date", calendar.getTime());
			calendar.set(Calendar.DATE, 0);
			parameters.put("lastDate", calendar.getTime());
			parameters.put("lastTotal", lastTotal);
			parameters.put("totalExpense", sumAmount(expenseList));
			parameters.put("totalIncome", sumAmount(incomeList));
			parameters.put("place", "Guadalupe");
			parameters.put("church", "Horeb");


			final JasperPrint jasperPrint = JasperFillManager.fillReport(
					report, parameters);
			JasperExportManager.exportReportToPdfFile(jasperPrint, "/home/raul/corte.pdf");
			


		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		}
		
	}



	private Map<Date, List<MovementDTO>> groupByDay(
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

	private List<MovementDTO> getMaintenanceList(List<MovementDTO> listofMovement) {
		final List<MovementDTO> result = new ArrayList<MovementDTO>();
		for (MovementDTO movementDTO : listofMovement) {
			if (movementDTO.getCategory().equals("Manutención") || movementDTO.getCategory().equals("Ayuda Transporte") || movementDTO.getCategory().equals("Amor")) {
				result.add(movementDTO);
			}
		}
		return result;
	}
	
	private List<MovementDTO> getConservationList(List<MovementDTO> listofMovement) {
		final List<MovementDTO> result = new ArrayList<MovementDTO>();
		for (MovementDTO movementDTO : listofMovement) {
			if (movementDTO.getCategory().equals("Conservación")) {
				result.add(movementDTO);
			}
		}
		return result;
	}
	
	private List<MovementDTO> getWaterList(List<MovementDTO> listofMovement) {
		final List<MovementDTO> result = new ArrayList<MovementDTO>();
		for (MovementDTO movementDTO : listofMovement) {
			if (movementDTO.getCategory().equals("Agua") || movementDTO.getCategory().equals("Agua Templo")
					|| movementDTO.getCategory().equals("Agua Casa Pastoral")) {
				result.add(movementDTO);
			}
		}
		return result;
	}
	
	private List<MovementDTO> getLightList(List<MovementDTO> listofMovement) {
		final List<MovementDTO> result = new ArrayList<MovementDTO>();
		for (MovementDTO movementDTO : listofMovement) {
			if (movementDTO.getCategory().equals("Luz") || movementDTO.getCategory().equals("Luz Templo")
					|| movementDTO.getCategory().equals("Luz Casa Pastoral")) {
				result.add(movementDTO);
			}
		}
		return result;
	}
	
	private List<MovementDTO> getPaperList(List<MovementDTO> listofMovement) {
		final List<MovementDTO> result = new ArrayList<MovementDTO>();
		for (MovementDTO movementDTO : listofMovement) {
			if (movementDTO.getCategory().equals("Copias") || movementDTO.getCategory().equals("Papelería")) {
				result.add(movementDTO);
			}
		}
		return result;
	}
	
	private List<MovementDTO> getTransportList(List<MovementDTO> listofMovement) {
		final List<MovementDTO> result = new ArrayList<MovementDTO>();
		for (MovementDTO movementDTO : listofMovement) { //TODO revisar que coincida las categorías
			if (movementDTO.getCategory().equals("Transportación")) {
				result.add(movementDTO);
			}
		}
		return result;
	}
	
	private List<MovementDTO> getFoodList(List<MovementDTO> listofMovement) {
		final List<MovementDTO> result = new ArrayList<MovementDTO>();
		for (MovementDTO movementDTO : listofMovement) { //TODO revisar que coincida las categorías
			if (movementDTO.getCategory().equals("Refrigerio")) {
				result.add(movementDTO);
			}
		}
		return result;
	}


}
