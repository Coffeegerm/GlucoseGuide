/*
 * Copyright 2017 Coffee and Cream Studios
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.coffeegerm.materiallogbook.model

import io.realm.RealmObject
import java.util.*

/**
 * Created by David Yarzebinski on 6/25/2017.
 *
 * EntryItem POJO for setting and retrieving data for layouts
 *
 * For Status
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
