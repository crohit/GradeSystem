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
     
    public static void main(String[] args) throws Exception {
        URI serviceUri = new URI(ENTRY_POINT_URI);
        URI serviceUri1 = new URI(ENTRY_POINT_URI1);
        happyPathTest(serviceUri, serviceUri1);
        
    }
    
    
    private static void happyPathTest(URI serviceUri, URI serviceUri1) throws Exception {
        LOG.info("Starting Happy Path Test with Service URI {}", serviceUri);
        // Place the appeal
        LOG.info("Step 1. Post the grades");
        System.out.println(String.format("About to start happy path test. Placing grades at [%s] via POST", serviceUri.toString()));
        //Grade grade = new Grade();  //creating grade object
       // LOG.debug("Created base order {}", grade);
   
        Client client = Client.create(); //initialize client - from jersey
        LOG.debug("Created client {}", client);
        Grade grade = new Grade('A');
          GradeRepresentations gradeRepresentation = client.resource(serviceUri).accept(GRADEAPPEAL_MEDIA_TYPE).type(GRADEAPPEAL_MEDIA_TYPE).post(GradeRepresentations.class, grade);
        LOG.debug("Created Grade Representation {} denoted by the URI {}", gradeRepresentation, gradeRepresentation.getSelfLink().getUri().toString());
        System.out.println(String.format("Grades posted at [%s]", gradeRepresentation.getSelfLink().getUri().toString()));
          System.out.println(String.format("Grades posted at [%s]", gradeRepresentation.getGrade().getValue()));
        
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
    
       LOG.debug("\n\nStep 4. Professor gets the appeal, revies and updates the appeal to approved");
        System.out.println(String.format("About to update appeal at [%s] via POST", postAppealRepresentation.getUpdateLink().getUri().toString()));
        Appeal ap = new Appeal(postAppealRepresentation.getAppeal().getStatus());
        Link upLink = postAppealRepresentation.getUpdateLink();
        ap.setStatus(APPROVED);
        AppealRepresentation upRepresentation = client.resource(upLink.getUri()).accept(upLink.getMediaType()).type(upLink.getMediaType()).post(AppealRepresentation.class, ap );
        LOG.debug("Created updated Appeal representation link {}", upRepresentation);
        System.out.println(String.format("Appeal updated by Professor at [%s] and status is %s", upRepresentation.getSelfLink().getUri().toString(),ap.getStatus()));
  
        
        LOG.debug("\n\nStep 4. Professor gets the appeal, revies and rejects the appeal");
        System.out.println(String.format("About to update appeal at [%s] via POST", postAppealRepresentation.getUpdateLink().getUri().toString()));
        Appeal app = new Appeal(postAppealRepresentation.getAppeal().getStatus());
        Link upLin = postAppealRepresentation.getUpdateLink();
        app.setStatus(REJECTED);
        AppealRepresentation uRepresentation = client.resource(upLink.getUri()).accept(upLink.getMediaType()).type(upLink.getMediaType()).post(AppealRepresentation.class, app );
        LOG.debug("Created updated Appeal representation link {}", uRepresentation);
        System.out.println(String.format("Appeal updated by Professor at [%s] and status is %s", uRepresentation.getSelfLink().getUri().toString(),app.getStatus()));
       
       LOG.debug("\n\nStep 5. Get the grade");
        System.out.println(String.format("About to request grade from [%s] via GET", gradeRepresentation.getSelfLink().getUri().toString()));
        Link gradeLink = gradeRepresentation.getSelfLink();
        GradeRepresentations postGradeRepresentation = client.resource(gradeLink.getUri()).accept(GRADEAPPEAL_MEDIA_TYPE).get(GradeRepresentations.class);
       System.out.println(String.format("Final grade placed at", postGradeRepresentation.getSelfLink())); 

        
 //Try to update a different order
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
        
       
/*        /*  // Pay for the order 
        LOG.debug("\n\nStep 4. Pay for the order");
        System.out.println(String.format("About to create a payment resource at [%s] via PUT", updatedRepresentation.getPaymentLink().getUri().toString()));
        Link paymentLink = updatedRepresentation.getPaymentLink();
        LOG.debug("Created payment link {} for updated order representation {}", paymentLink, updatedRepresentation);
        LOG.debug("paymentLink.getRelValue() = {}", paymentLink.getRelValue());
        LOG.debug("paymentLink.getUri() = {}", paymentLink.getUri());
        LOG.debug("paymentLink.getMediaType() = {}", paymentLink.getMediaType());
         Payment payment = new Payment(updatedRepresentation.getCost(), "A.N. Other", "12345677878", 12, 2999);
        LOG.debug("Created new payment object {}", payment);
        PaymentRepresentation  paymentRepresentation = client.resource(paymentLink.getUri()).accept(paymentLink.getMediaType()).type(paymentLink.getMediaType()).put(PaymentRepresentation.class, new PaymentRepresentation(payment));        
        LOG.debug("Created new payment representation {}", paymentRepresentation);
        System.out.println(String.format("Payment made, receipt at [%s]", paymentRepresentation.getReceiptLink().getUri().toString()));
        

        
        // Allow the barista some time to make the order
        LOG.debug("\n\nStep 7. Allow the barista some time to make the order");
        System.out.println("Pausing the client, press a key to continue");
        System.in.read();
        
        // Take the order if possible
        LOG.debug("\n\nStep 8. Take the order if possible");
        System.out.println(String.format("Trying to take the ready order from [%s] via DELETE. Note: the internal state machine must progress the order to ready before this should work, otherwise expect a 405 response.", receiptRepresentation.getOrderLink().getUri().toString()));
        ClientResponse finalResponse = client.resource(orderLink.getUri()).delete(ClientResponse.class);
        System.out.println(String.format("Tried to take final order, HTTP status [%d]", finalResponse.getStatus()));
        if(finalResponse.getStatus() == 200) {
            System.out.println(String.format("Order status [%s], enjoy your drink", finalResponse.getEntity(OrderRepresentation.class).getStatus()));
        }*/
    }
    
}
