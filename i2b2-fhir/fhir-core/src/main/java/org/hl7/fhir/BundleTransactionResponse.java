//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.06.25 at 02:29:55 PM EDT 
//


package org.hl7.fhir;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * A container for a collection of resources.
 * 
 * <p>Java class for Bundle.TransactionResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Bundle.TransactionResponse">
 *   &lt;complexContent>
 *     &lt;extension base="{http://hl7.org/fhir}BackboneElement">
 *       &lt;sequence>
 *         &lt;element name="status" type="{http://hl7.org/fhir}string"/>
 *         &lt;element name="location" type="{http://hl7.org/fhir}uri" minOccurs="0"/>
 *         &lt;element name="etag" type="{http://hl7.org/fhir}string" minOccurs="0"/>
 *         &lt;element name="lastModified" type="{http://hl7.org/fhir}instant" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Bundle.TransactionResponse", propOrder = {
    "status",
    "location",
    "etag",
    "lastModified"
})
public class BundleTransactionResponse
    extends BackboneElement
{

    @XmlElement(required = true)
    protected String status;
    protected Uri location;
    protected String etag;
    protected Instant lastModified;

    /**
     * Gets the value of the status property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStatus() {
        return status;
    }

    /**
     * Sets the value of the status property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStatus(String value) {
        this.status = value;
    }

    /**
     * Gets the value of the location property.
     * 
     * @return
     *     possible object is
     *     {@link Uri }
     *     
     */
    public Uri getLocation() {
        return location;
    }

    /**
     * Sets the value of the location property.
     * 
     * @param value
     *     allowed object is
     *     {@link Uri }
     *     
     */
    public void setLocation(Uri value) {
        this.location = value;
    }

    /**
     * Gets the value of the etag property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEtag() {
        return etag;
    }

    /**
     * Sets the value of the etag property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEtag(String value) {
        this.etag = value;
    }

    /**
     * Gets the value of the lastModified property.
     * 
     * @return
     *     possible object is
     *     {@link Instant }
     *     
     */
    public Instant getLastModified() {
        return lastModified;
    }

    /**
     * Sets the value of the lastModified property.
     * 
     * @param value
     *     allowed object is
     *     {@link Instant }
     *     
     */
    public void setLastModified(Instant value) {
        this.lastModified = value;
    }

}