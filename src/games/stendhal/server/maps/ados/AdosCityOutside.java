package games.stendhal.server.maps.ados;

import games.stendhal.common.Direction;
import games.stendhal.server.StendhalRPWorld;
import games.stendhal.server.StendhalRPZone;
import games.stendhal.server.entity.npc.ConversationStates;
import games.stendhal.server.entity.npc.NPCList;
import games.stendhal.server.entity.npc.SpeakerNPC;
import games.stendhal.server.entity.portal.Portal;
import games.stendhal.server.maps.ZoneConfigurator;
import games.stendhal.server.pathfinder.Path;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import marauroa.common.game.IRPZone;

/**
 * Creates the NPCs and portals in Ados City.
 *
 * @author hendrik
 */
public class AdosCityOutside implements ZoneConfigurator {
	private NPCList npcs = NPCList.get();


	/**
	 * builds the Ados City.
	 */
	public void build() {
		StendhalRPWorld world = StendhalRPWorld.get();

		configureZone(
			(StendhalRPZone) world.getRPZone(
				new IRPZone.ID("0_ados_city")),
			java.util.Collections.EMPTY_MAP);
	}


	/**
	 * Configure a zone.
	 *
	 * @param	zone		The zone to be configured.
	 * @param	attributes	Configuration attributes.
	 */
	public void configureZone(StendhalRPZone zone,
	 Map<String, String> attributes) {
		buildAdosCityAreaPortals(zone, attributes);
		buildFidorea(zone);
		buildKids(zone);
	}


	private void buildAdosCityAreaPortals(StendhalRPZone zone,
	 Map<String, String> attributes) {
		/*
		 * Portals configured in xml?
		 */
		if(attributes.get("xml-portals") == null) {
			Portal portal = new Portal();
			zone.assignRPObjectID(portal);
			portal.setX(60);
			portal.setY(16);
			portal.setNumber(0);
			portal.setDestination("int_ados_tavern_0", 0);
			zone.addPortal(portal);

			portal = new Portal();
			zone.assignRPObjectID(portal);
			portal.setX(65);
			portal.setY(16);
			portal.setNumber(1);
			portal.setDestination("int_ados_tavern_0", 1);
			zone.addPortal(portal);

			portal = new Portal();
			zone.assignRPObjectID(portal);
			portal.setX(54);
			portal.setY(19);
			portal.setNumber(6);
			portal.setDestination("int_ados_bank", 0);
			zone.addPortal(portal);
		
			portal = new Portal();
			zone.assignRPObjectID(portal);
			portal.setX(27);
			portal.setY(39);
			portal.setNumber(10);
			portal.setDestination("int_ados_bakery", 0);
			zone.addPortal(portal);

			portal = new Portal();
			zone.assignRPObjectID(portal);
			portal.setX(12);
			portal.setY(38);
			portal.setNumber(11);
			portal.setDestination("int_ados_haunted_house", 0);
			zone.addPortal(portal);
		}
	}

	private void buildFidorea(StendhalRPZone zone) {
		SpeakerNPC npc = new SpeakerNPC("Fidorea") {
			@Override
			protected void createPath() {
				// npc does not move
				List<Path.Node> nodes = new LinkedList<Path.Node>();
				setPath(nodes, false);
			}

			@Override
			protected void createDialog() {
				addGreeting("Hi, there. Do you need #help with anything?");
				// should be disabled as she doesn't give masks now.
				//	addHelp("If you don't like your mask, you can remove it by clicking on yourself and choosing Set Outfit."); 
				addHelp("Perhaps Anna still want some toys. She loves my costumes and dressing up, perhaps she'd like a dress to try on. If you already got her one, I guess she'll have to wait till I make more costumes!");// this is a hint that one of the items Anna wants is a dress (goblin dress)
				addJob("I am a makeup artist.");
				addReply("offer", "Normally I sell masks. But I ran out of clothes and cannot by new ones until the cloth seller gets back from his search.");
				addGoodbye("Bye, come back soon.");
			}
		};
		npcs.add(npc);
		zone.assignRPObjectID(npc);
		npc.put("class", "woman_008_npc");
		npc.set(20, 12);
		npc.setDirection(Direction.DOWN);
		npc.initHP(100);
		zone.addNPC(npc);
	}

	private void buildKids(StendhalRPZone zone) {
		String[] names = {"Jens", "George", "Anna"};
		String[] classes = {"kid3npc", "kid4npc", "kid5npc"};
		Path.Node[] start = new Path.Node[] {new Path.Node(40, 28), new Path.Node(40, 40), new Path.Node(45, 28)};
		for (int i = 0; i < 3; i++) {
		SpeakerNPC npc = new SpeakerNPC(names[i]) {
			@Override
			protected void createPath() {
				List<Path.Node> nodes = new LinkedList<Path.Node>();
				nodes.add(new Path.Node(40,28));
				nodes.add(new Path.Node(40,31));
				nodes.add(new Path.Node(34,31));
				nodes.add(new Path.Node(34,35));
				nodes.add(new Path.Node(39,35));
				nodes.add(new Path.Node(39,40));
				nodes.add(new Path.Node(40,40));
				nodes.add(new Path.Node(40,38));
				nodes.add(new Path.Node(45,38));
				nodes.add(new Path.Node(45,42));
				nodes.add(new Path.Node(51,42));
				nodes.add(new Path.Node(51,36));
				nodes.add(new Path.Node(46,36));
				nodes.add(new Path.Node(46,29));
				nodes.add(new Path.Node(45,29));
				nodes.add(new Path.Node(45,28));
				setPath(nodes, true);
			}

			@Override
			protected void createDialog() {
				// Anna is special because she has a quest
				if (!this.getName().equals("Anna")) {
					add(ConversationStates.IDLE, SpeakerNPC.GREETING_MESSAGES, ConversationStates.IDLE,
						"Mummy said, we are not allowed to talk to strangers. She is worried about that lost girl. Bye.",
						null);
				}
				addGoodbye("Bye bye!");
			}
		};
		npcs.add(npc);

		zone.assignRPObjectID(npc);
		npc.put("class", classes[i]);
		npc.set(start[i].x, start[i].y);
		npc.setDirection(Direction.DOWN);
		npc.initHP(100);
		zone.addNPC(npc);
		}
	}
}
