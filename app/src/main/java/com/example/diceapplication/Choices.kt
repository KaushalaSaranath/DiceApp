package com.example.diceapplication

import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Switch
import android.widget.TextView

class Choices : AppCompatActivity() {

    var computerwins = 0
    var humanwins = 0
    var isHard = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rules)

        val about_button: Button = findViewById(R.id.aboutid)
        val dialogbox = Dialog(this)

        computerwins = intent.getIntExtra("computerwins",0)
        humanwins = intent.getIntExtra("humanwins",0)


        about_button.setOnClickListener {
            dialogbox.setContentView(R.layout.activity_profile_pop_up)
            dialogbox.show()
        }


        val new_button: Button = findViewById(R.id.newgame)


        new_button.setOnClickListener {

            dialogbox.setContentView(R.layout.activity_user_name_and_target)
            val scorecount = dialogbox.findViewById(R.id.scoreid) as EditText
            val getusername = dialogbox.findViewById(R.id.getusername) as EditText
            val nextButton = dialogbox.findViewById(R.id.setupNext) as Button
            val hardswitch = dialogbox.findViewById(R.id.switch1) as Switch

            hardswitch?.setOnCheckedChangeListener { _, isChecked ->
                isHard = isChecked
            }

            nextButton.setOnClickListener {
                val intent= Intent(this,GamePage::class.java)
                intent.putExtra("key",scorecount.text.toString())
                if (scorecount.text.toString().isEmpty()){
                    val errorMessage = "enter value"
                    scorecount.setHint(errorMessage)

                }else{
                    intent.putExtra("computerwins",computerwins)
                    intent.putExtra("humanwins",humanwins)
                    intent.putExtra("isHard",isHard)
                    intent.putExtra("name",getusername.text.toString())
                    startActivity(intent)
                    dialogbox.dismiss()
                }
            }

            dialogbox.show()
        }

    }
}