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
//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.05.29 at 11:13:41 AM EDT 
//


package edu.harvard.i2b2.map;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the edu.harvard.i2b2.map package. 
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

    private final static QName _MapSystem_QNAME = new QName("http://i2b2.harvard.edu/map", "MapSystem");
    private final static QName _MapSystemSet_QNAME = new QName("http://i2b2.harvard.edu/map", "MapSystemSet");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: edu.harvard.i2b2.map
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link MapSystemSet }
     * 
     */
    public MapSystemSet createMapSystemSet() {
        return new MapSystemSet();
    }

    /**
     * Create an instance of {@link MapSystem }
     * 
     */
    public MapSystem createMapSystem() {
        return new MapSystem();
    }

    /**
     * Create an instance of {@link ValueMap }
     * 
     */
    public ValueMap createValueMap() {
        return new ValueMap();
    }

    /**
     * Create an instance of {@link FromPath }
     * 
     */
    public FromPath createFromPath() {
        return new FromPath();
    }

    /**
     * Create an instance of {@link ToPath }
     * 
     */
    public ToPath createToPath() {
        return new ToPath();
    }

    /**
     * Create an instance of {@link NameSpaceDeclaration }
     * 
     */
    public NameSpaceDeclaration createNameSpaceDeclaration() {
        return new NameSpaceDeclaration();
    }

    /**
     * Create an instance of {@link Map }
     * 
     */
    public Map createMap() {
        return new Map();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link MapSystem }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://i2b2.harvard.edu/map", name = "MapSystem")
    public JAXBElement<MapSystem> createMapSystem(MapSystem value) {
        return new JAXBElement<MapSystem>(_MapSystem_QNAME, MapSystem.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link MapSystemSet }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://i2b2.harvard.edu/map", name = "MapSystemSet")
    public JAXBElement<MapSystemSet> createMapSystemSet(MapSystemSet value) {
        return new JAXBElement<MapSystemSet>(_MapSystemSet_QNAME, MapSystemSet.class, null, value);
    }

}
