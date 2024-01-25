package com.example.quotes

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast

import androidx.appcompat.app.AppCompatActivity
import android.content.Context

import com.example.quotes.databinding.ActivityMainBinding
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.lang.Exception

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferences = getPreferences(MODE_PRIVATE)

        getquote()

        binding.next.setOnClickListener {
            getquote()
        }

        binding.share.setOnClickListener {
            shareQuote()
        }

        binding.save.setOnClickListener {
            saveFavoriteQuote()
        }

        binding.viewFavorites.setOnClickListener {
            startActivity(Intent(this@MainActivity, FavoriteQuotesActivity::class.java))
        }



    }

    private fun getquote() {
        getinprogress(true)
        GlobalScope.launch {
            try {
                val response = retroinstance.quotesapi.getquote()
                runOnUiThread {
                    getinprogress(false)
                    response.body()?.first()?.let {
                        ui(it)
                    }
                }
            } catch (e: Exception) {
                runOnUiThread {
                    getinprogress(false)
                    Toast.makeText(applicationContext, "Something Went Wrong", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun ui(quote: quotemodel) {
        binding.quote.text = quote.q
        binding.auth.text = quote.a
    }

    private fun getinprogress(inProgress: Boolean) {
        if (inProgress) {
            binding.progress.visibility = View.VISIBLE
            binding.next.visibility = View.GONE
        } else {
            binding.progress.visibility = View.GONE
            binding.next.visibility = View.VISIBLE
        }
    }

    private fun shareQuote() {
        val quote = binding.quote.text.toString()
        val author = binding.auth.text.toString()

        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "text/plain"
        shareIntent.putExtra(Intent.EXTRA_TEXT, "Quote: $quote\nAuthor: $author")

        val chooserIntent = Intent.createChooser(shareIntent, "Share via")
        startActivity(chooserIntent)
    }

    private fun saveFavoriteQuote() {
        val quote = binding.quote.text.toString()
        val author = binding.auth.text.toString()

        // Concatenate the quote and author into a single string
        val quoteString = "Quote: $quote\nAuthor: $author"

        // Save the quote to a file in internal storage
        applicationContext.openFileOutput("favorite_quotes.txt", Context.MODE_APPEND).use {
            it.write("$quoteString\n\n".toByteArray())
        }

        Toast.makeText(applicationContext, "Quote saved to favorites", Toast.LENGTH_SHORT).show()
    }


}
