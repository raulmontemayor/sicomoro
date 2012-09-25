package org.iemm.sicomoro.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.iemm.sicomoro.db.client.CatalogMapper;
import org.iemm.sicomoro.db.dao.Catalog;
import org.iemm.sicomoro.db.dao.CatalogExample;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CatalogServiceTest {
	
	final static private Logger LOG = LoggerFactory
			.getLogger(ContributorServiceTest.class);

	private CatalogService instance;
	/* Campos de la instancia */
	private CatalogMapper catalogMapper;
	
	@Before
	public void setUp() throws Exception {
		instance = new CatalogService();
		catalogMapper = mock(CatalogMapper.class);
		instance.setCatalogMapper(catalogMapper);
	}
	
	@Test
	public void testGetCatalog() {
		LOG.trace("testGetCatalog");
		final String group = "group";
		final String name = "name";
		
		final List<Catalog> lstCatalog = new ArrayList<Catalog>();
		final Catalog catalog = new Catalog();
		catalog.setValue("expected");
		lstCatalog.add(catalog);
		when(catalogMapper.selectByExample(any(CatalogExample.class)))
				.thenReturn(lstCatalog);
		
		final String result = instance.getCatalog(group, name);
		assertEquals("expected", result);
	}
	
	@Test
	public void testGetCatalogMapper() {
		LOG.trace("testGetCatalogMapper");
		assertEquals(catalogMapper, instance.getCatalogMapper());
	}

}
