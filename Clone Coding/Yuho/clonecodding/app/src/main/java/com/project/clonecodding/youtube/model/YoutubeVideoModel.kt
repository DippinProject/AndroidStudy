package com.project.clonecodding.youtube.model

data class YoutubeVideoModel(
    val title: String,
    val channelName: String,
    val viewers: String,
    val uploadDate: String,
    val channelImgUrl: String,
    val thumbnailImgUrl: String,
    val contentType: HomeContentType
)