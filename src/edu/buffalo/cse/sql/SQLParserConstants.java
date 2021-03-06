/* Generated By:JavaCC: Do not edit this line. SQLParserConstants.java */
package edu.buffalo.cse.sql;


/**
 * Token literal values and constants.
 * Generated by org.javacc.parser.OtherFilesGen#start()
 */
public interface SQLParserConstants {

  /** End of File. */
  int EOF = 0;
  /** RegularExpression Id. */
  int SELECT = 6;
  /** RegularExpression Id. */
  int CREATE_TABLE = 7;
  /** RegularExpression Id. */
  int FROM = 8;
  /** RegularExpression Id. */
  int JOIN = 9;
  /** RegularExpression Id. */
  int WHERE = 10;
  /** RegularExpression Id. */
  int ON = 11;
  /** RegularExpression Id. */
  int GROUP = 12;
  /** RegularExpression Id. */
  int BY = 13;
  /** RegularExpression Id. */
  int UNION = 14;
  /** RegularExpression Id. */
  int AND = 15;
  /** RegularExpression Id. */
  int AS = 16;
  /** RegularExpression Id. */
  int ASC = 17;
  /** RegularExpression Id. */
  int DESC = 18;
  /** RegularExpression Id. */
  int OR = 19;
  /** RegularExpression Id. */
  int NOT = 20;
  /** RegularExpression Id. */
  int TRUE = 21;
  /** RegularExpression Id. */
  int FALSE = 22;
  /** RegularExpression Id. */
  int FILEPATH = 23;
  /** RegularExpression Id. */
  int STRING_LITERAL = 24;
  /** RegularExpression Id. */
  int STRING_SLASH = 25;
  /** RegularExpression Id. */
  int STRING_QUOTE = 26;
  /** RegularExpression Id. */
  int FILE_NAME = 27;
  /** RegularExpression Id. */
  int INTEGER = 28;
  /** RegularExpression Id. */
  int DATE = 29;
  /** RegularExpression Id. */
  int FLOAT = 30;
  /** RegularExpression Id. */
  int STRING = 31;
  /** RegularExpression Id. */
  int BOOLEAN = 32;
  /** RegularExpression Id. */
  int DOUBLE = 33;
  /** RegularExpression Id. */
  int NUMBER = 34;
  /** RegularExpression Id. */
  int NEGATIVE = 35;
  /** RegularExpression Id. */
  int EXPONENT = 36;
  /** RegularExpression Id. */
  int FLOATING_POINT_LITERAL = 37;
  /** RegularExpression Id. */
  int SEMICOLON = 38;
  /** RegularExpression Id. */
  int COMMA = 39;
  /** RegularExpression Id. */
  int DOT = 40;
  /** RegularExpression Id. */
  int LESS = 41;
  /** RegularExpression Id. */
  int LESSEQUAL = 42;
  /** RegularExpression Id. */
  int GREATER = 43;
  /** RegularExpression Id. */
  int GREATEREQUAL = 44;
  /** RegularExpression Id. */
  int EQUAL = 45;
  /** RegularExpression Id. */
  int NOTEQUAL = 46;
  /** RegularExpression Id. */
  int MULTIPLY = 47;
  /** RegularExpression Id. */
  int DIVIDE = 48;
  /** RegularExpression Id. */
  int PLUS = 49;
  /** RegularExpression Id. */
  int MINUS = 50;
  /** RegularExpression Id. */
  int QUOTE = 51;
  /** RegularExpression Id. */
  int OPEN = 52;
  /** RegularExpression Id. */
  int CLOSE = 53;
  /** RegularExpression Id. */
  int COMMENT = 54;

  /** Lexical state. */
  int DEFAULT = 0;

  /** Literal token values. */
  String[] tokenImage = {
    "<EOF>",
    "\" \"",
    "\"\\n\"",
    "\"\\r\"",
    "\"\\t\"",
    "\"\\r\\n\"",
    "\"SELECT\"",
    "\"CREATE TABLE\"",
    "\"FROM\"",
    "\"JOIN\"",
    "\"WHERE\"",
    "\"ON\"",
    "\"GROUP\"",
    "\"BY\"",
    "\"UNION\"",
    "\"AND\"",
    "\"AS\"",
    "\"ASC\"",
    "\"DESC\"",
    "\"OR\"",
    "\"NOT\"",
    "\"TRUE\"",
    "\"FALSE\"",
    "\"FROM FILE\"",
    "<STRING_LITERAL>",
    "<STRING_SLASH>",
    "<STRING_QUOTE>",
    "<FILE_NAME>",
    "\"int\"",
    "\"date\"",
    "\"float\"",
    "\"string\"",
    "\"boolean\"",
    "\"double\"",
    "<NUMBER>",
    "<NEGATIVE>",
    "<EXPONENT>",
    "<FLOATING_POINT_LITERAL>",
    "\";\"",
    "\",\"",
    "\".\"",
    "\"<\"",
    "\"<=\"",
    "\">\"",
    "\">=\"",
    "\"=\"",
    "\"!=\"",
    "\"*\"",
    "\"/\"",
    "\"+\"",
    "\"-\"",
    "\"\\\'\"",
    "\"(\"",
    "\")\"",
    "<COMMENT>",
  };

}
