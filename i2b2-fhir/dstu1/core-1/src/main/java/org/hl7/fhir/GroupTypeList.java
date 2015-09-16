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
 * <p>Java class for GroupType-list.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="GroupType-list">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="person"/>
 *     &lt;enumeration value="animal"/>
 *     &lt;enumeration value="practitioner"/>
 *     &lt;enumeration value="device"/>
 *     &lt;enumeration value="medication"/>
 *     &lt;enumeration value="substance"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "GroupType-list")
@XmlEnum
public enum GroupTypeList {


    /**
     * Group contains "person" Patient resources.
     * 
     */
    @XmlEnumValue("person")
    PERSON("person"),

    /**
     * Group contains "animal" Patient resources.
     * 
     */
    @XmlEnumValue("animal")
    ANIMAL("animal"),

    /**
     * Group contains healthcare practitioner resources.
     * 
     */
    @XmlEnumValue("practitioner")
    PRACTITIONER("practitioner"),

    /**
     * Group contains Device resources.
     * 
     */
    @XmlEnumValue("device")
    DEVICE("device"),

    /**
     * Group contains Medication resources.
     * 
     */
    @XmlEnumValue("medication")
    MEDICATION("medication"),

    /**
     * Group contains Substance resources.
     * 
     */
    @XmlEnumValue("substance")
    SUBSTANCE("substance");
    private final java.lang.String value;

    GroupTypeList(java.lang.String v) {
        value = v;
    }

    public java.lang.String value() {
        return value;
    }

    public static GroupTypeList fromValue(java.lang.String v) {
        for (GroupTypeList c: GroupTypeList.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
