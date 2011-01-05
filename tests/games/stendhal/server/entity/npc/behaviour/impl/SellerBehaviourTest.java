/* $Id$ */
/***************************************************************************
 *                   (C) Copyright 2003-2010 - Stendhal                    *
 ***************************************************************************
 ***************************************************************************
 *                                                                         *
 *   This program is free software; you can redistribute it and/or modify  *
 *   it under the terms of the GNU General Public License as published by  *
 *   the Free Software Foundation; either version 2 of the License, or     *
 *   (at your option) any later version.                                   *
 *                                                                         *
 ***************************************************************************/
package games.stendhal.server.entity.npc.behaviour.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static utilities.SpeakerNPCTestHelper.getReply;
import games.stendhal.server.core.engine.RPClassGenerator;
import games.stendhal.server.entity.npc.SpeakerNPC;
import games.stendhal.server.entity.npc.behaviour.adder.SellerAdder;
import games.stendhal.server.entity.player.Player;

import java.util.HashMap;
import java.util.Map;

import org.junit.BeforeClass;
import org.junit.Test;

import utilities.PlayerTestHelper;

public class SellerBehaviourTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		new RPClassGenerator().createRPClassesWithoutBaking(); // for Player, NPC and TradeStateChangeEvent RPClasses
	}

	/**
	 * Tests for sellerBehaviour.
	 */
	@Test
	public void testSellerBehaviour() {
		final SellerBehaviour sb = new SellerBehaviour();
		assertTrue(sb.dealtItems().isEmpty());
		assertEquals(sb.amount, 0);
		assertNull(sb.chosenItemName);
		assertTrue(sb.itemNames.isEmpty());
		assertTrue(sb.priceList.isEmpty());
	}

	/**
	 * Tests for sellerBehaviourMapOfStringInteger.
	 */
	@Test
	public void testSellerBehaviourMapOfStringInteger() {

		final Map<String, Integer> pricelist = new HashMap<String, Integer>();
		SellerBehaviour sb = new SellerBehaviour(pricelist);
		assertTrue(sb.dealtItems().isEmpty());
		assertEquals(sb.amount, 0);
		assertNull(sb.chosenItemName);
		assertTrue(sb.itemNames.isEmpty());
		assertTrue(sb.priceList.isEmpty());

		pricelist.put("item1", 10);
		pricelist.put("item2", 20);

		sb = new SellerBehaviour(pricelist);
		assertEquals(sb.dealtItems().size(), 2);
		assertTrue(sb.dealtItems().contains("item1"));
		assertTrue(sb.dealtItems().contains("item2"));
		assertEquals(sb.amount, 0);
		assertNull(sb.chosenItemName);
	}
	
	/**
	 * Tests for bottlesGlasses.
	 */
	@Test
	public void testBottlesGlasses() {
		final Map<String, Integer> pricelist = new HashMap<String, Integer>();
		pricelist.put("dingo", 3);
		final SellerBehaviour sb = new SellerBehaviour(pricelist);
		final SpeakerNPC npc = new SpeakerNPC("npc");
		npc.addGreeting("blabla");
		new SellerAdder().addSeller(npc, sb);
	    final Player player = PlayerTestHelper.createPlayer("bob");
	    
	    npc.getEngine().step(player, "hi");
	    npc.getEngine().step(player, "buy 1 potion");
		assertEquals("Sorry, I don't sell bottles of potion.", getReply(npc));

	    npc.getEngine().step(player, "buy wine");
		assertEquals("Sorry, I don't sell glasses of wine.", getReply(npc));

	    npc.getEngine().step(player, "buy 1 glass of wine");
		assertEquals("Sorry, I don't sell glasses of wine.", getReply(npc));

	    npc.getEngine().step(player, "buy 1 bottle of wine");
		assertEquals("Sorry, I don't sell glasses of wine.", getReply(npc));
	}

	/**
	 * Tests for selling scrolls.
	 */
	@Test
	public void testScrolls() {
		final Map<String, Integer> pricelist = new HashMap<String, Integer>();
		pricelist.put("fado city scroll", 1000);
		pricelist.put("empty scroll", 3000);
		final SellerBehaviour sb = new SellerBehaviour(pricelist);
		final SpeakerNPC npc = new SpeakerNPC("npc");
		npc.addGreeting("Hello!");
		new SellerAdder().addSeller(npc, sb);
	    final Player player = PlayerTestHelper.createPlayer("bob");

	    npc.getEngine().step(player, "hi");
		assertEquals("Hello!", getReply(npc));

	    npc.getEngine().step(player, "buy fado city scroll");
		assertEquals("A fado city scroll will cost 1000. Do you want to buy it?", getReply(npc));
	    npc.getEngine().step(player, "no");

	    npc.getEngine().step(player, "buy two empty scrolls");
		assertEquals("2 empty scrolls will cost 6000. Do you want to buy them?", getReply(npc));
	    npc.getEngine().step(player, "no");
		assertEquals("Ok, how else may I help you?", getReply(npc));

	    npc.getEngine().step(player, "buy scroll");
		assertEquals("Please specify which sort of scroll you want to buy.", getReply(npc));

	    npc.getEngine().step(player, "buy anything-else");
		assertEquals("Sorry, I don't sell anything-elses.", getReply(npc));
	}

}
