package com.froi.library.dto.book;

import lombok.Value;

import java.sql.Date;

@Value
public class CreateBookRequestDTO {
    String code;
    String author;
    String publisher;
    String title;
    String publishDate;
    String cost;
    String stock;
}
