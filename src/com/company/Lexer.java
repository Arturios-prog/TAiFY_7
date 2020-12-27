package com.company;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Lexer
{
    private final String str;
    private int index = 0;

    private final HashTable hashTable = new HashTable();
    {
        hashTable.Insert(new Token(TokenType.BEGIN, "for ("));
        hashTable.Insert(new Token(TokenType.BEGIN, "for("));
        hashTable.Insert(new Token(TokenType.DO, "do"));
        hashTable.Insert(new Token(TokenType.MORE_OR_EQUAL, ">="));
        hashTable.Insert(new Token(TokenType.LESS_OR_EQUAL, "<="));
        hashTable.Insert(new Token(TokenType.NOT_EQUAL, "!="));
        hashTable.Insert(new Token(TokenType.EQUAL,"=="));
        hashTable.Insert(new Token(TokenType.ASSIGN, ":="));
        hashTable.Insert(new Token(TokenType.MORE, ">"));
        hashTable.Insert(new Token(TokenType.LESS, "<"));
        hashTable.Insert(new Token(TokenType.ADD, "+"));
        hashTable.Insert(new Token(TokenType.SUB, "-"));
        hashTable.Insert(new Token(TokenType.MUL, "*"));
        hashTable.Insert(new Token(TokenType.DIV, "/"));
        hashTable.Insert(new Token(TokenType.EQUAL, "="));
        hashTable.Insert(new Token(TokenType.LPAR, "("));
        hashTable.Insert(new Token(TokenType.RPAR, ")"));
        hashTable.Insert(new Token(TokenType.SM, ";"));
        hashTable.Insert(new Token(TokenType.QUO,"\""));
        hashTable.Insert(new Token(TokenType.INCREMENT,"++ "));
        hashTable.Insert(new Token(TokenType.DECR,"--"));
        hashTable.Insert(new Token(TokenType.SELF_ADD,"+="));
        hashTable.Insert(new Token(TokenType.SELF_SUB,"-="));
    }

    public Lexer(String str)
    {
        this.str = str;
    }

    private int match(Pattern pattern)
    {
        Matcher matcher = pattern.matcher(str);
        matcher.region(index, str.length());

        if (matcher.lookingAt())
            return matcher.end();
        return -1;
    }

    private Token matchSixteen(){
        Pattern varPattern = Pattern.compile("[0-9][0-9a-fA-F]+");
        int matched = match(varPattern);

        if (matched < 0) {
            return null;
        }

        String sixteenText = str.substring(index, matched);
        Token temp = new Token(TokenType.SIXTEEN, sixteenText, matched);
        hashTable.Insert(temp);
        return temp;
    }

    /*private Token matchNumber()
    {
        Pattern numberPattern = Pattern.compile("[0-9]+");
        int matched = match(numberPattern);
        if (matched < 0)
            return null;
        String numberText = str.substring(index, matched);

        Token temp = new Token(TokenType.NUMBER, numberText, matched);
        hashTable.Insert(temp);
        return temp;
    }*/

    private Token matchAnySymbol()
    {
        for (Token entry : hashTable.entrySet())
        {
            String key = entry.symbolText;
            TokenType value = entry.type;
            Pattern symbolPattern = Pattern.compile(Pattern.quote(key));
            int matched = match(symbolPattern);
            if (matched < 0)
                continue;
            String symbolText = str.substring(index, matched);
            return new Token(value, symbolText, matched);
        }
        return null;
    }

    private Token matchSpaces()
    {
        int i = index;
        while (i < str.length())
        {
            char ch = str.charAt(i);
            if (ch <= ' ')
                i++;
            else
                break;
        }
        if (i > index)
        {
            String spaces = str.substring(index, i);
            return new Token(TokenType.SPACES, spaces, i);
        }
        else
            return null;
    }

    private Token matchVariable()
    {
        Pattern varPattern = Pattern.compile("[A-Za-z]+[\\w]*");
        int matched = match(varPattern);
        if (matched < 0)
            return null;
        String varText = str.substring(index, matched);

        Token temp = new Token(TokenType.VAR, varText, matched);
        hashTable.Insert(temp);
        return temp;
    }

    private Token matchAnyToken() throws ParseException
    {
        if (index >= str.length())
            return null;

        Token spacesToken = matchSpaces();
        if (spacesToken != null)
            return spacesToken;

        /*Token NumberToken = matchNumber();
        if (NumberToken != null)
            return NumberToken;*/

        Token sixteenToken = matchSixteen();
        if (sixteenToken != null)
            return sixteenToken;


        Token symbolToken = matchAnySymbol();
        if (symbolToken != null)
            return symbolToken;

        Token varToken = matchVariable();
        if (varToken != null)
            return varToken;

        throw new ParseException("Unexpected character '" + str.charAt(index) + "'", index);
    }

    public Token nextToken() throws ParseException
    {
        while (true)
        {
            Token token = matchAnyToken();
            if (token == null)
                return null;

            index = token.to;
            if (token.type != TokenType.SPACES)
                return token;
        }
    }

    public List<Token> getAllTokens() throws ParseException
    {
        List<Token> allTokens = new ArrayList<>();
        while (true)
        {
            Token token = nextToken();
            if (token == null)
                break;
            allTokens.add(token);
        }
        return allTokens;
    }
}

