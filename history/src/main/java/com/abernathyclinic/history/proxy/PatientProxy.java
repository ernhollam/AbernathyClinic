package com.abernathyclinic.history.proxy;

import com.abernathyclinic.history.bean.PatientBean;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "patients", url = "${patients.url}")
public interface PatientProxy {
    @GetMapping("/patient/{id}")
    PatientBean getPatientById(@PathVariable("id") Integer id);
}
