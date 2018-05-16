package malp.States;


import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;

import malp.DemonSkies.DemonSkiesGame;
import malp.DemonSkies.GameEngine;
import malp.DemonSkies.ResourceManager;


/**
 * The Class Play represents the playing state of the game.
 */
public class Play extends BasicGameState{
	
	/** A handle to the {@link GameEngine}. */
	private GameEngine engine;
	
	/** A handle to the {@link ResourceManager}. */
	private ResourceManager resourceManager;
	
	
	
	/** Initializes the game state and loads the appropriate objects and resources. This method is inherited.
	 * @see org.newdawn.slick.state.GameState#init(org.newdawn.slick.GameContainer, org.newdawn.slick.state.StateBasedGame)
	 */
	@Override
	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
		engine = GameEngine.getEngine();
		resourceManager = ResourceManager.getResourceManager();
	}
	
	
	/**Called by the game upon entering this game state. This method is inherited.
	 * @see org.newdawn.slick.state.BasicGameState#enter(org.newdawn.slick.GameContainer, org.newdawn.slick.state.StateBasedGame)
	 */
	public void enter(GameContainer gc, StateBasedGame sbg)
	{
		//starts playing music
		resourceManager.getGameMusic().loop();
		//resets the game engine
		engine.resetEngine();
		//hides the cursor
		gc.setMouseGrabbed(true);
	}
	
	/** Renders the game state onto a game window. This method is inherited.
	 * @see org.newdawn.slick.state.GameState#render(org.newdawn.slick.GameContainer, org.newdawn.slick.state.StateBasedGame, org.newdawn.slick.Graphics)
	 */
	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		g.setBackground(resourceManager.getColor());
		g.drawImage(resourceManager.getBackground(), 0, 0);
		engine.render(gc, g);
	}

	/**Updates the game state. This method is inherited.
	 * @see org.newdawn.slick.state.GameState#update(org.newdawn.slick.GameContainer, org.newdawn.slick.state.StateBasedGame, int)
	 */
	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
		if (!engine.update(gc, delta)) //checks if the player is still alive while simultaneously updating the game state
		{
			sbg.enterState(DemonSkiesGame.MAIN_MENU, new FadeOutTransition(), new FadeInTransition());
			return;
		}
	}

	/**Returns the ID of this state to differentiate between different game states. This method is inherited.
	 * @see org.newdawn.slick.state.BasicGameState#getID()
	 */
	@Override
	public int getID() {
		// 
		return DemonSkiesGame.PLAY; //return the state ID for the StateBasedGame class's game handler
	}

}
