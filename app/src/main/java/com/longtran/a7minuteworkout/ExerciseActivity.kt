package com.longtran.a7minuteworkout

import android.app.Dialog
import android.content.Intent
import android.media.MediaPlayer
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import com.longtran.a7minuteworkout.databinding.ActivityExerciseBinding
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList

class ExerciseActivity : AppCompatActivity(), TextToSpeech.OnInitListener {
    private var restTimer : CountDownTimer? = null
    private var restProgress = 0
    private var restTimerDuration : Long = 1
    private var exerciseTimer : CountDownTimer? = null
    private var exerciseProgress = 0
    private var exerciseTimerDuration : Long = 1
    private var exerciseList : ArrayList<ExerciseModel>? = null
    private var currentExercisePosition = -1
    private var tts: TextToSpeech? = null
    private var player: MediaPlayer? = null
    private var exerciseAdapter : ExerciseStatusAdapter? = null

    private lateinit var binding: ActivityExerciseBinding

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExerciseBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        setSupportActionBar(binding.toolbarExerciseActivity)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbarExerciseActivity.setNavigationOnClickListener {
            customDialogForBackButton()
        }
        tts = TextToSpeech(this,this)
        binding.llExerciseView.visibility = View.GONE
        exerciseList = Constants.defaultExerciseList()
        setUpRestView()
        setupExerciseStatusRecyclerView()
    }
    override fun onDestroy() {
        if(restTimer != null){
            restTimer!!.cancel()
            restProgress = 0
        }
        //Kill textToSpeech
        if(exerciseTimer != null){
            exerciseTimer!!.cancel()
            exerciseProgress =0
        }
        if(tts != null){
            tts!!.stop()
            tts!!.shutdown()
        }
        if(player!= null){
            player!!.stop()
        }
        super.onDestroy()
    }


    private fun setRestProgressBar(){
       binding.progressBar.progress = restProgress
       restTimer = object : CountDownTimer(restTimerDuration*1000,1000){
           override fun onTick(millisUntilFinished: Long) {
               restProgress++
               binding.progressBar.progress = restTimerDuration.toInt()-restProgress
               binding.tvTimer.text = (restTimerDuration.toInt()-restProgress).toString()
           }
           @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
           override fun onFinish() {
               currentExercisePosition++
               exerciseList!![currentExercisePosition].setIsSelected(true)
               exerciseAdapter!!.notifyDataSetChanged()
               setUpExerciseView()
           }
       }.start()
    }
    private fun setUpRestView(){
        try {
            player = MediaPlayer.create(applicationContext,R.raw.press_start)
            player!!.isLooping = false
            player!!.start()
        }catch (e: Exception){
            e.printStackTrace()
        }
        binding.llRestView.visibility = View.VISIBLE
        binding.llExerciseView.visibility =View.GONE
        if(restTimer != null){
            restTimer!!.cancel()
            restProgress = 0
        }
        binding.tvUpcomingExerciseName.text =  exerciseList!![currentExercisePosition+1].getName()
        setRestProgressBar()
    }


    private fun setExerciseProgressBar(){
        binding.progressBarExercise.progress = exerciseProgress
        exerciseTimer = object : CountDownTimer(exerciseTimerDuration * 1000,1000){
            override fun onTick(millisUntilFinished: Long) {
                exerciseProgress++
                binding.progressBarExercise.progress = exerciseTimerDuration.toInt()-exerciseProgress
                binding.tvExerciseTimer.text = (exerciseTimerDuration.toInt()-exerciseProgress).toString()
            }
            override fun onFinish() {
               if(currentExercisePosition < exerciseList?.size!! -1){
                   exerciseList!![currentExercisePosition].setIsSelected(false)
                   exerciseList!![currentExercisePosition].setIsCompleted(true)
                   exerciseAdapter!!.notifyDataSetChanged()
                   setUpRestView()
               }else{
                   finish()
                   val intent = Intent(this@ExerciseActivity,FinishActivity::class.java)
                   startActivity(intent)
               }
            }
        }.start()
    }
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun setUpExerciseView(){
        binding.llRestView.visibility = View.GONE
        binding.llExerciseView.visibility =View.VISIBLE
        if(exerciseTimer != null){
            exerciseTimer!!.cancel()
            exerciseProgress = 0
        }
        //TextToSpeech
        speakOut(exerciseList!![currentExercisePosition].getName())

        binding.ivImage.setImageResource(exerciseList!![currentExercisePosition].getImage())
        binding.tvExerciseName.text = exerciseList!![currentExercisePosition].getName()
        setExerciseProgressBar()
    }

    override fun onInit(status: Int) {
        if(status == TextToSpeech.SUCCESS){
            val result = tts!!.setLanguage(Locale.US)
            if(result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED){
                Log.e("TTS","The language specified is not supported")
            }
        }else{
            Log.e("TTS","Initialized failed")
        }
    }
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun speakOut(text: String){
        tts!!.speak(text,TextToSpeech.QUEUE_FLUSH,null,"")
    }

    private fun setupExerciseStatusRecyclerView(){
        binding.rvExerciseStatus.layoutManager = LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL, false)
        exerciseAdapter = ExerciseStatusAdapter(exerciseList!!,this )
        binding.rvExerciseStatus.adapter = exerciseAdapter
    }
    @RequiresApi(Build.VERSION_CODES.P)
    private fun customDialogForBackButton(){
        val customDialog = Dialog(this)
        customDialog.setContentView(R.layout.dialog_custom_back_confirmation)
        customDialog.requireViewById<View>(R.id.tvYes).setOnClickListener {
            finish()
            customDialog.dismiss()
        }
        customDialog.requireViewById<View>(R.id.tvNo).setOnClickListener {
            customDialog.dismiss()
        }
        customDialog.show()
    }
}