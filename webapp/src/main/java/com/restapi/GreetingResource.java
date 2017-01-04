package com.restapi;

import java.util.List;

import javax.ws.rs.Produces;

import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

import com.example.guestbook.Greeting;
import com.example.guestbook.Guestbook;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.LoadResult;
import com.googlecode.objectify.ObjectifyService;

public class GreetingResource extends ServerResource{
	
    @Get("xml") @Produces("application/xml")
    public String getResource() {
    	//Long greetingID = (Long)this.getRequestAttributes().get("greetingID");
    	Long greetingID = Long.parseLong(getAttribute("greetingID"));
    	
    	
    	String bookName = "default";
    	Key<Guestbook> theBook = Key.create(Guestbook.class, bookName);
    	
        // Run an ancestor query to ensure we see the most up-to-date
        // view of the Greetings belonging to the selected Guestbook.
          Greeting greeting = ObjectifyService.ofy()
              .load()
              .type(Greeting.class) // We want only Greetings
              .parent(theBook)
              .id(greetingID)
              .now();
          
	  String xmlResponse = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
    	  xmlResponse = xmlResponse + "<greeting>";
    	  xmlResponse = xmlResponse + "<author_email>"+greeting.author_email+"</author_email>";
    	  xmlResponse = xmlResponse + "<author_id>"+greeting.author_id+"</author_id>";
    	  xmlResponse = xmlResponse + "<content>"+greeting.content+"</content>";
    	  xmlResponse = xmlResponse + "</greeting>";
      return xmlResponse;
         
    }
}
