package am.player.imlp.entity

import android.net.Uri

data class SongData(
    val title: String,
    val duration: Long,
    val durationSting: String,
    val author: String,
    val uri: Uri
)