package com.froi.library.dto.book;

import lombok.Value;

@Value
public class ReadBookResponseDTO {
    String code;
    String title;
    String author;
    String publisher;
    Integer loanCount;
}
