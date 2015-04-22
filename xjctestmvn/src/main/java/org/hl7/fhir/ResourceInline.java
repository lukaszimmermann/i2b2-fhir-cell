//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.04.22 at 03:04:28 PM EDT 
//


package org.hl7.fhir;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Resource.Inline complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Resource.Inline">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;choice>
 *         &lt;element ref="{http://hl7.org/fhir}Binary"/>
 *         &lt;element ref="{http://hl7.org/fhir}Provenance"/>
 *         &lt;element ref="{http://hl7.org/fhir}Condition"/>
 *         &lt;element ref="{http://hl7.org/fhir}CarePlan"/>
 *         &lt;element ref="{http://hl7.org/fhir}Supply"/>
 *         &lt;element ref="{http://hl7.org/fhir}Device"/>
 *         &lt;element ref="{http://hl7.org/fhir}Query"/>
 *         &lt;element ref="{http://hl7.org/fhir}Order"/>
 *         &lt;element ref="{http://hl7.org/fhir}Organization"/>
 *         &lt;element ref="{http://hl7.org/fhir}Procedure"/>
 *         &lt;element ref="{http://hl7.org/fhir}Substance"/>
 *         &lt;element ref="{http://hl7.org/fhir}DiagnosticReport"/>
 *         &lt;element ref="{http://hl7.org/fhir}Group"/>
 *         &lt;element ref="{http://hl7.org/fhir}ValueSet"/>
 *         &lt;element ref="{http://hl7.org/fhir}Medication"/>
 *         &lt;element ref="{http://hl7.org/fhir}MessageHeader"/>
 *         &lt;element ref="{http://hl7.org/fhir}ImmunizationRecommendation"/>
 *         &lt;element ref="{http://hl7.org/fhir}DocumentManifest"/>
 *         &lt;element ref="{http://hl7.org/fhir}MedicationDispense"/>
 *         &lt;element ref="{http://hl7.org/fhir}MedicationPrescription"/>
 *         &lt;element ref="{http://hl7.org/fhir}MedicationAdministration"/>
 *         &lt;element ref="{http://hl7.org/fhir}Encounter"/>
 *         &lt;element ref="{http://hl7.org/fhir}SecurityEvent"/>
 *         &lt;element ref="{http://hl7.org/fhir}MedicationStatement"/>
 *         &lt;element ref="{http://hl7.org/fhir}List"/>
 *         &lt;element ref="{http://hl7.org/fhir}Questionnaire"/>
 *         &lt;element ref="{http://hl7.org/fhir}Composition"/>
 *         &lt;element ref="{http://hl7.org/fhir}DeviceObservationReport"/>
 *         &lt;element ref="{http://hl7.org/fhir}OperationOutcome"/>
 *         &lt;element ref="{http://hl7.org/fhir}Conformance"/>
 *         &lt;element ref="{http://hl7.org/fhir}Media"/>
 *         &lt;element ref="{http://hl7.org/fhir}FamilyHistory"/>
 *         &lt;element ref="{http://hl7.org/fhir}Other"/>
 *         &lt;element ref="{http://hl7.org/fhir}Profile"/>
 *         &lt;element ref="{http://hl7.org/fhir}Location"/>
 *         &lt;element ref="{http://hl7.org/fhir}Observation"/>
 *         &lt;element ref="{http://hl7.org/fhir}AllergyIntolerance"/>
 *         &lt;element ref="{http://hl7.org/fhir}DocumentReference"/>
 *         &lt;element ref="{http://hl7.org/fhir}Immunization"/>
 *         &lt;element ref="{http://hl7.org/fhir}RelatedPerson"/>
 *         &lt;element ref="{http://hl7.org/fhir}Specimen"/>
 *         &lt;element ref="{http://hl7.org/fhir}OrderResponse"/>
 *         &lt;element ref="{http://hl7.org/fhir}Alert"/>
 *         &lt;element ref="{http://hl7.org/fhir}ConceptMap"/>
 *         &lt;element ref="{http://hl7.org/fhir}Patient"/>
 *         &lt;element ref="{http://hl7.org/fhir}Practitioner"/>
 *         &lt;element ref="{http://hl7.org/fhir}AdverseReaction"/>
 *         &lt;element ref="{http://hl7.org/fhir}ImagingStudy"/>
 *         &lt;element ref="{http://hl7.org/fhir}DiagnosticOrder"/>
 *       &lt;/choice>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Resource.Inline", propOrder = {
    "binary",
    "provenance",
    "condition",
    "carePlan",
    "supply",
    "device",
    "query",
    "order",
    "organization",
    "procedure",
    "substance",
    "diagnosticReport",
    "group",
    "valueSet",
    "medication",
    "messageHeader",
    "immunizationRecommendation",
    "documentManifest",
    "medicationDispense",
    "medicationPrescription",
    "medicationAdministration",
    "encounter",
    "securityEvent",
    "medicationStatement",
    "list",
    "questionnaire",
    "composition",
    "deviceObservationReport",
    "operationOutcome",
    "conformance",
    "media",
    "familyHistory",
    "other",
    "profile",
    "location",
    "observation",
    "allergyIntolerance",
    "documentReference",
    "immunization",
    "relatedPerson",
    "specimen",
    "orderResponse",
    "alert",
    "conceptMap",
    "patient",
    "practitioner",
    "adverseReaction",
    "imagingStudy",
    "diagnosticOrder"
})
public class ResourceInline {

    @XmlElement(name = "Binary")
    protected Binary binary;
    @XmlElement(name = "Provenance")
    protected Provenance provenance;
    @XmlElement(name = "Condition")
    protected Condition condition;
    @XmlElement(name = "CarePlan")
    protected CarePlan carePlan;
    @XmlElement(name = "Supply")
    protected Supply supply;
    @XmlElement(name = "Device")
    protected Device device;
    @XmlElement(name = "Query")
    protected Query query;
    @XmlElement(name = "Order")
    protected Order order;
    @XmlElement(name = "Organization")
    protected Organization organization;
    @XmlElement(name = "Procedure")
    protected Procedure procedure;
    @XmlElement(name = "Substance")
    protected Substance substance;
    @XmlElement(name = "DiagnosticReport")
    protected DiagnosticReport diagnosticReport;
    @XmlElement(name = "Group")
    protected Group group;
    @XmlElement(name = "ValueSet")
    protected ValueSet valueSet;
    @XmlElement(name = "Medication")
    protected Medication medication;
    @XmlElement(name = "MessageHeader")
    protected MessageHeader messageHeader;
    @XmlElement(name = "ImmunizationRecommendation")
    protected ImmunizationRecommendation immunizationRecommendation;
    @XmlElement(name = "DocumentManifest")
    protected DocumentManifest documentManifest;
    @XmlElement(name = "MedicationDispense")
    protected MedicationDispense medicationDispense;
    @XmlElement(name = "MedicationPrescription")
    protected MedicationPrescription medicationPrescription;
    @XmlElement(name = "MedicationAdministration")
    protected MedicationAdministration medicationAdministration;
    @XmlElement(name = "Encounter")
    protected Encounter encounter;
    @XmlElement(name = "SecurityEvent")
    protected SecurityEvent securityEvent;
    @XmlElement(name = "MedicationStatement")
    protected MedicationStatement medicationStatement;
    @XmlElement(name = "List")
    protected List list;
    @XmlElement(name = "Questionnaire")
    protected Questionnaire questionnaire;
    @XmlElement(name = "Composition")
    protected Composition composition;
    @XmlElement(name = "DeviceObservationReport")
    protected DeviceObservationReport deviceObservationReport;
    @XmlElement(name = "OperationOutcome")
    protected OperationOutcome operationOutcome;
    @XmlElement(name = "Conformance")
    protected Conformance conformance;
    @XmlElement(name = "Media")
    protected Media media;
    @XmlElement(name = "FamilyHistory")
    protected FamilyHistory familyHistory;
    @XmlElement(name = "Other")
    protected Other other;
    @XmlElement(name = "Profile")
    protected Profile profile;
    @XmlElement(name = "Location")
    protected Location location;
    @XmlElement(name = "Observation")
    protected Observation observation;
    @XmlElement(name = "AllergyIntolerance")
    protected AllergyIntolerance allergyIntolerance;
    @XmlElement(name = "DocumentReference")
    protected DocumentReference documentReference;
    @XmlElement(name = "Immunization")
    protected Immunization immunization;
    @XmlElement(name = "RelatedPerson")
    protected RelatedPerson relatedPerson;
    @XmlElement(name = "Specimen")
    protected Specimen specimen;
    @XmlElement(name = "OrderResponse")
    protected OrderResponse orderResponse;
    @XmlElement(name = "Alert")
    protected Alert alert;
    @XmlElement(name = "ConceptMap")
    protected ConceptMap conceptMap;
    @XmlElement(name = "Patient")
    protected Patient patient;
    @XmlElement(name = "Practitioner")
    protected Practitioner practitioner;
    @XmlElement(name = "AdverseReaction")
    protected AdverseReaction adverseReaction;
    @XmlElement(name = "ImagingStudy")
    protected ImagingStudy imagingStudy;
    @XmlElement(name = "DiagnosticOrder")
    protected DiagnosticOrder diagnosticOrder;

    /**
     * Gets the value of the binary property.
     * 
     * @return
     *     possible object is
     *     {@link Binary }
     *     
     */
    public Binary getBinary() {
        return binary;
    }

    /**
     * Sets the value of the binary property.
     * 
     * @param value
     *     allowed object is
     *     {@link Binary }
     *     
     */
    public void setBinary(Binary value) {
        this.binary = value;
    }

    /**
     * Gets the value of the provenance property.
     * 
     * @return
     *     possible object is
     *     {@link Provenance }
     *     
     */
    public Provenance getProvenance() {
        return provenance;
    }

    /**
     * Sets the value of the provenance property.
     * 
     * @param value
     *     allowed object is
     *     {@link Provenance }
     *     
     */
    public void setProvenance(Provenance value) {
        this.provenance = value;
    }

    /**
     * Gets the value of the condition property.
     * 
     * @return
     *     possible object is
     *     {@link Condition }
     *     
     */
    public Condition getCondition() {
        return condition;
    }

    /**
     * Sets the value of the condition property.
     * 
     * @param value
     *     allowed object is
     *     {@link Condition }
     *     
     */
    public void setCondition(Condition value) {
        this.condition = value;
    }

    /**
     * Gets the value of the carePlan property.
     * 
     * @return
     *     possible object is
     *     {@link CarePlan }
     *     
     */
    public CarePlan getCarePlan() {
        return carePlan;
    }

    /**
     * Sets the value of the carePlan property.
     * 
     * @param value
     *     allowed object is
     *     {@link CarePlan }
     *     
     */
    public void setCarePlan(CarePlan value) {
        this.carePlan = value;
    }

    /**
     * Gets the value of the supply property.
     * 
     * @return
     *     possible object is
     *     {@link Supply }
     *     
     */
    public Supply getSupply() {
        return supply;
    }

    /**
     * Sets the value of the supply property.
     * 
     * @param value
     *     allowed object is
     *     {@link Supply }
     *     
     */
    public void setSupply(Supply value) {
        this.supply = value;
    }

    /**
     * Gets the value of the device property.
     * 
     * @return
     *     possible object is
     *     {@link Device }
     *     
     */
    public Device getDevice() {
        return device;
    }

    /**
     * Sets the value of the device property.
     * 
     * @param value
     *     allowed object is
     *     {@link Device }
     *     
     */
    public void setDevice(Device value) {
        this.device = value;
    }

    /**
     * Gets the value of the query property.
     * 
     * @return
     *     possible object is
     *     {@link Query }
     *     
     */
    public Query getQuery() {
        return query;
    }

    /**
     * Sets the value of the query property.
     * 
     * @param value
     *     allowed object is
     *     {@link Query }
     *     
     */
    public void setQuery(Query value) {
        this.query = value;
    }

    /**
     * Gets the value of the order property.
     * 
     * @return
     *     possible object is
     *     {@link Order }
     *     
     */
    public Order getOrder() {
        return order;
    }

    /**
     * Sets the value of the order property.
     * 
     * @param value
     *     allowed object is
     *     {@link Order }
     *     
     */
    public void setOrder(Order value) {
        this.order = value;
    }

    /**
     * Gets the value of the organization property.
     * 
     * @return
     *     possible object is
     *     {@link Organization }
     *     
     */
    public Organization getOrganization() {
        return organization;
    }

    /**
     * Sets the value of the organization property.
     * 
     * @param value
     *     allowed object is
     *     {@link Organization }
     *     
     */
    public void setOrganization(Organization value) {
        this.organization = value;
    }

    /**
     * Gets the value of the procedure property.
     * 
     * @return
     *     possible object is
     *     {@link Procedure }
     *     
     */
    public Procedure getProcedure() {
        return procedure;
    }

    /**
     * Sets the value of the procedure property.
     * 
     * @param value
     *     allowed object is
     *     {@link Procedure }
     *     
     */
    public void setProcedure(Procedure value) {
        this.procedure = value;
    }

    /**
     * Gets the value of the substance property.
     * 
     * @return
     *     possible object is
     *     {@link Substance }
     *     
     */
    public Substance getSubstance() {
        return substance;
    }

    /**
     * Sets the value of the substance property.
     * 
     * @param value
     *     allowed object is
     *     {@link Substance }
     *     
     */
    public void setSubstance(Substance value) {
        this.substance = value;
    }

    /**
     * Gets the value of the diagnosticReport property.
     * 
     * @return
     *     possible object is
     *     {@link DiagnosticReport }
     *     
     */
    public DiagnosticReport getDiagnosticReport() {
        return diagnosticReport;
    }

    /**
     * Sets the value of the diagnosticReport property.
     * 
     * @param value
     *     allowed object is
     *     {@link DiagnosticReport }
     *     
     */
    public void setDiagnosticReport(DiagnosticReport value) {
        this.diagnosticReport = value;
    }

    /**
     * Gets the value of the group property.
     * 
     * @return
     *     possible object is
     *     {@link Group }
     *     
     */
    public Group getGroup() {
        return group;
    }

    /**
     * Sets the value of the group property.
     * 
     * @param value
     *     allowed object is
     *     {@link Group }
     *     
     */
    public void setGroup(Group value) {
        this.group = value;
    }

    /**
     * Gets the value of the valueSet property.
     * 
     * @return
     *     possible object is
     *     {@link ValueSet }
     *     
     */
    public ValueSet getValueSet() {
        return valueSet;
    }

    /**
     * Sets the value of the valueSet property.
     * 
     * @param value
     *     allowed object is
     *     {@link ValueSet }
     *     
     */
    public void setValueSet(ValueSet value) {
        this.valueSet = value;
    }

    /**
     * Gets the value of the medication property.
     * 
     * @return
     *     possible object is
     *     {@link Medication }
     *     
     */
    public Medication getMedication() {
        return medication;
    }

    /**
     * Sets the value of the medication property.
     * 
     * @param value
     *     allowed object is
     *     {@link Medication }
     *     
     */
    public void setMedication(Medication value) {
        this.medication = value;
    }

    /**
     * Gets the value of the messageHeader property.
     * 
     * @return
     *     possible object is
     *     {@link MessageHeader }
     *     
     */
    public MessageHeader getMessageHeader() {
        return messageHeader;
    }

    /**
     * Sets the value of the messageHeader property.
     * 
     * @param value
     *     allowed object is
     *     {@link MessageHeader }
     *     
     */
    public void setMessageHeader(MessageHeader value) {
        this.messageHeader = value;
    }

    /**
     * Gets the value of the immunizationRecommendation property.
     * 
     * @return
     *     possible object is
     *     {@link ImmunizationRecommendation }
     *     
     */
    public ImmunizationRecommendation getImmunizationRecommendation() {
        return immunizationRecommendation;
    }

    /**
     * Sets the value of the immunizationRecommendation property.
     * 
     * @param value
     *     allowed object is
     *     {@link ImmunizationRecommendation }
     *     
     */
    public void setImmunizationRecommendation(ImmunizationRecommendation value) {
        this.immunizationRecommendation = value;
    }

    /**
     * Gets the value of the documentManifest property.
     * 
     * @return
     *     possible object is
     *     {@link DocumentManifest }
     *     
     */
    public DocumentManifest getDocumentManifest() {
        return documentManifest;
    }

    /**
     * Sets the value of the documentManifest property.
     * 
     * @param value
     *     allowed object is
     *     {@link DocumentManifest }
     *     
     */
    public void setDocumentManifest(DocumentManifest value) {
        this.documentManifest = value;
    }

    /**
     * Gets the value of the medicationDispense property.
     * 
     * @return
     *     possible object is
     *     {@link MedicationDispense }
     *     
     */
    public MedicationDispense getMedicationDispense() {
        return medicationDispense;
    }

    /**
     * Sets the value of the medicationDispense property.
     * 
     * @param value
     *     allowed object is
     *     {@link MedicationDispense }
     *     
     */
    public void setMedicationDispense(MedicationDispense value) {
        this.medicationDispense = value;
    }

    /**
     * Gets the value of the medicationPrescription property.
     * 
     * @return
     *     possible object is
     *     {@link MedicationPrescription }
     *     
     */
    public MedicationPrescription getMedicationPrescription() {
        return medicationPrescription;
    }

    /**
     * Sets the value of the medicationPrescription property.
     * 
     * @param value
     *     allowed object is
     *     {@link MedicationPrescription }
     *     
     */
    public void setMedicationPrescription(MedicationPrescription value) {
        this.medicationPrescription = value;
    }

    /**
     * Gets the value of the medicationAdministration property.
     * 
     * @return
     *     possible object is
     *     {@link MedicationAdministration }
     *     
     */
    public MedicationAdministration getMedicationAdministration() {
        return medicationAdministration;
    }

    /**
     * Sets the value of the medicationAdministration property.
     * 
     * @param value
     *     allowed object is
     *     {@link MedicationAdministration }
     *     
     */
    public void setMedicationAdministration(MedicationAdministration value) {
        this.medicationAdministration = value;
    }

    /**
     * Gets the value of the encounter property.
     * 
     * @return
     *     possible object is
     *     {@link Encounter }
     *     
     */
    public Encounter getEncounter() {
        return encounter;
    }

    /**
     * Sets the value of the encounter property.
     * 
     * @param value
     *     allowed object is
     *     {@link Encounter }
     *     
     */
    public void setEncounter(Encounter value) {
        this.encounter = value;
    }

    /**
     * Gets the value of the securityEvent property.
     * 
     * @return
     *     possible object is
     *     {@link SecurityEvent }
     *     
     */
    public SecurityEvent getSecurityEvent() {
        return securityEvent;
    }

    /**
     * Sets the value of the securityEvent property.
     * 
     * @param value
     *     allowed object is
     *     {@link SecurityEvent }
     *     
     */
    public void setSecurityEvent(SecurityEvent value) {
        this.securityEvent = value;
    }

    /**
     * Gets the value of the medicationStatement property.
     * 
     * @return
     *     possible object is
     *     {@link MedicationStatement }
     *     
     */
    public MedicationStatement getMedicationStatement() {
        return medicationStatement;
    }

    /**
     * Sets the value of the medicationStatement property.
     * 
     * @param value
     *     allowed object is
     *     {@link MedicationStatement }
     *     
     */
    public void setMedicationStatement(MedicationStatement value) {
        this.medicationStatement = value;
    }

    /**
     * Gets the value of the list property.
     * 
     * @return
     *     possible object is
     *     {@link List }
     *     
     */
    public List getList() {
        return list;
    }

    /**
     * Sets the value of the list property.
     * 
     * @param value
     *     allowed object is
     *     {@link List }
     *     
     */
    public void setList(List value) {
        this.list = value;
    }

    /**
     * Gets the value of the questionnaire property.
     * 
     * @return
     *     possible object is
     *     {@link Questionnaire }
     *     
     */
    public Questionnaire getQuestionnaire() {
        return questionnaire;
    }

    /**
     * Sets the value of the questionnaire property.
     * 
     * @param value
     *     allowed object is
     *     {@link Questionnaire }
     *     
     */
    public void setQuestionnaire(Questionnaire value) {
        this.questionnaire = value;
    }

    /**
     * Gets the value of the composition property.
     * 
     * @return
     *     possible object is
     *     {@link Composition }
     *     
     */
    public Composition getComposition() {
        return composition;
    }

    /**
     * Sets the value of the composition property.
     * 
     * @param value
     *     allowed object is
     *     {@link Composition }
     *     
     */
    public void setComposition(Composition value) {
        this.composition = value;
    }

    /**
     * Gets the value of the deviceObservationReport property.
     * 
     * @return
     *     possible object is
     *     {@link DeviceObservationReport }
     *     
     */
    public DeviceObservationReport getDeviceObservationReport() {
        return deviceObservationReport;
    }

    /**
     * Sets the value of the deviceObservationReport property.
     * 
     * @param value
     *     allowed object is
     *     {@link DeviceObservationReport }
     *     
     */
    public void setDeviceObservationReport(DeviceObservationReport value) {
        this.deviceObservationReport = value;
    }

    /**
     * Gets the value of the operationOutcome property.
     * 
     * @return
     *     possible object is
     *     {@link OperationOutcome }
     *     
     */
    public OperationOutcome getOperationOutcome() {
        return operationOutcome;
    }

    /**
     * Sets the value of the operationOutcome property.
     * 
     * @param value
     *     allowed object is
     *     {@link OperationOutcome }
     *     
     */
    public void setOperationOutcome(OperationOutcome value) {
        this.operationOutcome = value;
    }

    /**
     * Gets the value of the conformance property.
     * 
     * @return
     *     possible object is
     *     {@link Conformance }
     *     
     */
    public Conformance getConformance() {
        return conformance;
    }

    /**
     * Sets the value of the conformance property.
     * 
     * @param value
     *     allowed object is
     *     {@link Conformance }
     *     
     */
    public void setConformance(Conformance value) {
        this.conformance = value;
    }

    /**
     * Gets the value of the media property.
     * 
     * @return
     *     possible object is
     *     {@link Media }
     *     
     */
    public Media getMedia() {
        return media;
    }

    /**
     * Sets the value of the media property.
     * 
     * @param value
     *     allowed object is
     *     {@link Media }
     *     
     */
    public void setMedia(Media value) {
        this.media = value;
    }

    /**
     * Gets the value of the familyHistory property.
     * 
     * @return
     *     possible object is
     *     {@link FamilyHistory }
     *     
     */
    public FamilyHistory getFamilyHistory() {
        return familyHistory;
    }

    /**
     * Sets the value of the familyHistory property.
     * 
     * @param value
     *     allowed object is
     *     {@link FamilyHistory }
     *     
     */
    public void setFamilyHistory(FamilyHistory value) {
        this.familyHistory = value;
    }

    /**
     * Gets the value of the other property.
     * 
     * @return
     *     possible object is
     *     {@link Other }
     *     
     */
    public Other getOther() {
        return other;
    }

    /**
     * Sets the value of the other property.
     * 
     * @param value
     *     allowed object is
     *     {@link Other }
     *     
     */
    public void setOther(Other value) {
        this.other = value;
    }

    /**
     * Gets the value of the profile property.
     * 
     * @return
     *     possible object is
     *     {@link Profile }
     *     
     */
    public Profile getProfile() {
        return profile;
    }

    /**
     * Sets the value of the profile property.
     * 
     * @param value
     *     allowed object is
     *     {@link Profile }
     *     
     */
    public void setProfile(Profile value) {
        this.profile = value;
    }

    /**
     * Gets the value of the location property.
     * 
     * @return
     *     possible object is
     *     {@link Location }
     *     
     */
    public Location getLocation() {
        return location;
    }

    /**
     * Sets the value of the location property.
     * 
     * @param value
     *     allowed object is
     *     {@link Location }
     *     
     */
    public void setLocation(Location value) {
        this.location = value;
    }

    /**
     * Gets the value of the observation property.
     * 
     * @return
     *     possible object is
     *     {@link Observation }
     *     
     */
    public Observation getObservation() {
        return observation;
    }

    /**
     * Sets the value of the observation property.
     * 
     * @param value
     *     allowed object is
     *     {@link Observation }
     *     
     */
    public void setObservation(Observation value) {
        this.observation = value;
    }

    /**
     * Gets the value of the allergyIntolerance property.
     * 
     * @return
     *     possible object is
     *     {@link AllergyIntolerance }
     *     
     */
    public AllergyIntolerance getAllergyIntolerance() {
        return allergyIntolerance;
    }

    /**
     * Sets the value of the allergyIntolerance property.
     * 
     * @param value
     *     allowed object is
     *     {@link AllergyIntolerance }
     *     
     */
    public void setAllergyIntolerance(AllergyIntolerance value) {
        this.allergyIntolerance = value;
    }

    /**
     * Gets the value of the documentReference property.
     * 
     * @return
     *     possible object is
     *     {@link DocumentReference }
     *     
     */
    public DocumentReference getDocumentReference() {
        return documentReference;
    }

    /**
     * Sets the value of the documentReference property.
     * 
     * @param value
     *     allowed object is
     *     {@link DocumentReference }
     *     
     */
    public void setDocumentReference(DocumentReference value) {
        this.documentReference = value;
    }

    /**
     * Gets the value of the immunization property.
     * 
     * @return
     *     possible object is
     *     {@link Immunization }
     *     
     */
    public Immunization getImmunization() {
        return immunization;
    }

    /**
     * Sets the value of the immunization property.
     * 
     * @param value
     *     allowed object is
     *     {@link Immunization }
     *     
     */
    public void setImmunization(Immunization value) {
        this.immunization = value;
    }

    /**
     * Gets the value of the relatedPerson property.
     * 
     * @return
     *     possible object is
     *     {@link RelatedPerson }
     *     
     */
    public RelatedPerson getRelatedPerson() {
        return relatedPerson;
    }

    /**
     * Sets the value of the relatedPerson property.
     * 
     * @param value
     *     allowed object is
     *     {@link RelatedPerson }
     *     
     */
    public void setRelatedPerson(RelatedPerson value) {
        this.relatedPerson = value;
    }

    /**
     * Gets the value of the specimen property.
     * 
     * @return
     *     possible object is
     *     {@link Specimen }
     *     
     */
    public Specimen getSpecimen() {
        return specimen;
    }

    /**
     * Sets the value of the specimen property.
     * 
     * @param value
     *     allowed object is
     *     {@link Specimen }
     *     
     */
    public void setSpecimen(Specimen value) {
        this.specimen = value;
    }

    /**
     * Gets the value of the orderResponse property.
     * 
     * @return
     *     possible object is
     *     {@link OrderResponse }
     *     
     */
    public OrderResponse getOrderResponse() {
        return orderResponse;
    }

    /**
     * Sets the value of the orderResponse property.
     * 
     * @param value
     *     allowed object is
     *     {@link OrderResponse }
     *     
     */
    public void setOrderResponse(OrderResponse value) {
        this.orderResponse = value;
    }

    /**
     * Gets the value of the alert property.
     * 
     * @return
     *     possible object is
     *     {@link Alert }
     *     
     */
    public Alert getAlert() {
        return alert;
    }

    /**
     * Sets the value of the alert property.
     * 
     * @param value
     *     allowed object is
     *     {@link Alert }
     *     
     */
    public void setAlert(Alert value) {
        this.alert = value;
    }

    /**
     * Gets the value of the conceptMap property.
     * 
     * @return
     *     possible object is
     *     {@link ConceptMap }
     *     
     */
    public ConceptMap getConceptMap() {
        return conceptMap;
    }

    /**
     * Sets the value of the conceptMap property.
     * 
     * @param value
     *     allowed object is
     *     {@link ConceptMap }
     *     
     */
    public void setConceptMap(ConceptMap value) {
        this.conceptMap = value;
    }

    /**
     * Gets the value of the patient property.
     * 
     * @return
     *     possible object is
     *     {@link Patient }
     *     
     */
    public Patient getPatient() {
        return patient;
    }

    /**
     * Sets the value of the patient property.
     * 
     * @param value
     *     allowed object is
     *     {@link Patient }
     *     
     */
    public void setPatient(Patient value) {
        this.patient = value;
    }

    /**
     * Gets the value of the practitioner property.
     * 
     * @return
     *     possible object is
     *     {@link Practitioner }
     *     
     */
    public Practitioner getPractitioner() {
        return practitioner;
    }

    /**
     * Sets the value of the practitioner property.
     * 
     * @param value
     *     allowed object is
     *     {@link Practitioner }
     *     
     */
    public void setPractitioner(Practitioner value) {
        this.practitioner = value;
    }

    /**
     * Gets the value of the adverseReaction property.
     * 
     * @return
     *     possible object is
     *     {@link AdverseReaction }
     *     
     */
    public AdverseReaction getAdverseReaction() {
        return adverseReaction;
    }

    /**
     * Sets the value of the adverseReaction property.
     * 
     * @param value
     *     allowed object is
     *     {@link AdverseReaction }
     *     
     */
    public void setAdverseReaction(AdverseReaction value) {
        this.adverseReaction = value;
    }

    /**
     * Gets the value of the imagingStudy property.
     * 
     * @return
     *     possible object is
     *     {@link ImagingStudy }
     *     
     */
    public ImagingStudy getImagingStudy() {
        return imagingStudy;
    }

    /**
     * Sets the value of the imagingStudy property.
     * 
     * @param value
     *     allowed object is
     *     {@link ImagingStudy }
     *     
     */
    public void setImagingStudy(ImagingStudy value) {
        this.imagingStudy = value;
    }

    /**
     * Gets the value of the diagnosticOrder property.
     * 
     * @return
     *     possible object is
     *     {@link DiagnosticOrder }
     *     
     */
    public DiagnosticOrder getDiagnosticOrder() {
        return diagnosticOrder;
    }

    /**
     * Sets the value of the diagnosticOrder property.
     * 
     * @param value
     *     allowed object is
     *     {@link DiagnosticOrder }
     *     
     */
    public void setDiagnosticOrder(DiagnosticOrder value) {
        this.diagnosticOrder = value;
    }

}
