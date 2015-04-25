/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.gradeappealclient.client;

import com.mycompany.gradeappealclient.representations.GradeRepresentations;
import com.mycompany.gradeappealclient.representations.AppealRepresentation;
import com.mycompany.gradeappealclient.representations.Link;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.mycompany.gradeappealclient.model.Grade;
import com.mycompany.gradeappealclient.model.Appeal;
import com.mycompany.gradeappealclient.model.AppealStatus;
import static com.mycompany.gradeappealclient.model.AppealStatus.APPROVED;
import static com.mycompany.gradeappealclient.model.AppealStatus.REJECTED;
import com.mycompany.gradeappealclient.representations.RestbucksUri;
import java.net.URI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Rohit
 */
public class Main {
    
    private static final Logger LOG = LoggerFactory.getLogger(Main.class);
    private static final String GRADEAPPEAL_MEDIA_TYPE = "application/vnd.cse564-appeals+xml";
    private static final String ENTRY_POINT_URI = "http://localhost:8080/GradeAppealServer/webresources/grades";
     private static final String ENTRY_POINT_URI1 = "http://localhost:8080/GradeAppealServer/webresources/appeal";
     private static final String WRONG_CASE_URI = "http://localhost:8080/GradeAppealServer/webresrources/getappeal";
     
    public static void main(String[] args) throws Exception {
        URI serviceUri = new URI(ENTRY_POINT_URI);
        URI serviceUri1 = new URI(ENTRY_POINT_URI1);
        URI serviceUri2 = new URI(WRONG_CASE_URI);
        happyPathTest(serviceUri, serviceUri1);
        AbandonPathTest(serviceUri1);
        AppealFollowUp(serviceUri1);
        AppealReject(serviceUri1);
       // BadPathTest(serviceUri2);

        System.out.println("****************************************");
        System.out.println("End of Process. Thank you. ");
        System.out.println("****************************************");
    
        
    }
    
    
    private static void happyPathTest(URI serviceUri, URI serviceUri1) throws Exception {
        System.out.println("Welcome to Student Professor Grade Appeal REST System");
        System.out.println();
        System.out.println("*********************************************************************************************************");
        
        LOG.info("Starting Happy Path Test with Service URI {}", serviceUri);
        // Place the appeal
        LOG.info("Step 1. Professors Posts the grades");
        System.out.println(String.format("About to start happy path test. Placing grades at [%s] via POST", serviceUri.toString()));
        //Grade grade = new Grade();  //creating grade object
       // LOG.debug("Created base order {}", grade);
   
        Client client = Client.create(); //initialize client - from jersey
        LOG.debug("Created client {}", client);
        Grade grade = new Grade('A');
        GradeRepresentations gradeRepresentation = client.resource(serviceUri).accept(GRADEAPPEAL_MEDIA_TYPE).type(GRADEAPPEAL_MEDIA_TYPE).post(GradeRepresentations.class, grade);
        LOG.debug("Created Grade Representation {} denoted by the URI {}", gradeRepresentation, gradeRepresentation.getSelfLink().getUri().toString());
        System.out.println(String.format("Grades posted at [%s]", gradeRepresentation.getSelfLink().getUri().toString()));
          
        System.out.println();
        //compose an appeal
       LOG.info("Step 2. Students Post an appeal");
       StringBuilder AppealRequest = new StringBuilder();
       AppealRequest.append("I have a questions. Please consider?");
       Appeal appeal = new Appeal(AppealRequest);
       AppealRepresentation appealRepresentation = client.resource(serviceUri1).accept(GRADEAPPEAL_MEDIA_TYPE).type(GRADEAPPEAL_MEDIA_TYPE).post(AppealRepresentation.class, appeal);
       LOG.debug("Created AppealRepresentation {} denoted by the URI {}", appealRepresentation, appealRepresentation.getSelfLink().getUri().toString());
        System.out.println(String.format("Appeals posted at [%s]", appealRepresentation.getSelfLink().getUri().toString())); 
       
                // Get an appeal
        LOG.debug("\n\nStep 3. Get the appeal");
        System.out.println(String.format("About to request appeal from [%s] via GET", appealRepresentation.getSelfLink().getUri().toString()));
        Link appealLink = appealRepresentation.getSelfLink();
        AppealRepresentation postAppealRepresentation = client.resource(appealLink.getUri()).accept(GRADEAPPEAL_MEDIA_TYPE).get(AppealRepresentation.class);
       System.out.println(String.format("Appeal placed, current status [%s], placed at %s", postAppealRepresentation.getStatus(), postAppealRepresentation.getSelfLink())); 
    
         
        LOG.debug("\n\nStep 4. Professor gets the appeal, reviews and updates the appeal to approved");
        System.out.println(String.format("About to update appeal at [%s] via POST", postAppealRepresentation.getUpdateLink().getUri().toString()));
        Appeal ap = new Appeal(postAppealRepresentation.getAppeal().getStatus());
        Link upLink = postAppealRepresentation.getUpdateLink();
        ap.setStatus(APPROVED);
        AppealRepresentation upRepresentation = client.resource(upLink.getUri()).accept(upLink.getMediaType()).type(upLink.getMediaType()).post(AppealRepresentation.class, ap );
        LOG.debug("Created updated Appeal representation link {}", upRepresentation);
        System.out.println(String.format("Appeal updated by Professor at [%s] and status is %s", upRepresentation.getSelfLink().getUri().toString(),ap.getStatus()));
        
       LOG.debug("\n\n Step 5. Professors approves the appeal, and gets the grade increases the grade");
        LOG.debug("\n\n Get the grade");
        System.out.println(String.format("About to request grade from [%s] via GET", gradeRepresentation.getSelfLink().getUri().toString()));
        Link gradeLink = gradeRepresentation.getSelfLink();
        GradeRepresentations postGradeRepresentation = client.resource(gradeLink.getUri()).accept(GRADEAPPEAL_MEDIA_TYPE).get(GradeRepresentations.class);
        System.out.println(String.format("Final grade placed at", postGradeRepresentation.getSelfLink())); 
     
        Grade newgrade = new Grade('B');
        Link newLink = postGradeRepresentation.getUpdateLink();
        //GradeRepresentations newRepresentation = client.resource(newLink.getUri()).accept(newLink.getMediaType()).type(newLink.getMediaType()).post(GradeRepresentations.class, newgrade );
        System.out.println("Grade is updated"); 
              
       
       
 //Try to update a different order
        System.out.println("bad path test");
        System.out.println("*********************************************************************************************************");
        LOG.info("\n\nStep 6. Updating an appeal with a BAD ID");
        System.out.println(String.format("About to update an order with bad URI [%s] via POST", appealRepresentation.getUpdateLink().getUri().toString() + "/bad-uri"));
        StringBuilder AppealRequest1 = new StringBuilder();
        AppealRequest1.append("I have a questions. Please consider the rectified appeal?");
        LOG.debug("Created base appeal {}", appeal);
        Link badLink = new Link("bad", new RestbucksUri(appealRepresentation.getSelfLink().getUri().toString() + "/bad-uri"), GRADEAPPEAL_MEDIA_TYPE);
        LOG.debug("Create bad link {}", badLink);
        ClientResponse badUpdateResponse = client.resource(badLink.getUri()).accept(badLink.getMediaType()).type(badLink.getMediaType()).post(ClientResponse.class, new AppealRepresentation(appeal));
        LOG.debug("Created Bad Update Response {}", badUpdateResponse);
        System.out.println(String.format("Tried to update appeal with bad URI at [%s] via POST, outcome [%d]", badLink.getUri().toString(), badUpdateResponse.getStatus()));
        
  
           
        LOG.debug("\n\nStep 7. Update appeal with legitimate ID");
        System.out.println(String.format("About to update appeal at [%s] via POST", postAppealRepresentation.getUpdateLink().getUri().toString()));
        StringBuilder AppealRequest2 = new StringBuilder();
        AppealRequest2.append("I have a couple of questions. Please consider the rectified appeal?");
        Appeal appeal3 = new Appeal(AppealRequest2);        

        Link updateLink = postAppealRepresentation.getUpdateLink();
         AppealRepresentation updatedRepresentation = client.resource(updateLink.getUri()).accept(updateLink.getMediaType()).type(updateLink.getMediaType()).post(AppealRepresentation.class, appeal3);
        LOG.debug("Created updated Appeal representation link {}", updatedRepresentation);
        System.out.println(String.format("Appeal updated at [%s]", updatedRepresentation.getSelfLink().getUri().toString()));
        

    }
    
    private static void AbandonPathTest(URI serviceUri1) throws Exception{
     
        System.out.println();
        System.out.println("*********************************************************************************************************");
        
        Client client = Client.create(); //initialize client - from jersey
      System.out.println("Starting Abandon path test. Students post appeal, and abandons the appeal before professor review");
      LOG.info("Step 1. Students Post an appeal");
      StringBuilder AppealRequest = new StringBuilder();
      AppealRequest.append("I have a questions. Please consider?");
       Appeal appeal = new Appeal(AppealRequest);
       AppealRepresentation appealRepresentation = client.resource(serviceUri1).accept(GRADEAPPEAL_MEDIA_TYPE).type(GRADEAPPEAL_MEDIA_TYPE).post(AppealRepresentation.class, appeal);
       LOG.debug("Created AppealRepresentation {} denoted by the URI {}", appealRepresentation, appealRepresentation.getSelfLink().getUri().toString());
        System.out.println(String.format("Appeals posted at [%s]", appealRepresentation.getSelfLink().getUri().toString())); 
       
                // Get an appeal
        LOG.debug("\n\nStep 2. Get the appeal");
        System.out.println(String.format("About to request appeal from [%s] via GET", appealRepresentation.getSelfLink().getUri().toString()));
        Link appealLink = appealRepresentation.getSelfLink();
        AppealRepresentation postAppealRepresentation = client.resource(appealLink.getUri()).accept(GRADEAPPEAL_MEDIA_TYPE).get(AppealRepresentation.class);
       System.out.println(String.format("Appeal placed, current status [%s], placed at %s", postAppealRepresentation.getStatus(), postAppealRepresentation.getSelfLink())); 
        
        
        LOG.debug("\n\nStep 3. Student abandons the appeal");
        System.out.println(String.format("Trying to remove the Pendng appeal from [%s] via DELETE. ", postAppealRepresentation.getDeleteLink().getUri().toString()));
        Link deleteLink = postAppealRepresentation.getSelfLink();
        ClientResponse finalResponse = client.resource(deleteLink.getUri()).delete(ClientResponse.class);
        System.out.println(String.format("Tried to delete appeal, HTTP status [%d]", finalResponse.getStatus()));
        if(finalResponse.getStatus() == 200) {
            System.out.println(String.format("Appeal status [%s],", finalResponse.getEntity(AppealRepresentation.class).getStatus()));
        }
    }
    
    private static void AppealFollowUp(URI serviceUri1) throws Exception {
     
       System.out.println();
       System.out.println("*********************************************************************************************************");
        System.out.println("Appeal follow up case. Students post appeal and professor forgets case, student follows up.");
       Client client = Client.create(); 
       LOG.info("Step 2. Students Post an appeal");
       StringBuilder AppealRequest = new StringBuilder();
       AppealRequest.append("I have a questions. Please consider?");
       Appeal appeal = new Appeal(AppealRequest);
       AppealRepresentation appealRepresentation = client.resource(serviceUri1).accept(GRADEAPPEAL_MEDIA_TYPE).type(GRADEAPPEAL_MEDIA_TYPE).post(AppealRepresentation.class, appeal);
       LOG.debug("Created AppealRepresentation {} denoted by the URI {}", appealRepresentation, appealRepresentation.getSelfLink().getUri().toString());
       System.out.println(String.format("Appeals posted at [%s]", appealRepresentation.getSelfLink().getUri().toString())); 

       LOG.info("Student updates the appeal - with follow up comments");
       System.out.println(String.format("About to update appeal at [%s] via POST", appealRepresentation.getUpdateLink().getUri().toString()));
        StringBuilder AppealRequest2 = new StringBuilder();
        AppealRequest2.append("I have a couple of questions. Please consider the rectified appeal?");
        Appeal appeal3 = new Appeal(AppealRequest2);        

        Link updateLink = appealRepresentation.getUpdateLink();
         AppealRepresentation updatedRepresentation = client.resource(updateLink.getUri()).accept(updateLink.getMediaType()).type(updateLink.getMediaType()).post(AppealRepresentation.class, appeal3);
        LOG.debug("Updated Appeal representation link {}", updatedRepresentation);
        System.out.println(String.format("Appeal updated at [%s]", updatedRepresentation.getSelfLink().getUri().toString()));
       LOG.info("Appeal updated with follow up comments");
        
    }

    private static void AppealReject(URI serviceUri1) throws Exception {

        System.out.println();
        System.out.println("*********************************************************************************************************");
        System.out.println("Case where appeal is posted, and professor rejects it.");
         
        
       Client client = Client.create();
       LOG.info("Step 2. Students Post an appeal");
       StringBuilder AppealRequest = new StringBuilder();
       AppealRequest.append("I have a questions. Please consider?");
       Appeal appeal = new Appeal(AppealRequest);
       AppealRepresentation appealRepresentation = client.resource(serviceUri1).accept(GRADEAPPEAL_MEDIA_TYPE).type(GRADEAPPEAL_MEDIA_TYPE).post(AppealRepresentation.class, appeal);
       LOG.debug("Created AppealRepresentation {} denoted by the URI {}", appealRepresentation, appealRepresentation.getSelfLink().getUri().toString());
        System.out.println(String.format("Appeals posted at [%s]", appealRepresentation.getSelfLink().getUri().toString())); 
       
                // Get an appeal
        LOG.debug("\n\nStep 3. Get the appeal");
        System.out.println(String.format("About to request appeal from [%s] via GET", appealRepresentation.getSelfLink().getUri().toString()));
        Link appealLink = appealRepresentation.getSelfLink();
        AppealRepresentation postAppealRepresentation = client.resource(appealLink.getUri()).accept(GRADEAPPEAL_MEDIA_TYPE).get(AppealRepresentation.class);
        System.out.println(String.format("Appeal placed, current status [%s], placed at %s", postAppealRepresentation.getStatus(), postAppealRepresentation.getSelfLink())); 

        LOG.debug("\n\nStep 4. Professor gets the appeal, revies and rejects the appeal");
        System.out.println(String.format("About to update appeal at [%s] via POST", postAppealRepresentation.getUpdateLink().getUri().toString()));
        Appeal app = new Appeal(postAppealRepresentation.getAppeal().getStatus());
        Link upLin = postAppealRepresentation.getUpdateLink();
        app.setStatus(REJECTED);
        AppealRepresentation uRepresentation = client.resource(upLin.getUri()).accept(upLin.getMediaType()).type(upLin.getMediaType()).post(AppealRepresentation.class, app );
        LOG.debug("Created updated Appeal representation link {}", uRepresentation);
        System.out.println(String.format("Appeal updated by Professor at [%s] and status is %s", uRepresentation.getSelfLink().getUri().toString(),app.getStatus()));
     
    }
    

}
