/*
 * Copyright (c) 2006-2007 Massachusetts General Hospital 
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the i2b2 Software License v1.0 
 * which accompanies this distribution. 
 * 
 * Contributors:
 * 		Kavishwar Wagholikar (kavi)
 * 		July 4, 2015
 */
//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.03.11 at 12:26:00 PM EDT 
//


package org.hl7.fhir;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for InstanceAvailability-list.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="InstanceAvailability-list">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="ONLINE"/>
 *     &lt;enumeration value="OFFLINE"/>
 *     &lt;enumeration value="NEARLINE"/>
 *     &lt;enumeration value="UNAVAILABLE"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "InstanceAvailability-list")
@XmlEnum
public enum InstanceAvailabilityList {


    /**
     * Resources are immediately available,.
     * 
     */
    ONLINE,

    /**
     * Resources need to be retrieved by manual intervention.
     * 
     */
    OFFLINE,

    /**
     * Resources need to be retrieved from relatively slow media.
     * 
     */
    NEARLINE,

    /**
     * Resources cannot be retrieved.
     * 
     */
    UNAVAILABLE;

    public java.lang.String value() {
        return name();
    }

    public static InstanceAvailabilityList fromValue(java.lang.String v) {
        return valueOf(v);
    }

}
