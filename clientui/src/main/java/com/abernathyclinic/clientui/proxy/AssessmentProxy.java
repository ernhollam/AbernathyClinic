package com.abernathyclinic.clientui.proxy;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "assessments", url = "${assessments.url}")
public interface AssessmentProxy {
    @GetMapping("/assess/{patientId}")
    String assessPatientRisk(@PathVariable("patientId") Integer patientId);
}
