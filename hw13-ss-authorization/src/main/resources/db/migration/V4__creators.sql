ALTER TABLE books ADD created_by VARCHAR(255);

UPDATE books
SET created_by = 'editor1'
WHERE id % 2 = 0;

UPDATE books
SET created_by = 'editor2'
WHERE id % 2 != 0;