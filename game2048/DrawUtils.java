package game2048;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.font.TextLayout;
import java.awt.geom.Rectangle2D;

public class DrawUtils //used for getting message dimensions for fitting in the graphic elements 
{
	public static int getMessageWidth(String message, Font font, Graphics2D g)
	{
		g.setFont(font);
		Rectangle2D bounds = g.getFontMetrics().getStringBounds(message, g); //constructs a 
		//rectangle2d based on the font metrics and string bounds, i.e. returns a bounding box
		//of the specified message in the specified graphic context
		return (int)bounds.getWidth(); //return the width of the bounding box
	}
	
	public static int getMessageHeight(String message, Font font, Graphics2D g)
	{
		g.setFont(font);
		if(message.length()==0) return 0;
		TextLayout tl = new TextLayout(message, font, g.getFontRenderContext());//constructs
		//a text layout based on the paragraph of text, font and the render context specified
		//by graphics2d object, fontRenderContext object contains the information needed to
		//correctly measure text
		return (int)tl.getBounds().getHeight(); //returns the height of the rectangle2d
		//constructed based on the message, font and render context 
		
		//try this one later when testing
		/*
		 Rectangle2D bounds = g.getFontMetrics().getStringBounds(message, g);
		 return (int)bounds.getHeight();
		 */
	}
}
