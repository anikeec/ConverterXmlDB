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
import com.apu.converterxmldb.utils.Log;
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
public class BookRepositoryJDBC implements Repository<Book> {
    
    private static JDBCPool dbPool = JDBCPool.getInstance();
    private static final Log log = Log.getInstance();
    private static final Class classname = AuthorRepositoryJDBC.class;
    
    private final AuthorRepositoryJDBC authorRepository;
    private final PublisherRepositoryJDBC publisherRepository;

    public BookRepositoryJDBC() {
        this.authorRepository = new AuthorRepositoryJDBC();
        this.publisherRepository = new PublisherRepositoryJDBC();       
    }
    
    private final String GET_ALL_STRING =
        "SELECT book.book_id AS 'book_id', "
            + "book.title AS 'book', "
            + "publ.publisher_id AS 'publisher_id', "
            + "publ.title AS 'publisher', "
            + "auth.author_id AS 'author_id', "
            + "auth.name AS 'author' "
            + "FROM book "
            + "INNER JOIN book_author ba ON book.book_id = ba.book_id "
            + "INNER JOIN author auth ON ba.author_id = auth.author_id "
            + "INNER JOIN publisher publ ON book.publisher = publ.publisher_id;"; 
    
    private final String INSERT_STRING = 
        "INSERT INTO book(title, publisher) SELECT * FROM (SELECT ?,?) AS tmp "
            + "WHERE NOT EXISTS (SELECT * FROM book WHERE title = ? AND "
            + "publisher = ?);";  
    
    private final String BA_INSERT_STRING = 
        "INSERT INTO book_author(book_id, author_id) "
            + "SELECT * FROM (SELECT ?, ?) AS tmp "
            + "WHERE NOT EXISTS "
            + "(SELECT * FROM book_author WHERE book_id = ? AND author_id = ?);";
    
    private final String FIND_STRING =
        "SELECT * FROM book WHERE title = ? AND publisher = ?"; 
    
    private final String REMOVE_STRING =
        "DELETE FROM book WHERE book_id = ?";

    /**
     *
     * @return
     * @throws RepositoryException
     */
    @Override
    public List<Book> getAll() throws RepositoryException {
        Connection con = null;
        List<Book> books = new ArrayList<>();
        try {        
            try {
                con = dbPool.getConnection();
                con.setAutoCommit(false);
                books = this.getAll(con);
            } finally {
                if(con != null) {
                    con.setAutoCommit(true);
                }
            }
        } catch(SQLException ex) {
            throw new RepositoryException(ex);
        } finally {
            dbPool.putConnection(con);
        }
        return books;
    }
    
    @Override
    public void delete(Book book) throws RepositoryException {   

        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void save(Book book) throws RepositoryException {
                
        Connection con = null;
        PreparedStatement insertStatement = null;
        PreparedStatement insertBAtableStatement = null;
        try {        
            try {
                con = dbPool.getConnection();
                con.setAutoCommit(false);
                
                //at first check if the same is exists
                
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
                insertStatement.setString(3, book.getTitle());
                insertStatement.setInt(4, publisher.getId());
                insertStatement.executeUpdate();
                try (ResultSet keys = insertStatement.getGeneratedKeys()) {
                    if(keys.next()) {                        
                        Integer key = keys.getInt(1);
                        book.setId(key);
                    }
                }
                
                if(book.getId() != null) {                    
                    insertBAtableStatement = con.prepareStatement(BA_INSERT_STRING);
                    for(Author author:authorList) {
                        insertBAtableStatement.setInt(1, book.getId());
                        insertBAtableStatement.setInt(2, author.getId());
                        insertBAtableStatement.setInt(3, book.getId());
                        insertBAtableStatement.setInt(4, author.getId());
                        insertBAtableStatement.executeUpdate();
                    }
                    
                }
                
                con.commit();

            } catch (SQLException ex ) {
                if (con != null) {
                    log.debug(classname, "Transaction is being rolled back");
                    con.rollback();
                }
                throw ex;
            } finally {
                if(insertStatement != null)
                    insertStatement.close();
                if(insertBAtableStatement != null)
                    insertBAtableStatement.close();
                if(con != null) {
                    con.setAutoCommit(true);
                }
            }
        } catch(SQLException ex) {
            throw new RepositoryException(ex);
        } finally {
            dbPool.putConnection(con);
        }
    }

    @Override
    public Book get() throws RepositoryException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    @Override
    public Book get(String str) throws RepositoryException {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public void delete(String str) throws RepositoryException {
        throw new UnsupportedOperationException("Not supported yet."); 
    } 
    
    public List<Book> getAll(Connection con) throws SQLException {
        PreparedStatement findStatement = null;
        List<Book> books = new ArrayList<>();
        try {        
            findStatement = con.prepareStatement(GET_ALL_STRING);
            ResultSet rs = findStatement.executeQuery();
            Book book = null;
            while(rs.next()) {
                Author author = 
                        new Author(rs.getInt("author_id"), 
                                    rs.getString("author"));
                Publisher publisher = 
                        new Publisher(rs.getInt("publisher_id"),
                                    rs.getString("publisher"));

                int bookId = rs.getInt("book_id");
                if((book != null) &&(book.getId() == bookId)) {
                    book.addAuthor(author);
                } else {
                    book = new Book();
                    book.setId(bookId);
                    book.setTitle(rs.getString("book"));
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
    
}
