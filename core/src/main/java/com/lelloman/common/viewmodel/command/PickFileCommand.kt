package com.lelloman.common.viewmodel.command

import androidx.annotation.IntRange

class PickFileCommand(
    @IntRange(from = 0, to = 0xff) val requestCode: Int
) : Command