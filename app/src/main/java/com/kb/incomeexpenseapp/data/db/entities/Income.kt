package com.kb.piechart.data.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.sql.Date

@Entity(tableName = "incomes")
data class Income (
    @PrimaryKey(autoGenerate = true)
    val incomeId: Int = 0,
    @ColumnInfo(name = "name") val name: String?,
    @ColumnInfo(name = "amount") val amount: Double?,
    @ColumnInfo(name = "day") val day: String?,
    @ColumnInfo(name = "month") val month: Int?,
    @ColumnInfo(name = "year") val year: Int?
)