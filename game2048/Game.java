package game2048;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JPanel;
//This class is responsible with updating and rendering of the game. It's also responsible for user inputs and interactions between game's classes.

public class Game extends JPanel implements KeyListener, MouseListener, Runnable
{
	private static final long serialVersionUID = 1L;
	public static final int WIDTH = GameBoard.BOARD_WIDTH + 20;
	public static final int HEIGHT = GameBoard.BOARD_HEIGHT + 170; //260
	public static final Font main = new Font("Calibri", Font.BOLD, 28);
	private Thread game;
	private boolean running;
	private BufferedImage image = new BufferedImage(WIDTH+12, HEIGHT+12, BufferedImage.TYPE_INT_ARGB); 
	//image with the specified dimensions and 8 bit color depth
	
	private GameBoard board;
	private Menu menu;
	
	
	private enum State
	{
		MENU, GAME, RECORDS, TOP10, DEAD, WON, QUIT
	};
	
	private State state = State.MENU;
	private boolean once = true;
	private String player;
	private Scores score;
	
	public Game(String player)
	{
		setFocusable(true);
		setPreferredSize(new Dimension(WIDTH,HEIGHT));
		addKeyListener(this);
		addMouseListener(this);
		
		this.player = player;
		score = new Scores();
		score.addEntry(this.player);
		
		//board = new GameBoard(10, 162); //245
		board = new GameBoard(16, 167);
		board.setBest(score.getBest(player));
		menu = new Menu();	
	}

	private void update()
	{
		if(state == State.GAME)	board.update();
		if(board.getDead()) 
			{
				state = State.DEAD;
				score.addScore(player, board.getScore()); //when dead add the score to the database if top 3
				board.setDead(false);
			}
		if(board.getWon())
		{
			state = State.WON;
			score.addScore(player, board.getScore());
			board.setWon(false);
		}
	}
	
	private void render()
	{
		if(state == State.MENU) //renders the main menu
		{
			Graphics2D g = image.createGraphics();
			menu.background(g);
			repaint();
			menu.main(g);
			g.dispose();
		}
		else if (state == State.GAME) //renders the game
		{
			Graphics2D g = (Graphics2D) image.getGraphics();
			g.setColor(new Color(237,232,232));
			g.fillRect(0, 0, WIDTH+12, HEIGHT+12);
			board.render(g);
			g.dispose();
		}
		else if(state == State.DEAD) //renders 
		{
			if(once == true)
			{
			Graphics2D g = image.createGraphics();
			for(int i=0; i<15; i++) //controls the delay for the dead menu
			{
				menu.background(g);
				repaint(); //paints the background first and then paints the text
			}
			once = false;
			menu.dead(g, board.getScore());
			g.dispose();
			}
		}
		else if(state == State.WON) //renders 
		{
			if(once == true)
			{
			Graphics2D g = image.createGraphics();
			for(int i=0; i<15; i++) //controls the delay for the dead menu
			{
				menu.background(g);
				repaint(); //paints the background first and then paints the text
			}
			once = false;
			menu.won(g, board.getScore());
			g.dispose();
			}
		}
		else if(state == State.RECORDS) //renders the personal records
		{
			Graphics2D g = image.createGraphics();
			int[] records = score.getScore(player);
			menu.background(g);
			repaint();
			menu.records(g, records);
		}
		else if(state == State.TOP10) //renders top 10
		{
			Graphics2D g = image.createGraphics();
			ArrayList<String> scores = score.getTop10();
			menu.background(g);
			repaint();
			menu.top10(g, scores);
		}
		else System.exit(0);
		repaint();
	}
		
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		g.drawImage(image, 0, 0 , null); //paints the final image to jpanel
	}
	
	@Override
	public void run() 
	{
		double then = System.nanoTime(); //get the current time in nanoseconds
		double current = 0;
		
		while(running)
		{
			double now = System.nanoTime();
			current += now - then;
			then = now;
			if(current >= 16666666.66666667) //condition for 60 frames/updates per second
			{
				update();
				render();
				current = 0;
			}
		}
	}

	public synchronized void starts() //starts the thread
	{
		if(running) return;
		running = true;
		game = new Thread(this, "game");
		game.start();
	}
	
	public synchronized void stop() //stops the thread
	{
		if(!running) return;
		running = false;
		System.exit(0);
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_LEFT && state == State.GAME) board.left = true;
		else if(e.getKeyCode() == KeyEvent.VK_RIGHT && state == State.GAME) board.right = true;
		else if(e.getKeyCode() == KeyEvent.VK_UP && state == State.GAME) board.up = true;
		else if(e.getKeyCode() == KeyEvent.VK_DOWN && state == State.GAME) board.down = true;
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		
		
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		
	}
	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseReleased(MouseEvent e) 
	{
		int mx = e.getX();
		int my = e.getY();
		
		if(state == State.MENU)
		{
			//g.drawRect(125, 170, 150, 50); //start button
			if(mx >= 125 && mx <= 275)
				{
					if(my >= 170 && my <= 220)
					{
						state = State.GAME;
						once = true;
						board.setBest(score.getBest(player));
						board.retry();
					}
				}
		//g.drawRect(125, 410, 150, 50); //quit button
		
			if(mx >= 125 && mx <= 275)
				{
					if(my >= 410 && my <= 460)
						state = State.QUIT;
				}
			
			//g.drawRect(125, 250, 150, 50); //records button
			
			if(mx >= 125 && mx <= 275)
			{
				if(my >= 250 && my <= 300)
					state = State.RECORDS;
			}
			
			//g.drawRect(125, 330, 150, 50); //top10 button
				
			if(mx >= 125 && mx <= 275)
			{
				if(my >= 330 && my <= 380)
					state = State.TOP10;
			}
			
		}
		
		if(state == State.DEAD || state == State.WON)
		{
		//	g.drawRect(125, 300, 150, 50); //retry button
			if(mx >= 125 && mx <= 275)
			{
				if(my >= 220 && my <= 270)
				{
					state = State.GAME;
					once = true;
					board.setBest(score.getBest(player));
					board.retry();
				}
				if(my >= 300 && my <= 350) state = State.MENU;
				
				// g.drawRect(125, 380, 150, 50); //quit button
				if(my >= 380 && my <= 430) state = State.QUIT;
			}
			
		}
		
		if(state == State.RECORDS)
		{
			//g.drawRect(120, 430, 150, 50); //back button
			if(mx >= 120 && mx <= 270)
			{
				if(my >= 430 && my <= 480) state = State.MENU;
			}
			
		}
		
		if(state == State.TOP10)
			 //g.drawRect(110, 440, 150, 50); //back button
			if(mx >= 110 && mx <= 260)
			{
				if(my >= 440 && my <= 490) state = State.MENU;
			}
	}
}
