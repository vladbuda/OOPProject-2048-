package game2048;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.ArrayList;
//This class draws the menu components to a buffered image which is later rendered to the jpanel.
public class Menu 
{
 public void main(Graphics g)
 {
		g.setColor(new Color(237,204,40));
		g.setFont(new Font("Calibri",Font.BOLD,70));
		g.drawString("2048", 130, 120);
		
		g.setColor(new Color(193, 168, 44));
		
		g.setFont(new Font("Calibri",Font.BOLD,36));
		
		g.drawRect(125, 170, 150, 50);
		g.drawRect(125, 250, 150, 50);
		g.drawRect(125, 330, 150, 50);
		g.drawRect(125, 410, 150, 50);
		
		g.setColor(new Color(127,116,61));
		g.drawString("Play", 169, 205);
		g.drawString("Records", 143, 285);
		g.drawString("Top 10", 153, 367);
		g.drawString("Exit", 170, 447);
 }
 
 public void background(Graphics g)
 {
	double then = System.currentTimeMillis();
	double current = 0;
	g.setColor(new Color(237,232,232,200));
		while(current< 30)
		{
			double now = System.currentTimeMillis();
			current += now - then;
			then = now;
		}
	g.fillRect(0, 0, Game.WIDTH+12, Game.HEIGHT+12);
 }
 
 public void dead(Graphics g, int score)
 {
	 g.setFont(new Font("Calibri", Font.BOLD, 56));
	 g.setColor(Color.RED);
	 g.drawString("GAME OVER", 56, 130);
	 
	 g.setColor(new Color(193, 168, 44));
	 g.drawRect(125, 220, 150, 50);
	 g.drawRect(125, 300, 150, 50);
	 g.drawRect(125, 380, 150, 50);
	 
	 g.setColor(new Color(127,116,61));
	 g.setFont(new Font("Calibri", Font.BOLD, 25));
	 g.drawString("Score: "+score, 133, 530); //56
	 g.setFont(new Font("Calibri", Font.BOLD, 36));
	 g.drawString("Retry", 158, 255);
	 g.drawString("Menu", 157, 335);
	 g.drawString("Exit", 172, 415);
 }
 
 public void won(Graphics g, int score)
 {
	 g.setFont(new Font("Calibri", Font.BOLD, 56));
	 g.setColor(new Color(237,204,40));
	 g.drawString("YOU WON", 56, 130);
	 
	 g.setColor(new Color(193, 168, 44));
	 g.drawRect(125, 220, 150, 50);
	 g.drawRect(125, 300, 150, 50);
	 g.drawRect(125, 380, 150, 50);
	 
	 g.setColor(new Color(127,116,61));
	 g.setFont(new Font("Calibri", Font.BOLD, 25));
	 g.drawString("Score: "+score, 133, 530); //56
	 g.setFont(new Font("Calibri", Font.BOLD, 36));
	 g.drawString("Retry", 158, 255);
	 g.drawString("Menu", 157, 335);
	 g.drawString("Exit", 172, 415);
 }
 
 public void records(Graphics g, int[] scores)
 {
	 g.setFont(new Font("Calibri", Font.BOLD, 36));
	 
	 g.setColor(new Color(237,204,40));
	 g.drawString("1. "+scores[0], 140, 150);
	 
	 g.setColor(new Color(127,116,61));
	 g.drawString("2. "+scores[1], 140, 230);
	 g.drawString("3. "+scores[2], 140, 310);
	 g.drawString("Back" , 160, 465);
	 
	g.setColor(new Color(193, 168, 44));
	 g.drawRect(120, 430, 150, 50);
}
 
 public void top10(Graphics g, ArrayList<String> scores)
 {
	 g.setFont(new Font("Calibri", Font.BOLD, 36));
	 g.setColor(new Color(237,204,40));
	 int y = 110;
	 g.drawString("1. "+scores.get(0), 70, 75);
	 g.setColor(new Color(127,116,61));
	 for(int i = 1; i < scores.size(); i++)
	 {
		 g.drawString(String.format("%d. %s", i+1, scores.get(i)), 70, y);
		 y+=35;
	 }
	 g.drawString("Back" , 150, 475);
	 
	 g.setColor(new Color(193, 168, 44));
	 g.drawRect(110, 440, 150, 50);
 }
}