package edu.ramapo.yashken1.pinochle;

import android.content.Context;
import android.content.res.AssetManager;

import java.util.*;
import java.io.*;

class Game implements Serializable{
 	//Class Constants:

	private Context mContext;
	//Class Variables:
	int round_number = 0;
	Round round1 = new Round();
	//GUI Components:
	/**
	 This function is responsible for retrieving the round number
	 @return an integer type representing the current round number.
	 */
	int get_r_num(){
		return round_number;
	}

	/**
	 This function is responsible for retrieving the Round object
	 @return an Round object holding the current round.
	 */
	Round get_round_object(){
		return round1;
	}
	//Constructor:
	/**
	 Default constructor for the Game class
	 */
	Game(){
		//temporary constructor
		round_number = 1;
	}
	/**
	 A constructor for the Game class, for when starting a new game
	 @param p1 a Human object
	 @param p2 a Computer object
	 */
	Game(Human p1, Computer p2) {
		round_number++;
		round1 = new Round(round_number, p1, p2);
	}

	/**
	 A constructor for the Game class used when loading a gme from a file.
	 @param p1 a Human object.
	 @param p2 a Computer object.
	 @param fileName a String type holsing the name of the file to be opened.
	 */
	Game(Human p1, Computer p2, String fileName) {
		String line;
		//This variable will help passing strings "by reference" to functions
		List<String> funcHelper = new ArrayList<String>();
		String word;
		//int round_num;
		int gameS, roundS; //scores;
		char slash;
		int i, index;

		/*FileInputStream is;
		BufferedReader reader;
		final File file = new File(fileName);*/

		try
		{
			//is = new FileInputStream(file);
			//reader = new BufferedReader(new InputStreamReader(is));
			BufferedReader reader = new BufferedReader(new FileReader(fileName));
			//AssetManager assetManager = getAssets();
			//BufferedReader reader = new BufferedReader(new InputStreamReader(getAssets().open("filename.txt")));

			//String file = "/storage/sdcard1/case1.txt";
			//InputStream in = this.getClass().getClassLoader().getResourceAsStream(file);
			//Defining round_number
			line = reader.readLine(); //This line contains round: #
			String[] strings = line.split(" ");
			round_number = Integer.parseInt(strings[1]);

			line = reader.readLine(); // empty line
			line = reader.readLine(); // line only contains "Computer:"
			line = reader.readLine(); //this line contains score: # / #
			
			strings = line.split(" ");
			
			gameS = Integer.parseInt(strings[4]);
			roundS = Integer.parseInt(strings[6]);
			//gameS and roundS now contain the game and round score of the player respectively
			p2.set_game_score(gameS);
			p2.set_round_score(roundS);
			
			
			int cutLoc;
			line = reader.readLine(); //this line should contain hand: hand cards
			line = line.toLowerCase();
			if (!(line.length() <= 9)) //making sure hand is not empty
			{
				cutLoc = line.indexOf(':');
				line = line.substring(cutLoc + 2);
				funcHelper.clear();
				funcHelper.add(line);
				get_rid_of_end_space(funcHelper);		//getting rid of space at end of line if exists
				line = funcHelper.get(0);
				//converting line to lower case
				line = line.toLowerCase();
			}
			List<String> temp_hand = new ArrayList<String>();
			if (line.length() > 0) {
				for (i = 0; i < line.length() - 1; i = i + 3)
				{
					temp_hand.add(line.substring(i, i+2));
				}
			}
			if (!temp_hand.isEmpty())
			{
				for (i = 0; i < temp_hand.size(); i++)
				{
					p2.add_to_hand(new Card(temp_hand.get(i).charAt(0), temp_hand.get(i).charAt(1)));
				}
			}

			line = reader.readLine(); //this line should contain capture pile: capture cards
			line = line.toLowerCase();
			List<String> temp_capture = new ArrayList<String>();
			if (!(line.length() <= 17)) //making sure capture pile is not empty
			{
				cutLoc = line.indexOf(':');
				line = line.substring(cutLoc + 2);
				funcHelper.clear();
				funcHelper.add(line);
				get_rid_of_end_space(funcHelper);		//getting rid of space at end of line if exists
				line = funcHelper.get(0);
				
				line = line.toLowerCase(); //converting line to lower case
				if (line.length() > 0) {
					for (i = 0; i < line.length() - 1; i = i + 3)
					{	
						temp_capture.add(line.substring(i, i+2));
					}
				}
			}
			if (!temp_capture.isEmpty())
			{
				//String tempHelp
				for (i = 0; i < temp_capture.size(); i++)
				{
					//tempHelp = temp_hand.get(i)
					p2.add_to_capture(temp_capture.get(i).charAt(0), temp_capture.get(i).charAt(1));
				}
			}

			//Another note- change characters to lower case!!!!
			//I stopped here- not sure how to insert the melds+meld names yet:

			String prev_melds_computer = "";
			int num_commas;
			line = reader.readLine(); //this line should contain melds: prev melds
			line = line.toLowerCase();
			if (!(line.length() <= 10)) //making sure player has melds to work with is not empty
			{
				cutLoc = line.indexOf(':');
				line = line.substring(cutLoc + 2);
				funcHelper.clear();
				funcHelper.add(line);
				get_rid_of_end_space(funcHelper);		//getting rid of space at end of line if exists
				line = funcHelper.get(0);
				
				line = line.toLowerCase(); //converting line to lower case
				prev_melds_computer = line;			//Saving this line to use is later to update previous_melds_names vector

				num_commas = number_of_commas(line);
				for (index = 0; index < num_commas; index++)
				{
					String one_meld = line.substring(0, line.indexOf(','));
					line = line.substring(line.indexOf(',') + 2);
					adding_melds(one_meld, p2);
					p2.add_to_previous_melds_cards(",");
				}
				adding_melds(line, p2);
			}

			line = reader.readLine(); // this should be empty
			line = reader.readLine();// this should only say "Human:"

			line = reader.readLine(); //this line contains score: # / #
			strings = line.split(" ");
			gameS = Integer.parseInt(strings[4]);
			roundS = Integer.parseInt(strings[6]);
			//gameS and roundS now contain the game and round score of the player respectively
			p1.set_game_score(gameS);
			p1.set_round_score(roundS);

			line = reader.readLine(); //this line should contain hand: hand cards
			line = line.toLowerCase();
			
			if (!(line.length() <= 9)) //making sure hand is not empty
			{
				cutLoc = line.indexOf(':');
				line = line.substring(cutLoc + 2);
				funcHelper.clear();
				funcHelper.add(line);
				get_rid_of_end_space(funcHelper);		//getting rid of space at end of line if exists
				line = funcHelper.get(0);
				
				//converting line to lower case
				line = line.toLowerCase();
				
				temp_hand.clear();
				if (line.length() > 0) {
					for (i = 0; i < line.length() - 1; i = i + 3)
					{
						temp_hand.add(line.substring(i, i+2));
					}
				}
			}
			if (!temp_hand.isEmpty())
			{
				for (i = 0; i < temp_hand.size(); i++)
				{
					p1.add_to_hand(new Card(temp_hand.get(i).charAt(0), temp_hand.get(i).charAt(1)));
				}
			}

			line = reader.readLine(); //this line should contain capture pile: capture cards
			line = line.toLowerCase();
			if (!(line.length() <= 17)) //making sure capture pile is not empty
			{
				cutLoc = line.indexOf(':');
				line = line.substring(cutLoc + 2);
				funcHelper.clear();
				funcHelper.add(line);
				get_rid_of_end_space(funcHelper);		//getting rid of space at end of line if exists
				line = funcHelper.get(0);
				
				line = line.toLowerCase(); //converting line to lower case
				temp_capture.clear();
				if (line.length() > 0) {
					for (i = 0; i < line.length() - 1; i = i + 3)
					{	
						temp_capture.add(line.substring(i, i+2));
					}
				}
			}
			if (!temp_capture.isEmpty())
			{
				for (i = 0; i < temp_capture.size(); i++)
				{
					p1.add_to_capture(temp_capture.get(i).charAt(0), temp_capture.get(i).charAt(1));
				}
			}

			line = reader.readLine(); //this line should contain melds: prev melds
			line = line.toLowerCase();
			
			String prev_melds_human = "";
			if (!(line.length() <= 10)) //making sure player has melds to work with is not empty
			{
				cutLoc = line.indexOf(':');
				line = line.substring(cutLoc + 2);
				funcHelper.clear();
				funcHelper.add(line);
				get_rid_of_end_space(funcHelper);		//getting rid of space at end of line if exists
				line = funcHelper.get(0);
				
				line = line.toLowerCase(); //converting line to lower case
				prev_melds_human = line;			//Saving this line to use is later to update previous_melds_names vector

				num_commas = number_of_commas(line);
				for (index = 0; index < num_commas; index++)
				{
					String one_meld = line.substring(0, line.indexOf(','));
					line = line.substring(line.indexOf(',') + 2);
					adding_melds(one_meld, p1);
					p1.add_to_previous_melds_cards(",");
				}
				adding_melds(line, p1);
			}

			line = reader.readLine();	//this line should be empty
			line = reader.readLine(); // this line should be trump card: __
			line = line.toLowerCase();
			cutLoc = line.indexOf(':');
			line = line.substring(cutLoc + 2);
			String temp_trump = line;

			line = reader.readLine();	//this line should contain stock: ___
			line = line.toLowerCase();
			cutLoc = line.indexOf(':');
			line = line.substring(cutLoc + 2);
			List<String> temp_deck = new ArrayList<String>();
			
			line = line.toLowerCase(); //converting line to lower case
			if (line.length() > 0) {					//created a vector of string cards
				for (i = 0; i < line.length() - 1; i = i + 3)
				{
					temp_deck.add(line.substring(i, i+2));
				}
			}
			Deck help_deck = new Deck(temp_deck);			//creating the deck
			if(temp_trump.charAt(0) == ' '){
				temp_trump = temp_trump.substring(1);
			}
			help_deck.set_trump(temp_trump);	//defining the trump card
			System.out.printf("LLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLL %c %c LLLLLLLLLLLLLLLLLLLLLLLLLLLL", help_deck.get_trump().get_type(), help_deck.get_trump().get_suit());

			if (prev_melds_human.length() > 0)
			{
				num_commas = number_of_commas(prev_melds_human);
				for (index = 0; index < num_commas; index++)
				{
					String one_meld = prev_melds_human.substring(0, prev_melds_human.indexOf(','));
					prev_melds_human = prev_melds_human.substring(prev_melds_human.indexOf(',') + 2);
					adding_melds_names(one_meld, p1, help_deck.get_trump());
				}
				adding_melds_names(prev_melds_human, p1, help_deck.get_trump());
			}

			if (prev_melds_computer.length() > 0)
			{
				num_commas = number_of_commas(prev_melds_computer);
				for (index = 0; index < num_commas; index++)
				{
					String one_meld = prev_melds_computer.substring(0, prev_melds_computer.indexOf(','));
					prev_melds_computer = prev_melds_computer.substring(prev_melds_computer.indexOf(',') + 2);
					adding_melds_names(one_meld, p2, help_deck.get_trump());
				}
				adding_melds_names(prev_melds_computer, p2, help_deck.get_trump());
			}
			String nextPlayer;
			line = reader.readLine();// this line should be empty
			nextPlayer = reader.readLine();
			nextPlayer = nextPlayer.substring(13); // cutting off the beginning of the line to be left only with the player's name
			
			reader.close();
			System.out.printf("Game is loaded\n");

			round1 = new Round(round_number, p1, p2, help_deck, nextPlayer);
		}
		catch (Exception e)
		{
			System.err.format("Could not open this file");
			e.printStackTrace();
		}
	}
	
	
	//Event handlers:

	
	//Selectors:
	
	
	//Mutators:
	
	
	//main() method for debugging:
	public static void main(String[] args){
		//Game g1 = new Game();
	}
	
	//Any utility (private) methods:
	/**
	 A constructor for the Game class used when loading a gme from a file.
	 @param help- a list of strings containing 1 stirng (using a list to pass by reference.
	 */
	private void get_rid_of_end_space(List<String> help)
	{
		String str = help.get(0);
		if (str.length() > 0)
		{
			if (str.charAt(str.length() - 1) == ' ')
			{
				str = str.substring(0, str.length() - 1);
			}
		}
		help.clear();
		help.add(str);
	}
	/**
	 A constructor for the Game class used when loading a gme from a file.
	 @param line- a string type that contains the previous melds line from the file.
	 @return an integer type that indicates how many commas are in a specific line
	 */
	private int number_of_commas(String line)
	{
		int count = 0;
		for (int index = 0; index < line.length(); index++)
		{
			if (line.charAt(index) == ',')
			{
				count++;
			}
		}
		return count;
	}

	/**
	 This function is used to calculate the number of spaces in a string
	 	in order to know how many melds are in the line (to be able to parse
	 	it correctly).
	 @param one_meld- a string type that contains the one meld from the line of melds
	 @return an integer type that indicates how many spaces are in a specific line
	 */
	private int number_of_spaces(String one_meld)
	{
		int count = 0;
		for (int index = 0; index < one_meld.length(); index++)
		{
			if (one_meld.charAt(index) == ' ')
			{
				count++;
			}
		}
		return count;
	}

	/**
	 This function is used to add the meld cards into the previous_melds_cards
	 	list.
	 @param one_meld- a string type that contains the one meld from the line of melds.
	 @param one_player- a player type that contains the player to which the cards belong.
	 */
	private void adding_melds(String one_meld, Player one_player)
	{
		int num_spaces = number_of_spaces(one_meld);
		if (num_spaces == 0)
		{
			one_player.add_to_previous_melds_cards(one_meld);

		}
		else
		{
			List<String> temp = new ArrayList<String>();
			String[] strings = one_meld.split(" ");

			for (int index = 0; index < strings.length; index++)
			{
				one_player.add_to_previous_melds_cards(strings[index]);
			}
		}
	}

	/**
	 This function is used to add the meld names into the previous_melds_names
	 list.
	 @param one_meld- a string type that contains the one meld from the line of melds.
	 @param one_player- a player type that contains the player to which the cards belong.
	 @param trump- a Card type that hold's the trump card's suit and type
	 */
	private void adding_melds_names(String one_meld, Player one_player, Card trump)
	{
		List<Card> meld_cards = new ArrayList<Card>();
		String card;
		
		if (one_meld.charAt(0) == ' ')
		{
			one_meld = one_meld.substring(1);	// getting rid of spaces at beginning of string if exist
		}
		int spaces = number_of_spaces(one_meld);
		for (int i = 0; i < spaces + 1; i++)
		{
			card = one_meld.substring(0, one_meld.indexOf(' ')+1);
			if (card.length() == 3)		//this means card has * at the end
			{
				card = card.substring(0, 2);		//cutting the * off the card.
			}
			if(card.length() == 2){
				meld_cards.add(new Card(card.charAt(0), card.charAt(1)));
			}
			
		}
		if (one_meld.length() == 3)		//this means card has * at the end
		{
			one_meld = one_meld.substring(0, 2);		//cutting the * off the card.
			meld_cards.add(new Card(one_meld.charAt(0), one_meld.charAt(1)));
		}

		String current_meld_name = one_player.best_meld(meld_cards, trump);
		one_player.add_to_previous_melds_names(current_meld_name);
	}
}