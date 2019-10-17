/*
 * Authors: 
 * Mia Blanchard 
 * Bijan Chamanbahar
 */
package clueGame;

public class BadConfigFormatException extends Exception{
	//exception for bad configurations 
	public BadConfigFormatException() {
		super("Error: Bad configuration format");
	}
	public BadConfigFormatException(String s) {
		super("Error: " + s + " is not in the legend");
	}
}
