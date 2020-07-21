
package com.develop.ShowDateAndTime;

import javax.ws.rs.GET;
import java.time.*;
import java.time.format.*;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.develop.ShowDateAndTime.BusinessLayer.StockAccess;
import com.develop.ShowDateAndTime.DataAccessObjects.StockDetails;

/** Example resource class hosted at the URI path "/myresource"
 */
@Path("/")
public class MyResource {

	/** Method processing HTTP GET requests, producing "text/plain" MIME media
	 * type.
	 * @return String that will be send back as a response of type "text/plain".
	 */
	@GET 
	@Produces("text/plain")
	public String getIt() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		return "Today's date and time : "+LocalDateTime.now().format(formatter);
		//  return "Hi there!";
	}


	@GET
	@Path("/daily")
	@Produces({MediaType.APPLICATION_JSON,MediaType.TEXT_PLAIN})
	public String getStockDaily(@QueryParam("company") String companyName ,@QueryParam("u") String name ,@QueryParam("p") String pass) throws Exception{
		Boolean auth=UserAuthentication.authenticate(name,pass);
		if(auth) {
			return new StockAccess().dailyAccess(companyName).getDetails();
		}
		else {
			return "Invalid Credentials";
		}
	}

	@GET
	@Path("/intra")
	@Produces({MediaType.APPLICATION_JSON,MediaType.TEXT_PLAIN})
	public String getStockIntraDaily(@QueryParam("company") String companyName ,@QueryParam("u") String name ,@QueryParam("p") String pass) throws Exception{
		Boolean authIntraDaily=UserAuthentication.authenticate(name,pass);
		if(authIntraDaily) {
			return new StockAccess().intradailyAccess(companyName).getDetails();
		}
		else {
			return "Invalid Credentials";
		}
	}
}
