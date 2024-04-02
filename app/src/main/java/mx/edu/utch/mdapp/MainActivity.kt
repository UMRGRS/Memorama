package mx.edu.utch.mdapp

import android.content.DialogInterface
import android.graphics.Color
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.View.OnClickListener
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import mx.edu.utch.mdapp.databinding.ActivityMainBinding
import java.util.Collections
import kotlin.system.exitProcess

class MainActivity : AppCompatActivity() {
    private var clicked:Boolean = true
    private var turno:Boolean = true

    private var first_card: ImageView? = null
    private var first_image:Int? = null

    private var score1:Int = 0
    private var score2:Int = 0

    private var deck = ArrayList<Int>(
        listOf(
            R.drawable.cloud,
            R.drawable.day,
            R.drawable.moon,
            R.drawable.night,
            R.drawable.rain,
            R.drawable.rainbow,
            R.drawable.cloud,
            R.drawable.day,
            R.drawable.moon,
            R.drawable.night,
            R.drawable.rain,
            R.drawable.rainbow
        )
    )

    private var images:ArrayList<ImageView>? = null
    private var binding:ActivityMainBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding!!.root)

        images = ArrayList<ImageView>(
            listOf(
                binding!!.gameZone.im11,
                binding!!.gameZone.im12,
                binding!!.gameZone.im13,
                binding!!.gameZone.im21,
                binding!!.gameZone.im22,
                binding!!.gameZone.im23,
                binding!!.gameZone.im31,
                binding!!.gameZone.im32,
                binding!!.gameZone.im33,
                binding!!.gameZone.im41,
                binding!!.gameZone.im42,
                binding!!.gameZone.im43
                )
        )
        binding!!.scoreZone.mainActivityTvPlayer1.setTypeface(null,Typeface.BOLD_ITALIC)
        binding!!.scoreZone.mainActivityTvPlayer2.setTypeface(null,Typeface.BOLD_ITALIC)
        startOn()
        clickOn()
        binding!!.fabPrincipal.setOnClickListener{
            endOpt()
        }
        setSupportActionBar(binding!!.mainBottomAppBar)
    }

    private fun startOn() {
        if(turno){
            binding!!.scoreZone.mainActivityTvPlayer1.setBackgroundColor(Color.GREEN)
            binding!!.scoreZone.mainActivityTvScorePlayer1.setBackgroundColor(Color.GREEN)
            binding!!.scoreZone.mainActivityTvPlayer2.setBackgroundColor(Color.TRANSPARENT)
            binding!!.scoreZone.mainActivityTvScorePlayer2.setBackgroundColor(Color.TRANSPARENT)
        }
        else{
            binding!!.scoreZone.mainActivityTvPlayer1.setBackgroundColor(Color.TRANSPARENT)
            binding!!.scoreZone.mainActivityTvScorePlayer1.setBackgroundColor(Color.TRANSPARENT)
            binding!!.scoreZone.mainActivityTvPlayer2.setBackgroundColor(Color.GREEN)
            binding!!.scoreZone.mainActivityTvScorePlayer2.setBackgroundColor(Color.GREEN)
        }
    }
    private fun clickOn() {
        Collections.shuffle(deck)
        for (i in (0..<images!!.size)){
            images!![i]!!.setOnClickListener{
                images!![i]!!.setImageResource(deck[i])
                saveClick(images!![i]!!,deck[i])
            }
        }
    }
    private fun saveClick(img:ImageView, card:Int) {
        if(clicked){
            first_card = img
            first_image = card
            first_card!!.isEnabled = false
            clicked=!clicked
        }
        else{
            xtivate(false)
            var handler = Handler(Looper.getMainLooper())
            handler.postDelayed({
                if(card == first_image){
                    if(turno){
                        score1 += 1
                        binding!!.scoreZone.mainActivityTvScorePlayer1.text = "Puntos: $score1"
                    }
                    else{
                        score2 += 1
                        binding!!.scoreZone.mainActivityTvScorePlayer2.text = "Puntos: $score2"
                    }
                    first_card!!.isVisible = false
                    img.isVisible = false
                    if(endGame()){
                        if(score1>score2){
                            showDialogAlertSimple("Ganador!","Ganó el jugador 1")
                        }
                        else if(score2>score1){
                            showDialogAlertSimple("Ganador!","Ganó el jugador 2")
                        }
                        else if(score1==score2){
                            showDialogAlertSimple("No one wins!","Empate :c")
                        }
                    }
                    else{
                        startOn()
                        xtivate(true)
                    }
                }
                else{
                    first_card!!.setImageResource(R.drawable.reverso)
                    img!!.setImageResource(R.drawable.reverso)
                    first_card!!.isEnabled = true
                    turno = !turno
                    startOn()
                    xtivate(true)
                }
            }, 2000)
            clicked = !clicked
        }
    }

    private fun xtivate(b: Boolean) {
        for (i in (0..<images!!.size)){
            images!![i]!!.isEnabled = b
        }
    }
    private fun endGame():Boolean{
        for (i in (0..<images!!.size)){
            if(images!![i].isVisible){
                return false
            }
        }
        return true
    }
    private fun makeVisible(){
        for (i in (0..<images!!.size)){
            images!![i]!!.isVisible = true
            images!![i]!!.setImageResource(R.drawable.reverso)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.option_1 ->{
                restart()
                true
            }
            R.id.option_2->{
                endOpt()
                true
            }
            R.id.option_3->{
                exit()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun restart() {
        makeVisible()
        clickOn()
        xtivate(true)
        clicked = true
        turno = true
        score1 = 0
        score2 = 0
        binding!!.scoreZone.mainActivityTvScorePlayer1.text = "Puntos: $score1"
        binding!!.scoreZone.mainActivityTvScorePlayer2.text = "Puntos: $score2"
        startOn()
    }

    private fun endOpt() {
        AlertDialog.Builder(this)
            .setTitle("Resultados")
            .setMessage("Jugador 1: $score1 \nJugador 2: $score2")
            .setPositiveButton(
                getString(R.string.newgame),
                DialogInterface.OnClickListener { _, _ ->
                    restart()
                })
            .setNegativeButton(
                getString(R.string.salir),
                DialogInterface.OnClickListener { _, _ ->
                    exit()
                })
            .setCancelable(false)
            .show()
    }
    private fun exit(){
        showDialogAlertSimple("Salir", "¿Estas seguro?")
    }
    private fun endApp(){
        finishAffinity()
        exitProcess(0)
    }

    private fun showDialogAlertSimple(title:String, msg:String) {
        AlertDialog.Builder(this)
            .setTitle(title)
            .setMessage(msg)
            .setPositiveButton(
                getString(R.string.continuar),
                DialogInterface.OnClickListener { _, _ ->
                    //botón continuar pulsado
                })
            .setNegativeButton(
                getString(R.string.salir),
                DialogInterface.OnClickListener { _, _ ->
                    endApp()
                })
            .setCancelable(false)
            .show()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu_owo, menu)
        return true
    }
}