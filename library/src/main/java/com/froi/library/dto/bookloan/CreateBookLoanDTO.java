package com.froi.library.dto.bookloan;

import lombok.Value;

@Value
public class CreateBookLoanDTO {
    String bookCode;
    String studentId;
    String degreeId;
}
