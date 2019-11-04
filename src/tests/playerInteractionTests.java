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
		//TESTS FOR RANDOM MOVEMENT (NO ROOMS IN LIST)
		
		//grab the first computer player. They are at location [12] [0]
		ComputerPlayer testPlayer = (ComputerPlayer) board.getListPeople().get(1);
		//calculate targets with movement of 2
		board.calcTargets(testPlayer.getRow(), testPlayer.getCol(), 2);
		Set<BoardCell> targets = board.getTargets();
		ArrayList<BoardCell> locations = new ArrayList<BoardCell>();
		//choose a location randomly 100 times
		for (int i = 0 ; i<100 ; i++) {
			//have computer pick a location
			locations.add(testPlayer.pickLocation(targets));
		}
		//they can only move to 2 possible locations, so we will test that we have visited both
		BoardCell possibleLocation1 = new BoardCell(12, 2);
		BoardCell possibleLocation2 = new BoardCell(13, 1);
		//counters to try to see if we're visiting somewhat equally
		int counterLocation1 = 0;
		int counterLocation2 = 0;
		for (BoardCell c: locations) {
			//check if they are in the list of locations
			if (c.equals(possibleLocation1)) {
				counterLocation1++;
			}
			else if (c.equals(possibleLocation2)){
				 counterLocation2++;
			}
		}	
		//check if we've visited both locations more than 25 times
		assertTrue(counterLocation1>25);
		assertTrue(counterLocation2>25);
		
		//TESTS FOR MOVING TO A DOOR IF WE DIDN'T JUST VISIT IT
		
		//change player location so they're near a door
		testPlayer.setRow(14);
		testPlayer.setCol(4);
		//calculate targets with movement of 1
		board.calcTargets(testPlayer.getRow(), testPlayer.getCol(), 1);
		targets = board.getTargets();
		//have computer pick a location
		BoardCell pickedLocation = testPlayer.pickLocation(targets);
		//since there is a door, we should have picked location [14][3]
		BoardCell goHere =  new BoardCell(14, 3);
		//check to see if we went to the door
		assertEquals(pickedLocation, goHere);
		
		//TESTS IF WE JUST VISITED THE DOOR (ALSO SELECTING RANDOMLY)
		
		//move back to [14][4]
		testPlayer.setRow(14);
		testPlayer.setCol(4);
		//calculate targets
		board.calcTargets(testPlayer.getRow(), testPlayer.getCol(), 1);
		targets = board.getTargets();
		//since we just visited the door at [14][3], we should be choosing a tile randomly now
		locations.clear();
		//choose a location randomly 100 times
		for (int i = 0 ; i<100 ; i++) {
			//have computer pick a location
			locations.add(testPlayer.pickLocation(targets));
		}
		//setting up boardcells for the possible locations the computer could have visited
		possibleLocation1.setRow(14);
		possibleLocation1.setCol(3);
		possibleLocation2.setRow(14);
		possibleLocation2.setCol(5);
		BoardCell possibleLocation3 = new BoardCell(15, 4);
		BoardCell possibleLocation4 = new BoardCell(13, 4);
		//set counters for each location to 0
		counterLocation1 = 0;
		counterLocation2 = 0;
		int counterLocation3 = 0;
		int counterLocation4 = 0;
		for (BoardCell c: locations) {
			if (c.equals(possibleLocation1)) {
				counterLocation1++;
			}
			else if (c.equals(possibleLocation2)){
				 counterLocation2++;
			}
			else if (c.equals(possibleLocation3)){
				 counterLocation3++;
			}
			else if (c.equals(possibleLocation4)){
				 counterLocation4++;
			}
		}	
		//check if we've visited each location multiple times
		assertTrue(counterLocation1>5);
		assertTrue(counterLocation2>5);
		assertTrue(counterLocation3>5);
		assertTrue(counterLocation4>5);
		
		
	}

}
