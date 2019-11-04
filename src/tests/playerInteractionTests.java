package tests;

import static org.junit.Assert.assertNull;
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
		// TESTS FOR ROOM MATCHING CURRENT LOCATION, MULTIPLE WEAPONS NOT SEEN, AND
		// MULTIPLE PEOPLE NOT SEEN

		// grab the first computer player
		ComputerPlayer testPlayer = (ComputerPlayer) board.getListPeople().get(1);
		// move player to door at the study
		testPlayer.setRow(3);
		testPlayer.setCol(14);
		ArrayList<Card> deck = new ArrayList<Card>();
		// set known cards to nothing
		testPlayer.setKnownCards(deck);
		ArrayList<Solution> suggestions = new ArrayList<Solution>();
		// create 100 random suggestions
		for (int i = 0; i < 100; i++) {
			suggestions.add(testPlayer.createSuggestion());
		}
		boolean inTheStudy = true;
		// create sets to keep track if we've seen each person and weapon at least once
		// in our suggestions
		Set<String> listPeople = new HashSet<String>();
		Set<String> listWeapons = new HashSet<String>();
		for (Solution s : suggestions) {
			// if the room is not the study, flag as false
			if (!s.getRoom().equals("Study")) {
				inTheStudy = false;
			}
			listPeople.add(s.getPerson());
			listWeapons.add(s.getWeapon());
		}
		// check if we're in the study every time
		assertTrue(inTheStudy);
		// since there are 6 people as options to choose from, the set should contain 6
		// people if it selected each one randomly once
		assertTrue(listPeople.size() == 6);
		// since there are 6 weapons as options to choose from, the set should contain 6
		// weapons if it selected each one randomly once
		assertTrue(listWeapons.size() == 6);

		// TESTS FOR IF ONE WEAPON AND ONE PERSON NOT SEEN, MUST PICK THEM!

		ArrayList<Card> deckCards = board.getDeck();
		// create cards for miss scarlett and candlestick so we can remove
		// them from the deck
		Card missScarlett = new Card("Miss Scarlett", CardType.PERSON);
		Card candleStick = new Card("Candlestick", CardType.WEAPON);
		for (Card c : deckCards) {
			// remove miss scarlett and candlestick from deck
			if (!c.equals(missScarlett) && !c.equals(candleStick)) {
				deck.add(c);
			}
		}
		// set known cards to everything EXCEPT Miss Scarlett and Candlestick. This
		// means we've seen every other person and weapon. Thus, the computer must ask
		// for Miss Scarlett and the Candlestick
		testPlayer.setKnownCards(deck);
		// create a suggestion
		Solution suggestion = new Solution();
		suggestion = testPlayer.createSuggestion();
		// check to see if we picked Study, Miss Scarlett, and Candlestick.
		assertTrue(suggestion.getRoom().equals("Study"));
		assertTrue(suggestion.getPerson().equals("Miss Scarlett"));
		assertTrue(suggestion.getWeapon().equals("Candlestick"));

	}

	@Test
	void testDisproveSuggestion() {
		// TESTS FOR HAVING ONE MATCHING CARD

		// grab the first computer player
		ComputerPlayer testPlayer = (ComputerPlayer) board.getListPeople().get(1);
		ArrayList<Card> knownCards = new ArrayList<Card>();
		// set our known cards to just miss scarlett
		Card missScarlett = new Card("Miss Scarlett", CardType.PERSON);
		knownCards.add(missScarlett);
		testPlayer.setListOfCards(knownCards);
		// we will be testing with the suggestion of miss scarlett, kitchen, and
		// candlestick
		Solution testSuggestion = new Solution("Miss Scarlett", "Kitchen", "Candlestick");
		// because we have the miss scarlett card, and the player is asking about it, we
		// must return the miss scarlett card
		assertEquals(missScarlett, testPlayer.disproveSuggestion(testSuggestion));

		// TESTS FOR HAVING MULTIPLE MATCHING CARDS

		// now, we will add kitchen to our list of cards. Now we have miss scarlett and
		// the kitchen.
		Card kitchen = new Card("Kitchen", CardType.ROOM);
		knownCards.add(kitchen);
		testPlayer.setListOfCards(knownCards);
		ArrayList<Card> testRandomCards = new ArrayList<Card>();
		// since we have multiple cards from the suggestion, we must return a random one
		for (int i = 0; i < 100; i++) {
			testRandomCards.add(testPlayer.disproveSuggestion(testSuggestion));
		}
		// check to see that we picked both cards at least once
		assertTrue(testRandomCards.contains(missScarlett));
		assertTrue(testRandomCards.contains(kitchen));

		// TESTS FOR NO MATCHING CARDS

		// clear our list of known cards, so now we know none of them
		knownCards.clear();
		testPlayer.setListOfCards(knownCards);
		// since we don't have any cards to disprove the suggestion, must return null
		assertNull(testPlayer.disproveSuggestion(testSuggestion));
	}

	@Test
	void testHandleSuggestion() {
		// TEST NO ONE CAN DISPROVE

		// grab human player
		HumanPlayer humanPlayer1 = (HumanPlayer) board.getListPeople().get(0);
		// grab the computer players
		ComputerPlayer computerPlayer1 = (ComputerPlayer) board.getListPeople().get(1);
		ComputerPlayer computerPlayer2 = (ComputerPlayer) board.getListPeople().get(2);
		ComputerPlayer computerPlayer3 = (ComputerPlayer) board.getListPeople().get(3);
		ComputerPlayer computerPlayer4 = (ComputerPlayer) board.getListPeople().get(4);
		ComputerPlayer computerPlayer5 = (ComputerPlayer) board.getListPeople().get(5);
		ArrayList<Card> knownCardsNone = new ArrayList<Card>();
		// set known cards to nothing (no one has any cards)
		humanPlayer1.setListOfCards(knownCardsNone);
		computerPlayer1.setListOfCards(knownCardsNone);
		computerPlayer2.setListOfCards(knownCardsNone);
		computerPlayer3.setListOfCards(knownCardsNone);
		computerPlayer4.setListOfCards(knownCardsNone);
		computerPlayer5.setListOfCards(knownCardsNone);
		// test suggestion will use miss scarlett, kitchen, and candlestick
		Solution testSuggestion = new Solution("Miss Scarlett", "Kitchen", "Candlestick");
		// since no one has a card, must return null
		assertNull(board.handleSuggestion(testSuggestion, humanPlayer1));

		// TEST ONLY ACCUSING PLAYER CAN DISPROVE

		Card missScarlett = new Card("Miss Scarlett", CardType.PERSON);
		ArrayList<Card> knownCardsScarlett = new ArrayList<Card>();
		knownCardsScarlett.add(missScarlett);
		// give computer player 1 a miss scarlett card
		computerPlayer1.setListOfCards(knownCardsScarlett);
		// since computer player 1 is the one asking the suggestion, and they already
		// have the miss scarlett card, it must return null
		assertNull(board.handleSuggestion(testSuggestion, computerPlayer1));

		// TEST ONLY HUMAN CAN DISPROVE

		// take away computer player 1's miss scarlett card
		computerPlayer1.setListOfCards(knownCardsNone);
		// give human the miss scarlett card
		humanPlayer1.setListOfCards(knownCardsScarlett);
		// since the computer is asking for miss scarlett, and the human player has it,
		// the player must return the miss scarlett card.
		assertEquals(missScarlett, board.handleSuggestion(testSuggestion, computerPlayer1));

		// TEST ONLY HUMAN CAN DISPROVE, BUT HUMAN IS ACCUSER

		// since the human is asking for miss scarlett, and they already have the card,
		// must return null
		assertNull(board.handleSuggestion(testSuggestion, humanPlayer1));

		// TEST THAT TWO PLAYERS CAN DISPROVE, CHOOSE CORRECT PLAYER

		// give miss scarlett card to computer 1
		computerPlayer1.setListOfCards(knownCardsScarlett);
		// take away miss scarlett from human
		humanPlayer1.setListOfCards(knownCardsNone);
		Card kitchen = new Card("Kitchen", CardType.ROOM);
		ArrayList<Card> knownCardsKitchen = new ArrayList<Card>();
		knownCardsKitchen.add(kitchen);
		// give kitchen card to computer 2
		computerPlayer2.setListOfCards(knownCardsKitchen);
		// since computer 1 comes before computer 2 in the list, the miss scarlett card
		// will be returned (NOT the kitchen card, because computer 2 is not next)
		assertEquals(missScarlett, board.handleSuggestion(testSuggestion, humanPlayer1));

		// TEST THAT HUMAN AND ANOTHER PLAYER CAN DISPROVE, BUT THE OTHER PLAYER IS NEXT
		// IN THE LIST

		// take away miss scarlett from computer 1
		computerPlayer1.setListOfCards(knownCardsNone);
		// give miss scarlett to human
		humanPlayer1.setListOfCards(knownCardsScarlett);
		// computer player 1 will ask for miss scarlett, kitchen, and candlestick. since
		// computer player 2 is next in the list, the kitchen card will be returned.
		// (NOT the miss scarlett card, because the human is not next in the list)
		assertEquals(kitchen, board.handleSuggestion(testSuggestion, computerPlayer1));

	}

}
