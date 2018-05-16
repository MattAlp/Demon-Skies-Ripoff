package malp.Entities;

import malp.DemonSkies.DemonSkiesGame;
import malp.DemonSkies.ResourceManager;

// TODO: Auto-generated Javadoc
/**
 * This class represents the main enemy encountered in the game, a floating skull.
 */
public class Skull extends Entity{
	
	/** The angle, in radians, at which the entity flies. */
	private double angle;
	
	/**
	 * Instantiates a new skull.
	 *
	 * @param x The x coordinate
	 * @param y The y coordinate
	 * @param angle The angle at which the entity flies
	 */
	public Skull(double x, double y, double angle) {
		super(ResourceManager.getResourceManager().getSkullAnimation(), x, y);
		this.angle = angle;
	}

	/** Translates the skull based on the {@link #angle}. This method is inherited.
	 * @see malp.Entities.Entity#update()
	 */
	@Override
	public void update() {
		//uses trigonometry	to translate the angle to an x and y displacement
		this.x += Math.sin(angle);
		this.y += Math.cos(angle);
		
		//moves the skull to the other side of the screen or from the bottom to the top / vice-versa to ensure the skull doesn't move off of the screen
		this.x =  (x % DemonSkiesGame.getWidth() + DemonSkiesGame.getWidth()) % DemonSkiesGame.getWidth();
		this.y = (y % DemonSkiesGame.getHeight() + DemonSkiesGame.getHeight()) % DemonSkiesGame.getHeight();
	}


	/**
	 * Gets the angle.
	 *
	 * @return The angle
	 */
	public double getAngle() {
		return angle;
	}

	/**
	 * Sets the angle.
	 *
	 * @param angle The new angle
	 */
	public void setAngle(double angle) {
		this.angle = angle;
	}
	
}
