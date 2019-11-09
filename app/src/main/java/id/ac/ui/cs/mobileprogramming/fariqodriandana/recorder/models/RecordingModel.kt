package id.ac.ui.cs.mobileprogramming.fariqodriandana.recorder.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class RecordingModel(
    val fileLocation: String,
    val fileName: String,
    val timestampStart: Long,
    val timestampEnd: Long,
    var textTranslation: String?
): Parcelable