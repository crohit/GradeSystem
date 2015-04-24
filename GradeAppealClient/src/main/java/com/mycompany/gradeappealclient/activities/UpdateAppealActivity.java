package com.mycompany.gradeappealclient.activities;

import com.mycompany.gradeappealclient.model.Appeal;
import com.mycompany.gradeappealclient.model.AppealStatus;
import com.mycompany.gradeappealclient.model.Identifier;
import com.mycompany.gradeappealclient.repositories.AppealRepository;
import com.mycompany.gradeappealclient.representations.AppealRepresentation;
import com.mycompany.gradeappealclient.representations.RestbucksUri;


public class UpdateAppealActivity {
    public AppealRepresentation update(Appeal appeal, RestbucksUri appealUri) throws Exception {
        Identifier appealIdentifier = appealUri.getId();

        AppealRepository repository = AppealRepository.current();
        
        if (AppealRepository.current().appealNotPlaced(appealIdentifier)) { // Defensive check to see if we have the order
            throw new Exception();
        }

        if (!appealCanBeChanged(appealIdentifier)) {
            throw new Exception();
        }

        Appeal storedAppeal = repository.get(appealIdentifier);
        
        storedAppeal.setStatus(storedAppeal.getStatus());
        //storedOrder.calculateCost();


        return AppealRepresentation.createResponseAppealRepresentation(storedAppeal, appealUri); 
    }
    
    private boolean appealCanBeChanged(Identifier identifier) {
        return AppealRepository.current().get(identifier).getStatus() == AppealStatus.APPROVED;
    }
}
