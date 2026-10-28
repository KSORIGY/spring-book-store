INSERT INTO categories (id, name, description, is_deleted) VALUES (1, 'Horror', 'Scary books', false);
INSERT INTO books (id, title, author, isbn, price, description, cover_image, is_deleted)
VALUES (1, 'Test Book', 'Test Author', '1234567890', 19.99, 'Test descr', 'cover.jpg', false);
INSERT INTO books_categories (book_id, category_id) VALUES (1, 1);