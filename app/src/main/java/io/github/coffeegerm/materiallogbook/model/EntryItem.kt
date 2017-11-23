package io.github.coffeegerm.materiallogbook.model

import io.realm.RealmObject
import java.util.*

/**
 * Created by David Yarzebinski on 6/25/2017.
 *
 *
 *
 *
 * EntryItem POJO for setting and retrieving data for layouts
 *
 *
 * for Status
 *
 *
 * case 0: null
 * 1: breakfast
 * 2: lunch
 * 3: dinner
 * 4: sick
 * 5: exercise
 * 6: sweets
 */

open class EntryItem : RealmObject() {
    var id: String = UUID.randomUUID().toString()
    var status: Int = 0
    var date: Date? = null
    var bloodGlucose: Int = 0
    var carbohydrates: Int = 0
    var insulin: Double = 0.toDouble()
}
