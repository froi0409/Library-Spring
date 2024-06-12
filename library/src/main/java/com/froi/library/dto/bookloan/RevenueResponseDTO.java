package com.froi.library.dto.bookloan;

import com.froi.library.entities.BookLoan;
import lombok.Value;

import java.util.List;
import java.util.Map;

@Value
public class RevenueResponseDTO {
    Map<String, Double> revenueInformation;
    List<BookLoan> bookLoanList;
}
