package com.thilian.se4x.robot.game;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.thilian.se4x.robot.game.enums.FleetType;
import com.thilian.se4x.robot.game.enums.Technology;

public class OptionalTechPurchaseTest  extends TechnologyPurchaseBase{
	@Test
	public void buyOptionalPontDefense() {
		assertDontBuyPD(1);
		ap.setSeenLevel(Technology.FIGHTERS, 1);
		assertBuyPD(1);
		assertDontBuyPD(2);
	}
	
	private void assertBuyPD(int expectedLevel) {
		assertBuyOptional(expectedLevel, Technology.POINT_DEFENSE, new BuyAction(){
			@Override
			void buy(AlienPlayer ap) {
				game.techBuyer.buyPointDefenseIfNeeded(ap);
			}
		});
	}

	private void assertDontBuyPD(int expectedLevel) {
		assertDontBuyOptional(expectedLevel, Technology.POINT_DEFENSE, new BuyAction(){
			@Override
			void buy(AlienPlayer ap) {
				game.techBuyer.buyPointDefenseIfNeeded(ap);
			}
		});
	}

	@Test
	public void buyOptionalMineSweep() {
		assertDontBuyMS(1);
		ap.setSeenLevel(Technology.MINES, 1);
		assertBuyMS(1);
		assertDontBuyMS(2);
	}
	
	private void assertBuyMS(int expectedLevel) {
		assertBuyOptional(expectedLevel, Technology.MINE_SWEEP, new BuyAction(){
			@Override
			void buy(AlienPlayer ap) {
				game.techBuyer.buyMineSweepIfNeeded(ap);
			}
		});
	}
	
	private void assertDontBuyMS(int expectedLevel) {
		assertDontBuyOptional(expectedLevel, Technology.MINE_SWEEP,  new BuyAction(){
			@Override
			void buy(AlienPlayer ap) {
				game.techBuyer.buyMineSweepIfNeeded(ap);
			}
		});
	}

	@Test
	public void buyOptionalScan() {
		ap.setSeenLevel(Technology.CLOAKING, 1);
		roller.mockRoll(4);
		assertBuyScanner(1);
		assertDontBuyScanner(2);
		
		ap.setSeenLevel(Technology.CLOAKING, 2);
		roller.mockRoll(5);
		assertDontBuyScanner(2);
		
		roller.mockRoll(4);
		assertBuyScanner(2);
		
		ap.setSeenLevel(Technology.CLOAKING, 2);
		ap.setLevel(Technology.SCANNER, 0);
		roller.mockRoll(4);
		assertOptionalBuy(
				Technology.SCANNER, 
				2, 
				100 - Technology.SCANNER.getCost(1) - Technology.SCANNER.getCost(2),
				new BuyAction(){
					@Override
					void buy(AlienPlayer ap) {
						game.techBuyer.buyScannerIfNeeded(ap);
					}
				});
	}

	private void assertBuyScanner(int expectedLevel) {
		assertBuyOptional(expectedLevel, Technology.SCANNER, new BuyAction(){
			@Override
			void buy(AlienPlayer ap) {
				game.techBuyer.buyScannerIfNeeded(ap);
			}
		});
	}
	
	private void assertDontBuyScanner(int expectedLevel) {
		assertDontBuyOptional(expectedLevel, Technology.SCANNER, new BuyAction(){
			@Override
			void buy(AlienPlayer ap) {
				game.techBuyer.buyScannerIfNeeded(ap);
			}
		});
	}

	@Test
	public void buyOptionalShipSize() {
		roller.mockRoll(10);
		assertBuyShipSize(2);

		assertBuyShipSize(3, 7);
		assertBuyShipSize(4, 6);
		assertBuyShipSize(5, 5);
		assertBuyShipSize(6, 3);
		
		assertDontBuyShipSize(7);
	}

	private void assertBuyShipSize(int newLevel, int rollNeeded) {
		roller.mockRoll(rollNeeded + 1);
		assertDontBuyShipSize(newLevel);
		roller.mockRoll(rollNeeded);
		assertBuyShipSize(newLevel);
	}

	private void assertBuyShipSize(int expectedLevel) {
		assertBuyOptional(expectedLevel, Technology.SHIP_SIZE, new BuyAction(){
			@Override
			void buy(AlienPlayer ap) {
				game.techBuyer.buyShipSizeIfRolled(ap);
			}
		});
	}
	
	private void assertDontBuyShipSize(int expectedLevel) {
		assertDontBuyOptional(expectedLevel, Technology.SHIP_SIZE, new BuyAction(){
			@Override
			void buy(AlienPlayer ap) {
				game.techBuyer.buyShipSizeIfRolled(ap);
			}
		});
	}

	@Test
	public void buyOptionalFighterLevel() {
		ap.setSeenLevel(Technology.POINT_DEFENSE, 0);
		assertDontBuyFighters(1);

		ap.setLevel(Technology.FIGHTERS, 1);
		roller.mockRoll(6);
		assertBuyFighters(2);
		
		ap.setSeenLevel(Technology.POINT_DEFENSE, 1);
		assertDontBuyFighters(3);
		
		ap.setSeenLevel(Technology.POINT_DEFENSE, 0);
		roller.mockRoll(7);
		assertDontBuyFighters(3);

		roller.mockRoll(6);
		assertBuyFighters(3);
	}

	private void assertBuyFighters(int expectedLevel) {
		assertBuyOptional(expectedLevel, Technology.FIGHTERS, new BuyAction(){
			@Override
			void buy(AlienPlayer ap) {
				game.techBuyer.buyFightersIfNeeded(ap);
			}
		});
	}
	
	private void assertDontBuyFighters(int expectedLevel) {
		assertDontBuyOptional(expectedLevel, Technology.FIGHTERS, new BuyAction(){
			@Override
			void buy(AlienPlayer ap) {
				game.techBuyer.buyFightersIfNeeded(ap);
			}
		});
	}
	
	@Test
	public void buyOptionalCloak() {
		fleet.setFleetType(FleetType.RAIDER_FLEET);;
		assertDontBuyCloaking(1);
		
		ap.setLevel(Technology.CLOAKING, 1);
		roller.mockRoll(7);
		assertDontBuyCloaking(2);
		
		roller.mockRoll(6);
		assertBuyCloaking(2);

		ap.setLevel(Technology.CLOAKING, 1);
		fleet.setFleetType(FleetType.REGULAR_FLEET);
		assertDontBuyCloaking(2);
	}

	private void assertBuyCloaking(int expectedLevel) {
		sheet.techCP = 100;
		game.techBuyer.buyCloakingIfNeeded(fleet);
		assertLevel(Technology.CLOAKING, expectedLevel);
		assertEquals(100 - Technology.CLOAKING.getCost(expectedLevel), sheet.techCP);
	}
	
	private void assertDontBuyCloaking(int expectedLevel) {
		sheet.techCP = 100;
		game.techBuyer.buyCloakingIfNeeded(fleet);
		assertLevel(Technology.CLOAKING, expectedLevel - 1);
		assertEquals(100, sheet.techCP);
	}

	private void assertBuyOptional(int expectedLevel, Technology technology, BuyAction buyAction) {
		assertOptionalBuy(
				technology, 
				expectedLevel, 
				100 - technology.getCost(expectedLevel),
				buyAction);
	}

	private void assertDontBuyOptional(int expectedLevel, Technology technology, BuyAction buyAction) {
		assertOptionalBuy(
				technology, 
				expectedLevel - 1, 
				100 , 
				buyAction);
	}

	private void assertOptionalBuy(Technology technology, int newLevel, int remainingCP, 
			BuyAction buyAction) {
		sheet.techCP = 100;
		buyAction.buy(ap);
		assertLevel(technology, newLevel);
		assertEquals(remainingCP, sheet.techCP);
	}

	abstract class BuyAction {
		abstract void buy(AlienPlayer ap);
	}
}
