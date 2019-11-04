/*
 * Authors: 
 * Mia Blanchard 
 * Bijan Chamanbahar
 */
package clueGame;

public class BoardCell {
private int col;
private int row;
private char initial; 
private DoorDirection door;

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

//gets the door direction
public DoorDirection getDoorDirection() {
	return door;
}

//gets the initial
public char getInitial() {
	return initial; 
}


}
