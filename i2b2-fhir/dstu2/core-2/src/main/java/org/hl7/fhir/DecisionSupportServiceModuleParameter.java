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
 * The DecisionSupportServiceModule resource describes decision support functionality that is available as a service.
 * 
 * <p>Java class for DecisionSupportServiceModule.Parameter complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="DecisionSupportServiceModule.Parameter">
 *   &lt;complexContent>
 *     &lt;extension base="{http://hl7.org/fhir}BackboneElement">
 *       &lt;sequence>
 *         &lt;element name="name" type="{http://hl7.org/fhir}code" minOccurs="0"/>
 *         &lt;element name="use" type="{http://hl7.org/fhir}code"/>
 *         &lt;element name="documentation" type="{http://hl7.org/fhir}string" minOccurs="0"/>
 *         &lt;element name="type" type="{http://hl7.org/fhir}code"/>
 *         &lt;element name="profile" type="{http://hl7.org/fhir}Reference" minOccurs="0"/>
 *         &lt;element name="mustSupport" type="{http://hl7.org/fhir}string" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="codeFilter" type="{http://hl7.org/fhir}DecisionSupportServiceModule.CodeFilter" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="dateFilter" type="{http://hl7.org/fhir}DecisionSupportServiceModule.DateFilter" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DecisionSupportServiceModule.Parameter", propOrder = {
    "name",
    "use",
    "documentation",
    "type",
    "profile",
    "mustSupport",
    "codeFilter",
    "dateFilter"
})
@javax.xml.bind.annotation.XmlRootElement(name="DecisionSupportServiceModuleParameter") 
public class DecisionSupportServiceModuleParameter
    extends BackboneElement
{

    protected Code name;
    @XmlElement(required = true)
    protected Code use;
    protected String documentation;
    @XmlElement(required = true)
    protected Code type;
    protected Reference profile;
    protected List<String> mustSupport;
    protected List<DecisionSupportServiceModuleCodeFilter> codeFilter;
    protected List<DecisionSupportServiceModuleDateFilter> dateFilter;

    /**
     * Gets the value of the name property.
     * 
     * @return
     *     possible object is
     *     {@link Code }
     *     
     */
    public Code getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     * 
     * @param value
     *     allowed object is
     *     {@link Code }
     *     
     */
    public void setName(Code value) {
        this.name = value;
    }

    /**
     * Gets the value of the use property.
     * 
     * @return
     *     possible object is
     *     {@link Code }
     *     
     */
    public Code getUse() {
        return use;
    }

    /**
     * Sets the value of the use property.
     * 
     * @param value
     *     allowed object is
     *     {@link Code }
     *     
     */
    public void setUse(Code value) {
        this.use = value;
    }

    /**
     * Gets the value of the documentation property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDocumentation() {
        return documentation;
    }

    /**
     * Sets the value of the documentation property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDocumentation(String value) {
        this.documentation = value;
    }

    /**
     * Gets the value of the type property.
     * 
     * @return
     *     possible object is
     *     {@link Code }
     *     
     */
    public Code getType() {
        return type;
    }

    /**
     * Sets the value of the type property.
     * 
     * @param value
     *     allowed object is
     *     {@link Code }
     *     
     */
    public void setType(Code value) {
        this.type = value;
    }

    /**
     * Gets the value of the profile property.
     * 
     * @return
     *     possible object is
     *     {@link Reference }
     *     
     */
    public Reference getProfile() {
        return profile;
    }

    /**
     * Sets the value of the profile property.
     * 
     * @param value
     *     allowed object is
     *     {@link Reference }
     *     
     */
    public void setProfile(Reference value) {
        this.profile = value;
    }

    /**
     * Gets the value of the mustSupport property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the mustSupport property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getMustSupport().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getMustSupport() {
        if (mustSupport == null) {
            mustSupport = new ArrayList<String>();
        }
        return this.mustSupport;
    }

    /**
     * Gets the value of the codeFilter property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the codeFilter property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCodeFilter().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link DecisionSupportServiceModuleCodeFilter }
     * 
     * 
     */
    public List<DecisionSupportServiceModuleCodeFilter> getCodeFilter() {
        if (codeFilter == null) {
            codeFilter = new ArrayList<DecisionSupportServiceModuleCodeFilter>();
        }
        return this.codeFilter;
    }

    /**
     * Gets the value of the dateFilter property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the dateFilter property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDateFilter().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link DecisionSupportServiceModuleDateFilter }
     * 
     * 
     */
    public List<DecisionSupportServiceModuleDateFilter> getDateFilter() {
        if (dateFilter == null) {
            dateFilter = new ArrayList<DecisionSupportServiceModuleDateFilter>();
        }
        return this.dateFilter;
    }

}
