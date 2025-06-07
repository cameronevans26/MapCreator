import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import javax.swing.JButton;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;
import java.awt.Font;
import java.awt.Graphics2D;

class GameView extends JPanel
{
	JButton b1;
	BufferedImage turtle_image, chair_image, lamp_image, mushroom_image, outhouse_image, pillar_image, pond_image, rock_image, statue_image, tree_image;
	Model model;

	JButton saveButton;
	JButton loadButton;

	int horizontalScroll = 0;
	int verticalScroll = 0;
	
	BufferedImage[] images = new BufferedImage[10]; // Create an array for images
	int i = 0; // to be incremented

	public int time = 0;
	
	GameView(Controller c, Model m)
	{
		// Make buttons
		saveButton = new JButton("Save");
		saveButton.addActionListener(c);
		saveButton.setFocusable(false);
		this.add(saveButton);

		loadButton = new JButton("Load");
		loadButton.addActionListener(c);
		loadButton.setFocusable(false);
		this.add(loadButton);

		//Link up to other objects
		c.setView(this);
		model = m;

		// Send mouse events to the controller
		this.addMouseListener(c);

		
		// Load the all the images
		try
		{
			images[0] = ImageIO.read(new File("images/turtle.png"));
			images[1] = ImageIO.read(new File("images/chair.png"));
			images[2] = ImageIO.read(new File("images/lamp.png"));
			images[3] = ImageIO.read(new File("images/mushroom.png"));
			images[4] = ImageIO.read(new File("images/outhouse.png"));
			images[5] = ImageIO.read(new File("images/pillar.png"));
			images[6] = ImageIO.read(new File("images/pond.png"));
			images[7] = ImageIO.read(new File("images/rock.png"));
			images[8] = ImageIO.read(new File("images/statue.png"));
			images[9] = ImageIO.read(new File("images/tree.png"));
		} 
		catch(Exception e) {
			e.printStackTrace(System.err);
			System.exit(1);
		}
	}
	
	public int getHorizontalScroll()
	{
		return horizontalScroll;
	}

	public int getVerticalScroll()
	{
		return verticalScroll;
	}

	public void incrementValue()
	{
		i++;
		if (i == 10)
			i = 0;
	}
	
	public int getI()
	{
		return i;
	}

	public void scrollRight()
	{
		horizontalScroll += 5;
	}

	public void scrollLeft()
	{
		horizontalScroll -= 5;
	}

	public void scrollUp()
	{
		verticalScroll -= 5;
	}

	public void scrollDown()
	{
		verticalScroll += 5;
	}

	ArrayList<Thing> things = new ArrayList<Thing>();
	
	public void addThing(int x, int y, int kind)
	{
		if (kind == 0) // Check if it's a turtle (kind 0)
		{ 
        Jumper newTurtle = new Jumper(x + horizontalScroll, y + verticalScroll); // Create a Jumper for turtles
        things.add(newTurtle);
    	} 

		else 
		{
    	    Thing newThing = new Thing(x + horizontalScroll, y + verticalScroll, kind); // For other kinds, create a Thing
    	    things.add(newThing);
    	}
	}
	
	public void removeThing(int x, int y) 
	{
	    if (things.isEmpty()) 
	    {
	        return; // No Things to remove
	    }

	    int closestThingIndex = 0; // Initialize with the first Thing
	    double minDistance = distance(x, y, things.get(0).x, things.get(0).y);

	    // Iterate through the rest of the Things to find the closest one
	    for (int i = 1; i < things.size(); i++) 
	    {
	        double currentDistance = distance(x, y, things.get(i).x, things.get(i).y);
	        if (currentDistance < minDistance) 
	        {
	            minDistance = currentDistance;
	            closestThingIndex = i;
	        }
	    }

	    // Remove the closest Thing
	    things.remove(closestThingIndex);
	}

	// Helper method to calculate the Euclidean distance between two points
	private double distance(int x1, int y1, int x2, int y2) 
	{
	    return Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
	}
	
	public void paintComponent(Graphics g)
	{
		time++;

		// Clear the background
		g.setColor(new Color(32, 230, 25));
		g.fillRect(0, 0, this.getWidth(), this.getHeight());
		
		int pinkBoxX = 0; 
        int pinkBoxY = 0;
        int pinkBoxWidth = 200;
        int pinkBoxHeight = 200;
        
        g.setColor(new Color(208, 20, 250));
        g.fillRect(pinkBoxX, pinkBoxY, pinkBoxWidth, pinkBoxHeight);
		//draw current image index in top left static position
        g.drawImage(images[i], 40, -10, null);
        
        //draw images outside of purple box    	
        for (Thing thing : things)
        {
        	int thingX = thing.getThingsX() - horizontalScroll;
        	int thingY = thing.getThingsY(time) - verticalScroll;
        	int thingKind = thing.getThingsKind();
        	
        	g.drawImage(images[thingKind], thingX, thingY, null);
        }

		// Cast the Graphics object to Graphics2D
		Graphics2D g2d = (Graphics2D) g;
		// Set the font and color for the text
		Font font = new Font("Arial", Font.PLAIN, 18);
		g2d.setFont(font);
		g2d.setColor(Color.BLACK); // Choose your text color
		// Draw the text at position (x, y)
		String text = "Use keys to move around the map";
		int x1 = 300 - horizontalScroll; // Adjust the x-coordinate
		int y1 = 300 - verticalScroll; // Adjust the y-coordinate
		g2d.drawString(text, x1, y1);
	}

	public Json marshal()
	{
  		Json map = Json.newObject();
 		Json list_of_things = Json.newList();
		
  		for (Thing t : things)
  		{
			Json thingData = Json.newObject();
    		thingData.add("x", t.getThingsX());
            thingData.add("y", t.getThingsY(time));
            thingData.add("kind", t.getThingsKind());
            list_of_things.add(thingData);
  		}
		map.add("things", list_of_things);

  		return map;
	}

	// Method to load positions of objects from JSON
    public void unmarshal(Json jsonData) 
	{
        Json list_of_things = jsonData.get("things");

        // Clear the current list of things
        this.things.clear();

        // Iterate over the JSON data and create new Thing objects
        for (int i = 0; i < list_of_things.size(); i++) 
		{
            Json thingData = list_of_things.get(i);
            int x = (int)thingData.getLong("x");
            int y = (int)thingData.getLong("y");
            int kind = (int)thingData.getLong("kind");
            this.addThing(x, y, kind);
        }
    }

	public int getTime()
	{
		return time;
	}
	
	void removeButton()
	{
		
	}
}
