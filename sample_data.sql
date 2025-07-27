USE library_db;

-- Insert sample users
INSERT INTO USER (id, name, role) VALUES
  ('u1', 'Alice Johnson', 'regular'),
  ('u2', 'Bob Smith', 'admin'),
  ('u3', 'Carol White', 'regular'),
  ('u4', 'David Brown', 'regular');

-- Insert sample books
INSERT INTO BOOK (id, title, author, genre, available_copies) VALUES
  ('b1', '1984', 'George Orwell', 'Dystopian', 3),
  ('b2', 'To Kill a Mockingbird', 'Harper Lee', 'Fiction', 2),
  ('b3', 'A Brief History of Time', 'Stephen Hawking', 'Science', 1),
  ('b4', 'The Great Gatsby', 'F. Scott Fitzgerald', 'Classic', 4),
  ('b5', 'The Catcher in the Rye', 'J.D. Salinger', 'Classic', 0);

-- Insert borrow records
INSERT INTO BORROWS (user_id, book_id) VALUES
  ('u1', 'b1'),
  ('u1', 'b2'),
  ('u3', 'b3'),
  ('u4', 'b4');
