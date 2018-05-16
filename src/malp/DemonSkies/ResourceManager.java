package malp.DemonSkies;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Color;
import org.newdawn.slick.Image;
import org.newdawn.slick.Music;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;


/**
 * The ResourceManager is responsible for loading in all resources used by other objects and then providing them to conserve memory.
 */
public class ResourceManager {
	
	/** The singular instance of the {@link #ResourceManager()} class, used to reinforce the singleton design pattern.*/
	private static final ResourceManager INSTANCE = new ResourceManager();
	
	/** The image representing the logo. */
	private Image LOGO;
	
	/** The image representing the background. */
	private Image BACKGROUND;
	
	/** The image representing the shine. */
	private Image SHINE;

	/** The image representing the sound on button. */
	private Image SOUND_ON;
	
	/** The image representing the sound off button. */
	private Image SOUND_OFF;
	
	/** The image representing the music on button. */
	private Image MUSIC_ON;
	
	/** The image representing the off button. */
	private Image MUSIC_OFF;
	
	/** The image representing the start button, in focus. */
	private Image START_BUTTON_FOCUS;// = new Image("res/StartFocus.png");
	
	/** The image representing the start button, unfocused. */
	private Image START_BUTTON_UNFOCUS;// = new Image("res/StartUnfocus.png");
	
	/** The image representing the shop button, in focus. */
	private Image SHOP_BUTTON_FOCUS;// = new Image("res/ShopFocus.png");
	
	/** The image representing the shop button, unfocused. */
	private Image SHOP_BUTTON_UNFOCUS;// = new Image("res/ShopUnfocus.png");
	
	/** The image representing the back button, in focus. */
	private Image BACK_BUTTON_FOCUS;// = new Image("res/ShopFocus.png");
	
	/** The image representing the back button, unfocused. */
	private Image BACK_BUTTON_UNFOCUS;// = new Image("res/ShopUnfocus.png");
	
	/** The image representing the haste upgrade. */
	private Image HASTE_UPGRADE;
	
	/** The image representing the velocity upgrade. */
	private Image VELOCITY_UPGRADE;
	
	/** The image representing the continuum upgrade. */
	private Image CONTINUUM_UPGRADE;
	
	/** The image representing the reach upgrade. */
	private Image REACH_UPGRADE;
	
	/** The animation represnting the player. */
	private Animation PLAYER;
	
	/** The animation represnting the skull. */
	private Animation SKULL;
	
	/** The animation represnting the arrow. */
	private Animation ARROW;
	
	/** The button sound. */
	private Sound BUTTON_SOUND;
	
	/** The player hit sound. */
	private Sound PLAYER_HIT_SOUND;
	
	/** The enemy hit sound. */
	private Sound ENEMY_HIT_SOUND;
	
	/** The menu music. */
	private Music MENU_MUSIC;
	
	/** The game music. */
	private Music GAME_MUSIC;
	
	/** The background color. */
	private Color backgroundColor;

	
	/**
	 * Instantiates a new resource manager, loading all the resources from disk for later distribution to other objects.
	 */
	private ResourceManager()
	{
		try
		{
			//loads images, sets their scaling type to NEAREST to avoid anti-aliasing blur with larger monitors			
			LOGO = new Image("res/Logo.png");
			LOGO.setFilter(Image.FILTER_NEAREST);
			BACKGROUND = new Image("res/Bricks.png", Color.white);
			BACKGROUND.setFilter(Image.FILTER_NEAREST);
			SHINE = new Image("res/Shine.png");
			SHINE.setFilter(Image.FILTER_NEAREST);
			
			START_BUTTON_FOCUS = new Image("res/StartFocus.png");
			START_BUTTON_FOCUS.setFilter(Image.FILTER_NEAREST);
			
			START_BUTTON_UNFOCUS = new Image("res/StartUnfocus.png");
			START_BUTTON_UNFOCUS.setFilter(Image.FILTER_NEAREST);
			
			SHOP_BUTTON_FOCUS = new Image("res/ShopFocus.png");
			SHOP_BUTTON_FOCUS.setFilter(Image.FILTER_NEAREST);
			
			SHOP_BUTTON_UNFOCUS = new Image("res/ShopUnfocus.png");
			SHOP_BUTTON_UNFOCUS.setFilter(Image.FILTER_NEAREST);
			
			BACK_BUTTON_FOCUS = new Image("res/BackFocus.png");
			BACK_BUTTON_FOCUS.setFilter(Image.FILTER_NEAREST);
			
			BACK_BUTTON_UNFOCUS = new Image("res/BackUnfocus.png");
			BACK_BUTTON_UNFOCUS.setFilter(Image.FILTER_NEAREST);
			
			HASTE_UPGRADE = new Image("res/HasteUpgrade.png");
			HASTE_UPGRADE.setFilter(Image.FILTER_NEAREST);
			
			VELOCITY_UPGRADE = new Image("res/VelocityUpgrade.png");
			VELOCITY_UPGRADE.setFilter(Image.FILTER_NEAREST);
			
			CONTINUUM_UPGRADE = new Image("res/ContinuumUpgrade.png");
			CONTINUUM_UPGRADE.setFilter(Image.FILTER_NEAREST);
			
			REACH_UPGRADE = new Image("res/ReachUpgrade.png");
			REACH_UPGRADE.setFilter(Image.FILTER_NEAREST);
			
			//loads the images from a string array manually into an animation
			Image playerImage = new Image("res/Player1.png");
			playerImage.setFilter(Image.FILTER_NEAREST);
			PLAYER = new Animation(new Image[] {playerImage.getScaledCopy((float) (DemonSkiesGame.getScale() / 4.0))}, 1);
			
			
			//repeats the same process, but through a custom method called generateAnimation
			SKULL = generateAnimation(new String[] {"res/Skull1.png", "res/Skull2.png"}, true);
			ARROW = generateAnimation(new String[] {"res/Arrow1.png"}, true);			

			MUSIC_ON = new Image("res/Music.png");
			MUSIC_ON.setFilter(Image.FILTER_NEAREST);
			MUSIC_OFF = new Image("res/MusicOff.png");
			MUSIC_OFF.setFilter(Image.FILTER_NEAREST);
			SOUND_ON = new Image("res/Sound.png");
			SOUND_ON.setFilter(Image.FILTER_NEAREST);
			SOUND_OFF = new Image("res/SoundOff.png");
			SOUND_OFF.setFilter(Image.FILTER_NEAREST);
			
			
			//loads game music
			MENU_MUSIC = new Music("res/MenuMusic.ogg");
			GAME_MUSIC = new Music("res/GameMusic.ogg");
			
			
			//loads game sound effects
			BUTTON_SOUND = new Sound("res/ButtonSound.wav");
			PLAYER_HIT_SOUND = new Sound("res/PlayerHit.wav");
			ENEMY_HIT_SOUND = new Sound("res/EnemyHit.wav");
			
			//generates the colour of the background and entity outlines through RGB values
			backgroundColor = new Color(34, 35, 46); //RGB values for a bluish-purple
			
		}
		catch(SlickException se)
		{
			System.exit(-1);
		}
	}
	

	
	/**
	 * Generate an animation given a string array of names..
	 *
	 * @param imageNames The image names, in array form
	 * @param loop Whether or not the animation loops
	 * @return The generated animation
	 */
	private Animation generateAnimation(String[] imageNames, boolean loop)
	{
		//creates a new animation that either runs once or loops infinitely
		Animation animation = new Animation(loop);
		try {
			for (String s: imageNames) //iterates over every resource/image name
			{
				Image i = new Image(s); //creates an image from that string
				i.setFilter(Image.FILTER_NEAREST); //sets the scaling type
				animation.addFrame(i.getScaledCopy((float) (DemonSkiesGame.getScale() / 4.0)), 200); //adds this frame with a duration of 200 milliseconds to the animation
			}
		} catch (SlickException e) {
			e.printStackTrace();
		}
		return animation;
	}
	
	/**
	 * Gets the haste icon.
	 *
	 * @return the haste icon
	 */
	public Image getHasteIcon() {
		return HASTE_UPGRADE.getScaledCopy((float) (DemonSkiesGame.getScale() / 2.0));
	}
	
	/**
	 * Gets the velocity icon.
	 *
	 * @return the velocity icon
	 */
	public Image getVelocityIcon() {
		return VELOCITY_UPGRADE.getScaledCopy((float) (DemonSkiesGame.getScale() / 2.0));
	}
	
	/**
	 * Gets the continuum icon.
	 *
	 * @return the continuum icon
	 */
	public Image getContinuumIcon() {
		return CONTINUUM_UPGRADE.getScaledCopy((float) (DemonSkiesGame.getScale() / 2.0));
	}
	
	/**
	 * Gets the reach icon.
	 *
	 * @return the reach icon
	 */
	public Image getReachIcon() {
		return REACH_UPGRADE.getScaledCopy((float) (DemonSkiesGame.getScale() / 2.0));
	}

	
	/**
	 * Gets the shop button unfocused.
	 *
	 * @return the shop button unfocused
	 */
	public Image getShopButtonUnfocused() {
		return SHOP_BUTTON_UNFOCUS.getScaledCopy((float) (DemonSkiesGame.getScale() / 2.0));
	}

	/**
	 * Gets the shop button focused.
	 *
	 * @return the shop button focused
	 */
	public Image getShopButtonFocused() {
		return SHOP_BUTTON_FOCUS.getScaledCopy((float) (DemonSkiesGame.getScale() / 2.0));
	}

	/**
	 * Gets the start button unfocused.
	 *
	 * @return the start button unfocused
	 */
	public Image getStartButtonUnfocused() {
		return START_BUTTON_UNFOCUS.getScaledCopy((float) (DemonSkiesGame.getScale() / 2.0));
	}

	/**
	 * Gets the start button focused.
	 *
	 * @return the start button focused
	 */
	public Image getStartButtonFocused() {
		return START_BUTTON_FOCUS.getScaledCopy((float) (DemonSkiesGame.getScale() / 2.0));
	}
	
	/**
	 * Gets the back button unfocused.
	 *
	 * @return the back button unfocused
	 */
	public Image getBackButtonUnfocused() {
		return BACK_BUTTON_UNFOCUS.getScaledCopy((float) (DemonSkiesGame.getScale() / 2.0));
	}

	/**
	 * Gets the back button focused.
	 *
	 * @return the back button focused
	 */
	public Image getBackButtonFocused() {
		return BACK_BUTTON_FOCUS.getScaledCopy((float) (DemonSkiesGame.getScale() / 2.0));
	}

	/**
	 * Gets the player animation.
	 *
	 * @return the player animation
	 */
	public Animation getPlayerAnimation()
	{
		return PLAYER;
	}
	
	/**
	 * Gets the skull animation.
	 *
	 * @return the skull animation
	 */
	public Animation getSkullAnimation()
	{
		return SKULL;
	}
	
	/**
	 * Gets the arrow animation.
	 *
	 * @return the arrow animation
	 */
	public Animation getArrowAnimation()
	{
		return ARROW;
	}
	/**
	 * Gets the resource manager.
	 *
	 * @return the resource manager
	 */
	public static ResourceManager getResourceManager()
	{
		return INSTANCE;
	}
	
	/**
	 * Gets the color.
	 *
	 * @return the color
	 */
	public Color getColor()
	{
		return backgroundColor;
	}
	
	/**
	 * Gets the logo.
	 *
	 * @return the logo
	 */
	public Image getLogo() {
		return LOGO.getScaledCopy((float) (DemonSkiesGame.getScale() / 4.0));
	}
	
	/**
	 * Gets the shine.
	 *
	 * @return the shine
	 */
	public Image getShine() {
		return SHINE.getScaledCopy((float) (DemonSkiesGame.getScale() / 4.0));
	}
	
	/**
	 * Gets the background.
	 *
	 * @return the background
	 */
	public Image getBackground() {
		return BACKGROUND.getScaledCopy((float) (DemonSkiesGame.getScale() / 2.0));
	}
	
	/**
	 * Gets the sound on.
	 *
	 * @return the sound on
	 */
	public Image getSoundOn()
	{
		return SOUND_ON.getScaledCopy((float) (DemonSkiesGame.getScale() / 2.0));
	}
	
	/**
	 * Gets the sound off.
	 *
	 * @return the sound off
	 */
	public Image getSoundOff()
	{
		return SOUND_OFF.getScaledCopy((float) (DemonSkiesGame.getScale() / 2.0));
	}
	
	/**
	 * Gets the music on.
	 *
	 * @return the music on
	 */
	public Image getMusicOn()
	{
		return MUSIC_ON.getScaledCopy((float) (DemonSkiesGame.getScale() / 2.0));
	}
	
	/**
	 * Gets the music off.
	 *
	 * @return the music off
	 */
	public Image getMusicOff()
	{
		return MUSIC_OFF.getScaledCopy((float) (DemonSkiesGame.getScale() / 2.0));
	}
	
	/**
	 * Gets the button sound.
	 *
	 * @return the button sound
	 */
	public Sound getButtonSound(){
		return BUTTON_SOUND;
	}
	
	/**
	 * Gets the player hit sound.
	 *
	 * @return the player hit sound
	 */
	public Sound getPlayerHitSound(){
		return PLAYER_HIT_SOUND;
	}
	
	/**
	 * Gets the enemy hit sound.
	 *
	 * @return the enemy hit sound
	 */
	public Sound getEnemyHitSound(){
		return ENEMY_HIT_SOUND;
	}

	/**
	 * Gets the menu music.
	 *
	 * @return the menu music
	 */
	public Music getMenuMusic()
	{
		return MENU_MUSIC;
	}
	
	/**
	 * Gets the game music.
	 *
	 * @return the game music
	 */
	public Music getGameMusic()
	{
		return GAME_MUSIC;
	}
}
