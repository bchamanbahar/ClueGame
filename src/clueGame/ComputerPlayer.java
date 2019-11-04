package clueGame;

import java.util.*;


public class ComputerPlayer extends Player {
	private ArrayList<BoardCell> previousLocations = new ArrayList<BoardCell>();
	
	public BoardCell pickLocation(Set<BoardCell> targets) {
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

	public void makeAccusation() {
		
	}
	public void createSuggestion() {
		
	}
}
