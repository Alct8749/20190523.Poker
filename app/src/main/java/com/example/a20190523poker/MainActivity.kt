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

            val cardList: List<Int> = it.toList()

            val pokerHand = determinePokerHand(cardList)
            main.jokboText.text = "포커 족보: $pokerHand"


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
    fun determinePokerHand(cards: List<Int>): String {
        val counts = mutableMapOf<Int, Int>()

        for (card in cards) {
            val rank = card % 13
            counts[rank] = counts.getOrDefault(rank, 0) + 1
        }

        val numPairs = counts.values.count { it == 2 }
        val numThreeOfAKind = counts.values.count { it == 3 }
        val numFourOfAKind = counts.values.count { it == 4 }

        val hasFlush = cards.all { it / 13 == cards[0] / 13 }

        val isStraight = counts.keys.sorted().let {
            it.size == 5 && (it[4] - it[0] == 4 || it == listOf(0, 9, 10, 11, 12))
        }

        // 백 스트레이트 플러시 확인
        val hasBackStraightFlush = counts.keys.sorted().let {
            it.size == 5 && it == listOf(0, 1, 2, 3, 4)
        }

        // 로열 스트레이트 플러시 확인
        val hasRoyalStraightFlush = counts.keys.sorted().let {
            it.size == 5 && it == listOf(0, 9, 10, 11, 12)
        }

        // 백 스트레이트 확인
        val hasBackStraight = counts.keys.sorted() == listOf(0, 1, 2, 3, 4)

        // 마운틴 확인
        val hasMountain = counts.keys.sorted() == listOf(0, 9, 10, 11, 12)

        return when {
            isStraight && hasFlush -> "스트레이트 플러시"
            hasBackStraightFlush -> "백 스트레이트 플러시"
            numFourOfAKind == 4 -> "포카드"
            numThreeOfAKind == 3 && numPairs == 2 -> "풀하우스"
            hasFlush -> "플러시"
            isStraight -> "스트레이트"
            numThreeOfAKind == 3 -> "트리플"
            numPairs == 2 -> "투 페어"
            numPairs == 1 -> "원 페어"
            hasRoyalStraightFlush -> "로열 스트레이트 플러시"
            hasBackStraight -> "백 스트레이트"
            hasMountain -> "마운틴"
            else -> "탑"
        }
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