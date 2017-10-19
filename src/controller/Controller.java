package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import model.CountryNode;
import model.GameDriver;
import view.CardsView;
import view.ControlsView;
import view.DiceRollView;
import view.MainView;
import view.MapView;
import view.PlayerInfoView;
import view.SetUpDialog;
import view.mapeditor.MapFrame;

/**
 * MVC - Controller that controls the interaction between models and view.
 * @author Gunpreet
 * @author Gurpreet
 * @author Amitt
 *
 */
public class Controller 
{
	/**
	 * Stores instance of GameDriver class.
	 */
	private GameDriver driver;
	/**
	 * Stores object of CardsView class.
	 */
	private CardsView cardsGUI;
	/**
	 * Stores object of ControlsView class.
	 */
	private ControlsView controlsGUI;
	/**
	 * Stores object of DiceRollView class.
	 */
	private DiceRollView diceRollGUI;
	/**
	 * Stores object of MapView class.
	 */
	private MapView mapGUI;
	/**
	 * Stores object of PlayerInfoView class.
	 */
	private PlayerInfoView playerInfoGUI;
	/**
	 * Stores object of SetUpDialog class.
	 */
	private SetUpDialog setupBox;
	/**
	 * ActionListener to add listener to "Add Armies" button.
	 */
	private ActionListener addArmiesListner;
	/**
	 * ActionListener to add listener to "Edit Map" button.
	 */
	private ActionListener mapEditListener;
	/**
	 * ActionListener to add listener to "Play Game" button.
	 */
	private ActionListener playGameListener;
	/**
	 * Stores object of MapFrame class.
	 */
	private MapFrame mapFrame;
	
	/**
	 * Controller class constructor to initialize GameDriver and SetUpDialog class objects.
	 */
	public Controller(GameDriver newDriver)
	{
		this.driver = newDriver;
		setupBox = new SetUpDialog();
	}
	
	/**
	 * Gets the player name from the user (functionality in SetUpDialog class).
	 * @return a string array containing the names of players.
	 */
	public String[] getPlayerInfo(){
		return setupBox.getPlayerInfo();
	}
	
	/**
	 * Calls the placeArmyDialog function of SetUpDialog class.
	 * @return the country selected by the user to place army.
	 */
	public String placeArmyDialog(String[] countriesNamesNoArmy) 
	{
		return setupBox.placeArmyDialog(countriesNamesNoArmy);
	}
	
	/**
	 * Fetches the instance of GameDriver class.
	 * @return the GameDriver class instance.
	 */
	public GameDriver getGameDriver()
	{
		return this.driver;
	}
	
	/**
	 * Sets Action Listeners for reinforcement phase.
	 */
	public void setActionListner()
	{
		addArmiesListner = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {				
				CountryNode country = driver.getCountry(controlsGUI.getCountrySelected());
				int armies = controlsGUI.getArmiesValue();
				System.out.println(armies);
				System.out.println(country.getCountryName());
				country.addArmy(armies);
				System.out.println(country.getArmiesCount());
				//driver.getCurrentPlayer().removeArmies(armies);
				driver.getCurrentPlayer().removeArmies(armies);
				System.out.println(driver.getPlayerArmies());
				driver.continuePhase();
			}
		};
		//call reinforcement phase first
		controlsGUI.addArmiesButtonAction(this.addArmiesListner);
		
		controlsGUI.endPhaseAction(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				driver.changePhase();
			}
		});
	}
	
	/**
	 * Sets Action Listeners for fortification phase.
	 */
	public void setFortificationListeners(){
		controlsGUI.countrieslistAction(new ActionListener() {
			@Override
            public void actionPerformed(ActionEvent e) {
				String countrySelected = (String) controlsGUI.getCountrySelected();
				CountryNode countrySelect = GameDriver.getInstance().getCurrentPlayer().getCountry(countrySelected);
				if(countrySelect.getArmiesCount()>1){
					ArrayList<String> neighborList = new ArrayList<String>();
					for(String name: countrySelect.getNeighbourCountriesString()){
						neighborList.add(name);
					}
					controlsGUI.updateFortification(countrySelect.getArmiesCount(), neighborList.toArray(new String[neighborList.size()]));
				}
			}
		});
		
		controlsGUI.playButtonAction(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(controlsGUI.isNeighbourSelected()) {
					String countrySelected = (String) controlsGUI.getCountrySelected();
					int selectedArmies = controlsGUI.getArmiesValue();
					CountryNode countrySelect = GameDriver.getInstance().getCurrentPlayer().getCountry(countrySelected);
					String neighbourSelected = controlsGUI.getNeighborSelected();
					countrySelect.setArmies(countrySelect.getArmiesCount()-selectedArmies); 
					for(CountryNode j : countrySelect.getNeighbourCountries()){
						if(j.getCountryName() == neighbourSelected){
							j.setArmies(j.getArmiesCount() + selectedArmies);
						}
					}
				}
				driver.changePhase();
			}
		});
	}
	
	/**
	 * Calls chooseMapEditorOrPlayGame() function of the SetUpDialog class to display Edit Map and Play Game options.
	 */
	public void chooseMapEditorOrPlayGame(){
		this.setupBox.chooseMapEditorOrPlayGame();
	}

	/**
	 * Sets listener for Edit Map button.
	 */
	public void mapEditorListener() {
		mapEditListener =  new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				mapFrame = new MapFrame();
				String[] a =null;
				mapFrame.main(a);
				setupBox.chooseOptionFrame().dispose();
			}
		};
		this.setupBox.mapEditAction(mapEditListener);
	}
	
	/**
	 * Sets listener for Play Game button.
	 */
	public void playGameListener() {
		playGameListener =  new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e){
				init();
				setupBox.chooseOptionFrame().dispose();
			}
		};
		this.setupBox.playGameAction(playGameListener);
	}
	
	/**
	 * Initializes the game after Play Game button selection.
	 */
	private void init(){
		driver.createMapObject(setupBox.getMapInfo("map"));
		playerInfoGUI = new PlayerInfoView();
		String temp = setupBox.getMapInfo("bmp");
        if(temp!=null) 
        {
        	mapGUI = new MapView(temp);
        }
        else 
        {
        	mapGUI = new MapView();
        }
        diceRollGUI = new DiceRollView();
        cardsGUI = new CardsView();
        controlsGUI = new ControlsView();
        MainView.createInstance(playerInfoGUI, mapGUI, diceRollGUI, cardsGUI, controlsGUI);
        driver.setPlayerView(playerInfoGUI);
		driver.setMapView(mapGUI);
		driver.setControlsView(controlsGUI);
		driver.runGame();
	}
}
