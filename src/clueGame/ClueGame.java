package clueGame;

import javax.swing.JFrame;

import java.awt.BorderLayout;


public class ClueGame extends JFrame{
	private Board board;
	public ClueGame(){
		setSize(1200, 800);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		board = Board.getInstance();
		ControlGui CGui = new ControlGui();

		// set the file names to use my config files
		board.setConfigFiles("Clue_Layout.csv", "legend.txt");		
		// Initialize will load BOTH config files 
		board.setPersonFile("people.txt");
		board.setDeckFile("deck.txt");
		try {
			board.initialize();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// paintComponent will automatically be called 1 time
		add(board, BorderLayout.CENTER);
		add(CGui, BorderLayout.SOUTH);
	}
	public static void main(String[] args) throws Exception {
		ClueGame game = new ClueGame();
		DetectiveNotesGui gui = new DetectiveNotesGui();
		gui.setVisible(true);
		game.setVisible(true);
		
	}
}	