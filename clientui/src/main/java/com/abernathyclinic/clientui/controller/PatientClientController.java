package com.abernathyclinic.clientui.controller;

import com.abernathyclinic.clientui.bean.PatientBean;
import com.abernathyclinic.clientui.exception.AlreadyExistsException;
import com.abernathyclinic.clientui.exception.PatientNotFoundException;
import com.abernathyclinic.clientui.proxy.PatientProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
public class PatientClientController {
    @Autowired
    private final PatientProxy patientProxy;

    public PatientClientController(PatientProxy patientProxy) {
        this.patientProxy = patientProxy;
    }

    /**
     * Home page for patients. It shows the list of patients from database.
     *
     * @param model holder for context data to be passed from controller to the view, contains list of patients
     * @return patients list page
     */
    @GetMapping({"/", "/patient/list"})
    public String home(Model model) {
        model.addAttribute("patients", patientProxy.getPatients());
        return "/patient/list";
    }

    /**
     * Shows update a patient form.
     *
     * @param id    ID of patient to update
     * @param model holder for context data to be passed from controller to the view, contains patient to be updated
     * @return update patient page if error, list of patients otherwise
     */
    @GetMapping("/patient/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model, RedirectAttributes redirectAttributes) {
        PatientBean patient = patientProxy.getPatientById(id);
        if (patient != null) {
            model.addAttribute("patient", patient);
            return "patient/update";
        }
        redirectAttributes.addFlashAttribute("error", "Patient with ID " + id + " does not exist.");
        return "redirect:/patient/list";
    }

    /**
     * Updates a patient.
     *
     * @param id                 ID of patient to be updated
     * @param patient            updated patient
     * @param result             result of validation form
     * @param model              holder for context data to be passed from controller to the view, contains patient to update and list of patients
     * @param redirectAttributes redirection attributes, contains success popup
     * @return list of patients if update is successful, update patient page otherwise
     */
    @PostMapping("/patient/update/{id}")
    public String updatePatient(@PathVariable("id") Integer id, @Valid PatientBean patient,
                                BindingResult result, Model model, RedirectAttributes redirectAttributes) {
        // check required fields
        if (result.hasErrors()) {
            patient.setId(id);
            model.addAttribute("patient", patient);
            return "patient/update";
        }
        // if valid call service to update PatientBean
        patient.setId(id);
        try {
            patientProxy.updatePatient(id, patient);
            redirectAttributes.addFlashAttribute("success", "Patient " + patient.getFamily() + " " + patient.getGiven() + " was successfully updated.");
        } catch (PatientNotFoundException notFoundException) {
            redirectAttributes.addFlashAttribute("error", "Error while trying to update patient " + patient.getFamily() + " " + patient.getGiven() + ":\n" + notFoundException.getMessage());
        }
        model.addAttribute("patients", patientProxy.getPatients());
        return "redirect:/patient/list";
    }

    /**
     * Shows add patient form. User should provide at least a family name, a given name and a date of birth
     *
     * @param patient patient to be added
     * @return add patient page
     */
    @GetMapping("/patient/add")
    public String showAddPatientForm(PatientBean patient, Model model) {
        model.addAttribute("patient", patient);
        return "patient/add";
    }

    /**
     * Validates add patient form and redirects to patients list if successful.
     *
     * @param patient            patient to be added
     * @param result             result of form validation
     * @param model              holder for context data to be passed from controller to the view, contains list of patients
     * @param redirectAttributes redirection attributes, contains success popup
     * @return add patient page if an error occurred, list of patients otherwise
     */
    @PostMapping("/patient/add")
    public String addPatient(@Valid PatientBean patient, BindingResult result, Model model, RedirectAttributes redirectAttributes) {
        if (!result.hasErrors()) {
            try {
                patientProxy.createPatient(patient);
                redirectAttributes.addFlashAttribute("success", "Patient " + patient.getFamily() + " " + patient.getGiven() + " was successfully created.");
                model.addAttribute("patients", patientProxy.getPatients());
            } catch (AlreadyExistsException alreadyExistsException) {
                redirectAttributes.addFlashAttribute("error", "Error while trying to add patient " + patient.getFamily() + " " + patient.getGiven() + ":\n" + alreadyExistsException.getMessage());
            }
            // redirect to list of patients page
            return "redirect:/patient/list";
        }
        return "patient/add";
    }

    /**
     * Deletes a patient.
     *
     * @param id                 ID of patient to be deleted
     * @param model              holder for context data to be passed from controller to the view, contains list of patients
     * @param redirectAttributes redirection attributes, contains success or failure popup
     * @return list of patients page
     */
    @GetMapping("/patient/delete/{id}")
    public String deletePatient(@PathVariable("id") Integer id, Model model, RedirectAttributes redirectAttributes) {
        // Find PatientBean by ID
        PatientBean patient = patientProxy.getPatientById(id);
        // and delete the patient
        if (patient != null) {
            patientProxy.deletePatient(patient.getId());
            redirectAttributes.addFlashAttribute("success", "Patient " + patient.getFamily() + " " + patient.getGiven() + " was successfully deleted.");
        } else {
            redirectAttributes.addFlashAttribute("error", "Patient with ID " + id + " does not exist.");
        }
        // return to PatientBean list
        model.addAttribute("patients", patientProxy.getPatients());
        return "redirect:/patient/list";
    }

}