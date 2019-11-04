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
		// TESTS FOR RANDOM MOVEMENT (NO ROOMS IN LIST)

		// grab the first computer player. They are at location [12] [0]
		ComputerPlayer testPlayer = (ComputerPlayer) board.getListPeople().get(1);
		// calculate targets with movement of 2
		board.calcTargets(testPlayer.getRow(), testPlayer.getCol(), 2);
		Set<BoardCell> targets = board.getTargets();
		ArrayList<BoardCell> locations = new ArrayList<BoardCell>();
		// choose a location randomly 100 times
		for (int i = 0; i < 100; i++) {
			// have computer pick a location
			locations.add(testPlayer.pickLocation(targets));
		}
		// they can only move to 2 possible locations, so we will test that we have
		// visited both
		BoardCell possibleLocation1 = new BoardCell(12, 2);
		BoardCell possibleLocation2 = new BoardCell(11, 1);
		// counters to try to see if we're visiting somewhat equally
		int counterLocation1 = 0;
		int counterLocation2 = 0;
		for (BoardCell c : locations) {
			// check if they are in the list of locations
			if (c.equals(possibleLocation1)) {
				counterLocation1++;
			} else if (c.equals(possibleLocation2)) {
				counterLocation2++;
			}
		}
		// check if we've visited both locations more than 25 times
		assertTrue(counterLocation1 > 25);
		assertTrue(counterLocation2 > 25);

		// TESTS FOR MOVING TO A DOOR IF WE DIDN'T JUST VISIT IT

		// change player location so they're near a door
		testPlayer.setRow(4);
		testPlayer.setCol(14);
		// calculate targets with movement of 1
		board.calcTargets(testPlayer.getRow(), testPlayer.getCol(), 1);
		targets.clear();
		targets = board.getTargets();
		// have computer pick a location
		BoardCell pickedLocation = testPlayer.pickLocation(targets);
		// since there is a door, we should have picked location [3][14]
		BoardCell goHere = new BoardCell(3, 14);
		// check to see if we went to the door
		assertEquals(pickedLocation, goHere);

		// TESTS IF WE JUST VISITED THE DOOR (ALSO SELECTING RANDOMLY)

		// move back to [4][14]
		testPlayer.setRow(4);
		testPlayer.setCol(14);
		// calculate targets
		board.calcTargets(testPlayer.getRow(), testPlayer.getCol(), 1);
		targets.clear();
		targets = board.getTargets();
		// since we just visited the door at [3][14], we should be choosing a tile
		// randomly now
		locations.clear();
		// choose a location randomly 100 times
		for (int i = 0; i < 100; i++) {
			// have computer pick a location
			locations.add(testPlayer.pickLocation(targets));
		}
		// setting up boardcells for the possible locations the computer could have
		// visited
		possibleLocation1 = new BoardCell(3, 14);
		possibleLocation2 = new BoardCell(5, 14);
		BoardCell possibleLocation3 = new BoardCell(4, 15);
		BoardCell possibleLocation4 = new BoardCell(4, 13);
		// set counters for each location to 0
		counterLocation1 = 0;
		counterLocation2 = 0;
		int counterLocation3 = 0;
		int counterLocation4 = 0;
		for (BoardCell c : locations) {
			// check if they are in the list of locations
			if (c.equals(possibleLocation1)) {
				counterLocation1++;
			} else if (c.equals(possibleLocation2)) {
				counterLocation2++;
			} else if (c.equals(possibleLocation3)) {
				counterLocation3++;
			} else if (c.equals(possibleLocation4)) {
				counterLocation4++;
			}
		}
		// check if we've visited each location multiple times
		assertTrue(counterLocation1 > 5);
		assertTrue(counterLocation2 > 5);
		assertTrue(counterLocation3 > 5);
		assertTrue(counterLocation4 > 5);

	}

	@Test
	void testMakingAccusations() throws IOException {
		// this is the solution we will be testing against
		Solution testSolution = new Solution("Miss Scarlett", "Kitchen", "Candlestick");
		// grab the first computer player
		ComputerPlayer testPlayer = (ComputerPlayer) board.getListPeople().get(1);

		// TESTS FOR CORRECT SOLUTION

		ArrayList<Card> deckCards = board.getDeck();
		// create cards for miss scarlett, kitchen, and candlestick so we can remove
		// them from the deck (since they're the killers)
		Card missScarlett = new Card("Miss Scarlett", CardType.PERSON);
		Card kitchen = new Card("Kitchen", CardType.ROOM);
		Card candleStick = new Card("Candlestick", CardType.WEAPON);
		ArrayList<Card> deckForCorrectSolution = new ArrayList<Card>();
		for (Card c : deckCards) {
			// remove miss scarlett, kitchen, and candlestick from deck
			if (!c.equals(missScarlett) && !c.equals(kitchen) && !c.equals(candleStick)) {
				deckForCorrectSolution.add(c);
			}
		}
		// set the cards the players knows to everything except the solution ones. This
		// will force the computer to give the right answer.
		testPlayer.setKnownCards(deckForCorrectSolution);
		assertEquals(testPlayer.makeAccusation(), testSolution);

		// TESTS FOR WRONG PERSON

		ArrayList<Card> deckForWrongPerson = new ArrayList<Card>();
		// now, we will remove everything from the deck except mr green, kitchen, and
		// candlestick.
		Card mrGreen = new Card("Mr. Green", CardType.PERSON);
		for (Card c : deckCards) {
			// remove mr green, kitchen, and candlestick from deck
			if (!c.equals(mrGreen) && !c.equals(kitchen) && !c.equals(candleStick)) {
				deckForWrongPerson.add(c);
			}
		}
		// set the cards the players knows to everything except mr green, kitchen, and
		// candlestick. This will force the computer to give the wrong answer because
		// the person card is wrong.
		testPlayer.setKnownCards(deckForWrongPerson);
		assertNotEquals(testPlayer.makeAccusation(), testSolution);

		// TESTS FOR WRONG ROOM

		ArrayList<Card> deckForWrongRoom = new ArrayList<Card>();
		// now, we will remove everything from the deck except miss scarlett, ballroom,
		// and candlestick.
		Card ballroom = new Card("Ballroom", CardType.ROOM);
		for (Card c : deckCards) {
			// remove miss scarlett, ballroom, and candlestick from deck
			if (!c.equals(missScarlett) && !c.equals(ballroom) && !c.equals(candleStick)) {
				deckForWrongRoom.add(c);
			}
		}
		// set the cards the players knows to everything except miss scarlett, ballroom,
		// and candlestick. This will force the computer to give the wrong answer
		// because the room card is wrong.
		testPlayer.setKnownCards(deckForWrongRoom);
		assertNotEquals(testPlayer.makeAccusation(), testSolution);

		// TESTS FOR WRONG WEAPON

		ArrayList<Card> deckForWrongWeapon = new ArrayList<Card>();
		// now, we will remove everything from the deck except miss scarlett, kitchen,
		// and dagger.
		Card dagger = new Card("Dagger", CardType.WEAPON);
		for (Card c : deckCards) {
			// remove miss scarlett, kitchen, and dagger from deck
			if (!c.equals(missScarlett) && !c.equals(kitchen) && !c.equals(dagger)) {
				deckForWrongWeapon.add(c);
			}
		}
		// set the cards the players knows to everything except miss scarlett, kitchen,
		// and dagger. This will force the computer to give the wrong answer because the
		// weapon card is wrong.
		testPlayer.setKnownCards(deckForWrongWeapon);
		assertNotEquals(testPlayer.makeAccusation(), testSolution);
	}

	@Test
	void testCreateSuggestion() {
		// grab the first computer player
		ComputerPlayer testPlayer = (ComputerPlayer) board.getListPeople().get(1);
		// move player to door
		testPlayer.setRow(3);
		testPlayer.setCol(14);
		ArrayList<Solution> suggestions = new ArrayList<Solution>();
		for (int i = 0; i < 100; i++) {
			suggestions.add(testPlayer.createSuggestion());
		}
		boolean inTheStudy = true;
		Set<String> listPeople = new HashSet<String>();
		Set<String> listWeapons = new HashSet<String>();
		for (Solution s : suggestions) {
			if (!s.getRoom().equals("Study")) {
				inTheStudy = false;
			}
			listPeople.add(s.getPerson());
			listWeapons.add(s.getWeapon());
		}
		assertTrue(inTheStudy);
		assertTrue(listPeople.size() == 6);
		assertTrue(listWeapons.size() == 6);
		
		ArrayList<Card> deckCards = board.getDeck();
		// create cards for miss scarlett and candlestick so we can remove
		// them from the deck 
		Card missScarlett = new Card("Miss Scarlett", CardType.PERSON);
		Card candleStick = new Card("Candlestick", CardType.WEAPON);
		ArrayList<Card> deck = new ArrayList<Card>();
		for (Card c : deckCards) {
			// remove miss scarlett, kitchen, and candlestick from deck
			if (!c.equals(missScarlett) && !c.equals(candleStick)) {
				deck.add(c);
			}
		}	
		testPlayer.setKnownCards(deck);
		Solution suggestion = new Solution();
		suggestion = testPlayer.createSuggestion();
		assertTrue(suggestion.getRoom() == "Study");
		assertTrue(suggestion.getPerson() == "Miss Scarlett");
		assertTrue(suggestion.getWeapon() == "Candlestick");
		
	}

}
