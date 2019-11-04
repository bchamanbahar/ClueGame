package clueGame;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class ComputerPlayer extends Player {
	private ArrayList<BoardCell> previousLocations = new ArrayList<BoardCell>();
	private ArrayList<Card> knownCards = new ArrayList<Card>();
	private ArrayList<Card> deckCards = new ArrayList<Card>();
	
	public ComputerPlayer() throws IOException {
		try {
			BufferedReader br = new BufferedReader(new FileReader("deck.txt"));
			String row;
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
		} catch(FileNotFoundException e){
			e.printStackTrace();
		}
	}
	
	public BoardCell pickLocation(Set<BoardCell> targets) {
		for (BoardCell c : targets) {
			if (c.isRoom()) {
				if (!previousLocations.isEmpty() && !(previousLocations.get(previousLocations.size()-1).equals(c))) {
					previousLocations.add(c);
					return c;
				}
			}
		}
		int randomNumber = new Random().nextInt(targets.size());
		int i = 0;
		for (BoardCell c : targets) {
			if (i == randomNumber) {
				previousLocations.add(c);
				return c;
			}
			i++;
		}
		return null;
	}
	
	public ArrayList<BoardCell> getPreviousLocations() {
		return previousLocations;
	}
	
	public boolean inPreviousLocations(BoardCell location) {
		for (BoardCell c : previousLocations) {
			if (c.equals(location)) {
				return true;
			}
		}
		return false;
	}

	public Solution makeAccusation() {
		ArrayList<Card> possibleCards = new ArrayList();
		for (Card c : deckCards) {
			if (!knownCards.contains(c)) {
				possibleCards.add(c);
			}
		}
		ArrayList<Card> possiblePeople = new ArrayList();
		ArrayList<Card> possibleRooms = new ArrayList();
		ArrayList<Card> possibleWeapons = new ArrayList();
		for (Card c : possibleCards) {
			if (c.getCardType() == CardType.PERSON) {
				possiblePeople.add(c);
			}
			if (c.getCardType() == CardType.ROOM) {
				possibleRooms.add(c);
			}
			if (c.getCardType() == CardType.WEAPON) {
				possibleWeapons.add(c);
			}
		}
		int randomPerson = new Random().nextInt(possiblePeople.size());
		int randomRoom = new Random().nextInt(possiblePeople.size());
		int randomWeapon = new Random().nextInt(possiblePeople.size());
		Solution accusation = new Solution(possiblePeople.get(randomPerson).getName(), possibleRooms.get(randomRoom).getName(), possibleWeapons.get(randomWeapon).getName());
		return accusation;
	}
	
	public void createSuggestion() {
		
	}
	
	public void setKnownCards(ArrayList<Card> cards) {
		knownCards = cards;
	}
}
