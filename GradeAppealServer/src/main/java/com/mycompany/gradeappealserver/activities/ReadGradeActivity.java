/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.gradeappealserver.activities;

import com.mycompany.gradeappealserver.model.Appeal;
import com.mycompany.gradeappealserver.model.Grade;
import com.mycompany.gradeappealserver.model.Identifier;
import com.mycompany.gradeappealserver.repositories.AppealRepository;
import com.mycompany.gradeappealserver.repositories.GradeRepository;
import com.mycompany.gradeappealserver.representations.AppealRepresentation;
import com.mycompany.gradeappealserver.representations.GradeRepresentations;
import com.mycompany.gradeappealserver.representations.RestbucksUri;

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