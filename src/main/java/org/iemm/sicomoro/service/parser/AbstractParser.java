package org.iemm.sicomoro.service.parser;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.iemm.sicomoro.service.ConfigService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractParser implements Parser {
	private Logger LOG = LoggerFactory.getLogger(AbstractParser.class);
	
	protected Date getDate(String theDate) {
		final SimpleDateFormat sdf = new SimpleDateFormat(ConfigService
				.getConfig().getString("date.format"));
		try {
			return sdf.parse(theDate);
		} catch (ParseException e) {
			LOG.error(e.getMessage(), e);
			return null;
		}
	}
}
