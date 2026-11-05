INSERT INTO users (id, email, password, first_name, last_name, is_deleted)
VALUES (100, 'testuser@gmail.com', 'password', 'Test', 'User', false);

INSERT INTO shopping_carts (user_id, is_deleted)
VALUES (100, false);

INSERT INTO books (id, title, author, isbn, price, is_deleted)
VALUES (100, 'Test Cart Book', 'Test Author', '12345678890123', 10.00, false);

INSERT INTO cart_items (id, shopping_cart_id, book_id, quantity)
VALUES (100, 100, 100, 10);
