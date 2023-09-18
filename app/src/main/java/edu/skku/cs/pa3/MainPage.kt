package edu.skku.cs.pa3

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.RadioButton
import android.widget.Toast

class MainPage : AppCompatActivity() {

    companion object {
        var isweightloss = "false"
        var ismild = "false"
        var isnormal = "false"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_page)

        val rb_weightloss = findViewById<RadioButton>(R.id.radioButton_weightloss)
        val rb_weightgain = findViewById<RadioButton>(R.id.radioButton_weightgain)
        val rb_mild = findViewById<RadioButton>(R.id.radioButton_mild)
        val rb_normal = findViewById<RadioButton>(R.id.radioButton_normal)
        val rb_extreme = findViewById<RadioButton>(R.id.radioButton_extreme)
        val btn_confirm = findViewById<Button>(R.id.button_confirmmain)

        btn_confirm.setOnClickListener {
            val intent = Intent(this, ResultPage::class.java).apply{
                if(!rb_weightloss.isChecked&&!rb_weightgain.isChecked) {
                    Toast.makeText(applicationContext, "Please check any blank boxes!!", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                else if(!rb_mild.isChecked&&!rb_normal.isChecked&&!rb_extreme.isChecked) {
                    Toast.makeText(applicationContext, "Please check any blank boxes!!", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                if(rb_weightloss.isChecked) {
                    putExtra(isweightloss, true)
                }
                else{
                    putExtra(isweightloss, false)
                }
                if(rb_mild.isChecked) {
                    putExtra(ismild, true)
                    putExtra(isnormal, false)
                }
                else if(rb_normal.isChecked) {
                    putExtra(ismild, false)
                    putExtra(isnormal, true)
                }
                else{
                    putExtra(ismild, false)
                    putExtra(isnormal, false)
                }
            }
            startActivity(intent)
        }
    }
}