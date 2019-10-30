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
import clueGame.Card;
import clueGame.CardType;
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
		board.setDeckFile("deck.txt");
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

	@Test 
	void testLoadingDeck() {
		//checks size of deck
		assertEquals(21, board.getDeckCards().size());
		//counters for each type of card
		int numRooms=0;
		int numWeapons=0; 
		int numPeople=0;
		for (Card aCard : board.getDeckCards()) {
			//checks the card type for each card in the deck and increments the appropriate counter
			if (aCard.getCardType().equals(CardType.PERSON)) {
				numPeople++;
			}
			else if (aCard.getCardType().equals(CardType.WEAPON)) {
				numWeapons++; 
			}
			else numRooms++;
		}
		//checks if there's the correct number of cards for each type
		assertEquals(9, numRooms);
		assertEquals(6, numWeapons);
		assertEquals(6, numPeople);
		//temp card c 
		Card c = new Card();
		//checks if Miss Scarlett card is in the deck
		c.setCardType(CardType.PERSON);
		c.setName("Miss Scarlett");
		assertTrue(board.getDeckCards().contains(c));
		//checks if Dagger card is in the deck
		c.setCardType(CardType.WEAPON);
		c.setName("Dagger");
		assertTrue(board.getDeckCards().contains(c));
		//checks if Kitchen card is in the deck
		c.setCardType(CardType.ROOM);
		c.setName("Kitchen");
		assertTrue(board.getDeckCards().contains(c));
	}
	
	@Test
	void testDealingCards() {
		board.dealCards();
		//checks that the deck is empty after dealing cards. Ensures that all cards have been dealt.
		assertEquals(0, board.getDeckCards().size());
		//Hash set for storing the number of cards each person has
		Set numCards = new HashSet();
		for (Player p : board.getListPeople()) {
			numCards.add(p.getListOfCards().size());
		}
		//The number of cards a person has should only be, at most, a difference of 1. This checks to make sure that all players have about the same number of cards. The set should not have more than 2 elements in it.
		assertTrue(numCards.size()<=2);
		//creates a set and an arraylist of the cards each player has. If there is a difference in size of the list, that means there was probably a repeat that the hashset deleted. 
		Set listOfCards = new HashSet();
		ArrayList<Card> cards = new ArrayList<Card>();
		for (Player p : board.getListPeople()) {
			for (Card c : p.getListOfCards()) {
				cards.add(c);
				listOfCards.add(c);
			}
		}
		assertEquals(cards.size(), listOfCards.size());
	}
}
