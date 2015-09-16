/*
 * Copyright (c) 2006-2007 Massachusetts General Hospital 
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the i2b2 Software License v1.0 
 * which accompanies this distribution. 
 * 
 * Contributors:
 * 		Kavishwar Wagholikar (kavi)
 * 		July 4, 2015
 */

/*
 * Copyright (c) 2006-2007 Massachusetts General Hospital 
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the i2b2 Software License v1.0 
 * which accompanies this distribution. 
 * 
 * Contributors:
 * 		Kavishwar Wagholikar (kavi)
 * 		July 4, 2015
 */
//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.03.11 at 12:26:00 PM EDT 
//


package org.hl7.fhir;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * An order for both supply of the medication and the instructions for administration of the medicine to a patient.
 * 
 * <p>Java class for MedicationPrescription.Dispense complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="MedicationPrescription.Dispense">
 *   &lt;complexContent>
 *     &lt;extension base="{http://hl7.org/fhir}BackboneElement">
 *       &lt;sequence>
 *         &lt;element name="medication" type="{http://hl7.org/fhir}ResourceReference" minOccurs="0"/>
 *         &lt;element name="validityPeriod" type="{http://hl7.org/fhir}Period" minOccurs="0"/>
 *         &lt;element name="numberOfRepeatsAllowed" type="{http://hl7.org/fhir}integer" minOccurs="0"/>
 *         &lt;element name="quantity" type="{http://hl7.org/fhir}Quantity" minOccurs="0"/>
 *         &lt;element name="expectedSupplyDuration" type="{http://hl7.org/fhir}Duration" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MedicationPrescription.Dispense", propOrder = {
    "medication",
    "validityPeriod",
    "numberOfRepeatsAllowed",
    "quantity",
    "expectedSupplyDuration"
})
public class MedicationPrescriptionDispense
    extends BackboneElement
{

    protected ResourceReference medication;
    protected Period validityPeriod;
    protected Integer numberOfRepeatsAllowed;
    protected Quantity quantity;
    protected Duration expectedSupplyDuration;

    /**
     * Gets the value of the medication property.
     * 
     * @return
     *     possible object is
     *     {@link ResourceReference }
     *     
     */
    public ResourceReference getMedication() {
        return medication;
    }

    /**
     * Sets the value of the medication property.
     * 
     * @param value
     *     allowed object is
     *     {@link ResourceReference }
     *     
     */
    public void setMedication(ResourceReference value) {
        this.medication = value;
    }

    /**
     * Gets the value of the validityPeriod property.
     * 
     * @return
     *     possible object is
     *     {@link Period }
     *     
     */
    public Period getValidityPeriod() {
        return validityPeriod;
    }

    /**
     * Sets the value of the validityPeriod property.
     * 
     * @param value
     *     allowed object is
     *     {@link Period }
     *     
     */
    public void setValidityPeriod(Period value) {
        this.validityPeriod = value;
    }

    /**
     * Gets the value of the numberOfRepeatsAllowed property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getNumberOfRepeatsAllowed() {
        return numberOfRepeatsAllowed;
    }

    /**
     * Sets the value of the numberOfRepeatsAllowed property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setNumberOfRepeatsAllowed(Integer value) {
        this.numberOfRepeatsAllowed = value;
    }

    /**
     * Gets the value of the quantity property.
     * 
     * @return
     *     possible object is
     *     {@link Quantity }
     *     
     */
    public Quantity getQuantity() {
        return quantity;
    }

    /**
     * Sets the value of the quantity property.
     * 
     * @param value
     *     allowed object is
     *     {@link Quantity }
     *     
     */
    public void setQuantity(Quantity value) {
        this.quantity = value;
    }

    /**
     * Gets the value of the expectedSupplyDuration property.
     * 
     * @return
     *     possible object is
     *     {@link Duration }
     *     
     */
    public Duration getExpectedSupplyDuration() {
        return expectedSupplyDuration;
    }

    /**
     * Sets the value of the expectedSupplyDuration property.
     * 
     * @param value
     *     allowed object is
     *     {@link Duration }
     *     
     */
    public void setExpectedSupplyDuration(Duration value) {
        this.expectedSupplyDuration = value;
    }

}
