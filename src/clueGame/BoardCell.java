/*
 * Authors: 
 * Mia Blanchard 
 * Bijan Chamanbahar
 */
package clueGame;

import java.awt.Color;
import java.awt.Graphics;

public class BoardCell {
private int col;
private int row;
private String name = "";
private static int LENGTH = 30;
private char initial; 
private DoorDirection door;
private Room room;

public void setDoor(DoorDirection door) {
	this.door = door;
}

public BoardCell(int col, int row) {
	super();
	//BoardCell has a row and column
	this.col = col;
	this.row = row;		
}

//equals method for BoardCells
@Override
public boolean equals(Object o) {
	//compares columns and rows
	if (this.col == (((BoardCell) o).getCol()) && this.row == (((BoardCell) o).getRow())){
		return true;
	}
	else return false;
}


public void setCol(int col) {
	this.col = col;
}

public void setRow(int row) {
	this.row = row;
}

public int getCol() {
	return col;
}

public int getRow() {
	return row;
}

public BoardCell(int col, int row, char i) {
	super();
	//BoardCell has a row and column
	this.col = col;
	this.row = row;		
	this.initial = i;
}

//returns true if the cell is a walkway
public boolean isWalkway() {
	if (this.initial == 'W') {
		return true;
	}
	else return false;
}

//returns true if the cell is a room
public boolean isRoom() {
	if (this.initial == 'W') {
		return false;
	}
	else if (this.initial == 'X') {
		return false;
	}
	else return true;
}

//returns true if the cell is a doorway
public boolean isDoorway() {
	if (door == DoorDirection.NONE) {
		return false;
	}
	else return true;
}
public boolean isName() {
	return(room != Room.NONE);
	
}

//gets the door direction
public DoorDirection getDoorDirection() {
	return door;
}


//gets the initial
public char getInitial() {
	return initial; 
}

public void draw(Graphics g) {
	if(isWalkway()) {
		g.setColor(Color.YELLOW);
		g.fillRect(row*LENGTH, col*LENGTH, LENGTH, LENGTH);
		g.setColor(Color.BLACK);
		g.drawRect(row*LENGTH, col*LENGTH, LENGTH, LENGTH);
	}else if(isDoorway()){
		g.setColor(Color.GRAY);
		g.fillRect(row*LENGTH, col*LENGTH, LENGTH, LENGTH);
		
		g.setColor(Color.BLUE);
		switch (door) {
		case UP: 

			g.fillRect(row*LENGTH, col*LENGTH, LENGTH, LENGTH / 10);
			break;
		case DOWN:
			g.fillRect(row*LENGTH, col*LENGTH + 9* LENGTH / 10, LENGTH, LENGTH / 10);
			break;
		case LEFT:
			g.fillRect(row*LENGTH, col*LENGTH, LENGTH / 10, LENGTH);
			break;
		case RIGHT:
			g.fillRect(row*LENGTH + 9* LENGTH / 10, col*LENGTH, LENGTH / 10, LENGTH);
			break;

		}
	}else { 
		g.setColor(Color.GRAY);
		g.fillRect(row*LENGTH, col*LENGTH, LENGTH, LENGTH);
		if(isRoom()) {
			g.setColor(Color.BLUE);
			g.drawString(name, row*LENGTH, col*LENGTH);
		}
	}
	
}

public void setRoom(Room c) {
	room = c;
	switch (room) {
	case CONSERVATORY:
		name = "CONSERVATORY";
		break;
	case BILLIARD:
		name = "BILLIARD";
		break;
	case LIBRARY:
		name = "LIBRARY";
		break;
	case STUDY:
		name = "STUDY";
		break;
	case BALLROOM:
		name = "BALLROOM";
		break;
	case HALL:
		name = "HALL";
		break;
	case KITCHEN:
		name = "KITCHEN";
		break;
	case DINING:
		name = "DINING";
		break;
	case LOUNGE:
		name = "LOUNGE";
		break;
	}
}


}
