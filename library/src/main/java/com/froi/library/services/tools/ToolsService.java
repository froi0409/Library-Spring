package com.froi.library.services.tools;

public interface ToolsService {
    boolean isValidISBN(String isbn);
    
    boolean isValidDateFormat(String date);
    
    boolean isValidDouble(Double number);
    
    boolean isMoney(String numberString);
    
    boolean isPositiveInteger(String numberString);
    
    boolean isAlphabetic(String str);
}
