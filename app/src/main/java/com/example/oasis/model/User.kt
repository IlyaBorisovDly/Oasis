package com.example.oasis.model

import com.example.oasis.ui.workout.WorkoutFactory
import org.json.JSONArray
import org.json.JSONObject

data class User(
    var name: String,
    var email: String,
    val bestResults: Map<String, Double> = initializeMap()
)

private fun initializeMap(): MutableMap<String, Double> {
    return mutableMapOf(
        "Жим лёжа" to 0.0,
        "Жим на обратной наклонной скамье" to 0.0,
        "Разводка гантелей" to 0.0,
        "Сведение рук в кроссовере" to 0.0,
        "Подъём штанги на бицепс" to 0.0,
        "Подъём штанги обратным хватом" to 0.0,
        "Подъём гантелей на наклонной скамье сидя" to 0.0,

        "Приседания со штангой" to 0.0,
        "Жим ногами лёжа" to 0.0,
        "Разгибания ног в тренажёре" to 0.0,
        "Подъёмы на носки стоя в станке под углом" to 0.0,
        "Жим гантелей вверх сидя" to 0.0,
        "Разведение гантелей стоя" to 0.0,
        "Жим Арнольда" to 0.0,

        "Становая тяга" to 0.0,
        "Подтягивания широким хватом" to 0.0,
        "Тяга верхнего блока" to 0.0,
        "Тяга нижнего блока" to 0.0,
        "Отжимания на брусьях" to 0.0,
        "Французский жим с гантелей сидя" to 0.0,
        "Разгибания на блоке" to 0.0
    )
}

