package clueGame;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class ClueGame extends JFrame{
	private Board board;
	public ClueGame(){
		setSize(1200, 900);
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
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		menuBar.add(createFileMenu());
		board.dealCards();
		CardGui Cards = new CardGui(this);

		// paintComponent will automatically be called 1 time
		add(board, BorderLayout.CENTER);
		add(Cards, BorderLayout.EAST);
		add(CGui, BorderLayout.PAGE_END);
	}
	
	
	private JMenu createFileMenu(){
		JMenu menu = new JMenu("File"); 
		menu.add(createFileExitItem());
		menu.add(createDetectiveNotes());
		return menu;
	}
	
	private JMenuItem createDetectiveNotes() {
		JMenuItem item = new JMenuItem("Detective Notes");
		class MenuItemListener implements ActionListener {
			public void actionPerformed(ActionEvent e)
			{
				DetectiveNotesGui gui;
				try {
					gui = new DetectiveNotesGui();
					gui.setVisible(true);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				
			}
		}
		item.addActionListener(new MenuItemListener());
		return item;
	}

	private JMenuItem createFileExitItem() {
		JMenuItem item = new JMenuItem("Exit");
		class MenuItemListener implements ActionListener {
			public void actionPerformed(ActionEvent e)
			{
				System.exit(0);
			}
		}
		item.addActionListener(new MenuItemListener());
		return item;
	}
	public static void main(String[] args) throws Exception {
		ClueGame game = new ClueGame();
		PopupGui dialog = new PopupGui();
		game.setVisible(true);
	

		
	}
}	