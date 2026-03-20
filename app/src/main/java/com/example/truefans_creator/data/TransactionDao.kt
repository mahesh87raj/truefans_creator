package com.example.truefans_creator.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.truefans_creator.models.Transaction

@Dao
interface TransactionDao {
    @Insert
    suspend fun insertTransaction(transaction: Transaction)

    @Query("SELECT * FROM transactions ORDER BY timestamp DESC")
    suspend fun getAllTransactions(): List<Transaction>
}
