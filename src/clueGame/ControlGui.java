/*
 * Authors: 
 * Mia Blanchard 
 * Bijan Chamanbahar
 */
package clueGame;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

public class ControlGui extends JPanel {
	private JTextField output;
	private static Board board = Board.getInstance();
	JPanel dieRoll;
	JDialog accusation;
	Solution accuse = new Solution();
	JComboBox<String> people, weapons, rooms;
	boolean canceled = false;

	public ControlGui() {
		people = new JComboBox<String>();
		weapons = new JComboBox<String>();
		rooms = new JComboBox<String>();
		for (Card c : board.deck) {
			// get list of cards, and add them to its respective combo box
			if (c.getCardType() == CardType.PERSON) {
				people.addItem(c.getName());
			}
			if (c.getCardType() == CardType.ROOM) {
				rooms.addItem(c.getName());
			}
			if (c.getCardType() == CardType.WEAPON) {
				weapons.addItem(c.getName());
			}
		}
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		// add the "Whose turn?"
		JPanel panel = createWhoseTurn();
		c.gridwidth = 1;
		c.weightx = 0.5;
		c.gridx = 0;
		c.gridy = 0;
		add(panel, c);
		// add the next player and make an accusation buttons
		panel = createButtons();
		c.ipady = 40;
		c.gridwidth = 2;
		c.gridx = 1;
		c.gridy = 0;
		add(panel, c);
		// add the die roll
		dieRoll = createDieRoll();
		c.gridwidth = 1;
		c.gridx = 0;
		c.gridy = 1;
		add(dieRoll, c);
		// add the guess
		panel = createGuess();
		c.gridwidth = 1;
		c.gridx = 1;
		c.gridy = 1;
		add(panel, c);
		// add the guess result
		panel = createGuessResult();
		c.gridwidth = 1;
		c.gridx = 2;
		c.gridy = 1;
		add(panel, c);
	}

	// Makes dialog box for whose turn it is
	private JPanel createWhoseTurn() {
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(1, 1));
		output = new JTextField(board.currentPlayer.getPlayerName());
		output.setEditable(false);
		panel.add(output);
		panel.setBorder(new TitledBorder(new EmptyBorder(10, 10, 10, 10), "Whose Turn?"));
		return panel;
	}

	// Makes dialog box for what the die roll is
	private JPanel createDieRoll() {
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(1, 2));
		JLabel nameLabel = new JLabel("Roll");
		output = new JTextField(String.valueOf(board.dieRoll));
		output.setEditable(false);
		panel.add(nameLabel);
		panel.add(output);
		panel.setBorder(new TitledBorder(new EtchedBorder(), "Die"));
		return panel;
	}

	// Creates buttons for going to the next player and making an accusation
	private JPanel createButtons() {
		JButton nextPlayer = new JButton("Next player");
		JButton accusation = new JButton("Make an accusation");
		JPanel panel = new JPanel();
		nextPlayer.addActionListener(new nextPlayerButton());
		accusation.addActionListener(new accusationButton());
		panel.setLayout(new GridLayout(1, 2));
		panel.add(nextPlayer);
		panel.add(accusation);
		return panel;
	}

	// Makes dialog box for the guess
	private JPanel createGuess() {
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(2, 2));
		JLabel nameLabel = new JLabel("Guess");
		output = new JTextField(
				board.suggest.getPerson() + " " + board.suggest.getWeapon() + " " + board.suggest.getRoom());
		output.setEditable(false);
		panel.add(nameLabel);
		panel.add(output, BorderLayout.SOUTH);
		panel.setBorder(new TitledBorder(new EtchedBorder(), "Guess"));
		return panel;
	}

	// Makes dialog box for the guess result
	private JPanel createGuessResult() {
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(1, 2));
		JLabel nameLabel = new JLabel("Response");
		output = new JTextField(board.guess.getName());
		output.setEditable(false);
		panel.add(nameLabel);
		panel.add(output);
		panel.setBorder(new TitledBorder(new EtchedBorder(), "Guess Result"));
		return panel;
	}

	private class nextPlayerButton implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			// if the human player has not picked a location, give error message
			if (!board.pickedLocation) {
				JFrame frame = new JFrame();
				JOptionPane.showMessageDialog(frame, "A target has not been selected.", "Error",
						JOptionPane.WARNING_MESSAGE);
			}
			// if they have picked a target, then go to next player
			else {
				board.goToNextPlayer();
				repaintWindow();
				// if next player is a computer, run algorithm for picking a target, and move
				// them there
				if (board.currentPlayer instanceof ComputerPlayer) {
					var chance = Math.random();
					if (chance < 0.1) {
						accuse = ((ComputerPlayer) board.currentPlayer).makeAccusation();
						if (board.checkAccusation(accuse)) {
							JFrame frame = new JFrame();
							JOptionPane.showMessageDialog(frame,
									"The computer guessed correctly! They guessed " + accuse.getPerson() + " in the "
											+ accuse.getRoom() + " with the " + accuse.getWeapon() + ".",
									"Computer won!", JOptionPane.INFORMATION_MESSAGE);
							System.exit(0);
						} else {
							JFrame frame = new JFrame();
							JOptionPane.showMessageDialog(frame,
									"The computer's guess was incorrect. They guessed " + accuse.getPerson()
											+ " in the " + accuse.getRoom() + " with the " + accuse.getWeapon() + ".",
									"Incorrect", JOptionPane.INFORMATION_MESSAGE);
							board.pickedLocation = true;
							board.targetsList.clear();
							board.repaint();
						}
					} else if (((ComputerPlayer) board.currentPlayer).getKnownCards().size() == board.deck.size()) {
						accuse = ((ComputerPlayer) board.currentPlayer).makeAccusation();
						if (board.checkAccusation(accuse)) {
							JFrame frame = new JFrame();
							JOptionPane.showMessageDialog(frame,
									"The computer guessed correctly! They guessed " + accuse.getPerson() + " in the "
											+ accuse.getRoom() + " with the " + accuse.getWeapon() + ".",
									"Computer won!", JOptionPane.INFORMATION_MESSAGE);
							System.exit(0);
						} else {
							JFrame frame = new JFrame();
							JOptionPane.showMessageDialog(frame,
									"The computer's guess was incorrect. They guessed " + accuse.getPerson()
											+ " in the " + accuse.getRoom() + " with the " + accuse.getWeapon() + ".",
									"Incorrect", JOptionPane.INFORMATION_MESSAGE);
							board.pickedLocation = true;
							board.targetsList.clear();
							board.repaint();
						}
					} else {
						BoardCell c = ((ComputerPlayer) board.currentPlayer).pickLocation(board.targetsList);
						board.currentPlayer.setRow(c.getCol());
						board.currentPlayer.setCol(c.getRow());
						board.targetsList.clear();
						board.repaint();
						if (board.getCellAt(board.currentPlayer.getRow(), board.currentPlayer.getCol()).isRoom()) {
							Solution sugg = new Solution();
							sugg = ((ComputerPlayer) board.currentPlayer).createSuggestion();
							board.handleSuggestion(sugg, board.currentPlayer);
							repaintWindow();
						}
					}

				}
			}

		}
	}

	private class accusationButton implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (board.pickedLocation) {
				JFrame frame = new JFrame();
				JOptionPane.showMessageDialog(frame, "You have already finished your turn.", "Error",
						JOptionPane.WARNING_MESSAGE);
			}
			accusation = new JDialog(accusation, "Make an Accusation", Dialog.ModalityType.APPLICATION_MODAL);
			accusation.setLayout(new GridLayout(4, 2));
			accusation.add(new JTextField("Room"));
			accusation.add(rooms);
			accusation.add(new JTextField("Person"));
			accusation.add(people);
			accusation.add(new JTextField("Weapon"));
			accusation.add(weapons);
			JButton submit = new JButton("Submit");
			JButton cancel = new JButton("Cancel");
			submit.addActionListener(new submitButton());
			cancel.addActionListener(new cancelButton());
			accusation.add(submit);
			accusation.add(cancel);
			accusation.setSize(500, 500);
			accusation.setVisible(true);
			if (!canceled) {
				if (board.checkAccusation(accuse)) {
					JFrame frame = new JFrame();
					JOptionPane.showMessageDialog(frame, "Congratulations, you guessed correctly!", "You've won!",
							JOptionPane.INFORMATION_MESSAGE);
					System.exit(0);
				} else {
					JFrame frame = new JFrame();
					JOptionPane.showMessageDialog(frame, "Unfortunately, your guess was incorrect.", "Incorrect",
							JOptionPane.INFORMATION_MESSAGE);
					board.pickedLocation = true;
					board.repaint();
				}
			}
			canceled = false;
		}
	}

	// repaints the window, in order to update dice roll each turn
	public void repaintWindow() {
		removeAll();
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		// add the "Whose turn?"
		JPanel panel = createWhoseTurn();
		c.gridwidth = 1;
		c.weightx = 0.5;
		c.gridx = 0;
		c.gridy = 0;
		add(panel, c);
		// add the next player and make an accusation buttons
		panel = createButtons();
		c.ipady = 40;
		c.gridwidth = 2;
		c.gridx = 1;
		c.gridy = 0;
		add(panel, c);
		// add the die roll
		dieRoll = createDieRoll();
		c.gridwidth = 1;
		c.gridx = 0;
		c.gridy = 1;
		add(dieRoll, c);
		// add the guess
		panel = createGuess();
		c.gridwidth = 1;
		c.gridx = 1;
		c.gridy = 1;
		add(panel, c);
		// add the guess result
		panel = createGuessResult();
		c.gridwidth = 1;
		c.gridx = 2;
		c.gridy = 1;
		add(panel, c);
		this.validate();
		this.repaint();
	}

	private class submitButton implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			accuse.setPerson((String) people.getSelectedItem());
			accuse.setWeapon((String) weapons.getSelectedItem());
			accuse.setRoom((String) rooms.getSelectedItem());
			accusation.dispose();
		}
	}

	private class cancelButton implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			canceled = true;
			accusation.dispose();
		}
	}

	public static void main(String[] args) {
		// creates the display box
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(750, 225);
		ControlGui gui = new ControlGui();
		frame.add(gui, BorderLayout.CENTER);
		// makes display visible
		frame.setVisible(true);
	}

}
