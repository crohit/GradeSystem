/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.gradeappealclient.activities;

import com.mycompany.gradeappealclient.model.Appeal;
import com.mycompany.gradeappealclient.model.Identifier;
import com.mycompany.gradeappealclient.repositories.AppealRepository;
import com.mycompany.gradeappealclient.representations.AppealRepresentation;
import com.mycompany.gradeappealclient.representations.Representations;
import com.mycompany.gradeappealclient.representations.RestbucksUri;

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
