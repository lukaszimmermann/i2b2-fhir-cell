//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.12.30 at 02:43:29 AM EST 
//


package org.hl7.fhir;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for QuestionnaireItemType-list.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="QuestionnaireItemType-list">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="group"/>
 *     &lt;enumeration value="display"/>
 *     &lt;enumeration value="question"/>
 *     &lt;enumeration value="boolean"/>
 *     &lt;enumeration value="decimal"/>
 *     &lt;enumeration value="integer"/>
 *     &lt;enumeration value="date"/>
 *     &lt;enumeration value="dateTime"/>
 *     &lt;enumeration value="instant"/>
 *     &lt;enumeration value="time"/>
 *     &lt;enumeration value="string"/>
 *     &lt;enumeration value="text"/>
 *     &lt;enumeration value="url"/>
 *     &lt;enumeration value="choice"/>
 *     &lt;enumeration value="open-choice"/>
 *     &lt;enumeration value="attachment"/>
 *     &lt;enumeration value="reference"/>
 *     &lt;enumeration value="quantity"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "QuestionnaireItemType-list")
@XmlEnum
public enum QuestionnaireItemTypeList {


    /**
     * An item with no direct answer but which has descendant items that are questions
     * 
     */
    @XmlEnumValue("group")
    GROUP("group"),

    /**
     * Text for display that will not capture an answer or have descendants
     * 
     */
    @XmlEnumValue("display")
    DISPLAY("display"),

    /**
     * An item that defines a specific answer to be captured (and may have descendant items)
     * 
     */
    @XmlEnumValue("question")
    QUESTION("question"),

    /**
     * Question with a yes/no answer
     * 
     */
    @XmlEnumValue("boolean")
    BOOLEAN("boolean"),

    /**
     * Question with is a real number answer
     * 
     */
    @XmlEnumValue("decimal")
    DECIMAL("decimal"),

    /**
     * Question with an integer answer
     * 
     */
    @XmlEnumValue("integer")
    INTEGER("integer"),

    /**
     * Question with adate answer
     * 
     */
    @XmlEnumValue("date")
    DATE("date"),

    /**
     * Question with a date and time answer
     * 
     */
    @XmlEnumValue("dateTime")
    DATE_TIME("dateTime"),

    /**
     * Question with a system timestamp answer
     * 
     */
    @XmlEnumValue("instant")
    INSTANT("instant"),

    /**
     * Question with a time (hour/minute/second) answer independent of date.
     * 
     */
    @XmlEnumValue("time")
    TIME("time"),

    /**
     * Question with a short (few words to short sentence) free-text entry answer
     * 
     */
    @XmlEnumValue("string")
    STRING("string"),

    /**
     * Question with a long (potentially multi-paragraph) free-text entry (still captured as a string) answer
     * 
     */
    @XmlEnumValue("text")
    TEXT("text"),

    /**
     * Question with a url (website, FTP site, etc.) answer
     * 
     */
    @XmlEnumValue("url")
    URL("url"),

    /**
     * Question with a Coding drawn from a list of options as an answer
     * 
     */
    @XmlEnumValue("choice")
    CHOICE("choice"),

    /**
     * Answer is a Coding drawn from a list of options or a free-text entry captured as Coding.display
     * 
     */
    @XmlEnumValue("open-choice")
    OPEN_CHOICE("open-choice"),

    /**
     * Question with binary content such as a image, PDF, etc. as an answer
     * 
     */
    @XmlEnumValue("attachment")
    ATTACHMENT("attachment"),

    /**
     * Question with a reference to another resource (practitioner, organization, etc.) as an answer
     * 
     */
    @XmlEnumValue("reference")
    REFERENCE("reference"),

    /**
     * Question with a combination of a numeric value and unit, potentially with a comparator (<, >, etc.) as an answer.
     * 
     */
    @XmlEnumValue("quantity")
    QUANTITY("quantity");
    private final java.lang.String value;

    QuestionnaireItemTypeList(java.lang.String v) {
        value = v;
    }

    public java.lang.String value() {
        return value;
    }

    public static QuestionnaireItemTypeList fromValue(java.lang.String v) {
        for (QuestionnaireItemTypeList c: QuestionnaireItemTypeList.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}