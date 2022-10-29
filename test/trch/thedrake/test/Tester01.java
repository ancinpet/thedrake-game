package trch.thedrake.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import trch.thedrake.Offset2D;
import trch.thedrake.PlayingSide;
import trch.thedrake.Troop;
import trch.thedrake.TroopFace;
import trch.thedrake.TroopInfo;

public class Tester01 {

	@Test
	public void offset2DTest() {
		Offset2D offset2D = new Offset2D(10, -5);
		assertSame(10, offset2D.x);
		assertSame(-5, offset2D.y);
		assertTrue(offset2D.equalsTo(10, -5));
		assertTrue(offset2D.yFlipped().equalsTo(10, 5));
		assertTrue(offset2D.yFlipped().yFlipped().equalsTo(10, -5));
		assertTrue(offset2D.yFlipped() != offset2D);
	}

	@Test
	public void troopInfoTest() {
		TroopInfo info = new TroopInfo("Archer", new Offset2D(1, 1), new Offset2D(0, 1));
		assertEquals("Archer", info.name());
		assertTrue(info.pivot(TroopFace.FRONT).equalsTo(1, 1));
		assertTrue(info.pivot(TroopFace.BACK).equalsTo(0, 1));
	}

	@Test
	public void troopTest() {
		TroopInfo info = new TroopInfo("Archer", new Offset2D(1, 1), new Offset2D(0, 1));
		Troop troop = new Troop(info, PlayingSide.BLUE, TroopFace.FRONT);
		assertSame(info, troop.info());
		assertEquals(PlayingSide.BLUE, troop.side());
		assertEquals(TroopFace.FRONT, troop.face());
		assertTrue(troop.pivot().equalsTo(1, 1));

		assertTrue(troop.flipped() != troop);
		assertEquals(TroopFace.BACK, troop.flipped().face());
		assertTrue(troop.flipped().pivot().equalsTo(0, 1));
	}
}
