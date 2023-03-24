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
    var aboutdialogbox: Dialog? = null
    var gamedialogbox: Dialog? = null
    var isNewgamePopup = false
    var isAboutPopup = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rules)

        val about_button: Button = findViewById(R.id.aboutid)
        aboutdialogbox = Dialog(this)
        gamedialogbox = Dialog(this)

        computerwins = intent.getIntExtra("computerwins",0)
        humanwins = intent.getIntExtra("humanwins",0)


        about_button.setOnClickListener {
            isAboutPopup = true
            aboutdialogbox!!.setContentView(R.layout.activity_profile_pop_up)
            aboutdialogbox!!.setOnCancelListener{
                isAboutPopup=false
            }
            aboutdialogbox!!.show()
        }

        val new_button: Button = findViewById(R.id.newgame)

        new_button.setOnClickListener {
            isNewgamePopup = true
            gamedialogbox!!.setContentView(R.layout.activity_user_name_and_target)
            val scorecount = gamedialogbox!!.findViewById(R.id.scoreid) as EditText
            val getusername = gamedialogbox!!.findViewById(R.id.getusername) as EditText
            val nextButton = gamedialogbox!!.findViewById(R.id.setupNext) as Button
            val hardswitch = gamedialogbox!!.findViewById(R.id.switch1) as Switch


            hardswitch.setOnCheckedChangeListener { _, isChecked ->
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
                    gamedialogbox!!.dismiss()
                }
            }
            gamedialogbox!!.setOnCancelListener{
                isNewgamePopup=false
            }
            gamedialogbox!!.show()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("computerwins", computerwins)
        outState.putInt("humanwins", humanwins)
        outState.putBoolean("isNewgamePopup", isNewgamePopup)
        outState.putBoolean("isAboutPopup", isAboutPopup)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        computerwins = savedInstanceState.getInt("computerWins")
        humanwins = savedInstanceState.getInt("userWins")
        isNewgamePopup = savedInstanceState.getBoolean("isNewgamePopup")
        isAboutPopup = savedInstanceState.getBoolean("isAboutPopup")

        whenRotateSet()
    }

    private fun whenRotateSet() {
        if (isAboutPopup){
            aboutdialogbox!!.setContentView(R.layout.activity_profile_pop_up)
            aboutdialogbox!!.setOnCancelListener{
                isAboutPopup=false
            }
            aboutdialogbox!!.show()
        }
        if (isNewgamePopup){
            gamedialogbox!!.setContentView(R.layout.activity_user_name_and_target)
            val scorecount = gamedialogbox!!.findViewById(R.id.scoreid) as EditText
            val getusername = gamedialogbox!!.findViewById(R.id.getusername) as EditText
            val nextButton = gamedialogbox!!.findViewById(R.id.setupNext) as Button
            val hardswitch = gamedialogbox!!.findViewById(R.id.switch1) as Switch


            hardswitch.setOnCheckedChangeListener { _, isChecked ->
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
                    gamedialogbox!!.dismiss()
                }
            }
            gamedialogbox!!.setOnCancelListener{
                isNewgamePopup=false
            }
            gamedialogbox!!.show()
        }
    }

    override fun onPause() {
        super.onPause()

        if (aboutdialogbox != null && aboutdialogbox!!.isShowing()) {
            aboutdialogbox!!.dismiss()
        }
        if (gamedialogbox != null && gamedialogbox!!.isShowing()) {
            gamedialogbox!!.dismiss()
        }
    }
}