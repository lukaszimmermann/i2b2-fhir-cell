//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.10.03 at 10:58:19 AM EDT 
//


package org.hl7.fhir;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * If the element is present, it must have either a @value, an @id, or extensions
 * 
 * <p>Java class for Condition complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Condition">
 *   &lt;complexContent>
 *     &lt;extension base="{http://hl7.org/fhir}DomainResource">
 *       &lt;sequence>
 *         &lt;element name="identifier" type="{http://hl7.org/fhir}Identifier" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="patient" type="{http://hl7.org/fhir}Reference"/>
 *         &lt;element name="encounter" type="{http://hl7.org/fhir}Reference" minOccurs="0"/>
 *         &lt;element name="asserter" type="{http://hl7.org/fhir}Reference" minOccurs="0"/>
 *         &lt;element name="dateRecorded" type="{http://hl7.org/fhir}date" minOccurs="0"/>
 *         &lt;element name="code" type="{http://hl7.org/fhir}CodeableConcept"/>
 *         &lt;element name="category" type="{http://hl7.org/fhir}CodeableConcept" minOccurs="0"/>
 *         &lt;element name="clinicalStatus" type="{http://hl7.org/fhir}code" minOccurs="0"/>
 *         &lt;element name="verificationStatus" type="{http://hl7.org/fhir}ConditionVerificationStatus"/>
 *         &lt;element name="severity" type="{http://hl7.org/fhir}CodeableConcept" minOccurs="0"/>
 *         &lt;choice minOccurs="0">
 *           &lt;element name="onsetDateTime" type="{http://hl7.org/fhir}dateTime"/>
 *           &lt;element name="onsetQuantity" type="{http://hl7.org/fhir}Age"/>
 *           &lt;element name="onsetPeriod" type="{http://hl7.org/fhir}Period"/>
 *           &lt;element name="onsetRange" type="{http://hl7.org/fhir}Range"/>
 *           &lt;element name="onsetString" type="{http://hl7.org/fhir}string"/>
 *         &lt;/choice>
 *         &lt;choice minOccurs="0">
 *           &lt;element name="abatementDateTime" type="{http://hl7.org/fhir}dateTime"/>
 *           &lt;element name="abatementQuantity" type="{http://hl7.org/fhir}Age"/>
 *           &lt;element name="abatementBoolean" type="{http://hl7.org/fhir}boolean"/>
 *           &lt;element name="abatementPeriod" type="{http://hl7.org/fhir}Period"/>
 *           &lt;element name="abatementRange" type="{http://hl7.org/fhir}Range"/>
 *           &lt;element name="abatementString" type="{http://hl7.org/fhir}string"/>
 *         &lt;/choice>
 *         &lt;element name="stage" type="{http://hl7.org/fhir}Condition.Stage" minOccurs="0"/>
 *         &lt;element name="evidence" type="{http://hl7.org/fhir}Condition.Evidence" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="bodySite" type="{http://hl7.org/fhir}CodeableConcept" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="notes" type="{http://hl7.org/fhir}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Condition", propOrder = {
    "identifier",
    "patient",
    "encounter",
    "asserter",
    "dateRecorded",
    "code",
    "category",
    "clinicalStatus",
    "verificationStatus",
    "severity",
    "onsetDateTime",
    "onsetQuantity",
    "onsetPeriod",
    "onsetRange",
    "onsetString",
    "abatementDateTime",
    "abatementQuantity",
    "abatementBoolean",
    "abatementPeriod",
    "abatementRange",
    "abatementString",
    "stage",
    "evidence",
    "bodySite",
    "notes"
})
@XmlRootElement(name="Condition")
public class Condition
    extends DomainResource
{

    protected List<Identifier> identifier;
    @XmlElement(required = true)
    protected Reference patient;
    protected Reference encounter;
    protected Reference asserter;
    protected Date dateRecorded;
    @XmlElement(required = true)
    protected CodeableConcept code;
    protected CodeableConcept category;
    protected Code clinicalStatus;
    @XmlElement(required = true)
    protected ConditionVerificationStatus verificationStatus;
    protected CodeableConcept severity;
    protected DateTime onsetDateTime;
    protected Age onsetQuantity;
    protected Period onsetPeriod;
    protected Range onsetRange;
    protected String onsetString;
    protected DateTime abatementDateTime;
    protected Age abatementQuantity;
    protected Boolean abatementBoolean;
    protected Period abatementPeriod;
    protected Range abatementRange;
    protected String abatementString;
    protected ConditionStage stage;
    protected List<ConditionEvidence> evidence;
    protected List<CodeableConcept> bodySite;
    protected String notes;

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
     * Gets the value of the encounter property.
     * 
     * @return
     *     possible object is
     *     {@link Reference }
     *     
     */
    public Reference getEncounter() {
        return encounter;
    }

    /**
     * Sets the value of the encounter property.
     * 
     * @param value
     *     allowed object is
     *     {@link Reference }
     *     
     */
    public void setEncounter(Reference value) {
        this.encounter = value;
    }

    /**
     * Gets the value of the asserter property.
     * 
     * @return
     *     possible object is
     *     {@link Reference }
     *     
     */
    public Reference getAsserter() {
        return asserter;
    }

    /**
     * Sets the value of the asserter property.
     * 
     * @param value
     *     allowed object is
     *     {@link Reference }
     *     
     */
    public void setAsserter(Reference value) {
        this.asserter = value;
    }

    /**
     * Gets the value of the dateRecorded property.
     * 
     * @return
     *     possible object is
     *     {@link Date }
     *     
     */
    public Date getDateRecorded() {
        return dateRecorded;
    }

    /**
     * Sets the value of the dateRecorded property.
     * 
     * @param value
     *     allowed object is
     *     {@link Date }
     *     
     */
    public void setDateRecorded(Date value) {
        this.dateRecorded = value;
    }

    /**
     * Gets the value of the code property.
     * 
     * @return
     *     possible object is
     *     {@link CodeableConcept }
     *     
     */
    public CodeableConcept getCode() {
        return code;
    }

    /**
     * Sets the value of the code property.
     * 
     * @param value
     *     allowed object is
     *     {@link CodeableConcept }
     *     
     */
    public void setCode(CodeableConcept value) {
        this.code = value;
    }

    /**
     * Gets the value of the category property.
     * 
     * @return
     *     possible object is
     *     {@link CodeableConcept }
     *     
     */
    public CodeableConcept getCategory() {
        return category;
    }

    /**
     * Sets the value of the category property.
     * 
     * @param value
     *     allowed object is
     *     {@link CodeableConcept }
     *     
     */
    public void setCategory(CodeableConcept value) {
        this.category = value;
    }

    /**
     * Gets the value of the clinicalStatus property.
     * 
     * @return
     *     possible object is
     *     {@link Code }
     *     
     */
    public Code getClinicalStatus() {
        return clinicalStatus;
    }

    /**
     * Sets the value of the clinicalStatus property.
     * 
     * @param value
     *     allowed object is
     *     {@link Code }
     *     
     */
    public void setClinicalStatus(Code value) {
        this.clinicalStatus = value;
    }

    /**
     * Gets the value of the verificationStatus property.
     * 
     * @return
     *     possible object is
     *     {@link ConditionVerificationStatus }
     *     
     */
    public ConditionVerificationStatus getVerificationStatus() {
        return verificationStatus;
    }

    /**
     * Sets the value of the verificationStatus property.
     * 
     * @param value
     *     allowed object is
     *     {@link ConditionVerificationStatus }
     *     
     */
    public void setVerificationStatus(ConditionVerificationStatus value) {
        this.verificationStatus = value;
    }

    /**
     * Gets the value of the severity property.
     * 
     * @return
     *     possible object is
     *     {@link CodeableConcept }
     *     
     */
    public CodeableConcept getSeverity() {
        return severity;
    }

    /**
     * Sets the value of the severity property.
     * 
     * @param value
     *     allowed object is
     *     {@link CodeableConcept }
     *     
     */
    public void setSeverity(CodeableConcept value) {
        this.severity = value;
    }

    /**
     * Gets the value of the onsetDateTime property.
     * 
     * @return
     *     possible object is
     *     {@link DateTime }
     *     
     */
    public DateTime getOnsetDateTime() {
        return onsetDateTime;
    }

    /**
     * Sets the value of the onsetDateTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link DateTime }
     *     
     */
    public void setOnsetDateTime(DateTime value) {
        this.onsetDateTime = value;
    }

    /**
     * Gets the value of the onsetQuantity property.
     * 
     * @return
     *     possible object is
     *     {@link Age }
     *     
     */
    public Age getOnsetQuantity() {
        return onsetQuantity;
    }

    /**
     * Sets the value of the onsetQuantity property.
     * 
     * @param value
     *     allowed object is
     *     {@link Age }
     *     
     */
    public void setOnsetQuantity(Age value) {
        this.onsetQuantity = value;
    }

    /**
     * Gets the value of the onsetPeriod property.
     * 
     * @return
     *     possible object is
     *     {@link Period }
     *     
     */
    public Period getOnsetPeriod() {
        return onsetPeriod;
    }

    /**
     * Sets the value of the onsetPeriod property.
     * 
     * @param value
     *     allowed object is
     *     {@link Period }
     *     
     */
    public void setOnsetPeriod(Period value) {
        this.onsetPeriod = value;
    }

    /**
     * Gets the value of the onsetRange property.
     * 
     * @return
     *     possible object is
     *     {@link Range }
     *     
     */
    public Range getOnsetRange() {
        return onsetRange;
    }

    /**
     * Sets the value of the onsetRange property.
     * 
     * @param value
     *     allowed object is
     *     {@link Range }
     *     
     */
    public void setOnsetRange(Range value) {
        this.onsetRange = value;
    }

    /**
     * Gets the value of the onsetString property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOnsetString() {
        return onsetString;
    }

    /**
     * Sets the value of the onsetString property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOnsetString(String value) {
        this.onsetString = value;
    }

    /**
     * Gets the value of the abatementDateTime property.
     * 
     * @return
     *     possible object is
     *     {@link DateTime }
     *     
     */
    public DateTime getAbatementDateTime() {
        return abatementDateTime;
    }

    /**
     * Sets the value of the abatementDateTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link DateTime }
     *     
     */
    public void setAbatementDateTime(DateTime value) {
        this.abatementDateTime = value;
    }

    /**
     * Gets the value of the abatementQuantity property.
     * 
     * @return
     *     possible object is
     *     {@link Age }
     *     
     */
    public Age getAbatementQuantity() {
        return abatementQuantity;
    }

    /**
     * Sets the value of the abatementQuantity property.
     * 
     * @param value
     *     allowed object is
     *     {@link Age }
     *     
     */
    public void setAbatementQuantity(Age value) {
        this.abatementQuantity = value;
    }

    /**
     * Gets the value of the abatementBoolean property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean getAbatementBoolean() {
        return abatementBoolean;
    }

    /**
     * Sets the value of the abatementBoolean property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setAbatementBoolean(Boolean value) {
        this.abatementBoolean = value;
    }

    /**
     * Gets the value of the abatementPeriod property.
     * 
     * @return
     *     possible object is
     *     {@link Period }
     *     
     */
    public Period getAbatementPeriod() {
        return abatementPeriod;
    }

    /**
     * Sets the value of the abatementPeriod property.
     * 
     * @param value
     *     allowed object is
     *     {@link Period }
     *     
     */
    public void setAbatementPeriod(Period value) {
        this.abatementPeriod = value;
    }

    /**
     * Gets the value of the abatementRange property.
     * 
     * @return
     *     possible object is
     *     {@link Range }
     *     
     */
    public Range getAbatementRange() {
        return abatementRange;
    }

    /**
     * Sets the value of the abatementRange property.
     * 
     * @param value
     *     allowed object is
     *     {@link Range }
     *     
     */
    public void setAbatementRange(Range value) {
        this.abatementRange = value;
    }

    /**
     * Gets the value of the abatementString property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAbatementString() {
        return abatementString;
    }

    /**
     * Sets the value of the abatementString property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAbatementString(String value) {
        this.abatementString = value;
    }

    /**
     * Gets the value of the stage property.
     * 
     * @return
     *     possible object is
     *     {@link ConditionStage }
     *     
     */
    public ConditionStage getStage() {
        return stage;
    }

    /**
     * Sets the value of the stage property.
     * 
     * @param value
     *     allowed object is
     *     {@link ConditionStage }
     *     
     */
    public void setStage(ConditionStage value) {
        this.stage = value;
    }

    /**
     * Gets the value of the evidence property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the evidence property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getEvidence().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ConditionEvidence }
     * 
     * 
     */
    public List<ConditionEvidence> getEvidence() {
        if (evidence == null) {
            evidence = new ArrayList<ConditionEvidence>();
        }
        return this.evidence;
    }

    /**
     * Gets the value of the bodySite property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the bodySite property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getBodySite().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link CodeableConcept }
     * 
     * 
     */
    public List<CodeableConcept> getBodySite() {
        if (bodySite == null) {
            bodySite = new ArrayList<CodeableConcept>();
        }
        return this.bodySite;
    }

    /**
     * Gets the value of the notes property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNotes() {
        return notes;
    }

    /**
     * Sets the value of the notes property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNotes(String value) {
        this.notes = value;
    }

}
