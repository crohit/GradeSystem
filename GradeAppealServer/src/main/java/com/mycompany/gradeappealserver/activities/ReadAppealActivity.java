/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.gradeappealserver.activities;

import com.mycompany.gradeappealserver.model.Appeal;
import com.mycompany.gradeappealserver.model.Identifier;
import com.mycompany.gradeappealserver.repositories.AppealRepository;
import com.mycompany.gradeappealserver.representations.AppealRepresentation;
import com.mycompany.gradeappealserver.representations.Representations;
import com.mycompany.gradeappealserver.representations.RestbucksUri;


/**
 *
 * @author Rohit
 */
public class ReadAppealActivity {
    
        public AppealRepresentation retrieveByUri(RestbucksUri appealUri) throws Exception {
        Identifier identifier  = appealUri.getId();
        
        Appeal appeal = AppealRepository.current().get(identifier);
        
        if(appeal == null) {
            throw new Exception();
        }
        
        return AppealRepresentation.createResponseAppealRepresentation(appeal, appealUri);
    
}

    
    
    
}
