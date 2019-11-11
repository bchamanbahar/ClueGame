package clueGame;

import java.awt.GridLayout;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

public class DetectiveNotesGui extends JDialog {
	// combo boxes for the guesses
	private JComboBox<String> people, weapons, rooms;
	// check boxes to keep track of seen cards
	private JCheckBox listPeople, listWeapons, listRooms;
	private static Board board = Board.getInstance();

	public DetectiveNotesGui() throws Exception {
		// I didn't want to hard code the list of people, rooms, and weapons, (because
		// they could possibly change depending on the configuration files) so there's
		// an instance of board that I initialize. This bit of code will likely be
		// deleted, since I mainly just needed it there for testing purposes.

		// set the file names to use my config files
		board.setConfigFiles("Clue_Layout.csv", "legend.txt");
		// Initialize will load BOTH config files
		board.setPersonFile("people.txt");
		board.setDeckFile("deck.txt");
		board.initialize();
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
		// add the option for being "Unsure" of your guess
		people.addItem("Unsure");
		weapons.addItem("Unsure");
		rooms.addItem("Unsure");
		people.setBorder(new TitledBorder(new EtchedBorder(), "Person Guess"));
		rooms.setBorder(new TitledBorder(new EtchedBorder(), "Room Guess"));
		weapons.setBorder(new TitledBorder(new EtchedBorder(), "Weapon Guess"));
		setTitle("Detective Notes");
		setSize(800, 800);
		setLayout(new GridLayout(3, 2));
		JPanel panel = createPeopleCheckList();
		// add people checklist and person guess
		add(panel);
		add(people);
		// add room checklist and room guess
		panel = createRoomsCheckList();
		add(panel);
		add(rooms);
		// add weapon checklist and weapon guess
		panel = createWeaponsCheckList();
		add(panel);
		add(weapons);
	}

	// creates checklist for list of people in our deck
	private JPanel createPeopleCheckList() {
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(3, 2));
		// for every card that is a person, they will be added to the list of check
		// boxes for people
		for (Card c : board.deck) {
			if (c.getCardType() == CardType.PERSON) {
				listPeople = new JCheckBox();
				listPeople.setText(c.getName());
				panel.add(listPeople);
			}
		}
		panel.setBorder(new TitledBorder(new EtchedBorder(), "People"));
		return panel;
	}

	// creates checklist for list of rooms in our deck
	private JPanel createRoomsCheckList() {
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(3, 2));
		// for every card that is a room, they will be added to the list of check
		// boxes for rooms
		for (Card c : board.deck) {
			if (c.getCardType() == CardType.ROOM) {
				listRooms = new JCheckBox();
				listRooms.setText(c.getName());
				panel.add(listRooms);
			}
		}
		panel.setBorder(new TitledBorder(new EtchedBorder(), "Rooms"));
		return panel;
	}

	// creates checklist for list of weapons in our deck
	private JPanel createWeaponsCheckList() {
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(3, 2));
		// for every card that is a weapon, they will be added to the list of check
		// boxes for weapons
		for (Card c : board.deck) {
			if (c.getCardType() == CardType.WEAPON) {
				listWeapons = new JCheckBox();
				listWeapons.setText(c.getName());
				panel.add(listWeapons);
			}
		}
		panel.setBorder(new TitledBorder(new EtchedBorder(), "Weapons"));
		return panel;
	}

	public static void main(String[] args) throws Exception {
		DetectiveNotesGui gui = new DetectiveNotesGui();
		gui.setVisible(true);
	}

}
