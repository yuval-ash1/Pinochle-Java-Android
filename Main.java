package edu.ramapo.yashken1.pinochle;

import java.io.*;
import java.util.*;

class  Main implements Serializable{

	// Class Constants

	// Class Variables
	int user_input;
	Game g1 = new Game();
	Human p1;
	Computer p2;
	String fileName;

	// GUI Components




	// Constructor
	/**
	 This is the default constructor for the main class, it serves as the "empty initializer" for
	 the Main object.
	 */
	Main() {

	}

	// Event handlers
	/**
	 This function is responsible for initializing the game, it is called after the user input was
	 set.
	 */
	void initialize_game(){
		if (user_input == 1) {
			p1 = new Human("Human");
			p2 = new Computer("Computer");
			g1 = new Game(p1, p2);
		}
		else {

			//System.out.printf("Please enter a file name: ");

			fileName = "/storage/sdcard1/" + fileName;

			p1 = new Human("Human");
			p2 = new Computer("Computer");

			g1 = new Game(p1, p2, fileName);

		}
	}

	// Selectors
	/**
	 This function is responsible for retrieving the Game object.
	 @return a Game object (the local Game object).
	 */
	Game get_game_object(){
		return g1;
	}

	/**
	 This function is responsible for retrieving the Human object.
	 @return a Human object (the local Human object).
	 */
	Human get_human_object(){
		return p1;
	}

	/**
	 This function is responsible for retrieving the Computer object.
	 @return a Computer object (the local Computer object).
	 */
	Computer get_computer_object(){
		return p2;
	}

	// Mutators
	/**
	 This function is responsible to set the name if the file to whatever the user inputs.
	 @param name- a string type representing the file name inputted by the user.
	 */
	void set_fileName(String name){
		fileName = name;
	}

	/**
	 This function is responsible to set the user input at the beginning of the game.
	 @param input- an integer type representing theuser's choice (1 for new game, 2 for loaded game).
	 */
	void set_user_input(int input){
		user_input = input;
	}

	// main() method for debugging

	public static void main(String[] args) {
		Main main1 = new Main();
		System.out.println("Hello welcome to the Pinochle game");
		System.out.println("To Start a game please press 1");
		System.out.println("To load a game please press 2");
	}

	// Any utility (private) methods

}