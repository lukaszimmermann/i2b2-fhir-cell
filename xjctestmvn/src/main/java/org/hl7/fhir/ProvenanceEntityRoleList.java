//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.04.22 at 01:13:51 PM EDT 
//


package org.hl7.fhir;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ProvenanceEntityRole-list.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="ProvenanceEntityRole-list">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="derivation"/>
 *     &lt;enumeration value="revision"/>
 *     &lt;enumeration value="quotation"/>
 *     &lt;enumeration value="source"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "ProvenanceEntityRole-list")
@XmlEnum
public enum ProvenanceEntityRoleList {


    /**
     * A transformation of an entity into another, an update of an entity resulting in a new one, or the construction of a new entity based on a preexisting entity.
     * 
     */
    @XmlEnumValue("derivation")
    DERIVATION("derivation"),

    /**
     * A derivation for which the resulting entity is a revised version of some original.
     * 
     */
    @XmlEnumValue("revision")
    REVISION("revision"),

    /**
     * The repeat of (some or all of) an entity, such as text or image, by someone who may or may not be its original author.
     * 
     */
    @XmlEnumValue("quotation")
    QUOTATION("quotation"),

    /**
     * A primary source for a topic refers to something produced by some agent with direct experience and knowledge about the topic, at the time of the topic's study, without benefit from hindsight.
     * 
     */
    @XmlEnumValue("source")
    SOURCE("source");
    private final java.lang.String value;

    ProvenanceEntityRoleList(java.lang.String v) {
        value = v;
    }

    public java.lang.String value() {
        return value;
    }

    public static ProvenanceEntityRoleList fromValue(java.lang.String v) {
        for (ProvenanceEntityRoleList c: ProvenanceEntityRoleList.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
