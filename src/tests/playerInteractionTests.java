package tests;
import static org.junit.jupiter.api.Assertions.*;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
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
import clueGame.Solution;

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
		BoardCell possibleLocation2 = new BoardCell(11, 1);
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
		testPlayer.setRow(4);
		testPlayer.setCol(14);
		//calculate targets with movement of 1
		board.calcTargets(testPlayer.getRow(), testPlayer.getCol(), 1);
		targets.clear();
		targets = board.getTargets();
		//have computer pick a location
		BoardCell pickedLocation = testPlayer.pickLocation(targets);
		//since there is a door, we should have picked location [3][14]
		BoardCell goHere =  new BoardCell(3, 14);
		//check to see if we went to the door
		assertEquals(pickedLocation, goHere);
		
		//TESTS IF WE JUST VISITED THE DOOR (ALSO SELECTING RANDOMLY)
		
		//move back to [4][14]
		testPlayer.setRow(4);
		testPlayer.setCol(14);
		//calculate targets
		board.calcTargets(testPlayer.getRow(), testPlayer.getCol(), 1);
		targets.clear();
		targets = board.getTargets();
		//since we just visited the door at [3][14], we should be choosing a tile randomly now
		locations.clear();
		//choose a location randomly 100 times
		for (int i = 0 ; i<100 ; i++) {
			//have computer pick a location
			locations.add(testPlayer.pickLocation(targets));
		}
		//setting up boardcells for the possible locations the computer could have visited
		possibleLocation1 = new BoardCell(3, 14);
		possibleLocation2 = new BoardCell(5, 14);
		BoardCell possibleLocation3 = new BoardCell(4, 15);
		BoardCell possibleLocation4 = new BoardCell(4, 13);
		//set counters for each location to 0
		counterLocation1 = 0;
		counterLocation2 = 0;
		int counterLocation3 = 0;
		int counterLocation4 = 0;
		for (BoardCell c: locations) {
			//check if they are in the list of locations
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
	
	@Test
	void testMakingAccusations() {
		Solution testSolution = new Solution("Miss Scarlett", "Kitchen", "Candlestick");
		//grab the first computer player
		ComputerPlayer testPlayer = (ComputerPlayer) board.getListPeople().get(1);
		ArrayList<Card> deckCards = new ArrayList<Card>();
		BufferedReader br = new BufferedReader(new FileReader("deck.txt"));
		String row;
		try {
			while ((row = br.readLine())!=null) {
				String [] data = row.split(",");
				//checks if the card is a room, weapon, or person
				if (data[0].equals("Room")) {
					Card c = new Card();
					c.setCardType(CardType.ROOM);
					c.setName(data[1].substring(1));
					//add card to deck
					deckCards.add(c);
				}
				else if (data[0].equals("Weapon")) {
					Card c = new Card();
					c.setCardType(CardType.WEAPON);
					c.setName(data[1].substring(1));
					//add card to deck
					deckCards.add(c);
				}
				else {
					Card c = new Card();
					c.setCardType(CardType.PERSON);
					c.setName(data[1].substring(1));
					//add card to deck
					deckCards.add(c);
				}			
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Card missScarlett = new Card("Miss Scarlett", CardType.PERSON);
		Card kitchen = new Card("Kitchen", CardType.ROOM);
		Card candleStick = new Card("Candlestick", CardType.WEAPON);
		ArrayList<Card> deckForCorrectSolution = new ArrayList<Card>();
		for (Card c : deckCards) {
			if (!c.equals(missScarlett) && !c.equals(kitchen) && !c.equals(candleStick)) {
				deckForCorrectSolution.add(c);
			}
		}
		testPlayer.setKnownCards(deckForCorrectSolution);
		assertEquals(testPlayer.makeAccusation(), testSolution);
		
		ArrayList<Card> deckForWrongPerson = new ArrayList<Card>();
		Card mrGreen = new Card("Mr. Green", CardType.PERSON);
		for (Card c : deckCards) {
			if (!c.equals(mrGreen) && !c.equals(kitchen) && !c.equals(candleStick)) {
				deckForWrongPerson.add(c);
			}
		}
		testPlayer.setKnownCards(deckForWrongPerson);
		assertNotEquals(testPlayer.makeAccusation(), testSolution);
		
		ArrayList<Card> deckForWrongRoom = new ArrayList<Card>();
		Card ballroom = new Card("Ballroom", CardType.ROOM);
		for (Card c : deckCards) {
			if (!c.equals(missScarlett) && !c.equals(ballroom) && !c.equals(candleStick)) {
				deckForWrongRoom.add(c);
			}
		}
		testPlayer.setKnownCards(deckForWrongRoom);
		assertNotEquals(testPlayer.makeAccusation(), testSolution);
		
		ArrayList<Card> deckForWrongWeapon = new ArrayList<Card>();
		Card dagger = new Card("Dagger", CardType.WEAPON);
		for (Card c : deckCards) {
			if (!c.equals(missScarlett) && !c.equals(kitchen) && !c.equals(dagger)) {
				deckForWrongWeapon.add(c);
			}
		}
		testPlayer.setKnownCards(deckForWrongWeapon);
		assertNotEquals(testPlayer.makeAccusation(), testSolution);
		
}
	
}