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

package com.thilian.se4x.robot.game.scenarios.vpscenario;

import java.util.Arrays;
import java.util.List;

import com.thilian.se4x.robot.game.Game;
import com.thilian.se4x.robot.game.enums.Difficulty;
import com.thilian.se4x.robot.game.scenarios.scenario4.Scenario4TechnologyBuyer;
import com.thilian.se4x.robot.game.scenarios.scenario4.Scenario4TechnologyPrices;

public class Vp2pScenario extends VpSoloScenario {
    @Override
    public void init(Game game) {
        techBuyer = new Scenario4TechnologyBuyer(game);
        techPrices = new Scenario4TechnologyPrices();
        fleetBuilder = new VpFleetBuilder(game);
        defenseBuilder = new DefenseBuilder(game);
        fleetLauncher = new VpCoopFleetLauncher(game);
    }

    @Override
    public List<? extends Difficulty> getDifficulties() {
        return Arrays.asList(VpDifficulties.Vp2pDifficulty.values());
    }
}
