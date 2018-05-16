/*
 * 
 */
package malp.DemonSkies;

import java.util.ArrayList;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.util.FontUtils;

import malp.Entities.Arrow;
import malp.Entities.Entity;
import malp.Entities.Player;
import malp.Entities.Skull;


/**
 * The GameEngine represents the current game state, including the {@link Player} entity, enemy entities, and statistics such as the {@link #score}, {@link #highScore}, and {@link #souls}. It also includes helper methods to update the game state and render it onto a window.
 */
public class GameEngine {
	
	/** The only instance of the {@link #GameEngine()}, used to reinforce the singleton design pattern. */
	
	private static final GameEngine INSTANCE = new GameEngine(); //Singleton design pattern, only one object of this class will ever exist

	
	/** The live entities within the game engine. */
	private ArrayList<Entity> liveEntities = new ArrayList<Entity>();
	
	/** The deferred entities, to be added in the next update loop. */
	private ArrayList<Entity> deferredEntities = new ArrayList<Entity>();
	
	/** The discarded entities, to be removed at the start of the next update loop. */
	private ArrayList<Entity> discardedEntities = new ArrayList<Entity>();
	
	/** The player entity. */
	private Player player;
	
	
	/** The arrow entity. */
	private Arrow arrow;
	
	
	
	/** The high score. */
	private int highScore = 0;
	
	/** The currency used in the shop. */
	private int souls = 0;
	
	/** The current score. */
	private int score = 0;
	
	/** The player's haste upgrade level. */
	private int hasteLevel = 0;
	
	/** The player's velocity upgrade level. */
	private int velocityLevel = 0;
	
	/** The player's continuum upgrade level. */
	private int continuumLevel = 0;
	
	/** The player's reach upgrade level. */
	private int reachLevel = 0;
	
	/** The Constant UPGRADE_PRICE represents the price an upgrade tier should cost. */
	private static final int UPGRADE_PRICE = 125;
	
//	/** The Constant MAX_ENTITIES represents the largest number of entities that can exist during a round. */
	private static final int MAX_ENTITIES = 20;
	
	/** The Constant MAX_UPGRADE_LEVEL represents the highest tier of upgrade possible. */
	private static final int MAX_UPGRADE_LEVEL = 5;
	
	/** The Constant TICK_DELTA represents how often the game state should ideally be updated. */
	private static final int TICK_DELTA = 16; //how many ticks (roughly) should pass before the game is updated (1000 milliseconds / 60 updates to coincide with 60 frames) = 16.6... frames a second, rounded down
	
	/** The accumulated differences in milliseconds between game state updates. */
	private int deltaSum = 0;

	
	/** The update count tracks the number of times {@link #update(GameContainer, int)} has been called this round. */
	private int updateCount = 0;
	
	/**
	 * Instantiates a new game engine. This method is private to enforce the singleton design pattern.
	 */
	private GameEngine() //private to enforce the Singleton design pattern
	{
	}
	
	/**
	 * Gets the singleton engine instance.
	 *
	 * @return the engine
	 */
	public static GameEngine getEngine() { //implementation of the Singleton design pattern, returns the only existing instance of the object when requested
        return INSTANCE; 
    }

	/**
	 * Resets the engine prior to playing a round.
	 */
	public void resetEngine() //resets the Engine after playing a round
	{
		
		System.out.println("RESET"); //stdout
		if (score > highScore)
		{
			highScore = score;
		}
		score = 0;
		updateCount = 0;
		liveEntities.clear(); //clear all current entities
		deferredEntities.clear(); //clear deferred entities
		discardedEntities.clear(); //clear the garbage entities
		player = new Player(DemonSkiesGame.getWidth() / 2, DemonSkiesGame.getHeight() / 2);
		arrow = new Arrow(0, 0, 0);
	}
	
	/**
	 * Handles the collision of two entities.
	 *
	 * @param first The first entity
	 * @param second The second entity
	 */
	private void handleCollision(Entity first, Entity second)
	{
		//the first entity can be any entity type, the second is only an enemy (as seen in the update() method)
		//this reduces the need for extra if statements
		if (first instanceof Skull && second instanceof Skull)
		{
			//if both of the entities are skulls, switch their angle direction to simulate a collision and bouncing
			double tmp;
			Skull s1, s2;
			s1 = (Skull) first;
			s2 = (Skull) second;
			tmp = s1.getAngle();
			s1.setAngle(s2.getAngle());
			s2.setAngle(tmp);
			for (int i = 0; i < 2; i++)//to ensure that the entities don't stick together, the first skull is repeatedly updated to move it away from the other skull
			{
				s1.update();
			}
		}
		else if (first instanceof Arrow && second instanceof Skull)
		{
			//if the first entity is an arrow, increase the number of times the arrow has hit an enemy and kill the second entity, a skull
			ResourceManager.getResourceManager().getEnemyHitSound().play();
			((Arrow) first).hit();
			second.kill();
			score++;
			souls++;
		}
		else if (first instanceof Player && second instanceof Skull)
		{
			//if the player collides with a skull, kill the player
			ResourceManager.getResourceManager().getPlayerHitSound().play();
			first.kill();
		}
	}
	
	/**
	 * Updates the game state and returns whether or not the game is still running or over.
	 *
	 * @param gc The GameContainer
	 * @param delta The difference in milliseconds since the last update
	 * @return true, if the player is still alive
	 */
	public boolean update(GameContainer gc, int delta) //the important method in my GameEngine, this method is called from a game state and is passed the delta (time that has passed since the last update) and the GameContainer to handle behind the scenes
	{
		//remove unused enemies, get a handle to the input device, increment the total amount of time that has passed since the last call to update()
		Input input = gc.getInput();
		deltaSum += delta;
		cleanUp();
		
		//sets the angle using the arctangent function
		player.setAngle(Math.atan2(input.getMouseY() - player.getY(), input.getMouseX() - player.getX()));
		
		//ensures the player won't go off-screen
		player.setX((player.getX() % DemonSkiesGame.getWidth() + DemonSkiesGame.getWidth()) % DemonSkiesGame.getWidth());
		player.setY((player.getY() % DemonSkiesGame.getHeight() + DemonSkiesGame.getHeight()) % DemonSkiesGame.getHeight());
		
		arrow.update();
		for (int i = 0; i < deltaSum; i+= TICK_DELTA) //catches up according to the elapsed time, this way the game runs smoothly even after lag spikes
		{		
			updateCount++;
			
			if (updateCount % 100 == 0) //increments the score and adds new enemies every 100 updates
			{
				score++;
				if(liveEntities.size() < MAX_ENTITIES)
				{
					if (Math.random() > 0.5) //static way of getting a 50/50% chance
					{
						add(new Skull(0, Math.random() * DemonSkiesGame.getHeight(), Math.random() * Math.PI * 2));
					}
					else
					{
						add(new Skull(Math.random() * DemonSkiesGame.getWidth(), 0, Math.random() * Math.PI * 2));
					}
				}

			}
			//shoots the arrow if it is not currently flying
			if (!arrow.isAlive() && input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON))
			{
				arrow.shoot(player.getX(), player.getY(), player.getAngle());
			}
			
			//moves the player
			if (input.isKeyDown(Input.KEY_W))
			{
				player.setY(player.getY() - 0.2 * hasteLevel * DemonSkiesGame.getScale() - 1);
			}
			if (input.isKeyDown(Input.KEY_S))
			{
				player.setY(player.getY() + 0.2 * hasteLevel * DemonSkiesGame.getScale() + 1);
			}
			if (input.isKeyDown(Input.KEY_D))
			{
				player.setX(player.getX() + 0.2 * hasteLevel * DemonSkiesGame.getScale()+ 1);
			}
			if (input.isKeyDown(Input.KEY_A))
			{
				player.setX(player.getX() - 0.2 * hasteLevel * DemonSkiesGame.getScale() - 1);
			}
			
			//updates every entity
			for (Entity e: liveEntities)
			{
				e.update();
			}
			
			//checks if any entities are colliding and then handles collision appropriately
			for (int starterEntityIndex = 0; starterEntityIndex < liveEntities.size(); starterEntityIndex++) {
				for (int comparedEntityIndex = starterEntityIndex + 1; comparedEntityIndex < liveEntities.size(); comparedEntityIndex++) {
					Entity e1 = liveEntities.get(starterEntityIndex);
					if (player.isCollided(e1))
					{
						handleCollision(player, e1);
					}
					if (arrow.isAlive() && arrow.isCollided(e1))
					{
						handleCollision(arrow, e1);
					}
					if (e1.isCollided(liveEntities.get(comparedEntityIndex))) {
						handleCollision(e1, liveEntities.get(comparedEntityIndex));
					}
				}
			}
		}
		//removes the used-up time in milliseconds
		deltaSum %= TICK_DELTA;
		liveEntities.addAll(deferredEntities); //add all the deferred entities
		deferredEntities.clear(); //clear the deferred entities
		
		//checks if the player is still alive after the update loop, determining if the game should go on
		return player.isAlive();
	}
	
	/**
	 * Cleans up the game state, removing unused and "dead" entities.
	 */
	private void cleanUp()
	{
		for (Entity entities: liveEntities) //iterate over the entities
		{
			if (!entities.isAlive()) //check if the entity is past the x coordinate at which they should be cleaned up
			{
				discardedEntities.add(entities); //add the object to the garbage arraylist
			}
		}
		for (Entity e: discardedEntities) //iterate over all the entities that are about to be removed
		{
			liveEntities.remove(e); //remove them from the spawned entities
		}
	}
	
	/**
	 * Render the game on to a graphical context.
	 *
	 * @param gc the GameContainer
	 * @param g the Graphics context
	 */
	public void render(GameContainer gc, Graphics g) //this method is called from the Play state
	{
		player.draw();
		arrow.draw();
		for (Entity e: liveEntities) //draw the entities
		{
			e.draw();
		}
		FontUtils.drawCenter(gc.getDefaultFont(), "Score: " + score + " | Highscore: " + highScore, gc.getWidth() /2, 0, ("Score: " + score + " | Highscore: " + highScore).length());
	}
	/**
	 * Adds an entity to the game state.
	 *
	 * @param entity the entity
	 */
	private void add(Entity entity) //add an entity to the game engine
	{
		deferredEntities.add(entity); //this method can be called at any time because of the use of the second arraylist deferredEntities, ensuring that it is thread safe
	}
	
	
	/**
	 * Gets the level for a specific upgrade type.
	 *
	 * @param category The upgrade category
	 * @return The upgrade level
	 */
	public int getLevel(UpgradeType category)
	{
		switch (category)
		{
		case CONTINUUM:
			return continuumLevel;
		case HASTE:
			return hasteLevel;
		case REACH:
			return reachLevel;
		case VELOCITY:
			return velocityLevel;
		default:
			return 0;
		}
	}
	
	/**
	 * Gets the price of an upgrade tier.
	 *
	 * @param category The upgrade category
	 * @return The upgrade tier price
	 */
	public int getPrice(UpgradeType category)
	{
		switch (category)
		{
		case CONTINUUM:
			return (continuumLevel + 1) * UPGRADE_PRICE;
		case HASTE:
			return (hasteLevel + 1) * UPGRADE_PRICE;
		case REACH:
			return (reachLevel + 1) * UPGRADE_PRICE;
		case VELOCITY:
			return (velocityLevel + 1) * UPGRADE_PRICE;
		default:
			return 0;
		}
	}
	
	/**
	 * Upgrades a specified upgrade type.
	 *
	 * @param type The upgrade type
	 */
	public void upgrade(UpgradeType type)
	{
		//attempts to purchase an upgrade category, incrementing the upgrade level and decrementing the balance based on the type
		switch (type)
		{
		case CONTINUUM:
			if (continuumLevel < MAX_UPGRADE_LEVEL && souls >= getPrice(type))
			{
				continuumLevel++;
				souls -= continuumLevel * UPGRADE_PRICE;
			}
			break;
		case HASTE:
			if (hasteLevel < MAX_UPGRADE_LEVEL && souls >= getPrice(type))
			{
				hasteLevel++;
				souls -= hasteLevel * UPGRADE_PRICE;
			}
			break;
		case REACH:
			if (reachLevel < MAX_UPGRADE_LEVEL && souls >= getPrice(type))
			{
				reachLevel++;
				souls -= reachLevel * UPGRADE_PRICE;
			}
			break;
		case VELOCITY:
			if (velocityLevel < MAX_UPGRADE_LEVEL && souls >= getPrice(type))
			{
				velocityLevel++;
				souls -= velocityLevel * UPGRADE_PRICE;
			}
			break;
		default:
			break;
		
		}
	}
	
	
	/**
	 * Gets the high score.
	 *
	 * @return The high score
	 */
	public int getHighScore() //return highscore, also automatically sets the highscore if the score is bigger than the highscore
	{
		if (score > highScore)
		{
			highScore = score;
		}
		return highScore;
	}
	
	/**
	 * Gets the current score.
	 *
	 * @return The score
	 */
	public int getScore() //returns score
	{
		return score;
	}
	
	/**
	 * Gets the currency, called souls.
	 *
	 * @return The number of souls
	 */
	public int getSouls()
	{
		return souls;
	}
	
	
	/**
	 * Returns the number of live entities.
	 *
	 * @return The size of {@link #liveEntities}.
	 */
	public int getEntityCount() //get the total amount of entities being updated and used by the engine
	{
		return liveEntities.size();
	}

}
