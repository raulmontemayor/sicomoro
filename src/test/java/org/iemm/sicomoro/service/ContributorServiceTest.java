package org.iemm.sicomoro.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.iemm.sicomoro.db.client.ContributorMapper;
import org.iemm.sicomoro.db.dao.Contributor;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ContributorServiceTest {

	final static private Logger LOG = LoggerFactory
			.getLogger(ContributorServiceTest.class);

	private ContributorService instance;
	private ContributorMapper contributorMapper;

	@Before
	public void setUp() throws Exception {
		instance = new ContributorService();
		contributorMapper = mock(ContributorMapper.class);
		instance.setContributorMapper(contributorMapper);
	}

	@Test
	public void testSave() {
		LOG.trace("testSave");
		final Contributor contributor = new Contributor();
		instance.save(contributor);

		verify(contributorMapper).insert(contributor);
		assertNotNull(contributor.getCreateDate());
		assertNotNull(contributor.getUpdateDate());
	}

	@Test
	public void testSaveUpdate() {
		LOG.trace("testSaveUpdate");
		final Contributor contributor = new Contributor();
		contributor.setIdContributor(Integer.valueOf(78));
		instance.save(contributor);
		verify(contributorMapper).updateByPrimaryKeySelective(contributor);
		assertNull(contributor.getCreateDate());
		assertNotNull(contributor.getUpdateDate());
	}

	@Test
	public void testFind() {
		LOG.trace("testFind");
		final List<Contributor> expected = new ArrayList<Contributor>();
		when(contributorMapper.selectByExample(null)).thenReturn(expected);
		final List<Contributor> result = instance.getAll();
		verify(contributorMapper).selectByExample(null);
		assertEquals(expected, result);
	}
	
	@Test
	public void testGetContributorMapper() {
		LOG.trace("testGetContributorMapper");
		assertEquals(instance.getContributorMapper(), contributorMapper);
	}
	
	@Test
	public void testSetContributorMapper() {
		LOG.trace("testSetContributorMapper");
		assertEquals(instance.getContributorMapper(), contributorMapper);
	}

}
