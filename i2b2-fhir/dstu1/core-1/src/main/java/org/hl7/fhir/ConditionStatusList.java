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
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ConditionStatus-list.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="ConditionStatus-list">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="provisional"/>
 *     &lt;enumeration value="working"/>
 *     &lt;enumeration value="confirmed"/>
 *     &lt;enumeration value="refuted"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "ConditionStatus-list")
@XmlEnum
public enum ConditionStatusList {


    /**
     * This is a tentative diagnosis - still a candidate that is under consideration.
     * 
     */
    @XmlEnumValue("provisional")
    PROVISIONAL("provisional"),

    /**
     * The patient is being treated on the basis that this is the condition, but it is still not confirmed.
     * 
     */
    @XmlEnumValue("working")
    WORKING("working"),

    /**
     * There is sufficient diagnostic and/or clinical evidence to treat this as a confirmed condition.
     * 
     */
    @XmlEnumValue("confirmed")
    CONFIRMED("confirmed"),

    /**
     * This condition has been ruled out by diagnostic and clinical evidence.
     * 
     */
    @XmlEnumValue("refuted")
    REFUTED("refuted");
    private final java.lang.String value;

    ConditionStatusList(java.lang.String v) {
        value = v;
    }

    public java.lang.String value() {
        return value;
    }

    public static ConditionStatusList fromValue(java.lang.String v) {
        for (ConditionStatusList c: ConditionStatusList.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
