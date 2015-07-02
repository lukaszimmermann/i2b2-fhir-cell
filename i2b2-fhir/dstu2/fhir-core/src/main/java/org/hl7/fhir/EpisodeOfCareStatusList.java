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
 * <p>Java class for EpisodeOfCareStatus-list.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="EpisodeOfCareStatus-list">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="planned"/>
 *     &lt;enumeration value="waitlist"/>
 *     &lt;enumeration value="active"/>
 *     &lt;enumeration value="onhold"/>
 *     &lt;enumeration value="finished"/>
 *     &lt;enumeration value="cancelled"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "EpisodeOfCareStatus-list")
@XmlEnum
public enum EpisodeOfCareStatusList {


    /**
     * This episode of care is planned to start at the date specified in the period.start. During this status an organization may perform assessments to determine if they are eligible to receive services, or be organizing to make resources available to provide care services.
     * 
     */
    @XmlEnumValue("planned")
    PLANNED("planned"),

    /**
     * This episode has been placed on a waitlist, pending the episode being made active (or cancelled).
     * 
     */
    @XmlEnumValue("waitlist")
    WAITLIST("waitlist"),

    /**
     * This episode of care is current.
     * 
     */
    @XmlEnumValue("active")
    ACTIVE("active"),

    /**
     * This episode of care is on hold, the organization has limited responsibility for the patient (such as while on respite).
     * 
     */
    @XmlEnumValue("onhold")
    ONHOLD("onhold"),

    /**
     * This episode of care is finished at the organization is not expecting to be providing care to the patient. Can also be known as "closed", "completed" or other similar terms.
     * 
     */
    @XmlEnumValue("finished")
    FINISHED("finished"),

    /**
     * The episode of care was cancelled, or withdrawn from service, often selected during the planned stage as the patient may have gone elsewhere, or the circumstances have changed and the organization is unable to provide the care. It indicates that services terminated outside the planned/expected workflow.
     * 
     */
    @XmlEnumValue("cancelled")
    CANCELLED("cancelled");
    private final java.lang.String value;

    EpisodeOfCareStatusList(java.lang.String v) {
        value = v;
    }

    public java.lang.String value() {
        return value;
    }

    public static EpisodeOfCareStatusList fromValue(java.lang.String v) {
        for (EpisodeOfCareStatusList c: EpisodeOfCareStatusList.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
