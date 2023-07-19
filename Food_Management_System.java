package Project;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

class FoodNotFoundException extends RuntimeException{
	FoodNotFoundException(String message){
		super(message);
	}
}

class ColumnNotFoundException extends RuntimeException{
	ColumnNotFoundException(String message){
		super(message);
	}
}

class IDUpdationException extends RuntimeException{
	IDUpdationException(String message){
		super(message);
	}
}

class InvalidRatingException extends RuntimeException{
	InvalidRatingException(String message){
		super(message);
	}
}

class Project{
	private static Statement st;
	private static PreparedStatement ps;
	private static ResultSet rs;
	private final static String driver="com.mysql.cj.jdbc.Driver";
	private final static String url="jdbc:mysql://localhost:3306/aditya";
	private final static String username="root";
	private final static String password="mysql@123456";
	
	static Connection getConnection() throws ClassNotFoundException, SQLException {
		Class.forName(driver);
		Connection c=DriverManager.getConnection(url,username,password);
		return c;
	}
	
	static void fetch(Connection c) throws SQLException {
		st=c.createStatement();
		rs=st.executeQuery("Select * from Food");
		while(rs.next()) {
			System.out.println(rs.getInt(1)+" "+rs.getString(2)+" "+rs.getInt(3)+" "+rs.getInt(4));
		}
	}
	
	static void insert(Connection c,Scanner sc) throws SQLException {
		System.out.println("Enter food id");
		int id=sc.nextInt();
		sc.nextLine();
		System.out.println("Enter food name");
		String name=sc.nextLine();
		System.out.println("Enter food price");
		int price=sc.nextInt();
		System.out.println("Enter food rating from 1 to 10");
		int rating=sc.nextInt();
		if(rating>=1&&rating<=10) {
			st=c.createStatement();
			ps=c.prepareStatement("Insert into Food value(?,?,?,?)");
			ps.setInt(1,id);
			ps.setString(2,name);;
			ps.setInt(3,price);
			ps.setInt(4,rating);
			int res=ps.executeUpdate();
			if(res>0) {
				System.out.println("Food information added successfully!");
			}
			else {
				System.out.println("Some issue occurred! Try again!");
			}
		}
		else {
			throw new InvalidRatingException("Rating should be from 1 to 10");
		}
	}
	
	static void update(Connection c,Scanner sc) throws SQLException {
		System.out.println("Enter id to be updated");
		int id=sc.nextInt();
		st=c.createStatement();
		rs=st.executeQuery("Select * from Food where id="+id);
		if(rs.next()) {
			System.out.println("Enter the column number to be updated");
			int col=sc.nextInt();
			if(col==1) {
				throw new IDUpdationException("You cannot update the food id");
			}
			else if(col==2) {
				sc.nextLine();
				System.out.println("Enter food name to be updated");
				String name=sc.nextLine();
				ps=c.prepareStatement("Update Food set name=? where id=?");
				ps.setString(1, name);
				ps.setInt(2, id);
				int res=ps.executeUpdate();
				if(res>0) {
					System.out.println("Record updated successfully!");
				}
				else {
					System.out.println("Failed to update!");
				}
			}
			else if(col==3) {
				System.out.println("Enter food price to be updated");
				int price=sc.nextInt();
				ps=c.prepareStatement("Update Food set price=? where id=?");
				ps.setInt(1, price);
				ps.setInt(2, id);
				int res=ps.executeUpdate();
				if(res>0) {
					System.out.println("Record updated successfully!");
				}
				else {
					System.out.println("Failed to update");
				}
			}
			else if(col==4) {
				System.out.println("Enter food rating to be updated");
				int rating=sc.nextInt();
				if(rating>=1&&rating<=10) {
					ps=c.prepareStatement("Update Food set rating=? where id=?");
					ps.setInt(1, rating);
					ps.setInt(2, id);
					int res=ps.executeUpdate();
					if(res>0) {
						System.out.println("Record updated successfully!");
					}
					else {
						System.out.println("Failed to update!");
					}
				}
				else {
					throw new InvalidRatingException("Rating should be from 1 to 10"); 
				}
			}
			else {
				throw new ColumnNotFoundException("No column "+col+" found!");
			}
		}
		else {
			throw new FoodNotFoundException("No food found!");
		}
	}
	
	static void delete(Connection c,Scanner sc) throws SQLException {
		System.out.println("Enter food id to delete");
		int id=sc.nextInt();
		st=c.createStatement();
		rs=st.executeQuery("Select * from Food where id="+id);
		if(rs.next()) {
			ps=c.prepareStatement("Delete from Food where id=?");
			ps.setInt(1,id);
			int res=ps.executeUpdate();
			if(res>0) {
				System.out.println("Record deleted successfully!");
			}
			else {
				System.out.println("Failed to delete!");
			}
		}
		else {
			throw new FoodNotFoundException("Food Not Found!");
		}
	}
}

public class Food_Management_System {
	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		Connection conn=Project.getConnection();
		boolean isExit=false;
		System.out.println("============== Food Management System ==============");
		while(!isExit) {
			System.out.println("============================");
			System.out.println("Enter your choice");
			System.out.println("1.Insert\n2.Update\n3.Delete\n4.Fetch\n5.Exit");
			System.out.println("============================");
			Scanner sc=new Scanner(System.in);
			int choice=sc.nextInt();
			switch(choice) {
				case 1:{
					Project.insert(conn,sc);
					break;
				}
				case 2:{
					Project.update(conn,sc);
					break;
				}
				case 3:{
					Project.delete(conn,sc);;
					break;
				}
				case 4:{
					Project.fetch(conn);
					break;
				}
				case 5:{
					System.out.println("Thank You!");
					System.exit(0);
				}
				default:{
					System.out.println("Please enter valid choice");
				}
			}
		}
	}
}