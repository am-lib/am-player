package am.player.imlp

import am.player.imlp.entity.Song
import am.player.imlp.entity.SongData
import android.content.Context
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.widget.Toast

class AmPlayer(private val context: Context) : MediaPlayer.OnPreparedListener,
    MediaPlayer.OnCompletionListener {
    constructor(context: Context, song: Song) : this(context) {
    }

    constructor(context: Context, songPathList: List<Int>) : this(context) {
        val songList = arrayListOf<Song>()
        songPathList.forEach {
            songList.add(Song(it, context))
        }
        setSongsList(songList)
    }

    private var mediaPlayer = MediaPlayer()
    private var songs = arrayListOf<SongData>()
    private var songPlayedTag = 0
    private var isPlayerCreated = false
    private var isPause = false
    private val PLAYLIST_END = "Playlist has ended"
    private val PLAYLIST_START = "Previous song doesn't exist"

    fun getSongsList(): List<SongData> {
        return songs
    }

    fun playList() {
        initPlayer()
        mediaPlayer.apply {
            setDataSource(context, songs[songPlayedTag].uri)
            prepareAsync()
        }
    }

    fun play(song: SongData) {
        stop()
        initPlayer()
        mediaPlayer.apply {
            setDataSource(context, song.uri)
            prepareAsync()
        }
        songPlayedTag = songs.indexOf(song)
    }

    fun pause() {
        if (isPlayerCreated) {
            if (isPause) {
                resume()
                isPause = false
            } else {
                isPause = true
                mediaPlayer.pause()
            }
        }
    }

    fun stop() {
        if (isPlayerCreated) {
            mediaPlayer.stop()
            mediaPlayer.release()
            isPlayerCreated = false
        }
    }

    fun resume() {
        mediaPlayer.start()
    }

    fun restart() {
        mediaPlayer.seekTo(0)
    }

    fun seekTo(time: Int) {
        if (time.isPositive()) {
            if (isPlayerCreated) {
                mediaPlayer.seekTo(time)
            }
        }
    }

    fun next() {
        validateNextSong()
    }

    fun prev() {
        validatePrevSong()
    }

    fun getCurrentSong(): SongData {
        return songs[songPlayedTag]
    }

    fun getCurrentSongName(): String {
        return songs[songPlayedTag].title
    }

    fun getCurrentStringDuration(): String {
        return songs[songPlayedTag].durationSting
    }

    fun getCurrentDuration(): Long {
        return songs[songPlayedTag].duration

    }

    fun getCurrentAuthor(): String {
        return songs[songPlayedTag].author
    }

    fun getCurrentSongsListSize(): Int = songs.size

    private fun setSongsList(songPathList: List<Song>) {
        songPathList.forEach {
            songs.add(
                SongData(
                    it.title,
                    it.duration,
                    it.durationString,
                    it.author,
                    it.uri
                )
            )
        }
    }

    private fun validateNextSong() {
        if (songs.size - 1 > songPlayedTag) {
            Int.MAX_VALUE
            stop()
            songPlayedTag++
            playList()
        } else {
            stop()
            songPlayedTag = songs.size
            Toast.makeText(context, PLAYLIST_END, Toast.LENGTH_SHORT).show()
        }
    }

    private fun validatePrevSong() {
        if (songPlayedTag > 0) {
            stop()
            songPlayedTag--
            playList()
        } else {
            stop()
            songPlayedTag = -1
            Toast.makeText(context, PLAYLIST_START, Toast.LENGTH_SHORT).show()
        }
    }

    private fun initPlayer() {
        mediaPlayer = MediaPlayer()
        isPlayerCreated = true
        mediaPlayer.setOnPreparedListener(this)
        mediaPlayer.setOnCompletionListener(this)
        mediaPlayer.apply {
            setAudioAttributes(
                AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .build()
            )

        }
        isPlayerCreated = true
    }

    private fun Int.isPositive(): Boolean {
        return this > 0
    }

    override fun onPrepared(mp: MediaPlayer?) {
        mediaPlayer.start()
    }

    override fun onCompletion(mp: MediaPlayer?) {
        next()
    }

}