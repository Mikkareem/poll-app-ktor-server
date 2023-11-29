package com.techullurgy.pollapp.network.models

import com.techullurgy.pollapp.domain.models.AppPollOption
import kotlinx.serialization.Serializable

@Serializable
data class AssignablePollOption(
    val optionName: String,
    val optionIndex: Int
) {
    fun toAppPollOption(): AppPollOption = AppPollOption(
        optionName = optionName,
        optionIndex = optionIndex
    )
}
