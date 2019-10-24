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
	private Set<BoardCell> targetsList;
	private Set<BoardCell> visitedList;
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
		visitedList = new HashSet<BoardCell>();
		targetsList = new HashSet<BoardCell>();
		loadRoomConfig();
		loadBoardConfig();
		calcAdj();
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
				if (!data[2].contains("Card") && !data[2].contains("Other")) {
					throw new BadConfigFormatException();
				}
			}
		}catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	//load the board
	public void loadBoardConfig() throws BadConfigFormatException, IOException {
		BoardCell [][] temp = new BoardCell[MAX_BOARD_SIZE][MAX_BOARD_SIZE];
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
				for (String dataString : data) {
					BoardCell cell = new BoardCell(i,j, dataString.charAt(0));
					//if the character is not in the legend, throw an exception
					if (!legend.containsKey(dataString.charAt(0))) {
						throw new BadConfigFormatException(dataString);
					}
					//set door direction if there is a door
					if (dataString.length()>1) {
						if (dataString.charAt(1)== 'U') {
							cell.setDoor(DoorDirection.UP);
						}
						else if (dataString.charAt(1)== 'D') {
							cell.setDoor(DoorDirection.DOWN);
						}
						else if (dataString.charAt(1)== 'L') {
							cell.setDoor(DoorDirection.LEFT);
						}
						else if (dataString.charAt(1)== 'R') {
							cell.setDoor(DoorDirection.RIGHT);
						}
						else cell.setDoor(DoorDirection.NONE);
					}
					else cell.setDoor(DoorDirection.NONE);
					temp[i][j] = cell; 
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
		numRows = i;
		numColumns = j;
		board = new BoardCell[i][j];
		for (int k = 0; k<i; k++) {
			for (int l= 0; l<j; l++) {
				board[k][l] = temp[k][l];
			}
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
	
	public void calcAdj() {
		//create AdjMatrix
		adjMatrix = new HashMap<BoardCell, Set<BoardCell>>();
		for(int i = 0; i < board.length; i++) {
			for(int j = 0; j < board[i].length; j++) {
				Set<BoardCell> temp = new HashSet<BoardCell>();
				//if statements to check if it's on the edge of the board
				//also check if tile is a door facing the right way
				if (i<board.length-1) {
					if((board[i+1][j].getDoorDirection() == DoorDirection.UP && board[i][j].isWalkway())|| 
					  (board[i+1][j].isWalkway() && !(board[i][j]).isRoom())||
					  (board[i][j].getDoorDirection() == DoorDirection.DOWN && board[i+1][j].isWalkway())) {
						temp.add(board[i+1][j]);	
					}		
				}
				
				if (i>0) {
					if((board[i-1][j].getDoorDirection() == DoorDirection.DOWN && board[i][j].isWalkway()) ||
					(board[i-1][j].isWalkway()&& !(board[i][j]).isRoom()) ||
					(board[i][j].getDoorDirection() == DoorDirection.UP && board[i-1][j].isWalkway())){
						temp.add(board[i-1][j]);	
					}
					
				}
				
				if (j<board[i].length-1) {
					if((board[i][j + 1].getDoorDirection() == DoorDirection.LEFT && board[i][j].isWalkway()) ||
					(board[i][j+1].isWalkway()&& !(board[i][j]).isRoom())||
					(board[i][j].getDoorDirection() == DoorDirection.RIGHT && board[i][j+1].isWalkway())) {
						temp.add(board[i][j+1]);	
					}
						
				}
				
				if (j>0) {
					if((board[i][j-1].getDoorDirection() == DoorDirection.RIGHT && board[i][j].isWalkway()) ||
					  (board[i][j-1].isWalkway()&& !(board[i][j]).isRoom()) ||
					  (board[i][j].getDoorDirection() == DoorDirection.LEFT && board[i][j-1].isWalkway())) {
						temp.add(board[i][j-1]);
					}
							 
				}
				//puts into map
				adjMatrix.put(board[i][j], temp);
			}
		}
	}
	
	public Set<BoardCell> getAdjList(int i, int j) {
		//returns the AdjMatrix at a boardcell
		BoardCell c = getCellAt(i, j);
		return adjMatrix.get(c);
	}
	
	public Set<BoardCell> getTargets() {
		//return target list
		return targetsList;
	}
	
	public void calcTargets(int i, int j, int k) {
		BoardCell startCell = getCellAt(i, j);
		visitedList = new HashSet<BoardCell>();
		targetsList = new HashSet<BoardCell>();
		visitedList.add(startCell);
		//call recursive method to calc the targets
		findAllTargets(i, j, k);
		
	}
	
	private void findAllTargets(int i, int j, int k) {
		//recursive method used to find all targets given a cell and a number of steps
		for (BoardCell c : getAdjList(i, j)) {
			if (visitedList.contains(c)) {
				continue;
			}
			else {
				visitedList.add(c);
				if (k==1) {
					targetsList.add(c);
				}
				else if (c.isDoorway()) {
					targetsList.add(c);
				}
				else {
					findAllTargets(c.getCol(), c.getRow(), k-1);
				}
				visitedList.remove(c); 
			}
		}
	}
}
