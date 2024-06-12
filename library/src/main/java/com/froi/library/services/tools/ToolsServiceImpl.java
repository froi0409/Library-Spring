package com.froi.library.services.tools;

import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ToolsServiceImpl implements ToolsService{
    
    @Override
    public boolean isValidISBN(String isbnStr) {
        // Delete possibly hyphen or space
        isbnStr = isbnStr.replaceAll("[\\s-]", "");
        char[] isbn = isbnStr.toCharArray();
        
        int sum = 0;
        if(isbn.length == 10) {
            for(int i = 0; i < 9; i++) {
                int digit = Character.getNumericValue(isbn[i]);
                sum += (i + 1) * digit; // Multiplicar el dígito por su posición (1-indexed)
            }
            
            if(Character.getNumericValue(isbn[9]) == sum % 11) return true;
        } else if(isbn.length == 13) {
            
            for(int i = 0; i < 12; i++) {
                int digit = Character.getNumericValue(isbn[i]);
                if(i % 2 == 0) {
                    sum += digit;
                } else {
                    sum += digit * 3;
                }
            }
            
            if(Character.getNumericValue(isbn[12]) == 10 - (sum % 10)) return true;
        }
        
        return false;
    }
    
    
    
    
    @Override
    public boolean isValidDateFormat(String date) {
        try {
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            
            dateFormatter.parse(date);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }
    
    @Override
    public boolean isValidDouble(Double number) {
        if (number == null || number <= 0) {
            return false;
        }
        String[] parts = number.toString().split("\\.");
        return parts.length <= 2 && parts[1].length() <= 2;
    }
    
    @Override
    public boolean isMoney(String numberString) {
        try {
            double number = Double.parseDouble(numberString);
            if (number < 0) {
                return false;
            }
            String[] parts = numberString.split("\\.");
            return parts.length <= 2 && parts[1].length() <= 2;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    
    @Override
    public boolean isPositiveInteger(String numberString) {
        try {
            int number = Integer.parseInt(numberString);
            return number > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    
    @Override
    public boolean isAlphabetic(String str) {
        if (str == null || str.isBlank()) {
            return false;
        }
        return str.matches("[a-zA-ZáéíóúÁÉÍÓÚüÜñÑ\\s]+");
    }
    
    @Override
    public boolean isValidEmail(String email) {
        String emailPattern = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        Pattern pattern = Pattern.compile(emailPattern);
        Matcher matcher = pattern.matcher(email);
        
        return matcher.matches();
    }
}
