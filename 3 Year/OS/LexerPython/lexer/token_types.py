from enum import Enum

TokenType = Enum(
    "TokenType",
    "BlockComment LineComment WhiteSpace Tab NewLine OpenBrace CloseBrace "
    "Semicolon Comma Point Plus Minus Multiply Divide GreaterEqual LessEqual "
    "EqualEqual OpeningCurlyBrace ClosingCurlyBrace OpeningSquareBrace ClosingSquareBrace"
    " DoubleConstant StringLiteral IntConstant Void Int Double Public Static Private "
    "TokenFalse TokenTrue Null Return New Class If Else While Equal NotEqual Greater"
    " Less Identifier CharLiteral CharLiteralWithSlash BitOr UnclosedComment Error"
)

