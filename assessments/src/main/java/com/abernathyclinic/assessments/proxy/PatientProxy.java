package com.abernathyclinic.assessments.proxy;

import com.abernathyclinic.assessments.bean.PatientBean;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@FeignClient(name = "patients", url = "${patients.url}")
public interface PatientProxy {
	@PostMapping("/patient")
	PatientBean createPatient(@Valid @RequestBody PatientBean patient);

	@GetMapping("/patient")
	List<PatientBean> getPatients();

	@GetMapping("/patient/{id}")
	PatientBean getPatientById(@PathVariable("id") Integer id);

	@GetMapping(value = "/patient/familyName")
	List<PatientBean> getPatientByFamilyName(@RequestParam("family") String family);

	@PutMapping("/patient/{id}")
	PatientBean updatePatient(@PathVariable("id") Integer id, @Valid @RequestBody PatientBean patient);

	@DeleteMapping("/patient/{id}")
	void deletePatient(@PathVariable("id") Integer id);
}
