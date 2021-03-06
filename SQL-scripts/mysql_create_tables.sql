use librarydb;

CREATE TABLE AUTHOR(
 author_id  INT NOT NULL AUTO_INCREMENT,
 name VARCHAR(20) DEFAULT '',
 PRIMARY KEY (author_id) 
);

CREATE TABLE PUBLISHER(
 publisher_id  INT NOT NULL AUTO_INCREMENT,
 title VARCHAR(20) DEFAULT '',
 PRIMARY KEY (publisher_id) 
);

CREATE TABLE BOOK(
 book_id  INT NOT NULL AUTO_INCREMENT,
 title VARCHAR(30) DEFAULT '',
 publisher INT, 
 PRIMARY KEY (book_id),
 FOREIGN KEY (publisher) REFERENCES PUBLISHER (publisher_id)
);

CREATE TABLE BOOK_AUTHOR(
 id  INT NOT NULL AUTO_INCREMENT,
 book_id  INT NOT NULL,
 author_id  INT NOT NULL,
 PRIMARY KEY (id),
 FOREIGN KEY (book_id) REFERENCES BOOK (book_id),
 FOREIGN KEY (author_id) REFERENCES AUTHOR (author_id)
);














