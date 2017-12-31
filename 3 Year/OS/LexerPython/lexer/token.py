"""
    The Token class represents token (lexeme). A token is a string of
    characters, categorized according to the rules as a symbol
"""

from .token_types import TokenType


class Token:
    def __init__(self, beginning, ending, token_string, token_type):
        self.begin = beginning  # The beginning and ending indexes of this token in the input
        self.end = ending
        self.type = token_type  # token category
        self.token_string = token_string  # string of characters that represents this token

    def __str__(self):
        if not self.is_whitespace():
            return self.type.name + "  '" + self.token_string + "' [" + str(self.begin) + ";" + str(self.end) + "] "

        else:
            return self.type.name + "   [" + str(self.begin) + ";" + str(self.end) + "] "

    def is_whitespace_or_comment(self):
        return self.is_comment() or self.is_whitespace()

    def is_whitespace(self):
        return self.type == TokenType.NewLine or self.type == TokenType.Tab or self.type == TokenType.WhiteSpace

    def is_comment(self):
        return self.type == TokenType.BlockComment or self.type == TokenType.LineComment

    def is_error(self):
        return self.type == TokenType.Error or self.type == TokenType.UnclosedComment
