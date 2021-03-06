//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.12.30 at 02:43:29 AM EST 
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
 * <p>Java class for ImagingStudy complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ImagingStudy">
 *   &lt;complexContent>
 *     &lt;extension base="{http://hl7.org/fhir}DomainResource">
 *       &lt;sequence>
 *         &lt;element name="started" type="{http://hl7.org/fhir}dateTime" minOccurs="0"/>
 *         &lt;element name="patient" type="{http://hl7.org/fhir}Reference"/>
 *         &lt;element name="uid" type="{http://hl7.org/fhir}oid"/>
 *         &lt;element name="accession" type="{http://hl7.org/fhir}Identifier" minOccurs="0"/>
 *         &lt;element name="identifier" type="{http://hl7.org/fhir}Identifier" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="order" type="{http://hl7.org/fhir}Reference" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="modalityList" type="{http://hl7.org/fhir}Coding" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="referrer" type="{http://hl7.org/fhir}Reference" minOccurs="0"/>
 *         &lt;element name="availability" type="{http://hl7.org/fhir}InstanceAvailability" minOccurs="0"/>
 *         &lt;element name="url" type="{http://hl7.org/fhir}uri" minOccurs="0"/>
 *         &lt;element name="numberOfSeries" type="{http://hl7.org/fhir}unsignedInt"/>
 *         &lt;element name="numberOfInstances" type="{http://hl7.org/fhir}unsignedInt"/>
 *         &lt;element name="procedure" type="{http://hl7.org/fhir}Reference" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="interpreter" type="{http://hl7.org/fhir}Reference" minOccurs="0"/>
 *         &lt;element name="description" type="{http://hl7.org/fhir}string" minOccurs="0"/>
 *         &lt;element name="series" type="{http://hl7.org/fhir}ImagingStudy.Series" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ImagingStudy", propOrder = {
    "started",
    "patient",
    "uid",
    "accession",
    "identifier",
    "order",
    "modalityList",
    "referrer",
    "availability",
    "url",
    "numberOfSeries",
    "numberOfInstances",
    "procedure",
    "interpreter",
    "description",
    "series"
})
@javax.xml.bind.annotation.XmlRootElement(name="ImagingStudy") 
public class ImagingStudy
    extends DomainResource
{

    protected DateTime started;
    @XmlElement(required = true)
    protected Reference patient;
    @XmlElement(required = true)
    protected Oid uid;
    protected Identifier accession;
    protected List<Identifier> identifier;
    protected List<Reference> order;
    protected List<Coding> modalityList;
    protected Reference referrer;
    protected InstanceAvailability availability;
    protected Uri url;
    @XmlElement(required = true)
    protected UnsignedInt numberOfSeries;
    @XmlElement(required = true)
    protected UnsignedInt numberOfInstances;
    protected List<Reference> procedure;
    protected Reference interpreter;
    protected String description;
    protected List<ImagingStudySeries> series;

    /**
     * Gets the value of the started property.
     * 
     * @return
     *     possible object is
     *     {@link DateTime }
     *     
     */
    public DateTime getStarted() {
        return started;
    }

    /**
     * Sets the value of the started property.
     * 
     * @param value
     *     allowed object is
     *     {@link DateTime }
     *     
     */
    public void setStarted(DateTime value) {
        this.started = value;
    }

    /**
     * Gets the value of the patient property.
     * 
     * @return
     *     possible object is
     *     {@link Reference }
     *     
     */
    public Reference getPatient() {
        return patient;
    }

    /**
     * Sets the value of the patient property.
     * 
     * @param value
     *     allowed object is
     *     {@link Reference }
     *     
     */
    public void setPatient(Reference value) {
        this.patient = value;
    }

    /**
     * Gets the value of the uid property.
     * 
     * @return
     *     possible object is
     *     {@link Oid }
     *     
     */
    public Oid getUid() {
        return uid;
    }

    /**
     * Sets the value of the uid property.
     * 
     * @param value
     *     allowed object is
     *     {@link Oid }
     *     
     */
    public void setUid(Oid value) {
        this.uid = value;
    }

    /**
     * Gets the value of the accession property.
     * 
     * @return
     *     possible object is
     *     {@link Identifier }
     *     
     */
    public Identifier getAccession() {
        return accession;
    }

    /**
     * Sets the value of the accession property.
     * 
     * @param value
     *     allowed object is
     *     {@link Identifier }
     *     
     */
    public void setAccession(Identifier value) {
        this.accession = value;
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
     * Gets the value of the order property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the order property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getOrder().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Reference }
     * 
     * 
     */
    public List<Reference> getOrder() {
        if (order == null) {
            order = new ArrayList<Reference>();
        }
        return this.order;
    }

    /**
     * Gets the value of the modalityList property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the modalityList property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getModalityList().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Coding }
     * 
     * 
     */
    public List<Coding> getModalityList() {
        if (modalityList == null) {
            modalityList = new ArrayList<Coding>();
        }
        return this.modalityList;
    }

    /**
     * Gets the value of the referrer property.
     * 
     * @return
     *     possible object is
     *     {@link Reference }
     *     
     */
    public Reference getReferrer() {
        return referrer;
    }

    /**
     * Sets the value of the referrer property.
     * 
     * @param value
     *     allowed object is
     *     {@link Reference }
     *     
     */
    public void setReferrer(Reference value) {
        this.referrer = value;
    }

    /**
     * Gets the value of the availability property.
     * 
     * @return
     *     possible object is
     *     {@link InstanceAvailability }
     *     
     */
    public InstanceAvailability getAvailability() {
        return availability;
    }

    /**
     * Sets the value of the availability property.
     * 
     * @param value
     *     allowed object is
     *     {@link InstanceAvailability }
     *     
     */
    public void setAvailability(InstanceAvailability value) {
        this.availability = value;
    }

    /**
     * Gets the value of the url property.
     * 
     * @return
     *     possible object is
     *     {@link Uri }
     *     
     */
    public Uri getUrl() {
        return url;
    }

    /**
     * Sets the value of the url property.
     * 
     * @param value
     *     allowed object is
     *     {@link Uri }
     *     
     */
    public void setUrl(Uri value) {
        this.url = value;
    }

    /**
     * Gets the value of the numberOfSeries property.
     * 
     * @return
     *     possible object is
     *     {@link UnsignedInt }
     *     
     */
    public UnsignedInt getNumberOfSeries() {
        return numberOfSeries;
    }

    /**
     * Sets the value of the numberOfSeries property.
     * 
     * @param value
     *     allowed object is
     *     {@link UnsignedInt }
     *     
     */
    public void setNumberOfSeries(UnsignedInt value) {
        this.numberOfSeries = value;
    }

    /**
     * Gets the value of the numberOfInstances property.
     * 
     * @return
     *     possible object is
     *     {@link UnsignedInt }
     *     
     */
    public UnsignedInt getNumberOfInstances() {
        return numberOfInstances;
    }

    /**
     * Sets the value of the numberOfInstances property.
     * 
     * @param value
     *     allowed object is
     *     {@link UnsignedInt }
     *     
     */
    public void setNumberOfInstances(UnsignedInt value) {
        this.numberOfInstances = value;
    }

    /**
     * Gets the value of the procedure property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the procedure property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getProcedure().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Reference }
     * 
     * 
     */
    public List<Reference> getProcedure() {
        if (procedure == null) {
            procedure = new ArrayList<Reference>();
        }
        return this.procedure;
    }

    /**
     * Gets the value of the interpreter property.
     * 
     * @return
     *     possible object is
     *     {@link Reference }
     *     
     */
    public Reference getInterpreter() {
        return interpreter;
    }

    /**
     * Sets the value of the interpreter property.
     * 
     * @param value
     *     allowed object is
     *     {@link Reference }
     *     
     */
    public void setInterpreter(Reference value) {
        this.interpreter = value;
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
     * Gets the value of the series property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the series property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSeries().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ImagingStudySeries }
     * 
     * 
     */
    public List<ImagingStudySeries> getSeries() {
        if (series == null) {
            series = new ArrayList<ImagingStudySeries>();
        }
        return this.series;
    }

}
