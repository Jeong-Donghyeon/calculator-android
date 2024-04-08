package com.donghyeon.dev.calculator

enum class Nav {
    EXIT,
    POP,
    POP_ROOT,
    POP_TO,
    PUSH,
    POP_PUSH,
}

enum class Dest(val route: String) {
    INFO("Info"),
    MAIN("Main"),
}
