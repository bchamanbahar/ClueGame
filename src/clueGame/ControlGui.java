/*
 * Authors: 
 * Mia Blanchard 
 * Bijan Chamanbahar
 */
package clueGame;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

public class ControlGui extends JPanel {
	private JTextField output;
	

	public ControlGui() {
		setLayout(new FlowLayout());
		//add the "Whose turn?" 
		JPanel panel = createWhoseTurn();
		panel.setPreferredSize(new Dimension(250,60));
		add(panel);
		//add the next player and make an accusation buttons
		panel = createButtons();
		panel.setPreferredSize(new Dimension(450,80));
		add(panel);
		//add the die roll
		panel = createDieRoll();	
		panel.setPreferredSize(new Dimension(100,50));
		add(panel);
		//add the guess
		panel = createGuess();
		panel.setPreferredSize(new Dimension(350,70));
		add(panel);
		//add the guess result
		panel = createGuessResult();
		panel.setPreferredSize(new Dimension(250,50));
		add(panel);	
	}
	
	//Makes dialog box for whose turn it is
	private JPanel createWhoseTurn() {
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(1, 1));
		output = new JTextField(20);
		output.setEditable(false);
		panel.add(output);	
		panel.setBorder(new TitledBorder(new EmptyBorder(10,10,10,10), "Whose Turn?"));
		return panel;
	}

	//Makes dialog box for what the die roll is
	private JPanel createDieRoll() {
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(1, 2));
		JLabel nameLabel = new JLabel("Roll");
		output = new JTextField(20);
		output.setEditable(false);
		panel.add(nameLabel);
		panel.add(output);
		panel.setBorder(new TitledBorder(new EtchedBorder(), "Die"));
		return panel;
	}
	
	//Creates buttons for going to the next player and making an accusation
	private JPanel createButtons() {
		JButton nextPlayer = new JButton("Next player");
		JButton accusation = new JButton("Make an accusation");
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(1, 2));
		panel.add(nextPlayer);
		panel.add(accusation);
		return panel;
	}
	
	//Makes dialog box for the guess
	private JPanel createGuess() {
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(2, 2));
		JLabel nameLabel = new JLabel("Guess");
		output = new JTextField(20);
		output.setEditable(false);
		panel.add(nameLabel);
		panel.add(output, BorderLayout.SOUTH);
		panel.setBorder(new TitledBorder(new EtchedBorder(), "Guess"));
		return panel;
	}
	
	//Makes dialog box for the guess result
	private JPanel createGuessResult() {
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(1, 2));	
		JLabel nameLabel = new JLabel("Response");
		output = new JTextField(20);
		output.setEditable(false);
		panel.add(nameLabel);
		panel.add(output);
		panel.setBorder(new TitledBorder(new EtchedBorder(), "Guess Result"));
		return panel;
	}

	public static void main(String[] args) {
		//creates the display box
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("GUI Example");
		frame.setSize(750, 225);
		ControlGui gui = new ControlGui();
		frame.add(gui, BorderLayout.CENTER);
		//makes display visable
		frame.setVisible(true);
	}

}
