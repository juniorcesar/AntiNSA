/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.utfpr.cm.antinsa.googledrive;

/**
 *
 * @author junior
 */
public class DataFile {

    private int id;
    private String name;
    private long date;
    private String hash;
    private String cloudHash;

    public DataFile(int id, String name,long date, String hash, String cloudHash) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.hash = hash;
        this.cloudHash = cloudHash;
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

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getCloudHash() {
        return cloudHash;
    }

    public void setCloudHash(String cloudHash) {
        this.cloudHash = cloudHash;
    }

    @Override
    public String toString() {
        return "DataFile{" + "id=" + id + ", name=" + name + ", date=" + date + ", hash=" + hash + '}';
    }
}
