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
			st.close();
			con.close();			
		}
		return val;
	}
	
	private static void StartConnection() throws ClassNotFoundException, SQLException
	{
		Class.forName("com.mysql.jdbc.Driver");
		con = DriverManager.getConnection(url,uname,password);
		st=con.createStatement();
	}

	public static String retrieveDaily(String company) throws SQLException, ClassNotFoundException {
		String value="";
		try {
			StartConnection();
			rs=st.executeQuery("Select * from StockDetails where company='"+company+"'");

			if(rs.next()) {
				StockDetails sd=new StockDetails();
				sd.setCompany(rs.getString(1));
				sd.setDetails(rs.getString(2));
				sd.setLastAccess(rs.getObject(3, LocalDateTime.class));
				value=sd.dailyAccess(sd,company);
				
			}
			else {
				value=JsonResource.getJsonInfo(company);
				String sql=	"Insert into StockDetails(company,details,lastAccess) values('"+company+"','"+value+"',now())";
				st.executeUpdate(sql);
			}
		}
		finally {
			
			if(con!=null)
				con.close();
			if(st!=null)
			    st.close();
			
		}
		return value;
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

	public static  String retrieveIntraDaily(String company) throws SQLException {
		String value="";
		try {
			StartConnection();
			rs=st.executeQuery("Select * from StockDetails where company='"+company+"'");

			if(rs.next()) {

				StockDetails sd=new StockDetails();
				sd.setCompany(rs.getString(1));
				sd.setIntradetails(rs.getString(2));
				sd.setIntralastAccess(rs.getObject(3, LocalDateTime.class));
				value=sd.intradailyAccess(sd,company);
			}
			else {
				value=JsonResource.getIntraDailyJson(company);
				String sql=	"Insert into StockDetails(company,intradetails,intralastAccess) values('"+company+"','"+value+"',now())";
				st.executeUpdate(sql);
			}
		}
		finally {
			if(con!=null)
				con.close();
			if(st!=null)
			    st.close();
			return value;
		}

	}


}
