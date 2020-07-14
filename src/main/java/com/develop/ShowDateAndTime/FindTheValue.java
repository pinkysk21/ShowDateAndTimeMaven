package com.develop.ShowDateAndTime;
import java.sql.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import javax.ws.rs.core.MediaType;


public class FindTheValue {

	public static String find(String company,String name,String pass,String id) throws SQLException {
		String url="jdbc:mysql://localhost:3306/alpha";
		String uname="root";
		String password="mysql";
		String value="";
		ResultSet rs=null;
		Connection con = null;
		Statement st= null;
		long diff=0;
		try {

			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection(url,uname,password);
			st=con.createStatement();
			rs=st.executeQuery("Select * from user where username='"+name+"' and apikey='"+pass+"'");
			if(rs.next()) {

				rs=st.executeQuery("Select * from StockDetails where company='"+company+"'");
				if(id.equalsIgnoreCase("daily")) {
					if(rs.next()) {

						LocalDateTime current=	rs.getObject("lastAccess", LocalDateTime.class);
						if(current!=null) {
							diff=Duration.between(current, LocalDateTime.now()).toHours();
						}
						if((current==null)||diff>=24) {
							value=JsonResource.getJsonInfo(company);
							String sql=	"Update StockDetails set details='"+value+"',lastAccess=now() where company='"+company+"'";
							st.executeUpdate(sql);
						}
						else
						{
							value=rs.getString("details");
						}
					}
					else {
						value=JsonResource.getJsonInfo(company);
						String sql=	"Insert into StockDetails(company,details,lastAccess) values('"+company+"','"+value+"',now())";
						st.executeUpdate(sql);
					}
				}
				else if(id.equalsIgnoreCase("intradaily")) {
					if(rs.next()) {

						LocalDateTime current=	rs.getObject("intralastAccess", LocalDateTime.class);

						if(current!=null) {
							diff=Duration.between(current, LocalDateTime.now()).toHours();
						}
						if((current==null)||diff>=24) {
							value=JsonResource.getIntraDailyJson(company);
							String sql=	"Update StockDetails set intradetails='"+value+"',intralastAccess=now() where company='"+company+"'";
							st.executeUpdate(sql);
						}
						else
						{
							value=rs.getString("details");
						}
					}
					else {
						value=JsonResource.getIntraDailyJson(company);
						String sql=	"Insert into StockDetails(company,intradetails,intralastAccess) values('"+company+"','"+value+"',now())";
						st.executeUpdate(sql);
					}
				}

			}
			else {
				value="invalid credentials";

			}
		}
		finally {
			st.close();
			con.close();
			return value;
		}

	}

}
