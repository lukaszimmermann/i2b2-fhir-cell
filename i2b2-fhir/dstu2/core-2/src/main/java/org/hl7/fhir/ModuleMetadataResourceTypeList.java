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
 * <p>Java class for ModuleMetadataResourceType-list.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="ModuleMetadataResourceType-list">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="documentation"/>
 *     &lt;enumeration value="evidence"/>
 *     &lt;enumeration value="citation"/>
 *     &lt;enumeration value="predecessor"/>
 *     &lt;enumeration value="successor"/>
 *     &lt;enumeration value="derived-from"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "ModuleMetadataResourceType-list")
@XmlEnum
public enum ModuleMetadataResourceTypeList {


    /**
     * Additional documentation for the module
     * 
     */
    @XmlEnumValue("documentation")
    DOCUMENTATION("documentation"),

    /**
     * Supporting evidence for the module
     * 
     */
    @XmlEnumValue("evidence")
    EVIDENCE("evidence"),

    /**
     * Bibliographic citation for the module
     * 
     */
    @XmlEnumValue("citation")
    CITATION("citation"),

    /**
     * The previous version of the module
     * 
     */
    @XmlEnumValue("predecessor")
    PREDECESSOR("predecessor"),

    /**
     * The next version of the module
     * 
     */
    @XmlEnumValue("successor")
    SUCCESSOR("successor"),

    /**
     * The module is derived from the resource
     * 
     */
    @XmlEnumValue("derived-from")
    DERIVED_FROM("derived-from");
    private final java.lang.String value;

    ModuleMetadataResourceTypeList(java.lang.String v) {
        value = v;
    }

    public java.lang.String value() {
        return value;
    }

    public static ModuleMetadataResourceTypeList fromValue(java.lang.String v) {
        for (ModuleMetadataResourceTypeList c: ModuleMetadataResourceTypeList.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
