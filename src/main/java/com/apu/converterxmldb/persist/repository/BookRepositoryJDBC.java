/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.converterxmldb.persist.repository;

import com.apu.converterxmldb.entity.Author;
import com.apu.converterxmldb.exception.RepositoryException;
import com.apu.converterxmldb.entity.Book;
import com.apu.converterxmldb.entity.Publisher;
import com.apu.converterxmldb.persist.JDBC.JDBCPool;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author apu
 */
@Log
public class BookRepositoryJDBC  extends AbstractRepository<Book,Integer> {
    
    private final AuthorRepositoryJDBC authorRepository;
    private final PublisherRepositoryJDBC publisherRepository;

    public BookRepositoryJDBC() {
        this.authorRepository = new AuthorRepositoryJDBC();
        this.publisherRepository = new PublisherRepositoryJDBC();       
    }

    private final int GET_QUERY_BOOK_TITLE_ID = 2;
    private final int GET_QUERY_PUBLISHER_TITLE_ID = 4;
    private final int GET_QUERY_AUTHOR_NAME_ID = 6;
    
    private final String GET_ALL_STRING =
        "SELECT book.book_id, "
            + "book.title, "
            + "publ.publisher_id, "
            + "publ.title, "
            + "auth.author_id, "
            + "auth.name "
            + "FROM book "
            + "INNER JOIN book_author ba ON book.book_id = ba.book_id "
            + "INNER JOIN author auth ON ba.author_id = auth.author_id "
            + "INNER JOIN publisher publ ON book.publisher = publ.publisher_id;";
    
    private final String GET_BY_PARAMETERS_STRING = 
        "SELECT book.book_id, "
            + "book.title, "
            + "publ.publisher_id, "
            + "publ.title, "
            + "auth.author_id, "
            + "auth.name "
            + "FROM book "
            + "INNER JOIN book_author ba ON book.book_id = ba.book_id "
            + "INNER JOIN author auth ON ba.author_id = auth.author_id " 
            + "INNER JOIN publisher publ ON book.publisher = publ.publisher_id "
            + "WHERE book.title = ? AND publ.title = ?;";
    
    private final String INSERT_STRING = 
        "INSERT INTO book(title, publisher) VALUES(?,?);";  
    
    private final String BA_INSERT_STRING = 
        "INSERT INTO book_author(book_id, author_id) VALUES(?,?);";

    private final String BA_GET_BY_PARAMETERS_STRING =
            "SELECT * FROM book_author WHERE book_id = ? AND author_id = ?";

    private final String FIND_STRING =
        "SELECT * FROM book WHERE title = ? AND publisher = ?"; 
    
    private final String REMOVE_STRING =
        "DELETE bk, ba FROM book bk "
            + "INNER JOIN book_author ba ON bk.book_id = ba.book_id "
            + "INNER JOIN author auth ON ba.author_id = auth.author_id "
            + "INNER JOIN publisher publ ON bk.publisher = publ.publisher_id"
            + "WHERE bk.book_id = ?;";

    @Override
    public List<Book> getAll(Connection con) throws SQLException {
        PreparedStatement findStatement = null;
        List<Book> books = new ArrayList<>();
        try {        
            findStatement = con.prepareStatement(GET_ALL_STRING);
            ResultSet rs = findStatement.executeQuery();
            Book book = null;
            while(rs.next()) {
                Author author = 
                        new Author(rs.getString(GET_QUERY_AUTHOR_NAME_ID));
                author.setId(rs.getInt("author_id"));

                Publisher publisher = 
                        new Publisher(rs.getString(GET_QUERY_PUBLISHER_TITLE_ID));
                publisher.setId(rs.getInt("publisher_id"));

                int bookId = rs.getInt("book_id");
                if((book != null) &&(book.getId() == bookId)) {
                    book.addAuthor(author);
                } else {
                    book = new Book();
                    book.setId(bookId);
                    book.setTitle(rs.getString(GET_QUERY_BOOK_TITLE_ID));
                    book.setPublisher(publisher);
                    book.addAuthor(author);
                    books.add(book);
                }                   
            }
            return books;
        } finally {
            if (findStatement != null)
                findStatement.close();
        }
    }

    @Override
    public Integer get(Book book4Search, Connection con) throws SQLException {
        
        PreparedStatement findStatement = null;
        List<Book> books = new ArrayList<>();
        try {        
            findStatement = con.prepareStatement(GET_BY_PARAMETERS_STRING);
            findStatement.setString(1, book4Search.getTitle());
            findStatement.setString(2, book4Search.getPublisher().getTitle());
            ResultSet rs = findStatement.executeQuery();
            Book book = null;
            while(rs.next()) {
                Author author =
                        new Author(rs.getString(GET_QUERY_AUTHOR_NAME_ID));
                author.setId(rs.getInt("author_id"));

                Publisher publisher =
                        new Publisher(rs.getString(GET_QUERY_PUBLISHER_TITLE_ID));
                publisher.setId(rs.getInt("publisher_id"));

                int bookId = rs.getInt("book_id");
                if((book != null) &&(book.getId() == bookId)) {
                    book.addAuthor(author);
                } else {
                    book = new Book();
                    book.setId(bookId);
                    book.setTitle(rs.getString(GET_QUERY_BOOK_TITLE_ID));
                    book.setPublisher(publisher);
                    book.addAuthor(author);
                    books.add(book);
                }                   
            }
            
            List<String> authors = new ArrayList<>();
            for(Author author:book4Search.getAuthors()) {
                authors.add(author.getName());
            }
            
            book = null;
            for(Book bookTmp: books) {
                if(bookTmp.getAuthors().size() != authors.size())
                    continue;
                List<String> authorsTmp = new ArrayList<>();
                for(Author aut:bookTmp.getAuthors()) {
                    authorsTmp.add(aut.getName());
                }
                if(authorsTmp.containsAll(authors)) {
                    book = bookTmp;
                    book4Search.setId(bookTmp.getId());
                    return bookTmp.getId();
                }
            }            
            
            return null;
        } finally {
            if (findStatement != null)
                findStatement.close();
        }
    }

    @Override
    public void delete(Book book, Connection con) throws SQLException {
        Integer id = get(book, con);
        if(id != null) {
            PreparedStatement removeStatement = null;
            try {        
                removeStatement = con.prepareStatement(REMOVE_STRING);
                removeStatement.setInt(1, id);
                removeStatement.executeUpdate();
            } finally {
                if (removeStatement != null)
                        removeStatement.close();
            }
        }
    }

    @Override
    public void save(Book book, Connection con) throws SQLException {
        PreparedStatement insertStatement = null;
        try { 
            //at first check if the same is exists
            Integer id = get(book, con);

            if(id == null) {
                //persist publisher or get id
                Publisher publisher = book.getPublisher();
                publisherRepository.save(publisher, con);

                //persist authors or get ids
                List<Author> authorList = book.getAuthors();
                for(Author author:authorList) {
                    authorRepository.save(author, con);
                }

                insertStatement = con.prepareStatement(INSERT_STRING, 
                                            Statement.RETURN_GENERATED_KEYS);
                insertStatement.setString(1, book.getTitle());
                insertStatement.setInt(2, publisher.getId());
                insertStatement.executeUpdate();
                try (ResultSet keys = insertStatement.getGeneratedKeys()) {
                    if(keys.next()) {                        
                        Integer key = keys.getInt(1);
                        book.setId(key);
                    }
                }

                //insert data to table book_author
                if(book.getId() != null) {  
                    fillBookAuthorTable(book, authorList, con);
                }
            } else {
                book.setId(id);
            }
        } finally {
            if (insertStatement != null)
                    insertStatement.close();
        }
    }

    private void fillBookAuthorTable(Book book, List<Author> authorList, Connection con) 
                                                        throws SQLException {
        PreparedStatement findStatement = null;
        PreparedStatement insertBAtableStatement = null;
        try {
            findStatement = con.prepareStatement(BA_GET_BY_PARAMETERS_STRING);

            insertBAtableStatement = con.prepareStatement(BA_INSERT_STRING);
            for(Author author:authorList) {
                findStatement.setInt(1, book.getId());
                findStatement.setInt(2, author.getId());
                ResultSet rs = findStatement.executeQuery();
                if(rs.next())
                    continue;
                insertBAtableStatement.setInt(1, book.getId());
                insertBAtableStatement.setInt(2, author.getId());
                insertBAtableStatement.executeUpdate();
            }
        } finally {
            if(insertBAtableStatement != null)
                    insertBAtableStatement.close();
        }
    }

}
