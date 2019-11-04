package tests;
import static org.junit.jupiter.api.Assertions.*;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.junit.BeforeClass;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import clueGame.Board;
import clueGame.BoardCell;
import clueGame.Card;
import clueGame.CardType;
import clueGame.ComputerPlayer;
import clueGame.HumanPlayer;
import clueGame.Player;

class playerInteractionTests {
private static Board board;
	
	@BeforeAll
	public static void setUp() throws Exception {
		// Board is singleton, get the only instance
		board = Board.getInstance();
		// set the file names to use my config files
		board.setConfigFiles("Clue_Layout.csv", "legend.txt");		
		// Initialize will load BOTH config files 
		board.setPersonFile("people.txt");
		board.setDeckFile("deck.txt");
		board.initialize();
	}
	@Test
	void testSelectTarget() {
		ComputerPlayer testPlayer = (ComputerPlayer) board.getListPeople().get(1);
		board.calcTargets(testPlayer.getRow(), testPlayer.getCol(), 4);
		Set<BoardCell> targets = board.getTargets();
		BoardCell location = testPlayer.pickLocation(targets);
		for (BoardCell c : targets) {
			if (c.isRoom()) {
				if (!testPlayer.inPreviousLocations(c)) {
					assertEquals(c, location);
				}
			}
		}
	}

}
