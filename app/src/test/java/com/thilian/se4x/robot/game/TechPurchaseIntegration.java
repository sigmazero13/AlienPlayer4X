package com.thilian.se4x.robot.game;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

import com.thilian.se4x.robot.game.enums.FleetType;
import com.thilian.se4x.robot.game.enums.Technology;

public class TechPurchaseIntegration extends TechnologyPurchaseBase {

    @Override
    protected void buildFleet() {
        fleet = new Fleet(ap, FleetType.REGULAR_FLEET, -1);
    }

    @Test
    public void integration() {
        sheet.setTechCP(120);
        ap.setLevel(Technology.SHIP_SIZE, 3);
        game.setSeenLevel(Technology.MINES, 1);
        ap.setLevel(Technology.FIGHTERS, 1);
        ap.setLevel(Technology.ATTACK, 2);
        ap.setLevel(Technology.DEFENSE, 1);
        roller.mockRoll(10); // no ship shize
        roller.mockRoll(3); // buys fighter
        roller.mockRoll(5, 5, 6);
        ap.buyTechs(fleet);
        assertLevel(Technology.DEFENSE, 2);
        assertLevel(Technology.TACTICS, 1);
        assertLevel(Technology.CLOAKING, 1);
        assertEquals(10, sheet.getTechCP());
    }
}
