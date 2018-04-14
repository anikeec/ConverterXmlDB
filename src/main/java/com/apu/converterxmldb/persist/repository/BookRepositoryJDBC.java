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
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author apu
 */
public class BookRepositoryJDBC implements BookRepository {
    
    private static JDBCPool dbPool = JDBCPool.getInstance();
    private static final Log log = Log.getInstance();
    private static final Class classname = AuthorRepositoryJDBC.class;
    
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
        "INSERT INTO author(author_id, name) "
            + "SELECT * FROM "
            + "(SELECT COALESCE((SELECT (MAX(author_id)+1) FROM author auth),0), ?) "
            + "AS tmp WHERE NOT EXISTS (SELECT * FROM author WHERE name = ?);";    
    
    private final String FIND_BY_NAME_STRING =
        "SELECT * FROM book WHERE name = ?"; 
    
    private final String REMOVE_STRING =
        "DELETE FROM book WHERE book_id = ?";

    @Override
    public List<Book> getBooks() throws RepositoryException {
        Connection con = null;
        PreparedStatement findStatement = null;
        List<Book> books = new ArrayList<>();
        try {        
            try {
                con = dbPool.getConnection();
                con.setAutoCommit(false);
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
                        book.setTitle(rs.getString("name"));
                        book.setPublisher(publisher);
                        book.addAuthor(author);
                        books.add(book);
                    }                   
                }
            } finally {
                if (findStatement != null) {
                    findStatement.close();
                }
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
    public void deleteBook(Book book) throws RepositoryException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//        if(book == null)
//            throw new IllegalArgumentException("book is NULL");
//        Connection con = null;
//        PreparedStatement removeStatement = null;
//        try {        
//            try {
//                con = dbPool.getConnection();
//                con.setAutoCommit(false);
//                removeStatement = con.prepareStatement(REMOVE_STRING);
//                removeStatement.setInt(1, id);
//                removeStatement.executeUpdate();
//                con.commit();
//            } catch (SQLException ex ) {
//                if (con != null) {
//                    log.debug(classname, "Transaction is being rolled back");
//                    con.rollback();
//                }
//                throw ex;
//            } finally {
//                if (removeStatement != null)
//                    removeStatement.close();
//                if(con != null)
//                    con.setAutoCommit(true);
//            }
//        } catch(SQLException ex) {
//            throw new RepositoryException(ex);
//        } finally {
//            dbPool.putConnection(con);
//        }
    }

    @Override
    public void saveBook(Book book) throws RepositoryException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//        Connection con = null;
//        PreparedStatement insertStatement = null;
//        try {        
//            try {
//                con = dbPool.getConnection();
//                con.setAutoCommit(false);
//                insertStatement = con.prepareStatement(INSERT_STRING);
//                insertStatement.setString(1, book.getName());
//                insertStatement.setString(2, book.getName());
//                insertStatement.executeUpdate();
//                con.commit();
//            } catch (SQLException ex ) {
//                if (con != null) {
//                    log.debug(classname, "Transaction is being rolled back");
//                    con.rollback();
//                }
//                throw ex;
//            } finally {
//                if (insertStatement != null) {
//                    insertStatement.close();
//                }
//                if(con != null) {
//                    con.setAutoCommit(true);
//                }
//            }
//        } catch(SQLException ex) {
//            throw new RepositoryException(ex);
//        } finally {
//            dbPool.putConnection(con);
//        }
    }
    
}
