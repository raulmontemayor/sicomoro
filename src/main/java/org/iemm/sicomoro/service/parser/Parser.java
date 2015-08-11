package org.iemm.sicomoro.service.parser;

import java.io.File;
import java.util.List;

import org.iemm.sicomoro.db.dto.MovementDTO;

public interface Parser {
	
	public List<MovementDTO> parse(File theFile);

}
