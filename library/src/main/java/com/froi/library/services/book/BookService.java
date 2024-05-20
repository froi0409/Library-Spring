package com.froi.library.services.book;

import com.froi.library.dto.book.CreateBookRequestDTO;
import com.froi.library.entities.Book;
import com.froi.library.exceptions.DuplicatedEntityException;
import com.froi.library.exceptions.EntitySyntaxException;

import java.util.Optional;

public interface BookService {

    Book createBook(CreateBookRequestDTO newBook) throws DuplicatedEntityException, EntitySyntaxException;
    
    public Optional<Book> getBookByCode(String bookCode);
    
    Book getOneBookByCode(String bookCode);
}
