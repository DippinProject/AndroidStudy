package com.project.clonecoding.presentation.youtube.model

import com.project.clonecoding.presentation.youtube.model.HomeContentType

data class YoutubeVideoModel(
    val title: String,
    val channelName: String,
    val viewers: String,
    val uploadDate: String,
    val channelImgUrl: String,
    val thumbnailImgUrl: String,
    val contentType: HomeContentType
)