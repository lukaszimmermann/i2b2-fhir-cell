//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.05.28 at 05:45:45 PM EDT 
//


package edu.harvard.i2b2.map;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for MapSystemSet complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="MapSystemSet">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="MapSystem" type="{http://i2b2.harvard.edu/map}MapSystem" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MapSystemSet", propOrder = {
    "mapSystem"
})
@XmlRootElement
public class MapSystemSet {

    @XmlElement(name = "MapSystem", required = true)
    protected List<MapSystem> mapSystem;

    /**
     * Gets the value of the mapSystem property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the mapSystem property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getMapSystem().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link MapSystem }
     * 
     * 
     */
    public List<MapSystem> getMapSystem() {
        if (mapSystem == null) {
            mapSystem = new ArrayList<MapSystem>();
        }
        return this.mapSystem;
    }

}
