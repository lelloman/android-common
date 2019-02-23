package com.lelloman.common.view.actionevent

@Suppress("unused")
class SwipePageActionEvent(val direction: Direction) : ViewActionEvent {

    enum class Direction { LEFT, RIGHT }
}