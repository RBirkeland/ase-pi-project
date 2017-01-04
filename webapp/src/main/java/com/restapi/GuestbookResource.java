package com.restapi;

import java.util.List;

import org.restlet.resource.Get;
import javax.ws.rs.Produces;

import org.restlet.resource.ServerResource;

import com.example.guestbook.Guestbook;
import com.example.guestbook.Greeting;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.ObjectifyService;

public class GuestbookResource extends ServerResource {
    @Get("xml") @Produces("application/xml")
    public String getResource() {
    	String bookName = (String)this.getRequestAttributes().get("bookName");
    	
    	
    	Key<Guestbook> theBook = Key.create(Guestbook.class, bookName);

        // Run an ancestor query to ensure we see the most up-to-date
        // view of the Greetings belonging to the selected Guestbook.
          List<Greeting> greetings = ObjectifyService.ofy()
              .load()
              .type(Greeting.class) // We want only Greetings
              .ancestor(theBook)    // Anyone in this book
              .order("-date")       // Most recent first - date is indexed.
              .list();

          String xmlResponse = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
          xmlResponse = xmlResponse + "<guestbook>";
          
          for(Greeting greeting : greetings){
        	  xmlResponse = xmlResponse + "<greeting>";
        	  xmlResponse = xmlResponse + "<author_email>"+greeting.author_email+"</author_email>";
        	  xmlResponse = xmlResponse + "<author_id>"+greeting.author_id+"</author_id>";
        	  xmlResponse = xmlResponse + "<content>"+greeting.content+"</content>";
        	  xmlResponse = xmlResponse + "</greeting>";
          }
          
          xmlResponse = xmlResponse + "</guestbook>";
          
          return xmlResponse;
    }
    
}
