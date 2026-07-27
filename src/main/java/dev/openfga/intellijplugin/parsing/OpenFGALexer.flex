package dev.openfga.intellijplugin.parsing;

import com.intellij.lexer.FlexLexer;
import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IElementType;

import static com.intellij.psi.TokenType.BAD_CHARACTER;
import static com.intellij.psi.TokenType.WHITE_SPACE;
import static dev.openfga.intellijplugin.psi.OpenFGATypes.*;

%%

%{
  public OpenFGALexer() {
    this((java.io.Reader)null);
  }

  /**
   * Returns true when only spaces or tabs remain between the end of the
   * current match and the end of the buffer, i.e. the match is part of a
   * whitespace-only final line.
   */
  private boolean isWhitespaceOnlyToEof() {
    for (int i = zzMarkedPos; i < zzEndRead; i++) {
      char c = zzBuffer.charAt(i);
      if (c != ' ' && c != '\t') {
        return false;
      }
    }
    return true;
  }
%}

%public
%class OpenFGALexer
%implements FlexLexer
%function advance
%type IElementType
%unicode

ALPHA_NUMERIC=[a-zA-Z0-9_-]+
SCHEMA_VERSION=(\d+\.\d+)
END_OF_LINE=(\r\n)|\n
WHITESPACE=[\ \t]
IDENT1=((\ {2})|\t)
IDENT2=((\ {4})|(\t{2}))
SINGLE_LINE_COMMENT=[ \t]*#+[^\n]*
// SINGLE_TRAILING_COMMENT=\s+#+[^\n]*
CONDITION_EXPRESSION=(\{[^\}]+\})

%%
<YYINITIAL> {
  "module"                    { return MODULE; }
  "model"                     { return MODEL; }
  "schema"                    { return SCHEMA; }
  "type"                      { return TYPE; }
  "extend"                    { return EXTEND; }
  "relations"                 { return RELATIONS; }
  "define"                    { return DEFINE; }
  "#"                         { return HASH; }
  ":"                         { return COLON; }
  "*"                         { return WILDCARD; }
  "["                         { return L_SQUARE; }
  "]"                         { return R_SQUARE; }
  "("                         { return L_PAREN; }
  ")"                         { return R_PAREN; }
  "<"                         { return LESS; }
  ">"                         { return GREATER; }
  ","                         { return COMMA; }
  "and"                       { return AND; }
  "or"                        { return OR; }
  "but not"                   { return BUT_NOT; }
  "from"                      { return FROM; }
  "condition"                 { return CONDITION; }
  "with"                      { return WITH; }

  {ALPHA_NUMERIC}             { return ALPHA_NUMERIC; }
  {SCHEMA_VERSION}            { return SCHEMA_VERSION; }
  {END_OF_LINE}               { return END_OF_LINE; }
  {WHITESPACE}                { return TokenType.WHITE_SPACE; }
  // Whitespace-only lines are plain whitespace, not indentation (#163).
  ^{WHITESPACE}+/{END_OF_LINE} { return TokenType.WHITE_SPACE; }
  ^{IDENT1}                   { return isWhitespaceOnlyToEof() ? TokenType.WHITE_SPACE : IDENT1; }
  ^{IDENT2}                   { return isWhitespaceOnlyToEof() ? TokenType.WHITE_SPACE : IDENT2; }
  ^{SINGLE_LINE_COMMENT}$     { return SINGLE_LINE_COMMENT; }
  {CONDITION_EXPRESSION}      { return CONDITION_EXPRESSION; }
}

[^] { return TokenType.BAD_CHARACTER; }