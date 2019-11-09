package id.ac.ui.cs.mobileprogramming.fariqodriandana.recorder.models

import id.ac.ui.cs.mobileprogramming.fariqodriandana.recorder.entities.AudioFileWithMetadata

data class AudioListItemModel(
    val filename: String,
    val info: String,
    val audioFileWithMetadata: AudioFileWithMetadata
)