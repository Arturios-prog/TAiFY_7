package com.company;

import java.text.ParseException;
import java.util.List;
import java.util.Scanner;

public class Parser1
{
    private final List<Token> tokens;
    private int index = 0;

    public Parser1(List<Token> tokens)
    {
        this.tokens = tokens;
    }

    private Token match(TokenType type)
    {
        if (index >= tokens.size())
            return null;

        Token token = tokens.get(index);
        if (token.type != type)
            return null;
        index++;

        return token;
    }

    private void error(String message) throws ParseException
    {
        int errorPosition;
        if (index >= tokens.size())
        {
            if (tokens.isEmpty())
                errorPosition = 0;
            else
                errorPosition = tokens.get(tokens.size() - 1).to;
        }
        else
        {
            Token token = tokens.get(index);
            errorPosition = token.to;
        }
        throw new ParseException(message, errorPosition);
    }

    public void matchExpression() throws ParseException
    {
        /*if(match(TokenType.SM) != null)
            matchExpression();
*/
        int paramCounter = 0;
        //Вначале должен быть for (
        Token lparam = match(TokenType.BEGIN);
        if(lparam != null)
            paramCounter++;
        else error("Missing for ( ");

        //Затем должно идти первое условие
        Token n1 = match(TokenType.SIXTEEN);
        Token n12 = match(TokenType.VAR);
        if (n1 == null && n12 == null)
            error("Missing number/variable!");
        else {
            while (true) {
                // Пока есть символ '=' или ':='...
                if ((match(TokenType.EQUAL) != null) || (match(TokenType.ASSIGN) != null)) {
                    // Требуем после знака снова ЧИСЛО:
                    Token n111 = match(TokenType.SIXTEEN);
                    Token n112 = match(TokenType.VAR);
                    if (n111 == null && n112 == null) {
                        error("Missing number/variable after expression");
                    }
                } else {
                    break;
                }
            }
        }

        //после точки с запятой, второе условие
        if(match(TokenType.SM) != null) {
            Token n21 = match(TokenType.SIXTEEN);
            Token n22 = match(TokenType.VAR);
            if (n21 == null && n22 == null)
                error("Missing number/variable!");
            else{
                while (true) {
                    // Пока есть символ '+' или '-'...
                    if ((match(TokenType.LESS) != null) || (match(TokenType.LESS_OR_EQUAL) != null) ||
                            (match(TokenType.MORE) != null) || (match(TokenType.MORE_OR_EQUAL) != null)) {
                        // Требуем после знака снова ЧИСЛО:
                        Token n211 = match(TokenType.SIXTEEN);
                        Token n212 = match(TokenType.VAR);
                        if (n211 == null && n212 == null) {
                            error("Missing number/variable after expression");
                        }
                    } else {
                        break;
                    }
                }
            }
        }
        else error("Missing 1st ';'");



        //После точки с запятой, третье условие
        if(match(TokenType.SM) != null) {
            Token n31 = match(TokenType.SIXTEEN);
            Token n32 = match(TokenType.VAR);
            if (n31 == null && n32 == null)
                error("Missing number/variable!");
            else{
                while (true) {
                    // Проверка на третье выражение
                    if((match(TokenType.DECR) != null)){
                        System.out.println("Считали инкремент или декремент");
                        break;
                    }
                    else if ((match(TokenType.INCREMENT) != null)){
                        break;
                    }

                    else if((match(TokenType.ASSIGN) != null) || (match(TokenType.EQUAL) != null)){
                        Token n211 = match(TokenType.SIXTEEN);
                        Token n212 = match(TokenType.VAR);
                        if (n211 == null && n212 == null) {
                            error("Missing number/variable after expression=");
                        }
                        else{
                            if((match(TokenType.ADD) != null) ||
                                    (match(TokenType.SUB) != null) || (match(TokenType.MUL) != null) ||
                                    (match(TokenType.DIV) != null)) {
                                // Требуем после знака снова ЧИСЛО:
                                Token n221 = match(TokenType.SIXTEEN);
                                Token n222 = match(TokenType.VAR);
                                if (n221 == null && n222 == null) {
                                    error("Missing number/variable after expression in expression");
                                }
                                }
                            }
                        }
                    else {
                        break;
                    }
                }
            }
        }
        else error("Missing 2nd ';'");

        if (match(TokenType.RPAR)!= null){
        }
        else error("Missing ')'");

        if (match(TokenType.DO) != null) {
            paramCounter--;
        }
        else error("Missing do");


        if(match(TokenType.SM) != null)
            matchExpression();
    }

    public static void main(String[] args) throws ParseException
    {
        Scanner in = new Scanner(System.in);
        String expression = in.nextLine();

        Lexer lexer = new Lexer(expression);
        List<Token> allTokens = lexer.getAllTokens();
        Parser1 parser = new Parser1(allTokens);
        parser.matchExpression();

        for (int i = 0; i < parser.tokens.size(); i++)
            System.out.println(parser.tokens.get(i).symbolText + " " + parser.tokens.get(i).type);
    }
}

