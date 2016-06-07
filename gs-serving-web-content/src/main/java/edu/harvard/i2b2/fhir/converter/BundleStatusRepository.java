package edu.harvard.i2b2.fhir.converter;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import hello.Customer;
public interface BundleStatusRepository extends CrudRepository<BundleStatus, Long> {

    List<BundleStatus> findByPatientId(String patientId);
}
