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
 * If the element is present, it must have either a @value, an @id, or extensions
 * 
 * <p>Java class for QuestionnaireAnswers complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="QuestionnaireAnswers">
 *   &lt;complexContent>
 *     &lt;extension base="{http://hl7.org/fhir}DomainResource">
 *       &lt;sequence>
 *         &lt;element name="identifier" type="{http://hl7.org/fhir}Identifier" minOccurs="0"/>
 *         &lt;element name="questionnaire" type="{http://hl7.org/fhir}Reference" minOccurs="0"/>
 *         &lt;element name="status" type="{http://hl7.org/fhir}QuestionnaireAnswersStatus"/>
 *         &lt;element name="subject" type="{http://hl7.org/fhir}Reference" minOccurs="0"/>
 *         &lt;element name="author" type="{http://hl7.org/fhir}Reference" minOccurs="0"/>
 *         &lt;element name="authored" type="{http://hl7.org/fhir}dateTime" minOccurs="0"/>
 *         &lt;element name="source" type="{http://hl7.org/fhir}Reference" minOccurs="0"/>
 *         &lt;element name="encounter" type="{http://hl7.org/fhir}Reference" minOccurs="0"/>
 *         &lt;element name="group" type="{http://hl7.org/fhir}QuestionnaireAnswers.Group" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "QuestionnaireAnswers", propOrder = {
    "identifier",
    "questionnaire",
    "status",
    "subject",
    "author",
    "authored",
    "source",
    "encounter",
    "group"
})
public class QuestionnaireAnswers
    extends DomainResource
{

    protected Identifier identifier;
    protected Reference questionnaire;
    @XmlElement(required = true)
    protected QuestionnaireAnswersStatus status;
    protected Reference subject;
    protected Reference author;
    protected DateTime authored;
    protected Reference source;
    protected Reference encounter;
    protected QuestionnaireAnswersGroup group;

    /**
     * Gets the value of the identifier property.
     * 
     * @return
     *     possible object is
     *     {@link Identifier }
     *     
     */
    public Identifier getIdentifier() {
        return identifier;
    }

    /**
     * Sets the value of the identifier property.
     * 
     * @param value
     *     allowed object is
     *     {@link Identifier }
     *     
     */
    public void setIdentifier(Identifier value) {
        this.identifier = value;
    }

    /**
     * Gets the value of the questionnaire property.
     * 
     * @return
     *     possible object is
     *     {@link Reference }
     *     
     */
    public Reference getQuestionnaire() {
        return questionnaire;
    }

    /**
     * Sets the value of the questionnaire property.
     * 
     * @param value
     *     allowed object is
     *     {@link Reference }
     *     
     */
    public void setQuestionnaire(Reference value) {
        this.questionnaire = value;
    }

    /**
     * Gets the value of the status property.
     * 
     * @return
     *     possible object is
     *     {@link QuestionnaireAnswersStatus }
     *     
     */
    public QuestionnaireAnswersStatus getStatus() {
        return status;
    }

    /**
     * Sets the value of the status property.
     * 
     * @param value
     *     allowed object is
     *     {@link QuestionnaireAnswersStatus }
     *     
     */
    public void setStatus(QuestionnaireAnswersStatus value) {
        this.status = value;
    }

    /**
     * Gets the value of the subject property.
     * 
     * @return
     *     possible object is
     *     {@link Reference }
     *     
     */
    public Reference getSubject() {
        return subject;
    }

    /**
     * Sets the value of the subject property.
     * 
     * @param value
     *     allowed object is
     *     {@link Reference }
     *     
     */
    public void setSubject(Reference value) {
        this.subject = value;
    }

    /**
     * Gets the value of the author property.
     * 
     * @return
     *     possible object is
     *     {@link Reference }
     *     
     */
    public Reference getAuthor() {
        return author;
    }

    /**
     * Sets the value of the author property.
     * 
     * @param value
     *     allowed object is
     *     {@link Reference }
     *     
     */
    public void setAuthor(Reference value) {
        this.author = value;
    }

    /**
     * Gets the value of the authored property.
     * 
     * @return
     *     possible object is
     *     {@link DateTime }
     *     
     */
    public DateTime getAuthored() {
        return authored;
    }

    /**
     * Sets the value of the authored property.
     * 
     * @param value
     *     allowed object is
     *     {@link DateTime }
     *     
     */
    public void setAuthored(DateTime value) {
        this.authored = value;
    }

    /**
     * Gets the value of the source property.
     * 
     * @return
     *     possible object is
     *     {@link Reference }
     *     
     */
    public Reference getSource() {
        return source;
    }

    /**
     * Sets the value of the source property.
     * 
     * @param value
     *     allowed object is
     *     {@link Reference }
     *     
     */
    public void setSource(Reference value) {
        this.source = value;
    }

    /**
     * Gets the value of the encounter property.
     * 
     * @return
     *     possible object is
     *     {@link Reference }
     *     
     */
    public Reference getEncounter() {
        return encounter;
    }

    /**
     * Sets the value of the encounter property.
     * 
     * @param value
     *     allowed object is
     *     {@link Reference }
     *     
     */
    public void setEncounter(Reference value) {
        this.encounter = value;
    }

    /**
     * Gets the value of the group property.
     * 
     * @return
     *     possible object is
     *     {@link QuestionnaireAnswersGroup }
     *     
     */
    public QuestionnaireAnswersGroup getGroup() {
        return group;
    }

    /**
     * Sets the value of the group property.
     * 
     * @param value
     *     allowed object is
     *     {@link QuestionnaireAnswersGroup }
     *     
     */
    public void setGroup(QuestionnaireAnswersGroup value) {
        this.group = value;
    }

}