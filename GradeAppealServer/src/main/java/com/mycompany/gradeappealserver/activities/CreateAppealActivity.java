/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.gradeappealserver.activities;

/**
 *
 * @author Rohit
 */
import com.mycompany.gradeappealserver.model.Appeal;
import com.mycompany.gradeappealserver.model.AppealStatus;
import com.mycompany.gradeappealserver.model.Identifier;
import com.mycompany.gradeappealserver.representations.AppealRepresentation;
import com.mycompany.gradeappealserver.representations.Link;
import com.mycompany.gradeappealserver.representations.Representations;
import com.mycompany.gradeappealserver.representations.RestbucksUri;
import com.mycompany.gradeappealserver.repositories.AppealRepository;

public class CreateAppealActivity {
    public AppealRepresentation create(Appeal appeal, RestbucksUri requestUri) {
        appeal.setStatus(AppealStatus.PENDING);
                
        Identifier identifier = AppealRepository.current().store(appeal);
        
        RestbucksUri appealUri = new RestbucksUri(requestUri.getBaseUri() + "/appeal/" + identifier.toString());
        //RestbucksUri paymentUri = new RestbucksUri(requestUri.getBaseUri() + "/payment/" + identifier.toString());
        return new AppealRepresentation(appeal, 
                new Link(Representations.RELATIONS_URI + "Delete", appealUri), 
                //new Link(Representations.RELATIONS_URI + "payment", paymentUri), 
                new Link(Representations.RELATIONS_URI + "update", appealUri),
                new Link(Representations.SELF_REL_VALUE, appealUri));
    }
}
