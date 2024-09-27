package com.project.clonecoding.presentation.tinder.model

data class TinderModel(
    val name: String,
    val age: String,
    val place: String,
    val distance: String,
    val imgUrl: String,
    var isLike: Boolean? = null,
    var isFavorite: Boolean = false
)
