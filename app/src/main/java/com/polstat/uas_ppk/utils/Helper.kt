package com.polstat.uas_ppk.utils

import java.util.Calendar
import kotlin.random.Random

object Helper {
    fun getGreeting(): String {
        val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY) // Ambil jam dalam format 24 jam

        return when {
            hour < 4 -> "Selamat Malam"
            hour < 11 -> "Selamat Pagi"
            hour < 16 -> "Selamat Siang"
            hour < 18 -> "Selamat Sore"
            else -> "Selamat Malam"
        }
    }

    fun getQuotes(): String {
        val quotes = listOf(
            "Segala sesuatu yang negatif, tekanan, dan tantangan adalah kesempatan bagiku untuk bangkit. -- Kobe Bryant",
            "Hidup tidak pernah mudah. Ada pekerjaan yang harus dilakukan dan kewajiban yang harus dipenuhi, kewajiban terhadap kebenaran, keadilan, dan kebebasan. -- John F. Kennedy",
            "Hal-hal besar dilakukan oleh serangkaian hal-hal kecil yang disatukan. -- Vincent van Gogh",
            "Kehidupan yang tidak diuji tidak layak untuk dijalani. -- Socrates",
            "Sudah lama menjadi perhatian saya bahwa orang-orang berprestasi jarang duduk dan membiarkan sesuatu terjadi pada mereka. Mereka keluar dan mengalami banyak hal. -- Leonardo da Vinci",
            "Satu-satunya sumber dari pengetahuan adalah pengalaman. -- Albert Einstein",
            "Kesenangan dalam sebuah pekerjaan membuat kesempurnaan pada hasil yang dicapai. -- Aristoteles",
            "Waktumu terbatas, jadi jangan sia-siakan dengan menjalani hidup orang lain. Jangan terjebak oleh dogma, yaitu hidup dengan hasil pemikiran orang lain. -- Steve Jobs",
            "Usaha dan keberanian tidak cukup tanpa tujuan dan arah perencanaan. -- John F. Kennedy",
            "Dunia ini cukup untuk memenuhi kebutuhan manusia, bukan untuk memenuhi keserakahan manusia. -- Mahatma Gandhi",
            "Semua impian kita bisa menjadi kenyataan, jika kita memiliki keberanian untuk mengejarnya. -- Walt Disney"
        )
        return quotes[Random.nextInt(quotes.size)]
    }
}
