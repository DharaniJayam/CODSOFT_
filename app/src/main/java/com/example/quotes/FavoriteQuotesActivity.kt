package com.example.quotes

import android.content.SharedPreferences
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.quotes.databinding.ActivityFavoriteQuotesBinding
import java.io.BufferedReader
import java.io.InputStreamReader


class FavoriteQuotesActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFavoriteQuotesBinding
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteQuotesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferences = getPreferences(MODE_PRIVATE)
        viewFavoriteQuotes()
    }

    private fun viewFavoriteQuotes() {
        try {
            // Read the saved quotes from the file in internal storage
            val fileInputStream = applicationContext.openFileInput("favorite_quotes.txt")
            val inputStreamReader = InputStreamReader(fileInputStream)
            val bufferedReader = BufferedReader(inputStreamReader)
            val favoritesList = mutableListOf<String>()

            bufferedReader.useLines { lines ->
                favoritesList.addAll(lines)
            }

            fileInputStream.close()

            if (favoritesList.isEmpty()) {
                binding.favoriteQuotesHeading.text = "No favorite quotes saved"
                binding.favoritesTextView.text = ""
            } else {
                // Join the list of quotes into a single string with line breaks
                val favorites = favoritesList.joinToString("\n\n")
                binding.favoriteQuotesHeading.text = "FAVORITE QUOTES"
                binding.favoriteQuotesHeading.setTypeface(null, Typeface.BOLD)
                binding.favoritesTextView.text = favorites
            }
        } catch (e: Exception) {
            Log.e("FavoriteQuotes", "Error reading file: ${e.message}")
            binding.favoriteQuotesHeading.text = "No favorite quotes saved"
            binding.favoritesTextView.text = ""
        }
    }


}
