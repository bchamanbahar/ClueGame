package clueGame;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class ClueGame extends JFrame implements MouseListener {
	private Board board;
	ControlGui CGui;
	//lists of people, weapons, and rooms to pick from
	JComboBox<String> people, weapons, rooms;
	Solution suggest = new Solution();
	JDialog suggestion;
	boolean canceled = false;

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
		//lists of people, weapons, and rooms to pick from
		people = new JComboBox<String>();
		weapons = new JComboBox<String>();
		rooms = new JComboBox<String>();
		for (Card c1 : board.deck) {
			// get list of cards, and add them to its respective combo box
			if (c1.getCardType() == CardType.PERSON) {
				people.addItem(c1.getName());
			}
			if (c1.getCardType() == CardType.ROOM) {
				rooms.addItem(c1.getName());
			}
			if (c1.getCardType() == CardType.WEAPON) {
				weapons.addItem(c1.getName());
			}
		}
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
		if (!board.pickedLocation) {
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
					//if the player moved to a room, prompt to make a suggestion
					if (board.getCellAt(board.currentPlayer.getRow(), board.currentPlayer.getCol()).isRoom()) {
						suggestion = new JDialog(this, "Make a Guess", Dialog.ModalityType.APPLICATION_MODAL);
						suggestion.setLayout(new GridLayout(4, 2));
						suggestion.add(new JTextField("Your Room"));
						JTextField room = new JTextField(board.legend.get(board
								.getCellAt(board.currentPlayer.getRow(), board.currentPlayer.getCol()).getInitial()));
						suggest.setRoom(board.legend.get(board
								.getCellAt(board.currentPlayer.getRow(), board.currentPlayer.getCol()).getInitial()));
						room.setEditable(false);
						suggestion.add(room);
						suggestion.add(new JTextField("Person"));
						suggestion.add(people);
						suggestion.add(new JTextField("Weapon"));
						suggestion.add(weapons);
						JButton submit = new JButton("Submit");
						JButton cancel = new JButton("Cancel");
						submit.addActionListener(new submitButton());
						cancel.addActionListener(new cancelButton());
						suggestion.add(submit);
						suggestion.add(cancel);
						suggestion.setSize(500, 500);
						suggestion.setVisible(true);
						//if the player did not hit the cancel button when creating a guess, handle the suggestion
						if (!canceled) {
							board.handleSuggestion(suggest, board.currentPlayer);
						}
						canceled = false;
					}
					// repaint the control panel, so it updates
					CGui.repaintWindow();
					break;
				}
			}
		}
		else {
			JFrame frame = new JFrame();
			JOptionPane.showMessageDialog(frame, "You have already finished your turn.", "Error",
					JOptionPane.WARNING_MESSAGE);
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

	private class submitButton implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			suggest.setPerson((String) people.getSelectedItem());
			suggest.setWeapon((String) weapons.getSelectedItem());
			suggestion.dispose();
		}
	}

	private class cancelButton implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			canceled = true;
			suggestion.dispose();
		}
	}

	public static void main(String[] args) throws Exception {
		ClueGame game = new ClueGame();
		PopupGui dialog = new PopupGui();
		game.setVisible(true);
	}

}