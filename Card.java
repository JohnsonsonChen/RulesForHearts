public class Card {
	private int suit;
	private int value;
	
	public Card(int s, int v) {
		suit = s;
		value = v;
	}
	
	public Card(Card nCard) {
	  this(nCard.getSuitText(), nCard.getValue());
	}
	
	public String getSuit() {
		String result = "";
		switch(suit) {
		case 0:
			result = "spade";
			break;
		case 1:
			result = "hearts";
			break;
		case 2:
			result = "clubs";
			break;
		case 3:
			result = "diamonds";
			break;
		}
		return result;
	}
	
    public int getValue() {
    	return value;
    }
    
    public int getSuitText() {
        return suit;
    }
    
    public String toString() {
    	return getSuit() + " " + value;
    }
    
    public void setSuit(int newSuit) {
    	suit = newSuit;
    }
    
    public void setValue(int newV) {
    	value = newV;
    }
    
}