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

package com.thilian.se4x.robot.game.buyers.fleet.optional;

import com.thilian.se4x.robot.game.Fleet;
import com.thilian.se4x.robot.game.Game;
import com.thilian.se4x.robot.game.buyers.OptionalGroupBuyer;
import com.thilian.se4x.robot.game.enums.FleetBuildOption;
import com.thilian.se4x.robot.game.enums.Seeable;
import com.thilian.se4x.robot.game.enums.ShipType;

public class ScoutBuyer extends OptionalGroupBuyer {
    public ScoutBuyer(Game game) {
        super(game);
    }

    @Override
    protected boolean shouldBuyGroups(Fleet fleet, FleetBuildOption... options) {
        return game.isSeenThing(Seeable.MINES);
    }

    @Override
    protected void doBuyGroups(Fleet fleet) {
        buyGroup(fleet, ShipType.SCOUT, 2);
    }
}
