package edu.ramapo.yashken1.pinochle;

import java.io.*; 
import java.util.*;

abstract class Player implements Serializable{
	//Class Constants:
	
	
	//Class Variables:
	protected String name;
	protected int round_score, game_score;
	String strategy;
	protected List<Card> hand = new ArrayList<Card>();
	protected List<Card> capture_pile = new ArrayList<Card>();
	protected List<String> previous_melds_names = new ArrayList<String>();
	protected List<String> previous_melds_cards = new ArrayList<String>();
	
	//GUI Components:
	/**
	 This functions is returning the computer's strategy so it can be printed when computer is playing
	 	and when user is asking for help.
	 @return a String type holding the computer's choice and justification.
	 */
	String getStrategy(){
		return strategy;
	}
	
	//Constructor:
	/**
	 The constructor for the Player class- initializing the player's name & score.
	 @param name1- a String type representing the name of the player.
	 */
	Player(String name1) {
		name = name1;
		game_score = 0;
		round_score = 0;
	}

	//Event handlers:
	/**
	 All of these are abstract classes- "pure virtual" that aer defined in the Computer and Human objects
	 */
	abstract Card play(Card trump, char lead_type, char lead_suit);
	abstract void perform_a_meld(Card trump, List<Card> meld_vector, String meld_name);
	abstract String help_with_meld(Card trump);

	/**
	 This function is contains the logic of the computer's strategy as lead player.
	 @param trump- a Card type holding the trump card of the current round.
	 @return a string type containing the the best card to play as a lead player.
	 */
	String strategy_lead(Card trump) {
		String best_card;
		String strCard = "";
		List<Card> newHand = new ArrayList<Card>();
		newHand = remove_meld_cards(trump);

		List<String> non_trumps = new ArrayList<String>();
		Card tempCard;
		for (int i = 0; i < newHand.size(); i++) {
			tempCard = newHand.get(i);
			if (Character.toLowerCase(tempCard.get_suit()) != trump.get_suit()) {
				strCard = "";
				strCard += tempCard.get_type();
				strCard += tempCard.get_suit();
				//Pushing to the List all the non-trumps in newHand
				non_trumps.add(strCard);
			}
		}
		if (!non_trumps.isEmpty()) {
			best_card = non_trumps.get(0);
			for (int i = 1; i < non_trumps.size(); i++) {
				//if a non-trump with higher precedence is found-
				if (find_card_index(best_card.charAt(0)) < find_card_index(non_trumps.get(i).charAt(0))) {
					//this is the new best card.
					best_card = non_trumps.get(i);
				}
			}
			strategy = "Strategy: playing the highest non trump card in hand: " + best_card.toUpperCase();
			System.out.printf("Strategy: playing the highest non trump card in hand: %s\n", best_card.toUpperCase());
			return best_card;

		} else      //If no non-trumps in newHand- look for the lowest trump in newHand.
		{
			//Setting best card to be first card in newHand (should be trump b/c no non-trumps)
			best_card = Character.toString(newHand.get(0).get_type());
			best_card += newHand.get(0).get_suit();
			//Iterating through newHand
			for (int i = 1; i < newHand.size(); i++) {
				//If card's type is of lower precedence:
				if (find_card_index(best_card.charAt(0)) > find_card_index(newHand.get(i).get_type())) {
					//Best card will now contain the better card.
					best_card = Character.toString(newHand.get(i).get_type());
					best_card += newHand.get(i).get_suit();
				}
			}
			strategy = "Strategy: no non-trumps in hand, playing the lowest trump in hand: " + best_card.toUpperCase();
			System.out.printf("Strategy: no non-trumps in hand, playing the lowest trump in hand: %s\n", best_card.toUpperCase());
			return best_card;
		}
	}

	/**
	 This function is contains the logic of the computer's strategy as chase player.
	 @param trump- a Card type holding the trump card of the current round.
	 @param lead_type- a char type containing the type  of the lead card.
	 @param lead_suit- char type containing the  suit of the lead card.
	 @return a string type containing the the best card to play as a chase player.
	 */
	String strategy_chase(Card trump, char lead_type, char lead_suit) {
		String best_card;
		List<Card> newHand = new ArrayList<Card>();
		newHand = remove_meld_cards(trump);

		//If lead is of trump suit
		if (lead_suit == trump.get_suit()) {
			List<Character> trump_cards = new ArrayList<Character>();
			for (int i = 0; i < newHand.size(); i++) {
				if (newHand.get(i).get_suit() == trump.get_suit()) {
					//Pushing to the List all the trump cards in newHand with higher precedence than the lead
					if (find_card_index(newHand.get(i).get_type()) > find_card_index(lead_type)) {
						trump_cards.add(newHand.get(i).get_type());
					}
				}
			}
			//If there are trump cards with higher precedence than lead- find the lowest one.
			if (!trump_cards.isEmpty()) {
				//At this point we know all of these cards can beat the lead, we are trying to find the least "valuable" option to win the turn
				best_card = Character.toString(trump_cards.get(0));
				for (int i = 0; i < trump_cards.size(); i++) {
					if (find_card_index(trump_cards.get(i)) < find_card_index(best_card.charAt(0))) {
						best_card = Character.toString(trump_cards.get(i));
					}
				}
				//Which is trump. Using this variable to save the function call trump.get_suit()
				best_card += lead_suit;
				strategy = "Strategy: playing the lowest possible trump card in hand to win round: " + best_card.toUpperCase();
				System.out.printf("Strategy: playing the lowest possible trump card in hand to win round: %s\n", best_card.toUpperCase());
			}
			//If I don't have any trump card that can beat the lead-turn is lost case- put the least valuable card- lowest non trump
			else {
				List<Card> non_trump_cards = new ArrayList<Card>();
				for (int i = 0; i < newHand.size(); i++) {
					//pushing all non-trumps to a List
					if (newHand.get(i).get_suit() != trump.get_suit()) {
						non_trump_cards.add(newHand.get(i));

					}
				}
				//if there are non trumps in my newHand
				if (!non_trump_cards.isEmpty()) {
					best_card = Character.toString(non_trump_cards.get(0).get_type());
					best_card += non_trump_cards.get(0).get_suit();
					for (int i = 0; i < non_trump_cards.size(); i++) {
						if ((find_card_index(non_trump_cards.get(i).get_type())) < find_card_index(best_card.charAt(0))) {
							best_card = Character.toString(non_trump_cards.get(i).get_type());
							best_card += non_trump_cards.get(i).get_suit();
						}
					}
					strategy = "Strategy: cannot win this round, playing the lowest non-trump in hand: " + best_card.toUpperCase();
					System.out.printf("Strategy: cannot win this round, playing the lowest non-trump in hand: %s\n", best_card.toUpperCase());
				}
				//if I don't have any non trumps- find the trump with the lowest precedence
				else {
					best_card = Character.toString(newHand.get(0).get_type());
					best_card += newHand.get(0).get_suit();
					for (int i = 0; i < newHand.size(); i++) {
						if (find_card_index(newHand.get(i).get_type()) < find_card_index(best_card.charAt(0))) {
							best_card = Character.toString(newHand.get(i).get_type());
							best_card += newHand.get(i).get_suit();
						}
					}
					strategy = "Strategy: cannot win this round but only have trump cards in hand, playing the lowest one: " + best_card.toUpperCase();
					System.out.printf("Strategy: cannot win this round but only have trump cards in hand, playing the lowest one: %s\n", best_card.toUpperCase());
				}
			}
		}
		//If lead is not of trump suit
		else {
			List<Character> lead_cards = new ArrayList<Character>();
			for (int i = 0; i < newHand.size(); i++) {
				if (newHand.get(i).get_suit() == lead_suit) {
					if (find_card_index(newHand.get(i).get_type()) > find_card_index(lead_type)) {
						//Pushing to the List all the lead_suit-cards in newHand with higher precedence than the lead_type
						lead_cards.add(newHand.get(i).get_type());
					}
				}
			}
			//If there are lead_type cards with higher precedence than lead-find the lowest one.
			if (!lead_cards.isEmpty()) {
				//At this point we know all of these cards can beat the lead, we are trying to find the least "valuable" option to win the turn
				best_card = Character.toString(lead_cards.get(0));
				for (int i = 0; i < lead_cards.size(); i++) {
					if ((find_card_index(lead_cards.get(i)) < find_card_index(best_card.charAt(0)))) {
						best_card = Character.toString(lead_cards.get(i));
					}
				}
				//Using this variable to save the function call lead.get_suit()
				best_card += lead_suit;
				strategy = "Strategy: playing the lowest possible card of suit " + lead_suit + " which is " + best_card.toUpperCase() + " to win the round";
				System.out.printf("Strategy: playing the lowest possible card of suit %c which is %s to win the round\n", lead_suit, best_card.toUpperCase());
			}
			//There are no lead_suit cards that can beat lead- therefore- find lowest trump-suit card in newHand
			else {
				List<Character> trumps = new ArrayList<Character>();
				for (int i = 0; i < newHand.size(); i++) {
					if (newHand.get(i).get_suit() == trump.get_suit()) {
						//Pushing to the List all the trump_suit-cards in newHand
						trumps.add(newHand.get(i).get_type());
					}
				}
				//If there are trump cards in newHand
				if (!trumps.isEmpty()) {
					best_card = Character.toString(trumps.get(0));
					for (int i = 0; i < trumps.size(); i++) {
						if ((find_card_index(trumps.get(i)) < find_card_index(best_card.charAt(0)))) {
							best_card = Character.toString(trumps.get(i));
						}
					}
					best_card += trump.get_suit();
					strategy = "Strategy: no cards with higher precedence than lead suit, playing the weakest trump to win: " + best_card.toUpperCase();
					System.out.printf("Strategy: no cards with higher precedence than lead suit, playing the weakest trump to win: %s\n", best_card.toUpperCase());
				}
				//If no lead-suited cards nor trump cards in newHand- lost case- put weakest card
				else {
					best_card = Character.toString(newHand.get(0).get_type());
					best_card += newHand.get(0).get_suit();
					for (int i = 0; i < newHand.size(); i++) {
						if (find_card_index(newHand.get(i).get_type()) < find_card_index(best_card.charAt(0))) {
							best_card = Character.toString(newHand.get(i).get_type());
							best_card += newHand.get(i).get_suit();
						}
					}
					strategy = "Strategy: cannot win this round, playing the least valuable card from hand: " + best_card.toUpperCase();
					System.out.printf("Strategy: cannot win this round, playing the least valuable card from hand: %s\n", best_card.toUpperCase());
				}
			}
		}
		return best_card;
	}

	/**
	 This function tries to see if the input cards are good enough
	 	to perform the input meld_name
	 @param meld_vector- a list of cards containing the cards of the meld.
	 @param meld_name- a string type containing the meld name.
	 @param trump- a Card type holding the trump card of the current round.
	 @return a string type containing the the best card to play as a chase player.
	 */
	int try_a_meld(List<Card> meld_vector, String meld_name, Card trump) {
		if ((meld_vector.isEmpty()))
		{
			return 0;
		}
		else if (meld_name == "flush")
		{
			int flush = flush_meld(meld_vector, trump);
			if (flush > 0 && !(previous_melds_names.contains("flush")))
			{
				previous_melds_names.add("flush");
				add_meld_cards(meld_name, trump.get_suit());
				return 150;
			}
		}
		else if (meld_name == "fourA")
		{
			int fourA = four_aces_meld(meld_vector, trump);
			if (fourA > 0 && !(previous_melds_names.contains("fourA")))
			{
				previous_melds_names.add("fourA");
				add_meld_cards(meld_name, trump.get_suit());
				return 100;
			}
		}
		else if (meld_name == "fourK")
		{
			int fourK = four_kings_meld(meld_vector, trump);
			if (fourK > 0 && !(previous_melds_names.contains("fourK")))
			{
				previous_melds_names.add("fourK");
				add_meld_cards(meld_name, trump.get_suit());
				return 80;
			}
		}
		else if (meld_name == "fourQ")
		{
			int fourQ = four_queens_meld(meld_vector, trump);
			if (fourQ > 0 && !(previous_melds_names.contains("fourQ")))
			{
				previous_melds_names.add("fourQ");
				add_meld_cards(meld_name, trump.get_suit());
				return 60;
			}
		}
		else if (meld_name == "royal_marriage")
		{
			int royal_marriage = royal_marriage_meld(meld_vector, trump);
			if (royal_marriage > 0 && !(previous_melds_names.contains("royal_marriage")))
			{
				previous_melds_names.add("royal_marriage");
				add_meld_cards(meld_name, trump.get_suit());
				return 40;
			}
		}
		else if (meld_name == "fourJ")
		{
			int fourJ = four_jacks_meld(meld_vector, trump);
			if (fourJ > 0 && !(previous_melds_names.contains("fourJ")))
			{
				previous_melds_names.add("fourJ");
				add_meld_cards(meld_name, trump.get_suit());
				return 40;
			}
		}
		else if (meld_name == "pinochle")
		{
			int pinochle = pinochle_meld(meld_vector, trump);
			if (pinochle > 0 && !(previous_melds_names.contains("pinochle")))
			{
				previous_melds_names.add("pinochle");
				add_meld_cards(meld_name, trump.get_suit());
				return 40;
			}
		}
		else if (meld_name == "spade_marriage")
		{
			int spade_marriage = spade_marriage_meld(meld_vector, trump);
			if (spade_marriage > 0 && !(previous_melds_names.contains("spade_marriage")))
			{
				previous_melds_names.add("spade_marriage");
				add_meld_cards(meld_name, trump.get_suit());
				return 20;
			}
		}
		else if (meld_name == "heart_marriage")
		{
			int heart_marriage = heart_marriage_meld(meld_vector, trump);
			if (heart_marriage > 0 && !(previous_melds_names.contains("heart_marriage")))
			{
				previous_melds_names.add("heart_marriage");
				add_meld_cards(meld_name, trump.get_suit());
				return 20;
			}
		}
		else if (meld_name == "diamond_marriage")
		{
			int diamond_marriage = diamond_marriage_meld(meld_vector, trump);
			if (diamond_marriage > 0 && !(previous_melds_names.contains("diamond_marriage")))
			{
				previous_melds_names.add("diamond_marriage");
				add_meld_cards(meld_name, trump.get_suit());
				return 20;
			}
		}
		else if (meld_name == "club_marriage")
		{
			int club_marriage = club_marriage_meld(meld_vector, trump);
			if (club_marriage > 0 && !(previous_melds_names.contains("club_marriage")))
			{
				previous_melds_names.add("club_marriage");
				add_meld_cards(meld_name, trump.get_suit());
				return 20;
			}
		}
		else if (meld_name == "dix")
		{
			int dix = dix_meld(meld_vector, trump);
			if (dix > 0 && !(previous_melds_names.contains("dix")))
			{
				previous_melds_names.add("dix");
				add_meld_cards(meld_name, trump.get_suit());
				return 10;
			}
		}
		else
		{
			System.out.printf("Meld name entered is not valid");
			return 0;
		}
		return 0;
	}

	/**
	 This function adds the right cards to previous_melds_cards.
	 @param meld_name- a string type containing the meld name
	 @param trumpSuit- a char type that holds the trump suit of the current round
	 */
	void add_meld_cards(String meld_name, char trumpSuit) {
		List<String> cards = new ArrayList<String>();
		trumpSuit = Character.toUpperCase(trumpSuit);
		if (meld_name == "flush")
		{
			String ace = "A", ten = "X", king = "K", queen = "Q", jack = "J";
			cards.add(ace + trumpSuit);
			cards.add(ten + trumpSuit);
			cards.add(king + trumpSuit);
			cards.add(queen + trumpSuit);
			cards.add(jack + trumpSuit);

		}
		else if (meld_name == "fourA")
		{
			cards.add("AS");
			cards.add("AC");
			cards.add("AD");
			cards.add("AH");
		}
		else if (meld_name == "fourK")
		{
			cards.add("KS");
			cards.add("KC");
			cards.add("KD");
			cards.add("KH");
		}
		else if (meld_name == "fourQ")
		{
			cards.add("QS");
			cards.add("QC");
			cards.add("QD");
			cards.add("QH");
		}
		else if (meld_name == "royal_marriage")
		{

			String kingT = "K", queenT = "Q";
			kingT += trumpSuit;
			queenT += trumpSuit;
			cards.add(kingT);
			cards.add(queenT);
		}
		else if (meld_name == "fourJ")
		{
			cards.add("JS");
			cards.add("JC");
			cards.add("JD");
			cards.add("JH");
		}
		else if (meld_name == "pinochle")
		{
			cards.add("QS");
			cards.add("JD");
		}
		else if (meld_name == "spade_marriage")
		{
			cards.add("KS");
			cards.add("QS");
		}
		else if (meld_name == "heart_marriage")
		{
			cards.add("KH");
			cards.add("QH");
		}
		else if (meld_name == "diamond_marriage")
		{
			cards.add("KD");
			cards.add("QD");
		}
		else if (meld_name == "club_marriage")
		{
			cards.add("KC");
			cards.add("QC");
		}
		else if (meld_name == "dix")
		{
			String card = "9";
			card += trumpSuit;
			cards.add(card);
		}
		if (!previous_melds_cards.isEmpty())
		{
			previous_melds_cards.add(",");
		}
		for (int index = 0; index < cards.size(); index++)
		{
			if (!previous_melds_cards.contains(cards.get(index)))
			{
				previous_melds_cards.add(cards.get(index));
			}
			else
			{
				String temp = cards.get(index) + "*";
				previous_melds_cards.add(temp);
			}
		}
	}

	/**
	 This function finds the best meld to perform.
	 @param meld_vector- a list of type Card, holding the cards to perform the meld with.
	 @param trump- Card type that holds the trump of the round.
	 @return a String type holding the best meld's name.
	 */
	String best_meld(List<Card> meld_vector, Card trump) {
		if ((meld_vector.isEmpty()))
		{
			return "No possible melds";
		}
		int flush = flush_meld(meld_vector, trump);
		int fourA = four_aces_meld(meld_vector, trump);
		int fourK = four_kings_meld(meld_vector, trump);
		int fourQ = four_queens_meld(meld_vector, trump);
		int royal_marriage = royal_marriage_meld(meld_vector, trump);
		int fourJ = four_jacks_meld(meld_vector, trump);
		int pinochle = pinochle_meld(meld_vector, trump);
		int spade_marriage = spade_marriage_meld(meld_vector, trump);
		int heart_marriage = heart_marriage_meld(meld_vector, trump);
		int diamond_marriage = diamond_marriage_meld(meld_vector, trump);
		int club_marriage = club_marriage_meld(meld_vector, trump);
		int dix = dix_meld(meld_vector, trump);
		
		
		if (flush > 0 && (!previous_melds_names.contains("flush")))
		{
			return "flush";
		}
		else if (fourA > 0 && (!previous_melds_names.contains("fourA")))
		{
			return "fourA";
		}
		else if (fourK > 0 && (!previous_melds_names.contains("fourK")))
		{
			return "fourK";
		}
		else if (fourQ > 0 && (!previous_melds_names.contains("fourQ")))
		{
			return "fourQ";
		}
		else if (royal_marriage > 0 && (!previous_melds_names.contains("royal_marriage")))
		{
			return "royal_marriage";
		}
		else if (fourJ > 0 && (!previous_melds_names.contains("fourJ")))
		{
			return "fourJ";
		}
		else if (pinochle > 0 && (!previous_melds_names.contains("pinochle")))
		{
			return "pinochle";
		}
		else if (spade_marriage > 0 && (!previous_melds_names.contains("spade_marriage")))
		{
			if ((trump.get_suit() == 's'))
			{
				if ((!previous_melds_names.contains("royal_marriage")))
				{
					return "royal_marriage";
				}
				else
				{
					return "No possible melds";
				}
			}
			return "spade_marriage";
		}
		else if (heart_marriage > 0 && (!previous_melds_names.contains("heart_marriage")))
		{
			if (trump.get_suit() == 'h')
			{
				if (!previous_melds_names.contains("royal_marriage"))
				{
					return "royal_marriage";
				}
				else
				{
					return "No possible melds";
				}
			}
			return "heart_marriage";
		}
		else if (diamond_marriage > 0 && (!previous_melds_names.contains("diamond_marriage")))
		{
			if (trump.get_suit() == 'd')
			{
				if ((!previous_melds_names.contains("royal_marriage")))
				{
					return "royal_marriage";
				}
				else
				{
					return "No possible melds";
				}
			}
			return "diamond_marriage";
		}
		else if (club_marriage > 0 && (!previous_melds_names.contains("club_marriage")))
		{
			if (trump.get_suit() == 'c')
			{
				if (!previous_melds_names.contains("royal_marriage"))
				{
					return "royal_marriage";
				}
				else
				{
					return "No possible melds";
				}
			}
			return "club_marriage";
		}
		else if (dix > 0 && (!previous_melds_names.contains("dix")))
		{
			return "dix";
		}
		else
		{
			return "No possible melds";
		}
	}

	/**
	 This function finds the best meld to perform and crerates a clone of the player's hand, with
	 	the meld cards removed. This way, computer takes into account all the melds and wouldn't
	 	reccoment to play a card that can be used in a meld.
	 @param trump- Card type that holds the trump of the round.
	 @return the hand of the player with the best meld cards removed. If not possible melds- the
	 	original hand of the player.
	 */
	List<Card> remove_meld_cards(Card trump){
		int index;
		Card aceT = new Card('a', trump.get_suit());
		Card tenT = new Card('x', trump.get_suit());
		Card kingT = new Card('k', trump.get_suit());
		Card queenT = new Card('q', trump.get_suit());
		Card jackT = new Card('j', trump.get_suit());
		Card nineT = new Card('9', trump.get_suit());
		Card kingS = new Card('k', 's');
		Card queenS = new Card('q', 's');
		Card kingH = new Card('k', 'h');
		Card queenH = new Card('q', 'h');
		Card kingC = new Card('k', 'c');
		Card queenC = new Card('q', 'c');
		Card kingD = new Card('k', 'd');
		Card queenD = new Card('q', 'd');
		Card aceS = new Card('a', 's');
		Card aceH = new Card('a', 'h');
		Card aceC = new Card('a', 'c');
		Card aceD = new Card('a', 'd');
		Card jackS = new Card('j', 's');
		Card jackH = new Card('j', 'h');
		Card jackC = new Card('j', 'c');
		Card jackD = new Card('j', 'd');

		//List<Card> newHand = new ArrayList();
		List<Card> newHand = new ArrayList<Card>(hand);
		//ArrayList<String> al2 = (ArrayList<String>)al.clone()
		//newHand = hand;
		String best_m = best_meld(hand, trump);
		if(best_m.equals("No possible melds")){
			return hand;
		}
		else{
			if(best_m.equals(("flush"))){
				index = find_idx(aceT, newHand);
				newHand.remove(index);
				index = find_idx(tenT, newHand);
				newHand.remove(index);
				index = find_idx(kingT, newHand);
				newHand.remove(index);
				index = find_idx(queenT, newHand);
				newHand.remove(index);
				index = find_idx(jackT, newHand);
				newHand.remove(index);
			}
			else if(best_m.equals(("fourA"))){
				index = find_idx(aceS, newHand);
				newHand.remove(index);
				index = find_idx(aceH, newHand);
				newHand.remove(index);
				index = find_idx(aceC, newHand);
				newHand.remove(index);
				index = find_idx(aceD, newHand);
				newHand.remove(index);
			}
			else if(best_m.equals(("fourK"))){
				index = find_idx(kingS, newHand);
				newHand.remove(index);
				index = find_idx(kingH, newHand);
				newHand.remove(index);
				index = find_idx(kingC, newHand);
				newHand.remove(index);
				index = find_idx(kingD, newHand);
				newHand.remove(index);
			}
			else if(best_m.equals(("fourQ"))){
				index = find_idx(queenS, newHand);
				newHand.remove(index);
				index = find_idx(queenH, newHand);
				newHand.remove(index);
				index = find_idx(queenC, newHand);
				newHand.remove(index);
				index = find_idx(queenD, newHand);
				newHand.remove(index);
			}
			else if(best_m.equals(("royal_marriage"))){
				index = find_idx(kingT, newHand);
				newHand.remove(index);
				index = find_idx(queenT, newHand);
				newHand.remove(index);
			}
			else if(best_m.equals(("fourJ"))){
				index = find_idx(jackS, newHand);
				newHand.remove(index);
				index = find_idx(jackH, newHand);
				newHand.remove(index);
				index = find_idx(jackC, newHand);
				newHand.remove(index);
				index = find_idx(jackD, newHand);
				newHand.remove(index);
			}
			else if(best_m.equals(("pinochle"))){
				index = find_idx(queenS, newHand);
				newHand.remove(index);
				index = find_idx(jackD, newHand);
				newHand.remove(index);
			}
			else if(best_m.equals(("spade_marriage"))){
				index = find_idx(kingS, newHand);
				newHand.remove(index);
				index = find_idx(queenS, newHand);
				newHand.remove(index);
			}
			else if(best_m.equals(("heart_marriage"))){
				index = find_idx(kingH, newHand);
				newHand.remove(index);
				index = find_idx(queenH, newHand);
				newHand.remove(index);
			}
			else if(best_m.equals(("diamond_marriage"))){
				index = find_idx(kingD, newHand);
				newHand.remove(index);
				index = find_idx(queenD, newHand);
				newHand.remove(index);
			}
			else if(best_m.equals(("club_marriage"))){
				index = find_idx(kingC, newHand);
				newHand.remove(index);
				index = find_idx(queenC, newHand);
				newHand.remove(index);
			}
			else{ //else- this is a dix meld
				index = find_idx(nineT, newHand);
				newHand.remove(index);
			}
			if(newHand.size() > 0) {
				return newHand;
			}
			else{
				return hand;
			}
		}
	}

	//Selectors:
	/**
	 This function is responsible to return the player's round score.
	 @return the player's round score.
	 */
	int get_round_score() {
		return round_score;
	}
	/**
	 This function is responsible to return the player's game score.
	 @return the player's game score.
	 */
	int get_game_score() {
		return game_score;
	}
	/**
	 This function is responsible to return the player's name.
	 @return the player's name.
	 */
	String get_name() {
		return name;
	}

	/**
	 This function is responsible to return the player's capture pile.
	 @return the player's capture pile.
	 */
	List<Card> get_capture_pile() {
		return capture_pile;
	}

	/**
	 This function is responsible to return the player's hand.
	 @return the player's hand.
	 */
	List<Card> get_hand() {
		return hand;
	}

	/**
	 This function is responsible to return the player's previous melds cards list.
	 @return a list of String type, each representing a card from a meld that the player performed
	 */
	List<String> get_previous_melds_cards() {
		return previous_melds_cards;
	}

	
	//Mutators:
	/**
	 This function is responsible to add points to the player's round score.
	 @param pts- an integer value holding the number of point to be added to the player's round score.
	 */
	void add_to_round_score(int pts) {
		round_score += pts;
	}

	/**
	 This function is responsible to add points to the player's game score.
	 @param pts- an integer value holding the number of points to be added to the player's game score.
	 */
	void add_to_game_score(int pts) {
		game_score += pts;
	}

	/**
	 This function is responsible to add a card to the player's hand.
	 @param c1- a Card type holding the card to be added to the player's hand.
	 */
	void add_to_hand(Card c1) {
		hand.add(c1);
	}

	/**
	 This function is responsible to add a card to the player's previous melds cards list.
	 @param c1- a String type holding the card to be added to the player's previous melds cards list.
	 */
	void add_to_previous_melds_cards(String c1) {
		previous_melds_cards.add(c1);
	}

	/**
	 This function is responsible to add a card to the player's previous melds names list.
	 @param meld_name- a String type holding name of the meld to be added to the player's
	 	previous melds names list.
	 */
	void add_to_previous_melds_names(String meld_name) {
		previous_melds_names.add(meld_name);
	}

	/**
	 This functions gets 2 cards (lead, chase) and adds it to the winner's capture pile.
	 @param lead- a Card type which holds the lead card of that round.
	 @param chase- a Card type which holds the chase card of that round.
	 */
	void add_to_capture(Card lead, Card chase) {
		capture_pile.add(lead);
		capture_pile.add(chase);
	}

	/**
	 This functions gets 2 char types (s, t) and adds the cards they
	 	represent to the winner's capture pile.
	 @param t- a char type which holds the type of a card.
	 @param s- a char type which holds the suit of a card.
	 */
	void add_to_capture(char t, char s) {
		capture_pile.add(new Card(t, s));
	}

	/**
	 This function sets the game score of a player to a given valuse
	 	(used when loading a game from a file).
	 @param gameS- an integer type that holds the player's game score.
	 */
	void set_game_score(int gameS) {
		game_score = gameS;
	}

	/**
	 This function sets the round score of a player to a given valuse
	 	(used when loading a game from a file).
	 @param roundS- an int type that holds the player's round score.
	 */
	void set_round_score(int roundS) {
		round_score = roundS;
	}

	
	//main() method for debugging:
	public static void main(String[] args){
	}
	
	//Any utility (private) methods:

	/**
	 This function is supposed to find the index of the card type (to know its priority).
	 @param card_type- character type containing the card's type
	 @return the index of this cards type in the local array (0 for 9, 1 for j, 2 for q, etc.).
	 */
	private int find_card_index(char card_type)
	{
		char card_order[] = { '9', 'j', 'q', 'k', 'x', 'a' };
		for (int index = 0; index < 6; index++)
		{
			if (card_type == card_order[index])
			{
				return index;
			}
		}
		//This is just to make sure all paths of function return a value.
		//technically the function should never return -1 because all cards are validated
		//before being sent to this function.
		return -1;	
	}

	/**
	 The following functions are each checking for a specific type of meld.
	 @param hand- card type list, holds the cards for the meld.
	 @param trump- Card type that holds the trump of the round.
	 @return number of points getting from that meld (if possible to do this meld)
	 	0 otherwise
	 */
	private int flush_meld(List<Card> hand, Card trump) {
		boolean trumpA = false, trumpT = false, trumpK = false, trumpQ = false, trumpJ = false;
		for (int i = 0; i < hand.size(); i++)
		{
			if (hand.get(i).get_suit() == trump.get_suit() && hand.get(i).get_type() == 'a')
			{
				trumpA = true;
			}
			if (hand.get(i).get_suit() == trump.get_suit() && hand.get(i).get_type() == 'x')
			{
				trumpT = true;
			}
			if (hand.get(i).get_suit() == trump.get_suit() && hand.get(i).get_type() == 'k')
			{
				trumpK = true;
			}
			if (hand.get(i).get_suit() == trump.get_suit() && hand.get(i).get_type() == 'q')
			{
				trumpQ = true;
			}
			if (hand.get(i).get_suit() == trump.get_suit() && hand.get(i).get_type() == 'j')
			{
				trumpJ = true;
			}
		}
		if (trumpA && trumpT && trumpK && trumpQ && trumpJ)
		{
			return 150;
		}
		else
		{
			return 0;
		}
	}
	private int four_aces_meld(List<Card> hand, Card trump) {
		boolean spadeA = false, clubA = false, diamondA = false, heartA = false;
		for (int i = 0; i < hand.size(); i++)
		{
			if (hand.get(i).get_suit() == 's' && hand.get(i).get_type() == 'a')
			{
				spadeA = true;
			}
			else if (hand.get(i).get_suit() == 'c' && hand.get(i).get_type() == 'a')
			{
				clubA = true;
			}
			else if (hand.get(i).get_suit() == 'd' && hand.get(i).get_type() == 'a')
			{
				diamondA = true;
			}
			else if (hand.get(i).get_suit() == 'h' && hand.get(i).get_type() == 'a')
			{
				heartA = true;
			}
		}
		if (spadeA && clubA && diamondA && heartA)
		{
			return 100;
		}
		else
		{
			return 0;
		}
	}

	private int four_kings_meld(List<Card> hand, Card trump) {
		boolean spadeK = false, clubK = false, diamondK = false, heartK = false;
		for (int i = 0; i < hand.size(); i++)
		{
			if (hand.get(i).get_suit() == 's' && hand.get(i).get_type() == 'k')
			{
				spadeK = true;
			}
			else if (hand.get(i).get_suit() == 'c' && hand.get(i).get_type() == 'K')
			{
				clubK = true;
			}
			else if (hand.get(i).get_suit() == 'd' && hand.get(i).get_type() == 'K')
			{
				diamondK = true;
			}
			else if (hand.get(i).get_suit() == 'h' && hand.get(i).get_type() == 'K')
			{
				heartK = true;
			}
		}
		if (spadeK && clubK && diamondK && heartK)
		{
			return 80;
		}
		else
		{
			return 0;
		}
	}
		
	private int four_queens_meld(List<Card> hand, Card trump) {
		boolean spadeQ = false, clubQ = false, diamondQ = false, heartQ = false;
		for (int i = 0; i < hand.size(); i++)
		{
			if (hand.get(i).get_suit() == 's' && hand.get(i).get_type() == 'q')
			{
				spadeQ = true;
			}
			else if (hand.get(i).get_suit() == 'c' && hand.get(i).get_type() == 'q')
			{
				clubQ = true;
			}
			else if (hand.get(i).get_suit() == 'd' && hand.get(i).get_type() == 'q')
			{
				diamondQ = true;
			}
			else if (hand.get(i).get_suit() == 'h' && hand.get(i).get_type() == 'q')
			{
				heartQ = true;
			}
		}
		if (spadeQ && clubQ && diamondQ && heartQ)
		{
			return 60;
		}
		else
		{
			return 0;
		}
	}

	private int royal_marriage_meld(List<Card> hand, Card trump){
		boolean trumpK = false, trumpQ = false;
		for (int i = 0; i < hand.size(); i++)
		{
			if (hand.get(i).get_suit() == trump.get_suit() && hand.get(i).get_type() == 'k')
			{
				trumpK = true;
			}
			else if (hand.get(i).get_suit() == trump.get_suit() && hand.get(i).get_type() == 'q')
			{
				trumpQ = true;
			}
		}
		if (trumpK && trumpQ)
		{
			return 40;
		}
		else
		{
			return 0;
		}
	}

	private int four_jacks_meld(List<Card> hand, Card trump) {
		boolean spadeJ = false, clubJ = false, diamondJ = false, heartJ = false;
		for (int i = 0; i < hand.size(); i++)
		{
			if (hand.get(i).get_suit() == 's' && hand.get(i).get_type() == 'j')
			{
				spadeJ = true;
			}
			else if (hand.get(i).get_suit() == 'c' && hand.get(i).get_type() == 'j')
			{
				clubJ = true;
			}
			else if (hand.get(i).get_suit() == 'd' && hand.get(i).get_type() == 'j')
			{
				diamondJ = true;
			}
			else if (hand.get(i).get_suit() == 'h' && hand.get(i).get_type() == 'j')
			{
				heartJ = true;
			}
		}
		if (spadeJ && clubJ && diamondJ && heartJ)
		{
			return 40;
		}
		else
		{
			return 0;
		}
	}

	private int pinochle_meld(List<Card> hand, Card trump) {
		boolean spadeQ = false, diamondJ = false;
		for (int i = 0; i < hand.size(); i++)
		{
			if (hand.get(i).get_suit() == 's' && hand.get(i).get_type() == 'q')
			{
				spadeQ = true;
			}
			if (hand.get(i).get_suit() == 'd' && hand.get(i).get_type() == 'j')
			{
				diamondJ = true;
			}
		}
		if (spadeQ && diamondJ)
		{
			return 40;
		}
		else
		{
			return 0;
		}
	}

	private int spade_marriage_meld(List<Card> hand, Card trump) {
		boolean spadeK = false, spadeQ = false;
		for (int i = 0; i < hand.size(); i++)
		{
			if (hand.get(i).get_suit() == 's' && hand.get(i).get_type() == 'k')
			{
				spadeK = true;
			}
			else if (hand.get(i).get_suit() == 's' && hand.get(i).get_type() == 'q')
			{
				spadeQ = true;
			}
		}
		if (spadeK && spadeQ)
		{
			return 20;
		}
		else
		{
			return 0;
		}
	}

	private int heart_marriage_meld(List<Card> hand, Card trump) {
		boolean heartK = false, heartQ = false;
		for (int i = 0; i < hand.size(); i++)
		{
			if (hand.get(i).get_suit() == 'h' && hand.get(i).get_type() == 'k')
			{
				heartK = true;
			}
			else if (hand.get(i).get_suit() == 'h' && hand.get(i).get_type() == 'q')
			{
				heartQ = true;
			}
		}
		if (heartK && heartQ)
		{
			return 20;
		}
		else
		{
			return 0;
		}
	}
	private int diamond_marriage_meld(List<Card> hand, Card trump) {
		boolean diamondK = false, diamondQ = false;
		for (int i = 0; i < hand.size(); i++)
		{
			if (hand.get(i).get_suit() == 'd' && hand.get(i).get_type() == 'k')
			{
				diamondK = true;
			}
			else if (hand.get(i).get_suit() == 'd' && hand.get(i).get_type() == 'q')
			{
				diamondQ = true;
			}
		}
		if (diamondK && diamondQ)
		{
			return 20;
		}
		else
		{
			return 0;
		}
	}

	private int club_marriage_meld(List<Card> hand, Card trump) {
		boolean clubK = false, clubQ = false;
		for (int i = 0; i < hand.size(); i++)
		{
			if (hand.get(i).get_suit() == 'c' && hand.get(i).get_type() == 'k')
			{
				clubK = true;
			}
			else if (hand.get(i).get_suit() == 'c' && hand.get(i).get_type() == 'q')
			{
				clubQ = true;
			}
		}
		if (clubK && clubQ)
		{
			return 20;
		}
		else
		{
			return 0;
		}
	}

	private int dix_meld(List<Card> hand, Card trump) {
		boolean trumpN = false;
		for (int i = 0; i < hand.size(); i++)
		{
			if (hand.get(i).get_suit() == trump.get_suit() && hand.get(i).get_type() == '9')
			{
				trumpN = true;
			}
		}
		if (trumpN)
		{
			return 10;
		}
		else
		{
			return 0;
		}
	}

	
	
	
	
	//Other functions

	/**
	 This function prints all the cards in the player's hand in the followng format "TypeSuit".
	 */
	void print_hand() {
		if (!(hand.isEmpty()))
		{
			for(int i = 0; i < hand.size(); i++){
				hand.get(i).print_card();
				System.out.printf(" ");
			}
			System.out.println();
		}
	}

	/**
	 This function prints all the cards in the player's capture pile
	 	in the following format "TypeSuit".
	 */
	void print_capture_pile() {
		if (!(capture_pile.isEmpty()))
		{
			for(int i = 0; i < capture_pile.size(); i++){
				capture_pile.get(i).print_card();
				System.out.printf(" ");
			}
			System.out.println();
		}
	}

	/**
	 This function prints all the cards in the player's preious_melds_cards
	 in the followng format "TypeSuit".
	 */
	void print_previous_melds_cards() {
		if (!(previous_melds_cards.isEmpty()))
		{
			for(int i = 0; i < previous_melds_cards.size(); i++){
				System.out.printf(previous_melds_cards.get(i).toUpperCase());
				System.out.printf(" ");
			}
			System.out.println();
		}
	}

	/**
	 This function resets score and clears melds_names, melds_cards, and
	 capture_pile for a new round
	 */
	void clear_for_new_round() {
		round_score = 0;
		previous_melds_cards.clear();
		previous_melds_names.clear();
		capture_pile.clear();
	}

	/**
	 This function is responsible for finding a give Card's index in the list.
	 @param c1- a Card type containing the card whose index we are trying to find.
	 @param newHand- a list of Card types, representing the player's hand.
	 @return and inteder type, representing the index of the card in the list, or -1 if
	 	card was not found in the list.
	 */
	int find_idx (Card c1, List<Card> newHand){
		for(int i = 0; i<newHand.size(); i++){
			if(newHand.get(i).equals(c1))
				return i;

		}
		return -1;
	}
}