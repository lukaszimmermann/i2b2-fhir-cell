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
 * Describes the event of a patient being administered a vaccination or a record of a vaccination as reported by a patient, a clinician or another party and may include vaccine reaction information and what vaccination protocol was followed.
 * 
 * <p>Java class for Immunization.VaccinationProtocol complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Immunization.VaccinationProtocol">
 *   &lt;complexContent>
 *     &lt;extension base="{http://hl7.org/fhir}BackboneElement">
 *       &lt;sequence>
 *         &lt;element name="doseSequence" type="{http://hl7.org/fhir}positiveInt"/>
 *         &lt;element name="description" type="{http://hl7.org/fhir}string" minOccurs="0"/>
 *         &lt;element name="authority" type="{http://hl7.org/fhir}Reference" minOccurs="0"/>
 *         &lt;element name="series" type="{http://hl7.org/fhir}string" minOccurs="0"/>
 *         &lt;element name="seriesDoses" type="{http://hl7.org/fhir}positiveInt" minOccurs="0"/>
 *         &lt;element name="targetDisease" type="{http://hl7.org/fhir}CodeableConcept" maxOccurs="unbounded"/>
 *         &lt;element name="doseStatus" type="{http://hl7.org/fhir}CodeableConcept"/>
 *         &lt;element name="doseStatusReason" type="{http://hl7.org/fhir}CodeableConcept" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Immunization.VaccinationProtocol", propOrder = {
    "doseSequence",
    "description",
    "authority",
    "series",
    "seriesDoses",
    "targetDisease",
    "doseStatus",
    "doseStatusReason"
})
@javax.xml.bind.annotation.XmlRootElement(name="ImmunizationVaccinationProtocol") 
public class ImmunizationVaccinationProtocol
    extends BackboneElement
{

    @XmlElement(required = true)
    protected PositiveInt doseSequence;
    protected String description;
    protected Reference authority;
    protected String series;
    protected PositiveInt seriesDoses;
    @XmlElement(required = true)
    protected List<CodeableConcept> targetDisease;
    @XmlElement(required = true)
    protected CodeableConcept doseStatus;
    protected CodeableConcept doseStatusReason;

    /**
     * Gets the value of the doseSequence property.
     * 
     * @return
     *     possible object is
     *     {@link PositiveInt }
     *     
     */
    public PositiveInt getDoseSequence() {
        return doseSequence;
    }

    /**
     * Sets the value of the doseSequence property.
     * 
     * @param value
     *     allowed object is
     *     {@link PositiveInt }
     *     
     */
    public void setDoseSequence(PositiveInt value) {
        this.doseSequence = value;
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
     * Gets the value of the authority property.
     * 
     * @return
     *     possible object is
     *     {@link Reference }
     *     
     */
    public Reference getAuthority() {
        return authority;
    }

    /**
     * Sets the value of the authority property.
     * 
     * @param value
     *     allowed object is
     *     {@link Reference }
     *     
     */
    public void setAuthority(Reference value) {
        this.authority = value;
    }

    /**
     * Gets the value of the series property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSeries() {
        return series;
    }

    /**
     * Sets the value of the series property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSeries(String value) {
        this.series = value;
    }

    /**
     * Gets the value of the seriesDoses property.
     * 
     * @return
     *     possible object is
     *     {@link PositiveInt }
     *     
     */
    public PositiveInt getSeriesDoses() {
        return seriesDoses;
    }

    /**
     * Sets the value of the seriesDoses property.
     * 
     * @param value
     *     allowed object is
     *     {@link PositiveInt }
     *     
     */
    public void setSeriesDoses(PositiveInt value) {
        this.seriesDoses = value;
    }

    /**
     * Gets the value of the targetDisease property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the targetDisease property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getTargetDisease().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link CodeableConcept }
     * 
     * 
     */
    public List<CodeableConcept> getTargetDisease() {
        if (targetDisease == null) {
            targetDisease = new ArrayList<CodeableConcept>();
        }
        return this.targetDisease;
    }

    /**
     * Gets the value of the doseStatus property.
     * 
     * @return
     *     possible object is
     *     {@link CodeableConcept }
     *     
     */
    public CodeableConcept getDoseStatus() {
        return doseStatus;
    }

    /**
     * Sets the value of the doseStatus property.
     * 
     * @param value
     *     allowed object is
     *     {@link CodeableConcept }
     *     
     */
    public void setDoseStatus(CodeableConcept value) {
        this.doseStatus = value;
    }

    /**
     * Gets the value of the doseStatusReason property.
     * 
     * @return
     *     possible object is
     *     {@link CodeableConcept }
     *     
     */
    public CodeableConcept getDoseStatusReason() {
        return doseStatusReason;
    }

    /**
     * Sets the value of the doseStatusReason property.
     * 
     * @param value
     *     allowed object is
     *     {@link CodeableConcept }
     *     
     */
    public void setDoseStatusReason(CodeableConcept value) {
        this.doseStatusReason = value;
    }

}
