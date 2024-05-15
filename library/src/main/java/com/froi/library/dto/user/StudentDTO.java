package com.froi.library.dto.user;

import lombok.Setter;
import lombok.Value;

@Value
@Setter
public class StudentDTO {
    String id;
    String firstName;
    String lastName;
    String degree;
    String birthDate;
    String status;
}
