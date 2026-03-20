package com.example.truefans_creator

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.truefans_creator.data.AppDatabase
import com.example.truefans_creator.databinding.ActivityPaymentGatewayBinding
import com.example.truefans_creator.models.Transaction
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.UUID

class PaymentGatewayActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPaymentGatewayBinding
    private var amount: Int = 0
    
    // Virtual Razorpay Key for demonstration
    private val VIRTUAL_RAZORPAY_KEY = "rzp_test_vIrTuAlKeY2024"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPaymentGatewayBinding.inflate(layoutInflater)
        setContentView(binding.root)

        amount = intent.getIntExtra("AMOUNT", 0)
        binding.tvAmount.text = "Rs. $amount"
        
        // Show Virtual Razorpay Key in the UI
        binding.tvVirtualKey.text = "Virtual Key: $VIRTUAL_RAZORPAY_KEY"

        binding.btnPayNow.setOnClickListener {
            // Start Virtual Success Payment
            simulateVirtualSuccess()
        }
    }

    private fun simulateVirtualSuccess() {
        // Show processing state
        binding.btnPayNow.isEnabled = false
        binding.paymentProgress.visibility = View.VISIBLE
        binding.btnPayNow.text = "Processing Virtual Payment..."

        // Delay for 2 seconds to simulate a "Real" gateway feel
        Handler(Looper.getMainLooper()).postDelayed({
            saveVirtualTransaction()
        }, 2000)
    }

    private fun saveVirtualTransaction() {
        // Generate a mock Transaction ID
        val mockTxnId = "VIRT_TXN_" + UUID.randomUUID().toString().substring(0, 8).uppercase()
        
        val transaction = Transaction(
            transactionId = mockTxnId,
            amount = amount.toDouble(),
            creatorName = "Virat Kohli",
            status = "SUCCESS"
        )

        CoroutineScope(Dispatchers.IO).launch {
            // Save to local database
            AppDatabase.getDatabase(this@PaymentGatewayActivity)
                .transactionDao().insertTransaction(transaction)
            
            launch(Dispatchers.Main) {
                // Navigate to Success Screen
                val intent = Intent(this@PaymentGatewayActivity, SuccessFailureActivity::class.java).apply {
                    putExtra("TRANSACTION_ID", mockTxnId)
                    putExtra("AMOUNT", amount)
                    putExtra("STATUS", "SUCCESS")
                }
                startActivity(intent)
                finish()
            }
        }
    }
}
