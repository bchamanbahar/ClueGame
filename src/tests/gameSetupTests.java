package tests;

import static org.junit.jupiter.api.Assertions.*;

import java.awt.Color;

import org.junit.BeforeClass;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import clueGame.Board;
import clueGame.ComputerPlayer;
import clueGame.HumanPlayer;
import clueGame.Player;

class gameSetupTests {
	private static Board board;
	
	@BeforeAll
	public static void setUp() throws Exception {
		// Board is singleton, get the only instance
		board = Board.getInstance();
		// set the file names to use my config files
		board.setConfigFiles("Clue_Layout.csv", "legend.txt");		
		// Initialize will load BOTH config files 
		board.setPersonFile("people.txt");
		board.initialize();
	}
	
	@Test
	void testLoadingPeople() {

		//test names
		assertEquals("Miss Scarlett", board.getListPeople().get(0).getPlayerName());
		assertEquals("Colonel Mustard", board.getListPeople().get(2).getPlayerName());
		assertEquals("Mrs. White", board.getListPeople().get(5).getPlayerName());
		//test colors
		assertEquals(Color.RED, board.getListPeople().get(0).getColor());
		assertEquals(Color.YELLOW, board.getListPeople().get(2).getColor());
		assertEquals(Color.WHITE, board.getListPeople().get(5).getColor());
		
		//test if player is human or computer
		assertTrue(board.getListPeople().get(0) instanceof HumanPlayer);
		assertTrue(board.getListPeople().get(2) instanceof ComputerPlayer);
		assertTrue(board.getListPeople().get(5) instanceof ComputerPlayer);
		
		//test starting location
		assertEquals(19, board.getListPeople().get(0).getRow());
		assertEquals(6, board.getListPeople().get(0).getCol());
		
		assertEquals(19, board.getListPeople().get(2).getRow());
		assertEquals(14, board.getListPeople().get(2).getCol());
		
		assertEquals(0, board.getListPeople().get(5).getRow());
		assertEquals(16, board.getListPeople().get(5).getCol());
	}

}
