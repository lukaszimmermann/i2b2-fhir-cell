//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.04.22 at 03:04:28 PM EDT 
//


package org.hl7.fhir;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * Immunization event information.
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
 *         &lt;element name="doseSequence" type="{http://hl7.org/fhir}integer"/>
 *         &lt;element name="description" type="{http://hl7.org/fhir}string" minOccurs="0"/>
 *         &lt;element name="authority" type="{http://hl7.org/fhir}ResourceReference" minOccurs="0"/>
 *         &lt;element name="series" type="{http://hl7.org/fhir}string" minOccurs="0"/>
 *         &lt;element name="seriesDoses" type="{http://hl7.org/fhir}integer" minOccurs="0"/>
 *         &lt;element name="doseTarget" type="{http://hl7.org/fhir}CodeableConcept"/>
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
    "doseTarget",
    "doseStatus",
    "doseStatusReason"
})
public class ImmunizationVaccinationProtocol
    extends BackboneElement
{

    @XmlElement(required = true)
    protected Integer doseSequence;
    protected String description;
    protected ResourceReference authority;
    protected String series;
    protected Integer seriesDoses;
    @XmlElement(required = true)
    protected CodeableConcept doseTarget;
    @XmlElement(required = true)
    protected CodeableConcept doseStatus;
    protected CodeableConcept doseStatusReason;

    /**
     * Gets the value of the doseSequence property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getDoseSequence() {
        return doseSequence;
    }

    /**
     * Sets the value of the doseSequence property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setDoseSequence(Integer value) {
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
     *     {@link ResourceReference }
     *     
     */
    public ResourceReference getAuthority() {
        return authority;
    }

    /**
     * Sets the value of the authority property.
     * 
     * @param value
     *     allowed object is
     *     {@link ResourceReference }
     *     
     */
    public void setAuthority(ResourceReference value) {
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
     *     {@link Integer }
     *     
     */
    public Integer getSeriesDoses() {
        return seriesDoses;
    }

    /**
     * Sets the value of the seriesDoses property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setSeriesDoses(Integer value) {
        this.seriesDoses = value;
    }

    /**
     * Gets the value of the doseTarget property.
     * 
     * @return
     *     possible object is
     *     {@link CodeableConcept }
     *     
     */
    public CodeableConcept getDoseTarget() {
        return doseTarget;
    }

    /**
     * Sets the value of the doseTarget property.
     * 
     * @param value
     *     allowed object is
     *     {@link CodeableConcept }
     *     
     */
    public void setDoseTarget(CodeableConcept value) {
        this.doseTarget = value;
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
