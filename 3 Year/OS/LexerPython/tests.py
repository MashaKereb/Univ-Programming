import unittest
from lexer.lexer import Lexer
from lexer.token_types import TokenType


class TestLexer(unittest.TestCase):

    @classmethod
    def setUpClass(cls):
        cls.lexer = Lexer()

    def test_identifier(self):
        self.assertEqual(TokenType.Identifier, self.lexer.get_first_token("Integer").type)
        self.assertEqual(TokenType.Identifier, self.lexer.get_first_token("FuncName()").type)
        self.assertEqual(TokenType.Identifier, self.lexer.get_first_token("Param_2 {}").type)
        self.assertEqual(TokenType.Identifier, self.lexer.get_first_token("Integer_2").type)
        self.assertEqual(TokenType.Identifier, self.lexer.get_first_token("x_1").type)

    def test_equality_symbols(self):
        self.assertEqual(TokenType.EqualEqual, self.lexer.get_first_token("==").type)
        self.assertEqual(TokenType.Equal, self.lexer.get_first_token("=").type)
        self.assertEqual(TokenType.GreaterEqual, self.lexer.get_first_token(">=").type)
        self.assertEqual(TokenType.LessEqual, self.lexer.get_first_token("<=").type)

    def test_errors(self):
        self.assertEqual(TokenType.Error, self.lexer.get_first_token("1h").type)
        self.assertEqual(TokenType.Error, self.lexer.get_first_token("$").type)
        self.assertEqual(TokenType.Error, self.lexer.get_first_token("#").type)
        self.assertEqual(TokenType.Error, self.lexer.get_first_token("1111_").type)
        self.assertEqual(TokenType.Error, self.lexer.get_first_token("44ss").type)
        self.assertEqual(TokenType.Error, self.lexer.get_first_token(r'"\"').type)
        self.assertEqual(TokenType.Error, self.lexer.get_first_token(r'15.jg').type)
        self.assertEqual(TokenType.Error, self.lexer.get_first_token(r'81.4.4').type)

        self.assertEqual(TokenType.Error, self.lexer.get_first_token(r"'77'").type)
        self.assertEqual(TokenType.Error, self.lexer.get_first_token("'of'").type)
        self.assertEqual(TokenType.Error, self.lexer.get_first_token(r'81..4').type)
        self.assertEqual(TokenType.Error, self.lexer.get_first_token(r'"xcvgbhnjm\\"jhgf"').type)

        self.assertEqual(TokenType.UnclosedComment, self.lexer.get_first_token('/*fjiofjgisjigfjsj\ngggg').type)

        self.assertEqual('/*fjiofjgisjigfjsj\nggg', self.lexer.get_first_token('/*fjiofjgisjigfjsj\nggg').token_string)

    def test_literals(self):
        self.assertEqual(TokenType.StringLiteral, self.lexer.get_first_token('"asdfff"').type)
        self.assertEqual(TokenType.StringLiteral, self.lexer.get_first_token(r'"asd\"fff"').type)
        self.assertEqual(TokenType.StringLiteral, self.lexer.get_first_token('"asdf494/2423*33ff"').type)
        self.assertEqual(TokenType.IntConstant, self.lexer.get_first_token("1000075==oo").type)
        self.assertEqual(TokenType.DoubleConstant, self.lexer.get_first_token("1557.25").type)
        self.assertEqual(TokenType.CharLiteral, self.lexer.get_first_token("'l'").type)
        self.assertEqual(TokenType.CharLiteral, self.lexer.get_first_token(r"'\n'").type)
        self.assertEqual(TokenType.CharLiteralWithSlash, self.lexer.get_first_token(r"'\p'").type)


if __name__ == '__main__':
    unittest.main()
