package org.iemm.sicomoro.service;

import java.io.File;
import java.util.List;

import org.iemm.sicomoro.db.dto.MovementDTO;
import org.iemm.sicomoro.service.parser.Parser;
import org.iemm.sicomoro.service.parser.XlsParser;


public class ParseFileService {
	
	public List<MovementDTO> parse(File theFile){
		Parser parser = null;
		if(theFile.getName().endsWith("xls") || theFile.getName().endsWith("xlsx")) {
			parser = new XlsParser();
		}
		return parser.parse(theFile);
	}

}
