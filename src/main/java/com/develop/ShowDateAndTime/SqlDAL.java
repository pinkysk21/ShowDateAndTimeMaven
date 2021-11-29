package com.develop.ShowDateAndTime;
import java.sql.*;
import static com.develop.ShowDateAndTime.MyListner.*;
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
	static PreparedStatement pst=null;
	static long diff=0;
	static CallableStatement callStat=null;
	//Class.forName("com.mysql.jdbc.Driver");

	public static  boolean authenticateUser(String name,String pass) throws SQLException, ClassNotFoundException {
		Boolean val=false;
		try {
			StartConnection();
			
			String a="";
			
			//pst=con.prepareStatement("Select * from user where username=? and apikey=? ");
		//	pst.setString(1,name);
		//	pst.setString(2, pass);
			//rs=st.executeQuery("Select * from user where username='"+name+"' and apikey='"+pass+"'");
			 callStat = con.prepareCall("{call GETUSERDETAILS(?,?)}");
			callStat.setString(1, name);
			callStat.setString(2, pass);
			callStat.execute();
			rs=callStat.getResultSet();
			val=rs.next();
		}
		finally {
			if(con!=null)
				con.close();
			if(st!=null)
			    st.close();	
			if(pst!=null)
			    pst.close();
			if(callStat!=null) {
				callStat.close();
			}
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
		
		if(callStat!=null) {
			callStat.close();
		}
	}

	public static StockDetails retrieveDaily(String company) throws SQLException, ClassNotFoundException 
	{
		try {
			StartConnection();
			//ResultSet rs=st.executeQuery("Select * from ResponseDataCache where ID=(select ID from Company where Name='"+company+"') and APIID=(Select ID from API where APIType='Daily') ");
			callStat = con.prepareCall("{call RETRIEVEUSERDETAILS(?,?)}");
			callStat.setString(1, company);
			callStat.setString(2, "Daily");
			callStat.execute();
			rs=callStat.getResultSet();
			
		//	ResultSet rs=st.executeQuery("select c.ID,r.APIID,r.Data,r.lastAccess from Company c join ResponseDataCache r on c.ID=r.ID  join api a on r.APIID=a.ID where a.APIType='Daily' and c.Name='"+company+"'");
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
			if(callStat!=null) {
				callStat.close();
			}
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
				CallableStatement callStat = con.prepareCall("{call INSERTUSERDETAILS(?,?,?)}");
				callStat.setString(1, stockDetails.getCompany());
				callStat.setString(2, "Daily");
				callStat.setString(3, stockDetails.getDetails());
				callStat.execute();
			//	sql = "insert into ResponseDataCache(ID,APIID,DATA,lastAccess) values((select ID from Company where Name='"+stockDetails.getCompany()+"'),(select ID from api where APIType='Daily'),'"+stockDetails.getDetails()+"',now())";
			}
			//If database information is expired
			else
			{
				 callStat = con.prepareCall("{call UPDATEUSERDETAILS(?,?,?)}");
					callStat.setString(1, stockDetails.getCompany());
					callStat.setString(2, "Daily");
					callStat.setString(3, stockDetails.getDetails());
					callStat.execute();
			//	sql = "Update ResponseDataCache set Data='"+stockDetails.getDetails()+"',lastAccess=now() where ID=(Select ID from Company where Name='"+stockDetails.getCompany()+"') and APIID=(Select ID from API where APIType='Daily')";
			}
		//	st.executeUpdate(sql);
			
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
				 callStat = con.prepareCall("{call INSERTUSERDETAILS(?,?,?)}");
				callStat.setString(1, stockDetails.getCompany());
				callStat.setString(2, "IntraDaily");
				callStat.setString(3, stockDetails.getIntradetails());
				callStat.execute();
				//sql = "insert into ResponseDataCache(ID,APIID,DATA,lastAccess) values((select ID from Company where Name='"+stockDetails.getCompany()+"'),(select ID from api where APIType='IntraDaily'),'"+stockDetails.getDetails()+"',now())";
			}
			//If database information is expired
			else
			{
				 callStat = con.prepareCall("{call UPDATEUSERDETAILS(?,?,?)}");
				callStat.setString(1, stockDetails.getCompany());
				callStat.setString(2, "IntraDaily");
				callStat.setString(3, stockDetails.getIntradetails());
				callStat.execute();
				//sql = "Update ResponseDataCache set Data='"+stockDetails.getDetails()+"',lastAccess=now() where ID=(Select ID from Company where Name='"+stockDetails.getCompany()+"') and APIID=(Select ID from API where APIType='IntraDaily')";
			}
		//	st.executeUpdate(sql);
			
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
			callStat = con.prepareCall("{call GETCOMPANY(?)}");
			callStat.setString(1, stockDetails.getCompany());
			
			callStat.execute();
			rs=callStat.getResultSet();
			//sql="Select * from Company where Name='"+stockDetails.getCompany()+"'";
		//	rs=st.executeQuery(sql);
			if(!rs.next())
			{
				callStat = con.prepareCall("{call INSERTCOMPANY(?)}");
				callStat.setString(1, stockDetails.getCompany());
				
				callStat.execute();
			//	sql="Insert into Company(Name) values('"+stockDetails.getCompany()+"')";
			//	st.executeUpdate(sql);
			}
			
		}
		finally {
			
		}
		return;
		
	}
	
	public static  StockDetails retrieveIntraDaily(String company) throws SQLException, ClassNotFoundException {
		
		try {
			StartConnection();
			callStat = con.prepareCall("{call RETRIEVEUSERDETAILS(?,?)}");
			callStat.setString(1, company);
			callStat.setString(2, "IntraDaily");
			callStat.execute();
			rs=callStat.getResultSet();
		//	rs=st.executeQuery("Select * from ResponseDataCache where ID=(select ID from Company where Name='"+company+"') and APIID=(Select ID from API where APIType='IntraDaily') ");
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
