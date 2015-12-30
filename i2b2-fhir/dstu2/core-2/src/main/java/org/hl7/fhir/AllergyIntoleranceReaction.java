//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.12.30 at 02:43:29 AM EST 
//


package org.hl7.fhir;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * Risk of harmful or undesirable, physiological response which is unique to an individual and associated with exposure to a substance.
 * 
 * <p>Java class for AllergyIntolerance.Reaction complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="AllergyIntolerance.Reaction">
 *   &lt;complexContent>
 *     &lt;extension base="{http://hl7.org/fhir}BackboneElement">
 *       &lt;sequence>
 *         &lt;element name="substance" type="{http://hl7.org/fhir}CodeableConcept" minOccurs="0"/>
 *         &lt;element name="certainty" type="{http://hl7.org/fhir}AllergyIntoleranceCertainty" minOccurs="0"/>
 *         &lt;element name="manifestation" type="{http://hl7.org/fhir}CodeableConcept" maxOccurs="unbounded"/>
 *         &lt;element name="description" type="{http://hl7.org/fhir}string" minOccurs="0"/>
 *         &lt;element name="onset" type="{http://hl7.org/fhir}dateTime" minOccurs="0"/>
 *         &lt;element name="severity" type="{http://hl7.org/fhir}AllergyIntoleranceSeverity" minOccurs="0"/>
 *         &lt;element name="exposureRoute" type="{http://hl7.org/fhir}CodeableConcept" minOccurs="0"/>
 *         &lt;element name="note" type="{http://hl7.org/fhir}Annotation" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AllergyIntolerance.Reaction", propOrder = {
    "substance",
    "certainty",
    "manifestation",
    "description",
    "onset",
    "severity",
    "exposureRoute",
    "note"
})
@javax.xml.bind.annotation.XmlRootElement(name="AllergyIntoleranceReaction") 
public class AllergyIntoleranceReaction
    extends BackboneElement
{

    protected CodeableConcept substance;
    protected AllergyIntoleranceCertainty certainty;
    @XmlElement(required = true)
    protected List<CodeableConcept> manifestation;
    protected String description;
    protected DateTime onset;
    protected AllergyIntoleranceSeverity severity;
    protected CodeableConcept exposureRoute;
    protected Annotation note;

    /**
     * Gets the value of the substance property.
     * 
     * @return
     *     possible object is
     *     {@link CodeableConcept }
     *     
     */
    public CodeableConcept getSubstance() {
        return substance;
    }

    /**
     * Sets the value of the substance property.
     * 
     * @param value
     *     allowed object is
     *     {@link CodeableConcept }
     *     
     */
    public void setSubstance(CodeableConcept value) {
        this.substance = value;
    }

    /**
     * Gets the value of the certainty property.
     * 
     * @return
     *     possible object is
     *     {@link AllergyIntoleranceCertainty }
     *     
     */
    public AllergyIntoleranceCertainty getCertainty() {
        return certainty;
    }

    /**
     * Sets the value of the certainty property.
     * 
     * @param value
     *     allowed object is
     *     {@link AllergyIntoleranceCertainty }
     *     
     */
    public void setCertainty(AllergyIntoleranceCertainty value) {
        this.certainty = value;
    }

    /**
     * Gets the value of the manifestation property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the manifestation property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getManifestation().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link CodeableConcept }
     * 
     * 
     */
    public List<CodeableConcept> getManifestation() {
        if (manifestation == null) {
            manifestation = new ArrayList<CodeableConcept>();
        }
        return this.manifestation;
    }

    /**
     * Gets the value of the description property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the value of the description property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescription(String value) {
        this.description = value;
    }

    /**
     * Gets the value of the onset property.
     * 
     * @return
     *     possible object is
     *     {@link DateTime }
     *     
     */
    public DateTime getOnset() {
        return onset;
    }

    /**
     * Sets the value of the onset property.
     * 
     * @param value
     *     allowed object is
     *     {@link DateTime }
     *     
     */
    public void setOnset(DateTime value) {
        this.onset = value;
    }

    /**
     * Gets the value of the severity property.
     * 
     * @return
     *     possible object is
     *     {@link AllergyIntoleranceSeverity }
     *     
     */
    public AllergyIntoleranceSeverity getSeverity() {
        return severity;
    }

    /**
     * Sets the value of the severity property.
     * 
     * @param value
     *     allowed object is
     *     {@link AllergyIntoleranceSeverity }
     *     
     */
    public void setSeverity(AllergyIntoleranceSeverity value) {
        this.severity = value;
    }

    /**
     * Gets the value of the exposureRoute property.
     * 
     * @return
     *     possible object is
     *     {@link CodeableConcept }
     *     
     */
    public CodeableConcept getExposureRoute() {
        return exposureRoute;
    }

    /**
     * Sets the value of the exposureRoute property.
     * 
     * @param value
     *     allowed object is
     *     {@link CodeableConcept }
     *     
     */
    public void setExposureRoute(CodeableConcept value) {
        this.exposureRoute = value;
    }

    /**
     * Gets the value of the note property.
     * 
     * @return
     *     possible object is
     *     {@link Annotation }
     *     
     */
    public Annotation getNote() {
        return note;
    }

    /**
     * Sets the value of the note property.
     * 
     * @param value
     *     allowed object is
     *     {@link Annotation }
     *     
     */
    public void setNote(Annotation value) {
        this.note = value;
    }

}
