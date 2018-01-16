package game2048;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
//This class implements the characteristics of a tile. It also implements the tile's animation and changes its color based on the value.
public class Tile 
{
	public static final int WIDTH = 80;
	public static final int HEIGHT = 80;
	public static final int SLIDE_SPEED = 20; //how many pixels we move on a direction in 
	//each board update
	public static final int ARC_WIDTH = 15;
	public static final int ARC_HEIGHT = 15;
	
	private int value;
	private BufferedImage tileImage;
	private Color background, text; //tile background and text color
	private Font font; //tile font
	private int x, y; //tile coordinates
	
	private boolean canCombine = true; //tells if a tile can combine or not (a tile can combine only once per slide)
	
	private boolean beginningAnimation = true;
	private double scaleFirst = 0.1;
	private BufferedImage beginningImage;
	
	public Tile(int value, int x, int y)
	{
		this.value = value;
		this.x = x;
		this.y = y;
		tileImage = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
		beginningImage = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
		drawImage();
	}
	
	private void drawImage() //draws the tile image (tile background color based on the value; fits the value string)
	{
		Graphics2D g = tileImage.createGraphics();
		switch (value) //choose background and font color based on the tile value
		{
		case 2:
		{
			background = new Color(233,233,233);
			text = new Color(0,0,0);
			break;
		}
		
		case 4:
		{
			background = new Color(255,249,229);
			text = new Color(0,0,0);
			break;
		}
		
		case 8:
		{
			background = new Color(247,157,61);
			text = new Color(255,255,255);
			break;
		}
		
		case 16:
		{
			background = new Color(242,128,7);
			text = new Color(255,255,255);
			break;
		}
		
		case 32:
		{
			background = new Color(245,94,59);
			text = new Color(255,255,255);
			break;
		}
		
		case 64:
		{
			background = new Color(255,18,18);
			text = new Color(255,255,255);
			break;
		}
		
		case 128:
		{
			background = new Color(233,222,132);
			text = new Color(255,255,255);
			break;
		}
		
		case 256:
		{
			background = new Color(246,235,115);
			text = new Color(255,255,255);
			break;
		}
		
		case 512:
		{
			background = new Color(245,228,85);
			text = new Color(255,255,255);
			break;
		}
		
		case 1024:
		{
			background = new Color(247,225,44);
			text = new Color(255,255,255);
			break;
		}
		
		case 2048:
		{
			background = new Color(255,228,0);
			text = new Color(255,255,255);
			break;
		}
		default:
		{
			background = Color.BLACK;
			text = Color.WHITE;
			break;
		}
	}
		
		g.setColor(new Color(0,0,0,0)); //RGB spectrum with alpha channel for transparency
		g.fillRect(0, 0, WIDTH, HEIGHT);
		
		g.setColor(background);
		g.fillRoundRect(0, 0, WIDTH, HEIGHT, ARC_WIDTH, ARC_HEIGHT); //draw filled rectangle with rounded edges
		
		g.setColor(text);
		
		if(value <= 64)
		{
			font = Game.main.deriveFont(36f); //apply size 36 to the main font
		}
		else font = Game.main; //use the main font
		g.setFont(font);
		int drawX = WIDTH / 2 - DrawUtils.getMessageWidth(""+value, font, g) / 2;
		int drawY = HEIGHT / 2 + DrawUtils.getMessageHeight(""+value, font, g) / 2;
		g.drawString("" + value, drawX, drawY);
		g.dispose();
	}
	
	public void update()
	{
		if(beginningAnimation)
		{
			AffineTransform transform = new AffineTransform();
			transform.translate(WIDTH / 2 - scaleFirst * WIDTH / 2, HEIGHT / 2 - scaleFirst * HEIGHT / 2); //translate the image from center to top left
			transform.scale(scaleFirst, scaleFirst); //scale the image with the scale factor
			Graphics2D g2d = beginningImage.createGraphics();
			g2d.setColor(new Color(0, 0, 0, 0));
			g2d.fillRect(0, 0, WIDTH, HEIGHT);
			g2d.drawImage(tileImage, transform, null);
			scaleFirst += 0.05;
			g2d.dispose();
			if(scaleFirst >= 1) beginningAnimation = false;
		}
	}
	
	public void render(Graphics2D g) //draws the tile to the board image at the specified coordinates
	{
		if(beginningAnimation) g.drawImage(beginningImage, x, y, null);
		else
		{
			g.drawImage(tileImage, x, y, null); //render the tile at the given coordinates
		}
	}
	
	public int getValue()
	{
		return value;
	}
	public void setValue(int value)
	{
		this.value = value;
		drawImage();
	}
	
	public boolean canCombine()
	{
		return canCombine;
	}
	
	public void setCanCombine(boolean canCombine)
	{
		this.canCombine = canCombine;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}
}
