package com.abernathyclinic.history.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "abernathy_clinic")
public class Note {
    @Id
    String id;
    @NotNull(message = "You must provide a patient ID")
    Integer patId;
    @NotBlank(message = "Notes/recommendations can not be blank")
    String content;
}
