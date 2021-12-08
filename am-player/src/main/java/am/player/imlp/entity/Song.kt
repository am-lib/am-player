package am.player.imlp.entity

import android.content.Context
import android.media.MediaMetadataRetriever
import android.net.Uri

class Song(private val rawPath: Int, private val context: Context) {
    val uri = getSongUri()
    val duration = getSongDuration(context)
    val durationString = getSongDurationString(context)
    val title = getSongTitle(context)
    val author = getSongAuthor(context)


    private fun getSongDuration(context: Context): Long {
        val metadataRetriever = MediaMetadataRetriever()
        metadataRetriever.setDataSource(context, uri)
        return metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
            ?.toLong()
            ?.div(1000)
            ?: 0
    }

    private fun getSongDurationString(context: Context): String {
        val metadataRetriever = MediaMetadataRetriever()
        metadataRetriever.setDataSource(context, uri)
        val seconds =
            metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
                ?.toLong()
                ?.div(1000)
                ?: 0
        return seconds.toString()
    }

    private fun getSongTitle(context: Context): String {
        val metadataRetriever = MediaMetadataRetriever()
        metadataRetriever.setDataSource(context, uri)
        return metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE)
            ?: "Unknown"
    }

    private fun getSongAuthor(context: Context): String {
        val metadataRetriever = MediaMetadataRetriever()
        metadataRetriever.setDataSource(context, uri)
        return metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_AUTHOR)
            ?: "Unknown"
    }

    private fun getSongUri(): Uri {
        return Uri.parse("android.resource://${context.packageName}/$rawPath")
    }
}