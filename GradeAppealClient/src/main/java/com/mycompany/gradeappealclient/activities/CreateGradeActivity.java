/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.gradeappealclient.activities;

/**
 *
 * @author Rohit
 */
import com.mycompany.gradeappealclient.model.Grade;
import com.mycompany.gradeappealclient.model.Identifier;
import com.mycompany.gradeappealclient.representations.GradeRepresentations;
import com.mycompany.gradeappealclient.representations.Link;
import com.mycompany.gradeappealclient.representations.Representations;
import com.mycompany.gradeappealclient.representations.RestbucksUri;
import com.mycompany.gradeappealclient.repositories.GradeRepository;

public class CreateGradeActivity {

    public GradeRepresentations create(Grade grade, RestbucksUri requestUri) {
        //grade.setStatus(AppealStatus.PENDING);
                
        Identifier identifier = GradeRepository.current().store(grade);
        
        RestbucksUri gradeUri = new RestbucksUri(requestUri.getBaseUri() + "/grades/" + identifier.toString());
        //RestbucksUri paymentUri = new RestbucksUri(requestUri.getBaseUri() + "/payment/" + identifier.toString());
        return new GradeRepresentations(grade,
                new Link(Representations.SELF_REL_VALUE, gradeUri));
    }
}
