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
    var throwRound=0
    var isWinPopUp: String? = null
    var selectOnlyList = mutableListOf<Boolean>(false,false,false,false,false)

    var userImageList: MutableList<ImageView>? = null
    var computerImageList: MutableList<ImageView>? = null

    var dialogBox: Dialog? = null


    lateinit var throwButton : Button
    lateinit var scorebutton : Button
    lateinit var userScore : TextView
    lateinit var computerScore : TextView
    lateinit var roundID : TextView
    lateinit var computerwinsText: TextView
    lateinit var humanwinsText: TextView
    lateinit var targetscoreId : TextView
    lateinit var getusername : TextView



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_page)

        dialogBox = Dialog(this)

        throwButton = findViewById(R.id.throwbtn)
        scorebutton = findViewById(R.id.scorebtn)
        userScore = findViewById(R.id.userscore)
        computerScore = findViewById(R.id.computerscore)
        roundID = findViewById(R.id.roundid)
        computerwinsText = findViewById(R.id.com_winsId)
        humanwinsText = findViewById(R.id.hum_winsId)
        targetscoreId = findViewById(R.id.targetscoreId)
        getusername = findViewById(R.id.user)

        targetScore = Integer.parseInt(intent.getStringExtra("key"))
        val username = intent.getStringExtra("name")
        computerwins = intent.getIntExtra("computerwins",0)
        humanwins = intent.getIntExtra("humanwins",0)
        humanwins = intent.getIntExtra("humanwins",0)
        isHard =  intent.getBooleanExtra("isHard",false)

        userImageList=mutableListOf<ImageView>(findViewById(R.id.dice1),findViewById(R.id.dice2),findViewById(R.id.dice3),findViewById(R.id.dice4),findViewById(R.id.dice5))
        computerImageList= mutableListOf<ImageView>(findViewById(R.id.dice6),findViewById(R.id.dice7),findViewById(R.id.dice8),findViewById(R.id.dice9),findViewById(R.id.dice10))

        if (savedInstanceState != null) {
            computerscore = savedInstanceState.getInt("computerscore")
            userscore = savedInstanceState.getInt("userscore")
            roundNum = savedInstanceState.getInt("roundNum")
            computerwins=savedInstanceState.getInt("computerwins")
            humanwins=savedInstanceState.getInt("humanwins")
            isTied= savedInstanceState.getBoolean("isTied")
            throwRound=savedInstanceState.getInt("throwRound")
            isWinPopUp=savedInstanceState.getString("isWinPopUp")

            user= savedInstanceState.getIntegerArrayList("user") as MutableList<Int>
            computer= savedInstanceState.getIntegerArrayList("computer") as MutableList<Int>
            var tempRemoveList=savedInstanceState.getIntegerArrayList("tempRemoveList") as MutableList<Int>
            for (index in 0..4){
                if (tempRemoveList[index]==0){
                    selectOnlyList[0]=false
                }else{
                    selectOnlyList[index]=true
                }
            }
        }

        computerwinsText.setText("C:" + computerwins)
        humanwinsText.setText("H:"+ humanwins)

        //initialize
        targetscoreId.text = targetScore.toString()
        getusername.text = username.toString()
        for (index in 0..4){
            userImageList!![index].setOnClickListener {
                if(selectOnlyList[index]==false){
                    selectOnlyList[index]=true
                    userImageList!![index].setBackgroundColor(Color.RED)
                }else {
                    selectOnlyList[index]=false
                    userImageList!![index].setBackgroundColor(Color.TRANSPARENT)
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
                        userImageList!![index].isClickable=true
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
                    userImageList!![index].isClickable=false
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
                userImageList!![index].setBackgroundColor(Color.TRANSPARENT)

            }

            for (diceIndex in 0 until 5){
                setImage(computerImageList!![diceIndex],computer[diceIndex])
                setImage(userImageList!![diceIndex],user[diceIndex])
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
                userImageList!![index].setBackgroundColor(Color.TRANSPARENT)
                userImageList!![index].isClickable=false

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
                isWinPopUp="win"

                dialogBox?.setContentView(R.layout.activity_win_orlose)
                val resultTextView = dialogBox?.findViewById(R.id.resulttext) as TextView
                resultTextView.text = winMessage
                resultTextView.setTextColor(winColor)
                dialogBox?.show()
                dialogBox?.setCanceledOnTouchOutside(false)
                dialogBox?.setOnCancelListener {
                    val intent= Intent(this,Choices::class.java)
                    intent.putExtra("computerwins",computerwins)
                    intent.putExtra("humanwins",humanwins)

                    startActivity(intent)
                    //
                }
            } else if (userscore < computerscore) {
                computerwins+=1
                isWinPopUp="lost"


                dialogBox?.setCanceledOnTouchOutside(false)
                dialogBox?.setContentView(R.layout.activity_win_orlose)
                val resultTextView = dialogBox?.findViewById(R.id.resulttext) as TextView
                resultTextView.text = loseMessage
                resultTextView.setTextColor(loseColor)
                dialogBox?.show()
                dialogBox?.setOnCancelListener {
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

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("computerscore", computerscore)
        outState.putInt("userscore", userscore)
        outState.putInt("roundNum", roundNum)
        outState.putInt("computerwins", computerwins)
        outState.putInt("humanwins", humanwins)
        outState.putBoolean("isTied", isTied)
        outState.putIntegerArrayList("user", ArrayList(user))
        outState.putIntegerArrayList("computer", ArrayList(computer))

        outState.putInt("throwRound", throwRound)
        outState.putString("isWinPopUp", isWinPopUp)
        println(selectOnlyList)

        var tempRemoveList = mutableListOf<Int>()
        for (index in 0..4){
            if (selectOnlyList[index]==true){
                tempRemoveList.add(1)
            }else{
                tempRemoveList.add(0)
            }
        }
        println(tempRemoveList)
        outState.putIntegerArrayList("tempRemoveList", ArrayList(tempRemoveList))
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        computerscore = savedInstanceState.getInt("computerscore")
        userscore = savedInstanceState.getInt("userscore")
        roundNum = savedInstanceState.getInt("roundNum")
        computerwins=savedInstanceState.getInt("computerwins")
        humanwins=savedInstanceState.getInt("humanwins")
        isTied= savedInstanceState.getBoolean("isTied")
        throwRound=savedInstanceState.getInt("throwRound")
        isWinPopUp=savedInstanceState.getString("isWinPopUp")

        user= savedInstanceState.getIntegerArrayList("user") as MutableList<Int>
        computer= savedInstanceState.getIntegerArrayList("computer") as MutableList<Int>
        var tempRemoveList=savedInstanceState.getIntegerArrayList("tempRemoveList") as MutableList<Int>
        println(tempRemoveList)
        for (index in 0..4){
            if (tempRemoveList[index]==0){
                selectOnlyList[index]=false
            }else{
                selectOnlyList[index]=true
            }
        }
        println(selectOnlyList)

        whenRotateSet()
    }

    private fun whenRotateSet() {
        computerScore.setText(computerscore.toString())
        userScore.setText(userscore.toString())
        roundID.setText(roundNum.toString())

        if (!(computer.size==0)){
            //set images in screen
            for (diceIndex in 0..4) {
                setImage(computerImageList!![diceIndex],computer[diceIndex])
                setImage(userImageList!![diceIndex],user[diceIndex])
            }
        }

        if (throwRound==0){
            throwButton.setText("Throw")
        }   else if (throwRound==1){
            throwButton.setText("Rethrow")
        }else{
            throwButton.setText("Rethrow")
        }

        if (isTied || throwRound==0){
            scorebutton.isVisible=false
            //user image set touch false
            for (index in 0..4){
                userImageList!![index].isClickable=false
            }
        }else{
            scorebutton.isVisible=true
            //user image set touch false
            for (index in 0..4){
                userImageList!![index].isClickable=true
            }
        }

        println(selectOnlyList)
        for (imageIndex in 0 until 5){
            if (selectOnlyList[imageIndex]==false){
                userImageList?.get(imageIndex)?.setBackgroundColor(Color.TRANSPARENT)
            }else{
                userImageList?.get(imageIndex)?.setBackgroundColor(Color.RED)
            }
        }

        val winMessage = "You Win!"
        val loseMessage = "You Lose"
        val winColor = Color.GREEN
        val loseColor = Color.RED

        if (isWinPopUp=="win"){
            dialogBox?.setContentView(R.layout.activity_win_orlose)
            val resultTextView = dialogBox?.findViewById(R.id.resulttext) as TextView
            resultTextView.text = winMessage
            resultTextView.setTextColor(winColor)
            dialogBox?.show()
            dialogBox?.setCanceledOnTouchOutside(false)
            dialogBox?.setOnCancelListener {
                val intent= Intent(this,Choices::class.java)
                intent.putExtra("computerwins",computerwins)
                intent.putExtra("humanwins",humanwins)

                startActivity(intent)
                //
            }
        }else if (isWinPopUp=="lost"){
            dialogBox?.setContentView(R.layout.activity_win_orlose)
            val resultTextView = dialogBox?.findViewById(R.id.resulttext) as TextView
            resultTextView.text = loseMessage
            resultTextView.setTextColor(loseColor)
            dialogBox?.show()
            dialogBox?.setCanceledOnTouchOutside(false)
            dialogBox?.setOnCancelListener {
                val intent= Intent(this,Choices::class.java)
                intent.putExtra("computerwins",computerwins)
                intent.putExtra("humanwins",humanwins)

                startActivity(intent)
                //
            }
        }
    }

    override fun onPause() {
        super.onPause()
        if (dialogBox != null && dialogBox!!.isShowing()) {
            dialogBox!!.dismiss()
        }
    }


}







