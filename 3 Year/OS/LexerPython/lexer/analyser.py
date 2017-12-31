"""
    Ієрархія аналізаторів різного є уособленням ускладнення логіки програми і методу аналізу токенів.

    RegexpAnalyser просто шукає співпадіння по регулярному виразу.

    FAAnalyzer реалізує скінченний автомат, який описується вхідним символом,
    словником переходів та множиною стній, що приймаються.

    FARegexpAnalyzer - той же скінченний автомат, але для спрощення (зменшення алфавіту) при
    описі переходів дозволяється використовувати регулярні вирази. Обто, замість того,
    щоб перераховувати всі 10 цифр, можна просто написати "\d"

    LookAheadFAAnalyzer - скінченний автомат, який додатково перевіряє відповідньсть наступного символа заданому.
    Використовується для того, щоб, наприклад, розрізнити int константу "10" від помилкового токена "10ррр".

    Насправді, всю цю ієрархію можна замінити одним, широким типом аналізатора (LookAheadFAAnalyzer),
    однак я вважаю корисним залишити їх для наочності.

"""

import re


class Analyzer:
    def check(self, string, position):
        raise NotImplemented()


class RegexpAnalyser(Analyzer):
    def __init__(self, regexp):
        self.regexp = regexp

    def check(self, string, position):
        pattern = re.compile(self.regexp + ".*", re.DOTALL)
        match = pattern.match(string, pos=position)
        if match:
            lexema = match.group(1)
            return lexema
        return None


class FABaseAnalyzer(Analyzer):
    def __init__(self, start_state, accept_states):
        """

        :param start_state:  initial state for fa
        :param accept_states: list of accepted states
        """
        self.accept_states = accept_states
        self.start_state = start_state

    def check(self, string, position):
        state = self.start_state
        curr_pos = position
        lexema = ""

        while curr_pos < len(string):

            next_state = self.get_next_state(state, string[curr_pos])
            if next_state is None:
                break
            lexema += string[curr_pos]
            curr_pos += 1
            state = next_state

        if lexema == "" or state not in self.accept_states or not self.additional_checks(curr_pos, string, state):
            return None
        return lexema

    def get_next_state(self, state, character):
        raise NotImplemented()

    def additional_checks(self, current_position, input_string, current_state):
        """
        Method for father extensions in successor classes

        :param current_position:
        :param input_string:
        :param current_state:
        :return:
        """
        return True


class FAAnalyzer(FABaseAnalyzer):
    def __init__(self, start_state, transition_map, accept_states):
        """

        :param start_state:  initial state for fa
        :param transition_map: transition function in such format {(state, character): next_state}
        :param accept_states: list of accepted states
        """
        super().__init__(start_state, accept_states)
        self.transition_map = transition_map

    def get_next_state(self, state, character):
        return self.transition_map.get((state, character), None)


class FARegexpAnalyzer(FABaseAnalyzer):
    def __init__(self, start_state, transition_list, accept_states):
        """

        :param start_state: initial state for fa
        :param transition_list: transition_function in such format [(state, regexp_for_char, next_state), ...]
        :param accept_states: list of accepted states
        """
        super().__init__(start_state, accept_states)
        self.transition_list = transition_list

    def get_next_state(self, state, char):
        for transition in self.transition_list:
            if transition[0] == state and re.match(transition[1], char, re.DOTALL):
                return transition[2]
        return None


class LookAheadFAAnalyzer(FARegexpAnalyzer):
    def __init__(self, start_state, transition_list, accept_states, next_symbol_regexp):
        super().__init__(start_state, transition_list, accept_states)
        self.next_symbol = next_symbol_regexp

    def lookahead_and_check(self, string):
        if re.match('.' + self.next_symbol, string, re.DOTALL):
            return True
        return False

    def additional_checks(self, current_position, input_string, current_state):
        return self.lookahead_and_check(input_string[current_position - 1:current_position + 1])
