from .analyser import RegexpAnalyser, FAAnalyzer, FARegexpAnalyzer, LookAheadFAAnalyzer
from .token import Token
from collections import OrderedDict
from .token_types import TokenType


class Lexer:
    class LexicalError(Exception):
        def __init__(self, position, text=""):
            self.position = position
            self.text = text

        def __str__(self):
            return "Lexical error at position " + str(self.position) + " " + self.text

    def __init__(self):
        self.result_list = []

        self.type_map = OrderedDict()

        self.type_map[TokenType.BlockComment] = RegexpAnalyser(r"(/\*.*?\*/)")
        self.type_map[TokenType.LineComment] = FARegexpAnalyzer(
            start_state=0,
            transition_list=((0, "/", 1), (1, "/", 2), (2, "[^\\n]", 2)),
            accept_states=[2]
        )
        self.type_map[TokenType.WhiteSpace] = RegexpAnalyser("( )")
        self.type_map[TokenType.Tab] = RegexpAnalyser(r"(\t)")
        self.type_map[TokenType.NewLine] = RegexpAnalyser(r"(\n)")
        self.type_map[TokenType.OpenBrace] = RegexpAnalyser(r"(\()")
        self.type_map[TokenType.CloseBrace] = RegexpAnalyser(r"(\))")
        self.type_map[TokenType.Semicolon] = RegexpAnalyser("(;)")
        self.type_map[TokenType.Comma] = RegexpAnalyser("(,)")
        self.type_map[TokenType.Point] = RegexpAnalyser(r"(\.)")
        self.type_map[TokenType.Plus] = RegexpAnalyser(r"(\+)")
        self.type_map[TokenType.Minus] = RegexpAnalyser(r"(\-)")
        self.type_map[TokenType.Multiply] = RegexpAnalyser(r"(\*)")
        self.type_map[TokenType.Divide] = RegexpAnalyser("(/)[^*]")

        self.type_map[TokenType.GreaterEqual] = FAAnalyzer(0, {(0, ">"): 1, (1, "="): 2}, [2])
        self.type_map[TokenType.LessEqual] = FAAnalyzer(0, {(0, "<"): 1, (1, "="): 2}, [2])
        self.type_map[TokenType.EqualEqual] = FAAnalyzer(0, {(0, "="): 1, (1, "="): 2}, [2])

        self.type_map[TokenType.OpeningCurlyBrace] = RegexpAnalyser(r"(\{)")
        self.type_map[TokenType.ClosingCurlyBrace] = RegexpAnalyser(r"(\})")
        self.type_map[TokenType.OpeningSquareBrace] = RegexpAnalyser(r"(\[)")
        self.type_map[TokenType.ClosingSquareBrace] = RegexpAnalyser(r"(\])")

        self.type_map[TokenType.CharLiteral] = FARegexpAnalyzer(
            0,
            ((0, "'", 2), (2, r"[^\\]", 4), (2, r"\\", 3), (3, r"[rnt'\\]", 4), (4, "'", 5)),
            [5]
        )

        self.type_map[TokenType.CharLiteralWithSlash] = FARegexpAnalyzer(
            0,
            ((0, "'", 2), (2, r"\\", 3), (3, r".", 4), (4, "'", 5)),
            [5]
        )

        self.type_map[TokenType.DoubleConstant] = LookAheadFAAnalyzer(
            start_state=0,
            transition_list=[(0, "\d", 1), (1, "\d", 1), (1, "\.", 2), (2, "\d", 3), (3, "\d", 3)],
            accept_states=[3],
            next_symbol_regexp="\\b([^a-zA-Z\.]|$)")

        self.type_map[TokenType.StringLiteral] = LookAheadFAAnalyzer(
            start_state=0,
            transition_list=((0, '"', 1), (1, '"', 3), (1, r"\\", 2), (2, r'[rnt\\"]', 1), (1, r'[^\\\n"]', 1)),
            accept_states=[3],
            next_symbol_regexp="(\W|$)"
        )

        self.type_map[TokenType.IntConstant] = LookAheadFAAnalyzer(
            start_state=0,
            transition_list=[(0, "\d", 1), (1, "\d", 1)],
            accept_states=[1],
            next_symbol_regexp="\\b([^a-zA-Z\.]|$)")

        self.type_map[TokenType.Void] = RegexpAnalyser(r"\b(void)\b")
        self.type_map[TokenType.Int] = RegexpAnalyser(r"\b(int)\b")
        self.type_map[TokenType.Double] = RegexpAnalyser(r"\b(int|double)\b")
        self.type_map[TokenType.Public] = RegexpAnalyser(r"\b(public)\b")
        self.type_map[TokenType.Static] = RegexpAnalyser(r"\b(static)\b")
        self.type_map[TokenType.Private] = RegexpAnalyser(r"\b(private)\b")
        self.type_map[TokenType.TokenFalse] = RegexpAnalyser(r"\b(false)\b")
        self.type_map[TokenType.TokenTrue] = RegexpAnalyser(r"\b(true)\b")
        self.type_map[TokenType.Null] = RegexpAnalyser(r"\b(null)\b")
        self.type_map[TokenType.Return] = RegexpAnalyser(r"\b(return)\b")
        self.type_map[TokenType.New] = RegexpAnalyser(r"\b(new)\b")
        self.type_map[TokenType.Class] = RegexpAnalyser(r"\b(class)\b")
        self.type_map[TokenType.If] = RegexpAnalyser(r"\b(if)\b")
        self.type_map[TokenType.Else] = RegexpAnalyser(r"\b(else)\b")
        self.type_map[TokenType.While] = RegexpAnalyser(r"\b(while)\b")
        self.type_map[TokenType.Equal] = RegexpAnalyser("(=)")
        self.type_map[TokenType.NotEqual] = RegexpAnalyser(r"(\!=)")
        self.type_map[TokenType.Greater] = RegexpAnalyser("(>)")
        self.type_map[TokenType.Less] = RegexpAnalyser("(<)")
        self.type_map[TokenType.BitOr] = RegexpAnalyser(r"(\|)")

        self.type_map[TokenType.Identifier] = FARegexpAnalyzer(0, (
            (0, "[a-zA-Z_]", 1),
            (1, "[\w]", 2),
            (2, "[\w]", 2)
        ), [1, 2])

        self.type_map[TokenType.UnclosedComment] = FARegexpAnalyzer(
            0,
            ((0, "/", 1), (1, "\\*", 2), (2, ".", 2)),
            [2],

        )
        self.type_map[TokenType.Error] = RegexpAnalyser("([^\s]+[\w]*)")

    def get_significant_tokens(self):
        return list(filter(lambda token: not token.is_whitespace_or_comment(), self.result_list))

    def get_errors(self):
        return list(filter(lambda token: token.is_error(), self.result_list))

    def separate_token(self, input_str, position):
        for key in self.type_map.keys():
            res = self.type_map[key].check(input_str, position)
            if res is not None:
                return Token(position, position + len(res), res, key)
        return None

    def tokenize(self, input_string):
        self.result_list = []
        position = 0
        token = 0
        while token is not None and position != len(input_string):
            token = self.separate_token(input_string, position)
            if token is not None:
                self.result_list.append(token)
                position = token.end

        if position == len(input_string) and token is not None:
            return self.result_list
        else:
            raise Lexer.LexicalError(position=position, text=input_string[position - 10:position + 10])

    def get_first_token(self, input_string):
        return self.tokenize(input_string)[0]

    def get_tokens_by_type(self, token_type):
        return list(filter(lambda token: token.type == token_type, self.result_list))
