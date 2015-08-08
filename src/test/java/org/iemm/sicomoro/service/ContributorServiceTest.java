package org.iemm.sicomoro.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;

import java.util.ArrayList;
import java.util.List;

import org.iemm.sicomoro.db.client.ContributorMapper;
import org.iemm.sicomoro.db.dao.Contributor;
import org.iemm.sicomoro.db.dao.ContributorExample;
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
	public void testGetAll() {
		LOG.trace("testGetAll");
		final List<Contributor> expected = new ArrayList<Contributor>();
		when(contributorMapper.selectByExample(any(ContributorExample.class))).thenReturn(expected);
		final List<Contributor> result = instance.getAll();
		verify(contributorMapper).selectByExample(any(ContributorExample.class));
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
	
	@Test
	public void testFind() {
		LOG.trace("testFind");
		final Integer idContributor = Integer.valueOf(554);
		final Contributor expected = new Contributor();
		when(contributorMapper.selectByPrimaryKey(idContributor)).thenReturn(expected);
		assertEquals(expected, instance.find(idContributor));
	}
	
	@Test
	public void testDelete() {
		LOG.trace("testDelete");
		final Integer idContributor = Integer.valueOf(554);
		final int expected = 44;
		when(contributorMapper.deleteByPrimaryKey(idContributor)).thenReturn(expected);
		assertEquals(expected, instance.delete(idContributor));
	}
}
