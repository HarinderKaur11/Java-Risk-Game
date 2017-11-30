package risk.model.player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;

import risk.model.gamemode.GameDriver;
import risk.model.map.CountryNode;

/**
 * Class for Benevolent player that implements the PlayerStrategy interface.
 */
public class BenevolentStrategy implements PlayerStrategy {
	
	/**
	 * GameDriver instance for benevolent player.
	 */
	GameDriver driver = new GameDriver();
	
	/**
	 * Stores the current player.
	 */
	Player player = driver.getCurrentPlayer();
	
	/**
	 * Stores the list of country nodes of the player.
	 */
	ArrayList<CountryNode> countries = player.getCountries();
	
	/**
	 * Reinforcement phase of benevolent player that reinforces its weakest countries.
	 * @see risk.model.player.PlayerStrategy#reinforcementPhase(int, java.lang.String[])
	 */
	@Override
	public void reinforcementPhase(int armies, String[] countryList) {
		
		//sort countries according to armies count.
		Collections.sort(countries, new Comparator<CountryNode>(){

			@Override
			public int compare(CountryNode o1, CountryNode o2) {
				return o1.getArmiesCount() - o2.getArmiesCount();
			}
		}); 
		
		//get the list of weak countries.
		int countOfWeakCountries = 1;
		ArrayList<CountryNode> weakCountryList = new ArrayList<CountryNode>();
		weakCountryList.add(countries.get(0));
		for(int i= 1; i < countries.size(); i++){
			if(countries.get(i).getArmiesCount() == countries.get(i-1).getArmiesCount()){
				weakCountryList.add(countries.get(i));
				countOfWeakCountries++;
			}
			else{
				break;
			}
		}
		
		//get the integer round-off of the armies to be alloted to each weak country.
		int armiesToBeReinforced = (int)(player.getArmiesCount()/countOfWeakCountries);
		for( CountryNode country: weakCountryList){
			country.addArmy(armiesToBeReinforced);
			player.removeArmies(armiesToBeReinforced);
		}
		
		//Move the armies remaining into the first weakest country in the list.
		int playerArmiesLeft = player.getArmiesCount();
				
		if(!(playerArmiesLeft == 0)){
			weakCountryList.get(0).addArmy(playerArmiesLeft);
			player.removeArmies(playerArmiesLeft);
		}
		
		driver.changePhase();
	}

	/**
	 * Attack phase: benevolent player never attacks.
	 * @see risk.model.player.PlayerStrategy#attackPhase(java.util.ArrayList)
	 */
	@Override
	public void attackPhase(ArrayList<String> countryList) {
		//skip attack phase.
		driver.changePhase();
	}
	
	/**
	 * Fortification phase of benevolent player: fortifies in order to move armies to weakest country.
	 * @see risk.model.player.PlayerStrategy#fortificationPhase(java.util.ArrayList)
	 */
	@Override
	public void fortificationPhase(ArrayList<String> countryList) {
		//fortify the weakest country.
		CountryNode weakest = countries.get(0);
		CountryNode strongest = countries.get(countries.size()-1);
		int average = (int)(weakest.getArmiesCount() + strongest.getArmiesCount()) / 2;
		weakest.addArmy(average);
		strongest.removeArmies(average);
		driver.changePhase();
	}

	@Override
	public String placeArmy(String[] strings, String string) {
		return strings[new Random().nextInt(strings.length)];
	}

}