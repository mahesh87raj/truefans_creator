package com.example.truefans_creator.presentation.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.truefans_creator.databinding.ActivitySuccessFailureBinding

class SuccessFailureActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySuccessFailureBinding
    private var transactionId: String = ""
    private var amount: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySuccessFailureBinding.inflate(layoutInflater)
        setContentView(binding.root)

        transactionId = intent.getStringExtra("TRANSACTION_ID") ?: "#123456789"
        amount = intent.getIntExtra("AMOUNT", 0)

        displayStatus()
        setupListeners()
    }

    private fun displayStatus() {
        binding.tvTransactionId.text = "Transaction ID: #$transactionId"
        binding.tvStatus.text = "Payment Successful!"
        binding.tvMessage.text = "Thank you for supporting your favorite creator with Rs. $amount. Your tip helps them create more amazing content!"
    }

    private fun setupListeners() {
        binding.btnBackToProfile.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
            finish()
        }

        binding.btnTipAgain.setOnClickListener {
            val intent = Intent(this, SupportCreatorActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
