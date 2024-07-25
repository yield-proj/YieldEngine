package com.xebisco.yieldengine.core.input;

public enum Key {
    UNDEFINED,
    VK_ENTER('\n'),
    VK_BACK_SPACE,
    VK_TAB('\t'),
    VK_CANCEL,
    VK_CLEAR,
    VK_SHIFT,
    VK_CONTROL,
    VK_ALT,
    VK_PAUSE,
    VK_CAPS_LOCK,
    VK_ESCAPE,
    VK_SPACE(' '),
    VK_PAGE_UP,
    VK_PAGE_DOWN,
    VK_END,
    VK_HOME,
    VK_LEFT,
    VK_UP,
    VK_RIGHT,
    VK_DOWN,
    VK_COMMA(','),
    VK_MINUS('-'),
    VK_PERIOD('.'),
    VK_SLASH('/'),
    VK_0('0'),
    VK_1('1'),
    VK_2('2'),
    VK_3('3'),
    VK_4('4'),
    VK_5('5'),
    VK_6('6'),
    VK_7('7'),
    VK_8('8'),
    VK_9('9'),
    VK_SEMICOLON(';'),
    VK_EQUALS('='),
    VK_A('A'),
    VK_B('B'),
    VK_C('C'),
    VK_D('D'),
    VK_E('E'),
    VK_F('F'),
    VK_G('G'),
    VK_H('H'),
    VK_I('I'),
    VK_J('J'),
    VK_K('K'),
    VK_L('L'),
    VK_M('M'),
    VK_N('N'),
    VK_O('O'),
    VK_P('P'),
    VK_Q('Q'),
    VK_R('R'),
    VK_S('S'),
    VK_T('T'),
    VK_U('U'),
    VK_V('V'),
    VK_W('W'),
    VK_X('X'),
    VK_Y('Y'),
    VK_Z('Z'),
    VK_OPEN_BRACKET('['),
    VK_BACK_SLASH('\\'),
    VK_CLOSE_BRACKET(']'),
    VK_DELETE,
    VK_NUM_LOCK,
    VK_SCROLL_LOCK,
    VK_F1,
    VK_F2,
    VK_F3,
    VK_F4,
    VK_F5,
    VK_F6,
    VK_F7,
    VK_F8,
    VK_F9,
    VK_F10,
    VK_F11,
    VK_F12,
    VK_PRINTSCREEN,
    VK_INSERT,
    VK_BACK_QUOTE('`'),
    VK_QUOTE('´'),
    VK_AMPERSAND('&'),
    VK_ASTERISK('*'),
    VK_QUOTEDBL('"'),
    VK_LESS('<'),
    VK_GREATER('>'),
    VK_BRACELEFT('{'),
    VK_BRACERIGHT('}'),
    VK_AT('@'),
    VK_COLON(':'),
    VK_CIRCUMFLEX('^'),
    VK_DOLLAR('$'),
    VK_EXCLAMATION_MARK('!'),
    VK_LEFT_PARENTHESIS('('),
    VK_NUMBER_SIGN('#'),
    VK_PLUS('+'),
    VK_RIGHT_PARENTHESIS(')'),
    VK_UNDERSCORE('_'),
    VK_SUPER;

    private final Character character;

    Key(Character character) {
        this.character = character;
    }

    Key() {
        this(null);
    }

    public static Key getKeyFromCharacter(Character character) {
        for(Key k : values()) {
            if(k.character != null && k.character.equals(Character.toUpperCase(character))) {
                return k;
            }
        }
        return null;
    }

    public Character getCharacter() {
        return character;
    }
}
