package com.develop.ShowDateAndTime.DataAccessObjects;

import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDateTime;

import com.develop.ShowDateAndTime.JsonResource;
import com.develop.ShowDateAndTime.SqlDAL;

public class StockDetails {
	private String company;
	private String details;
	private LocalDateTime lastAccess;
	private String intradetails;
	private LocalDateTime intralastAccess;
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	public String getDetails() {
		return details;
	}
	public void setDetails(String details) {
		this.details = details;
	}
	public LocalDateTime getLastAccess() {
		return lastAccess;
	}
	public void setLastAccess(LocalDateTime lastAccess) {
		this.lastAccess = lastAccess;
	}
	public String getIntradetails() {
		return intradetails;
	}
	public void setIntradetails(String intradetails) {
		this.intradetails = intradetails;
	}
	public LocalDateTime getIntralastAccess() {
		return intralastAccess;
	}
	public void setIntralastAccess(LocalDateTime intralastAccess) {
		this.intralastAccess = intralastAccess;
	}
	
	public String dailyAccess(StockDetails stockDetails,String company) throws SQLException {
		String value="";
		long diff=0;
		if(stockDetails.getLastAccess()!=null) {
			diff=Duration.between(stockDetails.getLastAccess(), LocalDateTime.now()).toHours();
		}
		if(stockDetails.getLastAccess()==null||diff>=24) {
			value=JsonResource.getJsonInfo(company);
			String sql=	"Update StockDetails set details='"+value+"',lastAccess=now() where company='"+company+"'";
			SqlDAL.queryUpdate(sql);
			
		}
		else {
			value=stockDetails.getDetails();
		}
		return value;
	}
	
	public String intradailyAccess(StockDetails stockDetails,String company) throws SQLException {
		String value="";
		long diff=0;
		if(stockDetails.getIntralastAccess()!=null) {
			diff=Duration.between(stockDetails.getLastAccess(), LocalDateTime.now()).toHours();
		}
		if(stockDetails.getIntralastAccess()==null||diff>=24) {
			value=JsonResource.getJsonInfo(company);
			String sql=	"Update StockDetails set intradetails='"+value+"',intralastAccess=now() where company='"+company+"'";
			SqlDAL.queryUpdate(sql);
			
		}
		else {
			value=stockDetails.getDetails();
		}
		return value;
	}
	
}
