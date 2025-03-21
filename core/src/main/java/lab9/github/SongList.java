/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package lab9.github;

/**
 *
 * @author Maria Gabriela
 */
public class SongList {
    
    private CancionNodo head;
    private int size;

    public SongList() {
        this.head = null;
        this.size = 0;
    }

    public void add(Cancion song) {
        CancionNodo newNode = new CancionNodo(song);

        if (head == null) {
            head = newNode;
        } else {
            CancionNodo current = head;
            while (current.getNext() != null) {
                current = (CancionNodo) current.getNext();
            }
            current.setNext(newNode);
        }

        size++;
    }

    public boolean remove(int index) {
        if (head == null || index < 0 || index >= size) {
            return false;
        }

        if (index == 0) {
            head = head.getNext();
            size--;
            return true;
        }

        CancionNodo current = head;
        int count = 0;

        while (count < index - 1) {
            current = current.getNext();
            count++;
        }

        if (current.getNext() == null) {
            return false;
        }

        current.setNext(current.getNext().getNext());
        size--;
        return true;
    }

    public Cancion get(int index) {
        if (index < 0 || index >= size) {
            return null;
        }

        CancionNodo current = head;
        int count = 0;

        while (count < index) {
            current = current.getNext();
            count++;
        }

        return current.getSong();
    }

    public Cancion getFirstSong() {
        if (head == null) {
            return null;
        }
        return head.getSong();
    }
    
    public CancionNodo getHead(){
        return head;
    }
    
    public int getSize(){
        return size;
    }
}
