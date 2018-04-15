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
    
    private final String GET_BY_PARAMETERS_STRING = 
        "SELECT bk.book_id, bk.title AS 'book', " 
            + "publ.publisher_id AS 'publisher_id', " 
            + "publ.title AS 'publisher', " 
            + "auth.author_id AS 'author_id', " 
            + "auth.name AS 'author' " 
            + "FROM book bk " 
            + "INNER JOIN book_author ba ON bk.book_id = ba.book_id " 
            + "INNER JOIN author auth ON ba.author_id = auth.author_id " 
            + "INNER JOIN publisher publ ON bk.publisher = publ.publisher_id " 
            + "WHERE bk.title = ? AND publ.title = ?;";
    
    private final String INSERT_STRING = 
        "INSERT INTO book(title, publisher) VALUES(?,?);";  
    
    private final String BA_INSERT_STRING = 
        "INSERT INTO book_author(book_id, author_id) "
            + "SELECT * FROM (SELECT ?, ?) AS tmp "
            + "WHERE NOT EXISTS "
            + "(SELECT * FROM book_author WHERE book_id = ? AND author_id = ?);";
    
    private final String FIND_STRING =
        "SELECT * FROM book WHERE title = ? AND publisher = ?"; 
    
    private final String REMOVE_STRING =
        "DELETE bk, ba FROM book bk "
            + "INNER JOIN book_author ba ON bk.book_id = ba.book_id "
            + "INNER JOIN author auth ON ba.author_id = auth.author_id "
            + "INNER JOIN publisher publ ON bk.publisher = publ.publisher_id"
            + "WHERE bk.book_id = ?;";
    
    private final int PARAM_BOOK_TITLE_ID = 0;
    private final int PARAM_BOOK_PUBLISHER_ID = 1;
    private final int PARAM_BOOK_AUTHOR_START_ID = 2;

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
            if(con != null)
                try {
                    con.close();
                } catch (SQLException ex) {}

        }
        return books;
    }
    
    @Override
    public void delete(Book book) throws RepositoryException {
        Connection con = null;
        try {        
            try {
                con = dbPool.getConnection();
                con.setAutoCommit(false);
                this.delete(book, con);
            } finally {
                if(con != null) {
                    con.setAutoCommit(true);
                }
            }
        } catch(SQLException ex) {
            throw new RepositoryException(ex);
        } finally {
            if(con != null)
                try {
                    con.close();
                } catch (SQLException ex) {}

        }
    }   
    
    @Override
    public void save(Book book) throws RepositoryException {                
        Connection con = null;        
        try {        
            try {
                con = dbPool.getConnection();
                con.setAutoCommit(false);                
                this.save(book, con);                
                con.commit();
            } catch (SQLException ex ) {
                if (con != null) {
                    log.debug(classname, "Transaction is being rolled back");
                    con.rollback();
                }
                throw ex;
            } finally {
                if(con != null) {
                    con.setAutoCommit(true);
                }
            }
        } catch(SQLException ex) {
            throw new RepositoryException(ex);
        } finally {
            if(con != null)
                try {
                    con.close();
                } catch (SQLException ex) {}

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
    
    @Override
    public Book get(List<String> strs) throws RepositoryException {
        Connection con = null;
        Book book = null;
        try {        
            try {
                con = dbPool.getConnection();
                con.setAutoCommit(false);
                book = this.get(strs, con);
            } finally {
                if(con != null) {
                    con.setAutoCommit(true);
                }
            }
        } catch(SQLException ex) {
            throw new RepositoryException(ex);
        } finally {
            if(con != null)
                try {
                    con.close();
                } catch (SQLException ex) {}

        }
        return book;
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
    
    public Book get(List<String> strs, Connection con) throws SQLException {
        
        PreparedStatement findStatement = null;
        List<Book> books = new ArrayList<>();
        try {        
            findStatement = con.prepareStatement(GET_BY_PARAMETERS_STRING);
            findStatement.setString(1, strs.get(PARAM_BOOK_TITLE_ID));
            findStatement.setString(2, strs.get(PARAM_BOOK_PUBLISHER_ID));
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
            
            List<String> authors = 
                    strs.subList(PARAM_BOOK_AUTHOR_START_ID, strs.size());
            
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
                    break;
                }
            }            
            
            return book;
        } finally {
            if (findStatement != null)
                findStatement.close();
        }
    }
    
    public void delete(Book book, Connection con) throws SQLException {
        List<String> args = new ArrayList<>();
        args.add(book.getTitle());
        args.add(book.getPublisher().getTitle());
        for(Author author:book.getAuthors()){
            args.add(author.getName());                   
        }                
        Book bookDB = get(args, con);
        if(bookDB != null) {
            PreparedStatement removeStatement = null;
            try {        
                removeStatement = con.prepareStatement(REMOVE_STRING);
                removeStatement.setInt(1, bookDB.getId());
                removeStatement.executeUpdate();
            } finally {
                if (removeStatement != null)
                        removeStatement.close();
            }
        }
    }
    
    public void save(Book book, Connection con) throws SQLException {
        PreparedStatement insertStatement = null;
        try { 
            //at first check if the same is exists
            List<String> args = new ArrayList<>();
            args.add(book.getTitle());
            args.add(book.getPublisher().getTitle());
            for(Author author:book.getAuthors()){
                args.add(author.getName());                   
            }                
            Book bookFromDB = get(args, con);

            if(bookFromDB == null) {
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
                book.setId(bookFromDB.getId());
            }
        } finally {
            if (insertStatement != null)
                    insertStatement.close();
        }
    }

    private void fillBookAuthorTable(Book book, List<Author> authorList, Connection con) 
                                                        throws SQLException {
        PreparedStatement insertBAtableStatement = null;
        try {
            insertBAtableStatement = con.prepareStatement(BA_INSERT_STRING);
            for(Author author:authorList) {
                insertBAtableStatement.setInt(1, book.getId());
                insertBAtableStatement.setInt(2, author.getId());
                insertBAtableStatement.setInt(3, book.getId());
                insertBAtableStatement.setInt(4, author.getId());
                insertBAtableStatement.executeUpdate();
            }
        } finally {
            if(insertBAtableStatement != null)
                    insertBAtableStatement.close();
        }
    }
    
}
