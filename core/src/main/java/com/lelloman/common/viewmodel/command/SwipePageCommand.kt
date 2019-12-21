package com.lelloman.common.viewmodel.command

@Suppress("unused")
class SwipePageCommand(val direction: Direction) : Command {

    enum class Direction { LEFT, RIGHT }
}