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
import com.thilian.se4x.robot.game.Group;
import com.thilian.se4x.robot.game.enums.FleetType;
import com.thilian.se4x.robot.game.enums.ShipType;

import java.util.Collection;
import java.util.EnumSet;

import static com.thilian.se4x.robot.game.enums.FleetType.EXPANSION_FLEET;
import static com.thilian.se4x.robot.game.enums.FleetType.REGULAR_FLEET;

public class FreeFullTransportBuyer extends AbstractFullTransportBuyer {
    public FreeFullTransportBuyer(Game game) {
        super(game);
    }

    @Override
    protected Collection<FleetType> getFleetTypes() {
        return EnumSet.of(EXPANSION_FLEET, REGULAR_FLEET);
    }

    @Override
    protected void doBuyGroups(Fleet fleet) {
        fleet.addFreeGroup(new Group(ShipType.TRANSPORT, 1));
        for(Group group : buyGroundUnits(fleet)){
            fleet.addFreeGroup(group);
        }
    }

}
