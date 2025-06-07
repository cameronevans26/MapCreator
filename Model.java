import java.util.ArrayList;
import java.util.Iterator;
import java.awt.image.BufferedImage;

class Model
{
	
	Model()
	{
		
	}

	public void update()
	{
		
	}

    public void reset()
    {
    
    }

}

class Thing
{
	protected int x;
	protected int y;
	public int kind;

	Thing(int x, int y, int kind)
	{
		this.x = x;
		this.y = y;
		this.kind = kind;
	}
	
	public int getThingsX()
	{
		return x;
	}
	public int getThingsY(int time)
	{
		return y;
	}
	public int getThingsKind()
	{
		return kind;
	}
	
}

class Jumper extends Thing 
{
    Jumper(int x, int y) 
	{
        super(x, y, 0); // Kind 0 represents turtles
    }

    // overriding
    public int getThingsY(int time) 
	{
		int t = time;
        // Implement jumping behavior for turtles
        double jumpHeight = 50 * Math.sin((double)t / 5);
        int newY = y - (int)Math.max(0.0, jumpHeight);
        return newY;
    }
}
	