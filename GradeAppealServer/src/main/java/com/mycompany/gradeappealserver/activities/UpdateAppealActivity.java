package com.mycompany.gradeappealserver.activities;

import com.mycompany.gradeappealserver.model.Appeal;
import com.mycompany.gradeappealserver.model.AppealStatus;
import com.mycompany.gradeappealserver.model.Identifier;
import com.mycompany.gradeappealserver.repositories.AppealRepository;
import com.mycompany.gradeappealserver.representations.AppealRepresentation;
import com.mycompany.gradeappealserver.representations.RestbucksUri;


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
        
        storedAppeal.setStatus(appeal.getStatus());
        //storedOrder.calculateCost();


        return AppealRepresentation.createResponseAppealRepresentation(storedAppeal, appealUri); 
    }
    
    private boolean appealCanBeChanged(Identifier identifier) {
        return AppealRepository.current().get(identifier).getStatus() == AppealStatus.PENDING;
    }
}
