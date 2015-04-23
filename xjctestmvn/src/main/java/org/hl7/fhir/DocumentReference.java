//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.04.23 at 01:15:20 PM EDT 
//


package org.hl7.fhir;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * If the element is present, it must have either a @value, an @id, or extensions
 * 
 * <p>Java class for DocumentReference complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="DocumentReference">
 *   &lt;complexContent>
 *     &lt;extension base="{http://hl7.org/fhir}Resource">
 *       &lt;sequence>
 *         &lt;element name="masterIdentifier" type="{http://hl7.org/fhir}Identifier"/>
 *         &lt;element name="identifier" type="{http://hl7.org/fhir}Identifier" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="subject" type="{http://hl7.org/fhir}ResourceReference"/>
 *         &lt;element name="type" type="{http://hl7.org/fhir}CodeableConcept"/>
 *         &lt;element name="class" type="{http://hl7.org/fhir}CodeableConcept" minOccurs="0"/>
 *         &lt;element name="author" type="{http://hl7.org/fhir}ResourceReference" maxOccurs="unbounded"/>
 *         &lt;element name="custodian" type="{http://hl7.org/fhir}ResourceReference" minOccurs="0"/>
 *         &lt;element name="policyManager" type="{http://hl7.org/fhir}uri" minOccurs="0"/>
 *         &lt;element name="authenticator" type="{http://hl7.org/fhir}ResourceReference" minOccurs="0"/>
 *         &lt;element name="created" type="{http://hl7.org/fhir}dateTime" minOccurs="0"/>
 *         &lt;element name="indexed" type="{http://hl7.org/fhir}instant"/>
 *         &lt;element name="status" type="{http://hl7.org/fhir}DocumentReferenceStatus"/>
 *         &lt;element name="docStatus" type="{http://hl7.org/fhir}CodeableConcept" minOccurs="0"/>
 *         &lt;element name="relatesTo" type="{http://hl7.org/fhir}DocumentReference.RelatesTo" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="description" type="{http://hl7.org/fhir}string" minOccurs="0"/>
 *         &lt;element name="confidentiality" type="{http://hl7.org/fhir}CodeableConcept" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="primaryLanguage" type="{http://hl7.org/fhir}code" minOccurs="0"/>
 *         &lt;element name="mimeType" type="{http://hl7.org/fhir}code"/>
 *         &lt;element name="format" type="{http://hl7.org/fhir}uri" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="size" type="{http://hl7.org/fhir}integer" minOccurs="0"/>
 *         &lt;element name="hash" type="{http://hl7.org/fhir}string" minOccurs="0"/>
 *         &lt;element name="location" type="{http://hl7.org/fhir}uri" minOccurs="0"/>
 *         &lt;element name="service" type="{http://hl7.org/fhir}DocumentReference.Service" minOccurs="0"/>
 *         &lt;element name="context" type="{http://hl7.org/fhir}DocumentReference.Context" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DocumentReference", propOrder = {
    "masterIdentifier",
    "identifier",
    "subject",
    "type",
    "clazz",
    "author",
    "custodian",
    "policyManager",
    "authenticator",
    "created",
    "indexed",
    "status",
    "docStatus",
    "relatesTo",
    "description",
    "confidentiality",
    "primaryLanguage",
    "mimeType",
    "format",
    "size",
    "hash",
    "location",
    "service",
    "context"
})
public class DocumentReference
    extends Resource
{

    @XmlElement(required = true)
    protected Identifier masterIdentifier;
    protected List<Identifier> identifier;
    @XmlElement(required = true)
    protected ResourceReference subject;
    @XmlElement(required = true)
    protected CodeableConcept type;
    @XmlElement(name = "class")
    protected CodeableConcept clazz;
    @XmlElement(required = true)
    protected List<ResourceReference> author;
    protected ResourceReference custodian;
    protected Uri policyManager;
    protected ResourceReference authenticator;
    protected DateTime created;
    @XmlElement(required = true)
    protected Instant indexed;
    @XmlElement(required = true)
    protected DocumentReferenceStatus status;
    protected CodeableConcept docStatus;
    protected List<DocumentReferenceRelatesTo> relatesTo;
    protected String description;
    protected List<CodeableConcept> confidentiality;
    protected Code primaryLanguage;
    @XmlElement(required = true)
    protected Code mimeType;
    protected List<Uri> format;
    protected Integer size;
    protected String hash;
    protected Uri location;
    protected DocumentReferenceService service;
    protected DocumentReferenceContext context;

    /**
     * Gets the value of the masterIdentifier property.
     * 
     * @return
     *     possible object is
     *     {@link Identifier }
     *     
     */
    public Identifier getMasterIdentifier() {
        return masterIdentifier;
    }

    /**
     * Sets the value of the masterIdentifier property.
     * 
     * @param value
     *     allowed object is
     *     {@link Identifier }
     *     
     */
    public void setMasterIdentifier(Identifier value) {
        this.masterIdentifier = value;
    }

    /**
     * Gets the value of the identifier property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the identifier property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getIdentifier().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Identifier }
     * 
     * 
     */
    public List<Identifier> getIdentifier() {
        if (identifier == null) {
            identifier = new ArrayList<Identifier>();
        }
        return this.identifier;
    }

    /**
     * Gets the value of the subject property.
     * 
     * @return
     *     possible object is
     *     {@link ResourceReference }
     *     
     */
    public ResourceReference getSubject() {
        return subject;
    }

    /**
     * Sets the value of the subject property.
     * 
     * @param value
     *     allowed object is
     *     {@link ResourceReference }
     *     
     */
    public void setSubject(ResourceReference value) {
        this.subject = value;
    }

    /**
     * Gets the value of the type property.
     * 
     * @return
     *     possible object is
     *     {@link CodeableConcept }
     *     
     */
    public CodeableConcept getType() {
        return type;
    }

    /**
     * Sets the value of the type property.
     * 
     * @param value
     *     allowed object is
     *     {@link CodeableConcept }
     *     
     */
    public void setType(CodeableConcept value) {
        this.type = value;
    }

    /**
     * Gets the value of the clazz property.
     * 
     * @return
     *     possible object is
     *     {@link CodeableConcept }
     *     
     */
    public CodeableConcept getClazz() {
        return clazz;
    }

    /**
     * Sets the value of the clazz property.
     * 
     * @param value
     *     allowed object is
     *     {@link CodeableConcept }
     *     
     */
    public void setClazz(CodeableConcept value) {
        this.clazz = value;
    }

    /**
     * Gets the value of the author property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the author property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAuthor().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ResourceReference }
     * 
     * 
     */
    public List<ResourceReference> getAuthor() {
        if (author == null) {
            author = new ArrayList<ResourceReference>();
        }
        return this.author;
    }

    /**
     * Gets the value of the custodian property.
     * 
     * @return
     *     possible object is
     *     {@link ResourceReference }
     *     
     */
    public ResourceReference getCustodian() {
        return custodian;
    }

    /**
     * Sets the value of the custodian property.
     * 
     * @param value
     *     allowed object is
     *     {@link ResourceReference }
     *     
     */
    public void setCustodian(ResourceReference value) {
        this.custodian = value;
    }

    /**
     * Gets the value of the policyManager property.
     * 
     * @return
     *     possible object is
     *     {@link Uri }
     *     
     */
    public Uri getPolicyManager() {
        return policyManager;
    }

    /**
     * Sets the value of the policyManager property.
     * 
     * @param value
     *     allowed object is
     *     {@link Uri }
     *     
     */
    public void setPolicyManager(Uri value) {
        this.policyManager = value;
    }

    /**
     * Gets the value of the authenticator property.
     * 
     * @return
     *     possible object is
     *     {@link ResourceReference }
     *     
     */
    public ResourceReference getAuthenticator() {
        return authenticator;
    }

    /**
     * Sets the value of the authenticator property.
     * 
     * @param value
     *     allowed object is
     *     {@link ResourceReference }
     *     
     */
    public void setAuthenticator(ResourceReference value) {
        this.authenticator = value;
    }

    /**
     * Gets the value of the created property.
     * 
     * @return
     *     possible object is
     *     {@link DateTime }
     *     
     */
    public DateTime getCreated() {
        return created;
    }

    /**
     * Sets the value of the created property.
     * 
     * @param value
     *     allowed object is
     *     {@link DateTime }
     *     
     */
    public void setCreated(DateTime value) {
        this.created = value;
    }

    /**
     * Gets the value of the indexed property.
     * 
     * @return
     *     possible object is
     *     {@link Instant }
     *     
     */
    public Instant getIndexed() {
        return indexed;
    }

    /**
     * Sets the value of the indexed property.
     * 
     * @param value
     *     allowed object is
     *     {@link Instant }
     *     
     */
    public void setIndexed(Instant value) {
        this.indexed = value;
    }

    /**
     * Gets the value of the status property.
     * 
     * @return
     *     possible object is
     *     {@link DocumentReferenceStatus }
     *     
     */
    public DocumentReferenceStatus getStatus() {
        return status;
    }

    /**
     * Sets the value of the status property.
     * 
     * @param value
     *     allowed object is
     *     {@link DocumentReferenceStatus }
     *     
     */
    public void setStatus(DocumentReferenceStatus value) {
        this.status = value;
    }

    /**
     * Gets the value of the docStatus property.
     * 
     * @return
     *     possible object is
     *     {@link CodeableConcept }
     *     
     */
    public CodeableConcept getDocStatus() {
        return docStatus;
    }

    /**
     * Sets the value of the docStatus property.
     * 
     * @param value
     *     allowed object is
     *     {@link CodeableConcept }
     *     
     */
    public void setDocStatus(CodeableConcept value) {
        this.docStatus = value;
    }

    /**
     * Gets the value of the relatesTo property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the relatesTo property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getRelatesTo().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link DocumentReferenceRelatesTo }
     * 
     * 
     */
    public List<DocumentReferenceRelatesTo> getRelatesTo() {
        if (relatesTo == null) {
            relatesTo = new ArrayList<DocumentReferenceRelatesTo>();
        }
        return this.relatesTo;
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
     * Gets the value of the confidentiality property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the confidentiality property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getConfidentiality().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link CodeableConcept }
     * 
     * 
     */
    public List<CodeableConcept> getConfidentiality() {
        if (confidentiality == null) {
            confidentiality = new ArrayList<CodeableConcept>();
        }
        return this.confidentiality;
    }

    /**
     * Gets the value of the primaryLanguage property.
     * 
     * @return
     *     possible object is
     *     {@link Code }
     *     
     */
    public Code getPrimaryLanguage() {
        return primaryLanguage;
    }

    /**
     * Sets the value of the primaryLanguage property.
     * 
     * @param value
     *     allowed object is
     *     {@link Code }
     *     
     */
    public void setPrimaryLanguage(Code value) {
        this.primaryLanguage = value;
    }

    /**
     * Gets the value of the mimeType property.
     * 
     * @return
     *     possible object is
     *     {@link Code }
     *     
     */
    public Code getMimeType() {
        return mimeType;
    }

    /**
     * Sets the value of the mimeType property.
     * 
     * @param value
     *     allowed object is
     *     {@link Code }
     *     
     */
    public void setMimeType(Code value) {
        this.mimeType = value;
    }

    /**
     * Gets the value of the format property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the format property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getFormat().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Uri }
     * 
     * 
     */
    public List<Uri> getFormat() {
        if (format == null) {
            format = new ArrayList<Uri>();
        }
        return this.format;
    }

    /**
     * Gets the value of the size property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getSize() {
        return size;
    }

    /**
     * Sets the value of the size property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setSize(Integer value) {
        this.size = value;
    }

    /**
     * Gets the value of the hash property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHash() {
        return hash;
    }

    /**
     * Sets the value of the hash property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHash(String value) {
        this.hash = value;
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
     * Gets the value of the service property.
     * 
     * @return
     *     possible object is
     *     {@link DocumentReferenceService }
     *     
     */
    public DocumentReferenceService getService() {
        return service;
    }

    /**
     * Sets the value of the service property.
     * 
     * @param value
     *     allowed object is
     *     {@link DocumentReferenceService }
     *     
     */
    public void setService(DocumentReferenceService value) {
        this.service = value;
    }

    /**
     * Gets the value of the context property.
     * 
     * @return
     *     possible object is
     *     {@link DocumentReferenceContext }
     *     
     */
    public DocumentReferenceContext getContext() {
        return context;
    }

    /**
     * Sets the value of the context property.
     * 
     * @param value
     *     allowed object is
     *     {@link DocumentReferenceContext }
     *     
     */
    public void setContext(DocumentReferenceContext value) {
        this.context = value;
    }

}
