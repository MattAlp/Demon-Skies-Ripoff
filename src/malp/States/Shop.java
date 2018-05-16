package malp.States;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.util.FontUtils;

import malp.DemonSkies.DemonSkiesGame;
import malp.DemonSkies.GameEngine;
import malp.DemonSkies.ResourceManager;
import malp.DemonSkies.UpgradeType;
import malp.Gui.Button;

/**
 * The Class Shop represents the shop menu of the game, where one can upgrade their character.
 */
public class Shop extends BasicGameState{
	
	/** The back button. */
	private Button backButton;
	
	/** The haste upgrade button. */
	private Button hasteUpgrade;
	
	/** The velocity upgrade button. */
	private Button velocityUpgrade;
	
	/** The continuum upgrade button. */
	private Button continuumUpgrade;
	
	/** The reach upgrade button. */
	private Button reachUpgrade;
	
	/** A handle to the {@link ResourceManager}. */
	private ResourceManager resourceManager;
	
	/** A handle to the {@link GameEngine}.*/
	private GameEngine engine;
	
	/** The upgrade text to display in the middle of the screen upon hovering over a button. */
	private String upgradeText = "";
	
	/** Initializes the game state and loads the appropriate objects and resources. This method is inherited.
	 * @see org.newdawn.slick.state.GameState#init(org.newdawn.slick.GameContainer, org.newdawn.slick.state.StateBasedGame)
	 */
	@Override
	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
		resourceManager = ResourceManager.getResourceManager();
		engine = GameEngine.getEngine();
		backButton = new Button(gc, resourceManager.getBackButtonFocused(), resourceManager.getBackButtonUnfocused(), 0, 0);
		hasteUpgrade = new Button(gc, resourceManager.getHasteIcon().getScaledCopy(0.5f), resourceManager.getHasteIcon() , 0, 0);
		velocityUpgrade = new Button(gc, resourceManager.getVelocityIcon().getScaledCopy(0.5f), resourceManager.getVelocityIcon(), 0, 0);
		continuumUpgrade = new Button(gc, resourceManager.getContinuumIcon().getScaledCopy(0.5f), resourceManager.getContinuumIcon(), 0, 0);
		reachUpgrade = new Button(gc, resourceManager.getReachIcon().getScaledCopy(0.5f), resourceManager.getReachIcon(), 0, 0);
	}
	
	/** Renders the game state onto a game window. This method is inherited.
	 * @see org.newdawn.slick.state.GameState#render(org.newdawn.slick.GameContainer, org.newdawn.slick.state.StateBasedGame, org.newdawn.slick.Graphics)
	 */
	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException { //this method is intended for rendering
		g.setFont(gc.getDefaultFont()); //sets the font to the default font
		g.drawImage(resourceManager.getBackground(), 0, 0); //draws the background
		
		 //draws the currency counter
		FontUtils.drawCenter(gc.getDefaultFont(), "Souls: " + engine.getSouls(), gc.getWidth() / 2, 0, ("Souls: " + engine.getSouls()).length());
		//draws the selected upgrade cost, level, and description
		FontUtils.drawCenter(gc.getDefaultFont(), upgradeText, gc.getWidth() / 2, gc.getHeight() / 4, upgradeText.length());
		
		//renders the various buttons
		backButton.render(gc, g);
		hasteUpgrade.render(gc, g);
		velocityUpgrade.render(gc, g);
		continuumUpgrade.render(gc, g);
		reachUpgrade.render(gc, g);
	}

	/**Updates the game state. This method is inherited.
	 * @see org.newdawn.slick.state.GameState#update(org.newdawn.slick.GameContainer, org.newdawn.slick.state.StateBasedGame, int)
	 */
	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException { //this method is intended for updating the game state
		//sets the button locations on the window
		backButton.setLocation((gc.getWidth() - backButton.getWidth()) / 2, gc.getHeight() * 4 / 5);
		hasteUpgrade.setLocation(gc.getWidth() / 5 - hasteUpgrade.getWidth() / 2, gc.getHeight() / 2);
		velocityUpgrade.setLocation(gc.getWidth() * 2 / 5 - velocityUpgrade.getWidth() / 2, gc.getHeight() / 2);
		continuumUpgrade.setLocation(gc.getWidth() * 3 / 5 - continuumUpgrade.getWidth() / 2, gc.getHeight() / 2);
		reachUpgrade.setLocation(gc.getWidth() * 4 / 5 - reachUpgrade.getWidth() / 2, gc.getHeight() / 2);
		
		
		//checks which buttons are pressed to buy upgrades, return to previous menus, and change the display text
		if (backButton.isButtonPressed())
		{
			sbg.enterState(DemonSkiesGame.MAIN_MENU);
			return;
		}
		else if (hasteUpgrade.isMouseOver())
		{
			upgradeText = "Haste - Player Speed Up\nPrice: " + engine.getPrice(UpgradeType.HASTE) + "\nLevel: " + engine.getLevel(UpgradeType.HASTE) + "/5";
		}
		else if (velocityUpgrade.isMouseOver())
		{
			upgradeText = "Velocity - Arrow Speed Up\nPrice: " + engine.getPrice(UpgradeType.VELOCITY) + "\nLevel: " + engine.getLevel(UpgradeType.VELOCITY) + "/5";;
		}
		else if (continuumUpgrade.isMouseOver())
		{
			upgradeText = "Continuum - Arrow Reach Up\nPrice: " + engine.getPrice(UpgradeType.CONTINUUM) + "\nLevel: " + engine.getLevel(UpgradeType.CONTINUUM) + "/5";;
		}
		else if (reachUpgrade.isMouseOver())
		{
			upgradeText = "Reach -  Arrow Pull Up\nPrice: " + engine.getPrice(UpgradeType.REACH) + "\nLevel: " + engine.getLevel(UpgradeType.REACH) + "/5";;
		}
		else
		{
			upgradeText = "Choose an Upgrade";
		}
		
		//attempts to upgrade the selected ability
		if (hasteUpgrade.isButtonPressed())
		{
			engine.upgrade(UpgradeType.HASTE);
		}
		else if (velocityUpgrade.isButtonPressed())
		{
			engine.upgrade(UpgradeType.VELOCITY);
		}
		else if (continuumUpgrade.isButtonPressed())
		{
			engine.upgrade(UpgradeType.CONTINUUM);
		}
		else if (reachUpgrade.isButtonPressed())
		{
			engine.upgrade(UpgradeType.REACH);
		}
	}

	/**Returns the ID of this state to differentiate between different game states. This method is inherited.
	 * @see org.newdawn.slick.state.BasicGameState#getID()
	 */
	@Override
	public int getID() {
		return DemonSkiesGame.SHOP;
	}

}
