package com.froi.library.services.bookloan;

import com.froi.library.dto.bookloan.RevenueResponseDTO;
import com.froi.library.entities.BookLoan;
import com.froi.library.exceptions.EntitySyntaxException;
import com.froi.library.repositories.BookLoanRepository;
import com.froi.library.services.tools.ToolsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class BookLoanReportsServiceImpl implements BookLoanReportsService {
    
    private BookLoanRepository bookLoanRepository;
    private ToolsService toolsService;
    
    @Autowired
    public BookLoanReportsServiceImpl(BookLoanRepository bookLoanRepository, ToolsService toolsService) {
        this.bookLoanRepository = bookLoanRepository;
        this.toolsService = toolsService;
    }
    
    @Override
    public List<Map<String, Object>> findBookLoansDueToday(String date) throws EntitySyntaxException {
        if (date != null && !toolsService.isValidDateFormat(date)) {
            throw new EntitySyntaxException("INVALID_DATE");
        }
        return bookLoanRepository.findBookLoansDueTodayWithBookTitle(Date.valueOf(date));
    }
    
    @Override
    public List<Map<String, Object>> findOverdueBookLoans(String date) throws EntitySyntaxException {
        if (!toolsService.isValidDateFormat(date)) {
            throw new EntitySyntaxException("INVALID_DATE");
        }
        return bookLoanRepository.findOverdueBookLoans(Date.valueOf(date));
    }

    @Override
    public RevenueResponseDTO findTotalRevenueBetweenDates(String startDate, String endDate) throws EntitySyntaxException {
        checkDates(startDate, endDate);
        
        Map<String, Double> revenueInformation = bookLoanRepository.findTotalRevenueBetweenDates(Date.valueOf(startDate), Date.valueOf(endDate));
        List<BookLoan> bookLoanList = bookLoanRepository.findBookLoansBetweenDates(Date.valueOf(startDate), Date.valueOf(endDate));
        return new RevenueResponseDTO(revenueInformation, bookLoanList);
    }

    @Override
    public RevenueResponseDTO findTotalRevenue() {
        Map<String, Double> revenueInformation = bookLoanRepository.findTotalRevenue();
        List<BookLoan> bookLoanList = bookLoanRepository.findAll();
        return new RevenueResponseDTO(revenueInformation, bookLoanList);
    }
    
    public boolean checkDates(String startDate, String endDate) throws EntitySyntaxException {
        if (!toolsService.isValidDateFormat(startDate)) {
            throw new EntitySyntaxException("INVALID_START_DATE");
        }
        if (!toolsService.isValidDateFormat(endDate)) {
            throw new EntitySyntaxException("INVALID_END_DATE");
        }
        if (LocalDate.parse(startDate).isAfter(LocalDate.parse(endDate))) {
            throw new EntitySyntaxException("START_DATE_AFTER_END_DATE");
        }
        return true;
    }
    
}