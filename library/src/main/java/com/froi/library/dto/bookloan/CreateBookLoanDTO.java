package com.froi.library.dto.bookloan;

import lombok.Value;

import java.util.List;

@Value
public class CreateBookLoanDTO {
    List<String> bookCodes;
    String studentId;
    String loanDate;
}
