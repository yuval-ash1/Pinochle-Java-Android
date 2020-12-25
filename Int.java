package edu.ramapo.yashken1.pinochle;

import java.io.*;
import java.util.*;

class Int implements Serializable{
	int nextPlayerIndex;
	
	
	int get_value() {
		return nextPlayerIndex;
	}
	void human_starts(){
		nextPlayerIndex = 0;
	}
	void computer_starts(){
		nextPlayerIndex = 1;
	}
	
	void switchPlayers() {
		nextPlayerIndex = (nextPlayerIndex + 1) % 2;
	}
	
}