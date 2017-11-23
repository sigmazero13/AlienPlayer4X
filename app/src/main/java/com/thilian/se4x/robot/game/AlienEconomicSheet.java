package com.thilian.se4x.robot.game;

import com.thilian.se4x.robot.game.enums.Difficulty;

public class AlienEconomicSheet {

	private static final int RESULT_FLEET = 0;
	private static final int RESULT_TECH = 1;
	private static final int RESULT_DEF = 2;

	private static int[][] resultTable = new int[][] {
		{ 99, 99, 99},
		{ 99,  3, 99},
		{  2,  4, 99},
		{  2,  5,  9},
		{  2,  6,  9},
		{  2,  6, 10},
		{  2,  7, 10},
		{  1,  6, 10},
		{  1,  6, 10},
		{  1,  6, 10},
		{  1,  7, 10},
		{  1,  7, 10},
		{  1,  7, 10},
		{  1,  7, 99},
		{  1,  7, 99},
		{  1,  8, 99},
		{  1,  8, 99},
		{  1,  9, 99},
		{  1,  9, 99},
		{  1, 10, 99},
		{  1, 10, 99},
	};

	private static int[] econRolls = new int[] {0, 1, 1, 2, 2, 2, 3, 3, 3, 3, 4, 4, 4, 4, 4, 5, 5, 5, 5, 5, 5};

	private static int[] fleetLaunch = new int[] {-99, -99, 10, 10, 5, 3, 4, 4, 4, 5, 5, 3, 3, 3, 10, 3, 10, 3, 10, 3, 10}; 
	
	private Difficulty difficulty;
	int fleetCP = 0;
	int techCP = 0;
	int defCP = 0;
	int[] extraEcon = new int[21];

	public AlienEconomicSheet(Difficulty difficulty) {
		this.difficulty = difficulty;
	}

	public void applyRoll(int turn, int result) {
		if(result >= resultTable[turn][RESULT_DEF])
			defCP  += 2 * difficulty.getCPPerEcon();
		else if (result >= resultTable[turn][RESULT_TECH])
			techCP += difficulty.getCPPerEcon();
		else if (result >= resultTable[turn][RESULT_FLEET])
			fleetCP  += difficulty.getCPPerEcon();
		else {
			for(int i = turn + 3; i < 21; i++)
				extraEcon[i] += 1;
		}
	}

	public int getExtraEcon(int turn) {
		return extraEcon[turn];
	}

	public int getEconRolls(int turn) {
		return econRolls[turn];
	}

	public int getFleetLaunch(int turn) {
		return fleetLaunch[turn];
	}

	public void spendFleetCP(int ammount) {
		fleetCP -= ammount;
	}

	public int getTechCP(){
		return techCP;
	}

}
