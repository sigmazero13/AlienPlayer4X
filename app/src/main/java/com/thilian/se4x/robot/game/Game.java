package com.thilian.se4x.robot.game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.thilian.se4x.robot.game.basegame.BaseGameTechnologyBuyer;
import com.thilian.se4x.robot.game.basegame.BaseGameTechnologyPrices;
import com.thilian.se4x.robot.game.enums.Difficulty;
import com.thilian.se4x.robot.game.enums.PlayerColor;
import com.thilian.se4x.robot.game.enums.Seeable;
import com.thilian.se4x.robot.game.enums.Technology;

public class Game {
    public DiceRoller roller;
    public Scenario scenario;
    public List<AlienPlayer> aliens;
    private Map<Technology, Integer> seenLevels;
    private Set<Seeable> seenThings;
    public int currentTurn;

    public void createGame(Difficulty difficulty, Scenario scenario) {
        scenario.init(this);
        this.scenario = scenario;
        this.aliens = new ArrayList<>();
        for (int i = 0; i < difficulty.getNumberOfAlienPlayers(); i++) {
            aliens.add(new AlienPlayer(new AlienEconomicSheet(difficulty), this, PlayerColor.values()[i]));
        }

        resetSeenLevels();

        currentTurn = 1;
    }

    public void resetSeenLevels() {
        seenThings = new HashSet<>();
        seenLevels = new HashMap<>();
        for (Technology technology : scenario.getAvailableTechs()) {
            int startingLevel = scenario.getStartingLevel(technology);
            seenLevels.put(technology, startingLevel);
        }
    }

    public List<Fleet> doEconomicPhase() {
        List<Fleet> newFleets = new ArrayList<>();
        for (AlienPlayer ap : aliens) {
            Fleet fleet = ap.makeEconRoll(currentTurn);
            if (fleet != null)
                newFleets.add(fleet);
        }
        currentTurn++;
        return newFleets;
    }

    public Integer getSeenLevel(Technology technology) {
        return seenLevels.get(technology);
    }

    public void setSeenLevel(Technology technology, Integer level) {
        seenLevels.put(technology, level);
    }

    public void addSeenThing(Seeable seeable) {
        seenThings.add(seeable);
    }

    public boolean isSeenThing(Seeable seeable) {
        return seenThings.contains(seeable);
    }
}