package edu.ramapo.yashken1.pinochle;


import java.io.Serializable;

public class Card implements Serializable {
	
	//Class Constants:
	
	//Class Variables:
	private char type;
	private char suit;
	
	//GUI Components:
	
	
	//Constructors:
	/** 
   Default constructor, initializes type and suit to 'e', empty
   @param -
   @return an initialized Card object
   */
	public Card(){
		type = 'e';
		suit = 'e';
	}
	
	/** 
   For each function, one-line description of the function
   @param type1- a character type representing the type of the card
   @param suit1- a character type representing the suit of the card
   @return an initialized Card object
   */
	Card(char type1, char suit1){
		type = type1;
		suit = suit1;
	}
	//Event handlers:
	//Selectors:
	/**
	 Retrieves the type of the card
	 @param -
	 @return A character type representing the type of the card.
	 */
	public char get_type() {
		return type;
	}
	int get_card_index() {
		//Initializing because compiler requeres. Cards are validated so this should not make a difference
		int card_index = 0;
		char typeOrder[] = { '9', 'j', 'q', 'k', 'x', 'a' };
		for (short index = 0; index < 6; index++)
		{
			if (type == typeOrder[index]) {
				card_index = index;
			}
		}
		return card_index;
	}

	/**
	 Retrieves the suit of the card.
	 @param -
	 @return A character type representing the suit of the card.
	 */
	public char get_suit() {
		return suit;
	}
	//Mutators:

	/**
	 Changes the type of the card according to a given parameter- used to update the trump
	 type after it is defined.
	 @param type1- a character that represents the type of the card.
	 */
	public void set_type(char type1) {
	type = type1;
	}

	/**
	 changes the suit of the card according to a given parameter- used to update the trump
	 suit after it is defined.
	 @param suit1- a character that represents the suit of the card.
	 */
	public void set_suit(char suit1) {
	suit = suit1;
	}
	
	//Other functions:

	/**
	 This function prints a card in upperlevel (type,suit).
	 @param -
	 */
	public void print_card() {
		System.out.printf("%c%c", Character.toUpperCase(type), Character.toUpperCase(suit));
	}

	/**
	 This function is responsible to check if two cards are the equivalent.
	 @param card1- Card type passed by reference.
	 @return a boolean type. true if the cards are the same, false if not.
	 */
	public boolean equals(Card card1) {
		if (card1 == null) return false;
		if (card1 == this) return true;
		if (!(card1 instanceof Card)) return false;
		Card o = (Card) card1;
		return (o.type == this.type && o.suit == this.suit);
	}
	
	//main() method for debugging:
	public static void main(String[] args){
	}
	
	
	//Any utility (private) methods:
	
  
}