# Страница регистрации
Перейти на `http://localhost:2025/r`

## Страница первичной оценки состояния
Перейти на `http://localhost:2025/s`

## Страница всех тестов
Перейти на `http://localhost:2025/t`

### Тесты на внимание
- `http://localhost:2025/t/a/1?s=0`

### Тесты на память
- `http://localhost:2025/t/m/1?s=0`

### Тесты на реакцию
- `http://localhost:2025/t/r/1?s=0`

### Тесты на обработку информации
- `http://localhost:2025/t/p/1?s=0`



### Тесты
1) Тест Струпа (внимание)
2) Мемтракс (память)
3) Статичный тест реакции
4) Обработка информации визуальное 
5) Обработка информации звуковое
6) Таблица Шульте (внимание)
7) Заучивание 10 слов (память)
8) Динамичный (круговой) тест реакции



```ALTER TABLE person ADD COLUMN fio_encrypted BYTEA```
```
UPDATE person
SET birthday_encrypted = pgp_sym_encrypt(birthday::text, 'diplom');

UPDATE person
SET fio_encrypted = pgp_sym_encrypt(fio, 'diplom');

UPDATE person
SET fio_initials_encrypted = pgp_sym_encrypt(fio_initials, 'diplom');
```

```
SELECT pgp_sym_decrypt(birthday_encrypted, 'diplom')::date
FROM person;
```