/*
 * 
 */
package malp.DemonSkies;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import malp.States.MainMenu;
import malp.States.Play;
import malp.States.Shop;

// TODO: Auto-generated Javadoc
/**
 * The class representing the resulting game and containing the {@link #main(String[])} entry point.
 */
public class DemonSkiesGame extends StateBasedGame{ //extends the Slick2D Finite-state-machine based game handler
	
	/** The game name and the string displayed on the game window.*/
	private static final String GAME_NAME = "Demon Skies"; //window name
	
	/** This ID is used internally to switch between different game states and menus; here, it represents the playing state. */
	public static final int PLAY = 0; //play state ID, required for the game handler
	
	/** This ID is used internally to switch between different game states and menus; here, it represents the main menu state. */
	public static final int MAIN_MENU = 1; //same as above for the main menu
	
	/** This ID is used internally to switch between different game states and menus; here, it represents the shop menu state. */
	public static final int SHOP = 2; //same as above but the tutorial
	
	/** The Constant RATIO_WIDTH represents the width ratio that the game window should be displayed at. */
	private static final int RATIO_WIDTH = 240;
	
	/** The Constant RATIO_HEIGHT represents the height ratio that the game window should be displayed at. */
	private static final int RATIO_HEIGHT = 180;
	
	/** The Constant MAX_FRAMES represents the target/maximum frame rate, in Hertz, at which the window should update itself. */
	private static final int MAX_FRAMES = 144;
	
	/** The scale at which the window will be resized to accommodate for different monitor sizes. */
	private static int drawScale = 1;
	
	/** The width, in pixels, that the game window is drawn at. */
	private static int width;
	
	/** The height, in pixels, that the game window is drawn at. */
	private static int height;
	
	/**
	 * Instantiates a new game instance of Demon Skies.
	 */
	public DemonSkiesGame()
	{
		//adds the appropriate menus to the game
		super(GAME_NAME); 
		this.addState(new MainMenu()); 
		this.addState(new Play()); 
		this.addState(new Shop()); 
	}
	
	/** Initiates the various game states, inherited method.
	 * @see org.newdawn.slick.state.StateBasedGame#initStatesList(org.newdawn.slick.GameContainer)
	 */
	@Override
	public void initStatesList(GameContainer gc) throws SlickException { //init the game handler and all of its game states
		getState(MAIN_MENU).init(gc, this); //init main menu
		getState(PLAY).init(gc, this); //init play
		getState(SHOP).init(gc, this); //init tutorial
		
		enterState(MAIN_MENU); //enter the MAIN_MENU game state
	}
	
	/**
	 * The main method and entry point for the program.
	 *
	 * @param args The command-line arguments
	 */
	public static void main(String[] args)
	{
		try
		{
			dropNatives(); //LWJGL and Slick2D require native libraries, which Java can only load from disk
		}
		catch (Exception e)
		{
			e.printStackTrace(); //if the File I/O fails, report it and then exit with an error status code
			System.exit(-1);
		}
		
		System.setProperty("org.lwjgl.librarypath", new File(System.getProperty("java.io.tmpdir") + "natives").getAbsolutePath()); //set the library native path programatically using the system temporary folder
		Input.disableControllers(); 
		
		AppGameContainer appgc; 
		try{
			appgc = new AppGameContainer(new DemonSkiesGame());
			appgc.setIcon("res/Icon.png"); 
			appgc.setTargetFrameRate(MAX_FRAMES);
			appgc.setUpdateOnlyWhenVisible(true);
			
			
			//scale the window to the appropriate size, keeping the right proportions
			DisplayMode dm = Display.getDesktopDisplayMode();
			drawScale = Math.min(dm.getWidth() / RATIO_WIDTH, dm.getHeight() / RATIO_HEIGHT);
			width = RATIO_WIDTH * drawScale;
			height = RATIO_HEIGHT * drawScale;
			appgc.setDisplayMode(width, height, false); //set the game to not fullscreen
			appgc.start();
		}
		catch(SlickException e)	{
			e.printStackTrace();
		}

	}
	
	/**
	 * Drops the natives files used by the game libraries onto a temporary location on-disk.
	 *
	 * @throws FileNotFoundException Signals that the file has not been found.
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private static void dropNatives() throws FileNotFoundException, IOException //throws IO errors to gracefully handle restrictions (i.e. Antivirus blocks *.dll files)
	{
		/*
		 * This library requires native files for hte game to run.
		 * These binaries must be dropped to disk.
		 * This method will get the corresponding binaries, extract them from
		 * the JAR, and finally drop them to the temporary directory of the OS,
		 * ensuring that cleaning up will be handled by the OS (eventually).
		 */
		if (System.getProperty("os.name").toLowerCase().contains("win")) //windows OS?
		{
			/*
			 * Drops all the related windows binaries, as determining the 
			 * architecture of the host OS is unreliable and error-prone.
			 */
			dropFile("natives", "jinput-dx8_64.dll");
			dropFile("natives", "jinput-dx8.dll");
			dropFile("natives", "jinput-raw_64.dll");
			dropFile("natives", "jinput-raw.dll");
			dropFile("natives", "lwjgl.dll");
			dropFile("natives", "lwjgl64.dll");
			dropFile("natives", "OpenAL32.dll");
			dropFile("natives", "OpenAL64.dll");
		}
		else if (System.getProperty("os.name").toLowerCase().contains("mac"))
			/*
			 * Drops the dylib files for Mac OS + the Apple Ecosystem
			 */
		{
			dropFile("natives", "libjinput-osx.dylib");
			dropFile("natives", "liblwjgl.dylib");
			dropFile("natives", "openal.dylib");
		}
		else if (System.getProperty("os.name").toLowerCase().contains("nix"))
			/*
			 * Drops the shared object files for *nix systems
			 */
		{
			dropFile("natives", "libjinput-linux.so");
			dropFile("natives", "libjinput-linux64.so");
			dropFile("natives", "liblwjgl.so");
			dropFile("natives", "liblwjgl64.so");
			dropFile("natives", "libopenal.so");
			dropFile("natives", "libopenal64.so");
		}
		else
		{
			throw new RuntimeException("This OS isn't supported!"); //OS returned an unsupported issue, this should *NEVER* happen.
		}
	}
	
	/**
	 * Drops a specified file to disk.
	 *
	 * @param location The location on-disk to write to
	 * @param name The internal file name
	 * @throws FileNotFoundException Signals that the file has not been found.
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private static void dropFile(String location, String name) throws FileNotFoundException, IOException
	{
		/*
		 * This method will drop a file from the JAR file to its corresponding 
		 * temporary location on the OS running the game.
		 */
		File folder = new File(System.getProperty("java.io.tmpdir") + location); //get handle to folder
		folder.mkdir(); //make the folder
		InputStream in = ClassLoader.getSystemClassLoader().getResourceAsStream(location + "/" + name); //grab resource as steam
        File nativeDrop = new File(folder + "/" + name).getAbsoluteFile(); //get absolute file path for the file that should be dropped to the disk
        System.out.println("Dropping file to: " + nativeDrop.getAbsolutePath()); //output file location and full name to stdout
		try (FileOutputStream out = new FileOutputStream(nativeDrop)) { //create file output stream
            //copy stream
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead); //keep writing until the byte buffer tells us it's time to stop
            }
        }
	}

	/**
	 * Gets the game scale.
	 *
	 * @return the scale
	 */
	public static int getScale()
	{
		return drawScale;
	}

	/**
	 * Gets the game width.
	 *
	 * @return the width
	 */
	public static int getWidth() {
		return width;
	}

	/**
	 * Gets the game height.
	 *
	 * @return the height
	 */
	public static int getHeight() {
		return height;
	}
}
