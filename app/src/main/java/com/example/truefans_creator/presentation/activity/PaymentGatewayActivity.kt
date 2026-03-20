package com.example.truefans_creator.presentation.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.truefans_creator.data.local.AppDatabase
import com.example.truefans_creator.databinding.ActivityPaymentGatewayBinding
import com.example.truefans_creator.domain.model.Transaction
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.UUID

class PaymentGatewayActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPaymentGatewayBinding
    private var amount: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPaymentGatewayBinding.inflate(layoutInflater)
        setContentView(binding.root)

        amount = intent.getIntExtra("AMOUNT", 0)
        binding.tvAmount.text = "Rs. $amount"

        binding.btnPayNow.setOnClickListener {
            simulatePayment()
        }
    }

    private fun simulatePayment() {
        binding.btnPayNow.isEnabled = false
        binding.paymentProgress.visibility = View.VISIBLE
        
        // Simulate a 2-second payment processing delay
        Handler(Looper.getMainLooper()).postDelayed({
            saveTransaction()
        }, 2000)
    }

    private fun saveTransaction() {
        val transactionId = "TXN" + UUID.randomUUID().toString().substring(0, 8).uppercase()
        val transaction = Transaction(
            transactionId = transactionId,
            amount = amount.toDouble(),
            creatorName = "Virat Kohli", // In a real app, pass this via intent
            status = "SUCCESS"
        )

        CoroutineScope(Dispatchers.IO).launch {
            AppDatabase.getDatabase(this@PaymentGatewayActivity)
                .transactionDao().insertTransaction(transaction)
            
            launch(Dispatchers.Main) {
                val intent = Intent(this@PaymentGatewayActivity, SuccessFailureActivity::class.java).apply {
                    putExtra("TRANSACTION_ID", transactionId)
                    putExtra("AMOUNT", amount)
                    putExtra("STATUS", "SUCCESS")
                }
                startActivity(intent)
                finish()
            }
        }
    }
}
