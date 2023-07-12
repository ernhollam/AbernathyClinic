package com.abernathyclinic.clientui.proxy;

import com.abernathyclinic.clientui.bean.NoteBean;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.util.List;

@FeignClient(name = "history", url = "${history.url}")
public interface HistoryProxy {
    @PostMapping("/patHistory")
    NoteBean createNote(@Valid @RequestBody NoteBean patient);

    @GetMapping("/patHistory")
    List<NoteBean> getNotes();

    @GetMapping("/patHistory/{id}")
    NoteBean getNoteById(@PathVariable("id") String id);

    @GetMapping("/patHistory/patient/{patientId}")
    List<NoteBean> getPatientHistory(@PathVariable("patientId") Integer patientId);

    @PutMapping("/patHistory/{id}")
    NoteBean updateNote(@PathVariable("id") String id, @Valid @RequestBody NoteBean patient);

    @DeleteMapping("/patHistory/{id}")
    void deleteNote(@PathVariable("id") String id);
}
