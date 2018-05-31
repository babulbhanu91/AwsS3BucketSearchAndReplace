package com.restcontroller;

import java.io.File;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

import org.codehaus.jackson.map.ObjectMapper;
import org.json.simple.JSONArray;

import com.main.FindReplace;

@Path("/search")
public class MainController {

	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String search(@QueryParam("accessKey") String accessKey,@QueryParam("secretKey") String secretKey,@QueryParam("searchText") String searchString,@QueryParam("replaceText") String replaceString ){
		FindReplace fr = new FindReplace(searchString,replaceString);
		 return fr.findAndReplace(accessKey,secretKey).toString();
	}
}
