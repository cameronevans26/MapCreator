import java.awt.event.MouseListener;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.text.View;

import java.awt.event.MouseEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;

class Controller implements ActionListener, MouseListener, KeyListener
{
	GameView view;
	Model model;
	Thing thing;
	boolean keyLeft;
	boolean keyRight;
	boolean keyUp;
	boolean keyDown;

	Controller(Model m)
	{
		model = m;
	}

	void setView(GameView v)
	{
		view = v;
	}

	public void actionPerformed(ActionEvent e)
	{
		// Handle Save button click
		if (e.getSource() == view.saveButton) 
		{
			try 
			{
    			FileWriter writer = new FileWriter("map.json");
    			writer.write(this.view.marshal().toString());
    			writer.close();
  			} 
			catch (IOException exception) 
			{
    			exception.printStackTrace();
    			System.exit(1);
  			}

        } 
		
		// Handle Load button click
		else if (e.getSource() == view.loadButton) 
		{
            try 
			{
            	// Read the contents of the "map.json" file
            	FileReader reader = new FileReader("map.json");
            	StringBuilder contents = new StringBuilder();
            	int character;
            	while ((character = reader.read()) != -1) 
				{
                	contents.append((char) character);
            	}
            	reader.close();

            	// Parse the JSON contents and update the view's things
            	Json jsonData = Json.parse(contents.toString());
            	view.unmarshal(jsonData);
        	} 	
			catch (IOException exception) 
			{
            	exception.printStackTrace();
            	System.exit(1);
        	}
        }
	}
	
	
	private boolean isInsidePinkBox(int x, int y) 
	{
	    int pinkBoxX = 0; 
	    int pinkBoxY = 0;
	    int pinkBoxWidth = 200;
	    int pinkBoxHeight = 200;
	    
	    // Check if (x, y) is inside the pink box boundaries
	    if (x >= pinkBoxX && x <= pinkBoxX + pinkBoxWidth && y >= pinkBoxY && y <= pinkBoxY + pinkBoxHeight) 
	    {
	        return true;
	    } 
	    else 
	    {
	        return false;
	    }
	}

	public void mousePressed(MouseEvent e)
	{
		int scrollx = view.getHorizontalScroll();
		int scrolly = view.getVerticalScroll();

		if (e.getButton() == 1 && isInsidePinkBox(e.getX(), e.getY()) == true) 
		{
			view.incrementValue();
		}
			
		else if (e.getButton() == 1 && isInsidePinkBox(e.getX(), e.getY()) == false)
		{
			view.addThing(e.getX(), e.getY(), view.getI());
		}
		
		else if (e.getButton() == 3)
		{
			view.removeThing(e.getX(), e.getY());
		}
	}

	public void mouseReleased(MouseEvent e) 
	{	}
	
	public void mouseEntered(MouseEvent e) 
	{	}
	
	public void mouseExited(MouseEvent e) 
	{	}
	
	public void mouseClicked(MouseEvent e) 
	{	}
	
	public void keyPressed(KeyEvent e)
	{
		switch(e.getKeyCode())
		{
			case KeyEvent.VK_RIGHT: 
				keyRight = true; 
				break;
			case KeyEvent.VK_LEFT: 
				keyLeft = true; 
				break;
			case KeyEvent.VK_UP: 
				keyUp = true; 
				break;
			case KeyEvent.VK_DOWN: 
				keyDown = true; 
				break;
		}
	}

	public void keyReleased(KeyEvent e)
	{
		switch(e.getKeyCode())
		{
			case KeyEvent.VK_RIGHT: 
				keyRight = false; 
				break;
			case KeyEvent.VK_LEFT: 
				keyLeft = false; 
				break;
			case KeyEvent.VK_UP: 
				keyUp = false; 
				break;
			case KeyEvent.VK_DOWN: 
				keyDown = false; 
				break;
			case KeyEvent.VK_ESCAPE:
				System.exit(0);
		}
		char c = Character.toLowerCase(e.getKeyChar());
		if(c == 'q')
			System.exit(0);
        if(c == 'r')
            model.reset();
	}

	public void keyTyped(KeyEvent e)
	{	}

	void update()
	{
		if(keyRight) 
            view.scrollRight();
		if(keyLeft) 
    		view.scrollLeft();
		if(keyDown) 
            view.scrollDown();
		if(keyUp)
            view.scrollUp();
	}
}
