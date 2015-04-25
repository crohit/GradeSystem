/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.gradeappealserver.activities;


import com.mycompany.gradeappealserver.model.Grade;
import com.mycompany.gradeappealserver.model.Identifier;
import com.mycompany.gradeappealserver.representations.RestbucksUri;
import com.mycompany.gradeappealserver.representations.GradeRepresentations;
import com.mycompany.gradeappealserver.repositories.GradeRepository;
import com.mycompany.gradeappealserver.model.Grade;
import com.mycompany.gradeappealserver.representations.Representations;

/**
 *
 * @author Rohit
 */
public class UpdateGradeActivity {
    public GradeRepresentations update(Grade grade, RestbucksUri gradeUri) throws Exception {
        Identifier gradeIdentifier = gradeUri.getId();

        GradeRepository repository = GradeRepository.current();
        
        if (GradeRepository.current().gradeNotPlaced(gradeIdentifier)) { // Defensive check to see if we have the order
            throw new Exception();
        }

        if (!gradeCanBeChanged(gradeIdentifier)) {
            throw new Exception();
        }

        Grade storedGrade = repository.get(gradeIdentifier);
        
       
        //storedOrder.calculateCost();


        return GradeRepresentations.createResponseGradeRepresentation(storedGrade, gradeUri); 
    }
    
    private boolean gradeCanBeChanged(Identifier identifier) {
        return true;
    }
    
}
