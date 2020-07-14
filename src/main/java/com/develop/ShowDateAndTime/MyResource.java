
package com.develop.ShowDateAndTime;

import javax.ws.rs.GET;
import java.time.*;
import java.time.format.*;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

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
    @Path("/stock")
    @Produces({MediaType.APPLICATION_JSON,MediaType.TEXT_PLAIN})
    public String getStock(@QueryParam("id") String id,@QueryParam("company") String param ,@QueryParam("u") String name ,@QueryParam("p") String pass) throws Exception{
    	return FindTheValue.find(param,name,pass,id);
    	
    }
}
