package com.techullurgy.pollapp.network.models

import com.techullurgy.pollapp.domain.models.AppPollOption
import kotlinx.serialization.Serializable

@Serializable
data class PollOption(
    val id: Long,
    val optionName: String,
    val optionIndex: Int
) {
    fun toAppPollOption(): AppPollOption = AppPollOption(
        id = id,
        optionName = optionName,
        optionIndex = optionIndex
    )
}
