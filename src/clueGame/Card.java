package clueGame;

public class Card {
	private String cardName;
	private CardType cardType;
	
	public boolean equals(Card aCard) {
		return false;
	}
	
	public void setName(String name) {
		this.cardName = name;
	}
	
	public void setCardType(CardType type){
		this.cardType = type;
	}
	
	public String getName() {
		return this.cardName;
	}
	
	public CardType getCardType() {
		return cardType;
	}
}
