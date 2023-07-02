package com.abernathyclinic.clientui.controller;

import com.abernathyclinic.clientui.proxy.PatientProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PatientClientController {
	@Autowired
	private final PatientProxy patientProxy;

	public PatientClientController(PatientProxy patientProxy) {
		this.patientProxy = patientProxy;
	}

	@GetMapping("/patient/list")
	public String home(Model model) {
		model.addAttribute("patients", patientProxy.getPatients());
		return "/patient/list";
	}
}