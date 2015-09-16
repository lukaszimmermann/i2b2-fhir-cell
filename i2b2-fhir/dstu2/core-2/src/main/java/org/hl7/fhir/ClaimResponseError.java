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
 * This resource provides the adjudication details from the processing of a Claim resource.
 * 
 * <p>Java class for ClaimResponse.Error complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ClaimResponse.Error">
 *   &lt;complexContent>
 *     &lt;extension base="{http://hl7.org/fhir}BackboneElement">
 *       &lt;sequence>
 *         &lt;element name="sequenceLinkId" type="{http://hl7.org/fhir}positiveInt" minOccurs="0"/>
 *         &lt;element name="detailSequenceLinkId" type="{http://hl7.org/fhir}positiveInt" minOccurs="0"/>
 *         &lt;element name="subdetailSequenceLinkId" type="{http://hl7.org/fhir}positiveInt" minOccurs="0"/>
 *         &lt;element name="code" type="{http://hl7.org/fhir}Coding"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ClaimResponse.Error", propOrder = {
    "sequenceLinkId",
    "detailSequenceLinkId",
    "subdetailSequenceLinkId",
    "code"
})
public class ClaimResponseError
    extends BackboneElement
{

    protected PositiveInt sequenceLinkId;
    protected PositiveInt detailSequenceLinkId;
    protected PositiveInt subdetailSequenceLinkId;
    @XmlElement(required = true)
    protected Coding code;

    /**
     * Gets the value of the sequenceLinkId property.
     * 
     * @return
     *     possible object is
     *     {@link PositiveInt }
     *     
     */
    public PositiveInt getSequenceLinkId() {
        return sequenceLinkId;
    }

    /**
     * Sets the value of the sequenceLinkId property.
     * 
     * @param value
     *     allowed object is
     *     {@link PositiveInt }
     *     
     */
    public void setSequenceLinkId(PositiveInt value) {
        this.sequenceLinkId = value;
    }

    /**
     * Gets the value of the detailSequenceLinkId property.
     * 
     * @return
     *     possible object is
     *     {@link PositiveInt }
     *     
     */
    public PositiveInt getDetailSequenceLinkId() {
        return detailSequenceLinkId;
    }

    /**
     * Sets the value of the detailSequenceLinkId property.
     * 
     * @param value
     *     allowed object is
     *     {@link PositiveInt }
     *     
     */
    public void setDetailSequenceLinkId(PositiveInt value) {
        this.detailSequenceLinkId = value;
    }

    /**
     * Gets the value of the subdetailSequenceLinkId property.
     * 
     * @return
     *     possible object is
     *     {@link PositiveInt }
     *     
     */
    public PositiveInt getSubdetailSequenceLinkId() {
        return subdetailSequenceLinkId;
    }

    /**
     * Sets the value of the subdetailSequenceLinkId property.
     * 
     * @param value
     *     allowed object is
     *     {@link PositiveInt }
     *     
     */
    public void setSubdetailSequenceLinkId(PositiveInt value) {
        this.subdetailSequenceLinkId = value;
    }

    /**
     * Gets the value of the code property.
     * 
     * @return
     *     possible object is
     *     {@link Coding }
     *     
     */
    public Coding getCode() {
        return code;
    }

    /**
     * Sets the value of the code property.
     * 
     * @param value
     *     allowed object is
     *     {@link Coding }
     *     
     */
    public void setCode(Coding value) {
        this.code = value;
    }

}
