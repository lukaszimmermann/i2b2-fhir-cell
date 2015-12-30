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
 * <p>Java class for GuidanceResponseStatus-list.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="GuidanceResponseStatus-list">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="success"/>
 *     &lt;enumeration value="data-requested"/>
 *     &lt;enumeration value="data-required"/>
 *     &lt;enumeration value="in-progress"/>
 *     &lt;enumeration value="failure"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "GuidanceResponseStatus-list")
@XmlEnum
public enum GuidanceResponseStatusList {


    /**
     * The request was processed successfully
     * 
     */
    @XmlEnumValue("success")
    SUCCESS("success"),

    /**
     * The request was processed successfully, but more data may result in a more complete evaluation
     * 
     */
    @XmlEnumValue("data-requested")
    DATA_REQUESTED("data-requested"),

    /**
     * The request was processed, but more data is required to complete the evaluation
     * 
     */
    @XmlEnumValue("data-required")
    DATA_REQUIRED("data-required"),

    /**
     * The request is currently being processed
     * 
     */
    @XmlEnumValue("in-progress")
    IN_PROGRESS("in-progress"),

    /**
     * The request was not processed successfully
     * 
     */
    @XmlEnumValue("failure")
    FAILURE("failure");
    private final java.lang.String value;

    GuidanceResponseStatusList(java.lang.String v) {
        value = v;
    }

    public java.lang.String value() {
        return value;
    }

    public static GuidanceResponseStatusList fromValue(java.lang.String v) {
        for (GuidanceResponseStatusList c: GuidanceResponseStatusList.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
