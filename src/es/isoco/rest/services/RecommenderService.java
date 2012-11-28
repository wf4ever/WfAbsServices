package es.isoco.rest.services;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import es.isoco.rest.logic.AbstractionInterface;
import es.isoco.rest.structure.Recommendation;

@Path("/recommend")
public class RecommenderService {
	
	
	// This method is called if TEXT_PLAIN is request
		@GET
		@Produces(MediaType.TEXT_HTML)
		public String sayPlainText() {
			String response="<?xml version=\"1.0\"?>"+
		"<rdf:RDF xmlns:rdf=\"http://www.w3.org/1999/02/22-rdf-syntax-ns#\" "+
	    " xmlns:roe=\"http://sandbox.wf4ever-project.org/wfabstraction\">"+
	    "<rdf:Description rdf:about=\"\">"+
	    "<roe:wfabstraction>/rest/recommend{?process[]}</roe:wfabstraction>"+
	    "</rdf:Description></rdf:RDF> ";
		return response;
		}

		@GET
		@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
		public Response recommend(@QueryParam("process") List<String> processes) {
			AbstractionInterface absInt=new AbstractionInterface();
			absInt.fillListUris();
			Recommendation item=new Recommendation();
			absInt.recommend(processes);
			item.fillList(absInt.getProbability(),absInt.getFrequency(), absInt.getProcessor());
			return Response.ok(item).build();
		}
		
}
