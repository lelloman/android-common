package com.lelloman.common.viewmodel.command

import java.io.File

class ShareFileCommand(
    val file: File,
    val authority: String
) : Command