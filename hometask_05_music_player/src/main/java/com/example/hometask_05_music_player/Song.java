package com.example.hometask_05_music_player;

class Song {
    private String title ;
    private String path ;
    private Boolean isPlaying ;
    private Boolean isPaused ;

    Song(String title, String path) {
        this.title = title;
        this.path = path;
        isPlaying = false ;
        isPaused = false ;
    }

    String getTitle() {
        return title;
    }

    String getPath() {
        return path;
    }

    Boolean getPlaying() {
        return isPlaying;
    }

    Boolean getPaused() {
        return isPaused;
    }

    void setIsPlaying(Boolean playing) {
        isPlaying = playing;
    }

    void setIsPaused(Boolean paused) {
        isPaused = paused ;
    }
}
