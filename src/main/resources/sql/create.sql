-- Таблица для хранения информации о читателях
DROP TABLE IF EXISTS reader CASCADE;
CREATE TABLE reader (
    id               SERIAL PRIMARY KEY,
    full_name        VARCHAR(255) NOT NULL,
    library_card_num VARCHAR(50) UNIQUE NOT NULL,
    address          TEXT,
    phone            VARCHAR(20)
);

-- Таблица для хранениея информации об авторах
DROP TABLE IF EXISTS author CASCADE;
CREATE TABLE author (
    id        SERIAL PRIMARY KEY,
    full_name VARCHAR(255) NOT NULL
);

-- Таблица для хранения информации о различных книгах
DROP TABLE IF EXISTS book CASCADE;
CREATE TABLE book (
    id        SERIAL PRIMARY KEY,
    title     VARCHAR(255) NOT NULL,
    publisher VARCHAR(255),
    year      INT CHECK (year > 0),
    isbn      VARCHAR(13) UNIQUE NOT NULL
);

-- Таблица для реализации связи многие-со-многими между
-- книгами и их автормаи
DROP TABLE IF EXISTS book_author CASCADE;
CREATE TABLE book_author (
    id        SERIAL PRIMARY KEY,
    book_id   INT NOT NULL,
    author_id INT NOT NULL,
    
	FOREIGN KEY (book_id) REFERENCES book(id) ON DELETE CASCADE,
    FOREIGN KEY (author_id) REFERENCES author(id) ON DELETE CASCADE
);

-- Тип данных, отвечающий за доступность экземпляров книги
-- (в PostgreSQL нет CREATE TYPE IF NOT EXISTS)
DO $$ 
BEGIN 
    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'book_status') THEN
        CREATE TYPE book_status AS ENUM ('available', 'borrowed');
    END IF;
END $$;

-- Таблица для хранения информации о экземплярах книг
DROP TABLE IF EXISTS book_copy CASCADE;
CREATE TABLE book_copy (
    id      SERIAL PRIMARY KEY,
    book_id INT NOT NULL,
    status  book_status NOT NULL,
    
	FOREIGN KEY (book_id) REFERENCES book(id) ON DELETE CASCADE
);

-- Таблица для хранения информации о истории выдачи книг читателям
DROP TABLE IF EXISTS borrowing CASCADE;
CREATE TABLE borrowing (
    id           SERIAL PRIMARY KEY,
    reader_id    INT NOT NULL,
    book_copy_id INT NOT NULL,
    borrow_date  DATE NOT NULL,
    return_date  DATE,
    
	FOREIGN KEY (reader_id) REFERENCES reader(id) ON DELETE CASCADE,
    FOREIGN KEY (book_copy_id) REFERENCES book_copy(id) ON DELETE CASCADE
);