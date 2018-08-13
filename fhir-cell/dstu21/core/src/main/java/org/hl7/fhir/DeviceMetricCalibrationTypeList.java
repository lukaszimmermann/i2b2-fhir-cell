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
 * <p>Java class for DeviceMetricCalibrationType-list.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="DeviceMetricCalibrationType-list">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="unspecified"/>
 *     &lt;enumeration value="offset"/>
 *     &lt;enumeration value="gain"/>
 *     &lt;enumeration value="two-point"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "DeviceMetricCalibrationType-list")
@XmlEnum
public enum DeviceMetricCalibrationTypeList {


    /**
     * TODO
     * 
     */
    @XmlEnumValue("unspecified")
    UNSPECIFIED("unspecified"),

    /**
     * TODO
     * 
     */
    @XmlEnumValue("offset")
    OFFSET("offset"),

    /**
     * TODO
     * 
     */
    @XmlEnumValue("gain")
    GAIN("gain"),

    /**
     * TODO
     * 
     */
    @XmlEnumValue("two-point")
    TWO_POINT("two-point");
    private final java.lang.String value;

    DeviceMetricCalibrationTypeList(java.lang.String v) {
        value = v;
    }

    public java.lang.String value() {
        return value;
    }

    public static DeviceMetricCalibrationTypeList fromValue(java.lang.String v) {
        for (DeviceMetricCalibrationTypeList c: DeviceMetricCalibrationTypeList.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}