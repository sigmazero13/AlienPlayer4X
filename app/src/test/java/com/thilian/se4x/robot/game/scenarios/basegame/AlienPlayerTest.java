package com.thilian.se4x.robot.game.scenarios.basegame;

import static com.thilian.se4x.robot.game.enums.FleetType.RAIDER_FLEET;
import static com.thilian.se4x.robot.game.enums.ShipType.BATTLECRUISER;
import static com.thilian.se4x.robot.game.enums.ShipType.BATTLESHIP;
import static com.thilian.se4x.robot.game.enums.ShipType.CRUISER;
import static com.thilian.se4x.robot.game.enums.ShipType.DESTROYER;
import static com.thilian.se4x.robot.game.enums.ShipType.RAIDER;
import static com.thilian.se4x.robot.game.enums.ShipType.SCOUT;
import static com.thilian.se4x.robot.game.enums.Technology.ATTACK;
import static com.thilian.se4x.robot.game.enums.Technology.CLOAKING;
import static com.thilian.se4x.robot.game.enums.Technology.MOVE;
import static com.thilian.se4x.robot.game.enums.Technology.POINT_DEFENSE;
import static com.thilian.se4x.robot.game.enums.Technology.SHIP_SIZE;
import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;

import com.thilian.se4x.robot.game.Fleet;
import com.thilian.se4x.robot.game.Group;
import com.thilian.se4x.robot.game.enums.FleetType;
import com.thilian.se4x.robot.game.enums.Seeable;
import com.thilian.se4x.robot.game.enums.ShipType;
import com.thilian.se4x.robot.game.enums.Technology;

public class AlienPlayerTest extends BasegameFixture {

    private Fleet fleet;

    @Test
    public void launchRegularFleetThenBuildLargestFleet(){
        launchRegularFleet();
        assertRegularFirstCombat(3);
        assertGroups(new Group(BATTLESHIP, 1), new Group(DESTROYER, 2), new Group(SCOUT, 5));
        assertCPs(2, 0, 10);
    }

    @Test
    public void launchRegularFleetThenBuildBalancedWith2SC(){
        setLevel(POINT_DEFENSE, 1);
        game.addSeenThing(Seeable.FIGHTERS);
        launchRegularFleet();
        assertRegularFirstCombat(8);
        assertGroups(new Group(BATTLESHIP, 1), new Group(DESTROYER, 1), new Group(SCOUT, 2), new Group(BATTLECRUISER, 1), new Group(CRUISER, 1));
        assertCPs(2, 0, 10);
    }

    @Test
    public void launchRegularFleetThenBuildLargestShips(){
        launchRegularFleet();
        assertRegularFirstCombat(8);
        assertGroups(new Group(BATTLESHIP, 3), new Group(DESTROYER, 1));
        assertCPs(1, 0, 10);
    }

    @Test
    public void launchCarrierFleetThenBuildLargestFleet(){
        sheet.setFleetCP(65);
        sheet.setTechCP(45);
        setLevel(SHIP_SIZE, 2);
        setLevel(ATTACK, 1);
        setLevel(Technology.FIGHTERS, 1);
        game.setSeenLevel(POINT_DEFENSE, 0);
        mock2Fleet1Tech1DefRoll();
        roller.mockRoll(7); //fleet launch
        roller.mockRoll(7); //move tech
        fleet = ap.makeEconRoll(10);
        assertCPs(0, 50, 10);
        assertEquals(75, fleet.getFleetCP());
        assertEquals(FleetType.REGULAR_FLEET, fleet.getFleetType());
        assertRoller();
        
        roller.mockRoll(8); //Ship size
        roller.mockRoll(6); //Buy next fighter level
        roller.mockRoll(7,5); //Fighters (no attack & cloak)
        roller.mockRoll(3); //fleet composition
        ap.firstCombat(fleet);
        assertLevel(SHIP_SIZE, 2);
        assertLevel(ATTACK, 1);
        assertLevel(Technology.FIGHTERS, 3);
        assertRoller();
        assertGroups(new Group(ShipType.CARRIER, 2), new Group(ShipType.FIGHTER, 6), new Group(DESTROYER, 1), new Group(SCOUT, 2));
        assertCPs(0, 0, 10);
    }

    @Test
    public void launchCarrierFleetThenBuildBalancedFleet(){
        sheet.setFleetCP(65);
        sheet.setTechCP(20);
        setLevel(SHIP_SIZE, 2);
        setLevel(ATTACK, 1);
        setLevel(Technology.FIGHTERS, 2);
        game.setSeenLevel(POINT_DEFENSE, 1);
        mock2Fleet1Tech1DefRoll();
        roller.mockRoll(7); //fleet launch
        roller.mockRoll(7); //move tech
        fleet = ap.makeEconRoll(10);
        assertCPs(0, 25, 10);
        assertEquals(75, fleet.getFleetCP());
        assertEquals(FleetType.REGULAR_FLEET, fleet.getFleetType());
        assertRoller();
        
        roller.mockRoll(8); //Ship size
        roller.mockRoll(7, 5); //Fighters (no attack & cloak)
        roller.mockRoll(4); //Has seen PD, but buy only full cariers
        roller.mockRoll(6); //fleet composition
        ap.firstCombat(fleet);
        assertLevel(SHIP_SIZE, 2);
        assertLevel(ATTACK, 1);
        assertLevel(Technology.FIGHTERS, 3);
        assertRoller();
        assertGroups(new Group(ShipType.CARRIER, 2), new Group(ShipType.FIGHTER, 6), new Group(DESTROYER, 1), new Group(SCOUT, 2));
        assertCPs(0, 0, 10);
    }

    @Test
    public void launchRegularFleetThenCarrierWithLargestShips(){
        sheet.setFleetCP(65);
        sheet.setTechCP(20);
        setLevel(SHIP_SIZE, 2);
        setLevel(ATTACK, 1);
        setLevel(Technology.FIGHTERS, 0);
        game.setSeenLevel(POINT_DEFENSE, 0);
        mock2Fleet1Tech1DefRoll();
        roller.mockRoll(5); //fleet launch
        roller.mockRoll(7); //move tech
        fleet = ap.makeEconRoll(10);
        assertCPs(0, 25, 10);
        assertEquals(75, fleet.getFleetCP());
        assertEquals(FleetType.REGULAR_FLEET, fleet.getFleetType());
        assertRoller();
        
        roller.mockRoll(8); //Ship size
        roller.mockRoll(7,5); //Fighters (no attack & cloak)
        roller.mockRoll(8); //fleet composition
        ap.firstCombat(fleet);
        assertLevel(SHIP_SIZE, 2);
        assertLevel(ATTACK, 1);
        assertLevel(Technology.FIGHTERS, 1);
        assertRoller();
        assertGroups(new Group(ShipType.CARRIER, 2), new Group(ShipType.FIGHTER, 6), new Group(DESTROYER, 2));
        assertCPs(3, 0, 10);
    }

    @Test
    public void launchRegularBuildRaider(){
        sheet.setFleetCP(60);
        sheet.setTechCP(45);
        setLevel(SHIP_SIZE, 4);
        setLevel(ATTACK, 1);
        mock2Fleet1Tech1DefRoll();
        roller.mockRoll(3); //fleet launch
        roller.mockRoll(7); //move tech
        fleet = ap.makeEconRoll(10);
        assertCPs(0, 50, 10);
        assertEquals(70, fleet.getFleetCP());
        assertEquals(FleetType.REGULAR_FLEET, fleet.getFleetType());
        assertRoller();
        roller.mockRoll(5); //Ship size
        roller.mockRoll(10,6); //Cloak
        ap.firstCombat(fleet);
        assertLevel(SHIP_SIZE, 5);
        assertLevel(CLOAKING, 1);
        assertRoller();
        assertGroups(new Group(RAIDER, 5));
        assertEquals(RAIDER_FLEET, fleet.getFleetType());
        assertCPs(10, 0,10);
    }

    @Test
    public void launchRaiderBuyTechs(){
        sheet.setFleetCP(12);
        sheet.setTechCP(45);
        setLevel(SHIP_SIZE, 4);
        setLevel(ATTACK, 1);
        setLevel(CLOAKING, 1);
        mock2Fleet1Tech1DefRoll();
        roller.mockRoll(7); //fleet launch
        roller.mockRoll(4); //move tech
        fleet = ap.makeEconRoll(10);
        assertCPs(10, 30, 10);
        assertLevel(MOVE, 2);
        assertGroups(new Group(RAIDER, 1));
        assertEquals(RAIDER_FLEET, fleet.getFleetType());
        assertRoller();

        roller.mockRoll(6); //Ship size
        roller.mockRoll(6); //next cloak
        ap.firstCombat(fleet);
        assertLevel(CLOAKING, 2);
        assertCPs(10, 0, 10);
    }


    @Test
    public void buildHomeDefenseNoRaiderFleetNoMineSweep(){
        sheet.setFleetCP(70);
        sheet.setTechCP(50);
        sheet.setDefCP(30);
        setLevel(SHIP_SIZE, 4);
        setLevel(ATTACK, 2);
        roller.mockRoll(5); //Ship size
        roller.mockRoll(9,6); //Cloak
        roller.mockRoll(4); //balanced fleet
        roller.mockRoll(7); //bases, then mines
        List<Fleet> fleets = ap.buildHomeDefense();
        assertEquals(FleetType.REGULAR_FLEET, fleets.get(0).getFleetType());
        assertGroups(fleets.get(0), new Group(BATTLESHIP, 1), new Group(DESTROYER, 1), new Group(BATTLECRUISER, 1), new Group(CRUISER, 2));
        assertEquals(FleetType.DEFENSE_FLEET, fleets.get(1).getFleetType());
        assertGroups(fleets.get(1), new Group(ShipType.BASE, 2), new Group(ShipType.MINE, 1));
        assertRoller();
        assertCPs(2, 0, 1);
        assertLevel(SHIP_SIZE, 5);
        assertLevel(CLOAKING, 1);
    }

    
    private void launchRegularFleet() {
        sheet.setFleetCP(60);
        sheet.setTechCP(45);
        setLevel(SHIP_SIZE, 4);
        setLevel(ATTACK, 1);
        mock2Fleet1Tech1DefRoll();
        roller.mockRoll(3); //fleet launch
        roller.mockRoll(7); //move tech
        fleet = ap.makeEconRoll(10);
        assertCPs(0, 50, 10);
        assertEquals(70, fleet.getFleetCP());
        assertEquals(FleetType.REGULAR_FLEET, fleet.getFleetType());
        assertRoller();
    }

    private void mock2Fleet1Tech1DefRoll() {
        roller.mockRoll(3);
        roller.mockRoll(6);
        roller.mockRoll(8);
        roller.mockRoll(10);
    }

    private void assertRegularFirstCombat(int fleetCompositionRoll) {
        roller.mockRoll(5); //Ship size
        roller.mockRoll(10,1); //Attack
        roller.mockRoll(fleetCompositionRoll); //fleet composition
        ap.firstCombat(fleet);
        assertLevel(SHIP_SIZE, 5);
        assertLevel(ATTACK, 2);
        assertRoller();
    }

    private void assertGroups(Group... expectedGroups) {
        assertGroups(fleet, expectedGroups);
    }
}
