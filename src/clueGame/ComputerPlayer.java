package clueGame;

import java.util.*;


public class ComputerPlayer extends Player {
	private ArrayList<BoardCell> previousLocations = new ArrayList<BoardCell>();
	private ArrayList<Card> knownCards = new ArrayList<Card>();
	
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
		return null;
	}
	
	public void createSuggestion() {
		
	}
	
	public void setKnownCards(ArrayList<Card> cards) {
		knownCards = cards;
		
	}
}
