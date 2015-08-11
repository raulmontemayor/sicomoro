package org.iemm.sicomoro.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;

import org.iemm.sicomoro.db.dto.MovementDTO;
import org.iemm.sicomoro.util.NumberToLetterConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReportService {
	
	private Logger LOG = LoggerFactory.getLogger(ReportService.class);
	private MovementService movementService = new MovementService();

	public void getReports(BigDecimal lastTotal, List<MovementDTO> listofMovement) {

		final List<MovementDTO> incomeList = movementService.getIncomeList(listofMovement);
		final BigDecimal totalIncome = movementService.sumAmount(incomeList);
		LOG.debug("generateReport() totalIncome {}", totalIncome);
		final List<MovementDTO> offeringList = movementService.getOfferingList(incomeList);

		final List<MovementDTO> titheList = movementService.getTitheList(incomeList);

		getIncomeReport(offeringList, titheList);

		final List<MovementDTO> taxList = movementService.getTaxList(incomeList);
		listofMovement.addAll(taxList);

		final List<MovementDTO> expenseList = movementService.getExpenseList(listofMovement);
		LOG.debug("--generateReport() expenseList {} ", expenseList);

		getExpenseReport(expenseList);

		getInvoiceReport(offeringList, titheList, expenseList);

		getNoInvoiceReport(expenseList);

		getMaintenanceReport(movementService.getMaintenanceList(expenseList));

		getCutReport(lastTotal, incomeList, expenseList);

	}
	
	private void getIncomeReport(List<MovementDTO> offeringList,
			List<MovementDTO> titheList) {
		try {
			final JasperReport report = (JasperReport) JRLoader
					.loadObject(ClassLoader.getSystemResourceAsStream("ingresos.jasper"));
			final BigDecimal totalOffering = movementService.sumAmount(offeringList);
			LOG.debug("generateReport() totalOffering {}", totalOffering);

			final BigDecimal totalTithe = movementService.sumAmount(titheList);
			LOG.debug("generateReport() totalTithe {}", totalTithe);

			final Map<String, Object> parameters = getDefaultParams();
			parameters.put("totalOffering", totalOffering);
			parameters.put("totalTithe", totalTithe);
			parameters.put("date", offeringList.get(0).getDate());
			parameters.put("total", totalOffering.add(totalTithe));

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
					.loadObject(ClassLoader.getSystemResourceAsStream("egresos.jasper"));
			final BigDecimal totalExpense = movementService.sumAmount(expenseList);
			LOG.debug("generateReport() totalExpense {}", totalExpense);
			
			final List<MovementDTO> maintenanceList = movementService.getMaintenanceList(expenseList);
			final BigDecimal totalMaintenance = movementService.sumAmount(maintenanceList);
			
			final List<MovementDTO> conservationList = movementService.getConservationList(expenseList);
			final BigDecimal totalConservation = movementService.sumAmount(conservationList);
			
			final List<MovementDTO> waterList = movementService.getWaterList(expenseList);
			final BigDecimal totalWater = movementService.sumAmount(waterList);

			final List<MovementDTO> lightList = movementService.getLightList(expenseList);
			final BigDecimal totalLight = movementService.sumAmount(lightList);
			
			final List<MovementDTO> paperList = movementService.getPaperList(expenseList);
			final BigDecimal totalPaper = movementService.sumAmount(paperList);
			
			final List<MovementDTO> transportList = movementService.getTransportList(expenseList);
			final BigDecimal totalTransport = movementService.sumAmount(transportList);
			
			final List<MovementDTO> foodList = movementService.getFoodList(expenseList);
			final BigDecimal totalFood = movementService.sumAmount(foodList);
			
			final List<MovementDTO> otherList = new ArrayList<MovementDTO>(expenseList);
			otherList.removeAll(maintenanceList);
			otherList.removeAll(conservationList);
			otherList.removeAll(waterList);
			otherList.removeAll(lightList);
			otherList.removeAll(paperList);
			otherList.removeAll(transportList);
			otherList.removeAll(foodList);
			final BigDecimal totalOther = movementService.sumAmount(otherList);

			final Map<String, Object> parameters = getDefaultParams();
			parameters.put("totalMaintenance", totalMaintenance);
			parameters.put("totalConservation", totalConservation);
			parameters.put("totalWater", totalWater);
			parameters.put("totalLight", totalLight);
			parameters.put("totalPaper", totalPaper);
			parameters.put("totalTransport", totalTransport);
			parameters.put("totalFood", totalFood);
			parameters.put("totalOther", totalOther);
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
					.loadObject(ClassLoader.getSystemResourceAsStream("factura.jasper"));
			//final BigDecimal totalExpense = sumAmount(expenseList);
			//LOG.debug("generateReport() totalExpense {}", totalExpense);
			
			final List<MovementDTO> maintenanceList = movementService.getMaintenanceList(expenseList);
			final BigDecimal totalMaintenance = movementService.sumAmount(maintenanceList);
			
			final List<MovementDTO> conservationList = movementService.getConservationList(expenseList);
			final BigDecimal totalConservation = movementService.sumAmount(conservationList);
			
			final List<MovementDTO> waterList = movementService.getWaterList(expenseList);
			final BigDecimal totalWater = movementService.sumAmount(waterList);

			final List<MovementDTO> lightList = movementService.getLightList(expenseList);
			final BigDecimal totalLight = movementService.sumAmount(lightList);
			
			final List<MovementDTO> paperList = movementService.getPaperList(expenseList);
			final BigDecimal totalPaper = movementService.sumAmount(paperList);
			
			final List<MovementDTO> transportList = movementService.getTransportList(expenseList);
			final BigDecimal totalTransport = movementService.sumAmount(transportList);
			
			final List<MovementDTO> foodList = movementService.getFoodList(expenseList);
			final BigDecimal totalFood = movementService.sumAmount(foodList);
			
			final List<MovementDTO> expenseInvoiceList = new ArrayList<MovementDTO>();
			expenseInvoiceList.addAll(maintenanceList);
			expenseInvoiceList.addAll(conservationList);
			expenseInvoiceList.addAll(waterList);
			expenseInvoiceList.addAll(lightList);
			expenseInvoiceList.addAll(paperList);
			expenseInvoiceList.addAll(transportList);
			expenseInvoiceList.addAll(foodList);
			
			final BigDecimal totalExpense = movementService.sumAmount(expenseInvoiceList);
			final BigDecimal totalOffering = movementService.sumAmount(offeringList);
			final BigDecimal totalTithe = movementService.sumAmount(titheList);
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
			
			final Map<Date, List<MovementDTO>> listByDayTithe = movementService.groupByDay(titheList);
			final Map<Date, List<MovementDTO>> listByDayOffering = movementService.groupByDay(offeringList);
			
			int i = 1;
			for (Map.Entry<Date, List<MovementDTO>> entry : listByDayTithe.entrySet()) {
				parameters.put(i + "MondayTithe", movementService.sumAmount(entry.getValue()));
				i++;
			}
			
			i = 1;
			for (Map.Entry<Date, List<MovementDTO>> entry : listByDayOffering.entrySet()) {
				parameters.put(i + "MondayOffering", movementService.sumAmount(entry.getValue()));
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
					.loadObject(ClassLoader.getSystemResourceAsStream("sinfactura.jasper"));
			
			final List<MovementDTO> noInvoiceExpenseList = new ArrayList<MovementDTO>(expenseList);
			
			noInvoiceExpenseList.removeAll(movementService.getMaintenanceList(expenseList));
			noInvoiceExpenseList.removeAll(movementService.getConservationList(expenseList));
			noInvoiceExpenseList.removeAll(movementService.getWaterList(expenseList));
			noInvoiceExpenseList.removeAll(movementService.getLightList(expenseList));
			noInvoiceExpenseList.removeAll(movementService.getPaperList(expenseList));
			noInvoiceExpenseList.removeAll(movementService.getTransportList(expenseList));
			noInvoiceExpenseList.removeAll(movementService.getFoodList(expenseList));
			
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
					.loadObject(ClassLoader.getSystemResourceAsStream("manutencion.jasper"));
			
			final Map<String, Object> parameters = new HashMap<String, Object>();	
			final Calendar calendar = GregorianCalendar.getInstance();
			calendar.setTime(maintenanceList.get(0).getDate());
			calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DATE));
			
			parameters.put("date", calendar.getTime());
			
			final BigDecimal total = movementService.sumAmount(maintenanceList);
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
			parameters.put("name", ConfigService.getConfig().getString("minister.first"));

			JasperPrint jasperPrint = JasperFillManager.fillReport(
					report, parameters);
			JasperExportManager.exportReportToPdfFile(jasperPrint, "/home/raul/manutencion.pdf");
			
			if(generarOtro) {
				parameters.put("name", ConfigService.getConfig().getString("minister.second"));
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
					.loadObject(ClassLoader.getSystemResourceAsStream("corte.jasper"));
			
			final Map<String, Object> parameters = new HashMap<String, Object>();	
			final Calendar calendar = GregorianCalendar.getInstance();
			calendar.setTime(incomeList.get(0).getDate());
			calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DATE));
			
			parameters.put("date", calendar.getTime());
			calendar.set(Calendar.DATE, 0);
			parameters.put("lastDate", calendar.getTime());
			parameters.put("lastTotal", lastTotal);
			parameters.put("totalExpense", movementService.sumAmount(expenseList));
			parameters.put("totalIncome", movementService.sumAmount(incomeList));
			
			
			final BigDecimal total = movementService.sumAmount(incomeList)
					.subtract(movementService.sumAmount(expenseList))
					.add(lastTotal);
			ConfigService.saveConfig("last.total", total);


			final JasperPrint jasperPrint = JasperFillManager.fillReport(
					report, parameters);
			JasperExportManager.exportReportToPdfFile(jasperPrint, "/home/raul/corte.pdf");
			


		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		}
		
	}
	
	private Map<String, Object> getDefaultParams() {
		final Map<String, Object> result = new HashMap<String, Object>();
		result.put("church", ConfigService.getConfig().getString("location.name"));
		result.put("place", ConfigService.getConfig().getString("location.city"));
		return result;
	}




}
