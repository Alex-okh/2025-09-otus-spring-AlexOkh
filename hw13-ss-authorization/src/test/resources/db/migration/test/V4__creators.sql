ALTER TABLE books ADD created_by VARCHAR(255);

UPDATE books
SET created_by = 'editor1';

