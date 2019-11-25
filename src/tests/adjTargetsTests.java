package tests;

/*
 * This program tests that adjacencies and targets are calculated correctly.
 */

import java.util.Set;

//Doing a static import allows me to write assertEquals rather than
//assertEquals
import static org.junit.Assert.*;
import org.junit.BeforeClass;
import org.junit.Test;

import clueGame.Board;
import clueGame.BoardCell;

public class adjTargetsTests {
	// We make the Board static because we can load it one time and 
	// then do all the tests. 
	private static Board board;
	@BeforeClass
	public static void setUp() throws Exception {
		// Board is singleton, get the only instance
		board = Board.getInstance();
		// set the file names to use my config files
		board.setConfigFiles("Clue_Layout.csv", "legend.txt");	
		board.setPersonFile("people.txt");
		board.setDeckFile("deck.txt");
		// Initialize will load BOTH config files 
		board.initialize();
	}

	// Test a variety of walkway scenarios
	// These tests are LIGHT PURPLE on the planning spreadsheet
	@Test
	public void testAdjacent()
	{
		//test with only walkways as adjacent locations
		Set<BoardCell> testList = board.getAdjList(1, 4);
		assertTrue(testList.contains(board.getCellAt(0, 4)));
		assertTrue(testList.contains(board.getCellAt(2, 4)));
		assertTrue(testList.contains(board.getCellAt(1, 5)));
		assertTrue(testList.contains(board.getCellAt(1, 3)));
		assertEquals(4, testList.size());
		
		//test locations within rooms
		testList = board.getAdjList(0, 0);
		assertEquals(0, testList.size());
		
		//test edges
		testList =board.getAdjList(11, 0);
		assertTrue(testList.contains(board.getCellAt(12, 0)));
		assertTrue(testList.contains(board.getCellAt(11, 1)));
		assertEquals(2, testList.size());
		
		testList = board.getAdjList(0, 4);
		assertTrue(testList.contains(board.getCellAt(0, 5)));
		assertTrue(testList.contains(board.getCellAt(1, 4)));
		assertTrue(testList.contains(board.getCellAt(0, 3)));
		assertEquals(3, testList.size());
		
		//test locations that are beside a room cell that is not a doorway
		testList = board.getAdjList(0, 3);
		assertTrue(testList.contains(board.getCellAt(1, 3)));
		assertTrue(testList.contains(board.getCellAt(0, 4)));
		assertEquals(2, testList.size());
		
		testList = board.getAdjList(4, 1);
		assertTrue(testList.contains(board.getCellAt(4, 2)));
		assertTrue(testList.contains(board.getCellAt(4, 0)));
		assertTrue(testList.contains(board.getCellAt(3, 1)));
		assertEquals(3, testList.size());
		
		//test locations that are adjacent to a doorway with needed direction
		testList = board.getAdjList(3, 2);
		assertTrue(testList.contains(board.getCellAt(4, 2)));
		assertTrue(testList.contains(board.getCellAt(3, 3)));
		assertTrue(testList.contains(board.getCellAt(2, 2)));
		assertTrue(testList.contains(board.getCellAt(3, 1)));
		assertEquals(4, testList.size());
		
		testList = board.getAdjList(7, 5);
		assertTrue(testList.contains(board.getCellAt(6, 5)));
		assertTrue(testList.contains(board.getCellAt(8, 5)));
		assertTrue(testList.contains(board.getCellAt(7, 4)));
		assertTrue(testList.contains(board.getCellAt(7, 6)));
		assertEquals(4, testList.size());
		
		//test locations that are doorways
		testList = board.getAdjList(2, 2);
		assertTrue(testList.contains(board.getCellAt(3, 2)));
		assertEquals(1, testList.size());
		
		testList = board.getAdjList(7, 4);
		assertTrue(testList.contains(board.getCellAt(7, 5)));
		assertEquals(1, testList.size());
		
	}
	
	@Test
	public void testTargets()
	{
		// Take one step
		board.calcTargets(11, 5, 1);
		Set<BoardCell> targets= board.getTargets();
		assertEquals(4, targets.size());
		assertTrue(targets.contains(board.getCellAt(11, 4)));
		assertTrue(targets.contains(board.getCellAt(10, 5)));
		assertTrue(targets.contains(board.getCellAt(11, 6)));
		assertTrue(targets.contains(board.getCellAt(12, 5)));
		
		// Take two steps
		board.calcTargets(11, 5, 2);
		targets= board.getTargets();
		assertEquals(8, targets.size());
		assertTrue(targets.contains(board.getCellAt(12, 4)));
		assertTrue(targets.contains(board.getCellAt(13, 5)));
		assertTrue(targets.contains(board.getCellAt(11, 3)));
		assertTrue(targets.contains(board.getCellAt(11, 7)));
		assertTrue(targets.contains(board.getCellAt(10, 4)));
		assertTrue(targets.contains(board.getCellAt(9, 5)));
		assertTrue(targets.contains(board.getCellAt(10, 6)));
		assertTrue(targets.contains(board.getCellAt(12, 6)));
		
		// Take three steps
		board.calcTargets(11, 5, 3);
		targets= board.getTargets();
		assertEquals(14, targets.size());
		assertTrue(targets.contains(board.getCellAt(12, 3)));
		assertTrue(targets.contains(board.getCellAt(14, 5)));
		assertTrue(targets.contains(board.getCellAt(11, 4)));
		assertTrue(targets.contains(board.getCellAt(9, 4)));
		assertTrue(targets.contains(board.getCellAt(9, 6)));
		assertTrue(targets.contains(board.getCellAt(10, 5)));
		assertTrue(targets.contains(board.getCellAt(11, 6)));
		assertTrue(targets.contains(board.getCellAt(12, 5)));
		assertTrue(targets.contains(board.getCellAt(13, 4)));
		assertTrue(targets.contains(board.getCellAt(8, 5)));
		assertTrue(targets.contains(board.getCellAt(11, 2)));
		assertTrue(targets.contains(board.getCellAt(11, 8)));
		assertTrue(targets.contains(board.getCellAt(13, 6)));
		
		// Take four steps
		board.calcTargets(11, 5, 4);
		targets= board.getTargets();
		assertEquals(19, targets.size());
		assertTrue(targets.contains(board.getCellAt(9, 5)));
		assertTrue(targets.contains(board.getCellAt(10, 4)));
		assertTrue(targets.contains(board.getCellAt(8, 6)));
		assertTrue(targets.contains(board.getCellAt(11, 7)));
		assertTrue(targets.contains(board.getCellAt(14, 6)));
		assertTrue(targets.contains(board.getCellAt(13, 5)));
		assertTrue(targets.contains(board.getCellAt(15, 5)));
		assertTrue(targets.contains(board.getCellAt(11, 3)));
		assertTrue(targets.contains(board.getCellAt(13, 4)));
		assertTrue(targets.contains(board.getCellAt(11, 9)));
		assertTrue(targets.contains(board.getCellAt(8, 4)));
		assertTrue(targets.contains(board.getCellAt(12, 6)));
		assertTrue(targets.contains(board.getCellAt(10, 6)));
		assertTrue(targets.contains(board.getCellAt(12, 4)));
		assertTrue(targets.contains(board.getCellAt(12, 4)));
		assertTrue(targets.contains(board.getCellAt(7, 5)));
		assertTrue(targets.contains(board.getCellAt(11, 1)));
		assertTrue(targets.contains(board.getCellAt(13, 3)));
		assertTrue(targets.contains(board.getCellAt(12, 8)));
		
		//Targets that allow users to enter rooms
		board.calcTargets(4, 8, 1);
		targets= board.getTargets();
		assertEquals(4, targets.size());
		assertTrue(targets.contains(board.getCellAt(5, 8)));
		assertTrue(targets.contains(board.getCellAt(3, 8)));
		assertTrue(targets.contains(board.getCellAt(4, 9)));
		assertTrue(targets.contains(board.getCellAt(4, 7)));
		
		board.calcTargets(8, 5, 1);
		targets= board.getTargets();
		assertEquals(4, targets.size());
		assertTrue(targets.contains(board.getCellAt(9, 5)));
		assertTrue(targets.contains(board.getCellAt(7, 5)));
		assertTrue(targets.contains(board.getCellAt(8, 4)));
		assertTrue(targets.contains(board.getCellAt(8, 6)));
		
		//Targets when leaving a room
		board.calcTargets(3, 9, 1);
		targets= board.getTargets();
		assertEquals(0, targets.size());


	}
	
	
	
	
	


}
