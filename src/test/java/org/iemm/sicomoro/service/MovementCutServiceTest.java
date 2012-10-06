package org.iemm.sicomoro.service;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.iemm.sicomoro.db.client.MovementCutMapper;
import org.iemm.sicomoro.db.dao.MovementCut;
import org.iemm.sicomoro.db.dao.MovementCutExample;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MovementCutServiceTest {
	
	final static private Logger LOG = LoggerFactory
			.getLogger(MovementCutServiceTest.class);
	
	private MovementCutService instance;
	private MovementCutMapper movementCutMapper;
	
	@Before
	public void setUp() {
		instance = new MovementCutService();
		movementCutMapper = mock(MovementCutMapper.class);
		instance.setMovementCutMapper(movementCutMapper);
	}

	@Test
	public void testGetLastCut() {
		LOG.trace("testGetLastCut");
		final List<MovementCut> movements = new ArrayList<MovementCut>();
		final MovementCut expected = new MovementCut();
		movements.add(expected);
		when(movementCutMapper.selectByExample(any(MovementCutExample.class)))
				.thenReturn(movements);
		final MovementCut result = instance.getLastMovementCut();
		assertEquals(expected, result);
	}
	
	@Test
	public void testSetMovementCutMapper() {
		assertEquals(movementCutMapper, instance.getMovementCutMapper());
	}

}
