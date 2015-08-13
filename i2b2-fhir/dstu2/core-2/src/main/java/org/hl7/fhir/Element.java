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
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * If the element is present, it must have a value for at least one of the defined elements, an @id referenced from the Narrative, or extensions
 * 
 * <p>Java class for Element complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Element">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="extension" type="{http://hl7.org/fhir}Extension" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="id" type="{http://hl7.org/fhir}id-primitive" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Element", propOrder = {
    "extension"
})
@XmlSeeAlso({
    InstanceAvailability.class,
    ElementDefinitionSlicing.class,
    ClaimType.class,
    EncounterClass.class,
    MedicationKind.class,
    CommunicationRequestStatus.class,
    AllergyIntoleranceType.class,
    CarePlanActivityStatus.class,
    LocationStatus.class,
    NamingSystemIdentifierType.class,
    ContactPoint.class,
    OrderStatus.class,
    DataType.class,
    DeviceMetricCalibrationState.class,
    SlotStatus.class,
    VisionEyes.class,
    OperationKind.class,
    NoteType.class,
    SubscriptionChannelType.class,
    ExtensionContext.class,
    DeviceStatus.class,
    RestfulConformanceMode.class,
    FlagStatus.class,
    DocumentMode.class,
    ClinicalImpressionStatus.class,
    Attachment.class,
    ProvenanceEntityRole.class,
    ReferralStatus.class,
    DataAbsentReason.class,
    DeviceMetricCategory.class,
    ResponseType.class,
    CompositionStatus.class,
    RemittanceOutcome.class,
    Decimal.class,
    DocumentReferenceStatus.class,
    CodeableConcept.class,
    ConformanceUseContext.class,
    SearchEntryMode.class,
    DigitalMediaType.class,
    NarrativeStatus.class,
    SupplyStatus.class,
    UnitsOfTime.class,
    UnsignedInt.class,
    ImagingModality.class,
    Address.class,
    AggregationMode.class,
    AppointmentStatus.class,
    Id.class,
    ElementDefinition.class,
    GroupType.class,
    ActionList.class,
    Timing.class,
    DateTime.class,
    MessageEvent.class,
    ParticipationStatus.class,
    Instant.class,
    SupplyDispenseStatus.class,
    LocationMode.class,
    DocumentRelationshipType.class,
    ResourceVersionPolicy.class,
    Use.class,
    Uri.class,
    AdministrativeGender.class,
    LinkType.class,
    EventTiming.class,
    DeviceMetricCalibrationType.class,
    Integer.class,
    ParametersPart.class,
    FHIRDefinedType.class,
    Range.class,
    MaritalStatus.class,
    Base64Binary.class,
    IssueSeverity.class,
    SpecialValues.class,
    SampledDataDataType.class,
    Modality.class,
    Code.class,
    PropertyRepresentation.class,
    CarePlanStatus.class,
    Oid.class,
    ProcedureStatus.class,
    TimingRepeat.class,
    EpisodeOfCareStatus.class,
    QuestionnaireAnswersStatus.class,
    DeviceMetricColor.class,
    SearchParamType.class,
    NamingSystemType.class,
    AllergyIntoleranceStatus.class,
    SubscriptionStatus.class,
    CompositionAttestationMode.class,
    Meta.class,
    MedicationAdministrationStatus.class,
    VisionBase.class,
    BundleType.class,
    SampledData.class,
    AllergyIntoleranceSeverity.class,
    EncounterState.class,
    SystemRestfulInteraction.class,
    FilterOperator.class,
    DeviceUseRequestStatus.class,
    ObservationReliability.class,
    ConceptMapEquivalence.class,
    AnswerFormat.class,
    AuditEventObjectType.class,
    QuantityComparator.class,
    OperationParameterUse.class,
    ObservationRelationshipType.class,
    DataElementSpecificity.class,
    ResourceType.class,
    IdentifierUse.class,
    QuestionnaireStatus.class,
    BindingStrength.class,
    ConformanceResourceStatus.class,
    Identifier.class,
    ContactPointSystem.class,
    StructureDefinitionType.class,
    GoalStatus.class,
    org.hl7.fhir.String.class,
    ParticipantStatus.class,
    AllergyIntoleranceCriticality.class,
    MedicationPrescriptionStatus.class,
    ListStatus.class,
    AuditEventAction.class,
    Extension.class,
    ParticipantRequired.class,
    SlicingRules.class,
    Time.class,
    HTTPVerb.class,
    DeviceUseRequestPriority.class,
    ConditionClinicalStatus.class,
    NameUse.class,
    DiagnosticOrderStatus.class,
    MessageSignificanceCategory.class,
    Coding.class,
    PositiveInt.class,
    DaysOfWeek.class,
    AuditEventObjectLifecycle.class,
    AllergyIntoleranceCategory.class,
    ElementDefinitionBinding.class,
    CommunicationStatus.class,
    ProcedureRequestPriority.class,
    TypeRestfulInteraction.class,
    ObservationStatus.class,
    ElementDefinitionMapping.class,
    CarePlanActivityCategory.class,
    Ratio.class,
    AuditEventOutcome.class,
    DiagnosticReportStatus.class,
    Reference.class,
    AuditEventParticipantNetworkType.class,
    AuditEventObjectRole.class,
    DeviceMetricOperationalStatus.class,
    Date.class,
    ElementDefinitionConstraint.class,
    ConstraintSeverity.class,
    ListMode.class,
    Uuid.class,
    MedicationStatementStatus.class,
    ProcedureRequestStatus.class,
    NutritionOrderStatus.class,
    Narrative.class,
    ClinicalBaseGender.class,
    Period.class,
    ProcedureRelationshipType.class,
    HumanName.class,
    AddressUse.class,
    ConformanceEventMode.class,
    MeasmntPrinciple.class,
    ContactPointUse.class,
    ElementDefinitionType.class,
    Quantity.class,
    AllergyIntoleranceCertainty.class,
    ParametersParameter.class,
    Boolean.class,
    MedicationDispenseStatus.class,
    BackboneElement.class,
    EncounterLocationStatus.class,
    Signature.class,
    DiagnosticOrderPriority.class,
    IdentityAssuranceLevel.class
})
public class Element {

    protected List<Extension> extension;
    @XmlAttribute(name = "id")
    protected java.lang.String id;

    /**
     * Gets the value of the extension property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the extension property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getExtension().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Extension }
     * 
     * 
     */
    public List<Extension> getExtension() {
        if (extension == null) {
            extension = new ArrayList<Extension>();
        }
        return this.extension;
    }

    /**
     * Gets the value of the id property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String }
     *     
     */
    public java.lang.String getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String }
     *     
     */
    public void setId(java.lang.String value) {
        this.id = value;
    }

}