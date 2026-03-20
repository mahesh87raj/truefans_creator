package com.example.truefans_creator

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.truefans_creator.data.ProfileManager
import com.example.truefans_creator.databinding.ActivityCheckoutSummaryBinding

class CheckoutSummaryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCheckoutSummaryBinding
    private lateinit var profileManager: ProfileManager
    private var amount: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCheckoutSummaryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        profileManager = ProfileManager(this)
        amount = intent.getIntExtra("AMOUNT", 0)

        setupToolbar()
        displaySummary()
        
        binding.btnConfirmPayment.setOnClickListener {
            // Navigate to Simulated Payment Gateway (Screen 3)
            val intent = Intent(this, PaymentGatewayActivity::class.java).apply {
                putExtra("AMOUNT", amount)
            }
            startActivity(intent)
        }
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener { finish() }
    }

    private fun displaySummary() {
        val profile = profileManager.getProfile()
        binding.tvCreatorName.text = profile?.name ?: "Virat Kohli"
        
        val platformFee = (amount * 0.10).toInt()
        val netAmount = amount - platformFee

        binding.tvTipAmount.text = "Rs. $amount"
        binding.tvPlatformFee.text = "Rs. $platformFee"
        binding.tvNetToCreator.text = "Rs. $netAmount"
    }
}
