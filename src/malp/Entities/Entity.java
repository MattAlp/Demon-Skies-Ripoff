package malp.Entities;

import org.newdawn.slick.Animation;

import malp.DemonSkies.GameEngine;

	
// TODO: Auto-generated Javadoc
/**
 * The Entity class, representing a living object that the {@link GameEngine} handles.
 */
public abstract class Entity {
	
	/** The {@link Animation} rendered on-screen by the {@link #draw()} method. */
	/*
	 * Entity class used by the Game Engine to represent an object in the game (i.e. a coin or a missile)
	 */
	private Animation animation; //the animation to display when rendering
	
	/** The x coordinate. */
	protected double x; //x coordinate of the sprite/entity
	
	/** The y coordinate. */
	protected double y; //y coordinate of the sprite/entity
	
	/** The active state of the entity, used internally and by the {@link GameEngine} to determine entity behaviour and clean up unused entities. */
	protected boolean alive = true;
	/**
	 * Instantiates a new entity.
	 *
	 * @param animation The animation to use.
	 * @param x The x coordinate.
	 * @param y The y coordinate.
	 */
	public Entity(Animation animation, double x, double y) 
	{
		this.animation = animation;
		this.x = x;
		this.y = y;
	}
	
	/**
	 * Update the entity.
	 */
	abstract public void update(); //abstract method, individually implemented
	
	/**
	 * Checks if the entity is collided with another one.
	 *
	 * @param otherEntity The other entity to test
	 * @return true, if is collided
	 */
	public boolean isCollided(Entity otherEntity) //checks if the entity is collided with another entity
	{
		return (this.x < otherEntity.x + otherEntity.animation.getCurrentFrame().getWidth() && this.x + this.animation.getCurrentFrame().getWidth() > otherEntity.x && this.y < otherEntity.y + otherEntity.animation.getCurrentFrame().getHeight() && this.y + this.animation.getHeight() > otherEntity.y);
	}
	
	/**
	 * Draw the entity on-screen.
	 */
	public void draw() //draw the object
	{
		animation.draw((float) x, (float) y);
	}
	

	/**
	 * Gets the x coordinate.
	 *
	 * @return the x
	 */
	public double getX() { //getter for x
		return x;
	}
	
	/**
	 * Sets the x coordinate.
	 *
	 * @param x the new x
	 */
	public void setX(double x) { //setter for x
		this.x = x;
	}

	/**
	 * Gets the y coordinate.
	 *
	 * @return the y
	 */
	public double getY() { //getter for x
		return y;
	}

	/**
	 * Sets the y coordinate.
	 *
	 * @param y the new y
	 */
	public void setY(double y) { //setter for Y
		this.y = y;
	}
	
	/**
	 * Checks if the entity is alive.
	 *
	 * @return true, if it is alive
	 */
	public boolean isAlive()
	{
		return alive;
	}
	
	/**
	 * Kills the entity, setting {@link #alive} to false.
	 */
	public void kill()
	{
		alive = false;
	}
}
