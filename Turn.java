package edu.ramapo.yashken1.pinochle;

import android.content.*;
import android.os.Environment;

import java.io.*;
import java.util.*;


class Turn implements Serializable{
	//Class Constants:
	
	
	//Class Variables:
	int player_choice;
	Card lead, chase;
	int p2_index;
	Int player2_index = new Int();
	int want_to_meld;
	Player[] playerList = new Player[2];
	Int nextPlayerIndex;
	Card trump;
	int round_number;
	List<Card> local_deck;
	String reccomended_card;
	Human local_human;
	Computer local_computer;
	String fileName;

	/**
	 * This function is responsible for setting name of the file we are trying to open.
	 * @param name- a string type holding the name of the file.
	 */
	void setFileName(String name){
		fileName = name;
	}

	/**
	 * This function is responsible for setting the lead card after it has been played
	 * @param str- a string type holding type and suit of the lead card.
	 */
	void setLead(String str){
		lead = new Card(str.charAt(0), str.charAt(1));
	}

	//GUI Components:
	/**
	 * This function is responsible for setting the player's choice of the menu.
	 */
	void set_player_choice(int choice){
		player_choice = choice;
	}

	/**
	 * This function is responsible for setting user choice- whether they want to meld or not.
	 * @param reply- an integer type holding the user's response.
	 */
	void set_want_to_meld(int reply){
		want_to_meld = reply;
	}

	//Constructor:
	/**
	 * This function is responsible acting accordingly with the user's choice and play as lead
	 * (calling the appropriate functions).
	 * @return A Card type chosen by the user.
	 */
	Card first_player_playing(){
		switch (player_choice) {
			case 1:
				save_the_game(playerList, nextPlayerIndex, trump, local_deck, round_number);
				System.exit(132);
				break;
			case 2:
				//This does nothing, I tried putting here the line Card chase= ... but-
				//then the computer does not see its declaration and doesn't know it exists.
				break;
			case 3:
				System.exit(132);
				//YUVYUV- I need to end the game here, how do I do that??
				break;
			case 4:
				reccomended_card = playerList[nextPlayerIndex.get_value()].strategy_lead(trump);
				break;
		}
		//Asking first player to play and defining the lead card accordingly
		lead = playerList[nextPlayerIndex.get_value()].play(trump, 'e', 'e');

		p2_index = nextPlayerIndex.get_value();
		p2_index = (p2_index + 1) % 2;

		if(p2_index == 0){
			player2_index.human_starts();
		}
		else{
			player2_index.computer_starts();
		}
		return lead;
	}
	/**
	 * This function is responsible acting accordingly with the user's choice and play as chase
	 * (calling the appropriate functions).
	 * @return A Card type chosen by the user.
	 */
	Card second_player_playing(){
		switch (player_choice) {
			case 1:
				save_the_game(playerList, player2_index, trump, local_deck, round_number);
				System.exit(132);
				//YUVYUV- I need to end the game here, how do I do that??
				break;
			case 2:
				//This does nothing, I tried putting here the line Card chase= ... but-
				//then the computer does not see its declaration and doesn't know it exists.
				break;
			case 3:
				System.exit(132);
				break;
			case 4:
				reccomended_card = playerList[p2_index].strategy_chase(trump, lead.get_type(), lead.get_suit());
				//System.out.printf("%s\n", reccomended_card);

				break;
		}
		//Asking second player to play and defining the chase card accordingly
		lead.print_card();
		chase = playerList[p2_index].play(trump, lead.get_type(), lead.get_suit());
		return chase;
	}

	/**
	 * This function is responsible determining the winner.
	 * @return A boolean type holding true if the lead player won and false if the chase player
	 * 		won
	 */
	boolean get_winner(){
		boolean did_lead_win = winner(lead, chase, trump);		//Determining who won
		if (did_lead_win)
		{
			//Determining the winning player
			System.out.printf("%s won this turn!\n", playerList[nextPlayerIndex.get_value()].get_name());
			//Adding cards to winner's capture pile
			playerList[nextPlayerIndex.get_value()].add_to_capture(lead, chase);
			//Adding points to winner's score
			playerList[nextPlayerIndex.get_value()].add_to_round_score(points(lead, chase));

		}
		else
		{
			System.out.printf("%s won this turn!\n", playerList[p2_index].get_name());
			playerList[p2_index].add_to_capture(lead, chase);
			playerList[p2_index].add_to_round_score(points(lead, chase));
			nextPlayerIndex.switchPlayers();
		}
		return did_lead_win;
	}
	/**
	 * This function is responsible for calling help_with_meld function and determing the best meld
	 * 		to perform.
	 * @return A String type holding the name of the best meld the player can perform.
	 * 		won
	 */
	String getting_best_meld(){
		String best_action = playerList[nextPlayerIndex.get_value()].help_with_meld(trump);
		return best_action;
	}
	void performing_meld(String meld_name, List<Card> meld_vector){
		if (want_to_meld == 1)
		{
			playerList[nextPlayerIndex.get_value()].perform_a_meld(trump, meld_vector, meld_name);
		}
	}

	/**
	 * The Turn constructor, initialized some of the class values.
	 */
	Turn(Human p1, Computer p2, Int nextPlayerIndex1, Card trump1, int round_number1, List<Card> local_deck1) {
		local_human = p1;
		local_computer = p2;
		playerList[0] = p1;
		playerList[1] = p2;
		nextPlayerIndex = nextPlayerIndex1;
		trump = trump1;
		round_number = round_number1;
		local_deck = local_deck1;
	}

	//Event handlers:
	/**
	 * This function is responsible to determine the winner of the turn.
	 * @param lead- a card type holding the lead card of the turn.
	 * @param chase- a card type holding the chase card of the turn.
	 * @param trump- a card type holding the trump type and suit for the round.
	 * @return a boolean type- true if lead won, false if chase won.
	 */
	boolean winner(Card lead, Card chase, Card trump){
		int leadIdx, chaseIdx;
		char typeOrder[] = { '9', 'j', 'q', 'k', 'x', 'a' };
		leadIdx = lead.get_card_index();
		chaseIdx = chase.get_card_index();
		if (lead.get_suit() == trump.get_suit()) {
			if (chase.get_suit() == trump.get_suit()) {
				if (chaseIdx > leadIdx)
				{
					return false;
				}
				else
				{
					return true;
				}
			}
			else {
				return true;
			}
		}
		else
		{
			if (chase.get_suit() == lead.get_suit())
			{
				if (chaseIdx > leadIdx)
				{
					return false;
				}
				else
				{
					return true;
				}
			}
			else if (chase.get_suit() == trump.get_suit())
			{
				return false;
			}
			else
			{
				return true;
			}
		}

	}

	/**
	 * This function determines how many points the winner gets from the lead and chase.
	 * @param lead- a card type holding the lead card of the turn.
	 * @param chase- a card type holding the chase card of the turn.
	 * @return an integer type- the number of point the winner is getting for lead and chase cards.
	 */
	int points(Card lead, Card chase) {
		int turnPts = 0;
		switch (lead.get_type())
		{
		case 'j':
			turnPts += 2;
			break;
		case 'q':
			turnPts += 3;
			break;
		case 'k':
			turnPts += 4;
			break;
		case 'x':
			turnPts += 10;
			break;
		case 'a':
			turnPts += 11;
			break;
		default:
			//type is 9, no points for 9
			break;
		}
		switch (chase.get_type())
		{
		case 'j':
			turnPts += 2;
			break;
		case 'q':
			turnPts += 3;
			break;
		case 'k':
			turnPts += 4;
			break;
		case 'x':
			turnPts += 10;
			break;
		case 'a':
			turnPts += 11;
			break;
		default:
			//type is 9, no points for 9
			break;
		}
		return turnPts;
	}
	
	//Selectors:
	
	
	//Mutators:
	
	
	//main() method for debugging:
	public static void main(String[] args){
		
	}
	
	//Any utility (private) methods:

	/**
	 * This function writes to a file the necessary information about the current game
	 * @param playerList- A player pointer type list pointing to the 2 playerd
	 * @param nextPlayerIndex- int type passed by reference holding the index to the player's list
	 * @param trump- a card type holding the trump type and suit for the round
	 * @param round_number- an int type holding the round number
	 * @param deck-a card vector type holding the deck of the current round
	 *
	 * got help with the translation from here: https://www.journaldev.com/864/java-open-file
	 */
	private void save_the_game(Player[] playerList, Int nextPlayerIndex, Card trump, List<Card> deck, int round_number) {
		int i, j;
		char s, t;
		File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES);
		File gameFile = new File(path, "/" + fileName);
		System.out.printf("Please enter a file name (without an extention): ");

		//reading the file name

		fileName += ".txt";
		//File gameFile = new File(fileName);
		//gameFile.createNewFile();
		
		try {
			FileWriter fileWriter = new FileWriter(fileName);
			PrintWriter writer = new PrintWriter(fileWriter);
	
            //FileWriter writer = new FileWriter("MyFile.txt", true);
			//PrintWriter writer = new PrintWriter(fileWriter);
			
			writer.printf("Round: %d\n\n", round_number);
			writer.printf("Computer:\n");
			writer.printf("   Score: %d / %d\n", playerList[1].get_game_score(), playerList[1].get_round_score());
			writer.printf("   Hand: ");
			if (!(playerList[1].get_hand()).isEmpty())
			{
				List<Card> hand_copy = new ArrayList<Card>();
				hand_copy = playerList[1].get_hand();
				for (i = 0; i < hand_copy.size(); i++)
				{
					t = hand_copy.get(i).get_type();
					s = hand_copy.get(i).get_suit();
					writer.printf("%c%c ", Character.toUpperCase(t), Character.toUpperCase(s));
				}
			}
			writer.printf("\n");

			writer.printf("   Capture Pile: ");
			if (!playerList[1].get_capture_pile().isEmpty())
			{
				List<Card> capture_pile_copy = new ArrayList<Card>();
				capture_pile_copy = playerList[1].get_capture_pile();
				
				for (i = 0; i < capture_pile_copy.size(); i++)
				{
					t = capture_pile_copy.get(i).get_type();
					s = capture_pile_copy.get(i).get_suit();
					writer.printf("%c%c ", Character.toUpperCase(t), Character.toUpperCase(s));
				}
			}
			writer.printf("\n");

			writer.printf("   Melds: ");
			if (!playerList[1].get_previous_melds_cards().isEmpty())
			{
				List<String> previous_meld_cards_copy = new ArrayList<String>();
				previous_meld_cards_copy = playerList[1].get_previous_melds_cards();
				/*
				for (i = 0; i < previous_meld_cards_copy.size(); i++)
				{
					for (j = 0; j < previous_meld_cards_copy.size(); j++)
					{
						previous_meld_cards_copy.set(j, previous_meld_cards_copy.get(i).toUpperCase());
					}
				}
				for (i = 0; i < previous_meld_cards_copy.size(); i++)
				{
					writer.printf("%s ", previous_meld_cards_copy.get(i));
				}
				*/
				for (i = 0; i < previous_meld_cards_copy.size(); i++)
				{
					writer.printf("%s ", previous_meld_cards_copy.get(i).toUpperCase());
				}
			}
			writer.printf("\n");


			writer.printf("Human:\n");
			writer.printf("   Score: %d / %d\n", playerList[0].get_game_score(), playerList[0].get_round_score());
			writer.printf("   Hand: ");
			if (!(playerList[0].get_hand()).isEmpty())
			{
				List<Card> hand_copy = new ArrayList<Card>();
				hand_copy = playerList[0].get_hand();
				for (i = 0; i < hand_copy.size(); i++)
				{
					t = hand_copy.get(i).get_type();
					s = hand_copy.get(i).get_suit();
					writer.printf("%c%c ", Character.toUpperCase(t), Character.toUpperCase(s));
				}
			}
			writer.printf("\n");

			writer.printf("   Capture Pile: ");
			if (!(playerList[0].get_capture_pile()).isEmpty())
			{
				List<Card> capture_pile_copy = new ArrayList<Card>();
				capture_pile_copy = playerList[0].get_capture_pile();
				for (i = 0; i < capture_pile_copy.size(); i++)
				{
					t = capture_pile_copy.get(i).get_type();
					s = capture_pile_copy.get(i).get_suit();
					writer.printf("%c%c ", Character.toUpperCase(t), Character.toUpperCase(s));
				}
			}
			writer.printf("\n");

			writer.printf("   Melds: ");
			if (!playerList[0].get_previous_melds_cards().isEmpty())
			{
				List<String> previous_meld_cards_copy = new ArrayList<String>();
				previous_meld_cards_copy = playerList[0].get_previous_melds_cards();

				for (i = 0; i < previous_meld_cards_copy.size(); i++)
				{
					writer.printf("%s ", previous_meld_cards_copy.get(i).toUpperCase());
				}
			}
			writer.printf("\n");

			if (trump.get_type() == ' ')
			{
				writer.printf("Trump Card: %c\n", Character.toUpperCase(trump.get_suit()));
			}
			else
			{
				writer.printf("Trump Card: %c%c\n", Character.toUpperCase(trump.get_type()), Character.toUpperCase(trump.get_suit()));
			}	
			writer.printf("Stock: ");
			if (!deck.isEmpty())
			{
				for (i = 0; i < deck.size(); i++)
				{
					t = deck.get(i).get_type();
					s = deck.get(i).get_suit();
					writer.printf("%c%c ", Character.toUpperCase(t), Character.toUpperCase(s));
				}
			}
			writer.printf("\n\n");
			writer.printf("Next Player: %s", playerList[nextPlayerIndex.get_value()].get_name());
			writer.close();
			
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
}