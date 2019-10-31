package id.ac.ui.cs.mobileprogramming.fariqodriandana.recorder.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import id.ac.ui.cs.mobileprogramming.fariqodriandana.recorder.R
import id.ac.ui.cs.mobileprogramming.fariqodriandana.recorder.adapters.AudioListAdapter
import id.ac.ui.cs.mobileprogramming.fariqodriandana.recorder.view_models.PlayerViewModel
import kotlinx.android.synthetic.main.activity_audio_list.*

class AudioListActivity : AppCompatActivity() {

    private lateinit var viewModel: PlayerViewModel
    private lateinit var audioFileListAdapter: AudioListAdapter
    private lateinit var audioRecyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audio_list)
        viewModel = ViewModelProviders.of(this).get(PlayerViewModel::class.java)
        viewModel.getAllFileWithMetadata(this)
        audioRecyclerView = audio_list
        viewModel.listOfAudioFileWithMetadata.observe(this, Observer {
            audioFileListAdapter = AudioListAdapter(it, this)
            audioRecyclerView.adapter = audioFileListAdapter
            audioRecyclerView.layoutManager = LinearLayoutManager(this)
            audioRecyclerView.itemAnimator = DefaultItemAnimator()
        })
    }
}
