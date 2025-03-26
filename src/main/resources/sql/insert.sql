-- Вставляем данные в readers
INSERT INTO reader (full_name, library_card_num, address, phone) VALUES
('Иван Иванов',        'L12345', 'ivan_ivanov@example.com',    '89031234567'),
('Мария Петрова',      'L12346', 'maria_petrova@example.com',  '89039876543'),
('Алексей Смирнов',    'L12347', 'alexey_smirnov@example.com', '89035556677'),
('Екатерина Сидорова', 'L12348', 'katya_sidorova@example.com', '89034445566'),
('Дмитрий Кузнецов',   'L12349', 'dima_kuznetsov@example.com', '89032223344');

-- Вставляем данные в authors
INSERT INTO author (full_name) VALUES
('Лев Толстой'),
('Фёдор Достоевский'),
('Александр Пушкин'),
('Антон Чехов'),
('Михаил Булгаков'),
('Илья Ильф'),
('Евгений Петров');

-- Вставляем данные в books
INSERT INTO book (title, publisher, year, isbn) VALUES
('Анна Каренина',      'Издательство 1', 1877, '9781234567897'),
('Братья Карамазовы',  'Издательство 2', 1880, '9789876543210'),
('Капитанская дочка',  'Издательство 3', 1836, '9781112223334'),
('Три сестры',         'Издательство 4', 1901, '9785556667778'),
('Белая гвардия',      'Издательство 5', 1925, '9789998887776'),
('Двенадцать стульев', 'Издательство 6', 1928, '9781122334455'),
('Золотой телёнок',    'Издательство 6', 1931, '9785566778899');

-- Вставляем данные в book_authors (книги с несколькими авторами)
INSERT INTO book_author (book_id, author_id) VALUES
(1, 1), -- Анна Каренина - Лев Толстой
(2, 2), -- Братья Карамазовы - Фёдор Достоевский
(3, 3), -- Капитанская дочка - Александр Пушкин
(4, 4), -- Три сестры - Антон Чехов
(5, 5), -- Белая гвардия - Михаил Булгаков
(6, 6), (6, 7), -- Двенадцать стульев - Илья Ильф и Евгений Петров
(7, 6), (7, 7); -- Золотой телёнок - Илья Ильф и Евгений Петров

-- Вставляем данные в book_copies (экземпляры книг)
INSERT INTO book_copy (book_id, status) VALUES
(1, 'available'),
(1, 'borrowed'),
(2, 'available'),
(2, 'available'),
(3, 'borrowed'),
(4, 'available'),
(5, 'borrowed'),
(6, 'available'),
(6, 'borrowed'),
(7, 'available');

-- Вставляем данные в borrowings (кто какие книги взял)
INSERT INTO borrowing (reader_id, book_copy_id, borrow_date, return_date) VALUES
(2, 5, '2025-02-25', '2025-03-10'), -- Мария Петрова взяла "Капитанская дочка" и вернула
(4, 3, '2025-02-28', '2025-03-08'), -- Екатерина Сидорова взяла "Братья Карамазовы" и вернула
(1, 2, '2025-03-01', NULL), -- Иван Иванов взял 2-й экземпляр "Анна Каренина"
(3, 7, '2025-03-03', NULL), -- Алексей Смирнов взял "Белая гвардия"
(5, 9, '2025-03-05', NULL); -- Дмитрий Кузнецов взял "Двенадцать стульев"
