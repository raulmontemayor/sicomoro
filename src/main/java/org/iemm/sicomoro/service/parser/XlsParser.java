package org.iemm.sicomoro.service.parser;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.iemm.sicomoro.db.dto.MovementDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XlsParser extends AbstractParser implements Parser {

	private Logger LOG = LoggerFactory.getLogger(XlsParser.class);

	public List<MovementDTO> parse(File theFile) {
		try {

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
							movement.setDate(getDate(cell.getStringCellValue()));
							break;
						case 2:
							movement.setType(cell.getStringCellValue().trim());
							break;
						case 3:
							movement.setCategory(cell.getStringCellValue()
									.trim());
							break;
						case 4:
							movement.setAmount(new BigDecimal(cell
									.getNumericCellValue()).abs());
							break;
						case 6:
							movement.setNotes(cell.getStringCellValue().trim());
							movement.setInvoice(cell.getStringCellValue()
									.indexOf("#invoice") != -1);
							break;
						default:
							break;
						}
						j++;
					}
					if (movement.getCategory().equals("Otros")) {
						movement.setCategory(movement.getNotes().replace(
								"#invoice", ""));
					}
					listOfMovements.add(movement);
				}
				i++;
			}

			return listOfMovements;

		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return null;
		}
	}


}
