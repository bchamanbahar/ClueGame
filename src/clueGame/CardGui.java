package clueGame;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

public class CardGui extends JPanel{
	private static Board board = Board.getInstance();
	private ClueGame cg;
	private ArrayList<Card> listOfCards;
	public CardGui(ClueGame cg) {
		this.cg = cg;
		setPreferredSize(new Dimension(100,800));
		listOfCards = board.listPeople.get(0).getListOfCards();		
		setLayout(new GridLayout(2,2));
		JPanel panel = makeCards();
		add(panel);
	}
	
	private JPanel makeCards() {
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(1,1));
		for (Card c : listOfCards) {
			JPanel card = new JPanel();
			card.setBorder(new TitledBorder(new EtchedBorder(), c.getName()));
			panel.add(card);
		}
		panel.setBorder(new TitledBorder(new EtchedBorder(), "My Cards"));
		return panel;
	}
}
