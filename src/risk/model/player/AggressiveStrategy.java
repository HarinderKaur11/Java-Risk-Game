package risk.model.player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;

import risk.model.gamemode.GameDriver;
import risk.model.map.CountryNode;

public class AggressiveStrategy implements PlayerStrategy {

	GameDriver driver = new GameDriver();
	Player player = driver.getCurrentPlayer();
	ArrayList<CountryNode> countries = player.getCountries();
	CountryNode strongest;
	
	@Override
	public void reinforcementPhase(int armies, String[] countryList) {
		//sort countries according to armies count in descending order.
		Collections.sort(countries, new Comparator<CountryNode>(){

			@Override
			public int compare(CountryNode o1, CountryNode o2) {
				return o2.getArmiesCount() - o1.getArmiesCount();
			}
		}); 
		
		strongest = countries.get(0);
		
		//get the list of strong countries.
		int countOfStrongCountries = 1;
		ArrayList<CountryNode> strongCountryList = new ArrayList<CountryNode>();
		strongCountryList.add(countries.get(0));
		for(int i= 1; i < countries.size(); i++){
			if(countries.get(i).getArmiesCount() == countries.get(i-1).getArmiesCount()){
				strongCountryList.add(countries.get(i));
				countOfStrongCountries++;
			}
			else{
				break;
			}
		}
		
		//get the integer round-off of the armies to be alloted to each strong country.
		int armiesToBeReinforced = (int)(player.getArmiesCount()/countOfStrongCountries);
		for( CountryNode country: strongCountryList){
			country.addArmy(armiesToBeReinforced);
			player.removeArmies(armiesToBeReinforced);
		}
		
		//Move the armies remaining into the first strong country in the list.
		int playerArmiesLeft = player.getArmiesCount();
				
		if(!(playerArmiesLeft == 0)){
			strongCountryList.get(0).addArmy(playerArmiesLeft);
			player.removeArmies(playerArmiesLeft);
		}
		
		driver.changePhase();

	}

	@Override
	public void attackPhase(ArrayList<String> countryList) {
		//to be implemented
		
		/*
		 if(strongest.getArmiesCount() > 1){
			CountryNode toBeAttacked = strongest.getNeighbours().get(new Random().nextInt(strongest.getNeighbours().size()));
			
		}
		else{
			driver.changePhase();
		}
		*/
	}

	@Override
	public void fortificationPhase(ArrayList<String> countryList) {
		//fortify the strongest country.
		CountryNode weakest = countries.get(countries.size()-1);
		int average = (int)(weakest.getArmiesCount() + strongest.getArmiesCount()) / 2;
		strongest.addArmy(average);
		weakest.removeArmies(average);
		driver.changePhase();

	}

	@Override
	public String placeArmy(String[] strings, String string) {
		// TODO Auto-generated method stub
		return null;
	}

}
