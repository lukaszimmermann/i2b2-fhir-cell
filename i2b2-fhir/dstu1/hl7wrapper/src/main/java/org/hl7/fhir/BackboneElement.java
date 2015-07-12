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
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.03.11 at 12:26:00 PM EDT 
//


package org.hl7.fhir;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * An element defined in a FHIR resources - can have modifierExtension elements
 * 
 * <p>Java class for BackboneElement complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="BackboneElement">
 *   &lt;complexContent>
 *     &lt;extension base="{http://hl7.org/fhir}Element">
 *       &lt;sequence>
 *         &lt;element name="modifierExtension" type="{http://hl7.org/fhir}Extension" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "BackboneElement", propOrder = {
    "modifierExtension"
})
@XmlSeeAlso({
    SecurityEventSource.class,
    ProfileQuery.class,
    SecurityEventParticipant.class,
    SecurityEventDetail.class,
    MessageHeaderSource.class,
    ProfileType.class,
    ConceptMapMap.class,
    SupplyDispense.class,
    PatientContact.class,
    ConditionLocation.class,
    DeviceObservationReportChannel.class,
    ConformanceMessaging.class,
    QueryResponse.class,
    ValueSetDefine.class,
    AdverseReactionExposure.class,
    MedicationContent.class,
    EncounterLocation.class,
    CompositionAttester.class,
    ImmunizationVaccinationProtocol.class,
    ConformanceCertificate.class,
    EncounterAccomodation.class,
    DocumentReferenceService.class,
    ConformanceOperation1 .class,
    MedicationDispenseSubstitution.class,
    MedicationAdministrationDosage.class,
    ProfileMapping.class,
    ObservationRelated.class,
    ProfileConstraint.class,
    ImmunizationRecommendationRecommendation.class,
    ConformanceSoftware.class,
    OrganizationContact.class,
    SecurityEventEvent.class,
    ConceptMapConcept.class,
    CarePlanParticipant.class,
    ValueSetInclude.class,
    CarePlanGoal.class,
    MedicationPrescriptionSubstitution.class,
    ProfileElement.class,
    EncounterParticipant.class,
    ConditionEvidence.class,
    PatientLink.class,
    MessageHeaderDestination.class,
    DocumentReferenceParameter.class,
    SpecimenTreatment.class,
    MedicationPrescriptionDosageInstruction.class,
    SpecimenContainer.class,
    ConditionRelatedItem.class,
    DiagnosticOrderEvent.class,
    ProfileMapping1 .class,
    PatientAnimal.class,
    ValueSetFilter.class,
    ProcedurePerformer.class,
    EncounterHospitalization.class,
    MedicationStatementDosage.class,
    MedicationIngredient.class,
    ProfileStructure.class,
    ImmunizationReaction.class,
    SecurityEventObject.class,
    ValueSetCompose.class,
    ConformanceImplementation.class,
    ProvenanceEntity.class,
    ConformanceDocument.class,
    DeviceObservationReportMetric.class,
    ConformanceSearchParam.class,
    ImmunizationRecommendationProtocol.class,
    DocumentReferenceRelatesTo.class,
    PractitionerQualification.class,
    DeviceObservationReportVirtualDevice.class,
    MedicationDispenseDispense.class,
    CarePlanActivity.class,
    ConformanceOperation.class,
    DocumentReferenceContext.class,
    FamilyHistoryRelation.class,
    FamilyHistoryCondition.class,
    ImagingStudyInstance.class,
    SubstanceInstance.class,
    ObservationReferenceRange.class,
    CompositionSection.class,
    MedicationProduct.class,
    ProfileBinding.class,
    SubstanceIngredient.class,
    CarePlanSimple.class,
    ValueSetContains.class,
    CompositionEvent.class,
    ConformanceSecurity.class,
    ValueSetExpansion.class,
    ConformanceResource.class,
    ConformanceRest.class,
    ProfileSlicing.class,
    ProvenanceAgent.class,
    Resource.class,
    QuestionnaireGroup.class,
    SpecimenSource.class,
    ProcedureRelatedItem.class,
    SecurityEventNetwork.class,
    LocationPosition.class,
    ConditionStage.class,
    ConformanceQuery.class,
    ProfileSearchParam.class,
    ConceptMapDependsOn.class,
    ProfileExtensionDefn.class,
    QuestionnaireQuestion.class,
    MedicationPackage.class,
    DiagnosticReportImage.class,
    ConformanceEvent.class,
    MessageHeaderResponse.class,
    ValueSetConcept.class,
    SpecimenCollection.class,
    MedicationPrescriptionDispense.class,
    ListEntry.class,
    OperationOutcomeIssue.class,
    MedicationDispenseDosage.class,
    OrderWhen.class,
    AdverseReactionSymptom.class,
    ImmunizationExplanation.class,
    GroupCharacteristic.class,
    ProfileDefinition.class,
    DiagnosticOrderItem.class,
    ImmunizationRecommendationDateCriterion.class,
    ImagingStudySeries.class
})
public class BackboneElement
    extends Element
{

    protected List<Extension> modifierExtension;

    /**
     * Gets the value of the modifierExtension property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the modifierExtension property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getModifierExtension().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Extension }
     * 
     * 
     */
    public List<Extension> getModifierExtension() {
        if (modifierExtension == null) {
            modifierExtension = new ArrayList<Extension>();
        }
        return this.modifierExtension;
    }

}
