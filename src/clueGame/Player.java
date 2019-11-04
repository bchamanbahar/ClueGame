package clueGame;
import java.awt.Color; // BE SURE TO USE THIS IMPORT
//not the one Eclipse suggests
import java.lang.reflect.Field;
import java.util.*;
public class Player {
	
	private String playerName;
	int row;
	int column; 
	private Color color; 
	private ArrayList<Card> listOfCards = new ArrayList<Card>();
	
	public Card disproveSuggestion(Solution suggestion) {
		return null;
	}
	
	public Color convertColor(String strColor) {
		 Color color;
		 try {
		 // We can use reflection to convert the string to a color
		 Field field = Class.forName("java.awt.Color").getField(strColor.trim());
		 color = (Color)field.get(null);
		 } catch (Exception e) {
		 color = null; // Not defined
		 }
		 return color;
	}
	
	//gets color
	public Color getColor() {
		return color;
	}
	
	//gets row
	public int getRow() {
		return row;
	}
	
	//gets column
	public int getCol() {
		return getColumn();
	} 
	
	//gets player name
	public String getPlayerName() {
		return playerName;
	}
	
	//sets player name
	public void setPlayerName(String name) {
		this.playerName = name;
	}
	
	//sets row
	public void setRow(int row) {
		this.row = row;
	}
	 
	//sets column
	public void setCol(int column) {
		this.setColumn(column);
	}
	
	//sets color
	public void setColor(Color color) {
		this.color = color;
	}
	
	//sets the list of cards
	public void setListOfCards(ArrayList<Card> cards) {
		this.listOfCards = cards;
	}
	
	//gets the list of cards each player has
	public ArrayList<Card> getListOfCards(){
		return this.listOfCards;
	}
	
	//gives a card to a player
	public void addACard(Card c) {
		this.listOfCards.add(c);
	}

	public int getColumn() {
		return column;
	}

	public void setColumn(int column) {
		this.column = column;
	}
	
}
