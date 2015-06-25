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
 * <p>Java class for Encounter complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Encounter">
 *   &lt;complexContent>
 *     &lt;extension base="{http://hl7.org/fhir}DomainResource">
 *       &lt;sequence>
 *         &lt;element name="identifier" type="{http://hl7.org/fhir}Identifier" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="status" type="{http://hl7.org/fhir}EncounterState"/>
 *         &lt;element name="statusHistory" type="{http://hl7.org/fhir}Encounter.StatusHistory" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="class" type="{http://hl7.org/fhir}EncounterClass" minOccurs="0"/>
 *         &lt;element name="type" type="{http://hl7.org/fhir}CodeableConcept" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="patient" type="{http://hl7.org/fhir}Reference" minOccurs="0"/>
 *         &lt;element name="episodeOfCare" type="{http://hl7.org/fhir}Reference" minOccurs="0"/>
 *         &lt;element name="incomingReferralRequest" type="{http://hl7.org/fhir}Reference" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="participant" type="{http://hl7.org/fhir}Encounter.Participant" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="fulfills" type="{http://hl7.org/fhir}Reference" minOccurs="0"/>
 *         &lt;element name="period" type="{http://hl7.org/fhir}Period" minOccurs="0"/>
 *         &lt;element name="length" type="{http://hl7.org/fhir}Duration" minOccurs="0"/>
 *         &lt;element name="reason" type="{http://hl7.org/fhir}CodeableConcept" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="indication" type="{http://hl7.org/fhir}Reference" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="priority" type="{http://hl7.org/fhir}CodeableConcept" minOccurs="0"/>
 *         &lt;element name="hospitalization" type="{http://hl7.org/fhir}Encounter.Hospitalization" minOccurs="0"/>
 *         &lt;element name="location" type="{http://hl7.org/fhir}Encounter.Location" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="serviceProvider" type="{http://hl7.org/fhir}Reference" minOccurs="0"/>
 *         &lt;element name="partOf" type="{http://hl7.org/fhir}Reference" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Encounter", propOrder = {
    "identifier",
    "status",
    "statusHistory",
    "clazz",
    "type",
    "patient",
    "episodeOfCare",
    "incomingReferralRequest",
    "participant",
    "fulfills",
    "period",
    "length",
    "reason",
    "indication",
    "priority",
    "hospitalization",
    "location",
    "serviceProvider",
    "partOf"
})
public class Encounter
    extends DomainResource
{

    protected List<Identifier> identifier;
    @XmlElement(required = true)
    protected EncounterState status;
    protected List<EncounterStatusHistory> statusHistory;
    @XmlElement(name = "class")
    protected EncounterClass clazz;
    protected List<CodeableConcept> type;
    protected Reference patient;
    protected Reference episodeOfCare;
    protected List<Reference> incomingReferralRequest;
    protected List<EncounterParticipant> participant;
    protected Reference fulfills;
    protected Period period;
    protected Duration length;
    protected List<CodeableConcept> reason;
    protected List<Reference> indication;
    protected CodeableConcept priority;
    protected EncounterHospitalization hospitalization;
    protected List<EncounterLocation> location;
    protected Reference serviceProvider;
    protected Reference partOf;

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
     * Gets the value of the status property.
     * 
     * @return
     *     possible object is
     *     {@link EncounterState }
     *     
     */
    public EncounterState getStatus() {
        return status;
    }

    /**
     * Sets the value of the status property.
     * 
     * @param value
     *     allowed object is
     *     {@link EncounterState }
     *     
     */
    public void setStatus(EncounterState value) {
        this.status = value;
    }

    /**
     * Gets the value of the statusHistory property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the statusHistory property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getStatusHistory().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link EncounterStatusHistory }
     * 
     * 
     */
    public List<EncounterStatusHistory> getStatusHistory() {
        if (statusHistory == null) {
            statusHistory = new ArrayList<EncounterStatusHistory>();
        }
        return this.statusHistory;
    }

    /**
     * Gets the value of the clazz property.
     * 
     * @return
     *     possible object is
     *     {@link EncounterClass }
     *     
     */
    public EncounterClass getClazz() {
        return clazz;
    }

    /**
     * Sets the value of the clazz property.
     * 
     * @param value
     *     allowed object is
     *     {@link EncounterClass }
     *     
     */
    public void setClazz(EncounterClass value) {
        this.clazz = value;
    }

    /**
     * Gets the value of the type property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the type property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getType().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link CodeableConcept }
     * 
     * 
     */
    public List<CodeableConcept> getType() {
        if (type == null) {
            type = new ArrayList<CodeableConcept>();
        }
        return this.type;
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
     * Gets the value of the episodeOfCare property.
     * 
     * @return
     *     possible object is
     *     {@link Reference }
     *     
     */
    public Reference getEpisodeOfCare() {
        return episodeOfCare;
    }

    /**
     * Sets the value of the episodeOfCare property.
     * 
     * @param value
     *     allowed object is
     *     {@link Reference }
     *     
     */
    public void setEpisodeOfCare(Reference value) {
        this.episodeOfCare = value;
    }

    /**
     * Gets the value of the incomingReferralRequest property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the incomingReferralRequest property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getIncomingReferralRequest().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Reference }
     * 
     * 
     */
    public List<Reference> getIncomingReferralRequest() {
        if (incomingReferralRequest == null) {
            incomingReferralRequest = new ArrayList<Reference>();
        }
        return this.incomingReferralRequest;
    }

    /**
     * Gets the value of the participant property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the participant property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getParticipant().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link EncounterParticipant }
     * 
     * 
     */
    public List<EncounterParticipant> getParticipant() {
        if (participant == null) {
            participant = new ArrayList<EncounterParticipant>();
        }
        return this.participant;
    }

    /**
     * Gets the value of the fulfills property.
     * 
     * @return
     *     possible object is
     *     {@link Reference }
     *     
     */
    public Reference getFulfills() {
        return fulfills;
    }

    /**
     * Sets the value of the fulfills property.
     * 
     * @param value
     *     allowed object is
     *     {@link Reference }
     *     
     */
    public void setFulfills(Reference value) {
        this.fulfills = value;
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
     * Gets the value of the length property.
     * 
     * @return
     *     possible object is
     *     {@link Duration }
     *     
     */
    public Duration getLength() {
        return length;
    }

    /**
     * Sets the value of the length property.
     * 
     * @param value
     *     allowed object is
     *     {@link Duration }
     *     
     */
    public void setLength(Duration value) {
        this.length = value;
    }

    /**
     * Gets the value of the reason property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the reason property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getReason().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link CodeableConcept }
     * 
     * 
     */
    public List<CodeableConcept> getReason() {
        if (reason == null) {
            reason = new ArrayList<CodeableConcept>();
        }
        return this.reason;
    }

    /**
     * Gets the value of the indication property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the indication property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getIndication().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Reference }
     * 
     * 
     */
    public List<Reference> getIndication() {
        if (indication == null) {
            indication = new ArrayList<Reference>();
        }
        return this.indication;
    }

    /**
     * Gets the value of the priority property.
     * 
     * @return
     *     possible object is
     *     {@link CodeableConcept }
     *     
     */
    public CodeableConcept getPriority() {
        return priority;
    }

    /**
     * Sets the value of the priority property.
     * 
     * @param value
     *     allowed object is
     *     {@link CodeableConcept }
     *     
     */
    public void setPriority(CodeableConcept value) {
        this.priority = value;
    }

    /**
     * Gets the value of the hospitalization property.
     * 
     * @return
     *     possible object is
     *     {@link EncounterHospitalization }
     *     
     */
    public EncounterHospitalization getHospitalization() {
        return hospitalization;
    }

    /**
     * Sets the value of the hospitalization property.
     * 
     * @param value
     *     allowed object is
     *     {@link EncounterHospitalization }
     *     
     */
    public void setHospitalization(EncounterHospitalization value) {
        this.hospitalization = value;
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
     * {@link EncounterLocation }
     * 
     * 
     */
    public List<EncounterLocation> getLocation() {
        if (location == null) {
            location = new ArrayList<EncounterLocation>();
        }
        return this.location;
    }

    /**
     * Gets the value of the serviceProvider property.
     * 
     * @return
     *     possible object is
     *     {@link Reference }
     *     
     */
    public Reference getServiceProvider() {
        return serviceProvider;
    }

    /**
     * Sets the value of the serviceProvider property.
     * 
     * @param value
     *     allowed object is
     *     {@link Reference }
     *     
     */
    public void setServiceProvider(Reference value) {
        this.serviceProvider = value;
    }

    /**
     * Gets the value of the partOf property.
     * 
     * @return
     *     possible object is
     *     {@link Reference }
     *     
     */
    public Reference getPartOf() {
        return partOf;
    }

    /**
     * Sets the value of the partOf property.
     * 
     * @param value
     *     allowed object is
     *     {@link Reference }
     *     
     */
    public void setPartOf(Reference value) {
        this.partOf = value;
    }

}
