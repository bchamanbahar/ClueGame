package clueGame;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.SwingUtilities;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class ClueGame extends JFrame implements MouseListener {
	private Board board;
	ControlGui CGui;

	public ClueGame() {
		setSize(1200, 900);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		board = Board.getInstance();
		board.addMouseListener(this);
		// set the file names to use my config files
		board.setConfigFiles("Clue_Layout.csv", "legend.txt");
		// Initialize will load BOTH config files
		board.setPersonFile("people.txt");
		board.setDeckFile("deck.txt");
		try {
			board.initialize();
		} catch (Exception e) {
			e.printStackTrace();
		}
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		menuBar.add(createFileMenu());
		board.dealCards();
		CardGui Cards = new CardGui(this);
		CGui = new ControlGui();
		// paintComponent will automatically be called 1 time
		add(board, BorderLayout.CENTER);
		add(Cards, BorderLayout.EAST);
		add(CGui, BorderLayout.PAGE_END);
	}

	// create file menu
	private JMenu createFileMenu() {
		JMenu menu = new JMenu("File");
		menu.add(createFileExitItem());
		menu.add(createDetectiveNotes());
		return menu;
	}

	// creates option to open the detective notes from file menu
	private JMenuItem createDetectiveNotes() {
		JMenuItem item = new JMenuItem("Detective Notes");
		class MenuItemListener implements ActionListener {
			public void actionPerformed(ActionEvent e) {
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

	// creates option to exit from file menu
	private JMenuItem createFileExitItem() {
		JMenuItem item = new JMenuItem("Exit");
		class MenuItemListener implements ActionListener {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		}
		item.addActionListener(new MenuItemListener());
		return item;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// checks where mouse was clicked and divides it by the length of the
		// boardcells, in order to figure out which row and column they clicked on
		int mouseRow = (int) e.getX() / Board.LENGTH;
		int mouseCol = (int) e.getY() / Board.LENGTH;
		for (BoardCell c : board.targetsList) {
			// if they clicked on a target in our targets list, move there
			if (mouseRow == c.getRow() && mouseCol == c.getCol()) {
				board.pickedLocation = true;
				board.currentPlayer.setCol((int) (e.getX() / Board.LENGTH));
				board.currentPlayer.setRow((int) (e.getY() / Board.LENGTH));
				board.targetsList.clear();
				board.repaint();
				// repaint the control panel, so it updates the dice roll
				CGui.repaintWindow();
				break;
			}
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {

	}

	@Override
	public void mouseReleased(MouseEvent e) {

	}

	@Override
	public void mouseEntered(MouseEvent e) {

	}

	@Override
	public void mouseExited(MouseEvent e) {

	}

	public static void main(String[] args) throws Exception {
		ClueGame game = new ClueGame();
		PopupGui dialog = new PopupGui();
		game.setVisible(true);
	}

}