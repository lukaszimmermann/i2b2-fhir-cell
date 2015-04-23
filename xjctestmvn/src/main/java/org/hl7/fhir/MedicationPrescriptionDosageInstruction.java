//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.04.23 at 01:15:20 PM EDT 
//


package org.hl7.fhir;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * An order for both supply of the medication and the instructions for administration of the medicine to a patient.
 * 
 * <p>Java class for MedicationPrescription.DosageInstruction complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="MedicationPrescription.DosageInstruction">
 *   &lt;complexContent>
 *     &lt;extension base="{http://hl7.org/fhir}BackboneElement">
 *       &lt;sequence>
 *         &lt;element name="text" type="{http://hl7.org/fhir}string" minOccurs="0"/>
 *         &lt;element name="additionalInstructions" type="{http://hl7.org/fhir}CodeableConcept" minOccurs="0"/>
 *         &lt;choice minOccurs="0">
 *           &lt;element name="timingDateTime" type="{http://hl7.org/fhir}dateTime"/>
 *           &lt;element name="timingPeriod" type="{http://hl7.org/fhir}Period"/>
 *           &lt;element name="timingSchedule" type="{http://hl7.org/fhir}Schedule"/>
 *         &lt;/choice>
 *         &lt;choice minOccurs="0">
 *           &lt;element name="asNeededBoolean" type="{http://hl7.org/fhir}boolean"/>
 *           &lt;element name="asNeededCodeableConcept" type="{http://hl7.org/fhir}CodeableConcept"/>
 *         &lt;/choice>
 *         &lt;element name="site" type="{http://hl7.org/fhir}CodeableConcept" minOccurs="0"/>
 *         &lt;element name="route" type="{http://hl7.org/fhir}CodeableConcept" minOccurs="0"/>
 *         &lt;element name="method" type="{http://hl7.org/fhir}CodeableConcept" minOccurs="0"/>
 *         &lt;element name="doseQuantity" type="{http://hl7.org/fhir}Quantity" minOccurs="0"/>
 *         &lt;element name="rate" type="{http://hl7.org/fhir}Ratio" minOccurs="0"/>
 *         &lt;element name="maxDosePerPeriod" type="{http://hl7.org/fhir}Ratio" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MedicationPrescription.DosageInstruction", propOrder = {
    "text",
    "additionalInstructions",
    "timingDateTime",
    "timingPeriod",
    "timingSchedule",
    "asNeededBoolean",
    "asNeededCodeableConcept",
    "site",
    "route",
    "method",
    "doseQuantity",
    "rate",
    "maxDosePerPeriod"
})
public class MedicationPrescriptionDosageInstruction
    extends BackboneElement
{

    protected String text;
    protected CodeableConcept additionalInstructions;
    protected DateTime timingDateTime;
    protected Period timingPeriod;
    protected Schedule timingSchedule;
    protected Boolean asNeededBoolean;
    protected CodeableConcept asNeededCodeableConcept;
    protected CodeableConcept site;
    protected CodeableConcept route;
    protected CodeableConcept method;
    protected Quantity doseQuantity;
    protected Ratio rate;
    protected Ratio maxDosePerPeriod;

    /**
     * Gets the value of the text property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getText() {
        return text;
    }

    /**
     * Sets the value of the text property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setText(String value) {
        this.text = value;
    }

    /**
     * Gets the value of the additionalInstructions property.
     * 
     * @return
     *     possible object is
     *     {@link CodeableConcept }
     *     
     */
    public CodeableConcept getAdditionalInstructions() {
        return additionalInstructions;
    }

    /**
     * Sets the value of the additionalInstructions property.
     * 
     * @param value
     *     allowed object is
     *     {@link CodeableConcept }
     *     
     */
    public void setAdditionalInstructions(CodeableConcept value) {
        this.additionalInstructions = value;
    }

    /**
     * Gets the value of the timingDateTime property.
     * 
     * @return
     *     possible object is
     *     {@link DateTime }
     *     
     */
    public DateTime getTimingDateTime() {
        return timingDateTime;
    }

    /**
     * Sets the value of the timingDateTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link DateTime }
     *     
     */
    public void setTimingDateTime(DateTime value) {
        this.timingDateTime = value;
    }

    /**
     * Gets the value of the timingPeriod property.
     * 
     * @return
     *     possible object is
     *     {@link Period }
     *     
     */
    public Period getTimingPeriod() {
        return timingPeriod;
    }

    /**
     * Sets the value of the timingPeriod property.
     * 
     * @param value
     *     allowed object is
     *     {@link Period }
     *     
     */
    public void setTimingPeriod(Period value) {
        this.timingPeriod = value;
    }

    /**
     * Gets the value of the timingSchedule property.
     * 
     * @return
     *     possible object is
     *     {@link Schedule }
     *     
     */
    public Schedule getTimingSchedule() {
        return timingSchedule;
    }

    /**
     * Sets the value of the timingSchedule property.
     * 
     * @param value
     *     allowed object is
     *     {@link Schedule }
     *     
     */
    public void setTimingSchedule(Schedule value) {
        this.timingSchedule = value;
    }

    /**
     * Gets the value of the asNeededBoolean property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean getAsNeededBoolean() {
        return asNeededBoolean;
    }

    /**
     * Sets the value of the asNeededBoolean property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setAsNeededBoolean(Boolean value) {
        this.asNeededBoolean = value;
    }

    /**
     * Gets the value of the asNeededCodeableConcept property.
     * 
     * @return
     *     possible object is
     *     {@link CodeableConcept }
     *     
     */
    public CodeableConcept getAsNeededCodeableConcept() {
        return asNeededCodeableConcept;
    }

    /**
     * Sets the value of the asNeededCodeableConcept property.
     * 
     * @param value
     *     allowed object is
     *     {@link CodeableConcept }
     *     
     */
    public void setAsNeededCodeableConcept(CodeableConcept value) {
        this.asNeededCodeableConcept = value;
    }

    /**
     * Gets the value of the site property.
     * 
     * @return
     *     possible object is
     *     {@link CodeableConcept }
     *     
     */
    public CodeableConcept getSite() {
        return site;
    }

    /**
     * Sets the value of the site property.
     * 
     * @param value
     *     allowed object is
     *     {@link CodeableConcept }
     *     
     */
    public void setSite(CodeableConcept value) {
        this.site = value;
    }

    /**
     * Gets the value of the route property.
     * 
     * @return
     *     possible object is
     *     {@link CodeableConcept }
     *     
     */
    public CodeableConcept getRoute() {
        return route;
    }

    /**
     * Sets the value of the route property.
     * 
     * @param value
     *     allowed object is
     *     {@link CodeableConcept }
     *     
     */
    public void setRoute(CodeableConcept value) {
        this.route = value;
    }

    /**
     * Gets the value of the method property.
     * 
     * @return
     *     possible object is
     *     {@link CodeableConcept }
     *     
     */
    public CodeableConcept getMethod() {
        return method;
    }

    /**
     * Sets the value of the method property.
     * 
     * @param value
     *     allowed object is
     *     {@link CodeableConcept }
     *     
     */
    public void setMethod(CodeableConcept value) {
        this.method = value;
    }

    /**
     * Gets the value of the doseQuantity property.
     * 
     * @return
     *     possible object is
     *     {@link Quantity }
     *     
     */
    public Quantity getDoseQuantity() {
        return doseQuantity;
    }

    /**
     * Sets the value of the doseQuantity property.
     * 
     * @param value
     *     allowed object is
     *     {@link Quantity }
     *     
     */
    public void setDoseQuantity(Quantity value) {
        this.doseQuantity = value;
    }

    /**
     * Gets the value of the rate property.
     * 
     * @return
     *     possible object is
     *     {@link Ratio }
     *     
     */
    public Ratio getRate() {
        return rate;
    }

    /**
     * Sets the value of the rate property.
     * 
     * @param value
     *     allowed object is
     *     {@link Ratio }
     *     
     */
    public void setRate(Ratio value) {
        this.rate = value;
    }

    /**
     * Gets the value of the maxDosePerPeriod property.
     * 
     * @return
     *     possible object is
     *     {@link Ratio }
     *     
     */
    public Ratio getMaxDosePerPeriod() {
        return maxDosePerPeriod;
    }

    /**
     * Sets the value of the maxDosePerPeriod property.
     * 
     * @param value
     *     allowed object is
     *     {@link Ratio }
     *     
     */
    public void setMaxDosePerPeriod(Ratio value) {
        this.maxDosePerPeriod = value;
    }

}
