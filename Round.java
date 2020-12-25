package edu.ramapo.yashken1.pinochle;

//import java.util.Scanner;
//import java.util.Random; 
import java.io.Serializable;
import java.util.*;

class Round implements Serializable {
	//Class Constants:

	//Class Variables:
	Player[] playerList = new Player[2];
	Int nextPlayerIndex = new Int();
	Deck deck1;
	Turn t1;
	int coin_choice, actualCoin;
	int round_num;
	Card trump;
	List<Card> local_deck = new ArrayList<Card>();
	Human local_human;
	Computer local_computer;

	//GUI Components:

	//Constructor:
	/**
	 * An empty constructor for Round object, user for initializaton purposes.
	 */
	Round() {

	}

	/**
	 * This is the round class constructor it is responsible for determining who starts,
	 * initializing and shuffling the deck, dealing cards, then displaying game info and
	 * determining and declaring who won the round.
	 *
	 * @param round_number- int type holding the round number.
	 * @param p1-           a Human object.
	 * @param p2-           a computer object.
	 */
	Round(int round_number, Human p1, Computer p2) {

		//Initializing a list of players:
		playerList[0] = p1;
		playerList[1] = p2;
		round_num = round_number;
		local_human = p1;
		local_computer = p2;
		System.out.printf("\nWelcome to round number %d!\n", round_number);


		deck1 = new Deck();
		System.out.printf("\nshuffeling deck..\n");

		deck1.shuffle_deck();
		System.out.printf("Deck is shuffled!\n");

		deck1.deal_cards(playerList, nextPlayerIndex);
		//determining the trump card
		trump = deck1.get_trump();
		local_deck = deck1.get_deck();

		//Game continues until both player's hands are empty
	}

	/**
	 * This function is responsible for initializing the round's logic after all the nputs have been
	 * initialized.
	 */
	void initialize_round() {
		System.out.printf("\t\t\t\t Round Number %d\n", round_num);
		System.out.printf("Trump: ");
		deck1.print_trump();
		System.out.printf("\n");
		System.out.printf("Deck: ");
		deck1.print_deck();
		System.out.printf("\n");

		//Displaying first player's name, hand, capture pile, and score
		System.out.printf("%s:\n", playerList[0].get_name());
		System.out.printf("\t Hand: ");
		playerList[0].print_hand();

		System.out.printf("\t Capture Pile: ");
		playerList[0].print_capture_pile();
		System.out.printf("\n");

		System.out.printf("\t Melds: ");
		playerList[0].print_previous_melds_cards();
		System.out.printf("\n");

		System.out.printf("\t Score: %d\n\n", playerList[0].get_round_score());

		//Displaying second player's name, hand, capture pile, and score
		System.out.printf("%s:\n", playerList[1].get_name());
		System.out.printf("\t Hand: ");
		playerList[1].print_hand();

		System.out.printf("\t Capture Pile: ");
		playerList[1].print_capture_pile();
		System.out.printf("\n");

		System.out.printf("\t Melds: ");
		playerList[1].print_previous_melds_cards();
		System.out.printf("\n");

		System.out.printf("\t Score: %d\n\n", playerList[1].get_round_score());

		//Starting a turn
		t1 = new Turn(local_human, local_computer, nextPlayerIndex, trump, round_num, local_deck);

	}

	/**
	 * This function is responsible for initializing calling the deal_turn_cards function to deal
	 * the players a card after a turn.
	 */
	void round_dealing_turn_cards() {
		deck1.deal_turn_cards(playerList, nextPlayerIndex);
	}

	/**
	 * This function is responsible for printing the round conclusion at the end of a round and
	 * clearing the necessary values for the next round.
	 */
	void round_conclusion() {
		//This if/else statement determined who won the round
		playerList[0].add_to_game_score(playerList[0].get_round_score());
		playerList[1].add_to_game_score(playerList[1].get_round_score());
		if (playerList[0].get_game_score() > playerList[1].get_game_score()) {
			System.out.printf("%s's score: \n", playerList[0].get_game_score());
			System.out.printf("%s's score: \n\n", playerList[1].get_game_score());
			System.out.printf("%s won this round!\n", playerList[0].get_name());
		} else if (playerList[1].get_game_score() > playerList[0].get_game_score()) {
			System.out.printf("%s's score: \n", playerList[0].get_game_score());
			System.out.printf("%s's score: \n\n", playerList[1].get_game_score());
			System.out.printf("%s won this round!\n", playerList[1].get_name());
		} else {
			System.out.printf("It's a tie!\n");
		}

		//resetting the players' round score, capture pile, and meld piles for new round
		playerList[0].clear_for_new_round();
		playerList[1].clear_for_new_round();
	}

	/**
	 * This function is responsible for determining the beginning player.
	 */
	void setting_beginner() {
		if (round_num == 1)                    //If first round- toss a coin
		{
			boolean who_starts;
			System.out.printf("Since this is the first round, we'll toss a coin to decide who starts:\n");
			who_starts = toss_a_coin(coin_choice);
			if (who_starts) {
				System.out.printf("%s starts", playerList[0].get_name());
				nextPlayerIndex.human_starts();
			} else {
				System.out.printf("%s starts", playerList[1].get_name());
				nextPlayerIndex.computer_starts();
			}
		}
		//If tied score- toss a coin
		else if (playerList[0].get_game_score() == playerList[1].get_game_score()) {
			System.out.printf("Since there is a tie, we'll toss a coin to decide who starts:\n");
			boolean who_starts;
			who_starts = toss_a_coin(coin_choice);
			if (who_starts) {
				System.out.printf("%s starts", playerList[0].get_name());
				nextPlayerIndex.human_starts();
			} else {
				System.out.printf("%s starts", playerList[1].get_name());
				nextPlayerIndex.computer_starts();
			}
		}
		//Otherwise- the player with more points starts first
		else {
			if (playerList[0].get_game_score() > playerList[1].get_game_score()) {
				System.out.printf("%s starts", playerList[0].get_name());
				nextPlayerIndex.human_starts();
			} else {
				System.out.printf("%s starts", playerList[1].get_name());
				nextPlayerIndex.computer_starts();
			}
		}
	}

	/**
	 * This is the round class constructor for a round that starts in the middle
	 * 	(when reading a game from a file)
	 * 	it is responsible for displaying the game info and determining and
	 * 	declaring who won the round.
	 * @param round_number- int type holding the round number
	 * 	@param p1- a Human object
	 * 	@param p2- a computer object
	 * 	@param help_deck- Deck type- the deck of the game
	 * 	@param nextPlayer- string type, the name of the next player
	 */
	Round(int round_number, Human p1, Computer p2, Deck help_deck, String nextPlayer) {
		playerList[0] = p1;
		playerList[1] = p2;
		round_num = round_number;
		local_human = p1;
		local_computer = p2;
		if (nextPlayer.equals(playerList[0].get_name())) {
			nextPlayerIndex.human_starts();
		} else {
			nextPlayerIndex.computer_starts();
		}

		local_deck = help_deck.get_deck();
		trump = help_deck.get_trump();
		deck1 = new Deck();
		deck1.set_deck(local_deck);
		deck1.set_trump(trump);
		System.out.printf("MPMPMPMPMPMPMPMPMPMPMPPM %c %c PMPMPMPMPMPMPMPMPMPPM", trump.get_type(), trump.get_suit());

		System.out.printf("\t\t\t\t Round Number %d\n", round_number);
		System.out.printf("Trump: ");
		help_deck.print_trump();
		System.out.printf("\n");
		System.out.printf("Deck: ");
		help_deck.print_deck();
		System.out.printf("\n");

		//Displaying first player's name, hand, capture pile, and score
		System.out.printf("%s:\n", p1.get_name());
		System.out.printf("\t Hand: ");
		p1.print_hand();

		System.out.printf("\t Capture Pile: ");
		p1.print_capture_pile();
		System.out.printf("\n");

		System.out.printf("\t Melds: ");
		p1.print_previous_melds_cards();
		System.out.printf("\n");

		System.out.printf("\t Score: %d\n\n", p1.get_round_score());

		//Displaying second player's name, hand, capture pile, and score
		System.out.printf("%s:\n", p2.get_name());
		System.out.printf("\t Hand: ");
		p2.print_hand();

		System.out.printf("\t Capture Pile: ");
		p2.print_capture_pile();
		System.out.printf("\n");

		System.out.printf("\t Melds: ");
		p2.print_previous_melds_cards();
		System.out.printf("\n");

		System.out.printf("\t Score: %d\n\n", p2.get_round_score());


		//Starting a turn
		Turn t1 = new Turn(local_human, local_computer, nextPlayerIndex, trump, round_number, local_deck);
		help_deck.deal_turn_cards(playerList, nextPlayerIndex);
		//}
	}

	//Event handlers:
	boolean toss_a_coin(int user_choice) {

		Random rand = new Random(System.currentTimeMillis());
		//Generating a random number between 0 and 1
		actualCoin = rand.nextInt(2);
		System.out.printf("Coin shows %d! ", actualCoin);
		if (actualCoin == user_choice) {
			return true;
		} else {
			return false;
		}
	}

	//Selectors:

	/**
	 * This function is getting the Int object holding the nextPlayerIndex.
	 *
	 * @return and Int type holding the nextPlayerIndex.
	 */
	Int getNPI() {
		return nextPlayerIndex;
	}

	/**
	 * This function is returning an integer value holding the random coind toss.
	 *
	 * @return and integer type holding a random number between 0 and 1 representing a coin toss.
	 */
	int get_actual_coin() {
		return actualCoin;
	}

	/**
	 * This function is responsible for retrieving the Deck object.
	 *
	 * @return a Deck object (the local deck object).
	 */
	Deck get_deck_object() {
		return deck1;
	}

	/**
	 * This function is responsible for retrieving the Int object (which holds the next player's
	 * index).
	 *
	 * @return a Game object (the local Int object).
	 */
	Int get_custom_int() {
		return nextPlayerIndex;
	}

	/**
	 * This function is responsible for retrieving the Turn object.
	 *
	 * @return a Game object (the local Turn object).
	 */
	Turn get_turn_object() {
		return t1;
	}

	/**
	 * This function is responsible for retrieving the trump of the current round.
	 *
	 * @return a Card type holsing the trump of the current round.
	 */
	Card get_trump() {
		return trump;
	}





	//Mutators:

	/**
	 * This function is responsible of setting the users coin choice.
	 *
	 * @param input- an integer type representing the user's coind choice.
	 */
	void set_coin(int input) {
		coin_choice = input;
	}


	//main() method for debugging:
	public static void main(String[] args) {
	}

	//Any utility (private) methods:

}