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
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * If the element is present, it must have either a @value, an @id, or extensions
 * 
 * <p>Java class for HealthcareService complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="HealthcareService">
 *   &lt;complexContent>
 *     &lt;extension base="{http://hl7.org/fhir}DomainResource">
 *       &lt;sequence>
 *         &lt;element name="identifier" type="{http://hl7.org/fhir}Identifier" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="providedBy" type="{http://hl7.org/fhir}Reference" minOccurs="0"/>
 *         &lt;element name="location" type="{http://hl7.org/fhir}Reference"/>
 *         &lt;element name="serviceCategory" type="{http://hl7.org/fhir}CodeableConcept" minOccurs="0"/>
 *         &lt;element name="serviceType" type="{http://hl7.org/fhir}HealthcareService.ServiceType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="serviceName" type="{http://hl7.org/fhir}string" minOccurs="0"/>
 *         &lt;element name="comment" type="{http://hl7.org/fhir}string" minOccurs="0"/>
 *         &lt;element name="extraDetails" type="{http://hl7.org/fhir}string" minOccurs="0"/>
 *         &lt;element name="photo" type="{http://hl7.org/fhir}Attachment" minOccurs="0"/>
 *         &lt;element name="telecom" type="{http://hl7.org/fhir}ContactPoint" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="coverageArea" type="{http://hl7.org/fhir}Reference" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="serviceProvisionCode" type="{http://hl7.org/fhir}CodeableConcept" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="eligibility" type="{http://hl7.org/fhir}CodeableConcept" minOccurs="0"/>
 *         &lt;element name="eligibilityNote" type="{http://hl7.org/fhir}string" minOccurs="0"/>
 *         &lt;element name="programName" type="{http://hl7.org/fhir}string" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="characteristic" type="{http://hl7.org/fhir}CodeableConcept" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="referralMethod" type="{http://hl7.org/fhir}CodeableConcept" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="publicKey" type="{http://hl7.org/fhir}string" minOccurs="0"/>
 *         &lt;element name="appointmentRequired" type="{http://hl7.org/fhir}boolean" minOccurs="0"/>
 *         &lt;element name="availableTime" type="{http://hl7.org/fhir}HealthcareService.AvailableTime" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="notAvailable" type="{http://hl7.org/fhir}HealthcareService.NotAvailable" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="availabilityExceptions" type="{http://hl7.org/fhir}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "HealthcareService", propOrder = {
    "identifier",
    "providedBy",
    "location",
    "serviceCategory",
    "serviceType",
    "serviceName",
    "comment",
    "extraDetails",
    "photo",
    "telecom",
    "coverageArea",
    "serviceProvisionCode",
    "eligibility",
    "eligibilityNote",
    "programName",
    "characteristic",
    "referralMethod",
    "publicKey",
    "appointmentRequired",
    "availableTime",
    "notAvailable",
    "availabilityExceptions"
})
public class HealthcareService
    extends DomainResource
{

    protected List<Identifier> identifier;
    protected Reference providedBy;
    @XmlElement(required = true)
    protected Reference location;
    protected CodeableConcept serviceCategory;
    protected List<HealthcareServiceServiceType> serviceType;
    protected String serviceName;
    protected String comment;
    protected String extraDetails;
    protected Attachment photo;
    protected List<ContactPoint> telecom;
    protected List<Reference> coverageArea;
    protected List<CodeableConcept> serviceProvisionCode;
    protected CodeableConcept eligibility;
    protected String eligibilityNote;
    protected List<String> programName;
    protected List<CodeableConcept> characteristic;
    protected List<CodeableConcept> referralMethod;
    protected String publicKey;
    protected Boolean appointmentRequired;
    protected List<HealthcareServiceAvailableTime> availableTime;
    protected List<HealthcareServiceNotAvailable> notAvailable;
    protected String availabilityExceptions;

    /**
     * Gets the value of the identifier property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the identifier property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getIdentifier().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Identifier }
     * 
     * 
     */
    public List<Identifier> getIdentifier() {
        if (identifier == null) {
            identifier = new ArrayList<Identifier>();
        }
        return this.identifier;
    }

    /**
     * Gets the value of the providedBy property.
     * 
     * @return
     *     possible object is
     *     {@link Reference }
     *     
     */
    public Reference getProvidedBy() {
        return providedBy;
    }

    /**
     * Sets the value of the providedBy property.
     * 
     * @param value
     *     allowed object is
     *     {@link Reference }
     *     
     */
    public void setProvidedBy(Reference value) {
        this.providedBy = value;
    }

    /**
     * Gets the value of the location property.
     * 
     * @return
     *     possible object is
     *     {@link Reference }
     *     
     */
    public Reference getLocation() {
        return location;
    }

    /**
     * Sets the value of the location property.
     * 
     * @param value
     *     allowed object is
     *     {@link Reference }
     *     
     */
    public void setLocation(Reference value) {
        this.location = value;
    }

    /**
     * Gets the value of the serviceCategory property.
     * 
     * @return
     *     possible object is
     *     {@link CodeableConcept }
     *     
     */
    public CodeableConcept getServiceCategory() {
        return serviceCategory;
    }

    /**
     * Sets the value of the serviceCategory property.
     * 
     * @param value
     *     allowed object is
     *     {@link CodeableConcept }
     *     
     */
    public void setServiceCategory(CodeableConcept value) {
        this.serviceCategory = value;
    }

    /**
     * Gets the value of the serviceType property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the serviceType property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getServiceType().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link HealthcareServiceServiceType }
     * 
     * 
     */
    public List<HealthcareServiceServiceType> getServiceType() {
        if (serviceType == null) {
            serviceType = new ArrayList<HealthcareServiceServiceType>();
        }
        return this.serviceType;
    }

    /**
     * Gets the value of the serviceName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getServiceName() {
        return serviceName;
    }

    /**
     * Sets the value of the serviceName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setServiceName(String value) {
        this.serviceName = value;
    }

    /**
     * Gets the value of the comment property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getComment() {
        return comment;
    }

    /**
     * Sets the value of the comment property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setComment(String value) {
        this.comment = value;
    }

    /**
     * Gets the value of the extraDetails property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getExtraDetails() {
        return extraDetails;
    }

    /**
     * Sets the value of the extraDetails property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setExtraDetails(String value) {
        this.extraDetails = value;
    }

    /**
     * Gets the value of the photo property.
     * 
     * @return
     *     possible object is
     *     {@link Attachment }
     *     
     */
    public Attachment getPhoto() {
        return photo;
    }

    /**
     * Sets the value of the photo property.
     * 
     * @param value
     *     allowed object is
     *     {@link Attachment }
     *     
     */
    public void setPhoto(Attachment value) {
        this.photo = value;
    }

    /**
     * Gets the value of the telecom property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the telecom property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getTelecom().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ContactPoint }
     * 
     * 
     */
    public List<ContactPoint> getTelecom() {
        if (telecom == null) {
            telecom = new ArrayList<ContactPoint>();
        }
        return this.telecom;
    }

    /**
     * Gets the value of the coverageArea property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the coverageArea property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCoverageArea().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Reference }
     * 
     * 
     */
    public List<Reference> getCoverageArea() {
        if (coverageArea == null) {
            coverageArea = new ArrayList<Reference>();
        }
        return this.coverageArea;
    }

    /**
     * Gets the value of the serviceProvisionCode property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the serviceProvisionCode property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getServiceProvisionCode().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link CodeableConcept }
     * 
     * 
     */
    public List<CodeableConcept> getServiceProvisionCode() {
        if (serviceProvisionCode == null) {
            serviceProvisionCode = new ArrayList<CodeableConcept>();
        }
        return this.serviceProvisionCode;
    }

    /**
     * Gets the value of the eligibility property.
     * 
     * @return
     *     possible object is
     *     {@link CodeableConcept }
     *     
     */
    public CodeableConcept getEligibility() {
        return eligibility;
    }

    /**
     * Sets the value of the eligibility property.
     * 
     * @param value
     *     allowed object is
     *     {@link CodeableConcept }
     *     
     */
    public void setEligibility(CodeableConcept value) {
        this.eligibility = value;
    }

    /**
     * Gets the value of the eligibilityNote property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEligibilityNote() {
        return eligibilityNote;
    }

    /**
     * Sets the value of the eligibilityNote property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEligibilityNote(String value) {
        this.eligibilityNote = value;
    }

    /**
     * Gets the value of the programName property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the programName property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getProgramName().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getProgramName() {
        if (programName == null) {
            programName = new ArrayList<String>();
        }
        return this.programName;
    }

    /**
     * Gets the value of the characteristic property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the characteristic property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCharacteristic().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link CodeableConcept }
     * 
     * 
     */
    public List<CodeableConcept> getCharacteristic() {
        if (characteristic == null) {
            characteristic = new ArrayList<CodeableConcept>();
        }
        return this.characteristic;
    }

    /**
     * Gets the value of the referralMethod property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the referralMethod property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getReferralMethod().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link CodeableConcept }
     * 
     * 
     */
    public List<CodeableConcept> getReferralMethod() {
        if (referralMethod == null) {
            referralMethod = new ArrayList<CodeableConcept>();
        }
        return this.referralMethod;
    }

    /**
     * Gets the value of the publicKey property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPublicKey() {
        return publicKey;
    }

    /**
     * Sets the value of the publicKey property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPublicKey(String value) {
        this.publicKey = value;
    }

    /**
     * Gets the value of the appointmentRequired property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean getAppointmentRequired() {
        return appointmentRequired;
    }

    /**
     * Sets the value of the appointmentRequired property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setAppointmentRequired(Boolean value) {
        this.appointmentRequired = value;
    }

    /**
     * Gets the value of the availableTime property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the availableTime property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAvailableTime().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link HealthcareServiceAvailableTime }
     * 
     * 
     */
    public List<HealthcareServiceAvailableTime> getAvailableTime() {
        if (availableTime == null) {
            availableTime = new ArrayList<HealthcareServiceAvailableTime>();
        }
        return this.availableTime;
    }

    /**
     * Gets the value of the notAvailable property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the notAvailable property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getNotAvailable().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link HealthcareServiceNotAvailable }
     * 
     * 
     */
    public List<HealthcareServiceNotAvailable> getNotAvailable() {
        if (notAvailable == null) {
            notAvailable = new ArrayList<HealthcareServiceNotAvailable>();
        }
        return this.notAvailable;
    }

    /**
     * Gets the value of the availabilityExceptions property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAvailabilityExceptions() {
        return availabilityExceptions;
    }

    /**
     * Sets the value of the availabilityExceptions property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAvailabilityExceptions(String value) {
        this.availabilityExceptions = value;
    }

}
