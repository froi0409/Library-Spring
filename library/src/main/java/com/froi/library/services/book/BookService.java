package com.froi.library.services.book;

import com.froi.library.dto.book.CreateBookRequestDTO;
import com.froi.library.entities.Book;
import com.froi.library.exceptions.DuplicatedEntityException;
import com.froi.library.exceptions.EntityNotFoundException;
import com.froi.library.exceptions.EntitySyntaxException;
import org.springframework.data.domain.Pageable;


import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface BookService {

    Book createBook(CreateBookRequestDTO newBook) throws DuplicatedEntityException, EntitySyntaxException;
    
    public Optional<Book> getBookByCode(String bookCode);
    
    Book getOneBookByCode(String bookCode) throws EntityNotFoundException;
    
    List<Map<String, Object>> findAll(String searchTerm);
}
