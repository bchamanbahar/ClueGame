package clueGame;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class ComputerPlayer extends Player {
	private ArrayList<BoardCell> previousLocations = new ArrayList<BoardCell>();
	private ArrayList<Card> knownCards = new ArrayList<Card>();
	private static Board board = Board.getInstance();

	public ComputerPlayer() throws IOException {

	}

	public BoardCell pickLocation(Set<BoardCell> targets) {
		for (BoardCell c : targets) {
			// if there is a room in our list of targets, and we did not previously enter
			// the room, we must select it
			if (c.isRoom()) {
				if (previousLocations.size() > 1 && !(previousLocations.get(previousLocations.size() - 1).equals(c))
						&& !(previousLocations.get(previousLocations.size() - 2).equals(c))) {
					// add to our list of previous locations (since we're moving there)
					previousLocations.add(c);
					board.pickedLocation = true;
					return c;
				}
			}
		}
		// else, pick a random location from our list of targets
		int randomNumber = new Random().nextInt(targets.size());
		int i = 0;
		for (BoardCell c : targets) {
			if (i == randomNumber) {
				// add to our list of previous locations (since we're moving there)
				previousLocations.add(c);
				board.pickedLocation = true;
				return c;
			}
			i++;
		}
		return null;
	}

	public ArrayList<BoardCell> getPreviousLocations() {
		return previousLocations;
	}

	public ArrayList<Card> getKnownCards() {
		return knownCards;
	}

	public Solution makeAccusation() {
		// possible cards for the accusations are every card that we have not seen from
		// the deck. Thus, we should take out our list of known cards, and pick from
		// that
		ArrayList<Card> possibleCards = new ArrayList();
		for (Card c : board.deck) {
			// if we've seen the card, take it out
			if (!knownCards.contains(c)) {
				possibleCards.add(c);
			}
		}
		// lists for our possible people, rooms, and weapons
		ArrayList<Card> possiblePeople = new ArrayList();
		ArrayList<Card> possibleRooms = new ArrayList();
		ArrayList<Card> possibleWeapons = new ArrayList();
		for (Card c : possibleCards) {
			// grab all the possible people it could be
			if (c.getCardType() == CardType.PERSON) {
				possiblePeople.add(c);
			}
			// grab all the possible rooms it could be
			if (c.getCardType() == CardType.ROOM) {
				possibleRooms.add(c);
			}
			// grab all the possible weapons it could be
			if (c.getCardType() == CardType.WEAPON) {
				possibleWeapons.add(c);
			}
		}
		// pick a random person, room, and weapon from our list
		int randomPerson = new Random().nextInt(possiblePeople.size());
		int randomRoom = new Random().nextInt(possiblePeople.size());
		int randomWeapon = new Random().nextInt(possiblePeople.size());
		// our accusation is the random person, the random room, and the random weapon
		Solution accusation = new Solution(possiblePeople.get(randomPerson).getName(),
				possibleRooms.get(randomRoom).getName(), possibleWeapons.get(randomWeapon).getName());
		return accusation;
	}

	public Solution createSuggestion() {
		// possible cards for the solutions are every card that we have not seen from
		// the deck. Thus, we should take out our list of known cards, and pick from
		// that
		ArrayList<Card> possibleCards = new ArrayList();
		for (Card c : board.deck) {
			// if we've seen the card, take it out
			if (!knownCards.contains(c)) {
				possibleCards.add(c);
			}
		}
		// lists for our possible people and weapons
		ArrayList<Card> possiblePeople = new ArrayList();
		ArrayList<Card> possibleWeapons = new ArrayList();
		for (Card c : possibleCards) {
			// grab all the possible people it could be
			if (c.getCardType() == CardType.PERSON) {
				possiblePeople.add(c);
			}
			// grab all the possible weapons it could be
			if (c.getCardType() == CardType.WEAPON) {
				possibleWeapons.add(c);
			}
		}
		// pick a random person and weapon from our list
		int randomPerson = new Random().nextInt(possiblePeople.size());
		int randomWeapon = new Random().nextInt(possiblePeople.size());
		// get the location of our computer player, and pick the room that they are in
		BoardCell location = board.getCellAt(row, column);
		String room = board.legend.get(location.getInitial());
		// our suggestion is the random person, the room we're in, and the random weapon
		if (possibleWeapons.size() == 1) {
			if (possiblePeople.size() == 1) {
				Solution suggestion = new Solution(possiblePeople.get(0).getName(), room,
						possibleWeapons.get(0).getName());
				return suggestion;
			}
			Solution suggestion = new Solution(possiblePeople.get(randomPerson).getName(), room,
					possibleWeapons.get(0).getName());
			return suggestion;
		}
		if (possiblePeople.size() == 1) {
			Solution suggestion = new Solution(possiblePeople.get(0).getName(), room,
					possibleWeapons.get(randomWeapon).getName());
			return suggestion;
		}
		Solution suggestion = new Solution(possiblePeople.get(randomPerson).getName(), room,
				possibleWeapons.get(randomWeapon).getName());
		return suggestion;
	}

	public void setKnownCards(ArrayList<Card> cards) {
		knownCards = cards;
	}

	public void addKnownCard(Card card) {
		knownCards.add(card);
	}
}
