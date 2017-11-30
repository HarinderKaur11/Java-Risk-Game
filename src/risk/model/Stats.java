package risk.model;

import risk.model.gamemode.Mode;

public class Stats {
	
	/**
	 * reference to Mode object 
	 */
	private static Mode mode;
	
	/**
	 * 
	 */
	public Stats(Mode newMode) {
		mode = newMode;
	}
	
	/**
	 * notify mode (TournamentMode/SingleMode) class about winner of game
	 */
	public static void notifyGameResult(String winnerPlayer) {
		mode.updateResults(winnerPlayer);
	}
	
}
