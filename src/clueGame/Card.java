package clueGame;

public class Card {
	private String cardName;
	private CardType cardType;
	
	public Card() {

	}
	
	public Card(String name, CardType type) {
		cardName = name; 
		cardType = type;
	}
	
	//equals method for cards
	@Override
	public boolean equals(Object o) {
		//compares name and type of card
		if (this.cardName.equals(((Card) o).getName()) && this.cardType.equals(((Card) o).getCardType())){
			return true;
		}
		else return false;
	}
	
	@Override
	public int hashCode() {
		return cardName.hashCode();
	}
	
	//set card name
	public void setName(String name) {
		this.cardName = name;
	}
	
	//set card type
	public void setCardType(CardType type){
		this.cardType = type;
	}
	
	//get name of card
	public String getName() {
		return this.cardName;
	}
	
	//get card type
	public CardType getCardType() {
		return this.cardType;
	}
}
