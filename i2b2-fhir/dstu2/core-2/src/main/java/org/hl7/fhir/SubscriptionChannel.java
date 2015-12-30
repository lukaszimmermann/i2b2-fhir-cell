//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.12.30 at 02:43:29 AM EST 
//


package org.hl7.fhir;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * The subscription resource is used to define a push based subscription from a server to another system. Once a subscription is registered with the server, the server checks every resource that is created or updated, and if the resource matches the given criteria, it sends a message on the defined "channel" so that another system is able to take an appropriate action.
 * 
 * <p>Java class for Subscription.Channel complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Subscription.Channel">
 *   &lt;complexContent>
 *     &lt;extension base="{http://hl7.org/fhir}BackboneElement">
 *       &lt;sequence>
 *         &lt;element name="type" type="{http://hl7.org/fhir}SubscriptionChannelType"/>
 *         &lt;element name="endpoint" type="{http://hl7.org/fhir}uri" minOccurs="0"/>
 *         &lt;element name="payload" type="{http://hl7.org/fhir}string"/>
 *         &lt;element name="header" type="{http://hl7.org/fhir}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Subscription.Channel", propOrder = {
    "type",
    "endpoint",
    "payload",
    "header"
})
@javax.xml.bind.annotation.XmlRootElement(name="SubscriptionChannel") 
public class SubscriptionChannel
    extends BackboneElement
{

    @XmlElement(required = true)
    protected SubscriptionChannelType type;
    protected Uri endpoint;
    @XmlElement(required = true)
    protected String payload;
    protected String header;

    /**
     * Gets the value of the type property.
     * 
     * @return
     *     possible object is
     *     {@link SubscriptionChannelType }
     *     
     */
    public SubscriptionChannelType getType() {
        return type;
    }

    /**
     * Sets the value of the type property.
     * 
     * @param value
     *     allowed object is
     *     {@link SubscriptionChannelType }
     *     
     */
    public void setType(SubscriptionChannelType value) {
        this.type = value;
    }

    /**
     * Gets the value of the endpoint property.
     * 
     * @return
     *     possible object is
     *     {@link Uri }
     *     
     */
    public Uri getEndpoint() {
        return endpoint;
    }

    /**
     * Sets the value of the endpoint property.
     * 
     * @param value
     *     allowed object is
     *     {@link Uri }
     *     
     */
    public void setEndpoint(Uri value) {
        this.endpoint = value;
    }

    /**
     * Gets the value of the payload property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPayload() {
        return payload;
    }

    /**
     * Sets the value of the payload property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPayload(String value) {
        this.payload = value;
    }

    /**
     * Gets the value of the header property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHeader() {
        return header;
    }

    /**
     * Sets the value of the header property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHeader(String value) {
        this.header = value;
    }

}
