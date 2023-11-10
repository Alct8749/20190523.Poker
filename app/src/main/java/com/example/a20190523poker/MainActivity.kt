package com.example.a20190523poker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.a20190523poker.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var main: ActivityMainBinding
    private lateinit var model: CardDealerViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        main = ActivityMainBinding.inflate(layoutInflater)
        setContentView(main.root)

        set_image() // 이미지 설정

        main.btnShuffle.setOnClickListener {
        model.shuffle()
        }
    }

    private fun set_image()
    {

        model = ViewModelProvider(this)[CardDealerViewModel::class.java]
        model.cards.observe(this, Observer {
            //조커모양으로 초기화
            val res = IntArray(5)
            for (i in it.indices) {

                if(it[i]==-1)
                {
                    res[i] = R.drawable.c_black_joker
                }
                else{
                    res[i] = resources.getIdentifier(
                        getCardName(it[i]),
                        "drawable",
                        packageName
                    )
                }
                
            } 

            main.card1.setImageResource(res[0])
            main.card11.setImageResource(res[1])
            main.card111.setImageResource(res[2])
            main.card2.setImageResource(res[3])
            main.card22.setImageResource(res[4])
        })
    }



    private fun getCardName(c: Int) : String {
        // val에서 var로 변경
        var shape = when (c / 13) {
            0 -> "spades"
            1 -> "diamonds"
            2 -> "hearts"
            3 -> "clubs"
            else -> "error"
        }

        val number = when (c % 13) {
            0 -> "ace"
            in 1..9 -> (c % 13 + 1).toString()
            10 -> {
                shape = shape.plus("2")
                "jack"
            }
            11 -> {
                shape = shape.plus("2")
                "queen"
            }
            12 -> {
                shape = shape.plus("2")
                "king"
            }
            else -> "error"
        }

        // 이 방법이 더 나을 듯
//        if (c % 13 in 10..12)
//            return "c_${number}_of_${shape}2"
//        else
//            return "c_${number}_of_${shape}"

        return "c_${number}_of_${shape}"
    }

}