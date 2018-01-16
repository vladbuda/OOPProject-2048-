package game2048;

import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class Start 
{
	public static void main(String[] args) 
	{
		while(true)
		{
		String username = new String();
		username = JOptionPane.showInputDialog(null, "Enter your name", "2048", JOptionPane.PLAIN_MESSAGE);
		if(username == null) System.exit(1);
		if(!username.isEmpty())
		{
		 Game game = new Game(username);
		 
		 JFrame window = new JFrame("2048");	 
		 window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		 window.add(game);
		 window.setResizable(false);
		 window.pack();
		 Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		 window.setLocation(dim.width/2-window.getWidth()/2, dim.height/2-window.getHeight()/2);
		 window.setVisible(true);
		 game.starts();
		 break;
		}
		else JOptionPane.showMessageDialog(null, "You have to choose one", "2048", JOptionPane.PLAIN_MESSAGE);
		}
	}
}
