package com.froi.library.services.bookloan;

import com.froi.library.dto.bookloan.RevenueResponseDTO;
import com.froi.library.exceptions.EntitySyntaxException;

import java.util.List;
import java.util.Map;

public interface BookLoanReportsService {
    List<Map<String, Object>> findBookLoansDueToday(String date) throws EntitySyntaxException;
    
    List<Map<String, Object>> findOverdueBookLoans(String date) throws EntitySyntaxException;
    
    RevenueResponseDTO findTotalRevenueBetweenDates(String startDate, String endDate) throws EntitySyntaxException;
    
    RevenueResponseDTO findTotalRevenue();
    
}
