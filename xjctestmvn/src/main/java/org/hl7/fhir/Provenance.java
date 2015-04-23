//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.04.23 at 01:15:20 PM EDT 
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
 * <p>Java class for Provenance complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Provenance">
 *   &lt;complexContent>
 *     &lt;extension base="{http://hl7.org/fhir}Resource">
 *       &lt;sequence>
 *         &lt;element name="target" type="{http://hl7.org/fhir}ResourceReference" maxOccurs="unbounded"/>
 *         &lt;element name="period" type="{http://hl7.org/fhir}Period" minOccurs="0"/>
 *         &lt;element name="recorded" type="{http://hl7.org/fhir}instant"/>
 *         &lt;element name="reason" type="{http://hl7.org/fhir}CodeableConcept" minOccurs="0"/>
 *         &lt;element name="location" type="{http://hl7.org/fhir}ResourceReference" minOccurs="0"/>
 *         &lt;element name="policy" type="{http://hl7.org/fhir}uri" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="agent" type="{http://hl7.org/fhir}Provenance.Agent" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="entity" type="{http://hl7.org/fhir}Provenance.Entity" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="integritySignature" type="{http://hl7.org/fhir}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Provenance", propOrder = {
    "target",
    "period",
    "recorded",
    "reason",
    "location",
    "policy",
    "agent",
    "entity",
    "integritySignature"
})
public class Provenance
    extends Resource
{

    @XmlElement(required = true)
    protected List<ResourceReference> target;
    protected Period period;
    @XmlElement(required = true)
    protected Instant recorded;
    protected CodeableConcept reason;
    protected ResourceReference location;
    protected List<Uri> policy;
    protected List<ProvenanceAgent> agent;
    protected List<ProvenanceEntity> entity;
    protected String integritySignature;

    /**
     * Gets the value of the target property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the target property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getTarget().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ResourceReference }
     * 
     * 
     */
    public List<ResourceReference> getTarget() {
        if (target == null) {
            target = new ArrayList<ResourceReference>();
        }
        return this.target;
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
     * Gets the value of the recorded property.
     * 
     * @return
     *     possible object is
     *     {@link Instant }
     *     
     */
    public Instant getRecorded() {
        return recorded;
    }

    /**
     * Sets the value of the recorded property.
     * 
     * @param value
     *     allowed object is
     *     {@link Instant }
     *     
     */
    public void setRecorded(Instant value) {
        this.recorded = value;
    }

    /**
     * Gets the value of the reason property.
     * 
     * @return
     *     possible object is
     *     {@link CodeableConcept }
     *     
     */
    public CodeableConcept getReason() {
        return reason;
    }

    /**
     * Sets the value of the reason property.
     * 
     * @param value
     *     allowed object is
     *     {@link CodeableConcept }
     *     
     */
    public void setReason(CodeableConcept value) {
        this.reason = value;
    }

    /**
     * Gets the value of the location property.
     * 
     * @return
     *     possible object is
     *     {@link ResourceReference }
     *     
     */
    public ResourceReference getLocation() {
        return location;
    }

    /**
     * Sets the value of the location property.
     * 
     * @param value
     *     allowed object is
     *     {@link ResourceReference }
     *     
     */
    public void setLocation(ResourceReference value) {
        this.location = value;
    }

    /**
     * Gets the value of the policy property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the policy property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getPolicy().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Uri }
     * 
     * 
     */
    public List<Uri> getPolicy() {
        if (policy == null) {
            policy = new ArrayList<Uri>();
        }
        return this.policy;
    }

    /**
     * Gets the value of the agent property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the agent property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAgent().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ProvenanceAgent }
     * 
     * 
     */
    public List<ProvenanceAgent> getAgent() {
        if (agent == null) {
            agent = new ArrayList<ProvenanceAgent>();
        }
        return this.agent;
    }

    /**
     * Gets the value of the entity property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the entity property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getEntity().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ProvenanceEntity }
     * 
     * 
     */
    public List<ProvenanceEntity> getEntity() {
        if (entity == null) {
            entity = new ArrayList<ProvenanceEntity>();
        }
        return this.entity;
    }

    /**
     * Gets the value of the integritySignature property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIntegritySignature() {
        return integritySignature;
    }

    /**
     * Sets the value of the integritySignature property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIntegritySignature(String value) {
        this.integritySignature = value;
    }

}
