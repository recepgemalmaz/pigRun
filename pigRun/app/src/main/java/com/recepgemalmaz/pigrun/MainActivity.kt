package com.recepgemalmaz.pigrun

import android.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import com.recepgemalmaz.pigrun.databinding.ActivityMainBinding
import java.util.Timer
import java.util.TimerTask
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding



    private var isPigClickable = true
    private var pigScore = 0
    private val timer = Timer()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        hideAllPigs()
        startPigTimer()

        object : CountDownTimer(10000, 1000) {
            override fun onTick(p0: Long) {
                binding.textView1.text = (p0 / 1000).toString()
            }

            override fun onFinish() {
                save(view)
            }
        }.start()
    }

    private fun startPigTimer() {
        val interval = 1000L // 1 saniye
        timer.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                runOnUiThread {
                    showRandomPig()
                }
            }
        }, 0, interval)
    }

    private fun showRandomPig() {
        val pigIds = listOf(
            binding.imageView1.id,binding.imageView2.id,binding.imageView3.id,
            binding.imageView4.id,binding.imageView5.id,binding.imageView6.id,
            binding.imageView7.id,binding.imageView8.id,binding.imageView9.id)
        hideAllPigs()

        val randomIndex = Random.nextInt(0, pigIds.size)
        val visiblePigId = pigIds[randomIndex]
        binding.root.findViewById<ImageView>(visiblePigId).visibility = View.VISIBLE

        Timer().schedule(object : TimerTask() {
            override fun run() {
                runOnUiThread {
                    binding.root.findViewById<ImageView>(visiblePigId).visibility = View.INVISIBLE
                }
            }
        }, 1000)
    }

    private fun hideAllPigs() {
        val pigIds = listOf(
            binding.imageView1.id,binding.imageView2.id,binding.imageView3.id,
            binding.imageView4.id,binding.imageView5.id,binding.imageView6.id,
            binding.imageView7.id,binding.imageView8.id,binding.imageView9.id)
        for (pigId in pigIds) {
            binding.root.findViewById<ImageView>(pigId).visibility = View.INVISIBLE
        }
    }

    fun pigClicked(view: View) {
        if (isPigClickable) {
            pigScore++
            binding.textView2.text = "Score: $pigScore"
            isPigClickable = false
            Timer().schedule(object : TimerTask() {
                override fun run() {
                    isPigClickable = true
                }
            }, 1000)
        }
    }

    fun save(view : View) {

        val alert = AlertDialog.Builder(this@MainActivity)
        alert.setTitle("Game Over")
        alert.setMessage("Do you want to play again?")

        alert.setPositiveButton("Yes") {dialog, which ->

            //sure sifirlanip oyun yeniden baslamasi
            pigScore = 0
            binding.textView2.text = "Score: $pigScore"
            //sure bastan baslasin
            object : CountDownTimer(10000, 1000) {
                override fun onTick(p0: Long) {
                    binding.textView1.text = (p0 / 1000).toString()
                }

                override fun onFinish() {
                    save(view)
                }
            }.start()

        }
        alert.setNegativeButton("No") {dialog, which ->
            //oyun kapatilsin
            finish()
        }

        //secim yapilmadan cikilmasin
        alert.setCancelable(false)

        alert.show()

    }

}






