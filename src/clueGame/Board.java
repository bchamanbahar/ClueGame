/*
 * Authors: 
 * Mia Blanchard 
 * Bijan Chamanbahar
 */
package clueGame;
import java.util.*;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Board {
	private int numRows;
	private int numColumns; 
	public static final int MAX_BOARD_SIZE = 50;
	private BoardCell [][] board;
	private Map<Character, String> legend;
	private Map<BoardCell, Set<BoardCell>> adjMatrix;
	private Set<BoardCell> targets;
	private String boardConfigFile;
	private String roomConfigFile;
	// variable used for singleton pattern
	private static Board theInstance = new Board();
	// constructor is private to ensure only one can be created
	private Board() {
		
	}
	// this method returns the only Board
	public static Board getInstance() {
		return theInstance;
	}
	//initialize the board
	public void initialize() throws Exception {
		board = new BoardCell[MAX_BOARD_SIZE][MAX_BOARD_SIZE];
		int i=0;
		int j=0;
		try {
			//load boardconfig file
			BufferedReader br = new BufferedReader(new FileReader(boardConfigFile));
			String row;
			while ((row = br.readLine())!=null) {
				j=0;
				String []data = row.split(",");
				for (String s : data) {
					BoardCell cell = new BoardCell(i,j, s.charAt(0));
					if (s.length()>1) {
						//set door direction if there is a door
						if (s.charAt(1)== 'U') {
							cell.setDoor(DoorDirection.UP);
						}
						else if (s.charAt(1)== 'D') {
							cell.setDoor(DoorDirection.DOWN);
						}
						else if (s.charAt(1)== 'L') {
							cell.setDoor(DoorDirection.LEFT);
						}
						else if (s.charAt(1)== 'R') {
							cell.setDoor(DoorDirection.RIGHT);
						}
						else cell.setDoor(DoorDirection.NONE);
					}
					//give it the none direction if there isn't a door
					else cell.setDoor(DoorDirection.NONE);
					//insert into the door
					board[i][j] = cell; 
					j++;
				}
				i++;
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		numRows = i;
		numColumns = j;
		legend = new HashMap();
		try {
			//load roomconfig file
			BufferedReader br = new BufferedReader(new FileReader(roomConfigFile));
			String row;
			while ((row = br.readLine())!=null) {
				String [] data = row.split(",");
				//use the character as the key and insert into legend
				legend.put(data[0].charAt(0), data[1].substring(1));
			}
		}catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	//load the rooms
	public void loadRoomConfig() throws IOException, BadConfigFormatException {
		legend = new HashMap();
		try {
			//load roomconfig file
			BufferedReader br = new BufferedReader(new FileReader(roomConfigFile));			
			String row;
			while ((row = br.readLine())!=null) {
				String [] data = row.split(",");
				//use the character as the key and insert into legend
				legend.put(data[0].charAt(0), data[1].substring(1));
				//checks if it's a card or other
				if (data[2]!=" Card" && data[2]!=" Other") {
					throw new BadConfigFormatException();
				}
			}
		}catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	//load the board
	public void loadBoardConfig() throws BadConfigFormatException, IOException {
		board = new BoardCell[MAX_BOARD_SIZE][MAX_BOARD_SIZE];
		int i=0;
		int j=0;
		//set is used to make sure there's an equal number of commas on each line (meaning, an equal number of columns)
		Set commas = new HashSet();
		try {
			//load boardconfig file
			BufferedReader br = new BufferedReader(new FileReader(boardConfigFile));
			String row;
			while ((row = br.readLine())!=null) {
				j=0;
				String []data = row.split(",");
				commas.add(data.length);
				for (String s : data) {
					BoardCell cell = new BoardCell(i,j, s.charAt(0));
					//if the character is not in the legend, throw an exception
					if (!legend.containsKey(s.charAt(0))) {
						throw new BadConfigFormatException(s);
					}
					//set door direction if there is a door
					if (s.length()>1) {
						if (s.charAt(1)== 'U') {
							cell.setDoor(DoorDirection.UP);
						}
						else if (s.charAt(1)== 'D') {
							cell.setDoor(DoorDirection.DOWN);
						}
						else if (s.charAt(1)== 'L') {
							cell.setDoor(DoorDirection.LEFT);
						}
						else if (s.charAt(1)== 'R') {
							cell.setDoor(DoorDirection.RIGHT);
						}
						else cell.setDoor(DoorDirection.NONE);
					}
					else cell.setDoor(DoorDirection.NONE);
					board[i][j] = cell; 
					j++;
				}
				i++;
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		//if there is an unequal number of columns, throw exception
		if (commas.size()>1) {
			throw new BadConfigFormatException();
		}
	}
	//sets the files
	public void setConfigFiles(String csvFile, String txtFile) {
		boardConfigFile = csvFile;
		roomConfigFile = txtFile;
	}
	
	//gets the legend
	public Map<Character, String> getLegend() {
		return legend;
	}
	//gets the numRows
	public int getNumRows() {
		return numRows;
	}
	//gets the cell at a row and column
	public BoardCell getCellAt(int i, int j) {
		return board[i][j];
	}
	//gets the columns
	public int getNumColumns() {
		return numColumns;
	}
	
}
