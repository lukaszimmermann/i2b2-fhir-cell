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
 * <p>Java class for LocationStatus-list.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="LocationStatus-list">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="active"/>
 *     &lt;enumeration value="suspended"/>
 *     &lt;enumeration value="inactive"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "LocationStatus-list")
@XmlEnum
public enum LocationStatusList {


    /**
     * The location is operational.
     * 
     */
    @XmlEnumValue("active")
    ACTIVE("active"),

    /**
     * The location is temporarily closed.
     * 
     */
    @XmlEnumValue("suspended")
    SUSPENDED("suspended"),

    /**
     * The location is no longer used.
     * 
     */
    @XmlEnumValue("inactive")
    INACTIVE("inactive");
    private final java.lang.String value;

    LocationStatusList(java.lang.String v) {
        value = v;
    }

    public java.lang.String value() {
        return value;
    }

    public static LocationStatusList fromValue(java.lang.String v) {
        for (LocationStatusList c: LocationStatusList.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
