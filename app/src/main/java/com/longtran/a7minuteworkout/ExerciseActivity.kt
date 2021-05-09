package com.longtran.a7minuteworkout

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.Toast
import com.longtran.a7minuteworkout.databinding.ActivityExerciseBinding

class ExerciseActivity : AppCompatActivity() {
    private var restTimer : CountDownTimer? = null
    private var restProgress = 0
    private var restTimerDuration : Long = 10
    private var exerciseTimer : CountDownTimer? = null
    private var exerciseProgress = 0
    private var exerciseTimerDuration : Long = 30
    private lateinit var binding: ActivityExerciseBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExerciseBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        setSupportActionBar(binding.toolbarExerciseActivity)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbarExerciseActivity.setNavigationOnClickListener {
            onBackPressed()
        }
        binding.llRestView2.visibility = View.GONE
        setUpRestView()

    }

    override fun onDestroy() {
        if(restTimer != null){
            restTimer!!.cancel()
            restProgress = 0
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
           override fun onFinish() {
               Toast.makeText(this@ExerciseActivity,"Here now we will start the exercise",Toast.LENGTH_LONG).show()
               binding.llRestView.visibility = View.GONE
               binding.llRestView2.visibility =View.VISIBLE
               setUpExerciseView()
           }
       }.start()
    }
    private fun setUpRestView(){
        if(restTimer != null){
            restTimer!!.cancel()
            restProgress = 0
        }
        setRestProgressBar()
    }
    private fun setExerciseProgressBar(){
        binding.progressBar2.progress = exerciseProgress
        restTimer = object : CountDownTimer(exerciseTimerDuration * 1000,1000){
            override fun onTick(millisUntilFinished: Long) {
                exerciseProgress++
                binding.progressBar2.progress = exerciseTimerDuration.toInt()-exerciseProgress
                binding.tvTimer2.text = (exerciseTimerDuration.toInt()-exerciseProgress).toString()
            }
            override fun onFinish() {
                Toast.makeText(this@ExerciseActivity,"Here now we will start the exercise",Toast.LENGTH_LONG).show()
            }
        }.start()
    }
    private fun setUpExerciseView(){
        if(restTimer != null){
            exerciseTimer!!.cancel()
            exerciseProgress = 0
        }
        setExerciseProgressBar()
    }
}