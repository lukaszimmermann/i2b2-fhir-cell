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
 * <p>Java class for StructureDefinitionType-list.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="StructureDefinitionType-list">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="type"/>
 *     &lt;enumeration value="resource"/>
 *     &lt;enumeration value="constraint"/>
 *     &lt;enumeration value="extension"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "StructureDefinitionType-list")
@XmlEnum
public enum StructureDefinitionTypeList {


    /**
     * A data type - either a primitive or complex structure that defines a set of data elements. These can be used throughout Resource and extension definitions.
     * 
     */
    @XmlEnumValue("type")
    TYPE("type"),

    /**
     * A resource defined by the FHIR specification.
     * 
     */
    @XmlEnumValue("resource")
    RESOURCE("resource"),

    /**
     * A set of constraints on a resource or data type that describe how it is used for a particular use.
     * 
     */
    @XmlEnumValue("constraint")
    CONSTRAINT("constraint"),

    /**
     * A definition of an extension that can be used in a FHIR resource (or a set of constraints on an exsting extension).
     * 
     */
    @XmlEnumValue("extension")
    EXTENSION("extension");
    private final java.lang.String value;

    StructureDefinitionTypeList(java.lang.String v) {
        value = v;
    }

    public java.lang.String value() {
        return value;
    }

    public static StructureDefinitionTypeList fromValue(java.lang.String v) {
        for (StructureDefinitionTypeList c: StructureDefinitionTypeList.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
