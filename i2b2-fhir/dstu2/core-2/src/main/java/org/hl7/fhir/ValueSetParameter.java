/*
 * Copyright (c) 2006-2007 Massachusetts General Hospital 
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the i2b2 Software License v1.0 
 * which accompanies this distribution. 
 * 
 * Contributors:
 * 		Kavishwar Wagholikar (kavi)
 * 		July 4, 2015
 */

/*
 * Copyright (c) 2006-2007 Massachusetts General Hospital 
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the i2b2 Software License v1.0 
 * which accompanies this distribution. 
 * 
 * Contributors:
 * 		Kavishwar Wagholikar (kavi)
 */
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
 * A value set specifies a set of codes drawn from one or more code systems.
 * 
 * <p>Java class for ValueSet.Parameter complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ValueSet.Parameter">
 *   &lt;complexContent>
 *     &lt;extension base="{http://hl7.org/fhir}BackboneElement">
 *       &lt;sequence>
 *         &lt;element name="name" type="{http://hl7.org/fhir}string"/>
 *         &lt;choice minOccurs="0">
 *           &lt;element name="valueString" type="{http://hl7.org/fhir}string"/>
 *           &lt;element name="valueBoolean" type="{http://hl7.org/fhir}boolean"/>
 *           &lt;element name="valueInteger" type="{http://hl7.org/fhir}integer"/>
 *           &lt;element name="valueDecimal" type="{http://hl7.org/fhir}decimal"/>
 *           &lt;element name="valueUri" type="{http://hl7.org/fhir}uri"/>
 *           &lt;element name="valueCode" type="{http://hl7.org/fhir}code"/>
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
@XmlType(name = "ValueSet.Parameter", propOrder = {
    "name",
    "valueString",
    "valueBoolean",
    "valueInteger",
    "valueDecimal",
    "valueUri",
    "valueCode"
})
public class ValueSetParameter
    extends BackboneElement
{

    @XmlElement(required = true)
    protected String name;
    protected String valueString;
    protected Boolean valueBoolean;
    protected Integer valueInteger;
    protected Decimal valueDecimal;
    protected Uri valueUri;
    protected Code valueCode;

    /**
     * Gets the value of the name property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * Gets the value of the valueString property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getValueString() {
        return valueString;
    }

    /**
     * Sets the value of the valueString property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setValueString(String value) {
        this.valueString = value;
    }

    /**
     * Gets the value of the valueBoolean property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean getValueBoolean() {
        return valueBoolean;
    }

    /**
     * Sets the value of the valueBoolean property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setValueBoolean(Boolean value) {
        this.valueBoolean = value;
    }

    /**
     * Gets the value of the valueInteger property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getValueInteger() {
        return valueInteger;
    }

    /**
     * Sets the value of the valueInteger property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setValueInteger(Integer value) {
        this.valueInteger = value;
    }

    /**
     * Gets the value of the valueDecimal property.
     * 
     * @return
     *     possible object is
     *     {@link Decimal }
     *     
     */
    public Decimal getValueDecimal() {
        return valueDecimal;
    }

    /**
     * Sets the value of the valueDecimal property.
     * 
     * @param value
     *     allowed object is
     *     {@link Decimal }
     *     
     */
    public void setValueDecimal(Decimal value) {
        this.valueDecimal = value;
    }

    /**
     * Gets the value of the valueUri property.
     * 
     * @return
     *     possible object is
     *     {@link Uri }
     *     
     */
    public Uri getValueUri() {
        return valueUri;
    }

    /**
     * Sets the value of the valueUri property.
     * 
     * @param value
     *     allowed object is
     *     {@link Uri }
     *     
     */
    public void setValueUri(Uri value) {
        this.valueUri = value;
    }

    /**
     * Gets the value of the valueCode property.
     * 
     * @return
     *     possible object is
     *     {@link Code }
     *     
     */
    public Code getValueCode() {
        return valueCode;
    }

    /**
     * Sets the value of the valueCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link Code }
     *     
     */
    public void setValueCode(Code value) {
        this.valueCode = value;
    }

}
