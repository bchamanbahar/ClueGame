package clueGame;

import java.awt.Color; // BE SURE TO USE THIS IMPORT
import java.awt.Graphics;
//not the one Eclipse suggests
import java.lang.reflect.Field;
import java.util.*;

public class Player {

	private String playerName;
	private static int LENGTH = 30;
	int row;
	int column;
	private Color color;
	private ArrayList<Card> listOfCards = new ArrayList<Card>();

	public Card disproveSuggestion(Solution suggestion) {
		ArrayList<Card> listOfMatchingCards = new ArrayList<Card>();
		for (Card c : listOfCards) {
			//if we have a matching card, add it to our list of matching cards
			if (c.getName() == suggestion.getPerson() || c.getName() == suggestion.getRoom()
					|| c.getName() == suggestion.getWeapon()) {
				listOfMatchingCards.add(c);
			}
		}
		//if we have 1 matching card, return it
		if (listOfMatchingCards.size() == 1) {
			return listOfMatchingCards.get(0);
		}
		//if we have multiple matching cards, return a random one
		else if (listOfMatchingCards.size() > 1) {
			int randomCard = new Random().nextInt(listOfMatchingCards.size());
			return listOfMatchingCards.get(randomCard);
		}
		//else, return null
		else return null;
	}

	public Color convertColor(String strColor) {
		Color color;
		try {
			// We can use reflection to convert the string to a color
			Field field = Class.forName("java.awt.Color").getField(strColor.trim());
			color = (Color) field.get(null);
		} catch (Exception e) {
			color = null; // Not defined
		}
		return color;
	}

	// gets color
	public Color getColor() {
		return color;
	}

	// gets row
	public int getRow() {
		return row;
	}

	// gets column
	public int getCol() {
		return getColumn();
	}

	// gets player name
	public String getPlayerName() {
		return playerName;
	}

	// sets player name
	public void setPlayerName(String name) {
		this.playerName = name;
	}

	// sets row
	public void setRow(int row) {
		this.row = row;
	}

	// sets column
	public void setCol(int column) {
		this.setColumn(column);
	}

	// sets color
	public void setColor(Color color) {
		this.color = color;
	}

	// sets the list of cards
	public void setListOfCards(ArrayList<Card> cards) {
		this.listOfCards = cards;
	}

	// gets the list of cards each player has
	public ArrayList<Card> getListOfCards() {
		return this.listOfCards;
	}

	// gives a card to a player
	public void addACard(Card c) {
		this.listOfCards.add(c);
	}

	public int getColumn() {
		return column;
	}

	public void setColumn(int column) {
		this.column = column;
	}

	public void draw(Graphics g) {
		g.setColor(Color.BLACK);
		g.drawOval(column*LENGTH, row*LENGTH, LENGTH, LENGTH);
		g.setColor(color);
		g.fillOval(column*LENGTH, row*LENGTH, LENGTH, LENGTH);
		
	}

	public void setLength(int i) {
		LENGTH = i;
	}
}