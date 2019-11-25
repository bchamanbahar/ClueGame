package clueGame;

public class Solution {
	public String person;
	public String room;
	public String weapon;
	
	public Solution() {
		person = " ";
		room = " ";
		weapon = " ";
	}

	public Solution(String person, String room, String weapon) {
		this.person = person;
		this.room = room;
		this.weapon = weapon;
	}

	// equals method for cards
	@Override
	public boolean equals(Object o) {
		// compares name and type of card
		if (this.person.equals(((Solution) o).getPerson()) && this.room.equals(((Solution) o).getRoom()) && this.weapon.contentEquals(((Solution) o ).getWeapon())) {
			return true;
		} else
			return false;
	}

	public String getPerson() {
		return person;
	}

	public void setPerson(String person) {
		this.person = person;
	}

	public String getRoom() {
		return room;
	}

	public void setRoom(String room) {
		this.room = room;
	}

	public String getWeapon() {
		return weapon;
	}

	public void setWeapon(String weapon) {
		this.weapon = weapon;
	}
}
