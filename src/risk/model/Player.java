package risk.model;

import java.util.ArrayList;

import risk.model.map.MapNode;

/**
 * This class represent the player.
 * @author Gurpreet
 * @version 1.0
 */
public class Player {
	
	/**
	 * name of player.
	 */
	private String name;
	
	/**
	 * list of countries owned by player.
	 */
	private ArrayList<CountryNode> countries;
	
	/**
	 * list of continents owned by player.
	 */
	private ArrayList<MapNode> continents;
	
	/**
	 * list of cards player has.
	 */
	private ArrayList<Card> cards;
	
	/**
	 * number of times player exchanged the cards.
	 */
	private int cardsusedCount = 1;
	
	/**
	 * number of armies player has.
	 */
	private int armiesCount;
	
	/**
	 * turn of player
	 */
	private boolean turn = false;
	
	/**
	 * ArrayList of all continents in the Map.
	 */
	private ArrayList<MapNode> mapData;
	
	/**
	 * number of countries owned by player
	 */
	private int playerCountryCount;

	/**
	 * shows if player is still in game 
	 */
	private boolean lost = false;
	
	/**
	 * Initialize player object with name.
	 * @param name name of player.
	 */
	public Player(String name) {
		this.name = name;
		this.countries = new ArrayList<CountryNode>();
		this.continents = new ArrayList<MapNode>();
		this.cards = new ArrayList<Card>();
	}
	
	/**
	 * Initialize player object with name and armies.
	 * @param name name of the player.
	 * @param newArmies armies of the player.
	 */
	public Player(String name, int newArmies) {
		this.name = name;
		this.countries = new ArrayList<CountryNode>();
		this.continents = new ArrayList<MapNode>();
		this.cards = new ArrayList<Card>();
		this.armiesCount = newArmies;
		this.mapData = new ArrayList<MapNode>();
	}
	
	/**
	 * Initialize player object with name and armies.
	 * @param name name of the player.
	 * @param newArmies armies of the player.
	 * @param countriesList ArrayList of all countries owned by player.
	 */
	public Player(String name, int newArmies, ArrayList<CountryNode> countriesList) {
		this.name = name;
		this.countries = countriesList;
		this.continents = new ArrayList<MapNode>();
		this.cards = new ArrayList<Card>();
		this.armiesCount = newArmies;
		this.mapData = new ArrayList<MapNode>();
	}
	
	/**
	 * return name of the player.
	 * @return name of player in string format.
	 */
	public String getName() {
		return this.name;
	}
	
	/**
	 * Add country to the list of countries owned by player.
	 * @param country country owned by player
	 */
	public void addCountry(CountryNode country) {
		this.countries.add(country);
	}
	
	/**
	 * returns list of countries owned by the player.
	 * @return ArrayList containing countries.
	 */
	public ArrayList<CountryNode> getCountries() {
		return this.countries ;	
	}
	
	/**
	 * Gets the list of countries owned by the player.
	 * @return list of country names
	 */
	public String[] getCountriesNames() {
		String[] names = new String[this.countries.size()];
		for(int i=0;i<names.length;i++){
			names[i] = this.countries.get(i).getCountryName();
			System.out.println(names[i]);
		}
		return names;
	}
	
	/**
	 * Gives the countries owned by the player having no armies.
	 * @return list of country names with no army.
	 */
	public String[] getCountriesNamesNoArmy(){
		ArrayList<String> names = new ArrayList<String>();
		for(CountryNode c : this.countries){
			if(c.getArmiesCount()==0)
				names.add(c.getCountryName());
		}
		return names.toArray(new String[names.size()]);
	}
	
	/**
	 * Removes country from list of countries owned by player.
	 * @param country Country object to be removed from list
	 */
	public void removeCountry(CountryNode country) {
		this.countries.remove(country);
	}
	
	/**
	 * Add new card to list of cards player has.
	 * @param card new card to be added to list.
	 */
	public void addCard(Card card) {
		this.cards.add(card);
	}

	/**
	 * Removes the card from the list of cards.
	 * @param card card to be removed from list. 
	 */
	public void removeCard(Card card) {
		this.cards.remove(card);
	}
	
	/**
	 * Add new continent to the list of continents.
	 * @param continent continent to be added to list of continent owned by player.
	 */
	public void addContinent(MapNode continent) {
		this.continents.add(continent);
	}
	
	/**
	 * remove continent from the list of continent owned by the player.
	 * @param continent continent to be removed from list of continents owned by player.
	 */
	public void removeContinent(MapNode continent) {
		this.continents.remove(continent);
	}
	
	/**
	 * Checks for the continents owned by the player.
	 */
	public void checkContinent() {
		for (MapNode continent : this.mapData) {
			System.out.println("Inside ForLoop");
			System.out.println(continent.getContinentName());
			if (this.countries.containsAll(continent.getCountryList())) {
				System.out.println("Inside If Stametment");
				addContinent(continent);
				System.out.println("Added" + continent.getContinentName());
			}
		}
	}
	
	/**
	 * Calculates the armies to be alloted to the player at each turn.
	 * @return army count
	 */
	public int getArmies() {
		checkContinent();
		int countriesCount = this.countries.size();
		int continentsCount = this.continents.size();
		int cardsCount = this.cards.size();
		int armyCount = countriesCount/3;
		if(armyCount<3) {
			armyCount = 3;
		}
		if (continentsCount > 0) {
			continentsCount = 0;
			for (MapNode continent : this.continents){
				continentsCount =+ continent.getControlValue();
			}
		}
		armyCount += continentsCount;
		System.out.println(armyCount);
		if (cardsCount > 5) {
			//do something here with the cards count
			armyCount =+ 5* this.cardsusedCount;
			this.cardsusedCount++;
			//remove cards here
		}
		
		return armyCount;
	}
	
	/**
	 *  Assign armies to the player.
	 *  @param newCount armies to be assigned.
	 */
	public void assignArmies(int newCount) {
		this.armiesCount += newCount;
	}
	
	/**
	 *  Remove Armies from player.
	 *  @param newCount armies to be removed.
	 */
	public void removeArmies(int newCount) {
		this.armiesCount -= newCount;
	}
	
	/**
	 * Gives the number of armies the player has.
	 * @return number of armies player has.
	 */
	public int getArmiesCount() {
		return this.armiesCount;
	}
	
	/**
	 * Gives the country node for the given country name.
	 * @param newCountry country name whose country node is required.
	 * @return country node matching the string 
	 */
	public CountryNode getCountry(String newCountry) {
		return CountryNode.getCountry(countries, newCountry);
	}
	
	/**
	 *  sets player turn to true
	 */
	public void setTurnTrue() {
		this.turn = true;
	}
	
	/**
	 *  sets player turn to false
	 */
	public void setTurnFalse() {
		this.turn = false;
	}
	
	/**
	 * Gives the value of the turn of the player (True or False).
	 *  @return turn of player
	 */
	public boolean getTurn() {
		return this.turn;
	}
	
	/**
	 * Sets the armies of the player to the new value.
	 * @param newArmies new value of the army to be set.
	 */
	public void setArmies(int newArmies) {
		this.armiesCount = newArmies;
	}
	
	/**
	 * This method runs the reinforcement phase
	 */
	public void reinforcementPhase(){
		GameDriver.getInstance().getControlGUI().reinforcementControls(getArmiesCount(), getCountriesNames());
		GameDriver.getInstance().setControlsActionListeners();
	}
	
	/**
	 * This method runs attack phase.
	 */
	public void attackPhase(){
		ArrayList<String> countriesList = new ArrayList<String>();
		for(CountryNode c : this.countries) {
			if(c.getArmiesCount()>1) {
				for(CountryNode n: c.getNeighbourCountries()) {
					if(!n.getOwner().equals(this)) {
						countriesList.add(c.getCountryName());
					}
				}
			}
		}
		if(countriesList.isEmpty()) {
			GameDriver.getInstance().changePhase();
		}
		else {
			GameDriver.getInstance().getControlGUI().attackControls(countriesList.toArray(new String[countriesList.size()]));
			GameDriver.getInstance().setAttackListeners();
		}
	}
	
	/**
	 * This method runs the fortification phase
	 */
	public void fortificationPhase(){
		GameDriver.getInstance().getControlGUI().fortificationControls(getCountriesNames());
		GameDriver.getInstance().setFortificationLiteners();
	}
	
	/**
	 * Shifts(or places) the armies of the player on each reinforcement.
	 * @param country the country name to which armies are added.
	 * @param armies the number of armies to be reinforced.
	 * @return the army count left for the player.
	 */
	public int shiftArmiesOnReinforcement(String country, int armies) {
		getCountry(country).addArmy(armies);
		removeArmies(armies);
		return this.armiesCount;
	}
	
	/**
	 * Shifts the armies of the player from one country to another.
	 * @param sCountry the country name from which armies are moved.
	 * @param sNeighbour the country name to which armies are added.
	 * @param selectedArmies the number of armies to be moved.
	 * @return the army count left in sNeighbour country.
	 */
	public int getArmiesShiftedAfterFortification(String sCountry, String sNeighbour, int selectedArmies){
		CountryNode countrySelect = getCountry(sCountry);
		CountryNode neighbourC = getCountry(sNeighbour);
		countrySelect.setArmies(countrySelect.getArmiesCount()-selectedArmies);
		neighbourC.setArmies(neighbourC.getArmiesCount() + selectedArmies);
		return neighbourC.getArmiesCount();
	}
	
	/**
	 * This method calculate the number of dice a player can roo during attack phase
	 * @param country country selected as attacking or attacked
	 * @return number of dice to roll
	 */
	public int selectDiceForAttack(String country) {
		CountryNode aCountry = getCountry(country);
		int aArmies = aCountry.getArmiesCount();
		if(turn && aArmies>4) {
			aArmies = 3;
		}
		else if(turn) {
			aArmies =- 1;
		}
		else if(aArmies>2) {
			aArmies = 2;
		}
		return GameDriver.getInstance().setUpBoxInput(1, aArmies,this.name+"! Please select number of dice to roll.");
	}
	
	/**
	 * 
	 * @return number of countries owned by player
	 */
	public int getPlayerCountryCount(){
		return getCountries().size();
	}

	/**
	 * This methods returns value of lost attribute. 
	 * @return value of lost
	 */
	public boolean getPlayerState() {
		return lost;
	}
	
	/**
	 * This method set value of lost attribute.
	 */
	public void setPlayerState(boolean value) {
		this.lost = value;

	}
	
	/**
	 * {@inheritDoc}
	 */
	public String toString() {
		return name;
	}
	
	public boolean haveInfantryCard(){
		for (Card card: this.cards){
			if (card.getName().equals("Infantry")){
				return true;
			}
		}
		return false;
	}
	
	public boolean haveCavalryCard(){
		for (Card card: this.cards){
			if (card.getName().equals("Cavalry")){
				return true;
			}
		}
		return false;
	}
	
	public boolean haveArtilleryCard(){
		for (Card card: this.cards){
			if (card.getName().equals("Artillery")){
				return true;
			}
		}
		return false;
	}
	
	public boolean haveDistinctCards(){
		if (haveInfantryCard() && haveArtilleryCard() && haveCavalryCard()){
			return true;
		}
		else{
			return false;
		}
	}
	
	public boolean haveThreeSameTypeCards(){
		int cavilary = 0;
		int artillery = 0;
		int infantry = 0;
		for (Card card :this.cards){
			if (card.getName().equals("Artillery")){
				artillery++;
			}
			else if(card.getName().equals("Cavalry")){
				cavilary++;
			}
			else if (card.getName().equals("Infantry")){
				infantry++;
			}
		}
		if(cavilary == 3 || artillery == 3 || infantry == 3){
			return true;
		}
		else{
			return false;
		}
	}
	

	/**
	 * 
	 * @return list of cards player has.
	 */
	public ArrayList<Card> getCards(){
		return this.cards;
	}
	
}


