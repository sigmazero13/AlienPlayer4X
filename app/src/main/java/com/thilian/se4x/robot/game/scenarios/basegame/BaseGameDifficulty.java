/*
 * Copyright (C) 2018 Balázs Péter
 *
 * This file is part of Alien Player 4X.
 *
 * Alien Player 4X is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Alien Player 4X is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Alien Player 4X.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.thilian.se4x.robot.game.scenarios.basegame;

public enum BaseGameDifficulty implements com.thilian.se4x.robot.game.enums.Difficulty {
    EASY("EASY", 5, 2), NORMAL("NORMAL", 5, 3), HARD("HARD", 10, 2),
    HARDER("HARDER", 10, 3), REALLY_TOUGH("REALLY_TOUGH", 15, 2), GOOD_LUCK("GOOD_LUCK", 15, 3);

    private String name;
    private int cpPerEcon;
    private int numberOfAlienPlayers;

    private BaseGameDifficulty(String name, int cpPerEcon, int numberOfAlienPlayers) {
        this.name = name;
        this.cpPerEcon = cpPerEcon;
        this.numberOfAlienPlayers = numberOfAlienPlayers;
    }

    @Override
    public int getCPPerEcon() {
        return cpPerEcon;
    }

    @Override
    public int getNumberOfAlienPlayers() {
        return numberOfAlienPlayers;
    }

    @Override
    public String getName() {
        return name;
    }
}
