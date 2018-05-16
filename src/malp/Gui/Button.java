package malp.Gui;

import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.gui.GUIContext;
import org.newdawn.slick.gui.MouseOverArea;

import malp.DemonSkies.ResourceManager;


// TODO: Auto-generated Javadoc
/**
 * The Button class.
 * 
 * This class takes the LWJGL/Slick2D class known as the MouseOverArea and
 * implements it to generate a GUI component that can detect mouse interaction with it.
 */
public class Button extends MouseOverArea {
	
	/**
	 * Instantiates a new button in the current GUIContext.
	 *
	 * @param container The container
	 * @param image The image to show when the button is not selected
	 * @param mouseOverImage The image to show when the button is selected
	 * @param x The x coordinate
	 * @param y The y coordinate
	 */

	public Button(GUIContext container, Image image, Image mouseOverImage, int x, int y) { //everything but the mouseOverImage is necessary for a superclass call
		super(container, image, x, y); //superclass override
		setMouseOverImage(mouseOverImage); //register mouseOverImage
		setMouseDownSound(ResourceManager.getResourceManager().getButtonSound()); //register mouseClickedSound to the button sound from my ResourceManager
	}
	
	/**
	 * Checks if the button is pressed.
	 *
	 * @return true, if the button is pressed
	 */
	public boolean isButtonPressed()
	{
		if (isMouseOver()) //if the mouseOverArea is being hovered over
		{
			Input input = container.getInput(); //grab global input queue/handler class
			if (input.isMousePressed(Input.MOUSE_LEFT_BUTTON)) //check if the left mouse button is down
			{
				ResourceManager.getResourceManager().getButtonSound().play();
				return true; //return true
			}
		}
		return false; //otherwise drop down to the end of the method, return false

	}
	

}
