package com.develop.ShowDateAndTime;
import java.sql.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import javax.ws.rs.core.MediaType;

import com.develop.ShowDateAndTime.DataAccessObjects.StockDetails;


public class SqlDAL {
	static String url="jdbc:mysql://localhost:3306/alpha";
	static String uname="root";
	static String password="mysql";
	static ResultSet rs=null;
	static Connection con = null;
	static Statement st= null;
	static long diff=0;
	//Class.forName("com.mysql.jdbc.Driver");

	public static  boolean authenticateUser(String name,String pass) throws SQLException, ClassNotFoundException {
		Boolean val=false;
		try {
			StartConnection();
			
			String a="";
			rs=st.executeQuery("Select * from user where username='"+name+"' and apikey='"+pass+"'");
			val=rs.next();
		}
		finally {
			if(con!=null)
				con.close();
			if(st!=null)
			    st.close();		
		}
		return val;
	}
	
	private static void StartConnection() throws ClassNotFoundException, SQLException
	{
		Class.forName("com.mysql.jdbc.Driver");
		con = DriverManager.getConnection(url,uname,password);
		st=con.createStatement();
	}
	
	private static void StopConnection() throws ClassNotFoundException, SQLException
	{
		if(st!=null && !st.isClosed())
		    st.close();
		
		if(con!=null && !con.isClosed())
			con.close();
	}

	public static StockDetails retrieveDaily(String company) throws SQLException, ClassNotFoundException 
	{
		try {
			StartConnection();
			ResultSet rs=st.executeQuery("Select * from ResponseDataCache where ID=(select ID from Company where Name='"+company+"') and APIID=(Select ID from API where APIType='Daily') ");
			
			StockDetails sd=new StockDetails();
			sd.setCompany(company);
			
			if(rs.next()) {
				sd.setDetails(rs.getString(3));
				sd.setLastAccess(rs.getObject(4, LocalDateTime.class));
			}
			return sd;
		}
		finally {
			StopConnection();
		}
	}
	
	public static void queryUpdate(String sql) throws SQLException {
		try {
			st.executeUpdate(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
			st.close();
			con.close();
		}
		return;
	}
	
	public static void updateOrInsertDaily(boolean isInsert, StockDetails stockDetails) throws SQLException, ClassNotFoundException {
		try {
			String sql="";
			StartConnection();
			//If database information is not present
			if(isInsert)
			{
				insertCompany(stockDetails);
				sql = "insert into ResponseDataCache(ID,APIID,DATA,lastAccess) values((select ID from Company where Name='"+stockDetails.getCompany()+"'),(select ID from api where APIType='Daily'),'"+stockDetails.getDetails()+"',now())";
			}
			//If database information is expired
			else
			{
				sql = "Update ResponseDataCache set Data='"+stockDetails.getDetails()+"',lastAccess=now() where ID=(Select ID from Company where Name='"+stockDetails.getCompany()+"') and APIID=(Select ID from API where APIType='IntraDaily')";
			}
			st.executeUpdate(sql);
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
			StopConnection();
		}
		return;
	}

	public static void updateOrInsertIntraDaily(boolean isInsert, StockDetails stockDetails) throws SQLException, ClassNotFoundException {
		try {
			String sql="";
			StartConnection();
			//If database information is not present
			if(isInsert)
			{
				insertCompany(stockDetails);
				sql = "insert into ResponseDataCache(ID,APIID,DATA,lastAccess) values((select ID from Company where Name='"+stockDetails.getCompany()+"'),(select ID from api where APIType='IntraDaily'),'"+stockDetails.getDetails()+"',now())";
			}
			//If database information is expired
			else
			{
				sql = "Update ResponseDataCache set Data='"+stockDetails.getDetails()+"',lastAccess=now() where ID=(Select ID from Company where Name='"+stockDetails.getCompany()+"') and APIID=(Select ID from API where APIType='Daily')";
			}
			st.executeUpdate(sql);
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
			StopConnection();
		}
		return;
	}
	
	public static void insertCompany(StockDetails stockDetails) throws SQLException {
		try {
			String sql="";
			sql="Select * from Company where Name='"+stockDetails.getCompany()+"'";
			rs=st.executeQuery(sql);
			if(!rs.next())
			{
				sql="Insert into Company(Name) values('"+stockDetails.getCompany()+"')";
				st.executeUpdate(sql);
			}
			
		}
		finally {
			
		}
		return;
		
	}
	
	public static  StockDetails retrieveIntraDaily(String company) throws SQLException, ClassNotFoundException {
		
		try {
			StartConnection();
			rs=st.executeQuery("Select * from ResponseDataCache where ID=(select ID from Company where Name='"+company+"') and APIID=(Select ID from API where APIType='IntraDaily') ");
			StockDetails sd=new StockDetails();
			sd.setCompany(company);
			if(rs.next()) {
				sd.setIntradetails(rs.getString(3));
				sd.setIntralastAccess(rs.getObject(4, LocalDateTime.class));
				
			}
			return sd;
		}
		finally {
			StopConnection();
			
		}

	}


}
