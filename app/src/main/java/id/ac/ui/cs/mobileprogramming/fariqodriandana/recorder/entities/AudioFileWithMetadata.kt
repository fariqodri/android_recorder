package id.ac.ui.cs.mobileprogramming.fariqodriandana.recorder.entities

import androidx.room.Embedded
import androidx.room.Relation

data class AudioFileWithMetadata(
    @Embedded
    val audioFile: AudioFile,

    @Relation(parentColumn = "id", entityColumn = "fileId", entity = AudioMetadata::class)
    private val _metadatas: List<AudioMetadata>
) {
    val metadatas: AudioMetadata?
        get() = _metadatas.firstOrNull()
}