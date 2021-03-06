//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.12.30 at 02:43:29 AM EST 
//


package org.hl7.fhir;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * A formal agreement between parties regarding the conduct of business, exchange of information or other matters.
 * 
 * <p>Java class for Contract.Legal complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Contract.Legal">
 *   &lt;complexContent>
 *     &lt;extension base="{http://hl7.org/fhir}BackboneElement">
 *       &lt;sequence>
 *         &lt;choice>
 *           &lt;element name="contentAttachment" type="{http://hl7.org/fhir}Attachment"/>
 *           &lt;element name="contentReference" type="{http://hl7.org/fhir}Reference"/>
 *         &lt;/choice>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Contract.Legal", propOrder = {
    "contentAttachment",
    "contentReference"
})
@javax.xml.bind.annotation.XmlRootElement(name="ContractLegal") 
public class ContractLegal
    extends BackboneElement
{

    protected Attachment contentAttachment;
    protected Reference contentReference;

    /**
     * Gets the value of the contentAttachment property.
     * 
     * @return
     *     possible object is
     *     {@link Attachment }
     *     
     */
    public Attachment getContentAttachment() {
        return contentAttachment;
    }

    /**
     * Sets the value of the contentAttachment property.
     * 
     * @param value
     *     allowed object is
     *     {@link Attachment }
     *     
     */
    public void setContentAttachment(Attachment value) {
        this.contentAttachment = value;
    }

    /**
     * Gets the value of the contentReference property.
     * 
     * @return
     *     possible object is
     *     {@link Reference }
     *     
     */
    public Reference getContentReference() {
        return contentReference;
    }

    /**
     * Sets the value of the contentReference property.
     * 
     * @param value
     *     allowed object is
     *     {@link Reference }
     *     
     */
    public void setContentReference(Reference value) {
        this.contentReference = value;
    }

}
