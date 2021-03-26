package com.britishbroadcast.gitto.model.data

data class GitUsersResponse(
    val incomplete_results: Boolean,
    val items: List<Item>,
    val total_count: Int
)