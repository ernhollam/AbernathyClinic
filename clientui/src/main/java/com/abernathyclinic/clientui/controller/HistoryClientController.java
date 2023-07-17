package com.abernathyclinic.clientui.controller;

import com.abernathyclinic.clientui.bean.NoteBean;
import com.abernathyclinic.clientui.bean.PatientBean;
import com.abernathyclinic.clientui.exception.PatientNotFoundException;
import com.abernathyclinic.clientui.proxy.HistoryProxy;
import com.abernathyclinic.clientui.proxy.PatientProxy;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class HistoryClientController {
	@Autowired
	private PatientProxy patientProxy;
	@Autowired
	private HistoryProxy historyProxy;

	/**
	 * Show list of notes for patient if they exist
	 *
	 * @param model holder for context data to be passed from controller to the view, contains list of notes
	 * @return Patient history, list of patients if patient was not found
	 */
	@GetMapping("/patient/{patientId}/patHistory")
	public String showPatientHistory(@PathVariable("patientId") Integer patientId, Model model,
			RedirectAttributes redirectAttributes) {
		PatientBean patient = getPatientIfExists(patientId, redirectAttributes);
		if (patient == null) {
			return "patient/list";
		}
		model.addAttribute("patHistory", historyProxy.getPatientHistory(patientId));
		model.addAttribute("patient", patient);
		model.addAttribute("currentPage", "history");
		return "/note/list";
	}

	/**
	 * Shows update a note form.
	 *
	 * @param noteId ID of note to update
	 * @param model  holder for context data to be passed from controller to the view, contains note to be updated
	 * @return update note page if error, list of notes otherwise
	 */
	@GetMapping("/patient/{patientId}/patHistory/update/{noteId}")
	public String showUpdateNoteForm(@PathVariable("patientId") Integer patientId,
			@PathVariable("noteId") String noteId, Model model, RedirectAttributes redirectAttributes) {
		PatientBean patient = getPatientIfExists(patientId, redirectAttributes);
		if (patient == null) {
			return "patient/list";
		}
		NoteBean note = historyProxy.getNoteById(noteId);
		if (note != null) {
			model.addAttribute("note", note);
			model.addAttribute("patient", patient);
			return "note/update";
		}
		// return to patient profile if note was not found
		redirectAttributes.addFlashAttribute("error", "Note with ID " + noteId + " does not exist.");
		model.addAttribute("patHistory", historyProxy.getPatientHistory(patientId));
		model.addAttribute("currentPage", "history");
		return "redirect:/note/list";
	}

	/**
	 * Updates a note.
	 *
	 * @param noteId             ID of note to be updated
	 * @param note               updated note
	 * @param result             result of validation form
	 * @param model              holder for context data to be passed from controller to the view, contains note to update and list of notes
	 * @param redirectAttributes redirection attributes, contains success popup
	 * @return list of notes if update is successful, update note page otherwise
	 */
	@PostMapping("/patient/{patientId}/patHistory/update/{noteId}")
	public String updateNote(@PathVariable("patientId") Integer patientId, @PathVariable("noteId") String noteId,
			@Valid NoteBean note,
			BindingResult result, Model model, RedirectAttributes redirectAttributes) {
		PatientBean patient = getPatientIfExists(patientId, redirectAttributes);
		if (patient == null) {
			return "patient/list";
		}
		// check required fields
		if (result.hasErrors()) {
			note.setId(noteId);
			model.addAttribute("note", note);
			return "note/update";
		}
		// if valid call service to update NoteBean
		note.setId(noteId);
		try {
			historyProxy.updateNote(noteId, note);
			redirectAttributes.addFlashAttribute("success", "Note " + note.getId() + " was successfully updated.");
		} catch (Exception exception) {
			redirectAttributes.addFlashAttribute("error",
					"Error while trying to update note " + note.getId() + ":\n" + exception.getMessage());
		}
		model.addAttribute("patHistory", historyProxy.getPatientHistory(patientId));
		model.addAttribute("patient", patient);
		model.addAttribute("currentPage", "history");
		return "redirect:/patient/" + patient.getId() + "/patHistory";
	}

	/**
	 * Shows add note form. User should provide at least a family name, a given name and a date of birth
	 *
	 * @param note note to be added
	 * @return add note page
	 */
	@GetMapping("/patient/{patientId}/patHistory/add")
	public String showAddNoteForm(@PathVariable("patientId") Integer patientId, NoteBean note, Model model) {
		note.setPatId(patientId);
		model.addAttribute("note", note);
		model.addAttribute("patient", patientProxy.getPatientById(note.getPatId()));
		model.addAttribute("currentPage", "history");
		return "note/add";
	}

	/**
	 * Validates add note form and redirects to notes list if successful.
	 *
	 * @param note               note to be added
	 * @param result             result of form validation
	 * @param model              holder for context data to be passed from controller to the view, contains list of notes
	 * @param redirectAttributes redirection attributes, contains success popup
	 * @return add note page if an error occurred, list of notes otherwise
	 */
	@PostMapping("/patient/{patientId}/patHistory/add")
	public String addNoteForPatient(@PathVariable("patientId") Integer patientId, @Valid NoteBean note,
			BindingResult result, Model model, RedirectAttributes redirectAttributes) {
		PatientBean patient = getPatientIfExists(patientId, redirectAttributes);
		model.addAttribute("currentPage", "history");
		if (patient == null) {
			return "patient/list";
		}
		model.addAttribute("patient", patient);
		note.setPatId(patientId);
		model.addAttribute("note", note);
		if (!result.hasErrors()) {
			try {
				historyProxy.createNote(note);
				redirectAttributes.addFlashAttribute("success",
						"Note " + note.getContent() + " was successfully created.");
				model.addAttribute("notes", patientProxy.getPatients());
			} catch (Exception exception) {
				redirectAttributes.addFlashAttribute("error",
						"Error while trying to add note " + note.getContent() + ":\n" + exception.getMessage());
			}
			model.addAttribute("patHistory", historyProxy.getPatientHistory(patientId));
			model.addAttribute("patient", patient);
			model.addAttribute("currentPage", "history");
			// redirect to list of notes page
			return "redirect:/patient/" + patient.getId() + "/patHistory";
		}
		return "note/add";
	}

	/**
	 * Deletes a note.
	 *
	 * @param patientId          ID of note to be deleted
	 * @param model              holder for context data to be passed from controller to the view, contains list of notes
	 * @param redirectAttributes redirection attributes, contains success or failure popup
	 * @return list of notes page
	 */
	@GetMapping("/patient/{patientId}/patHistory/delete/{noteId}")
	public String deletePatient(@PathVariable("patientId") Integer patientId, @PathVariable("noteId") String noteId,
			Model model, RedirectAttributes redirectAttributes) {
		PatientBean patient = getPatientIfExists(patientId, redirectAttributes);
		if (patient == null) {
			return "patient/list";
		}
		// Find NoteBean by ID
		NoteBean note = historyProxy.getNoteById(noteId);
		// and delete the note
		if (note != null) {
			historyProxy.deleteNote(noteId);
			redirectAttributes.addFlashAttribute("success", "Note " + note.getContent() + " was successfully deleted.");
			model.addAttribute("patient", patient);
		} else {
			redirectAttributes.addFlashAttribute("error", "Note with ID " + noteId + " does not exist.");
		}
		// return to NoteBean list
		model.addAttribute("patHistory", historyProxy.getPatientHistory(patientId));
		model.addAttribute("currentPage", "history");
		return "redirect:/patient/" + patient.getId() + "/patHistory";
	}

	/**
	 * Checks if patient for which you want to show history or update a note for really exists. If so, the process goes on, if not, an error message is thrown.
	 *
	 * @param patientId          Note to be updated or deleted.
	 * @param redirectAttributes error message
	 * @return patient if exists, null otherwise
	 */
	private PatientBean getPatientIfExists(Integer patientId, RedirectAttributes redirectAttributes) {
		try {
			return patientProxy.getPatientById(patientId);
		} catch (PatientNotFoundException patientNotFoundException) {
			log.error("Patient with the provided ID " + patientId + " was not found.");
			redirectAttributes.addFlashAttribute("error", "Patient with ID " + patientId + "does not exist.");
		}
		return null;
	}
}