package com.froi.library.services.book;

import com.froi.library.dto.book.CreateBookRequestDTO;
import com.froi.library.entities.Book;
import com.froi.library.exceptions.DuplicatedEntityException;
import com.froi.library.exceptions.EntitySyntaxException;

public interface BookService {

    Book createBook(CreateBookRequestDTO newBook) throws DuplicatedEntityException, EntitySyntaxException;
    
}
