//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.12.30 at 02:43:29 AM EST 
//


package org.hl7.fhir;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ProcedureRequestPriority-list.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="ProcedureRequestPriority-list">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="routine"/>
 *     &lt;enumeration value="urgent"/>
 *     &lt;enumeration value="stat"/>
 *     &lt;enumeration value="asap"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "ProcedureRequestPriority-list")
@XmlEnum
public enum ProcedureRequestPriorityList {


    /**
     * The request has a normal priority.
     * 
     */
    @XmlEnumValue("routine")
    ROUTINE("routine"),

    /**
     * The request should be done urgently.
     * 
     */
    @XmlEnumValue("urgent")
    URGENT("urgent"),

    /**
     * The request is time-critical.
     * 
     */
    @XmlEnumValue("stat")
    STAT("stat"),

    /**
     * The request should be acted on as soon as possible.
     * 
     */
    @XmlEnumValue("asap")
    ASAP("asap");
    private final java.lang.String value;

    ProcedureRequestPriorityList(java.lang.String v) {
        value = v;
    }

    public java.lang.String value() {
        return value;
    }

    public static ProcedureRequestPriorityList fromValue(java.lang.String v) {
        for (ProcedureRequestPriorityList c: ProcedureRequestPriorityList.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}