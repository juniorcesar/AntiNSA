/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.utfpr.cm.antinsa.service.googledrive;

/**
 *
 * @author junior
 */
public class DataFile {

    private int id;
    private String name;
    private int size;
    private long date;
    private String hash;

    public DataFile(int id, String name, int size, long date, String hash) {
        this.id = id;
        this.name = name;
        this.size = size;
        this.date = date;
        this.hash = hash;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    @Override
    public String toString() {
        return "DataFile{" + "id=" + id + ", name=" + name + ", size=" + size + ", date=" + date +  ", hash=" + hash + '}';
    }
}
