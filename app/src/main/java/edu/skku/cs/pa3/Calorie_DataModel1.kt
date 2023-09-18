package edu.skku.cs.pa3

import com.google.gson.annotations.SerializedName

data class CalorieResponse(
    val status_code: Int,
    val request_result: String,
    val data: CalorieData
)

data class CalorieData(
    val BMR: Double,
    val goals: CalorieGoals
)

data class CalorieGoals(
    @SerializedName("maintain weight")
    val maintain_weight: Double,
    @SerializedName("Mild weight loss")
    val Mild_weight_loss: CalorieLoss,
    @SerializedName("Weight loss")
    val Weight_loss: CalorieLoss,
    @SerializedName("Extreme weight loss")
    val Extreme_weight_loss: CalorieLoss,
    @SerializedName("Mild weight gain")
    val Mild_weight_gain: CalorieGain,
    @SerializedName("Weight gain")
    val Weight_gain: CalorieGain,
    @SerializedName("Extreme weight gain")
    val Extreme_weight_gain: CalorieGain
)

data class CalorieLoss(
    @SerializedName("loss weight")
    val loss_weight: String,
    val calory: Double
)

data class CalorieGain(
    @SerializedName("gain weight")
    val gain_weight: String,
    val calory: Double
)