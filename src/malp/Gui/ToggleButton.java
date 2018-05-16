package malp.Gui;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Image;
import org.newdawn.slick.gui.GUIContext;

/**
 * The ToggleButton class, a special button with two states; on, and off.
 */
public class ToggleButton extends Button {
	
	/** The button state, either toggled or untoggled. */
	private boolean buttonState; //toggle state
	
	/** The image displayed when toggled. */
	private Image toggledImage; //toggled image
	
	/** The image displayed when untoggled. */
	private Image image; //default / not toggled image

	/**
	 * Instantiates a new toggle button.
	 *
	 * @param container The {@link GameContainer} context
	 * @param image The image to display when the button is untoggled.
	 * @param toggledImage The image to display when the button is toggled.
	 * @param x The x coordinate.
	 * @param y The y coordinate.
	 */
	public ToggleButton(GUIContext container, Image image, Image toggledImage, int x, int y) { //these are all necessary for my superclass call, see my Button class for more
		super(container, image, image, x, y); //superclass override
		this.image = image; //register constructor argument images with the Image private to this class
		this.toggledImage = toggledImage; //register constructor argument images with the Image private to this class
		setMouseOverImage(image);  //override the mouseOverImage functionality by setting the mouseOverImage to the same image as the mouseNotOverImage
	}

	/**
	 * Try to toggle the button.
	 */
	public void tryToggle() //this method will toggle the button if it is pressed, but must be called explicitly from the game thread to avoid all sorts of threading problems
	{
		if (isButtonPressed()) //checks if the button is pressed
		{
			buttonState = !buttonState; //toggle the boolean, Java will initialize it for me behind the scenes; explicitly stating it will lead to the button not working properly
			if (buttonState) //if original state
			{
				setNormalImage(toggledImage); //set the default image to the toggled image
				setMouseOverImage(toggledImage); //set the mouse over image to the toggled image
			}
			else //otherwise:
			{
				setNormalImage(image); //same as above, but the inverse (i.e. different image this time)
				setMouseOverImage(image); //same as above
			}

		}
	}
	
	/**
	 * Checks if the button is toggled.
	 *
	 * @return true, if it is toggled
	 */
	public boolean isToggled() //return the current state
	{
		return buttonState;
	}
}
