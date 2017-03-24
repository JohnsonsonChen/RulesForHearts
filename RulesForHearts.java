import java.util.ArrayList;
import java.util.Scanner;
import java.io.*;

public class RulesForHearts {
	static Scanner keyboard = new Scanner(System.in);
	
	public static void main(String[] args) {
		System.out.println("Welcome to Rules For Hearts");
		System.out.println();
		System.out.println("type r for rules");
		System.out.println("type s to start");
		System.out.println("type q to quit");
		System.out.println();
		
		int[] points = new int[4]; // 0: player, 1: c1, 2: c2, 3: c3
		for(int i = 0; i < points.length; i++) {
			points[i] = 0;
		}
		
		boolean done = false;
		
		while(!done) {
		  String input = keyboard.nextLine(); // read input
		  if(input.equalsIgnoreCase("r")) {
			readRules(); // read rules from file
		  }
		  else if(input.equalsIgnoreCase("s")) {
			DeckOfCards allCards = new DeckOfCards(); // create a new deck of cards
			allCards.shuffle(); // shuffle it
			startGames(allCards, points); // start game
			System.out.println("Wanna Play again? (type s to play again)");
			System.out.println("type r for rules");
			System.out.println("type q to quit");
		  }
          else if(input.equalsIgnoreCase("q")) {
			done = true;
		  }
          else {
        	System.out.println("type r for rules");
      		System.out.println("type s to start");
      		System.out.println("type q to quit");
          }
		}
		
	}//main
	
	//read rules from file
	private static void readRules() {
		try {
			File r = new File("./RulesForHearts/roh.txt");
			
			BufferedReader b = new BufferedReader(
					new FileReader(r));
			
			String s = b.readLine();
			
			while(s != null) {
				System.out.println(s);
				s = b.readLine();
			}
			b.close();
		}
		catch(IOException e) {
			System.out.println("An IO Error Occurred");
			System.exit(0);
		}
	} // readRules
	
	private static void startGames(DeckOfCards allCards, int[] points) {
		Card[] p = new Card[13]; // position: south
		Card[] c1 = new Card[13]; // position: west
		Card[] c2 = new Card[13]; // position: north
		Card[] c3 = new Card[13]; // position: east
		
		//ArrayList<Card> round = new ArrayList<Card>(); // all cards in one round
		
		Card[] round = new Card[4];// all cards in one round
		round[0] = new Card(-1, -1);
		round[1] = new Card(-1, -1);
		round[2] = new Card(-1, -1);
		round[3] = new Card(-1, -1);
		
		ArrayList<String> allDealsInRound = new ArrayList<String>(); // all cards that are deal by players in one round
		
		
		String[] turnArr = new String[1]; // determine the first player of next turn
		
		int[] heartBroken = new int[1]; // determine if the heart is broken
		heartBroken[0] = 0;
		
		
		dealCards(p,c1,c2,c3,allCards); // deal cards to all the players
		
		int n0 = 13;
		int n1 = 13;
		int n2 = 13;
		int n3 = 13;
		
		printOpponentCards(n1, n2, n3);
		//printOpponentCardsShow(c1, c2, c3);
		printPlayerCards(p);
		
		boolean gameOver = false;
		boolean passCards = true;
		boolean beginningTurn = true;
		boolean firstRound = true;
		String turn = "";
		int pos = -1;
		int turnCounter = 0;
		
		//System.out.println("Please select 3 cards(i.e. \"1 2 3\") to pass to the component on your right");
		
		while(!gameOver) {
			//pass 3 cards to your component on your right at the beginning of the round
			if(passCards) {
				System.out.println("Please select 3 cards(i.e. \"1 2 3\") to pass to the component on your right");
				int fc = keyboard.nextInt() - 1;
				int sc = keyboard.nextInt() - 1;
				int tc = keyboard.nextInt() - 1;
				if(fc == sc || sc == tc || fc == tc) {
					System.out.println("Plase input 3 different numbers");
					continue;
				}
				else if(fc < 0 || fc > 12 || sc < 0 || sc > 12 || tc < 0 || tc > 12) {
					System.out.println("Plase input 3 valid numbers (between 1 to 13)");
					continue;
				}
				passCard(p, c1, c2, c3, fc, sc, tc);
				passCards = false;
				printOpponentCards(n1, n2, n3);
				//printOpponentCardsShow(c1, c2, c3);
				printPlayerCards(p);
			}
			else {
				if(beginningTurn) {
				  turnArr = beginningTurn(p,c1,c2,c3);
				  turn = turnArr[0];
				  //System.out.println(turnArr[1]);
				  pos = Integer.parseInt(turnArr[1]);
				}
				
				//Your turn
				if(turn.equals("p")) {
					System.out.println("Your Points: " + points[0]);
				    System.out.println("Jack's Points: " + points[1]);
				    System.out.println("Jason's Points: " + points[2]);
				    System.out.println("Janet's Points: " + points[3]);
				    if(!allDealsInRound.isEmpty()) {
						for(String s : allDealsInRound) {
							System.out.println(s);
						}
					}
				    else {
				    	System.out.println("You lead this round");
				    }
					playerDealCard(p, beginningTurn, pos, round, points, turnArr, allDealsInRound, firstRound, heartBroken);
					n1 = getSize(c1);
					n2 = getSize(c2);
					n3 = getSize(c3);
					printOpponentCards(n1, n2, n3);
					printPlayerCards(p);
					turnCounter ++;
					if(beginningTurn) {
						beginningTurn = false;
					}
				}
				
				//computer's turn
				else {
					OpponentDealCard(turn, pos, c1, c2, c3, round, points, turnArr, allDealsInRound, firstRound, heartBroken);
					n1 = getSize(c1);
					n2 = getSize(c2);
					n3 = getSize(c3);
					printOpponentCards(n1, n2, n3);
					printPlayerCards(p);
					turnCounter ++;
					if(beginningTurn) {
						beginningTurn = false;
					}
				}
				//determine who's going to play for the next turn
				turn = getNextTurn(turn);
				
				//reset everything if this turn is done
				if(turnCounter == 4) {
					turnCounter = 0;
					turn = turnArr[0];
					allDealsInRound.clear();
					firstRound = false;
					n0 = getSize(p);
					n1 = getSize(c1);
					n2 = getSize(c2);
					n3 = getSize(c3);
					if(n0 == 0 && n1 == 0 && n2 == 0 && n3 == 0) {
						gameOver = true;
						whoWins(points);
					}
				}
			}
		}
	}
	
	//determine who wins this round
	private static void whoWins(int[] points) {
		ArrayList<Integer> winners = new ArrayList<Integer>();
		int min = 100;
		int counter = 0;
		for(int i : points) {
			if(i < min) {
				min = i;
				winners.clear();
				winners.add(counter);
			}
			else if(i == min) {
				winners.add(counter);
			}
			counter ++;
		}
		
		String result = "";
		for(int i : winners) {
			switch(i) {
			case 0:
				result += "You " + ": " + points[0] + " points ";
				break;
			case 1:
				result += "Jack " + ": " + points[1] + " points ";
				break;
			case 2:
				result += "Jason " + ": " + points[2] + " points ";
				break;
			case 3:
				result += "Janet " + ": " + points[3] + " points ";
				break;
			}
		}
		
		System.out.println(result + "Win!");
	}//who wins
	
	private static void dealCards(Card[] p, Card[] c1, Card[] c2, Card[] c3, DeckOfCards allCards) {
		
		for(int i = 0; i < 4; i++) {
			for(int j = 0; j < 13; j++) {
				if(i == 0) {
					p[j] = allCards.deal();
				}
				else if(i == 1) {
					c1[j] = allCards.deal();
				}
                else if(i == 2) {
                	c2[j] = allCards.deal();
				}
                else if(i == 3) {
                	c3[j] = allCards.deal();
                }
			}
		}
	}
	
	private static void printPlayerCards(Card[] p) {
		int counter = 1;
		System.out.println("You(bottom): ");
		System.out.println("[");
		for(Card c : p) {
			if(c.getSuitText() != -1) {
			  System.out.println(counter + ": " + c.toString());
			}
			counter ++;
		}
		System.out.println("]");
		System.out.println();
	}//printPlayerCards
	
	private static void printOpponentCards(int n1, int n2, int n3) {
		int i = 0;
		
	    System.out.print("Jack(left): ");
		while(i < n1) {
			System.out.print("*");
			i++;
		}
		System.out.println();
		
		i = 0;
		System.out.print("Jason(top): ");
		while(i < n2) {
			System.out.print("*");
			i++;
		}
		System.out.println();
		
		i = 0;
		System.out.print("Janet(right): ");
		while(i < n3) {
			System.out.print("*");
			i++;
		}
		System.out.println();
	}//printOpponentCards
	
	private static void printOpponentCardsShow(Card[] c1, Card[] c2, Card[] c3) {
		int counter = 1;
		
	    System.out.print("Jack(left): ");
	    System.out.println("[");
		for(Card c : c1) {
			if(c.getSuitText() != -1) {
				  System.out.println(counter + ": " + c.toString());
			}
			counter ++;
		}
		System.out.println("]");
		System.out.println();
		
		counter = 1;
		System.out.print("Jason(top): ");
		System.out.println("[");
		for(Card c : c2) {
			if(c.getSuitText() != -1) {
				  System.out.println(counter + ": " + c.toString());
			}
			counter ++;
		}
		System.out.println("]");
		System.out.println();
		
		counter = 1;
		System.out.print("Janet(right): ");
		System.out.println("[");
		for(Card c : c3) {
			if(c.getSuitText() != -1) {
				  System.out.println(counter + ": " + c.toString());
			}
			counter ++;
		}
		System.out.println("]");
		System.out.println();
	}//printOpponentCards
	
	private static void passCard(Card[] p, Card[] c1, Card[] c2, Card[] c3, int fc, int sc, int tc) {
		// p -> c1
		//System.out.println("p -> c1: " + p[fc].toString() + ", " + p[sc].toString() + ", " + p[tc].toString());
		int ran1 = (int)Math.floor(Math.random() * 13);
		int ran2 = (int)Math.floor(Math.random() * 13);
		while(ran2 == ran1) {
			ran2 = (int)Math.floor(Math.random() * 13);
		}
		int ran3 = (int)Math.floor(Math.random() * 13);
        while(ran3 == ran1 || ran3 == ran2) {
        	ran3 = (int)Math.floor(Math.random() * 13);
		}
		
		Card cardOne = c1[ran1];
		Card cardTwo = c1[ran2];
		Card cardThree = c1[ran3];
		
		c1[ran1] = p[fc];
		c1[ran2] = p[sc];
		c1[ran3] = p[tc];
		
		//System.out.println("c1 -> c2: " + cardOne.toString() + ", " + cardTwo.toString() + ", " + cardThree.toString());
		
		
		// c3 -> p
		ran1 = (int)Math.floor(Math.random() * 13);
		ran2 = (int)Math.floor(Math.random() * 13);
		while(ran2 == ran1) {
			ran2 = (int)Math.floor(Math.random() * 13);
		}	
		ran3 = (int)Math.floor(Math.random() * 13);
		while(ran3 == ran1 || ran3 == ran2) {
        	ran3 = (int)Math.floor(Math.random() * 13);
		}
		
		p[fc] = c3[ran1];
		p[sc] = c3[ran2];
		p[tc] = c3[ran3];

		//System.out.println("c3 -> p: " + c3[ran1] + ", " + c3[ran2] + ", " + c3[ran3]);
		
		// c2 -> c3
		int ran4 = (int)Math.floor(Math.random() * 13);
		int ran5 = (int)Math.floor(Math.random() * 13);
		while(ran5 == ran4) {
			ran5 = (int)Math.floor(Math.random() * 13);
		}	
		int ran6 = (int)Math.floor(Math.random() * 13);
		while(ran6 == ran5 || ran6 == ran4) {
        	ran6 = (int)Math.floor(Math.random() * 13);
		}
		
		c3[ran1] = c2[ran4];
		c3[ran2] = c2[ran5];
		c3[ran3] = c2[ran6];
		
		//System.out.println("c2 -> c3: " + c2[ran4] + ", " + c2[ran5] + ", " + c2[ran6]);
		
		// c1 -> c2
		c2[ran4] = cardOne;
		c2[ran5] = cardTwo;
		c2[ran6] = cardThree;

	}//passCard
	
	//determine who's having the clubs of two at the beginning of this round 
	private static String[] beginningTurn(Card[] p, Card[] c1, Card[] c2, Card[] c3) {
		boolean found = false;
		String[] result = new String[2];
		int counter = 0;
		
		for(Card c : p) {
			if(c.getSuit().equals("clubs") && c.getValue() == 2) {
				found = true;
				result[0] = "p";
				result[1] = Integer.toString(counter);
			}
			counter ++;
		}
		
		counter = 0;
		if(!found) {
			for(Card c : c1) {
				if(c.getSuit().equals("clubs") && c.getValue() == 2) {
					found = true;
					result[0] = "c1";
					result[1] = Integer.toString(counter);
				}
				counter ++;
			}
		}
		
		counter = 0;
		if(!found) {
			for(Card c : c2) {
				if(c.getSuit().equals("clubs") && c.getValue() == 2) {
					found = true;
					result[0] = "c2";
					result[1] = Integer.toString(counter);
				}
				counter ++;
			}
		}
		
		counter = 0;
		if(!found) {
			for(Card c : c3) {
				if(c.getSuit().equals("clubs") && c.getValue() == 2) {
					found = true;
					result[0] = "c3";
					result[1] = Integer.toString(counter);
				}
				counter ++;
			}
		}
		
		return result;
	}//beginning Turn
	
	
	private static String getNextTurn(String t) {
		String newTurn = "";
		switch(t) {
		case "p":
			newTurn = "c1";
			break;
		case "c1":
			newTurn = "c2";
			break;
		case "c2":
			newTurn = "c3";
			break;
		case "c3":
			newTurn = "p";
			break;
		}
		
		return newTurn;
	}// getNextTurn
	
	
	private static void playerDealCard(Card[] p, boolean begin, int co2, Card[] round, int[] points, String[] turns, ArrayList<String> allDealsInRound, boolean firstRound, int[] heartBroken) {
		boolean dealed = false;
		String suit = "";
		//int vaule = -1;
		int roundSize = getSize(round);
		
		int size = getSize(p);
		
		if(roundSize != 0) {
			suit = round[0].getSuit();
			//vaule = round.get(0).getValue();
		}
		
		while(!dealed) {
			System.out.print("Please select a card to deal: ");
			int num = keyboard.nextInt();
			keyboard.nextLine();
			
			//at the beginning of the game(round)
			if(begin) {
				if(num != co2 + 1) {
					System.out.println("Since it is the beginning of the game, you have to deal club of 2");
					System.out.println("And the club of 2 is at " + (co2 + 1));
					continue;
				}
			}
			// check valid number of cards
			else if(num > size  || num < 1) {
			  System.out.println("Please Select a Card from 1 to " + size);
			  continue;
			}
			//check valid suit
			else if(roundSize != 0 && getSmallestSameSuit(p, round) != -1) {
			   String s = p[num - 1].getSuit();
			     if(!suit.equals(s)) {
				    System.out.println("Please Select a Card that has the same suit as the first card dealed in this turn: " + suit);
					continue;
				 }
			}
			//check heart or the queen of spades at the first round
			else if(firstRound) {
				String s = p[num - 1].getSuit();
				int v = p[num - 1].getValue();
				if(s.equals("hearts") || (s.equals("spade") && v == 12)) {
					System.out.println("You cannot select a heart or the queen of spades at the first round");
					continue;
				}
			}
			//check if the hearts is broken
			else if(roundSize == 0 && heartBroken[0] == 0) {
				String s = p[num - 1].getSuit();
				if(s.equals("hearts")) {
					System.out.println("You cannot lead with hearts until the hearts is played on the other suits");
					continue;
				}
			}
			//store the card that is deal by player into an array
			round[roundSize] = new Card(p[num-1].getSuitText(), p[num-1].getValue());
			
			//calculate points at the end of the round
			if(roundSize == 3) {
				calculatePoints(points, round, "p", turns, heartBroken);
			}
			
			dealOneCard(p, num - 1, "p", allDealsInRound);
			dealed = true;
		}
		
		//print player's cards
		printPlayerCards(p);
		
	}//playerDealCard
	
	private static void OpponentDealCard(String turn, int pos, Card[] c1, Card[] c2, Card[] c3, Card[] round, int[] points, String[] turns, ArrayList<String> allDealsInRound, boolean firstRound, int[] heartBroken) {
		int size = 0; // how many cards do the player have
		int roundSize = getSize(round); // how many cards that has been deal so far

		if(turn.equals("c1")) { // c1's turn
			size = getSize(c1); 
			if(roundSize != 0) { // if c1 is not leading this round
				pos = getSmallestSameSuit(c1, round);  // get the smallest card that has the same suit as the first card deal in this round
				if(pos == -1) { // if there is no card with the same suit, pick a hearts and deal
					size = getSize(c1);
					pos = 0;
					String s = c1[pos].getSuit();
					while(!(s.equals("hearts")) && pos < size) { 
						pos ++;
						s = c1[pos].getSuit();
					}
					if(pos == size && size > 0) {
						pos --;
					}
				}
				if(firstRound) { // it can deal any cards except for the hearts and spade of queen at the beginning of the game
					String s = c1[pos].getSuit();
					int v = c1[pos].getValue();
					while(s.equals("hearts") || (s.equals("spade") && v == 12)) {
						pos = (int)Math.floor(Math.random() * size);
						s = c1[pos].getSuit();
						v = c1[pos].getValue();
					}
				}
			}
			if(roundSize == 0 && !firstRound) { // if c1 is leading this round
				if(heartBroken[0] == 0) { // if hearts has not been broken, deal a card that is not a hearts
					pos = 0;
					String s = c1[pos].getSuit();
					//int v = c1[pos].getValue();
					while(s.equals("hearts") && pos < size) {
						pos ++;
						s = c1[pos].getSuit();
					}
					if(pos == size) {
						pos --;
					}
					//System.out.println("c1 pos without heartbroken: " + pos);
				}
				else {
					pos = (int)Math.floor(Math.random() * size);
				}
			}
			round[roundSize] = new Card(c1[pos].getSuitText(), c1[pos].getValue()); // store the card deal by c1 into an array
            //calculate the points at the end of this round
			if(roundSize == 3) {
            	calculatePoints(points, round, "c1", turns, heartBroken);
			}
			dealOneCard(c1, pos, turn, allDealsInRound);
		}
		else if(turn.equals("c2")) { // c2's turn
			size = getSize(c2);
			if(roundSize != 0) {
				pos = getSmallestSameSuit(c2, round);
				if(pos == -1) {
					size = getSize(c2);
					pos = 0;
					String s = c2[pos].getSuit();
					while(!(s.equals("hearts")) && pos < size) {
						pos ++;
						s = c2[pos].getSuit();
					}
					if(pos == size && size > 0) {
						pos --;
					}
				}
				if(firstRound) {
					String s = c2[pos].getSuit();
					int v = c2[pos].getValue();
					while(s.equals("hearts") || (s.equals("spade") && v == 12)) {
						pos = (int)Math.floor(Math.random() * size);
						s = c2[pos].getSuit();
						v = c2[pos].getValue();
					}
				}
			}
			if(roundSize == 0 && !firstRound) {
				if(heartBroken[0] == 0) {
					pos = 0;
					String s = c2[pos].getSuit();
					//int v = c2[pos].getValue();
					while(s.equals("hearts") && pos < size) {
						pos ++;
						s = c2[pos].getSuit();
					}
					if(pos == size) {
						pos --;
					}
					//System.out.println("c2 pos without heartbroken: " + pos);
				}
				else {
					pos = (int)Math.floor(Math.random() * size);
				}
			}
			round[roundSize] = new Card(c2[pos].getSuitText(), c2[pos].getValue());
            if(roundSize == 3) {
            	calculatePoints(points, round, "c2", turns, heartBroken);
			}
			dealOneCard(c2, pos, turn, allDealsInRound);
		}
        else if(turn.equals("c3")) { // c3's turn
        	size = getSize(c3);
        	if(roundSize != 0) {
				pos = getSmallestSameSuit(c3, round);
				if(pos == -1) {
					size = getSize(c3);
					pos = 0;
					String s = c3[pos].getSuit();
					while(!(s.equals("hearts")) && pos < size) {
						pos ++;
						s = c3[pos].getSuit();
					}
					if(pos == size && size > 0) {
						pos --;
					}
				}
				if(firstRound) {
					String s = c3[pos].getSuit();
					int v = c3[pos].getValue();
					while(s.equals("hearts") || (s.equals("spade") && v == 12)) {
						pos = (int)Math.floor(Math.random() * size);
						s = c3[pos].getSuit();
						v = c3[pos].getValue();
					}
				}
			}
        	if(roundSize == 0 && !firstRound) {
				if(heartBroken[0] == 0) {
					pos = 0;
					String s = c3[pos].getSuit();
					//int v = c3[pos].getValue();
					while(s.equals("hearts") && pos < size) {
						pos ++;
						s = c3[pos].getSuit();
					}
					if(pos == size) {
						pos --;
					}
					//System.out.println("c3 pos without heartbroken: " + pos);
				}
				else {
					pos = (int)Math.floor(Math.random() * size);
				}
			}
        	round[roundSize] = new Card(c3[pos].getSuitText(), c3[pos].getValue());
            if(roundSize == 3) {
            	calculatePoints(points, round, "c3", turns, heartBroken);
			}
        	dealOneCard(c3, pos, turn, allDealsInRound);
		}
		/*printOpponentCardsShow(c1, c2, c3);
		System.out.println();
		for(Card c : round) {
	    	System.out.println(c.getSuitText() + " " + c.getValue());
	    }*/
	}//OpponentDealCard
	
	private static void dealOneCard(Card[] cards, int index, String turn, ArrayList<String> allDealsInRound) {
		System.out.println(turn + " dealed: " + cards[index].toString()); // print out what card has been deal by whom
		allDealsInRound.add(turn + " dealed: " + cards[index].toString());
		//cards[index].setSuit(-1);
		//cards[index].setValue(-1);
		
		int size = getSize(cards);
		
		//shift all the card by 1 slot
		for(int i = index; i < size - 1; i++) {
			cards[i] = cards[i+1];
		}
		
		//set the deal card to -1
		if(size - 1 >= 0) {
		  cards[size - 1] = new Card(-1, -1);
		}
	}// dealOneCard
	
	private static int getSize(Card[] cards) {
		int i = cards.length - 1;
		for(; i >= 0; i--) {
			if(cards[i].getSuitText() != -1) {
				break;
			}
		}
		return i + 1;
	} // getSize
	
	//find out the smallest card with the same suit as the first card being played this turn
	private static int getSmallestSameSuit(Card[] cards, Card[] round) {
		//ArrayList<Card> allSameSuit = new ArrayList<Card>();
		String fSuit = round[0].getSuit();
		//int fValue = round[0].getValue();
		
		int size = getSize(cards);
		int min = 100;
        int min_index = -1;
		for(int i = 0; i < size; i++) {
			String s = cards[i].getSuit();
			int v = cards[i].getValue();
			if(v == 1) {
				v = 14;
			}
			if(fSuit.equals(s)) {
				if(v <= min) {
					min = v;
					min_index = i;
				}
			}
		}
		return min_index;
	}//getSmallestSameSuit
	
	private static void calculatePoints(int[] points, Card[] round, String turn, String[] turns, int[] heartBroken) {
		String fSuit = round[0].getSuit();
		//int fValue = round[0].getValue();
		
		int size = getSize(round);
		int max = -1;
        int max_index = -1;
        for(int i = 0; i < size; i++) {
			String s = round[i].getSuit();
			int v = round[i].getValue();
			if(v == 1) {
				v = 14;
			}
			if(fSuit.equals(s)) { // find out the max cards in this turn
				if(v > max) {
					max = v;
					max_index = i;
				}
			}
			if(s.equals("hearts") && i > 0) { // break the hearts
				heartBroken[0] = 1;
			}
		}
        
        int whoGetsPoints = 0;
        if(turn.equals("p")) {
        	if(max_index == 0) {
        		whoGetsPoints = 1; // c1
        	}
        	else if(max_index == 1) {
        		whoGetsPoints = 2; // c2
        	}
            else if(max_index == 2) {
                whoGetsPoints = 3; // c3
        	}
            else if(max_index == 3) {
                whoGetsPoints = 0; // p
            }
        }
        else if(turn.equals("c1")) {
            if(max_index == 0) {
            	whoGetsPoints = 2; // c2
        	}
        	else if(max_index == 1) {
        		whoGetsPoints = 3; // c3
        	}
            else if(max_index == 2) {
            	whoGetsPoints = 0; // p
        	}
            else if(max_index == 3) {
            	whoGetsPoints = 1; // c1
            }
        }
        else if(turn.equals("c2")) {
            if(max_index == 0) {
            	whoGetsPoints = 3; // c3
        	}
        	else if(max_index == 1) {
        		whoGetsPoints = 0; // p
        	}
            else if(max_index == 2) {
            	whoGetsPoints = 1; // c1
        	}
            else if(max_index == 3) {
            	whoGetsPoints = 2; // c2
            }
        }
        else if(turn.equals("c3")) {
            if(max_index == 0) {
            	whoGetsPoints = 0; // p
        	}
        	else if(max_index == 1) {
        		whoGetsPoints = 1; // c1
        	}
            else if(max_index == 2) {
            	whoGetsPoints = 2; // c2
        	}
            else if(max_index == 3) {
            	whoGetsPoints = 3; // c3
            }
       }
        
       int point = 0; 
       for(Card c : round) {
    	   if(c.getSuit().equals("hearts")) { // if there is one hearts, add one point
    		   point ++;
    	   }
    	   if(c.getSuit().equals("spade") && c.getValue() == 12) { // if there is a spade of queen, add 3 points
    		   point += 3;
    	   }
       }
       
       points[whoGetsPoints] += point;
       String nextTurn = "";
       
       //whoever gets the points will play first at next turn
       switch(whoGetsPoints) {
       case 0:
    	   nextTurn = "p";
    	   break;
       case 1:
    	   nextTurn = "c1";
    	   break;
       case 2:
    	   nextTurn = "c2";
    	   break;
       case 3:
    	   nextTurn = "c3";
    	   break;
       }
       turns[0] = nextTurn;
        
       
       System.out.println("Your Points: " + points[0]);
       System.out.println("Jack's Points: " + points[1]);
       System.out.println("Jason's Points: " + points[2]);
       System.out.println("Janet's Points: " + points[3]);
       
       for(Card c: round) {
         c.setSuit(-1);
         c.setValue(-1);
       }
       
       /*for(Card c: round) {
    	   System.out.println("Round Right Now: " + c.toString());
       }*/
        
	}
}