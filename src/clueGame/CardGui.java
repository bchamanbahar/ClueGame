package clueGame;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

public class CardGui extends JPanel{
	private static Board board = Board.getInstance();
	private ClueGame cg;
	private ArrayList<Card> listOfCards;
	private JLabel card;
	public CardGui(ClueGame cg) {
		this.cg = cg;
		setPreferredSize(new Dimension(500,500));
		setLayout(new FlowLayout());
		JPanel panel = makeCards();
		add(panel);
	}
	
	private JPanel makeCards() {
		JPanel panel = new JPanel();
		listOfCards = board.listPeople.get(0).getListOfCards();	
		panel.setLayout(new GridLayout(3,1));
		JPanel people = new JPanel();
		people.setBorder(new TitledBorder(new EtchedBorder(), "People"));
		people.setLayout(new FlowLayout());
		JPanel weapons = new JPanel();
		weapons.setBorder(new TitledBorder(new EtchedBorder(), "Weapons"));
		weapons.setLayout(new FlowLayout());
		JPanel rooms = new JPanel();
		rooms.setBorder(new TitledBorder(new EtchedBorder(), "Rooms"));
		rooms.setLayout(new FlowLayout());
		for (Card c : listOfCards) {
			if (c.getCardType() == CardType.PERSON) {
				card = new JLabel(c.getName());
				card.setHorizontalAlignment(SwingConstants.CENTER);
				card.setVerticalAlignment(SwingConstants.CENTER);
				card.setBorder(new LineBorder(Color.BLACK));
				card.setPreferredSize(new Dimension(100,100));
				card.setBackground(Color.WHITE);
				card.setOpaque(true);
				people.add(card);
			}
			if (c.getCardType() == CardType.ROOM) {
				card = new JLabel(c.getName());
				card.setHorizontalAlignment(SwingConstants.CENTER);
				card.setVerticalAlignment(SwingConstants.CENTER);
				card.setBorder(new LineBorder(Color.BLACK));
				card.setPreferredSize(new Dimension(100,100));
				card.setOpaque(true);
				card.setBackground(Color.WHITE);
				rooms.add(card);
			}
			if (c.getCardType() == CardType.WEAPON) {
				card = new JLabel(c.getName());
				card.setHorizontalAlignment(SwingConstants.CENTER);
				card.setVerticalAlignment(SwingConstants.CENTER);
				card.setBorder(new LineBorder(Color.BLACK));
				card.setPreferredSize(new Dimension(100,100));
				card.setOpaque(true);
				card.setBackground(Color.WHITE);
				weapons.add(card);
			}
		}
		panel.setBorder(new TitledBorder(new EtchedBorder(), "My Cards"));
		panel.add(people);
		panel.add(rooms);
		panel.add(weapons);
		return panel;
	}
}
