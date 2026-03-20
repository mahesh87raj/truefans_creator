package com.example.truefans_creator

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.truefans_creator.databinding.ActivitySuccessFailureBinding
import org.json.JSONObject

class SuccessFailureActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySuccessFailureBinding
    private var transactionId: String? = null
    private var amount: Int = 0
    private var status: String = "SUCCESS"
    private var errorMsg: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySuccessFailureBinding.inflate(layoutInflater)
        setContentView(binding.root)

        transactionId = intent.getStringExtra("TRANSACTION_ID")
        amount = intent.getIntExtra("AMOUNT", 0)
        status = intent.getStringExtra("STATUS") ?: "SUCCESS"
        errorMsg = intent.getStringExtra("ERROR_MSG")

        displayStatus()
        setupListeners()
    }

    private fun displayStatus() {
        if (status == "SUCCESS") {
            binding.tvStatus.text = "Payment Successful!"
            binding.tvStatus.setTextColor(ContextCompat.getColor(this, android.R.color.holo_green_dark))
            binding.tvTransactionId.visibility = View.VISIBLE
            binding.tvTransactionId.text = "Transaction ID: #${transactionId ?: "N/A"}"
            binding.tvMessage.text = "Thank you for supporting your favorite creator with Rs. $amount. Your tip helps them create more amazing content!"
            binding.btnTipAgain.visibility = View.GONE
        } else {
            binding.tvStatus.text = "Payment Failed"
            binding.tvStatus.setTextColor(ContextCompat.getColor(this, android.R.color.holo_red_dark))
            binding.tvTransactionId.visibility = View.GONE
            
            // Parse user-friendly error from Razorpay JSON
            val displayError = try {
                if (errorMsg?.startsWith("{") == true) {
                    val json = JSONObject(errorMsg!!)
                    val error = json.getJSONObject("error")
                    error.getString("description")
                } else {
                    errorMsg
                }
            } catch (e: Exception) {
                "Authentication failed or payment cancelled."
            }
            
            binding.tvMessage.text = displayError ?: "Something went wrong. Please try again."
            binding.btnTipAgain.text = "Try Again"
            binding.btnTipAgain.visibility = View.VISIBLE
        }
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
