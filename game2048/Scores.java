package game2048;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
//This class is responsible with the connection to the database and also the management of scores.
public class Scores {

	private Connection con; //connection to database
	
	public Scores()
	{
			con = getConnection();
			createTable(con);
	}
	
	public int getBest(String name) //returns the best score of a specified player
	{
		try
		{
			Statement statement = con.createStatement();
			ResultSet result = statement.executeQuery("Select first FROM players WHERE name='"+name+"'");
			while(result.next()) return result.getInt("first");
		}
		
		catch(Exception e)
		{
			System.out.println(e);
		}
		return 0;
	}
	
	public ArrayList<String> getTop10() //returns an array list of top 10 players
	{
		ArrayList<String> top10 = new ArrayList<String>();
		try
		{
			Statement statement = con.createStatement();
			ResultSet result = statement.executeQuery("SELECT name, first FROM players ORDER BY first DESC LIMIT 10");
			while(result.next())
			{
				top10.add(String.format("%-10s %-8d", result.getString("name"), result.getInt("first")));
			}
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
		return top10;
	}
	
	public int[] getScore(String name) //returns all 3 scores of a specified player
	{
		int[] score = new int[3];
		try
		{
			Statement statement = con.createStatement();
			ResultSet result = statement.executeQuery("SELECT first, second, third FROM players WHERE name='"+name+"'");
			while(result.next())
			{
				score[0] = result.getInt("first");
				score[1] = result.getInt("second");
				score[2] = result.getInt("third");
			}
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
		return score;
	}
	
	public void addScore(String name, int score) //adds score to a player
	{
		try
		{
		Statement statement =  con.createStatement();
		String st = "Select third FROM players WHERE name='"+name+"'"; //gets the third best score
		ResultSet result = statement.executeQuery(st);
		int first = 0, second = 0, third = 0;
		while(result.next())
		{
			third = result.getInt("third");
		}
		if(score > third) //check if current score better than the third best score
		{
			st = "Select second FROM players WHERE name='"+name+"'";
			result = statement.executeQuery(st);
			while(result.next())
				{
					second = result.getInt("second"); //gets the second best score
				}
			if(score > second) //checks if current score better than the second best
			{
				st = "Select first FROM players WHERE name='"+name+"'";
				result = statement.executeQuery(st);
				while(result.next())
					{
						first = result.getInt("first"); //gets the best score
					}
				if(score > first) //checks if current score bigger than the best
					{
						statement.executeUpdate("UPDATE players SET first = '"+score+"' WHERE name ='"+name+"'"); //puts the current score in "first" column
						statement.executeUpdate("UPDATE players SET second = '"+first+"' WHERE name ='"+name+"'"); //moves "first" to "second"
						statement.executeUpdate("UPDATE players SET third = '"+second+"' WHERE name ='"+name+"'"); //moves "second" to "third"
					}
				else
					{
						statement.executeUpdate("UPDATE players SET second = '"+score+"' WHERE name ='"+name+"'"); //updates "second" with the current score
						statement.executeUpdate("UPDATE players SET third = '"+second+"' WHERE name ='"+name+"'"); //updates "third" with the "second" score
					}
			}
			else
				{
					statement.executeUpdate("UPDATE players SET third = '"+score+"' WHERE name ='"+name+"'"); //adds current to "third" column
				}
		}
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
	}
	
	public void addEntry(String name) //checks if an entry already exists and adds it otherwise
	{
		try
		{
		Statement statement = con.createStatement();
		String st = "SELECT * FROM players WHERE name='"+name+"'";
		ResultSet result = statement.executeQuery(st);
		if(!result.next()) statement.executeUpdate("INSERT INTO players (name, first, second, third) VALUES ('"+name+"', 0, 0, 0)"); 
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
	}
	
	 private void createTable(Connection con) //creates the table with the specified format
	{
		try
		{
			PreparedStatement create = con.prepareStatement("CREATE TABLE IF NOT EXISTS players(id int NOT NULL AUTO_INCREMENT, name varchar(255), first int, second int, third int, PRIMARY KEY(id));");
			create.executeUpdate();
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
	}
	 
	private Connection getConnection() //connects to the database
	{
		
		try 
		{
			String driver = "com.mysql.jdbc.Driver";
			String url = "jdbc:mysql://localhost:3306/database2048";
			String username = "root";
			String password = "parola1";
			Class.forName(driver);
			
			Connection conn = DriverManager.getConnection(url, username, password);
			return conn;
		}
		
		catch(Exception e)
		{
			System.out.print(e);
		}
		
		return null;
	}
}
