package com.github.le_yams.openfga4intellij.parsing;

import com.intellij.lexer.FlexLexer;
import com.intellij.psi.tree.IElementType;

import static com.intellij.psi.TokenType.BAD_CHARACTER;
import static com.intellij.psi.TokenType.WHITE_SPACE;
import static com.github.le_yams.openfga4intellij.psi.OpenFGATypes.*;

%%

%{
  public OpenFGALexer() {
    this((java.io.Reader)null);
  }
%}

%public
%class OpenFGALexer
%implements FlexLexer
%function advance
%type IElementType
%unicode

ALPHA_NUMERIC=[a-zA-Z0-9_-]+
END_OF_LINE=(\r\n)|\n
WHITESPACE=[\ \t]
IDENT1=((\ {2})|\t)
IDENT2=((\ {4})|(\t{2}))
SINGLE_LINE_COMMENT=[ \t]*#.*

%%
<YYINITIAL> {
  "                "          { return WHITE_SPACE; }

  "model"                     { return MODEL; }
  "schema"                    { return SCHEMA; }
  "1.1"                       { return SCHEMA_VERSION_V1_1; }
  "type"                      { return TYPE; }
  "relations"                 { return RELATIONS; }
  "define"                    { return DEFINE; }
  "#"                         { return HASH; }
  ":"                         { return COLON; }
  "*"                         { return WILDCARD; }
  "["                         { return L_SQUARE; }
  "]"                         { return R_SQUARE; }
  ","                         { return COMMA; }
  "and"                       { return AND; }
  "or"                        { return OR; }
  "but not"                   { return BUT_NOT; }
  "from"                      { return FROM; }

  {ALPHA_NUMERIC}             { return ALPHA_NUMERIC; }
  {END_OF_LINE}               { return END_OF_LINE; }
  {WHITESPACE}                { return WHITESPACE; }
  {IDENT1}                    { return IDENT1; }
  {IDENT2}                    { return IDENT2; }
  {SINGLE_LINE_COMMENT}       { return SINGLE_LINE_COMMENT; }

}

[^] { return BAD_CHARACTER; }
