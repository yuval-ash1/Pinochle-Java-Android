package edu.ramapo.yashken1.pinochle;

import java.io.*; 
import java.util.*;

class Human extends Player implements Serializable{
 	//Class Constants:
	
	
	//Class Variables:
	String card_choice;
	//GUI Components:
	/**
	 This function is responsible to set the human's card choice
	 @param c1- a string type representing the suit and type of the card the user chose.
	 */
	void set_card_choice(String c1){
		card_choice = c1;
	}
	//Constructor:
	/**
	 A constructor for the Human object.
	 @param name- a string type representing the computer player's name.
	 */
	Human(String name) {
		super(name);
	}

	//Event handlers:

	/**
	 This function is responsible to handle human playing a turn in the pinochle game.
	 @param trump- a Card object representing the trump card of the current round
	 @param lead_type- a character type representing thr type of the lead card (of the current turn)
	 @param lead_suit- a character type representing thr suit of the lead card (of the current turn)
	 @return userCard- a Card type representing the card played by the human
	 */
	Card play(Card trump, char lead_type, char lead_suit){
		System.out.printf("%s please play a card: ", name);

		char t, s;
		t = card_choice.charAt(0);
		s = card_choice.charAt(1);
		Card userCard = new Card(t, s);
		int index = find_idx(userCard, hand);
		hand.remove(index);
		
		return userCard;
	}

	/**
	 This function is responsible play a meld- validate meld, then add points to the human's score, and
	 push the right cards into the meld_cards vector, and right names into the meld_names
	 into the right vector.
	 @param trump- Card type that hold's the trump card's suit and type.
	 @param meld_vector- a list of Card objects representing the card to perform the melds with
	 @param meld_name- a string type representing the name of the meld the human is trying to make.
	 */
	void perform_a_meld(Card trump, List<Card> meld_vector, String meld_name) {
		int meld_points = try_a_meld(meld_vector, meld_name, trump);

		if (meld_points != 0)
		{
			add_to_round_score(meld_points);
		}
		else
		{
			//need to return some string/num here to indicate meld invalid.
			System.out.printf("you have already performed this specific meld\n");
		}
	}

	/**
	 This function is responsible to help the human player by advising the best meld to perform.
	 @param trump- Card type that hold's the trump card's suit and type.
	 @return a String type holding the computer's advice for the human player.
	 */
	String help_with_meld(Card trump) {
				String response = "";
		String meld = best_meld(hand, trump);
		if (meld == "No possible melds")
		{
			response = "No possible melds at this point";
		}
		else
		{
			response = "The Best meld you can make is: " + meld;
		}
		return response;
	}
	
	//Selectors:
	
	
	//Mutators:
	
	
	//main() method for debugging:
	public static void main(String[] args){
	}
	
	//Any utility (private) methods:

}
