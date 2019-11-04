/*
 * Authors: 
 * Mia Blanchard 
 * Bijan Chamanbahar
 */
package clueGame;
import java.util.*;
import java.awt.Color;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;

public class Board {
	
	private int numRows;
	private int numColumns; 
	public static final int MAX_BOARD_SIZE = 50;
	private BoardCell [][] board;
	Map<Character, String> legend;
	private Map<BoardCell, Set<BoardCell>> adjMatrix;
	private Set<BoardCell> targetsList;
	private Set<BoardCell> visitedList;
	private String boardConfigFile;
	private String roomConfigFile;
	private String personConfigFile;
	private String deckConfigFile;
	private ArrayList<Player> listPeople = new ArrayList<Player>();
	private ArrayList<Card> deckCards = new ArrayList<Card>();
	private ArrayList<Card> deck = new ArrayList<Card>();
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
		loadPersonConfigFile();
		loadDeckConfigFile();
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
	
	//set the file to get the list of people in the game
	public void setPersonFile(String txtFile) {
		personConfigFile = txtFile;
	}
	
	//set the file to get the list of cards
	public void setDeckFile(String txtFile) {
		deckConfigFile = txtFile;
	}
	
	//load the file to set up the deck of cards
	public void loadDeckConfigFile() throws IOException {
		try {
			BufferedReader br = new BufferedReader(new FileReader(deckConfigFile));
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
		deck = deckCards;
	}
	
	//load the file to set up the people
	public void loadPersonConfigFile() throws IOException {
		try {
			BufferedReader br = new BufferedReader(new FileReader(personConfigFile));
			String row;
			while ((row = br.readLine())!=null) {
				String [] data = row.split(",");
				//checks if the player is a human or a computer
				if (data[0].equals("Human")) {
					HumanPlayer human = new HumanPlayer();
					//set player info
					human.setPlayerName(data[1].substring(1));
					human.setColor(convertColor(data[2].substring(1)));
					human.setRow(Integer.parseInt(data[3].substring(1)));
					human.setCol(Integer.parseInt(data[4].substring(1)));
					//add to list of people
					listPeople.add(human);
				}
				else {
					ComputerPlayer computer = new ComputerPlayer();
					//set player info
					computer.setPlayerName(data[1].substring(1));
					computer.setColor(convertColor(data[2].substring(1)));
					computer.setRow(Integer.parseInt(data[3].substring(1)));
					computer.setCol(Integer.parseInt(data[4].substring(1)));
					//add to list of people
					listPeople.add(computer);
				}			
			}
		} catch(FileNotFoundException e){
			e.printStackTrace();
		}
	}
	
	public void dealCards() {
		//shuffle deck
		Collections.shuffle(deckCards, new Random());
		//counts which player we're dealing to
		int counter = 0;
		//iterator to go over the deck, so we can remove cards as we go
		Iterator<Card> it = deckCards.iterator();
		while (it.hasNext()) {
			//gives a card to a player
			getListPeople().get(counter).addACard(it.next());
			counter++;
			//if we've reached the last player, loop back to the first player
			if (counter>=getListPeople().size()) {
				counter = 0;
			}
			//remove card from deck
			it.remove();
		}
	}
	
	//converts string to color
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
	
	//gets the legend
	public Map<Character, String> getLegend() {
		return legend;
	}
	
	//gets the numRows
	public int getNumRows() {
		return numRows;
	}
	
	//gets the cell at a row and column
	public BoardCell getCellAt(int row, int col) {
		return board[row][col];
	}
	
	//gets the columns
	public int getNumColumns() {
		return numColumns;
	}
	
	//calculates the adjacencies
	public void calcAdj() {
		//create AdjMatrix
		adjMatrix = new HashMap<BoardCell, Set<BoardCell>>();
		for(int i = 0; i < board.length; i++) {
			for(int j = 0; j < board[i].length; j++) {
				Set<BoardCell> temp = new HashSet<BoardCell>();
				//if statements to check if it's on the edge of the board
				//also check if tile is a door facing the right way
				if (i<board.length-1) {
					//If the tile below has a door direction up and the tile is a walkway
					//If the tile below is a walkway and is not a room
					//If the tile is a door and the tile below is a walkway
					if((board[i+1][j].getDoorDirection() == DoorDirection.UP && board[i][j].isWalkway())|| 
					  (board[i+1][j].isWalkway() && !(board[i][j]).isRoom())||
					  (board[i][j].getDoorDirection() == DoorDirection.DOWN && board[i+1][j].isWalkway())) {
						temp.add(board[i+1][j]);	
					}		
				}
				//If the tile above has a door direction down and the tile is a walkway
				//If the tile above is a walkway and is not a room
				//If the tile is a door and the tile above is a walkway
				if (i>0) {
					if((board[i-1][j].getDoorDirection() == DoorDirection.DOWN && board[i][j].isWalkway()) ||
					(board[i-1][j].isWalkway()&& !(board[i][j]).isRoom()) ||
					(board[i][j].getDoorDirection() == DoorDirection.UP && board[i-1][j].isWalkway())){
						temp.add(board[i-1][j]);	
					}
				}
				//If the tile right has a door direction left and the tile is a walkway
				//If the tile right is a walkway and is not a room
				//If the tile is a door and the tile right is a walkway
				if (j<board[i].length-1) {
					if((board[i][j + 1].getDoorDirection() == DoorDirection.LEFT && board[i][j].isWalkway()) ||
					(board[i][j+1].isWalkway()&& !(board[i][j]).isRoom())||
					(board[i][j].getDoorDirection() == DoorDirection.RIGHT && board[i][j+1].isWalkway())) {
						temp.add(board[i][j+1]);	
					}	
				}
				//If the tile left has a door direction right and the tile is a walkway
				//If the tile left is a walkway and is not a room
				//If the tile is a door and the tile left is a walkway
				if (j>0) {
					if((board[i][j-1].getDoorDirection() == DoorDirection.RIGHT && board[i][j].isWalkway()) ||
					  (board[i][j-1].isWalkway()&& !(board[i][j]).isRoom()) ||
					  (board[i][j].getDoorDirection() == DoorDirection.LEFT && board[i][j-1].isWalkway())) {
						temp.add(board[i][j-1]);
					}	 
				}
				//puts the temporary set of targets into map
				adjMatrix.put(board[i][j], temp);
			}
		}
	}
	
	public boolean checkAccusation(Solution accusation) {
		return false;
	}
	
	public Set<BoardCell> getAdjList(int row, int col) {
		//returns the AdjMatrix at a boardcell
		BoardCell c = getCellAt(row, col);
		return adjMatrix.get(c);
	}
	
	public Set<BoardCell> getTargets() {
		//return target list
		return targetsList;
	}
	
	public void calcTargets(int row, int col, int numSteps) {
		BoardCell startCell = getCellAt(row, col);
		visitedList = new HashSet<BoardCell>();
		targetsList = new HashSet<BoardCell>();
		visitedList.add(startCell);
		//call recursive method to calc the targets
		findAllTargets(row, col, numSteps);
	}
	
	private void findAllTargets(int row, int col, int numSteps) {
		//recursive method used to find all targets given a cell and a number of steps
		for (BoardCell c : getAdjList(row, col)) {
			if (visitedList.contains(c)) {
				continue;
			}
			else {
				visitedList.add(c);
				if (numSteps==1) {
					targetsList.add(c);
				}
				else if (c.isDoorway()) {
					targetsList.add(c);
				}
				else {
					findAllTargets(c.getCol(), c.getRow(), numSteps-1);
				}
				visitedList.remove(c); 
			}
		}
	}
	
	//gets the list of people
	public ArrayList<Player> getListPeople() {
		return listPeople;
	}
	
	//gets the deck of cards
	public ArrayList<Card> getDeckCards(){
		return deckCards; 
	}

	public ArrayList<Card> getDeck() {
		return deck;
	}
	
	
}
