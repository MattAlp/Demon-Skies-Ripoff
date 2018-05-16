package malp.Entities;

import malp.DemonSkies.DemonSkiesGame;
import malp.DemonSkies.GameEngine;
import malp.DemonSkies.ResourceManager;
import malp.DemonSkies.UpgradeType;

// TODO: Auto-generated Javadoc
/**
 * This class represents the arrow {@link Entity} which is shot by the {@link Player} to kill enemy entities..
 */
public class Arrow extends Entity{
	
	/** The angle the arrow "flies" at, in radians ranging from 0 to two pi. */
	private double angle;
	
	/** The distance the arrow has traveled since it was shot by the {@link #shoot(double, double, double)} method. */
	private int distance;
	
	/** The maximum distance the arrow can travel since it was shot by the {@link #shoot(double, double, double)} method. */
	private int maxDistance;
	
	/** The velocity at which the arrow travels at. */
	private int velocity;

	/** The number of enemy entities the arrow has hit since it was shot. */
	private int hitCount;
	
	/** The maximum number of enemies the arrow can hit per shot. */
	private int maxHits;
	
/**
 * Instantiates a new arrow.
 *
 * @param x The x coordinate to shoot from
 * @param y The y coordinate to shoot from
 * @param angle The angle, in radians, at which the arrow "flies" at
 */
public Arrow(double x, double y, double angle) {
		super(ResourceManager.getResourceManager().getArrowAnimation(), x, y);
		this.angle = angle;
	}

	/** Moves the arrow at the appropriate {@link #angle} and determines whether the arrow's lifespan is up.
	 * @see malp.Entities.Entity#update()
	 */
	@Override
	public void update() {
		distance += velocity;
		if (distance >= maxDistance) //kills the arrow after it travels a set distance
		{
			this.alive = false;
		}
		if (this.alive)
		{
			for (int i = 0; i <= velocity; i++) //uses trigonometry to translate the angle into an x and y displacement
			{
				this.x += Math.cos(angle);
				this.y += Math.sin(angle);
			}
			//ensures the arrow doesn't go offscreen
			this.x =  (x % DemonSkiesGame.getWidth() + DemonSkiesGame.getWidth()) % DemonSkiesGame.getWidth();
			this.y = (y % DemonSkiesGame.getHeight() + DemonSkiesGame.getHeight()) % DemonSkiesGame.getHeight();
		}
		if (this.hitCount > maxHits) //ensures the arrow doesn't kill more than a set number of enemies
		{
			this.alive = false;
		}
		
	}
	
	/* (non-Javadoc)
	 * @see malp.Entities.Entity#draw()
	 */
	@Override
	public void draw() {
		if (alive)
		{
			super.draw();	
		}	
	}
	
	/**
	 * Shoots the arrow from a given position at a given angle.
	 *
	 * @param x The x coordinate
	 * @param y The y coordinate
	 * @param angle The angle, in radians, at which the arrow "flies"
	 */
	public void shoot(double x, double y, double angle)
	{
		//sets the arrow's properties
		this.velocity = (GameEngine.getEngine().getLevel(UpgradeType.VELOCITY) + 1) * 2;
		this.x = x;
		this.y = y;
		this.angle = angle;
		ResourceManager.getResourceManager().getArrowAnimation().getCurrentFrame().setRotation((float) Math.toDegrees(angle));
		this.alive = true;
		this.distance = 0;
		this.maxDistance = (GameEngine.getEngine().getLevel(UpgradeType.CONTINUUM) + 1) * 250;
		this.hitCount = 0;
		this.maxHits = (GameEngine.getEngine().getLevel(UpgradeType.REACH) + 1) * 5;
	}
	
	/**
	 * Called by the {@link GameEngine} whenever it detects a collision between the arrow and an enemy entity such as the {@link Skull}.
	 * This increases the {@link #hitCount} value to ensure that the arrow can only hit a set number of entities each shot.
	 */
	public void hit()
	{
		hitCount++;
	}
}
