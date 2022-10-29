package trch.thedrake.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import trch.thedrake.Board;
import trch.thedrake.BoardChange;
import trch.thedrake.CaptureOnly;
import trch.thedrake.CapturedTroops;
import trch.thedrake.PlayingSide;
import trch.thedrake.StandardDrakeSetup;
import trch.thedrake.StepAndCapture;
import trch.thedrake.StepOnly;
import trch.thedrake.StrikeAction;
import trch.thedrake.TilePosition;
import trch.thedrake.Troop;
import trch.thedrake.TroopFace;
import trch.thedrake.TroopInfo;
import trch.thedrake.TroopTile;

public class Tester03 {

	@Test
	public void playingSideTest() {
		assertSame(PlayingSide.ORANGE, PlayingSide.BLUE.opposite());
		assertSame(PlayingSide.BLUE, PlayingSide.ORANGE.opposite());
	}
	
	@Test
	public void capturedTroopsTest() {
		StandardDrakeSetup setup = new StandardDrakeSetup();
		CapturedTroops captured1 = new CapturedTroops();
		assertTrue(captured1.troops(PlayingSide.BLUE).isEmpty());
		assertTrue(captured1.troops(PlayingSide.ORANGE).isEmpty());
		
		List<TroopInfo> blues = Arrays.asList(setup.DRAKE, setup.ARCHER, setup.CLUBMAN); 
		List<TroopInfo> oranges = Arrays.asList(setup.MONK, setup.SPEARMAN);
		
		CapturedTroops captured2 = captured1
				.withTroop(PlayingSide.BLUE, blues.get(2))
				.withTroop(PlayingSide.ORANGE, oranges.get(1));
		CapturedTroops captured3 = captured2
				.withTroop(PlayingSide.BLUE, blues.get(1))
				.withTroop(PlayingSide.ORANGE, oranges.get(0))
				.withTroop(PlayingSide.BLUE, blues.get(0));
                
                // Custom Vypis
                
                System.out.println("VYPI <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");

                
                System.out.println(captured2.troops(PlayingSide.BLUE).size());
                
                System.out.println(captured2.troops(PlayingSide.ORANGE).size());
                
                System.out.println(captured2);
                
                System.out.println(" <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
                System.out.println(blues);
                
	
		assertEquals(
				Collections.singletonList(blues.get(2)), 
				captured2.troops(PlayingSide.BLUE));
		assertEquals(
				Collections.singletonList(oranges.get(1)), 
				captured2.troops(PlayingSide.ORANGE));
		
		assertEquals(blues, captured3.troops(PlayingSide.BLUE));
		assertEquals(oranges, captured3.troops(PlayingSide.ORANGE));
		try{ 
			captured3.troops(PlayingSide.BLUE).add(setup.ARCHER); 
			fail(); 
		} catch(UnsupportedOperationException e) {};
		try{ 
			captured3.troops(PlayingSide.ORANGE).add(setup.ARCHER); 
			fail(); 
		} catch(UnsupportedOperationException e) {};
	}
	
	@Test
	public void boardTest() {
		StandardDrakeSetup setup = new StandardDrakeSetup();
		Troop blueDrake = new Troop(setup.DRAKE, PlayingSide.BLUE, TroopFace.FRONT);
		Troop orangeDrake = new Troop(setup.DRAKE, PlayingSide.ORANGE, TroopFace.FRONT);
		
		Board board = new Board(
				4, 
				new TroopTile(new TilePosition("b1"), blueDrake),
				new TroopTile(new TilePosition("c2"), orangeDrake)); 
		
		assertTrue(board.canTakeFrom(new TilePosition("b1")));
		assertTrue(board.canTakeFrom(new TilePosition("c2")));
		assertFalse(board.canTakeFrom(new TilePosition("c3")));
		assertFalse(board.canTakeFrom(new TilePosition("f10")));
		
		assertTrue(board.canPlaceTo(blueDrake, new TilePosition("b2")));
		assertTrue(board.canPlaceTo(orangeDrake, new TilePosition("c3")));
		assertFalse(board.canPlaceTo(blueDrake, new TilePosition("c2")));
		assertFalse(board.canPlaceTo(orangeDrake, new TilePosition("b1")));
		assertFalse(board.canPlaceTo(orangeDrake, new TilePosition("a0")));
		
		assertTrue(board.canStepOnly(new TilePosition("b1"), new TilePosition("b2")));
		assertTrue(board.canStepOnly(new TilePosition("c2"), new TilePosition("c3")));
		assertFalse(board.canStepOnly(new TilePosition("c2"), new TilePosition("b1")));
		assertFalse(board.canStepOnly(new TilePosition("c3"), new TilePosition("d3")));
		assertFalse(board.canStepOnly(new TilePosition("g5"), new TilePosition("f10")));
		
		assertTrue(board.canCaptureOnly(new TilePosition("b1"), new TilePosition("c2")));
		assertTrue(board.canCaptureOnly(new TilePosition("c2"), new TilePosition("b1")));
		assertFalse(board.canCaptureOnly(new TilePosition("b2"), new TilePosition("c3")));
		assertFalse(board.canCaptureOnly(new TilePosition("c3"), new TilePosition("d3")));
		assertFalse(board.canCaptureOnly(new TilePosition("a5"), new TilePosition("c5")));
		
		assertTrue(board.canStepAndCapture(new TilePosition("b1"), new TilePosition("c2")));
		assertTrue(board.canStepAndCapture(new TilePosition("c2"), new TilePosition("b1")));
		assertFalse(board.canStepAndCapture(new TilePosition("b3"), new TilePosition("d3")));
		assertFalse(board.canStepAndCapture(new TilePosition("d4"), new TilePosition("a5")));
		assertFalse(board.canStepAndCapture(new TilePosition("x1"), new TilePosition("x11")));
		
		Board b1 = board.stepOnly(new TilePosition("b1"), new TilePosition("b2"));
		assertTrue(b1.canTakeFrom(new TilePosition("b2")));
		assertFalse(b1.canTakeFrom(new TilePosition("b1")));
		assertSame(TroopFace.BACK, b1.tileAt(new TilePosition("b2")).troop().face());
		assertSame(setup.DRAKE, b1.tileAt(new TilePosition("b2")).troop().info());
		
		
		Board b2 = board.stepAndCapture(new TilePosition("c2"), new TilePosition("b1"));
		assertTrue(b2.canTakeFrom(new TilePosition("b1")));
		assertFalse(b2.canTakeFrom(new TilePosition("c2")));
		assertSame(TroopFace.BACK, b2.tileAt(new TilePosition("b1")).troop().face());
		assertSame(setup.DRAKE, b2.tileAt(new TilePosition("b1")).troop().info());
		assertEquals(
				Collections.singletonList(setup.DRAKE), 
				b2.captured().troops(PlayingSide.BLUE));
		
		Board b3 = board.captureOnly(new TilePosition("b1"), new TilePosition("c2"));
		assertTrue(b3.canTakeFrom(new TilePosition("b1")));
		assertFalse(b3.canTakeFrom(new TilePosition("c2")));
		assertSame(TroopFace.BACK, b3.tileAt(new TilePosition("b1")).troop().face());
		assertSame(setup.DRAKE, b3.tileAt(new TilePosition("b1")).troop().info());
		assertEquals(
				Collections.singletonList(setup.DRAKE), 
				b3.captured().troops(PlayingSide.ORANGE));
		
		Troop troop = new Troop(setup.CLUBMAN, PlayingSide.BLUE, TroopFace.BACK);
		Board b4 = board.withCaptureAndTiles(
				setup.ARCHER, PlayingSide.ORANGE,
				new TroopTile(new TilePosition("a1"), troop));
		assertSame(troop, b4.tileAt(new TilePosition("a1")).troop());
		assertEquals(
				Collections.singletonList(setup.ARCHER), 
				b4.captured().troops(PlayingSide.ORANGE));
	}
	
	@Test
	public void changesTest() {
		StandardDrakeSetup setup = new StandardDrakeSetup();
		Troop blueDrake = new Troop(setup.DRAKE, PlayingSide.BLUE, TroopFace.FRONT);
		Troop orangeDrake = new Troop(setup.DRAKE, PlayingSide.ORANGE, TroopFace.FRONT);
		
		Board board = new Board(
				4, 
				new TroopTile(new TilePosition("b1"), blueDrake),
				new TroopTile(new TilePosition("c2"), orangeDrake));
		
		StepOnly stepOnly = new StepOnly(board, new TilePosition("b1"), new TilePosition("b2"));
		Board b1 = stepOnly.resultBoard();
		assertTrue(b1.canTakeFrom(new TilePosition("b2")));
		assertFalse(b1.canTakeFrom(new TilePosition("b1")));
		assertSame(TroopFace.BACK, b1.tileAt(new TilePosition("b2")).troop().face());
		assertSame(setup.DRAKE, b1.tileAt(new TilePosition("b2")).troop().info());
		
		StepAndCapture stepAndCapture = new StepAndCapture(board, new TilePosition("c2"), new TilePosition("b1"));
		Board b2 = stepAndCapture.resultBoard();
		assertTrue(b2.canTakeFrom(new TilePosition("b1")));
		assertFalse(b2.canTakeFrom(new TilePosition("c2")));
		assertSame(TroopFace.BACK, b2.tileAt(new TilePosition("b1")).troop().face());
		assertSame(setup.DRAKE, b2.tileAt(new TilePosition("b1")).troop().info());
		assertEquals(
				Collections.singletonList(setup.DRAKE), 
				b2.captured().troops(PlayingSide.BLUE));
		
		CaptureOnly captureOnly = new CaptureOnly(board, new TilePosition("b1"), new TilePosition("c2"));
		Board b3 = captureOnly.resultBoard();
		assertTrue(b3.canTakeFrom(new TilePosition("b1")));
		assertFalse(b3.canTakeFrom(new TilePosition("c2")));
		assertSame(TroopFace.BACK, b3.tileAt(new TilePosition("b1")).troop().face());
		assertSame(setup.DRAKE, b3.tileAt(new TilePosition("b1")).troop().info());
		assertEquals(
				Collections.singletonList(setup.DRAKE), 
				b3.captured().troops(PlayingSide.ORANGE));
	}
	
	@Test
	public void strikeActionTest() {
		StandardDrakeSetup setup = new StandardDrakeSetup();
		Troop blueSpearman = new Troop(setup.SPEARMAN, PlayingSide.BLUE, TroopFace.FRONT);
		Troop orangeDrake = new Troop(setup.DRAKE, PlayingSide.ORANGE, TroopFace.FRONT);
		Troop orangeSpearman = new Troop(setup.SPEARMAN, PlayingSide.ORANGE, TroopFace.FRONT);
		Troop blueDrake = new Troop(setup.DRAKE, PlayingSide.BLUE, TroopFace.FRONT);
		
		Board board = new Board(
				4, 
				new TroopTile(new TilePosition("b2"), blueSpearman),
				new TroopTile(new TilePosition("c2"), blueDrake),
				new TroopTile(new TilePosition("b3"), orangeDrake),
				new TroopTile(new TilePosition("c3"), orangeSpearman));
		
		StrikeAction action1 = new StrikeAction(1, 1);
		StrikeAction action2 = new StrikeAction(-1, 1);
		List<BoardChange> changes1 = action1.changesFrom(new TilePosition("b2"), PlayingSide.BLUE, board);
		List<BoardChange> changes2 = action2.changesFrom(new TilePosition("b2"), PlayingSide.BLUE, board);
		
		assertSame(1, changes1.size());
		assertEquals(Collections.emptyList(), changes2);
		
		BoardChange ch1 = changes1.get(0);
		assertSame(CaptureOnly.class, ch1.getClass());
		assertSame('b', ch1.origin().column());
		assertSame(2, ch1.origin().row());
		assertSame('c', ch1.target().column());
		assertSame(3, ch1.target().row());		
		
		
		List<BoardChange> changes3 = action1.changesFrom(new TilePosition("c3"), PlayingSide.ORANGE, board);
		List<BoardChange> changes4 = action2.changesFrom(new TilePosition("c3"), PlayingSide.ORANGE, board);
		
		assertSame(1, changes4.size());
		assertEquals(Collections.emptyList(), changes3);
		
		BoardChange ch2 = changes4.get(0);
		assertSame(CaptureOnly.class, ch2.getClass());
		assertSame('c', ch2.origin().column());
		assertSame(3, ch2.origin().row());
		assertSame('b', ch2.target().column());
		assertSame(2, ch2.target().row());		
	}
}

