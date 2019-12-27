package id.ac.ui.cs.mobileprogramming.fariqodriandana.recorder.opengl

import android.graphics.Color

object MyColor {
    internal fun red(): FloatArray {
        return floatArrayOf(
            Color.red(Color.RED) / 255f,
            Color.green(Color.RED) / 255f,
            Color.blue(Color.RED) / 255f,
            1.0f
        )
    }

    internal fun green(): FloatArray {
        return floatArrayOf(
            Color.red(Color.GREEN) / 255f,
            Color.green(Color.GREEN) / 255f,
            Color.blue(Color.GREEN) / 255f,
            1.0f
        )
    }

    internal fun blue(): FloatArray {
        return floatArrayOf(
            Color.red(Color.BLUE) / 255f,
            Color.green(Color.BLUE) / 255f,
            Color.blue(Color.BLUE) / 255f,
            1.0f
        )
    }

    internal fun yellow(): FloatArray {
        return floatArrayOf(
            Color.red(Color.YELLOW) / 255f,
            Color.green(Color.YELLOW) / 255f,
            Color.blue(Color.YELLOW) / 255f,
            1.0f
        )
    }

    internal fun cyan(): FloatArray {
        return floatArrayOf(
            Color.red(Color.CYAN) / 255f,
            Color.green(Color.CYAN) / 255f,
            Color.blue(Color.CYAN) / 255f,
            1.0f
        )
    }

    internal fun gray(): FloatArray {
        return floatArrayOf(
            Color.red(Color.GRAY) / 255f,
            Color.green(Color.GRAY) / 255f,
            Color.blue(Color.GRAY) / 255f,
            1.0f
        )
    }


}
