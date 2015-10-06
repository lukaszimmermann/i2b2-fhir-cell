//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.10.03 at 10:58:19 AM EDT 
//


package org.hl7.fhir;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


/**
 * If the element is present, it must have either a @value, an @id, or extensions
 * 
 * <p>Java class for MedicationAdministrationStatus complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="MedicationAdministrationStatus">
 *   &lt;complexContent>
 *     &lt;extension base="{http://hl7.org/fhir}Element">
 *       &lt;attribute name="value" type="{http://hl7.org/fhir}MedicationAdministrationStatus-list" />
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MedicationAdministrationStatus")
public class MedicationAdministrationStatus
    extends Element
{

    @XmlAttribute(name = "value")
    protected MedicationAdministrationStatusList value;

    /**
     * Gets the value of the value property.
     * 
     * @return
     *     possible object is
     *     {@link MedicationAdministrationStatusList }
     *     
     */
    public MedicationAdministrationStatusList getValue() {
        return value;
    }

    /**
     * Sets the value of the value property.
     * 
     * @param value
     *     allowed object is
     *     {@link MedicationAdministrationStatusList }
     *     
     */
    public void setValue(MedicationAdministrationStatusList value) {
        this.value = value;
    }

}
