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
 * <p>Java class for ListMode-list.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="ListMode-list">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="working"/>
 *     &lt;enumeration value="snapshot"/>
 *     &lt;enumeration value="changes"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "ListMode-list")
@XmlEnum
public enum ListModeList {


    /**
     * This list is the master list, maintained in an ongoing fashion with regular updates as the real world list it is tracking changes.
     * 
     */
    @XmlEnumValue("working")
    WORKING("working"),

    /**
     * This list was prepared as a snapshot. It should not be assumed to be current.
     * 
     */
    @XmlEnumValue("snapshot")
    SNAPSHOT("snapshot"),

    /**
     * The list is prepared as a statement of changes that have been made or recommended.
     * 
     */
    @XmlEnumValue("changes")
    CHANGES("changes");
    private final java.lang.String value;

    ListModeList(java.lang.String v) {
        value = v;
    }

    public java.lang.String value() {
        return value;
    }

    public static ListModeList fromValue(java.lang.String v) {
        for (ListModeList c: ListModeList.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
