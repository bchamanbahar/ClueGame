/*
 * Authors: 
 * Mia Blanchard 
 * Bijan Chamanbahar
 */
package tests;

import static org.junit.Assert.*;

import java.util.Map;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import clueGame.Board;
import clueGame.BoardCell;
import clueGame.DoorDirection;

public class loadRoomTests {
	// Constants that I will use to test whether the file was loaded correctly
	public static final int LEGEND_SIZE = 11;
	public static final int NUM_ROWS = 20;
	public static final int NUM_COLUMNS = 20;
	// NOTE: I made Board static because I only want to set it up one 
	// time (using @BeforeClass), no need to do setup before each test.
	private static Board board;
		
	@BeforeClass
	public static void setUp() throws Exception {
		// Board is singleton, get the only instance
		board = Board.getInstance();
		// set the file names to use my config files
		board.setConfigFiles("Clue_Layout.csv", "legend.txt");		
		// Initialize will load BOTH config files 
		board.initialize();
	}
	@Test
	public void testRooms() {
		//tests that the legend loaded correctly
		Map<Character, String> legend = board.getLegend();
		assertEquals(LEGEND_SIZE, legend.size());
		//test first 
		assertEquals("Conservatory", legend.get('C'));
		assertEquals("Library", legend.get('L'));
		assertEquals("Study", legend.get('S'));
		assertEquals("Lounge", legend.get('O'));
		//test last
		assertEquals("Walkway", legend.get('W'));
	}
	
	@Test
	public void testBoardDimensions() {
		// Ensure we have the proper number of rows and columns
		assertEquals(NUM_ROWS, board.getNumRows());
		assertEquals(NUM_COLUMNS, board.getNumColumns());		
	}
	
	@Test
	public void FourDoorDirections() {
		//tests that the door at [2][2] points down
		BoardCell room = board.getCellAt(2, 2);
		assertTrue(room.isDoorway());
		assertEquals(DoorDirection.DOWN, room.getDoorDirection());
		//tests that the door at [13][3] points up
		room = board.getCellAt(13, 3);
		assertTrue(room.isDoorway());
		assertEquals(DoorDirection.UP, room.getDoorDirection());
		//tests that the door at [7][4] points right
		room = board.getCellAt(7, 4);
		assertTrue(room.isDoorway());
		assertEquals(DoorDirection.RIGHT, room.getDoorDirection());
		//tests that the door at [13][17] points left
		room = board.getCellAt(13, 17);
		assertTrue(room.isDoorway());
		assertEquals(DoorDirection.LEFT, room.getDoorDirection());
		// Test that room pieces that aren't doors know it
		room = board.getCellAt(0, 0);
		assertFalse(room.isDoorway());	
		// Test that walkways are not doors
		BoardCell cell = board.getCellAt(0, 4);
		assertFalse(cell.isDoorway());		
	}
	
	// Test that we have the correct number of doors
	@Test
	public void testNumberOfDoorways() 
	{
		int numDoors = 0;
		for (int row=0; row<board.getNumRows(); row++)
			for (int col=0; col<board.getNumColumns(); col++) {
				BoardCell cell = board.getCellAt(row, col);
				if (cell.isDoorway())
					numDoors++;
			}
		Assert.assertEquals(15, numDoors);
	}
	
	@Test
	public void testRoomInitials() {
		// Test first cell in room
		assertEquals('C', board.getCellAt(0, 0).getInitial());
		assertEquals('R', board.getCellAt(0, 9).getInitial());
		assertEquals('K', board.getCellAt(9, 0).getInitial());
		// Test last cell in room
		assertEquals('D', board.getCellAt(19, 19).getInitial());
		assertEquals('L', board.getCellAt(18, 10).getInitial());
		// Test a walkway
		assertEquals('W', board.getCellAt(2, 5).getInitial());
		// Test the closet
		assertEquals('X', board.getCellAt(9,11).getInitial());
	}

}
