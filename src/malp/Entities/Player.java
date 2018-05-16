package malp.Entities;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import malp.DemonSkies.DemonSkiesGame;
import malp.DemonSkies.ResourceManager;

// TODO: Auto-generated Javadoc
/**
 * This class represents the entity controlled via the mouse and keyboard by the player.
 */
public class Player extends Entity{
	
	/** The image representing the bow. */
	private Image bow;
	
	/** The angle, in radians, that the player shoots arrows at. */
	private double angle;
	
	/**
	 * Instantiates a new player.
	 *
	 * @param x The x coordinate
	 * @param y The y coordinate
	 */
	public Player(int x, int y) {
		super(ResourceManager.getResourceManager().getPlayerAnimation(), x, y);
		try {
			bow = new Image("res/Bow1.png");
			bow.setFilter(Image.FILTER_NEAREST);
			bow = bow.getScaledCopy((float) (DemonSkiesGame.getScale() / 4.0));
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}

	/** This method is empty, as this entity is controlled via peripherals such as a mouse and keyboard.
	 * @see malp.Entities.Entity#update()
	 */
	@Override
	public void update() {
	}
	
	/** Renders the player animation, as defined {@link malp.Entities.Entity#draw()}, followed by drawing the {@link #bow} on top of the player, rotated to the {@link #angle}.
	 * @see malp.Entities.Entity#draw()
	 */
	@Override
	public void draw()
	{
		super.draw();//draws the player animation
		bow.setRotation((float) (Math.toDegrees(angle))); //rotates the bow image to the correcr angle (converted from radians to degrees)
		bow.draw((float) x + bow.getWidth()/ 2, (float) y); //draws the bow image on top of the player
	}
	
	/**
	 * Sets the angle.
	 *
	 * @param angle The new angle
	 */
	public void setAngle(double angle)
	{
		this.angle = angle;
	}
	
	/**
	 * Gets the angle.
	 *
	 * @return The angle
	 */
	public double getAngle()
	{
		return angle;
	}
	
}
