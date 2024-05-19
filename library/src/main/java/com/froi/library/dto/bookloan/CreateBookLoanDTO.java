package com.froi.library.dto.bookloan;

import lombok.Setter;
import lombok.Value;

import java.util.List;

@Value
@Setter
public class CreateBookLoanDTO {
    List<String> bookCodes;
    String studentId;
    String loanDate;
}
