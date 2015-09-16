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
 * <p>Java class for ResourceVersionPolicy-list.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="ResourceVersionPolicy-list">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="no-version"/>
 *     &lt;enumeration value="versioned"/>
 *     &lt;enumeration value="versioned-update"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "ResourceVersionPolicy-list")
@XmlEnum
public enum ResourceVersionPolicyList {


    /**
     * VersionId meta-property is not suppoerted (server) or used (client).
     * 
     */
    @XmlEnumValue("no-version")
    NO_VERSION("no-version"),

    /**
     * VersionId meta-property is suppoerted (server) or used (client).
     * 
     */
    @XmlEnumValue("versioned")
    VERSIONED("versioned"),

    /**
     * VersionId is must be correct for updates (server) or will be specified (If-match header) for updates (client).
     * 
     */
    @XmlEnumValue("versioned-update")
    VERSIONED_UPDATE("versioned-update");
    private final java.lang.String value;

    ResourceVersionPolicyList(java.lang.String v) {
        value = v;
    }

    public java.lang.String value() {
        return value;
    }

    public static ResourceVersionPolicyList fromValue(java.lang.String v) {
        for (ResourceVersionPolicyList c: ResourceVersionPolicyList.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
