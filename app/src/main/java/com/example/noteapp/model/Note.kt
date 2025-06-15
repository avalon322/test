package com.example.noteapp.model

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path

data class Note(
    val id: Long = System.currentTimeMillis(),
    var text: String = "",
    val paths: List<Pair<Path, Color>> = emptyList(),
    var group: String? = null,
    var parentId: Long? = null,
    val created: Long = System.currentTimeMillis()
)
