package trch.thedrake.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

import trch.thedrake.Board;
import trch.thedrake.EmptyTile;
import trch.thedrake.Offset2D;
import trch.thedrake.PlayingSide;
import trch.thedrake.Tile;
import trch.thedrake.TilePosition;
import trch.thedrake.Troop;
import trch.thedrake.TroopFace;
import trch.thedrake.TroopInfo;
import trch.thedrake.TroopTile;

public class Tester02 {

	@Test
	public void troopInfoTest() {
		TroopInfo info = new TroopInfo("Drake", new Offset2D(0, 0));
		assertTrue(info.pivot(TroopFace.FRONT).equalsTo(0, 0));
		assertTrue(info.pivot(TroopFace.BACK).equalsTo(0, 0));
		
		TroopInfo info2 = new TroopInfo("Drake");
		assertTrue(info2.pivot(TroopFace.FRONT).equalsTo(1, 1));
		assertTrue(info2.pivot(TroopFace.BACK).equalsTo(1, 1));
	}
	
	@Test
	public void tilesTest() {
		TroopInfo drakeInfo = new TroopInfo("Drake");
		Troop drake = new Troop(drakeInfo, PlayingSide.ORANGE, TroopFace.FRONT);
		
		TilePosition b2 = new TilePosition("b2");
		Tile emptyTile = new EmptyTile(b2);
		assertSame(b2, emptyTile.position());
		assertTrue(emptyTile.acceptsTroop(drake));
		assertFalse(emptyTile.hasTroop());
		try{ emptyTile.troop(); fail(); } catch(UnsupportedOperationException e) {};
		
		TilePosition c3 = new TilePosition("c3");
		Tile troopTile = new TroopTile(c3, drake);
		assertSame(c3, troopTile.position());
		assertFalse(troopTile.acceptsTroop(drake));
		assertTrue(troopTile.hasTroop());
		assertSame(drake, troopTile.troop());
	}

	@Test
	public void boardTest() {
		Board board = new Board(5);
		assertEquals(5, board.dimension());
		
		for(int row = 1; row <= 5; row++) {
			for(char col = 'a'; col <= 'e'; col++) {
				TilePosition pos = new TilePosition(col, row);
				assertTrue(board.contains(pos));
				assertTrue(board.tileAt(pos) instanceof EmptyTile);
			}
		}
		
		assertFalse(board.contains(new TilePosition("a0")));
		assertFalse(board.contains(new TilePosition("a6")));
		assertFalse(board.contains(new TilePosition("f1")));
		assertFalse(board.contains(new TilePosition("f5")));
		assertFalse(board.contains(new TilePosition("e0")));
		assertFalse(board.contains(new TilePosition("e6")));
		
		TroopInfo drakeInfo = new TroopInfo("Drake");
		Troop drake1 = new Troop(drakeInfo, PlayingSide.BLUE, TroopFace.FRONT);
		Troop drake2 = new Troop(drakeInfo, PlayingSide.ORANGE, TroopFace.FRONT);
		
		TroopTile tile1 = new TroopTile(new TilePosition("c3"), drake1); 
		TroopTile tile2 = new TroopTile(new TilePosition("d4"), drake2);
		
		Board board2 = board.withTiles(tile1, tile2); 
		assertSame(tile1, board2.tileAt(new TilePosition("c3")));
		assertSame(tile2, board2.tileAt(new TilePosition("d4")));
		
		//Check, that the board was copied properly
		assertTrue(board != board2);		
		for(int row = 1; row <= 5; row++) {
			for(char col = 'a'; col <= 'e'; col++) {
				TilePosition pos = new TilePosition(col, row);
				assertTrue(board.contains(pos));
				assertTrue(board.tileAt(pos) instanceof EmptyTile);
			}
		}
	}
}
