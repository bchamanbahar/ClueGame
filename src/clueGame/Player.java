package clueGame;
import java.awt.*; // BE SURE TO USE THIS IMPORT
//not the one Eclipse suggests
import java.lang.reflect.Field;
public class Player {
	
	private String playerName;
	private int row;
	private int column; 
	private Color color; 
	
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
	
	public Color getColor() {
		return color;
	}
	
	public int getRow() {
		return row;
	}
	
	public int getCol() {
		return column;
	} 
	
	public String getPlayerName() {
		return playerName;
	}
	
	public void setPlayerName(String name) {
		this.playerName = name;
	}
	
	public void setRow(int row) {
		this.row = row;
	}
	 
	public void setCol(int column) {
		this.column = column;
	}
	
	public void setColor(Color color) {
		this.color = color;
	}
	
}
