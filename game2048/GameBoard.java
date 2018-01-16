package game2048;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Random;
//This class implements the game logic and also creates the board image.
public class GameBoard 
{
	public static int ROWS = 4, COLS = 4;
	
	private final int startingTiles = 2; //number of starting tiles
	private Tile[][] board;
	private boolean dead, won;
	private BufferedImage gameBoard, finalBoard;
	
	private int x, y; //coordinates to draw the board
	
	private static int SPACING = 10;
	public static int BOARD_WIDTH = (COLS+1) * SPACING + COLS * Tile.WIDTH; //cols+1 spacing between the column tile
	//plus the number of column tiles
	public static int BOARD_HEIGHT = (ROWS+1) * SPACING + ROWS * Tile.HEIGHT; //same
	public boolean left, right, up, down;
	
//	private boolean flag = false;
	
	private int score = 0;
	private int best;
	public GameBoard(int x, int y)
	{
		this.x = x;
		this.y = y;
		board = new Tile[ROWS][COLS];
		gameBoard = new BufferedImage(BOARD_WIDTH, BOARD_HEIGHT, BufferedImage.TYPE_INT_RGB);
		finalBoard = new BufferedImage(BOARD_WIDTH, BOARD_HEIGHT, BufferedImage.TYPE_INT_RGB);
		createBoardImage(); //draw on generated boards
		start();
	}
	
	private void createBoardImage() //draws the game board background along with each tile's background
	{
		Graphics2D g = (Graphics2D) gameBoard.getGraphics();
		g.setColor(Color.DARK_GRAY); //set board background color
		g.fillRect(0, 0, BOARD_WIDTH, BOARD_HEIGHT);//draw the gray background
		g.setColor(Color.LIGHT_GRAY); //set tile background color
		
		for(int row = 0; row < ROWS; row++) //draw each tile background in light gray
			for(int col = 0; col < COLS; col++)
			{ 
				int x = (col + 1) * SPACING + Tile.WIDTH * col; //get the tile coordinates
				int y = (row + 1) * SPACING + Tile.HEIGHT * row;
				g.fillRoundRect(x, y, Tile.WIDTH, Tile.HEIGHT, Tile.ARC_WIDTH, Tile.ARC_HEIGHT);
			}//draw tile background with rounded corners
	}
	
	private void start() //spawns the first 2 tiles at random positions
	{
		for(int i = 0; i < startingTiles; i++) //spawn first 2 tiles
			spawnRandom();
	}
	
	private void spawnRandom() //finds a random free position to spawn a new tile
	{
		Random random = new Random();
		boolean notValid = true;
		while (notValid)	//while we haven't found free space for a new tile
		{
			int location = random.nextInt(ROWS*COLS); //find a random location
			int row = location / ROWS; //compute row value
			int col = location % COLS;	//compute column value
			Tile current = board[row][col];	
			if(current == null)	//check if the found location is empty
			{
				int value = random.nextInt(10) < 7 ? 2 : 4; //if empty decide which value to choose (70% chance for 2)
				Tile tile = new Tile(value, getTileX(col), getTileY(row)); //create the new tile
				board[row][col] = tile; //save the tile at the found location
				notValid = false;
			}
		}
	}
	
	public int getTileX(int col) //computes the new tile's x coordinate based on the column number and spacing
	{
		return SPACING + col * Tile.WIDTH + col * SPACING; 
	}
	
	public int getTileY(int row) //same for the row number
	{
		return SPACING + row * Tile.HEIGHT + row * SPACING;
	}
	
	public void render(Graphics2D g) //renders the board image along with the current tiles
	{
		Graphics2D g2d = (Graphics2D) finalBoard.getGraphics();
		g2d.drawImage(gameBoard, 0, 0, null);
		
		for(int row = 0; row < ROWS; row++) //render the ready tiles from board
			for(int col = 0; col < COLS; col++)
				{
					Tile current = board[row][col];
					if(current == null) continue; //if no tile continue
					current.render(g2d); //render the current tile
				}
		g.setColor(new Color(127,116,61));
		g.setFont(new Font("Calibri",Font.BOLD,28));
		g.drawString("Score: "+score, 20, 120);
		g.setColor(new Color(237,204,40));
		g.drawString("Best: "+best, 20, 55);
		g.drawImage(finalBoard, x, y, null); //draw the final board image
		g2d.dispose();
	}
	
	public void update()
	{
		checkKeys();
		up = down = right = left = false;
		for(int row = 0; row < ROWS; row++)
			
			for(int col = 0; col < COLS; col++)
			{
				Tile current = board[row][col];
				if(current == null)	continue;
				current.update();
				resetPosition(current, row, col);
				if(current.getValue() == 2048) won = true;
			}
	}
	
	private void resetPosition(Tile current, int row, int col) //responsible with moving the tile
	//on the board; moveTile function just moves the tile in the board array, but does not
	//change the previous x, y coordinate of the new tile
	{
		//if(current == null) return;
		int x = getTileX(col); //takes the current tile's x coordinate on the board (where the
		//tile should be and it's different than the actual x, y values of the tile)
		int y = getTileY(row); //same for y
		
		int distX = current.getX() - x; //find the move distance along de x and y axis, i.e.
		//the difference between the previous x, y values (stored in the tile's coordinates)
		//and the new, real coordinates of the tile found above based on the new position
		//in the board matrix; positive if we move to the left and negative to the right
		int distY = current.getY() - y; //positive if we move up and negative if we move down
	
		if(Math.abs(distX) < Tile.SLIDE_SPEED) //if the distance to move is smaller than the
		//tile speed, move the tile directly to x
			{
				current.setX(x);
			}
		else //else check if the tile must move left or right and add or subtract the slide_speed
			//from the current x value
			{
				if(distX < 0) current.setX(current.getX() + Tile.SLIDE_SPEED); //move to the right, add distance
				if(distX > 0) current.setX(current.getX() - Tile.SLIDE_SPEED); //move to the left, subtract distance
			}
		
		if(Math.abs(distY) < Tile.SLIDE_SPEED) //same, but for the y axis
			{
				current.setY(y);
			}
		else
			{
				if(distY < 0) current.setY(current.getY() + Tile.SLIDE_SPEED); //move down
				if(distY > 0) current.setY(current.getY() - Tile.SLIDE_SPEED); //move up
			}
	}
	
	private boolean checkOutOfBounds(Direction d, int row, int col) //returns true if the tile's new position is out of bound
	{
		return (row>ROWS-1 || row <0 || col < 0 || col>COLS-1);
	}
	
	private boolean move(int row, int col, int horizontalDirection, int verticalDirection, Direction d)
	{
		boolean canMove = false;
		Tile current = board[row][col];
		if(current == null) return false;
		int x,y;
		x = board[row][col].getX(); //save current tile original x y coordinates
		y = board[row][col].getY();
		boolean move = true;
		int newCol = col, newRow = row;
		while(move)
		{
			newCol += horizontalDirection; //compute the new position of the tile to be moved
			newRow += verticalDirection;
			if(checkOutOfBounds(d, newRow, newCol)) break; //check if the tile's new position is out of bound
			
			if(board[newRow][newCol] == null) //if the new location is empty
			{
				board[newRow][newCol] = current; //move the tile to the new location
				board[newRow - verticalDirection][newCol - horizontalDirection] = null; //empty the old location (set it to null)
			//	board[newRow][newCol].setSlideTo(new Point(newRow, newCol)); //set slide to the new tile's coordinates
				canMove = true;
			}
			else if(board[newRow][newCol].getValue() == current.getValue() && board[newRow][newCol].canCombine()) //check if the current tile and the tile on the new position has the same value; 
				//check if the tile at the new position can combine, i.e hasn't combined with another tile during this slide
			{
				board[newRow][newCol].setCanCombine(false); //set can combina to false; can no longer combine during this slide
				board[newRow][newCol].setValue(board[newRow][newCol].getValue() * 2); //double the value of the tile on the new position
				canMove = true;
				//board[newRow][newCol].setX(board[newRow - verticalDirection][newCol - horizontalDirection].getX());
				//board[newRow][newCol].setY(board[newRow - verticalDirection][newCol - horizontalDirection].getY());
				board[newRow][newCol].setX(x); //in case of combining, set the x y coordinates of the new tile to the value
				//of the tile to combine with (for animation purpose)
				board[newRow][newCol].setY(y);
				board[newRow - verticalDirection][newCol - horizontalDirection] = null; //set old tile to empty
			//	board[newRow][newCol].setSlideTo(new Point(newRow, newCol));
				score += board[newRow][newCol].getValue();
			}
			else move = false;
		}
		return canMove;
	}
	
	private boolean moveTiles(Direction d)
	{
		boolean canMove = false;
		int horizontalDirection = 0;
		int verticalDirection = 0;
		
		if(d == Direction.LEFT) 
		{
			horizontalDirection = -1; //move current tile to the left with 1 position, i.e subtract 1 from the current column value
			for(int row = 0; row < ROWS; row++)
				for(int col = 0; col < COLS; col++) //iterate from left to right on the horizontal axis
				{
					if(!canMove) canMove = move(row, col, horizontalDirection, verticalDirection, d); //if the previous tile can't move, check for the current one
					else move(row, col, horizontalDirection, verticalDirection, d); //if the previous tile moved, so can the current one no matter what
				}
		}
		
		else if(d == Direction.RIGHT)
		{
			horizontalDirection = 1; //move current tile to the right with 1 pos, add 1 to the current row value
			for(int row = 0; row < ROWS; row++)
				for(int col = COLS - 1; col >= 0; col--) //iterate from right to left on the horizontal axis in order to avoid blank spaces between tiles
				{
					if(!canMove) canMove = move(row, col, horizontalDirection, verticalDirection, d);
					else move(row, col, horizontalDirection, verticalDirection, d);
				}
		}
		
		else if(d == Direction.UP)
		{
			verticalDirection = -1; //move current tile up, subtract 1 from the row value
			for(int row = 0; row < ROWS; row++) //iterate from up to bottom on the vertical axis
				for(int col = 0; col < COLS; col++)
				{
					if(!canMove) canMove = move(row, col, horizontalDirection, verticalDirection, d);
					else move(row, col, horizontalDirection, verticalDirection, d);
				}
		}
		
		else if(d == Direction.DOWN)
		{
			verticalDirection = 1; //move tile down with 1 position, add 1 to the row value
			for(int row = ROWS - 1; row >= 0; row--) //iterate from bottom to the top on the vertical axis
				for(int col = 0; col < COLS; col++)
				{
					if(!canMove) canMove = move(row, col, horizontalDirection, verticalDirection, d);
					else move(row, col, horizontalDirection, verticalDirection, d);
				}
		}
		
		else System.out.println("Not a valid direction");
		
		for(int row = 0; row < ROWS; row++) //any tile can combine once again after a move was done
			for(int col = 0; col < COLS; col++)
			{
				Tile current = board[row][col];
				if(current == null) continue;
				current.setCanCombine(true);
			}
		if(canMove) //if a move was performed spawn a new tile and check if dead
			{
				spawnRandom();
				checkWon();
				checkDead();
			}
		return canMove;
	}
	private void checkWon()
	{
		for(int row = 0; row < ROWS; row++)
			for(int col = 0; col < COLS; col++)
			{
				if(board[row][col] != null)
				{
					if(board[row][col].getValue() == 2048) won = true;
				}
			}
	}
	
	private void checkDead() //checks if the player can no longer make moves; returns true if there is an empty space or a tile can combine with a surrounding one
	{
		for(int row = 0; row < ROWS; row++)
			for(int col = 0; col < COLS; col++)
			{
				if(board[row][col] == null) return;
				if(checkSurroundingTiles(row, col, board[row][col])) return;
			}
		dead = true;
		//setHighScore(score);
	}
	
	
	private boolean checkSurroundingTiles(int row, int col, Tile current) //returns true if the current tile can combine with a surrounding tile or there is an empty space nearby
	{
		if(row > 0) //checks the upper tile
		{
			Tile check = board[row - 1][col];
			if(check == null) return true;
			if(current.getValue() == check.getValue()) return true;
		}
		if(row < ROWS - 1) //checks the lower tile
		{
			Tile check = board[row + 1][col];
			if(check == null) return true;
			if(current.getValue() == check.getValue()) return true;
		}
		
		if(col > 0) //checks the left tile
		{
			Tile check = board[row][col - 1];
			if(check == null) return true;
			if(current.getValue() == check.getValue()) return true;
		}
		
		if(col < COLS - 1) //checks the right tile
		{
			Tile check = board[row][col + 1];
			if(check == null) return true;
			if(current.getValue() == check.getValue()) return true;
		}
		return false;
	}
	
	public void retry()
	{
		board = new Tile[ROWS][COLS];
		score = 0;
		start();
	}
	
	public boolean getDead()
	{
		return dead;
	}
	
	public void setDead(boolean val)
	{
		dead = val;
	}
	
	public boolean getWon()
	{
		return won;
	}
	
	public void setWon(boolean val)
	{
		won = val;
	}
	public int getScore()
	{
		return score;
	}
	
	public void setBest(int best)
	{
		this.best = best;
	}
	
	private void checkKeys()
	{
		if(left)
		{
			moveTiles(Direction.LEFT);
			//System.out.println("left");
		}
		if(right)
		{
			moveTiles(Direction.RIGHT);
			//System.out.println("right");
		}
		if(up)
		{
			moveTiles(Direction.UP);
			//System.out.println("up");
		}
		if(down)
		{
			moveTiles(Direction.DOWN);
			//System.out.println("down");
		}
	}
}
