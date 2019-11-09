package id.ac.ui.cs.mobileprogramming.fariqodriandana.recorder.view_models

import android.content.Context
import androidx.lifecycle.ViewModel
import id.ac.ui.cs.mobileprogramming.fariqodriandana.recorder.entities.UserName
import id.ac.ui.cs.mobileprogramming.fariqodriandana.recorder.repositories.UserRepo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class UserViewModel : ViewModel() {
    var promptedForAccount = false

    private val job by lazy {
        Job()
    }

    private val coroutineScope by lazy {
        CoroutineScope(Dispatchers.Default + job)
    }

    fun insert(userName: UserName, context: Context) {
        coroutineScope.launch {
            val userRepo = UserRepo.getInstance(context)
            userRepo.insert(userName)
        }
    }
}