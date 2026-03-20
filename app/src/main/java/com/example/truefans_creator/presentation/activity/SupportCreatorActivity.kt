package com.example.truefans_creator.presentation.activity

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.truefans_creator.databinding.ActivitySupportCreatorBinding

class SupportCreatorActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySupportCreatorBinding
    private var selectedAmount: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySupportCreatorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        setupListeners()
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener { finish() }
    }

    private fun setupListeners() {
        binding.cardFan.setOnClickListener { selectTier(50) }
        binding.cardSuperFan.setOnClickListener { selectTier(100) }
        binding.cardChampion.setOnClickListener { selectTier(500) }

        binding.btnNext.setOnClickListener {
            val customAmount = binding.etCustomAmount.text.toString()
            if (customAmount.isNotEmpty()) {
                selectedAmount = customAmount.toInt()
            }

            if (selectedAmount > 0) {
                val intent = Intent(this, CheckoutSummaryActivity::class.java).apply {
                    putExtra("AMOUNT", selectedAmount)
                }
                startActivity(intent)
            } else {
                Toast.makeText(this, "Please select or enter an amount", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun selectTier(amount: Int) {
        selectedAmount = amount
        binding.etCustomAmount.text?.clear()
        
        // Visual feedback for selection
        binding.cardFan.strokeColor = if (amount == 50) Color.parseColor("#FF6F00") else Color.LTGRAY
        binding.cardSuperFan.strokeColor = if (amount == 100) Color.parseColor("#FF6F00") else Color.LTGRAY
        binding.cardChampion.strokeColor = if (amount == 500) Color.parseColor("#FF6F00") else Color.LTGRAY
        
        binding.cardFan.strokeWidth = if (amount == 50) 4 else 2
        binding.cardSuperFan.strokeWidth = if (amount == 100) 4 else 2
        binding.cardChampion.strokeWidth = if (amount == 500) 4 else 2
    }
}
