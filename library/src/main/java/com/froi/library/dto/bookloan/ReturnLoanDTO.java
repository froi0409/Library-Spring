package com.froi.library.dto.bookloan;

import lombok.Value;

@Value
public class ReturnLoanDTO {
    String loanId;
    String totalPay;
    String delayTotalPay;
    String returnDate;
}
