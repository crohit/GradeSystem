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
import javax.xml.bind.annotation.XmlEnumValue;


public enum AppealStatus {
    @XmlEnumValue(value="Approved")
    APPROVED,
    @XmlEnumValue(value="Pending")
    PENDING, 
    @XmlEnumValue(value="Rejected")
    REJECTED, 
    
}