package id.ac.ui.cs.mobileprogramming.fariqodriandana.recorder.fragments


import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import id.ac.ui.cs.mobileprogramming.fariqodriandana.recorder.R
import id.ac.ui.cs.mobileprogramming.fariqodriandana.recorder.activities.MainActivity
import id.ac.ui.cs.mobileprogramming.fariqodriandana.recorder.adapters.AudioListAdapter
import id.ac.ui.cs.mobileprogramming.fariqodriandana.recorder.databinding.FragmentPlayerBinding
import id.ac.ui.cs.mobileprogramming.fariqodriandana.recorder.event_handlers.PlayerViewEventHandler
import id.ac.ui.cs.mobileprogramming.fariqodriandana.recorder.entities.AudioFileWithMetadata
import id.ac.ui.cs.mobileprogramming.fariqodriandana.recorder.view_models.PlayerViewModel
import id.ac.ui.cs.mobileprogramming.fariqodriandana.recorder.enums.PlayEnum.*
import kotlinx.android.synthetic.main.fragment_player.*


/**
 * A simple [Fragment] subclass.
 * Use the [PlayerFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PlayerFragment : Fragment() {

    private lateinit var viewModel: PlayerViewModel
    private lateinit var audioFileListAdapter: AudioListAdapter
    private lateinit var audioRecyclerView: RecyclerView
    private lateinit var playerUIReceiver: PlayerUIReceiver
    private lateinit var activity: MainActivity
    private lateinit var eventHandler: PlayerViewEventHandler

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity = context as MainActivity
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = activity.run {
            ViewModelProviders.of(this).get(PlayerViewModel::class.java)
        }
        viewModel.getAllFileWithMetadata(activity)
        eventHandler = PlayerViewEventHandler(this)
        val intentFilter = IntentFilter(STOP_PLAYER_ACTION.name)
        intentFilter.addAction(PLAY_ACTION.name)
        playerUIReceiver = PlayerUIReceiver()
    }

    override fun onResume() {
        super.onResume()
        activity.registerReceiver(playerUIReceiver, IntentFilter().apply {
            addAction(STOP_PLAYER_ACTION.name)
            addAction(PLAY_ACTION.name)
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val dataBinding = DataBindingUtil.inflate<FragmentPlayerBinding>(inflater, R.layout.fragment_player, container, false)
        dataBinding.viewModel = viewModel
        dataBinding.eventHandler = eventHandler
        dataBinding.lifecycleOwner = activity
        return dataBinding.root
    }

    private fun setupRecyclerView() {
        audioRecyclerView = audio_list
        val layoutManager = LinearLayoutManager(activity)
        val items = ArrayList<AudioFileWithMetadata>()
        audioRecyclerView.layoutManager = layoutManager
        audioRecyclerView.itemAnimator = DefaultItemAnimator()
        audioFileListAdapter = AudioListAdapter(items, activity, eventHandler)
        audioRecyclerView.adapter = audioFileListAdapter
        audioRecyclerView.addItemDecoration(DividerItemDecoration(activity, layoutManager.orientation))
        viewModel.listOfAudioFileWithMetadata.observe(this, Observer {
            items.addAll(it)
            audioRecyclerView.adapter!!.notifyDataSetChanged()
        })
    }

    fun pauseAudio() {
        activity.sendBroadcast(Intent(PAUSE_ACTION.name))
    }

    fun resumeAudio() {
        activity.sendBroadcast(Intent(RESUME_ACTION.name))
    }

    fun stopAudio() {
        activity.sendBroadcast(Intent(STOP_PLAYER_ACTION.name))
    }

    override fun onPause() {
        super.onPause()
        activity.unregisterReceiver(playerUIReceiver)
    }


    companion object {
        @JvmStatic
        fun newInstance() = PlayerFragment()
    }

    internal inner class PlayerUIReceiver : BroadcastReceiver() {
        override fun onReceive(p0: Context?, p1: Intent?) {
            Log.d("Intent Action", p1?.action)
            when (p1?.action) {
                STOP_PLAYER_ACTION.name -> {
                    viewModel.isPlaying.value = false
                }
                PLAY_ACTION.name -> {
                    val textTranslation = p1.getStringExtra("TEXT_TRANSLATION")
                    viewModel.isPlaying.value = true
                    viewModel.textTranslation.value = textTranslation
                }
            }
        }
    }
}
