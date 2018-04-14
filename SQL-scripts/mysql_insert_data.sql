INSERT INTO publisher(title) VALUES('Kyiv');
INSERT INTO publisher(title) VALUES('Dnipro');

INSERT INTO author(name) VALUES('Horstmann');
INSERT INTO author(name) VALUES('Geri');
INSERT INTO author(name) VALUES('Ekkel');

INSERT INTO book(title, publisher) VALUES('Java2', (SELECT publisher_id FROM publisher AS pub WHERE pub.title = 'Dnipro'));
INSERT INTO book_author(book_id, author_id) VALUES(1, (SELECT author_id FROM author AS aut WHERE aut.name = 'Horstmann'));
INSERT INTO book_author(book_id, author_id) VALUES(1, (SELECT author_id FROM author AS aut WHERE aut.name = 'Geri'));

INSERT INTO book(title, publisher) VALUES('Philosophy of Java', (SELECT publisher_id FROM publisher AS pub WHERE pub.title = 'Kyiv'));
INSERT INTO book_author(book_id, author_id) VALUES(2, (SELECT author_id FROM author AS aut WHERE aut.name = 'Ekkel'));