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

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * If the element is present, it must have a value for at least one of the defined elements, an @id referenced from the Narrative, or extensions
 * 
 * <p>Java class for Signature complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Signature">
 *   &lt;complexContent>
 *     &lt;extension base="{http://hl7.org/fhir}Element">
 *       &lt;sequence>
 *         &lt;element name="type" type="{http://hl7.org/fhir}Coding" maxOccurs="unbounded"/>
 *         &lt;element name="when" type="{http://hl7.org/fhir}instant"/>
 *         &lt;choice>
 *           &lt;element name="whoBoolean" type="{http://hl7.org/fhir}boolean"/>
 *           &lt;element name="whoInteger" type="{http://hl7.org/fhir}integer"/>
 *           &lt;element name="whoDecimal" type="{http://hl7.org/fhir}decimal"/>
 *           &lt;element name="whoBase64Binary" type="{http://hl7.org/fhir}base64Binary"/>
 *           &lt;element name="whoInstant" type="{http://hl7.org/fhir}instant"/>
 *           &lt;element name="whoString" type="{http://hl7.org/fhir}string"/>
 *           &lt;element name="whoUri" type="{http://hl7.org/fhir}uri"/>
 *           &lt;element name="whoDate" type="{http://hl7.org/fhir}date"/>
 *           &lt;element name="whoDateTime" type="{http://hl7.org/fhir}dateTime"/>
 *           &lt;element name="whoTime" type="{http://hl7.org/fhir}time"/>
 *           &lt;element name="whoCode" type="{http://hl7.org/fhir}code"/>
 *           &lt;element name="whoOid" type="{http://hl7.org/fhir}oid"/>
 *           &lt;element name="whoUuid" type="{http://hl7.org/fhir}uuid"/>
 *           &lt;element name="whoId" type="{http://hl7.org/fhir}id"/>
 *           &lt;element name="whoUnsignedInt" type="{http://hl7.org/fhir}unsignedInt"/>
 *           &lt;element name="whoPositiveInt" type="{http://hl7.org/fhir}positiveInt"/>
 *           &lt;element name="whoAttachment" type="{http://hl7.org/fhir}Attachment"/>
 *           &lt;element name="whoIdentifier" type="{http://hl7.org/fhir}Identifier"/>
 *           &lt;element name="whoCodeableConcept" type="{http://hl7.org/fhir}CodeableConcept"/>
 *           &lt;element name="whoCoding" type="{http://hl7.org/fhir}Coding"/>
 *           &lt;element name="whoQuantity" type="{http://hl7.org/fhir}Quantity"/>
 *           &lt;element name="whoRange" type="{http://hl7.org/fhir}Range"/>
 *           &lt;element name="whoPeriod" type="{http://hl7.org/fhir}Period"/>
 *           &lt;element name="whoRatio" type="{http://hl7.org/fhir}Ratio"/>
 *           &lt;element name="whoReference" type="{http://hl7.org/fhir}Reference"/>
 *           &lt;element name="whoSampledData" type="{http://hl7.org/fhir}SampledData"/>
 *           &lt;element name="whoSignature" type="{http://hl7.org/fhir}Signature"/>
 *           &lt;element name="whoHumanName" type="{http://hl7.org/fhir}HumanName"/>
 *           &lt;element name="whoAddress" type="{http://hl7.org/fhir}Address"/>
 *           &lt;element name="whoContactPoint" type="{http://hl7.org/fhir}ContactPoint"/>
 *           &lt;element name="whoTiming" type="{http://hl7.org/fhir}Timing"/>
 *           &lt;element name="whoMeta" type="{http://hl7.org/fhir}Meta"/>
 *         &lt;/choice>
 *         &lt;element name="blob" type="{http://hl7.org/fhir}base64Binary"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Signature", propOrder = {
    "type",
    "when",
    "whoBoolean",
    "whoInteger",
    "whoDecimal",
    "whoBase64Binary",
    "whoInstant",
    "whoString",
    "whoUri",
    "whoDate",
    "whoDateTime",
    "whoTime",
    "whoCode",
    "whoOid",
    "whoUuid",
    "whoId",
    "whoUnsignedInt",
    "whoPositiveInt",
    "whoAttachment",
    "whoIdentifier",
    "whoCodeableConcept",
    "whoCoding",
    "whoQuantity",
    "whoRange",
    "whoPeriod",
    "whoRatio",
    "whoReference",
    "whoSampledData",
    "whoSignature",
    "whoHumanName",
    "whoAddress",
    "whoContactPoint",
    "whoTiming",
    "whoMeta",
    "blob"
})
public class Signature
    extends Element
{

    @XmlElement(required = true)
    protected List<Coding> type;
    @XmlElement(required = true)
    protected Instant when;
    protected Boolean whoBoolean;
    protected Integer whoInteger;
    protected Decimal whoDecimal;
    protected Base64Binary whoBase64Binary;
    protected Instant whoInstant;
    protected String whoString;
    protected Uri whoUri;
    protected Date whoDate;
    protected DateTime whoDateTime;
    protected Time whoTime;
    protected Code whoCode;
    protected Oid whoOid;
    protected Uuid whoUuid;
    protected Id whoId;
    protected UnsignedInt whoUnsignedInt;
    protected PositiveInt whoPositiveInt;
    protected Attachment whoAttachment;
    protected Identifier whoIdentifier;
    protected CodeableConcept whoCodeableConcept;
    protected Coding whoCoding;
    protected Quantity whoQuantity;
    protected Range whoRange;
    protected Period whoPeriod;
    protected Ratio whoRatio;
    protected Reference whoReference;
    protected SampledData whoSampledData;
    protected Signature whoSignature;
    protected HumanName whoHumanName;
    protected Address whoAddress;
    protected ContactPoint whoContactPoint;
    protected Timing whoTiming;
    protected Meta whoMeta;
    @XmlElement(required = true)
    protected Base64Binary blob;

    /**
     * Gets the value of the type property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the type property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getType().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Coding }
     * 
     * 
     */
    public List<Coding> getType() {
        if (type == null) {
            type = new ArrayList<Coding>();
        }
        return this.type;
    }

    /**
     * Gets the value of the when property.
     * 
     * @return
     *     possible object is
     *     {@link Instant }
     *     
     */
    public Instant getWhen() {
        return when;
    }

    /**
     * Sets the value of the when property.
     * 
     * @param value
     *     allowed object is
     *     {@link Instant }
     *     
     */
    public void setWhen(Instant value) {
        this.when = value;
    }

    /**
     * Gets the value of the whoBoolean property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean getWhoBoolean() {
        return whoBoolean;
    }

    /**
     * Sets the value of the whoBoolean property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setWhoBoolean(Boolean value) {
        this.whoBoolean = value;
    }

    /**
     * Gets the value of the whoInteger property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getWhoInteger() {
        return whoInteger;
    }

    /**
     * Sets the value of the whoInteger property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setWhoInteger(Integer value) {
        this.whoInteger = value;
    }

    /**
     * Gets the value of the whoDecimal property.
     * 
     * @return
     *     possible object is
     *     {@link Decimal }
     *     
     */
    public Decimal getWhoDecimal() {
        return whoDecimal;
    }

    /**
     * Sets the value of the whoDecimal property.
     * 
     * @param value
     *     allowed object is
     *     {@link Decimal }
     *     
     */
    public void setWhoDecimal(Decimal value) {
        this.whoDecimal = value;
    }

    /**
     * Gets the value of the whoBase64Binary property.
     * 
     * @return
     *     possible object is
     *     {@link Base64Binary }
     *     
     */
    public Base64Binary getWhoBase64Binary() {
        return whoBase64Binary;
    }

    /**
     * Sets the value of the whoBase64Binary property.
     * 
     * @param value
     *     allowed object is
     *     {@link Base64Binary }
     *     
     */
    public void setWhoBase64Binary(Base64Binary value) {
        this.whoBase64Binary = value;
    }

    /**
     * Gets the value of the whoInstant property.
     * 
     * @return
     *     possible object is
     *     {@link Instant }
     *     
     */
    public Instant getWhoInstant() {
        return whoInstant;
    }

    /**
     * Sets the value of the whoInstant property.
     * 
     * @param value
     *     allowed object is
     *     {@link Instant }
     *     
     */
    public void setWhoInstant(Instant value) {
        this.whoInstant = value;
    }

    /**
     * Gets the value of the whoString property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getWhoString() {
        return whoString;
    }

    /**
     * Sets the value of the whoString property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setWhoString(String value) {
        this.whoString = value;
    }

    /**
     * Gets the value of the whoUri property.
     * 
     * @return
     *     possible object is
     *     {@link Uri }
     *     
     */
    public Uri getWhoUri() {
        return whoUri;
    }

    /**
     * Sets the value of the whoUri property.
     * 
     * @param value
     *     allowed object is
     *     {@link Uri }
     *     
     */
    public void setWhoUri(Uri value) {
        this.whoUri = value;
    }

    /**
     * Gets the value of the whoDate property.
     * 
     * @return
     *     possible object is
     *     {@link Date }
     *     
     */
    public Date getWhoDate() {
        return whoDate;
    }

    /**
     * Sets the value of the whoDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link Date }
     *     
     */
    public void setWhoDate(Date value) {
        this.whoDate = value;
    }

    /**
     * Gets the value of the whoDateTime property.
     * 
     * @return
     *     possible object is
     *     {@link DateTime }
     *     
     */
    public DateTime getWhoDateTime() {
        return whoDateTime;
    }

    /**
     * Sets the value of the whoDateTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link DateTime }
     *     
     */
    public void setWhoDateTime(DateTime value) {
        this.whoDateTime = value;
    }

    /**
     * Gets the value of the whoTime property.
     * 
     * @return
     *     possible object is
     *     {@link Time }
     *     
     */
    public Time getWhoTime() {
        return whoTime;
    }

    /**
     * Sets the value of the whoTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link Time }
     *     
     */
    public void setWhoTime(Time value) {
        this.whoTime = value;
    }

    /**
     * Gets the value of the whoCode property.
     * 
     * @return
     *     possible object is
     *     {@link Code }
     *     
     */
    public Code getWhoCode() {
        return whoCode;
    }

    /**
     * Sets the value of the whoCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link Code }
     *     
     */
    public void setWhoCode(Code value) {
        this.whoCode = value;
    }

    /**
     * Gets the value of the whoOid property.
     * 
     * @return
     *     possible object is
     *     {@link Oid }
     *     
     */
    public Oid getWhoOid() {
        return whoOid;
    }

    /**
     * Sets the value of the whoOid property.
     * 
     * @param value
     *     allowed object is
     *     {@link Oid }
     *     
     */
    public void setWhoOid(Oid value) {
        this.whoOid = value;
    }

    /**
     * Gets the value of the whoUuid property.
     * 
     * @return
     *     possible object is
     *     {@link Uuid }
     *     
     */
    public Uuid getWhoUuid() {
        return whoUuid;
    }

    /**
     * Sets the value of the whoUuid property.
     * 
     * @param value
     *     allowed object is
     *     {@link Uuid }
     *     
     */
    public void setWhoUuid(Uuid value) {
        this.whoUuid = value;
    }

    /**
     * Gets the value of the whoId property.
     * 
     * @return
     *     possible object is
     *     {@link Id }
     *     
     */
    public Id getWhoId() {
        return whoId;
    }

    /**
     * Sets the value of the whoId property.
     * 
     * @param value
     *     allowed object is
     *     {@link Id }
     *     
     */
    public void setWhoId(Id value) {
        this.whoId = value;
    }

    /**
     * Gets the value of the whoUnsignedInt property.
     * 
     * @return
     *     possible object is
     *     {@link UnsignedInt }
     *     
     */
    public UnsignedInt getWhoUnsignedInt() {
        return whoUnsignedInt;
    }

    /**
     * Sets the value of the whoUnsignedInt property.
     * 
     * @param value
     *     allowed object is
     *     {@link UnsignedInt }
     *     
     */
    public void setWhoUnsignedInt(UnsignedInt value) {
        this.whoUnsignedInt = value;
    }

    /**
     * Gets the value of the whoPositiveInt property.
     * 
     * @return
     *     possible object is
     *     {@link PositiveInt }
     *     
     */
    public PositiveInt getWhoPositiveInt() {
        return whoPositiveInt;
    }

    /**
     * Sets the value of the whoPositiveInt property.
     * 
     * @param value
     *     allowed object is
     *     {@link PositiveInt }
     *     
     */
    public void setWhoPositiveInt(PositiveInt value) {
        this.whoPositiveInt = value;
    }

    /**
     * Gets the value of the whoAttachment property.
     * 
     * @return
     *     possible object is
     *     {@link Attachment }
     *     
     */
    public Attachment getWhoAttachment() {
        return whoAttachment;
    }

    /**
     * Sets the value of the whoAttachment property.
     * 
     * @param value
     *     allowed object is
     *     {@link Attachment }
     *     
     */
    public void setWhoAttachment(Attachment value) {
        this.whoAttachment = value;
    }

    /**
     * Gets the value of the whoIdentifier property.
     * 
     * @return
     *     possible object is
     *     {@link Identifier }
     *     
     */
    public Identifier getWhoIdentifier() {
        return whoIdentifier;
    }

    /**
     * Sets the value of the whoIdentifier property.
     * 
     * @param value
     *     allowed object is
     *     {@link Identifier }
     *     
     */
    public void setWhoIdentifier(Identifier value) {
        this.whoIdentifier = value;
    }

    /**
     * Gets the value of the whoCodeableConcept property.
     * 
     * @return
     *     possible object is
     *     {@link CodeableConcept }
     *     
     */
    public CodeableConcept getWhoCodeableConcept() {
        return whoCodeableConcept;
    }

    /**
     * Sets the value of the whoCodeableConcept property.
     * 
     * @param value
     *     allowed object is
     *     {@link CodeableConcept }
     *     
     */
    public void setWhoCodeableConcept(CodeableConcept value) {
        this.whoCodeableConcept = value;
    }

    /**
     * Gets the value of the whoCoding property.
     * 
     * @return
     *     possible object is
     *     {@link Coding }
     *     
     */
    public Coding getWhoCoding() {
        return whoCoding;
    }

    /**
     * Sets the value of the whoCoding property.
     * 
     * @param value
     *     allowed object is
     *     {@link Coding }
     *     
     */
    public void setWhoCoding(Coding value) {
        this.whoCoding = value;
    }

    /**
     * Gets the value of the whoQuantity property.
     * 
     * @return
     *     possible object is
     *     {@link Quantity }
     *     
     */
    public Quantity getWhoQuantity() {
        return whoQuantity;
    }

    /**
     * Sets the value of the whoQuantity property.
     * 
     * @param value
     *     allowed object is
     *     {@link Quantity }
     *     
     */
    public void setWhoQuantity(Quantity value) {
        this.whoQuantity = value;
    }

    /**
     * Gets the value of the whoRange property.
     * 
     * @return
     *     possible object is
     *     {@link Range }
     *     
     */
    public Range getWhoRange() {
        return whoRange;
    }

    /**
     * Sets the value of the whoRange property.
     * 
     * @param value
     *     allowed object is
     *     {@link Range }
     *     
     */
    public void setWhoRange(Range value) {
        this.whoRange = value;
    }

    /**
     * Gets the value of the whoPeriod property.
     * 
     * @return
     *     possible object is
     *     {@link Period }
     *     
     */
    public Period getWhoPeriod() {
        return whoPeriod;
    }

    /**
     * Sets the value of the whoPeriod property.
     * 
     * @param value
     *     allowed object is
     *     {@link Period }
     *     
     */
    public void setWhoPeriod(Period value) {
        this.whoPeriod = value;
    }

    /**
     * Gets the value of the whoRatio property.
     * 
     * @return
     *     possible object is
     *     {@link Ratio }
     *     
     */
    public Ratio getWhoRatio() {
        return whoRatio;
    }

    /**
     * Sets the value of the whoRatio property.
     * 
     * @param value
     *     allowed object is
     *     {@link Ratio }
     *     
     */
    public void setWhoRatio(Ratio value) {
        this.whoRatio = value;
    }

    /**
     * Gets the value of the whoReference property.
     * 
     * @return
     *     possible object is
     *     {@link Reference }
     *     
     */
    public Reference getWhoReference() {
        return whoReference;
    }

    /**
     * Sets the value of the whoReference property.
     * 
     * @param value
     *     allowed object is
     *     {@link Reference }
     *     
     */
    public void setWhoReference(Reference value) {
        this.whoReference = value;
    }

    /**
     * Gets the value of the whoSampledData property.
     * 
     * @return
     *     possible object is
     *     {@link SampledData }
     *     
     */
    public SampledData getWhoSampledData() {
        return whoSampledData;
    }

    /**
     * Sets the value of the whoSampledData property.
     * 
     * @param value
     *     allowed object is
     *     {@link SampledData }
     *     
     */
    public void setWhoSampledData(SampledData value) {
        this.whoSampledData = value;
    }

    /**
     * Gets the value of the whoSignature property.
     * 
     * @return
     *     possible object is
     *     {@link Signature }
     *     
     */
    public Signature getWhoSignature() {
        return whoSignature;
    }

    /**
     * Sets the value of the whoSignature property.
     * 
     * @param value
     *     allowed object is
     *     {@link Signature }
     *     
     */
    public void setWhoSignature(Signature value) {
        this.whoSignature = value;
    }

    /**
     * Gets the value of the whoHumanName property.
     * 
     * @return
     *     possible object is
     *     {@link HumanName }
     *     
     */
    public HumanName getWhoHumanName() {
        return whoHumanName;
    }

    /**
     * Sets the value of the whoHumanName property.
     * 
     * @param value
     *     allowed object is
     *     {@link HumanName }
     *     
     */
    public void setWhoHumanName(HumanName value) {
        this.whoHumanName = value;
    }

    /**
     * Gets the value of the whoAddress property.
     * 
     * @return
     *     possible object is
     *     {@link Address }
     *     
     */
    public Address getWhoAddress() {
        return whoAddress;
    }

    /**
     * Sets the value of the whoAddress property.
     * 
     * @param value
     *     allowed object is
     *     {@link Address }
     *     
     */
    public void setWhoAddress(Address value) {
        this.whoAddress = value;
    }

    /**
     * Gets the value of the whoContactPoint property.
     * 
     * @return
     *     possible object is
     *     {@link ContactPoint }
     *     
     */
    public ContactPoint getWhoContactPoint() {
        return whoContactPoint;
    }

    /**
     * Sets the value of the whoContactPoint property.
     * 
     * @param value
     *     allowed object is
     *     {@link ContactPoint }
     *     
     */
    public void setWhoContactPoint(ContactPoint value) {
        this.whoContactPoint = value;
    }

    /**
     * Gets the value of the whoTiming property.
     * 
     * @return
     *     possible object is
     *     {@link Timing }
     *     
     */
    public Timing getWhoTiming() {
        return whoTiming;
    }

    /**
     * Sets the value of the whoTiming property.
     * 
     * @param value
     *     allowed object is
     *     {@link Timing }
     *     
     */
    public void setWhoTiming(Timing value) {
        this.whoTiming = value;
    }

    /**
     * Gets the value of the whoMeta property.
     * 
     * @return
     *     possible object is
     *     {@link Meta }
     *     
     */
    public Meta getWhoMeta() {
        return whoMeta;
    }

    /**
     * Sets the value of the whoMeta property.
     * 
     * @param value
     *     allowed object is
     *     {@link Meta }
     *     
     */
    public void setWhoMeta(Meta value) {
        this.whoMeta = value;
    }

    /**
     * Gets the value of the blob property.
     * 
     * @return
     *     possible object is
     *     {@link Base64Binary }
     *     
     */
    public Base64Binary getBlob() {
        return blob;
    }

    /**
     * Sets the value of the blob property.
     * 
     * @param value
     *     allowed object is
     *     {@link Base64Binary }
     *     
     */
    public void setBlob(Base64Binary value) {
        this.blob = value;
    }

}
