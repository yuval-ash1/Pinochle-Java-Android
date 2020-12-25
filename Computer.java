package edu.ramapo.yashken1.pinochle;

import java.io.*; 
import java.util.*;

class Computer extends Player implements Serializable{
 	//Class Constants:
	
	
	//Class Variables:
	private String strategy;
	
	//GUI Components:
	/**
	 This function is responsible to retrieve the conputer's strategy to the GUI.
	 @return A string type containing the computer's strategy.
	 */
	String getComputerStrategy(){
		return strategy;
	}
	
	//Constructor:
	/**
	 A constructor for the Computer object
	 @param name- a string type representing the computer player's name.
	 */
	Computer(String name) {
		super(name);
	}
	
	//Event handlers:
	/**
	 This function is responsible to play a turn in the pinochle game (computer)
	 @param trump- a Card object representing the trump card of the current round
	 @param lead_type- a character type representing thr type of the lead card (of the current turn)
	 @param lead_suit- a character type representing thr suit of the lead card (of the current turn)
	 @return computerCard- a Card type representing the card played by the computer
	 */
	Card play(Card trump, char lead_type, char lead_suit){
		String c1;
		char t, s;
		if (lead_type == 'e' && lead_suit == 'e')
		{
			c1 = strategy_lead(trump);
			strategy = getStrategy();
		}
		else
		{
			c1 = strategy_chase(trump, lead_type, lead_suit);
			strategy = getStrategy();
		}
		t = c1.charAt(0);
		s = c1.charAt(1);
		Card computerCard = new Card(t, s);
		int index = find_idx(computerCard, hand);
		hand.remove(index);
		
		System.out.printf("%s's choice: ", name);
		computerCard.print_card();
		System.out.printf("\n\n");

		return computerCard;
	}

	/**
	 This function is responsible play a meld- validate meld, then add points to the computer's score, and
	 	push the right cards into the meld_cards vector, and right names into the meld_names
	 	into the right vector.
	 @param trump- Card type that hold's the trump card's suit and type.
	 @param meld_vector- a list of Card objects representing the card to perform the melds with
	 @param meld_name- a string type representing the name of the meld the computer is trying to make.
	 */
	void perform_a_meld(Card trump, List<Card> meld_vector, String meld_name) {

		meld_name = best_meld(hand, trump);
		int meld_points = try_a_meld(hand, meld_name, trump);

		if (meld_points != 0)
		{
			add_to_round_score(meld_points);
		}
	}

	/**
	 This function is not doing anything in the Computer class, it is used in the human class
	 @return An string type- enpty.
	 */
	String help_with_meld(Card trump) {
		return "";
		//Nothing to do here, only human can get help with melds
	}
	
	
	
	//Selectors:
	
	
	//Mutators:
	
	
	//main() method for debugging:
	public static void main(String[] args){
		
	}
	
	//Any utility (private) methods:

}