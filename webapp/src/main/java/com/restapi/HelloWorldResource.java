package com.restapi;

import java.util.List;

import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

import com.example.guestbook.Greeting;
import com.googlecode.objectify.ObjectifyService;

/**
 * Resource which has only one representation.
 *
 */
public class HelloWorldResource extends ServerResource {

    @Get
    public String represent() {
        return "hello, world (from the cloud!)";
    }

}