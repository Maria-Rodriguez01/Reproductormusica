/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package lab9.github;

/**
 *
 * @author Maria Gabriela
 */
public class CancionNodo {
    Cancion song;
    CancionNodo nextSong;
    
    public CancionNodo(Cancion song) {
        this.song = song;
        this.nextSong = null;
    }
    
    public Cancion getSong() {
        return song;
    }
    public CancionNodo getNext() {
        return nextSong;
    }
    public void setNext(CancionNodo nextSong) { 
        this.nextSong = nextSong;
    }

}
