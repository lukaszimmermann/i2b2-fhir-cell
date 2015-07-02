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

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * A person who is directly or indirectly involved in the provisioning of healthcare.
 * 
 * <p>Java class for Practitioner.PractitionerRole complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Practitioner.PractitionerRole">
 *   &lt;complexContent>
 *     &lt;extension base="{http://hl7.org/fhir}BackboneElement">
 *       &lt;sequence>
 *         &lt;element name="managingOrganization" type="{http://hl7.org/fhir}Reference" minOccurs="0"/>
 *         &lt;element name="role" type="{http://hl7.org/fhir}CodeableConcept" minOccurs="0"/>
 *         &lt;element name="specialty" type="{http://hl7.org/fhir}CodeableConcept" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="period" type="{http://hl7.org/fhir}Period" minOccurs="0"/>
 *         &lt;element name="location" type="{http://hl7.org/fhir}Reference" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="healthcareService" type="{http://hl7.org/fhir}Reference" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Practitioner.PractitionerRole", propOrder = {
    "managingOrganization",
    "role",
    "specialty",
    "period",
    "location",
    "healthcareService"
})
public class PractitionerPractitionerRole
    extends BackboneElement
{

    protected Reference managingOrganization;
    protected CodeableConcept role;
    protected List<CodeableConcept> specialty;
    protected Period period;
    protected List<Reference> location;
    protected List<Reference> healthcareService;

    /**
     * Gets the value of the managingOrganization property.
     * 
     * @return
     *     possible object is
     *     {@link Reference }
     *     
     */
    public Reference getManagingOrganization() {
        return managingOrganization;
    }

    /**
     * Sets the value of the managingOrganization property.
     * 
     * @param value
     *     allowed object is
     *     {@link Reference }
     *     
     */
    public void setManagingOrganization(Reference value) {
        this.managingOrganization = value;
    }

    /**
     * Gets the value of the role property.
     * 
     * @return
     *     possible object is
     *     {@link CodeableConcept }
     *     
     */
    public CodeableConcept getRole() {
        return role;
    }

    /**
     * Sets the value of the role property.
     * 
     * @param value
     *     allowed object is
     *     {@link CodeableConcept }
     *     
     */
    public void setRole(CodeableConcept value) {
        this.role = value;
    }

    /**
     * Gets the value of the specialty property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the specialty property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSpecialty().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link CodeableConcept }
     * 
     * 
     */
    public List<CodeableConcept> getSpecialty() {
        if (specialty == null) {
            specialty = new ArrayList<CodeableConcept>();
        }
        return this.specialty;
    }

    /**
     * Gets the value of the period property.
     * 
     * @return
     *     possible object is
     *     {@link Period }
     *     
     */
    public Period getPeriod() {
        return period;
    }

    /**
     * Sets the value of the period property.
     * 
     * @param value
     *     allowed object is
     *     {@link Period }
     *     
     */
    public void setPeriod(Period value) {
        this.period = value;
    }

    /**
     * Gets the value of the location property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the location property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getLocation().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Reference }
     * 
     * 
     */
    public List<Reference> getLocation() {
        if (location == null) {
            location = new ArrayList<Reference>();
        }
        return this.location;
    }

    /**
     * Gets the value of the healthcareService property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the healthcareService property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getHealthcareService().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Reference }
     * 
     * 
     */
    public List<Reference> getHealthcareService() {
        if (healthcareService == null) {
            healthcareService = new ArrayList<Reference>();
        }
        return this.healthcareService;
    }

}
