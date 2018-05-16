package malp.States;

import java.awt.Color;
import java.awt.Font;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.util.ResourceLoader;

import malp.DemonSkies.DemonSkiesGame;
import malp.DemonSkies.ResourceManager;
import malp.Gui.Button;
import malp.Gui.ToggleButton;

// TODO: Auto-generated Javadoc
/**
 * The Class MainMenu represents the main menu of the game.
 */
public class MainMenu extends BasicGameState{
	
	/** The image representing the game logo . */
	private Image logo; 
	
	/** The image representing the shine effect. */
	private Image shine;
	
	/** The velocity and direction the logo rotates at. */
	private float velocityDirection = 0.03f;
	
	/** The start button. */
	private Button startButton; 
	
	/** The shop button. */
	private Button shopButton; 
	
	/** The music on/off button. */
	private ToggleButton musicButton;
	
	/** The sound on/off button. */
	private ToggleButton soundButton;
	
	/**  A handle to the {@link ResourceManager}. */
	private ResourceManager resourceManager;
	
	/** Initializes the game state and loads the appropriate objects and resources. This method is inherited.
	 * @see org.newdawn.slick.state.GameState#init(org.newdawn.slick.GameContainer, org.newdawn.slick.state.StateBasedGame)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
		try {
			Font awtFont = Font.createFont(Font.PLAIN, ResourceLoader.getResourceAsStream("res/SupertextBold.ttf")); //create an AWT Font from the packaged ttf file
			awtFont = awtFont.deriveFont(Font.PLAIN, 6 * DemonSkiesGame.getScale()); //resize it using the deriveFont method
			UnicodeFont ucFont = new UnicodeFont(awtFont); //build LWJGL/Slick2D font on top of the awtFont
			ucFont.getEffects().add(new ColorEffect(Color.white)); //white default color for font
			ucFont.addAsciiGlyphs(); //register all the Ascii glyphs (all the possible ascii characters)
			ucFont.loadGlyphs(); //load the glyphs into memory
			gc.setDefaultFont(ucFont); //register the font as the GameContainer's default font
		}
		catch (Exception e) {
			e.printStackTrace(); //report an error if this happens, the Engine will use the existing default font
		}	
		resourceManager = ResourceManager.getResourceManager();
		logo = resourceManager.getLogo();
		shine = resourceManager.getShine();
		musicButton = new ToggleButton(gc, resourceManager.getMusicOn(), resourceManager.getMusicOff(), 0, 0); // create a new toggle button using images from the resource manager
		soundButton = new ToggleButton(gc, resourceManager.getSoundOn(), resourceManager.getSoundOff(), 0, 0); // create a new toggle button using images from the resource manager
		startButton = new Button(gc, resourceManager.getStartButtonFocused(), resourceManager.getStartButtonUnfocused(), 0, 0); // create a new button using images from the resource manager
		shopButton = new Button(gc, resourceManager.getShopButtonFocused(), resourceManager.getShopButtonUnfocused(), 0, 0); // create a new button using images from the resource manager
		
	}
	
	/**Called by the game upon entering this game state. This method is inherited.
	 * @see org.newdawn.slick.state.BasicGameState#enter(org.newdawn.slick.GameContainer, org.newdawn.slick.state.StateBasedGame)
	 */
	@Override
	public void enter(GameContainer gc, StateBasedGame sbg)
	{
		gc.setMouseGrabbed(false); //ungrab the mouse cursor, make it appear again (after the play state grabs it)
		resourceManager.getMenuMusic().loop(); //loop the menu music
	}
	
	/** Renders the game state onto a game window. This method is inherited.
	 * @see org.newdawn.slick.state.GameState#render(org.newdawn.slick.GameContainer, org.newdawn.slick.state.StateBasedGame, org.newdawn.slick.Graphics)
	 */
	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		g.setBackground(resourceManager.getColor());
		g.drawImage(resourceManager.getBackground(), 0, 0);
		g.setFont(gc.getDefaultFont());
		shine.draw((gc.getWidth() / 2) - (shine.getWidth() / 2), startButton.getX() - (startButton.getHeight() + shopButton.getHeight() + shine.getHeight() / 2));
		logo.draw((gc.getWidth() / 2) - (logo.getWidth() / 2), startButton.getX() - (startButton.getHeight() + shopButton.getHeight() + logo.getHeight() / 2));
		musicButton.render(gc, g);
		soundButton.render(gc, g);
		startButton.render(gc, g);
		shopButton.render(gc, g);
		
	}

	/**Updates the game state. This method is inherited.
	 * @see org.newdawn.slick.state.GameState#update(org.newdawn.slick.GameContainer, org.newdawn.slick.state.StateBasedGame, int)
	 */
	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
		musicButton.setX(gc.getWidth() - musicButton.getWidth());
		musicButton.setY(0);
		
		soundButton.setX(gc.getWidth()  - soundButton.getWidth() - musicButton.getWidth());
		soundButton.setY(0);
		
		startButton.setX((gc.getWidth() / 2) - (startButton.getWidth() / 2));
		startButton.setY((gc.getHeight() / 2) - (startButton.getHeight() / 2));
		
		shopButton.setX((gc.getWidth() / 2) - (shopButton.getWidth() / 2));
		shopButton.setY((gc.getHeight() / 2) - (shopButton.getHeight() / 2) + startButton.getHeight() + DemonSkiesGame.getScale());
		
		if (delta > 100)
		{
			delta = 100; //make sure that the delta is never greater than 100, this is a main menu and large deltas accumulated by minimizing the window will throw off the game clock, causing problems down the line
		}
		shine.rotate(0.05f * delta);
		if (logo.getRotation() > 10 && velocityDirection > 0) //switch the direction the logo is going on
		{
			velocityDirection *= -1; //multiplying by -1 returns the negative of the current direction
		}
		if (logo.getRotation() < -10 && velocityDirection < 0) //same thing, other direction
		{
			velocityDirection *= -1; //same as above
		}
		logo.setRotation(logo.getRotation() + velocityDirection * delta);

		if (startButton.isButtonPressed()) //if the start button is pressed
		{
			sbg.enterState(DemonSkiesGame.PLAY); //switch to the play state
			return;
		}
		if (shopButton.isButtonPressed()) //if tutorial button is pressed
		{
			sbg.enterState(DemonSkiesGame.SHOP); //switch to the tutorial state
			return;
		}
		musicButton.tryToggle(); //toggle button logic, see malp.Gui.ToggleButton
		if (musicButton.isToggled()) //if toggled
		{
			gc.setMusicVolume(0); //volume off
		}
		else
		{
			gc.setMusicVolume(1); //volume on
		}
		soundButton.tryToggle(); //same as above
		if (soundButton.isToggled()) //if toggled
		{
			gc.setSoundVolume(0); //sound off
		}
		else
		{
			gc.setSoundVolume(1); //sound on
		}
		
	}

	/**Returns the ID of this state to differentiate between different game states. This method is inherited.
	 * @see org.newdawn.slick.state.BasicGameState#getID()
	 */
	@Override
	public int getID() {
		// 
		return DemonSkiesGame.MAIN_MENU; //return ID for StateBasedGame
	}

}
