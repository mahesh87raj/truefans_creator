package com.example.truefans_creator.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transactions")
data class Transaction(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val transactionId: String,
    val amount: Double,
    val creatorName: String,
    val timestamp: Long = System.currentTimeMillis(),
    val status: String // "SUCCESS" or "FAILURE"
)
