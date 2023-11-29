package com.techullurgy.pollapp.domain.models

import com.techullurgy.pollapp.network.models.PollOption

data class AppPollOption(
    val id: Long = -1,
    val optionName: String,
    val optionIndex: Int
) {
    fun toPollOption(): PollOption = PollOption(
        id = id,
        optionName = optionName,
        optionIndex = optionIndex
    )
}
