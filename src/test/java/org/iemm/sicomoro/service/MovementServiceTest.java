package org.iemm.sicomoro.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.iemm.sicomoro.db.client.MovementCutMapper;
import org.iemm.sicomoro.db.client.MovementMapper;
import org.iemm.sicomoro.db.client.MovementTypeMapper;
import org.iemm.sicomoro.db.dao.Movement;
import org.iemm.sicomoro.db.dao.MovementType;
import org.iemm.sicomoro.db.dao.MovementTypeExample;
import org.iemm.sicomoro.exception.BussinesLogicException;
import org.iemm.sicomoro.service.MovementService.MovementTypeE;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MovementServiceTest {
	
	final static private Logger LOG = LoggerFactory
			.getLogger(MovementServiceTest.class);
	
	/** Instancia a probar */
	private MovementService instance;
	
	/* Campos de la instancia a probar */
	private MovementMapper movementMapper;
	private MovementCutMapper movementCutMapper;
	private MovementTypeMapper movementTypeMapper;

	@Before
	public void setUp() throws Exception {
		instance = new MovementService();
		movementMapper = mock(MovementMapper.class);
		instance.setMovementMapper(movementMapper);
		movementCutMapper = mock(MovementCutMapper.class);
		instance.setMovementCutMapper(movementCutMapper);
		movementTypeMapper = mock(MovementTypeMapper.class);
		instance.setMovementTypeMapper(movementTypeMapper);
	}
	
	@Test
	public void testGetMovementMapper() {
		LOG.trace("testGetMovementMapper");
		assertEquals(movementMapper, instance.getMovementMapper());
	}
	
	@Test
	public void testGetMovementCutMapper() {
		LOG.trace("testGetMovementCutMapper");
		assertEquals(movementCutMapper, instance.getMovementCutMapper());
	}
	
	@Test
	public void testGetMovementTypeMapper() {
		LOG.trace("testGetMovementTypeMapper");
		assertEquals(movementTypeMapper, instance.getMovementTypeMapper());
	}
	
	@Test
	public void testCreateMovement() throws BussinesLogicException {
		LOG.trace("testCreateMovement");
		
		final BigDecimal amount = BigDecimal.valueOf(45.50);
		final Integer idContributor = null;
		final MovementTypeE movementType = MovementTypeE.UP;
		
		final List<MovementType> lstMovementTypes = new ArrayList<MovementType>();
		lstMovementTypes.add(new MovementType());
		when(movementTypeMapper.selectByExample(any(MovementTypeExample.class))).thenReturn(lstMovementTypes);
		when(movementMapper.insert(any(Movement.class))).thenReturn(50);
		int result = instance.createMovement(amount, idContributor, movementType);
		assertEquals(50, result);
	}
	
	@Test
	public void testCreateMovementTithe() throws BussinesLogicException {
		LOG.trace("testCreateMovementTithe");
		
		final BigDecimal amount = BigDecimal.valueOf(45.50);
		final Integer idContributor = Integer.valueOf(90);
		final MovementTypeE movementType = MovementTypeE.TITHE;
		
		final List<MovementType> lstMovementTypes = new ArrayList<MovementType>();
		lstMovementTypes.add(new MovementType());
		when(movementTypeMapper.selectByExample(any(MovementTypeExample.class))).thenReturn(lstMovementTypes);
		when(movementMapper.insert(any(Movement.class))).thenReturn(50);
		int result = instance.createMovement(amount, idContributor, movementType);
		assertEquals(50, result);
	}
	
	@Test(expected = BussinesLogicException.class)
	public void testCreateMovementTitheWithoutContributor() throws BussinesLogicException {
		LOG.trace("testCreateMovementTitheWithoutContributor");
		final BigDecimal amount = BigDecimal.valueOf(45.50);
		final Integer idContributor = null;
		final MovementTypeE movementType = MovementTypeE.TITHE;
		instance.createMovement(amount, idContributor, movementType);
		fail("BussinesLogicException expected");
	}
	
	@Test
	public void testFind() {
		LOG.trace("testFind");
		final Integer idMovement = Integer.valueOf(51);
		final Movement expected = new Movement();
		when(movementMapper.selectByPrimaryKey(idMovement)).thenReturn(expected);
		assertEquals(expected, instance.find(idMovement));

	}

}
