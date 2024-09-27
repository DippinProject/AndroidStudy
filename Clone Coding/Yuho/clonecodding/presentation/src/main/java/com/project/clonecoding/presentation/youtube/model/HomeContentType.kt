package com.project.clonecoding.presentation.youtube.model

sealed class HomeContentType(val type: String, val text: String) {
    data object Compass : HomeContentType("compass", "Compass")
    data object All : HomeContentType("all", "All")
    data object Music : HomeContentType("music", "Music")
    data object Figma : HomeContentType("figma", "Figma")
    data object Mixes : HomeContentType("mixes", "Mixes")
    data object Graphics : HomeContentType("graphics", "Graphics")
    data object Games : HomeContentType("games", "Games")
}