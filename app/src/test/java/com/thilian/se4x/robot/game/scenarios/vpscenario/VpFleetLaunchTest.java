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

import com.thilian.se4x.robot.game.Fleet;
import com.thilian.se4x.robot.game.enums.FleetType;

import org.junit.Test;

import static com.thilian.se4x.robot.game.enums.FleetType.EXPANSION_FLEET;
import static com.thilian.se4x.robot.game.enums.FleetType.EXTERMINATION_FLEET_GALACTIC_CAPITAL;
import static com.thilian.se4x.robot.game.enums.FleetType.EXTERMINATION_FLEET_HOME_WORLD;
import static com.thilian.se4x.robot.game.enums.FleetType.RAIDER_FLEET;
import static com.thilian.se4x.robot.game.enums.Technology.CLOAKING;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class VpFleetLaunchTest extends VpScenarioFixture{

    @Test
    public void noCpLaunchesExpansionFleetFromBank(){
        roller.mockRoll(1); //launch
        assertFleetLaunched(EXPANSION_FLEET, "1", 50);
        assertBank(50);

        roller.mockRoll(1); //launch
        assertFleetLaunched(EXPANSION_FLEET, "2", 50);
        assertBank(0);

        roller.mockRoll(1); //launch
        assertNull(fleetLauncher.rollFleetLaunch(ap, 2));
        assertBank(0);
    }

    private void assertBank(int expectedBank) {
        assertEquals(expectedBank, ((VpEconomicSheet) sheet).getBank());
    }

    @Test
    public void dontSpendMoreFromTheBankThanAvailable(){
        VpEconomicSheet vpSheet = (VpEconomicSheet)sheet;
        vpSheet.spendBank(40);
        assertEquals(60, vpSheet.getBank());
        roller.mockRoll(1); //launch
        assertFleetLaunched(EXPANSION_FLEET, "1", 50);
        assertBank(10);
        roller.mockRoll(1); //launch
        assertNull(fleetLauncher.rollFleetLaunch(ap, 2));
        assertBank(10);
    }

    @Test
    public void launchExpansionFleetIfRolled(){
        VpEconomicSheet vpSheet = (VpEconomicSheet)sheet;

        vpSheet.setFleetCP(10);
        roller.mockRoll(1); //launch
        roller.mockRoll(7);
        assertFleetLaunched(EXPANSION_FLEET, "1", 60);
        assertBank(50);

        vpSheet.setFleetCP(10);
        vpSheet.setBank(60);
        roller.mockRoll(1); //launch
        roller.mockRoll(5);
        assertFleetLaunched(8, EXPANSION_FLEET, "2", 60);
        assertBank(10);

        vpSheet.setFleetCP(10);
        vpSheet.setBank(60);
        roller.mockRoll(1); //launch
        roller.mockRoll(3);
        assertFleetLaunched(11, EXPANSION_FLEET, "3", 60);
        assertBank(10);
    }

    @Test
    public void launchExterminationFleet(){
        VpEconomicSheet vpSheet = (VpEconomicSheet)sheet;

        vpSheet.setBank(60);

        vpSheet.setFleetCP(10);
        roller.mockRoll(1); //launch
        roller.mockRoll(8);
        roller.mockRoll(2, 1);
        assertFleetLaunched(7, EXTERMINATION_FLEET_HOME_WORLD, "1", 10);
        assertBank(60);

        vpSheet.setFleetCP(10);
        roller.mockRoll(1); //launch
        roller.mockRoll(6);
        roller.mockRoll(2, 1);
        assertFleetLaunched(8, EXTERMINATION_FLEET_HOME_WORLD, "2", 10);
        assertBank(60);

        vpSheet.setFleetCP(10);
        roller.mockRoll(1); //launch
        roller.mockRoll(6);
        roller.mockRoll(2, 2);
        assertFleetLaunched(9, EXTERMINATION_FLEET_GALACTIC_CAPITAL, "3", 10);
        assertBank(60);


        vpSheet.setFleetCP(10);
        roller.mockRoll(1); //launch
        roller.mockRoll(6);
        roller.mockRoll(2, 2);
        assertFleetLaunched(10, EXTERMINATION_FLEET_GALACTIC_CAPITAL, "4", 10);
        assertBank(60);

        vpSheet.setFleetCP(10);
        roller.mockRoll(1); //launch
        roller.mockRoll(4);
        roller.mockRoll(2, 1);
        assertFleetLaunched(11, EXTERMINATION_FLEET_HOME_WORLD, "5", 10);
        assertBank(60);
    }

    @Test
    public void launchRaiderFleet(){
        VpEconomicSheet vpSheet = (VpEconomicSheet)sheet;

        vpSheet.setFleetCP(12);
        vpSheet.setBank(10);
        ap.setLevel(CLOAKING, 1);
        roller.mockRoll(1); //launch
        assertFleetLaunched(RAIDER_FLEET, "1", 12);
        assertBank(10);
    }

    private void assertFleetLaunched(FleetType fleetType, String name, int fleetCP) {
        assertFleetLaunched(2, fleetType, name, fleetCP);
    }

    private void assertFleetLaunched(int turn, FleetType fleetType, String name, int fleetCP) {
        Fleet fleet = fleetLauncher.rollFleetLaunch(ap, turn);
        assertEquals(fleetType, fleet.getFleetType());
        assertEquals(name, fleet.getName());
        assertEquals(fleetCP, fleet.getFleetCP());
    }
}
