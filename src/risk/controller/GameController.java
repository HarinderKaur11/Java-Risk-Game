package risk.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import risk.model.gamemode.GameDriver;
import risk.model.map.CountryNode;
import risk.model.player.Player;
import risk.model.util.GameLogger;
import risk.view.CardsView;
import risk.view.ControlsView;
import risk.view.MainView;
import risk.view.MapView;
import risk.view.PhaseView;
import risk.view.PlayerInfoView;
import risk.view.SetUpDialog;
import risk.view.WorldDominationView;


/**
 * MVC - Controller that controls the interaction between models and view.
 * @author Gunpreet
 * @author Gurpreet
 * @author Amitt
 */
public class GameController{
	
	/**
	 * driver variable storing the reference of the class GameDriver.
	 */
	private GameDriver driver;

	/**
	 * phaseView variable storing the reference of the class PhaseView.
	 */
	private PhaseView phaseView;
	
	/**
	 * dominationView variable storing the reference of the class WorldDominationView.
	 */
	private WorldDominationView dominationView;
	
	/**
	 * cardsGUI variable storing the reference of the class CardsView.
	 */
	private CardsView cardsGUI;
	
	/**
	 * controlsGUI variable storing the reference of the class ControlsView.
	 */
	private ControlsView controlsGUI;
	
	/**
	 * mapGUI variable storing the reference of the class MapView.
	 */
	private MapView mapGUI;
	
	/**
	 * playerInfoGUI variable storing the reference of the class PlayerInfoView.
	 */
	private PlayerInfoView playerInfoGUI;
	
	/**
	 * setupBox variable storing the reference of the class SetUpDialog.
	 */
	private SetUpDialog setupBox;
	
	/**
	 * ActionListener to add listener to "Add Armies" button.
	 */
	private ActionListener addArmiesListner;
	
	/**
	 * ActionListener to add listener to "Save Game" button.
	 */
	private ActionListener saveGameListener;
	
	/**
	 * gameLogger variable storing the instance of the class GameLogger.
	 */
	private GameLogger gameLogger;
	
	/**
	 * Constructor for object creation
	 * @param newSetupBox SetUpDialog object
	 */
	public GameController(SetUpDialog newSetupBox){
		this(new GameDriver(), newSetupBox);
	}
	
	/**
	 * Controller class constructor to initialize GameDriver and SetUpDialog class objects.
	 * @param newDriver GameDriver instance.
	 * @param newSetupBox SetUpDialog object
	 */
	public GameController(GameDriver newDriver, SetUpDialog newSetupBox) {
		this.setupBox = newSetupBox;
		this.driver = newDriver;
		driver.setController(this);
		init();
	}
	
	/**
	 * Controller class constructor to initialize GameDriver and SetUpDialog class objects.
	 * @param newMap Map file path.
	 * @param newMapImage Map file image path.
	 * @param moveLimit number of turns
	 * @param playerNames names of the players.
	 */
	public GameController(String newMap, String newMapImage, String[][] playerNames, int moveLimit) {
		mapGUI = new MapView(newMapImage);
		setupBox = new SetUpDialog();
		driver = new GameDriver(newMap, moveLimit);
		driver.setController(this);
		playerInfoGUI = new PlayerInfoView();
		playerInfoGUI.setPlayerInfo(playerNames);
		init();
		driver.runGame(playerNames);
	}
	
	/**
	 * Controller class constructor to initialize GameDriver and SetUpDialog class objects.
	 * @param newMap Map file path.
	 * @param moveLimit number of turns
	 * @param playerNames names of the players.
	 */
	public GameController(String newMap, String[][] playerNames, int moveLimit) {
		mapGUI = new MapView();
		setupBox = new SetUpDialog();
		driver = new GameDriver(newMap, moveLimit);
		driver.setController(this);
		playerInfoGUI = new PlayerInfoView();
		playerInfoGUI.setPlayerInfo(playerNames);
		init();
		driver.runGame(playerNames);
	}
	
	/**
	 * Controller constructor to load the saved game state.
	 * @param newMap map file path
	 * @param currentPlayer current player of the game
	 * @param armyCountList army count for each country
	 * @param countryList country list of each player
	 * @param players game players
	 * @param phaseName name of the phase ongoing.
	 */
	public GameController(String newMap, String[][] players, ArrayList<ArrayList<String>> countryList, ArrayList<ArrayList<Integer>> armyCountList, String currentPlayer, String phaseName){
		mapGUI = new MapView();
		setupBox = new SetUpDialog();
		driver = new GameDriver(newMap, 0);
		driver.setController(this);
		playerInfoGUI = new PlayerInfoView();
		int i = 0;
		
		for (ArrayList<String> countrylist: countryList){
			int j=0;
			ArrayList<CountryNode> list = new ArrayList<CountryNode>();
			for(String country: countrylist){
				CountryNode cn = driver.getMap().getCountry(country);
				cn.setArmies(armyCountList.get(i).get(j));
				list.add(cn);
				j++;
			}
			Player player = new Player(players[i][0], 0, list, driver);
			player.setStrategy(driver.createBehavior(players[i][1]));
			if(player.getName().equals(currentPlayer)){
				driver.setCurrentPlayer(player);
			}
			driver.setPlayerList(player);
			i++;
		}

		playerInfoGUI.setPlayerInfo(players);
		init();
		driver.getTurnManager().setPhase(phaseName);
		if(phaseName.trim().equals("Reinforcement")){
			driver.getCurrentPlayer().assignArmies(driver.getCurrentPlayer().getArmies());
		}
		driver.continuePhase();
	}
	
	/**
	 * Initializes the game after Play Game button selection.
	 */
	public void init() {
		/*Initialize all the views for the main window and run game.*/
        cardsGUI = new CardsView();
        controlsGUI = new ControlsView();
        phaseView = new PhaseView();
        dominationView = new WorldDominationView();
        gameLogger = new GameLogger();
        MainView.createInstance(playerInfoGUI, mapGUI, controlsGUI, phaseView, dominationView);
		driver.addObserver(phaseView);
		driver.addObserver(dominationView);
		driver.addObserver(cardsGUI);
		driver.addObserver(gameLogger);
		driver.getMap().addObserver(mapGUI);
		this.setSaveGameListener();
	}
	
	/**
	 * Calls the placeArmyDialog function of SetUpDialog class.
	 * @param countriesNamesNoArmy list of countries with no armies. 
	 * @param message message explaining the purpose of input.
	 * @return the country selected by the user to place army.
	 */
	public String placeArmyDialog(String[] countriesNamesNoArmy, String message) {
		return setupBox.placeArmyDialog(countriesNamesNoArmy, message);
	}
	
	/**
	 * Sets Action Listeners for reinforcement controls.
	 */
	public void setActionListner() {
		addArmiesListner = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int armies = controlsGUI.getArmiesValue();
				driver.shiftArmiesOnReinforcement(controlsGUI.getCountrySelected(), armies);
			}
		};
		controlsGUI.addArmiesButtonAction(this.addArmiesListner);
	}
	
	/**
	 * Sets Action Listeners for fortification controls.
	 */
	public void setFortificationListeners() {
		controlsGUI.countrieslistAction(new ActionListener() {
			@Override
            public void actionPerformed(ActionEvent e) {
				String countrySelected = (String) controlsGUI.getCountrySelected();
				driver.fortificationNeighbourListUpdate(countrySelected);
			}
		});
		
		controlsGUI.playButtonAction(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(controlsGUI.isNeighbourSelected()) {
					driver.getArmiesShiftedAfterFortification(controlsGUI.getCountrySelected(), 
							controlsGUI.getNeighborSelected(), controlsGUI.getArmiesValue());
				}
				driver.changePhase();
			}
		});
	}
	
	/**
	 * This method updates the neighbor list combobox in controlsview
	 * @param newArmies number of armies user can move
	 * @param newNeighbourList list of neighbor counties
	 */
	public void updateControlsFortification(int newArmies, String[] newNeighbourList) {
		controlsGUI.updateFortification(newArmies, newNeighbourList);
	}
	
	/**
	 * Method set the listeners to components for attack phase in controls view
	 */
	public void setAttackListeners() {
		controlsGUI.countrieslistAction(new ActionListener() {
			@Override
            public void actionPerformed(ActionEvent e) {
				String countrySelected = (String) controlsGUI.getCountrySelected();
				driver.attackNeighbourListUpdate(countrySelected);
			}
		});
		
		controlsGUI.playButtonAction(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(controlsGUI.isNeighbourSelected()) {
					driver.announceAttack(controlsGUI.getCountrySelected(),controlsGUI.getNeighborSelected());
				}
			}
		});
		
		controlsGUI.endPhaseAction(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				driver.changePhase();
			}
		});
	}
	
	/**
	 * Update list of neighbors for combobox in controls view
	 * @param neighbourList list of neighbor countries
	 */
	public void updateNeighborList(String[] neighbourList) {
		controlsGUI.setNeighborList(neighbourList);
	}
	
	/**
	 * delegate method to call getInput from SetUpDialog class. 
	 * @see SetUpDialog
	 * @param min minimum value user can select 
	 * @param max maximum value user can select
	 * @param message message explaining the purpose of input
	 * @return a number selected by user
	 */
	public int setUpBoxInput(int min, int max, String message) {
		return setupBox.getInput(min, max,message);
	}

	/**
	 * Removes all controls when Game is Over.
	 */
	public void removeAllControls() {
		controlsGUI.removeAll();
	}

	/**
	* set the reinforcement controls using number of armies and names of countries
	* @param countryList country list
	* @param armies Armies assigned on reinforcement
	*/
	public void setReinforcementControls(int armies, String[] countryList) {
		controlsGUI.reinforcementControls(armies, countryList);
	}

	/**
	* set attack controls using string array
	* @param array country list
	*/
	public void setAttackControls(String[] array) {
		controlsGUI.attackControls(array);
	}

	/**
	* set fortification controls using string array
	* @param array country list
	*/
	public void setFortificationControls(String[] array) {
		controlsGUI.fortificationControls(array);		
	}
	
	/**
	 * Sets Action Listeners for save game.
	 */
	public void setSaveGameListener() {
		saveGameListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				driver.saveGameDataToFile();
			}
		};
		controlsGUI.saveGameButtonAction(this.saveGameListener);
	}
	
}
