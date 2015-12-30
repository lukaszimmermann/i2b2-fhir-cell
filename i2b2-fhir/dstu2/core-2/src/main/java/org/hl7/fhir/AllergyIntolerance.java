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
 * If the element is present, it must have either a @value, an @id, or extensions
 * 
 * <p>Java class for AllergyIntolerance complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="AllergyIntolerance">
 *   &lt;complexContent>
 *     &lt;extension base="{http://hl7.org/fhir}DomainResource">
 *       &lt;sequence>
 *         &lt;element name="identifier" type="{http://hl7.org/fhir}Identifier" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="onset" type="{http://hl7.org/fhir}dateTime" minOccurs="0"/>
 *         &lt;element name="recordedDate" type="{http://hl7.org/fhir}dateTime" minOccurs="0"/>
 *         &lt;element name="recorder" type="{http://hl7.org/fhir}Reference" minOccurs="0"/>
 *         &lt;element name="patient" type="{http://hl7.org/fhir}Reference"/>
 *         &lt;element name="reporter" type="{http://hl7.org/fhir}Reference" minOccurs="0"/>
 *         &lt;element name="substance" type="{http://hl7.org/fhir}CodeableConcept"/>
 *         &lt;element name="status" type="{http://hl7.org/fhir}AllergyIntoleranceStatus" minOccurs="0"/>
 *         &lt;element name="criticality" type="{http://hl7.org/fhir}AllergyIntoleranceCriticality" minOccurs="0"/>
 *         &lt;element name="type" type="{http://hl7.org/fhir}AllergyIntoleranceType" minOccurs="0"/>
 *         &lt;element name="category" type="{http://hl7.org/fhir}AllergyIntoleranceCategory" minOccurs="0"/>
 *         &lt;element name="lastOccurence" type="{http://hl7.org/fhir}dateTime" minOccurs="0"/>
 *         &lt;element name="note" type="{http://hl7.org/fhir}Annotation" minOccurs="0"/>
 *         &lt;element name="reaction" type="{http://hl7.org/fhir}AllergyIntolerance.Reaction" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AllergyIntolerance", propOrder = {
    "identifier",
    "onset",
    "recordedDate",
    "recorder",
    "patient",
    "reporter",
    "substance",
    "status",
    "criticality",
    "type",
    "category",
    "lastOccurence",
    "note",
    "reaction"
})
@javax.xml.bind.annotation.XmlRootElement(name="AllergyIntolerance") 
public class AllergyIntolerance
    extends DomainResource
{

    protected List<Identifier> identifier;
    protected DateTime onset;
    protected DateTime recordedDate;
    protected Reference recorder;
    @XmlElement(required = true)
    protected Reference patient;
    protected Reference reporter;
    @XmlElement(required = true)
    protected CodeableConcept substance;
    protected AllergyIntoleranceStatus status;
    protected AllergyIntoleranceCriticality criticality;
    protected AllergyIntoleranceType type;
    protected AllergyIntoleranceCategory category;
    protected DateTime lastOccurence;
    protected Annotation note;
    protected List<AllergyIntoleranceReaction> reaction;

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
     * Gets the value of the recordedDate property.
     * 
     * @return
     *     possible object is
     *     {@link DateTime }
     *     
     */
    public DateTime getRecordedDate() {
        return recordedDate;
    }

    /**
     * Sets the value of the recordedDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link DateTime }
     *     
     */
    public void setRecordedDate(DateTime value) {
        this.recordedDate = value;
    }

    /**
     * Gets the value of the recorder property.
     * 
     * @return
     *     possible object is
     *     {@link Reference }
     *     
     */
    public Reference getRecorder() {
        return recorder;
    }

    /**
     * Sets the value of the recorder property.
     * 
     * @param value
     *     allowed object is
     *     {@link Reference }
     *     
     */
    public void setRecorder(Reference value) {
        this.recorder = value;
    }

    /**
     * Gets the value of the patient property.
     * 
     * @return
     *     possible object is
     *     {@link Reference }
     *     
     */
    public Reference getPatient() {
        return patient;
    }

    /**
     * Sets the value of the patient property.
     * 
     * @param value
     *     allowed object is
     *     {@link Reference }
     *     
     */
    public void setPatient(Reference value) {
        this.patient = value;
    }

    /**
     * Gets the value of the reporter property.
     * 
     * @return
     *     possible object is
     *     {@link Reference }
     *     
     */
    public Reference getReporter() {
        return reporter;
    }

    /**
     * Sets the value of the reporter property.
     * 
     * @param value
     *     allowed object is
     *     {@link Reference }
     *     
     */
    public void setReporter(Reference value) {
        this.reporter = value;
    }

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
     * Gets the value of the status property.
     * 
     * @return
     *     possible object is
     *     {@link AllergyIntoleranceStatus }
     *     
     */
    public AllergyIntoleranceStatus getStatus() {
        return status;
    }

    /**
     * Sets the value of the status property.
     * 
     * @param value
     *     allowed object is
     *     {@link AllergyIntoleranceStatus }
     *     
     */
    public void setStatus(AllergyIntoleranceStatus value) {
        this.status = value;
    }

    /**
     * Gets the value of the criticality property.
     * 
     * @return
     *     possible object is
     *     {@link AllergyIntoleranceCriticality }
     *     
     */
    public AllergyIntoleranceCriticality getCriticality() {
        return criticality;
    }

    /**
     * Sets the value of the criticality property.
     * 
     * @param value
     *     allowed object is
     *     {@link AllergyIntoleranceCriticality }
     *     
     */
    public void setCriticality(AllergyIntoleranceCriticality value) {
        this.criticality = value;
    }

    /**
     * Gets the value of the type property.
     * 
     * @return
     *     possible object is
     *     {@link AllergyIntoleranceType }
     *     
     */
    public AllergyIntoleranceType getType() {
        return type;
    }

    /**
     * Sets the value of the type property.
     * 
     * @param value
     *     allowed object is
     *     {@link AllergyIntoleranceType }
     *     
     */
    public void setType(AllergyIntoleranceType value) {
        this.type = value;
    }

    /**
     * Gets the value of the category property.
     * 
     * @return
     *     possible object is
     *     {@link AllergyIntoleranceCategory }
     *     
     */
    public AllergyIntoleranceCategory getCategory() {
        return category;
    }

    /**
     * Sets the value of the category property.
     * 
     * @param value
     *     allowed object is
     *     {@link AllergyIntoleranceCategory }
     *     
     */
    public void setCategory(AllergyIntoleranceCategory value) {
        this.category = value;
    }

    /**
     * Gets the value of the lastOccurence property.
     * 
     * @return
     *     possible object is
     *     {@link DateTime }
     *     
     */
    public DateTime getLastOccurence() {
        return lastOccurence;
    }

    /**
     * Sets the value of the lastOccurence property.
     * 
     * @param value
     *     allowed object is
     *     {@link DateTime }
     *     
     */
    public void setLastOccurence(DateTime value) {
        this.lastOccurence = value;
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

    /**
     * Gets the value of the reaction property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the reaction property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getReaction().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link AllergyIntoleranceReaction }
     * 
     * 
     */
    public List<AllergyIntoleranceReaction> getReaction() {
        if (reaction == null) {
            reaction = new ArrayList<AllergyIntoleranceReaction>();
        }
        return this.reaction;
    }

}
