/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package lab9.github;

import com.badlogic.gdx.graphics.Texture;

/**
 *
 * @author Maria Gabriela
 */
public class Cancion {

    String songPath;
    String songName;
    String artist;
    String duration;
    Texture albumCover;
    String musicGenre;

    public Cancion(String songPath, String songName, String artist, String duration, Texture albumCover, String musicGenre) {
        this.songPath=songPath;
        this.songName = songName;
        this.artist = artist;
        this.duration = duration;
        this.albumCover = albumCover;
        this.musicGenre = musicGenre;
    }

    public String getSongPath() {
        return songPath;
    }

    public String getSongName() {
        return songName;
    }

    public String getArtist() {
        return artist;
    }

    public String getDuration() {
        return duration;
    }

    public Texture getAlbumCover() {
        return albumCover;
    }

    public String getMusicGenre() {
        return musicGenre;
    }
    
    public void setAlbumCover(Texture texture){
        this.albumCover=texture;
    }
}
