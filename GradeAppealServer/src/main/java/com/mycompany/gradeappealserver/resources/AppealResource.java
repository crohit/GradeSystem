/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.gradeappealserver.resources;


import com.mycompany.gradeappealserver.activities.CreateAppealActivity;
import com.mycompany.gradeappealserver.activities.CreateGradeActivity;
import com.mycompany.gradeappealserver.activities.ReadAppealActivity;
import com.mycompany.gradeappealserver.activities.RemoveAppealActivity;
import com.mycompany.gradeappealserver.activities.UpdateAppealActivity;
import com.mycompany.gradeappealserver.model.Identifier;
import com.mycompany.gradeappealserver.repositories.GradeRepository;
import com.mycompany.gradeappealserver.representations.AppealRepresentation;
import com.mycompany.gradeappealserver.representations.GradeRepresentations;
import com.mycompany.gradeappealserver.representations.RestbucksUri;
import com.mycompany.gradeappealserver.model.Appeal;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Rohit
 */
@Path("/appeal")
public class AppealResource {
    
    private static final Logger LOG = LoggerFactory.getLogger(AppealResource.class);

    private @Context UriInfo uriInfo;
    

    public AppealResource() {
        LOG.info("AppealResource constructor");
    }

    /**
     * Used in test cases only to allow the injection of a mock UriInfo.
     * 
     * @param uriInfo
     */
    /*public AppealResource(UriInfo uriInfo) {
        LOG.info("OrderResource constructor with mock uriInfo {}", uriInfo);
        this.uriInfo = uriInfo;  
    }*/
    @GET
    @Path("/{appealId}")
    @Produces("application/vnd.cse564-appeals+xml ")
    public Response getTheAppeal() {
        LOG.info("Retrieving an appeal Resource");
        
        Response response;
        
        try {
            AppealRepresentation responseRepresentation = new ReadAppealActivity().retrieveByUri(new RestbucksUri(uriInfo.getRequestUri()));
            response = Response.ok().entity(responseRepresentation).build();
        } catch(Exception nsoe) {
            LOG.debug("No such order");
            response = Response.status(Response.Status.NOT_FOUND).build();
        } 
        
        LOG.debug("Retrieved the appeal resource", response);
        
        return response;
    }
    
        @POST
    @Path("/{appealId}")
    @Consumes("application/vnd.cse564-appeals+xml ")
    @Produces("application/vnd.cse564-appeals+xml ")
    public Response updateAppeal(String appealrepresentation) {
        LOG.info("Updating an appeal Resource");
        
        Response response;
                try {
            AppealRepresentation responseRepresentation = new UpdateAppealActivity().update(AppealRepresentation.fromXmlString(appealrepresentation).getAppeal(), new RestbucksUri(uriInfo.getRequestUri()));
            response = Response.ok().entity(responseRepresentation).build();
        } catch (Exception ioe) {
            LOG.debug("Something went wrong updating the order resource");
            response = Response.serverError().build();
        } 
        
        LOG.debug("Resulting response for updating the order resource is {}", response);
        
        return response;
     }

        @DELETE
    @Path("/{appealId}")
    @Produces("application/vnd.cse564-appeals+xml")
    public Response removeAppeal() {
        LOG.info("Removing an Appeal Reource");
        
        Response response;
        
        try {
            AppealRepresentation removedAppeal = new RemoveAppealActivity().delete(new RestbucksUri(uriInfo.getRequestUri()));
            response = Response.ok().entity(removedAppeal).build();
        } catch (Exception nsoe) {
            LOG.debug("Problems deleting the appeal resource");
            response = Response.serverError().build();
        }
        
        LOG.debug("Resulting response for deleting the appeal resource is {}", response);
        
        return response;
    }
    
    
    @POST
    @Consumes("application/vnd.cse564-appeals+xml")
    @Produces("application/vnd.cse564-appeals+xml")
            public Response createAppeal(String appealRepresentation){
    Response response;
        
        try {
            AppealRepresentation ap = AppealRepresentation.fromXmlString(appealRepresentation);
            
            
            AppealRepresentation responseRepresentation=new CreateAppealActivity().create(ap.getAppeal(), new RestbucksUri(uriInfo.getRequestUri()));
//new GradeRepresentations(GradeRepresentations.fromXmlString(gradeRepresentation).getGrade(), new Link("self", new RestbucksUri("localhost")));
           // return  Response.ok().entity(rep).build();
            response = Response.ok().entity(responseRepresentation).build();
        } catch (Exception ioe) {
           // LOG.debug("Invalid Order - Problem with the orderrepresentation {}", gradeRepresentation);
            response = Response.status(Response.Status.BAD_REQUEST).build();
        }  
        
        LOG.debug("Resulting response for creating the appeal resource is {}", response);
        
        return response;
    }
            
            
            
}