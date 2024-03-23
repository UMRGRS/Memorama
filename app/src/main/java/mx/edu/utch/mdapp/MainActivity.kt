package mx.edu.utch.mdapp

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
import androidx.core.view.isVisible
import mx.edu.utch.mdapp.databinding.ActivityMainBinding
import java.util.Collections

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
        Collections.shuffle(deck)
        startOn()
        clickOn()
        binding!!.fabPrincipal.setOnClickListener{
            Toast.makeText(this,"Fab prin", Toast.LENGTH_SHORT).show()
            binding!!.gameZone.im11.setImageResource(R.drawable.day)
        }
        
        setSupportActionBar(binding!!.mainBottomAppBar)
    }

    private fun startOn() {
        if(turno){
            binding!!.scoreZone.mainActivityTvPlayer1.setBackgroundColor(Color.GREEN)
            binding!!.scoreZone.mainActivityTvPlayer2.setBackgroundColor(Color.TRANSPARENT)
        }
        else{
            binding!!.scoreZone.mainActivityTvPlayer1.setBackgroundColor(Color.TRANSPARENT)
            binding!!.scoreZone.mainActivityTvPlayer2.setBackgroundColor(Color.GREEN)
        }
        binding!!.scoreZone.mainActivityTvPlayer1.setTypeface(null,Typeface.BOLD_ITALIC)
        binding!!.scoreZone.mainActivityTvPlayer2.setTypeface(null,Typeface.BOLD_ITALIC)
    }
    private fun clickOn() {
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
                    first_card!!.isVisible = false
                    img.isVisible = false
                    startOn()
                    xtivate(true)
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.option_1 ->{
                newGame()
                true
            }
            R.id.option_2->{
                showHelp()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showHelp() {
        Log.e("Click","opt 1")
    }

    private fun newGame() {
        Log.e("Click","opt 2")
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu_owo, menu)
        return true
    }
}