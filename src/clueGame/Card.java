package clueGame;

public class Card {
	private String cardName;
	private CardType cardType;
	
	@Override
	public boolean equals(Object o) {
		if (this.cardName.equals(((Card) o).getName()) && this.cardType.equals(((Card) o).getCardType())){
			return true;
		}
		else return false;
	}
	
	@Override
	public int hashCode() {
		return cardName.hashCode();
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
		return this.cardType;
	}
}
