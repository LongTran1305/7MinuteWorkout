package com.longtran.a7minuteworkout

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.longtran.a7minuteworkout.databinding.ActivityBmiBinding
import java.math.BigDecimal
import java.math.RoundingMode

class BMIActivity : AppCompatActivity() {
    private val METRIC_UNITS_VIEW = "METRIC_UNIT_VIEW"
    private val US_UNITS_VIEW = "US_UNIT_VIEWS"
    var currentVisibleView : String = METRIC_UNITS_VIEW
    private lateinit var binding: ActivityBmiBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBmiBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        setSupportActionBar(binding.toolbarBmiActivity)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "History"
        binding.toolbarBmiActivity.setNavigationOnClickListener{
            onBackPressed()
        }
        binding.btnCalculateUnits.setOnClickListener{
            if(currentVisibleView == METRIC_UNITS_VIEW){
                if (validateMetricUnits()){
                    val heightValue : Float = binding.etMetricUnitHeight.text.toString().toFloat() / 100
                    val weightValue : Float = binding.etMetricUnitWeight.text.toString().toFloat()
                    val bmi = weightValue / ( heightValue*heightValue)
                    Log.i("BMIMetric",bmi.toString())
                    displayBMIResult(bmi)
                }else{
                    Toast.makeText(this@BMIActivity,"Please enter valid values",Toast.LENGTH_LONG).show()
                }
            }else{
                if(validateUsUnits()){
                    val usUnitHeightValueFeet : String = binding.etUsUnitHeightFeet.text.toString()
                    val usUnitHeightValueInch : String = binding.etUsUnitHeightInch.text.toString()
                    val usUnitWeightValue : Float = binding.etUsUnitWeight.text.toString().toFloat()

                    val heightValue = usUnitHeightValueInch.toFloat() + usUnitHeightValueFeet.toFloat() * 12
                    val bmi = 703 * (usUnitWeightValue / ( heightValue * heightValue))
                    Log.i("BMIUs",bmi.toString())
                    displayBMIResult(bmi)
                }else{
                    Toast.makeText(this@BMIActivity,"Please enter valid values",Toast.LENGTH_LONG).show()
                }
            }
        }
        makeVisibleMetricUnitsView()
        binding.rgUnits.setOnCheckedChangeListener { group, checkedId ->
            if(checkedId == R.id.rbMetricUnits){
                makeVisibleMetricUnitsView()
            }else{
                makeVisibleUsUnitsView()
            }
        }
    }
    private fun makeVisibleUsUnitsView(){
        currentVisibleView = US_UNITS_VIEW
        binding.tilMetricUnitWeight.visibility = View.GONE
        binding.tilMetricUnitHeight.visibility = View.GONE

        binding.etUsUnitWeight.text!!.clear()
        binding.etUsUnitHeightFeet.text!!.clear()
        binding.etUsUnitHeightInch.text!!.clear()
        binding.tilUsUnitWeight.visibility = View.VISIBLE
        binding.llUsUnitsHeight.visibility = View.VISIBLE

        binding.llDisplayBMIResult.visibility =  View.INVISIBLE

    }
    private fun makeVisibleMetricUnitsView(){
        currentVisibleView = METRIC_UNITS_VIEW
        binding.tilMetricUnitWeight.visibility = View.VISIBLE
        binding.tilMetricUnitHeight.visibility = View.VISIBLE

        binding.etMetricUnitHeight.text!!.clear()
        binding.etMetricUnitWeight.text!!.clear()

        binding.tilUsUnitWeight.visibility = View.GONE
        binding.llUsUnitsHeight.visibility = View.GONE

        binding.llDisplayBMIResult.visibility =  View.INVISIBLE
    }


    private fun displayBMIResult(bmi: Float){
        val bmiLabel : String
        val bmiDescription : String

        if (bmi.compareTo(15f) <= 0) {
            bmiLabel = "Very severely underweight"
            bmiDescription = "Oops! You really need to take better care of yourself! Eat more!"
        } else if (bmi.compareTo(15f) > 0 && bmi.compareTo(16f) <= 0
        ) {
            bmiLabel = "Severely underweight"
            bmiDescription = "Oops!You really need to take better care of yourself! Eat more!"
        } else if (bmi.compareTo(16f) > 0 && bmi.compareTo(18.5f) <= 0
        ) {
            bmiLabel = "Underweight"
            bmiDescription = "Oops! You really need to take better care of yourself! Eat more!"
        } else if (bmi.compareTo(18.5f) > 0 && bmi.compareTo(25f) <= 0
        ) {
            bmiLabel = "Normal"
            bmiDescription = "Congratulations! You are in a good shape!"
        } else if (bmi.compareTo(25f) > 0 && bmi.compareTo(30f) <= 0
        ) {
            bmiLabel = "Overweight"
            bmiDescription = "Oops! You really need to take care of your yourself! Workout maybe!"
        } else if (bmi.compareTo(30f) > 0 && bmi.compareTo(35f) <= 0
        ) {
            bmiLabel = "Obese Class | (Moderately obese)"
            bmiDescription = "Oops! You really need to take care of your yourself! Workout maybe!"
        } else if (bmi.compareTo(35f) > 0 && bmi.compareTo(40f) <= 0
        ) {
            bmiLabel = "Obese Class || (Severely obese)"
            bmiDescription = "OMG! You are in a very dangerous condition! Act now!"
        } else {
            bmiLabel = "Obese Class ||| (Very Severely obese)"
            bmiDescription = "OMG! You are in a very dangerous condition! Act now!"
        }
        binding.llDisplayBMIResult.visibility = View.VISIBLE

        // This is used to round the result value to 2 decimal values after "."
        val bmiValue = BigDecimal(bmi.toDouble()).setScale(2, RoundingMode.HALF_EVEN).toString()

        binding.tvBMIValue.text = bmiValue // Value is set to TextView
        binding.tvBMIType.text = bmiLabel // Label is set to TextView
        binding.tvBMIDescription.text = bmiDescription // Description is set to TextView
    }

    private fun validateMetricUnits(): Boolean{
        var isValid =  true
        if(binding.etMetricUnitWeight.text.toString().isEmpty())
            isValid = false
        else if(binding.etMetricUnitHeight.text.toString().isEmpty())
            isValid = false
        return isValid
    }
    private fun validateUsUnits(): Boolean{
        var isValid =  true
        if(binding.etUsUnitHeightFeet.text.toString().isEmpty())
            isValid = false
        else if(binding.etUsUnitWeight.text.toString().isEmpty())
            isValid = false
        else if(binding.etUsUnitHeightInch.text.toString().isEmpty())
            isValid = false
        return isValid
    }
}