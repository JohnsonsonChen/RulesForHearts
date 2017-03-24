public class DeckOfCards {
	private Card[] deckOfCards;
	
	private int currentCard = 0;
	
	public DeckOfCards() {
		deckOfCards = new Card[52];
		int i = 0;
		for(int suit = 0; suit < 4; suit ++) {
			for(int value = 1; value <= 13; value++) {
				deckOfCards[i] = new Card(suit, value);
				i ++;
			}
		}
	}
	
	public void shuffle() {
		for(int i = 0; i < 51; i++) {
		  int num1 = (int)Math.floor(Math.random() * (51 - i)) + i;
		
	      Card temp = deckOfCards[i];
	      deckOfCards[i] = deckOfCards[num1];
	      deckOfCards[num1] = temp;
		}
	}
	
	public Card deal() {
		return deckOfCards[currentCard ++];
	}
	
	public void restart() {
		shuffle();
		currentCard = 0;
	}
}