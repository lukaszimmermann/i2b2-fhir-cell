//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.07.03 at 11:19:33 PM EDT 
//


package edu.harvard.i2b2.fhir.core;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the edu.harvard.i2b2.fhir.core package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _MetaData_QNAME = new QName("http://i2b2.harvard.edu/fhir/core", "MetaData");
    private final static QName _MetaResourceSet_QNAME = new QName("http://i2b2.harvard.edu/fhir/core", "MetaResourceSet");
    private final static QName _MetaResource_QNAME = new QName("http://i2b2.harvard.edu/fhir/core", "MetaResource");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: edu.harvard.i2b2.fhir.core
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link MetaResourceSet }
     * 
     */
    public MetaResourceSet createMetaResourceSet() {
        return new MetaResourceSet();
    }

    /**
     * Create an instance of {@link MetaData }
     * 
     */
    public MetaData createMetaData() {
        return new MetaData();
    }

    /**
     * Create an instance of {@link MetaResource }
     * 
     */
    public MetaResource createMetaResource() {
        return new MetaResource();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link MetaData }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://i2b2.harvard.edu/fhir/core", name = "MetaData")
    public JAXBElement<MetaData> createMetaData(MetaData value) {
        return new JAXBElement<MetaData>(_MetaData_QNAME, MetaData.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link MetaResourceSet }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://i2b2.harvard.edu/fhir/core", name = "MetaResourceSet")
    public JAXBElement<MetaResourceSet> createMetaResourceSet(MetaResourceSet value) {
        return new JAXBElement<MetaResourceSet>(_MetaResourceSet_QNAME, MetaResourceSet.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link MetaResource }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://i2b2.harvard.edu/fhir/core", name = "MetaResource")
    public JAXBElement<MetaResource> createMetaResource(MetaResource value) {
        return new JAXBElement<MetaResource>(_MetaResource_QNAME, MetaResource.class, null, value);
    }

}