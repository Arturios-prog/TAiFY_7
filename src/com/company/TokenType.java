package com.company;

public enum TokenType
{
    //Шестнадцатиричное число
    SIXTEEN,
    // Пробелы
    SPACES,
    //Шестнадцатеричное число только из цифры
    SIXTEEN_NUMBER,
    // Символ '+'
    ADD,
    // Символ '-'
    SUB,
    // Символ '*'
    MUL,
    //Символ '/'
    DIV,
    // Символ '('
    LPAR,
    // Символ ')'
    RPAR,
    // Символ '+='
    SELF_ADD,
    // Символ '-='
    SELF_SUB,
    // Переменная
    VAR,
    // Приравнивание
    ASSIGN,
    // Больше
    MORE,
    // Меньше
    LESS,
    // Равно
    EQUAL,
    // Больше или равно
    MORE_OR_EQUAL,
    // Меньше или равно
    LESS_OR_EQUAL,
    // НЕ равно
    NOT_EQUAL,
    //Символ ';'
    SM,
    //двойные кавычки
    QUO,
    //for для обозначения начала
    BEGIN,
    //do
    DO,
    //Инкремент
    INCREMENT,
    //Декремент
    DECR
}
