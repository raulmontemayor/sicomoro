package org.iemm.sicomoro.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.time.DateUtils;
import org.iemm.sicomoro.db.client.MovementCutMapper;
import org.iemm.sicomoro.db.client.MovementMapper;
import org.iemm.sicomoro.db.client.MovementTypeMapper;
import org.iemm.sicomoro.db.dao.Movement;
import org.iemm.sicomoro.db.dao.MovementCut;
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
	private MovementTypeMapper movementTypeMapper;
	private MovementCutService movementCutService;

	@Before
	public void setUp() throws Exception {
		instance = new MovementService();
		movementMapper = mock(MovementMapper.class);
		instance.setMovementMapper(movementMapper);
		movementCutService = mock(MovementCutService.class);
		instance.setMovementCutService(movementCutService);
		movementTypeMapper = mock(MovementTypeMapper.class);
		instance.setMovementTypeMapper(movementTypeMapper);
	}
	
	@Test
	public void testGetMovementMapper() {
		LOG.trace("testGetMovementMapper");
		assertEquals(movementMapper, instance.getMovementMapper());
	}
	
	@Test
	public void testGetMovementCutService() {
		LOG.trace("testGetMovementCutService");
		assertEquals(movementCutService, instance.getMovementCutService());
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
		final Date movementDate = new Date();
		
		final List<MovementType> lstMovementTypes = new ArrayList<MovementType>();
		lstMovementTypes.add(new MovementType());
		when(movementTypeMapper.selectByExample(any(MovementTypeExample.class))).thenReturn(lstMovementTypes);
		when(movementMapper.insert(any(Movement.class))).thenReturn(50);
		int result = instance.createMovement(amount, idContributor, movementDate, null, movementType);
		assertEquals(50, result);
	}
	
	@Test(expected = BussinesLogicException.class)
	public void testCreateMovementWhitCutDateBefore() throws BussinesLogicException {
		LOG.trace("testCreateMovement");
		
		final BigDecimal amount = BigDecimal.valueOf(45.50);
		final Integer idContributor = null;
		final MovementTypeE movementType = MovementTypeE.UP;
		final Date movementDate = new Date();
		
		final List<MovementType> lstMovementTypes = new ArrayList<MovementType>();
		lstMovementTypes.add(new MovementType());
		when(movementTypeMapper.selectByExample(any(MovementTypeExample.class))).thenReturn(lstMovementTypes);
		when(movementMapper.insert(any(Movement.class))).thenReturn(50);
		final MovementCut lastMovement = new MovementCut();
		lastMovement.setCutDate(DateUtils.addDays(movementDate, 1));
		when(movementCutService.getLastMovementCut()).thenReturn(lastMovement);
		
		instance.createMovement(amount, idContributor, movementDate, null, movementType);
		fail("Debio haber mandado una exepcion de logica de negocios porque" +
				" la fecha del ultimo corte es mayor a la fecha del movimiento");
	}
	
	@Test
	public void testCreateMovementTithe() throws BussinesLogicException {
		LOG.trace("testCreateMovementTithe");
		
		final BigDecimal amount = BigDecimal.valueOf(45.50);
		final Integer idContributor = Integer.valueOf(90);
		final MovementTypeE movementType = MovementTypeE.TITHE;
		final Date movementDate = new Date();
		
		final List<MovementType> lstMovementTypes = new ArrayList<MovementType>();
		lstMovementTypes.add(new MovementType());
		when(movementTypeMapper.selectByExample(any(MovementTypeExample.class))).thenReturn(lstMovementTypes);
		when(movementMapper.insert(any(Movement.class))).thenReturn(50);
		int result = instance.createMovement(amount, idContributor, movementDate, null, movementType);
		assertEquals(50, result);
	}
	
	@Test(expected = BussinesLogicException.class)
	public void testCreateMovementTitheWithoutContributor() throws BussinesLogicException {
		LOG.trace("testCreateMovementTitheWithoutContributor");
		final BigDecimal amount = BigDecimal.valueOf(45.50);
		final Integer idContributor = null;
		final MovementTypeE movementType = MovementTypeE.TITHE;
		final Date movementDate = new Date();
		instance.createMovement(amount, idContributor, movementDate, null, movementType);
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
