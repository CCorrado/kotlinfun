package com.ccorrads.kotlinfun.models

import com.j256.ormlite.field.DatabaseField
import com.j256.ormlite.table.DatabaseTable
import com.ccorrads.kotlinfun.interfaces.ServerSync
import org.joda.time.DateTime

@DatabaseTable(tableName = "meal_prep")
data class MealPrep(private val date: DateTime) : ServerSync {

    constructor() : this(DateTime.now())

    @DatabaseField(generatedId = true, allowGeneratedIdInsert = true)
    var id: Int = 0

    @DatabaseField
    var ingredientOne: String = ""

    @DatabaseField
    var ingredientTwo: String = ""

    @DatabaseField
    var ingredientThree: String = ""

    @DatabaseField
    var ingredientFour: String = ""

    @DatabaseField(unique = true)
    var dateTime: DateTime = this.date

    @DatabaseField
    var needSync: Boolean = true

    override var shouldSync: Boolean
        get() = needSync
        set(value) {
            this.needSync = value
        }
}