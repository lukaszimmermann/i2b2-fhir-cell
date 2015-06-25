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
 * <p>Java class for DiagnosticReportStatus-list.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="DiagnosticReportStatus-list">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="registered"/>
 *     &lt;enumeration value="partial"/>
 *     &lt;enumeration value="final"/>
 *     &lt;enumeration value="corrected"/>
 *     &lt;enumeration value="appended"/>
 *     &lt;enumeration value="cancelled"/>
 *     &lt;enumeration value="entered-in-error"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "DiagnosticReportStatus-list")
@XmlEnum
public enum DiagnosticReportStatusList {


    /**
     * The existence of the report is registered, but there is nothing yet available.
     * 
     */
    @XmlEnumValue("registered")
    REGISTERED("registered"),

    /**
     * This is a partial (e.g. initial, interim or preliminary) report: data in the report may be incomplete or unverified.
     * 
     */
    @XmlEnumValue("partial")
    PARTIAL("partial"),

    /**
     * The report is complete and verified by an authorized person.
     * 
     */
    @XmlEnumValue("final")
    FINAL("final"),

    /**
     * The report has been modified subsequent to being Final, and is complete and verified by an authorized person.
     * 
     */
    @XmlEnumValue("corrected")
    CORRECTED("corrected"),

    /**
     * The report has been modified subsequent to being Final, and is complete and verified by an authorized person. New content has been added, but existing content hasn't changed.
     * 
     */
    @XmlEnumValue("appended")
    APPENDED("appended"),

    /**
     * The report is unavailable because the measurement was not started or not completed (also sometimes called "aborted").
     * 
     */
    @XmlEnumValue("cancelled")
    CANCELLED("cancelled"),

    /**
     * The report has been withdrawn following previous Final release.
     * 
     */
    @XmlEnumValue("entered-in-error")
    ENTERED_IN_ERROR("entered-in-error");
    private final java.lang.String value;

    DiagnosticReportStatusList(java.lang.String v) {
        value = v;
    }

    public java.lang.String value() {
        return value;
    }

    public static DiagnosticReportStatusList fromValue(java.lang.String v) {
        for (DiagnosticReportStatusList c: DiagnosticReportStatusList.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
