package com.tangqiang.adb.types;

/**
 * TODO
 *
 * @author Tom
 * @version 1.0 2018-01-05 0005 Tom create
 * @date 2018-01-05 0005
 * @copyright Copyright © 2018 Grgbanking All rights reserved.
 */
public enum KeyCode {
    UNKNOWN(0),

    SOFT_LEFT(1),

    SOFT_RIGHT(2),

    HOME(3),

    BACK(4),

    CALL(5),

    ENDCALL(6),

    KEYCODE_0(7),

    KEYCODE_1(8),

    KEYCODE_2(9),

    KEYCODE_3(10),

    KEYCODE_4(11),

    KEYCODE_5(12),

    KEYCODE_6(13),

    KEYCODE_7(14),

    KEYCODE_8(15),

    KEYCODE_9(16),

    STAR(17),

    POUND(18),

    DPAD_UP(19),

    DPAD_DOWN(20),

    DPAD_LEFT(21),

    DPAD_RIGHT(22),

    DPAD_CENTER(23),

    VOLUME_UP(24),

    VOLUME_DOWN(25),

    POWER(26),

    CAMERA(27),

    CLEAR(28),

    A(29),

    B(30),

    C(31),

    D(32),

    E(33),

    F(34),

    G(35),

    H(36),

    I(37),

    J(38),

    K(39),

    L(40),

    M(41),

    N(42),

    O(43),

    P(44),

    Q(45),

    R(46),

    S(47),

    T(48),

    U(49),

    V(50),

    W(51),

    X(52),

    Y(53),

    Z(54),

    COMMA(55),

    PERIOD(56),

    ALT_LEFT(57),

    ALT_RIGHT(58),

    SHIFT_LEFT(59),

    SHIFT_RIGHT(60),

    TAB(61),

    SPACE(62),

    SYM(63),

    EXPLORER(64),

    ENVELOPE(65),

    ENTER(66),

    DEL(67),

    GRAVE(68),

    MINUS(69),

    EQUALS(70),

    LEFT_BRACKET(71),

    RIGHT_BRACKET(72),

    BACKSLASH(73),

    SEMICOLON(74),

    APOSTROPHE(75),

    SLASH(76),

    AT(77),

    NUM(78),

    HEADSETHOOK(79),

    FOCUS(80),//*Camera*focus

    PLUS(81),

    MENU(82),

    NOTIFICATION(83),

    SEARCH(84),

    MEDIA_PLAY_PAUSE(85),

    MEDIA_STOP(86),

    MEDIA_NEXT(87),

    MEDIA_PREVIOUS(88),

    MEDIA_REWIND(89),

    MEDIA_FAST_FORWARD(90),

    MUTE(91);


    private int value;

    KeyCode(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
