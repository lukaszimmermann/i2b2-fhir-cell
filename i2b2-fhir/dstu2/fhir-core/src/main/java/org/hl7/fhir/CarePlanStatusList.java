/*
 * Copyright (c) 2006-2007 Massachusetts General Hospital 
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the i2b2 Software License v1.0 
 * which accompanies this distribution. 
 * 
 * Contributors:
 * 		Kavishwar Wagholikar (kavi)
 */
//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.06.25 at 02:29:55 PM EDT 
//


package org.hl7.fhir;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for CarePlanStatus-list.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="CarePlanStatus-list">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="planned"/>
 *     &lt;enumeration value="active"/>
 *     &lt;enumeration value="completed"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "CarePlanStatus-list")
@XmlEnum
public enum CarePlanStatusList {


    /**
     * The plan is in development or awaiting use but is not yet intended to be acted upon.
     * 
     */
    @XmlEnumValue("planned")
    PLANNED("planned"),

    /**
     * The plan is intended to be followed and used as part of patient care.
     * 
     */
    @XmlEnumValue("active")
    ACTIVE("active"),

    /**
     * The plan is no longer in use and is not expected to be followed or used in patient care.
     * 
     */
    @XmlEnumValue("completed")
    COMPLETED("completed");
    private final java.lang.String value;

    CarePlanStatusList(java.lang.String v) {
        value = v;
    }

    public java.lang.String value() {
        return value;
    }

    public static CarePlanStatusList fromValue(java.lang.String v) {
        for (CarePlanStatusList c: CarePlanStatusList.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
