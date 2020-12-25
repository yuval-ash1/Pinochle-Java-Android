package edu.ramapo.yashken1.pinochle;


import java.io.*; 
import java.util.*;

public class Deck implements Serializable{
	
	//Class Constants:
	
	
	//Class Variables:
	private List<Card> deck = new ArrayList<Card>();
	private Card trump = new Card();
	
	//GUI Components:

	/**
	 This function is responsible for setting the deck object's deck.
	 @param d1- a list of cards representing the deck to set.
	 */
	void set_deck(List<Card> d1){
		deck = d1;
	}

	/**
	 This function is responsible for setting the deck object's trump card.
	 @param t1 A card type representing the trump card to set.
	 */
	void set_trump(Card t1){
		trump = t1;
	}
	
	//Constructor:

	/**
	 The deck Class's default constructor, initializes the deck- a vector of Card types
	 */
	Deck(){
		Card[] cards1 = {new Card('9', 's'), new Card('9', 'c'), new Card('9', 'd'), new Card('9', 'h'),
		new Card('9', 's'), new Card('9', 'c'), new Card('9', 'd'), new Card('9', 'h'),
		new Card('x', 's'), new Card('x', 'c'), new Card('x', 'd'), new Card('x', 'h'),
		new Card('x', 's'), new Card('x', 'c'), new Card('x', 'd'), new Card('x', 'h'),
		new Card('j', 's'), new Card('j', 'c'), new Card('j', 'd'), new Card('j', 'h'),
		new Card('j', 's'), new Card('j', 'c'), new Card('j', 'd'), new Card('j', 'h'),
		new Card('q', 's'), new Card('q', 'c'), new Card('q', 'd'), new Card('q', 'h'),
		new Card('q', 's'), new Card('q', 'c'), new Card('q', 'd'), new Card('q', 'h'),
		new Card('k', 's'), new Card('k', 'c'), new Card('k', 'd'), new Card('k', 'h'),
		new Card('k', 's'), new Card('k', 'c'), new Card('k', 'd'), new Card('k', 'h'),
		new Card('a', 's'), new Card('a', 'c'), new Card('a', 'd'), new Card('a', 'h'),
		new Card('a', 's'), new Card('a', 'c'), new Card('a', 'd'), new Card('a', 'h')
		};

		for(Card item: cards1) {
			deck.add(item);
		}
	}

	/**
	 A deck constructor, creates a deck of cards using an existing deck of strings.
	 @param deck1 a list of string type, containing cards that are represented with strings
	 		-this is used to initialize a deck that was read from a file.
	 */
	Deck(List<String> deck1){
		for(int i = 0; i < deck1.size(); i++){
				String tempS = deck1.get(i);
				Card tempCard = new Card(tempS.charAt(0), tempS.charAt(1));
				deck.add(tempCard);
		}
	}

	
	//Event handlers:

	/**
	 This function is responsible to randomly shuffle the local deck.
	 */
	void shuffle_deck() {
		Collections.shuffle(deck);
	}

	/**
	 This function deals the cards at the beginning on the round
		 4 cards a player at a time (x3)
	 	then it sets the trump card to be the next card in the stock
	 @param playerList- a Pointer of player type to a plater array- holds the
	 		two players of the game
	 @param nextPlayerIndex- an int type passed by value which serves as the index to the playerList
	 */
	void deal_cards(Player[] playerList, Int nextPlayerIndex) {
		int player2_index = (nextPlayerIndex.get_value() + 1) % 2;
		for (int i = 0; i < 3; i++)
		{
			for (int j = 0; j < 4; j++)
			{
				playerList[nextPlayerIndex.get_value()].add_to_hand(deck.get(0));
				deck.remove(0);
			}
			for (int j = 0; j < 4; j++)
			{
				playerList[player2_index].add_to_hand(deck.get(0));
				deck.remove(0);
			}
		}
		trump.set_type(deck.get(0).get_type());
		trump.set_suit(deck.get(0).get_suit());
		deck.remove(0);
	}

	/**
	 This function deals cards to the players after each turn:
		 Winner of the last turn gets a card first
	 	 If no cards left for the second player- the trump card is dealt
	 @param playerList- a Pointer of player type to a plater array- holds the
	 two players of the game
	 @param nextPlayerIndex- an int type passed by value which serves as the index to the playerList
	 */
	void deal_turn_cards(Player[] playerList, Int nextPlayerIndex) {
		//If the deck is not empty
		if (!(deck.isEmpty())) {
			//Giving a card to the winner
			playerList[nextPlayerIndex.get_value()].add_to_hand(deck.get(0));
			//Erasing that card from the deck
			deck.remove(0);
			int player2Idx = (nextPlayerIndex.get_value() + 1) % 2;
			//If the deck is still not empty
			if (!(deck.isEmpty()))
			{
				//Second player gets the next card in the deck
				playerList[player2Idx].add_to_hand(deck.get(0));
				deck.remove(0);
			}
			else
			{
				//If deck is now empty- second player gets the trump
				playerList[player2Idx].add_to_hand(trump);
				trump.set_type(' ');
			}
		}
		//Else- do nothing
	}
	
	//Selectors:
	/**
	 This function is responsible to retrieve the deck of the current round as a constant.
	 @return deck- The local deck- A list of Cards.
	 */
	List<Card> get_deck() {
		return deck;
	}

	/**
	 This function is responsible to retrieve the trump of the current round as a constant
	 @return trump- A card that holds the trump's suit and type
	 */
	Card get_trump() {
		return trump;	
	}
	
	//Mutators:

	/**
	 Defines the local trump  card
	 	-this is used to set the stump when reading a game from a file
	 @param trump_card- a string type which hold the type and suit of the trump card.
	 */
	void set_trump(String trump_card){
		if(trump_card.length() == 1){
			Card tempT = new Card(' ', trump_card.charAt(0));
			trump = tempT;
		}
		else{
			Card tempT = new Card(trump_card.charAt(0), trump_card.charAt(1));
			trump = tempT;
		}
	}
	
	//main() method for debugging:
	public static void main(String[] args){
	}
	
	//Any utility (private) methods:
	
	
	//Other functions:

	/**
	 This function prints all the cards in the stock pile
	 */
	public void print_deck() {
		if (!(deck.isEmpty()))
		{
			for(int i = 0; i < deck.size(); i++){
				deck.get(i).print_card();
				System.out.printf(" ");
			}
			System.out.println();
		}
	}

	/**
	 This function prints the trump card in upper-level
	 */
	void print_trump() {
		trump.print_card();
	}

}