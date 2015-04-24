/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.gradeappealserver.model;

/**
 *
 * @author Rohit
 */
import java.lang.StringBuilder;

public class Appeal {
    
     StringBuilder AppealComments = new StringBuilder(500);
     AppealStatus appStatus = AppealStatus.PENDING;
     
     public StringBuilder getComments()
     {
         return AppealComments;
     }
     
      public void setComments(StringBuilder com)
     {
         AppealComments.append(com);
     }
      public Appeal(StringBuilder app)
      {
           AppealComments.append(app);
           this.appStatus = AppealStatus.PENDING;
      }
      
      public void setStatus(AppealStatus s)
      {
          appStatus = s;
      }
      public AppealStatus getStatus()
      {
          return appStatus;
      }
}
