package com.froi.library.services.book;

import com.froi.library.dto.book.CreateBookRequestDTO;
import com.froi.library.entities.Book;
import com.froi.library.exceptions.DuplicatedEntityException;
import com.froi.library.exceptions.EntityNotFoundException;
import com.froi.library.exceptions.EntitySyntaxException;
import com.froi.library.repositories.BookRepository;
import com.froi.library.services.tools.ToolsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.Optional;

@Service
public class BookServiceImpl implements BookService {

    private BookRepository bookRepository;
    private ToolsService toolsService;
    
    @Autowired
    public BookServiceImpl(BookRepository bookRepository, ToolsService toolsService) {
        this.bookRepository = bookRepository;
        this.toolsService = toolsService;
    }
    
    
    @Override
    public Book createBook(CreateBookRequestDTO newBook) throws DuplicatedEntityException, EntitySyntaxException {
        Optional<Book> bookToCheck = bookRepository.findById(newBook.getCode());
        if (bookToCheck.isPresent()) {
            throw new DuplicatedEntityException("DUPLICATED_BOOK");
        }
        
        if (!toolsService.isValidISBN(newBook.getCode())) {
            throw new EntitySyntaxException("INVALID_ISBN");
        }
        
        if (!toolsService.isValidDateFormat(newBook.getPublishDate())) {
            throw new EntitySyntaxException("INVALID_DATE");
        }
        
        if (!toolsService.isPositiveInteger(newBook.getStock())) {
            throw new EntitySyntaxException("INVALID_STOCK");
        }
        
        
        Book bookEntity = new Book();
        bookEntity.setCode(newBook.getCode());
        bookEntity.setTitle(newBook.getTitle());
        bookEntity.setAuthor(newBook.getAuthor());
        bookEntity.setPublisher(newBook.getPublisher());
        bookEntity.setCost(Double.parseDouble(newBook.getCost()));
        bookEntity.setPublishDate(Date.valueOf(newBook.getPublishDate()));
        bookEntity.setStock(Integer.parseInt(newBook.getStock()));
        
        bookRepository.save(bookEntity);
        
        return bookEntity;
    }
    
    @Override
    public Optional<Book> getBookByCode(String bookCode) {
        return bookRepository.findById(bookCode);
    }
    
    @Override
    public Book getOneBookByCode(String bookCode) throws EntityNotFoundException {
        return bookRepository.findById(bookCode)
                .orElseThrow(() -> new EntityNotFoundException("BOOK_NOT_FOUND"));
    }
    
}
