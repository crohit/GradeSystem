/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.gradeappealclient.activities;

import com.mycompany.gradeappealclient.model.Grade;
import com.mycompany.gradeappealclient.model.Identifier;
import com.mycompany.gradeappealclient.repositories.GradeRepository;
import com.mycompany.gradeappealclient.representations.GradeRepresentations;
import com.mycompany.gradeappealclient.representations.RestbucksUri;



/**
 *
 * @author Rohit
 */
public class ReadGradeActivity {
    
        public GradeRepresentations retrieveByUri(RestbucksUri gradeUri) throws Exception {
        Identifier identifier  = gradeUri.getId();
        
        Grade grade = GradeRepository.current().get(identifier);
        
        if(grade == null) {
            throw new Exception();
        }
        
        return GradeRepresentations.createResponseGradeRepresentation(grade, gradeUri);
    
}

    
    
    
}