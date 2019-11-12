package clueGame;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class PopupGui extends JFrame{

	public PopupGui(){
		JFrame frame = new JFrame();
		JOptionPane.showMessageDialog(frame, "You are Miss Scarlett, press Next Player to begin play" , "Welcome to Clue", JOptionPane.INFORMATION_MESSAGE);
	}
	
	
}
