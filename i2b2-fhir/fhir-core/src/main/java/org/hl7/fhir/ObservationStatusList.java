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
 * <p>Java class for ObservationStatus-list.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="ObservationStatus-list">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="registered"/>
 *     &lt;enumeration value="preliminary"/>
 *     &lt;enumeration value="final"/>
 *     &lt;enumeration value="amended"/>
 *     &lt;enumeration value="cancelled"/>
 *     &lt;enumeration value="entered-in-error"/>
 *     &lt;enumeration value="unknown"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "ObservationStatus-list")
@XmlEnum
public enum ObservationStatusList {


    /**
     * The existence of the observation is registered, but there is no result yet available.
     * 
     */
    @XmlEnumValue("registered")
    REGISTERED("registered"),

    /**
     * This is an initial or interim observation: data may be incomplete or unverified.
     * 
     */
    @XmlEnumValue("preliminary")
    PRELIMINARY("preliminary"),

    /**
     * The observation is complete and verified by an authorized person.
     * 
     */
    @XmlEnumValue("final")
    FINAL("final"),

    /**
     * The observation has been modified subsequent to being Final, and is complete and verified by an authorized person.
     * 
     */
    @XmlEnumValue("amended")
    AMENDED("amended"),

    /**
     * The observation is unavailable because the measurement was not started or not completed (also sometimes called "aborted").
     * 
     */
    @XmlEnumValue("cancelled")
    CANCELLED("cancelled"),

    /**
     * The observation has been withdrawn following previous Final release.
     * 
     */
    @XmlEnumValue("entered-in-error")
    ENTERED_IN_ERROR("entered-in-error"),

    /**
     * The observation status is unknown.  Note that "unknown" is a value of last resort and every attempt should be made to provide a meaningful value other than "unknown".
     * 
     */
    @XmlEnumValue("unknown")
    UNKNOWN("unknown");
    private final java.lang.String value;

    ObservationStatusList(java.lang.String v) {
        value = v;
    }

    public java.lang.String value() {
        return value;
    }

    public static ObservationStatusList fromValue(java.lang.String v) {
        for (ObservationStatusList c: ObservationStatusList.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
