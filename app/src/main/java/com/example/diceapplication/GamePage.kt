package com.example.diceapplication

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Switch
import android.widget.TextView
import androidx.core.view.isVisible
import kotlin.random.Random

class GamePage : AppCompatActivity() {

    val userImageList=null
    val computerImageList=null
    var userscore = 0
    var computerscore = 0
    var roundNum = 0
    var user: MutableList<Int> = mutableListOf()
    var computer:MutableList<Int> = mutableListOf()
    var targetScore = 101
    var isTied = false
    var computerwins = 0
    var humanwins = 0
    var isHard = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_page)
        val data = Integer.parseInt(intent.getStringExtra("key"))
        val targetscoreId : TextView = findViewById(R.id.targetscoreId)
        val getusername : TextView = findViewById(R.id.user)
        val username = intent.getStringExtra("name")

        targetscoreId.text = data.toString()
        getusername.text = username.toString()
        computerwins = intent.getIntExtra("computerwins",0)
        humanwins = intent.getIntExtra("humanwins",0)
        humanwins = intent.getIntExtra("humanwins",0)
        isHard =  intent.getBooleanExtra("isHard",false)
        println(isHard)

        targetScore=data

        var throwRound=0


        val throwButton : Button = findViewById(R.id.throwbtn)
        val scorebutton : Button = findViewById(R.id.scorebtn)
        val userScore : TextView = findViewById(R.id.userscore)
        val computerScore : TextView = findViewById(R.id.computerscore)
        val roundID : TextView = findViewById(R.id.roundid)
        val computerwinsText: TextView = findViewById(R.id.com_winsId)
        val humanwinsText: TextView = findViewById(R.id.hum_winsId)

        val userImageList=mutableListOf<ImageView>(findViewById(R.id.dice1),findViewById(R.id.dice2),findViewById(R.id.dice3),findViewById(R.id.dice4),findViewById(R.id.dice5))
        val computerImageList= mutableListOf<ImageView>(findViewById(R.id.dice6),findViewById(R.id.dice7),findViewById(R.id.dice8),findViewById(R.id.dice9),findViewById(R.id.dice10))
        var selectOnlyList = mutableListOf<Boolean>(false,false,false,false,false)

        computerwinsText.setText("C:" + computerwins)
        humanwinsText.setText("H:"+ humanwins)

        //initialize
        for (index in 0..4){
            userImageList[index].setOnClickListener {
                if(selectOnlyList[index]==false){
                    selectOnlyList[index]=true
                    userImageList[index].setBackgroundColor(Color.RED)
                }else {
                    selectOnlyList[index]=false
                    userImageList[index].setBackgroundColor(Color.TRANSPARENT)
                }
            }
        }
        scorebutton.isVisible=false
        throwButton.setOnClickListener {

            if (throwRound==0){
                computer=randomGenarateNumbers()
                if (isTied==false){
                    throwButton.setText("Rethrow")
                    for (index in 0..4){
                        userImageList[index].isClickable=true
                    }
                    throwRound++
                    scorebutton.isVisible=true
                }
                user=randomGenarateNumbers()

            }else if (throwRound==1){

                throwButton.setText("Rethrow")

                throwRound++
                for(index in  0..4){
                    if(selectOnlyList[index]==false){
                        val num1 = (1..6).shuffled().last()
                        user[index]=num1

                    }
                }

            }else {
                if (isHard){
                    hardComputerPlayerStrategy()
                }else{
                    ComputerrandomReroll()
                    ComputerrandomReroll()
                }

                throwButton.setText("Throw")


                throwRound=0
                scoreUpdate()
                computerScore.setText(computerscore.toString())
                userScore.setText(userscore.toString())
                scorebutton.isVisible=false
                roundNum+=1
                roundID.text=roundNum.toString()

                for (index in 0..4){
                    userImageList[index].isClickable=false
                }
                for(index in  0..4){
                    if(selectOnlyList[index]==false){
                        val num1 = (1..6).shuffled().last()
                        user[index]=num1

                    }

                }
                winMatchCheck()
            }

            if(isTied==true){
                scoreUpdate()
                computerScore.setText(computerscore.toString())
                userScore.setText(userscore.toString())
                roundNum+=1
                roundID.text=roundNum.toString()
                winMatchCheck()

            }
            selectOnlyList = mutableListOf<Boolean>(false,false,false,false,false)

            for (index in 0..4){
                userImageList[index].setBackgroundColor(Color.TRANSPARENT)

            }

            for (diceIndex in 0 until 5){
                setImage(computerImageList[diceIndex],computer[diceIndex])
                setImage(userImageList[diceIndex],user[diceIndex])
            }

        }

        scorebutton.setOnClickListener{
            if (isHard){
                hardComputerPlayerStrategy()
                print("hard")
            }else{
                ComputerrandomReroll()
                ComputerrandomReroll()
                print("ezy")
            }


            scoreUpdate()
            computerScore.setText(computerscore.toString())
            userScore.setText(userscore.toString())
            scorebutton.isVisible=false
            throwRound=0
            throwButton.setText("Throw")
            roundNum+=1
            roundID.text=roundNum.toString()


            selectOnlyList = mutableListOf<Boolean>(false,false,false,false,false)
            for (index in 0..4){
                userImageList[index].setBackgroundColor(Color.TRANSPARENT)
                userImageList[index].isClickable=false

            }
            winMatchCheck()
        }

    }
    // check the match (win or lose )

    private fun winMatchCheck() {
        val winMessage = "You Win!"
        val loseMessage = "You Lose"
        val winColor = Color.GREEN
        val loseColor = Color.RED

        if (userscore >= targetScore || computerscore >= targetScore) {
            if (userscore > computerscore) {
                humanwins+=1

                val dialogBox = Dialog(this)
                dialogBox.setContentView(R.layout.activity_win_orlose)
                val resultTextView = dialogBox.findViewById(R.id.resulttext) as TextView
                resultTextView.text = winMessage
                resultTextView.setTextColor(winColor)
                dialogBox.show()
                dialogBox.setCanceledOnTouchOutside(false)
                dialogBox.setOnCancelListener {
                    val intent= Intent(this,Choices::class.java)
                    intent.putExtra("computerwins",computerwins)
                    intent.putExtra("humanwins",humanwins)

                    startActivity(intent)
                   //
                }
            } else if (userscore < computerscore) {
                computerwins+=1


                val dialogBox = Dialog(this)
                dialogBox.setCanceledOnTouchOutside(false)
                dialogBox.setContentView(R.layout.activity_win_orlose)
                val resultTextView = dialogBox.findViewById(R.id.resulttext) as TextView
                resultTextView.text = loseMessage
                resultTextView.setTextColor(loseColor)
                dialogBox.show()
                dialogBox.setOnCancelListener {
                    val intent= Intent(this,Choices::class.java)
                    intent.putExtra("computerwins",computerwins)
                    intent.putExtra("humanwins",humanwins)

                    startActivity(intent)

                }

            } else {
                isTied=true
            }
        }
    }

    private fun setImage(image: ImageView, randomValue: Int) {
        image.isVisible=true
        if (randomValue==1){
            image.setImageResource(R.drawable.dice_1)
        }else if (randomValue==2){
            image.setImageResource(R.drawable.dice_2)
        }else if (randomValue==3){
            image.setImageResource(R.drawable.dice_3)
        }else if (randomValue==4){
            image.setImageResource(R.drawable.dice_4)
        }else if (randomValue==5){
            image.setImageResource(R.drawable.dice_5)
        }else if (randomValue==6){
            image.setImageResource(R.drawable.dice_6)
        }
    }

    private fun randomGenarateNumbers(): MutableList<Int> {
        val tempList : MutableList<Int> = mutableListOf()

        for (count in 0..4){
            val random1 = (1..6).shuffled().last()
            tempList.add(random1)

        }
        return tempList
    }
    fun scoreUpdate(){
        for (count in 0..4){
            userscore += user[count]
            computerscore += computer[count]
        }
    }

    //computer strategy
    fun ComputerrandomReroll(){
        val randomBoolean = if (Random.nextInt(0, 2) == 0) false else true
        val randomComputer =  mutableListOf<Boolean>(if (Random.nextInt(0, 2) == 0) false else true,if (Random.nextInt(0, 2) == 0) false else true,if (Random.nextInt(0, 2) == 0) false else true,if (Random.nextInt(0, 2) == 0) false else true,if (Random.nextInt(0, 2) == 0) false else true)

        if (randomBoolean){
            for(index in  0..4){
                if(randomComputer[index]==false){
                    val num1 = (1..6).shuffled().last()
                    computer[index]=num1
                }
            }
        }

    }
    private fun hardComputerPlayerStrategy() {
        var tempcomputerscore=computerscore
        var gap = userscore - tempcomputerscore

        //throw
        if (gap > 20){
            //high
            //checking high values
            for (count in  0 ..4){
                var randomindex = Random.nextInt(5)
                if (computer[randomindex] <= 3){
                    computer[randomindex]= Random.nextInt(6) + 1
                }
            }
        }else if(gap < 20){
            ComputerrandomReroll()
            ComputerrandomReroll()
        }else {
            //normal
            ComputerrandomReroll()
            for (count in  0 ..4){
                var randomindex = Random.nextInt(5)
                if (computer[randomindex] <= 2){
                    computer[randomindex]=Random.nextInt(6) + 1
                }
            }
        }

        for (i in 0..4) {
            tempcomputerscore += computer[i]
        }

        //second throw
        if ((tempcomputerscore<(userscore+10))){
            var gap = userscore - tempcomputerscore

            if (gap > 20){
                //high
                //checking high values
                for (count in  0 ..4){
                    var randomindex = Random.nextInt(5) + 1
                    if (computer[randomindex] <= 3){
                        computer[randomindex]= Random.nextInt(6) + 1
                    }
                }
            }else if(gap < 20){
                ComputerrandomReroll()
                ComputerrandomReroll()
            }else {
                //normal
                //normal
                ComputerrandomReroll()
                for (count in  0 ..4){
                    var randomindex = Random.nextInt(5) + 1
                    if (computer[randomindex] <= 2){
                        computer[randomindex]= Random.nextInt(6) + 1
                    }
                }
            }
        }
    }


}







