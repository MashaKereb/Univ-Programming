from lexer.lexer import Lexer
from lexer.token_types import TokenType


def print_list(item_list, text=None):
    if text is not None and len(item_list):
        print(text.center(50, "-"))
    for item in item_list:
        print(item)


if __name__ == "__main__":
    lexer = Lexer()

    with open("Test.java") as f:
        lexer.tokenize(f.read())

    print_list(lexer.get_significant_tokens(), "All tokens")
    print_list(lexer.get_errors(), "Errors")
    print_list(lexer.get_tokens_by_type(TokenType.StringLiteral), text="String literals")
    print_list(lexer.get_tokens_by_type(TokenType.CharLiteral), text="Char literals")
