package com.abernathyclinic.clientui.proxy;

import com.abernathyclinic.clientui.bean.PatientBean;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.util.List;

@FeignClient(name = "patients", url = "http://localhost:8081")
public interface PatientProxy {
	@PostMapping("/patient")
	PatientBean createPatient(@Valid @RequestBody PatientBean patient);

	@GetMapping("/patient")
	List<PatientBean> getPatients();

	@GetMapping("/patient/{id}")
	PatientBean getPatientById(@PathVariable("id") Integer id);

	@PutMapping("/patient/{id}")
	PatientBean updatePatient(@PathVariable("id") Integer id, @Valid @RequestBody PatientBean patient);

	@DeleteMapping("/patient/{id}")
	void deletePatient(@PathVariable("id") Integer id);
}
