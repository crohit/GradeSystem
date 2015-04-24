/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.gradeappealclient.model;

import com.mycompany.gradeappealclient.representations.Representations;
import java.lang.StringBuilder;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
/**
 *
 * @author Rohit
 */
@XmlRootElement(name="appeal", namespace = Representations.RESTBUCKS_NAMESPACE)
public class Appeal {
      @XmlElement(name = "AppealComments", namespace = Representations.RESTBUCKS_NAMESPACE)
    private StringBuilder AppealComments = new StringBuilder();
    
    @XmlElement(name = "status", namespace = Representations.RESTBUCKS_NAMESPACE)
    private AppealStatus appStatus = AppealStatus.PENDING;
    
         
     public StringBuilder getComments()
     {
         return AppealComments;
     }
     Appeal()
     { }
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
