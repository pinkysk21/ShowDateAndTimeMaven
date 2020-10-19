package com.develop.ShowDateAndTime.BusinessLayer;

import java.sql.SQLException;
import static com.develop.ShowDateAndTime.MyListner.*;
import java.time.Duration;
import java.time.LocalDateTime;

import com.develop.ShowDateAndTime.AlphaVantageResource;
import com.develop.ShowDateAndTime.SqlDAL;
import com.develop.ShowDateAndTime.DataAccessObjects.StockDetails;

public class StockAccess {
	public StockDetails dailyAccess(String companyName) throws SQLException, ClassNotFoundException {
		long diff=0;
		
		//Fetch daily api details from database
		StockDetails stockDetails = SqlDAL.retrieveDaily(companyName);
		if(stockDetails.getDetails()==null)
		{
			stockDetails.setDetails(AlphaVantageResource.getDailyJsonInfo(stockDetails.getCompany()));
			logger.info("Called AlphaVantage API");
			SqlDAL.updateOrInsertDaily(true, stockDetails);
		}
		else {
			if(stockDetails.getLastAccess()!=null) {
				diff=Duration.between(stockDetails.getLastAccess(), LocalDateTime.now()).toHours();
			}
			
			//If database information is expired
			if(stockDetails.getLastAccess()==null || diff>=24) {
				stockDetails.setDetails(AlphaVantageResource.getDailyJsonInfo(stockDetails.getCompany()));
				logger.info("Called AlphaVantage API");
				SqlDAL.updateOrInsertDaily(false, stockDetails);
			}
			else {
				logger.info("From Database- daily");
			}
		}
		return stockDetails;
	}
	
	public StockDetails intradailyAccess(String company) throws SQLException, ClassNotFoundException {
		long diff=0;
		StockDetails stockDetails = SqlDAL.retrieveIntraDaily(company);
		if(stockDetails.getIntradetails()==null)
		{
			stockDetails.setIntradetails(AlphaVantageResource.getDailyJsonInfo(stockDetails.getCompany()));
			logger.info("Called AlphaVantage API");
			SqlDAL.updateOrInsertIntraDaily(true, stockDetails);
		}
		else {
			if(stockDetails.getIntralastAccess()!=null) {
				diff=Duration.between(stockDetails.getIntralastAccess(), LocalDateTime.now()).toHours();
			}
			
			//If database information is expired
			if(stockDetails.getIntralastAccess()==null || diff>=24) {
				stockDetails.setIntradetails(AlphaVantageResource.getDailyJsonInfo(stockDetails.getCompany()));
				logger.info("Called AlphaVantage API");
				SqlDAL.updateOrInsertDaily(false, stockDetails);
			}
			else {
				logger.info("From Database- intradaily ");
			}
		}
		return stockDetails;
}
}