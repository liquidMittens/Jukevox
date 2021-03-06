package com.liquidcode.jukevox.networking.MessageObjects;

/**
 * SongInfoWrapper.java
 * Artist/song name that is wrapped in a object
 * Built from incoming socket byte[]
 * Created by mikev on 5/26/2017.
 */
public class SongInfoWrapper {
    private String m_artist;
    private String m_songName;
    private byte m_clientID;
    private SongDataWrapper m_songData;
    private long m_dataIndex;

    public SongInfoWrapper(String artist, String song, byte clientID) {
        m_artist = artist;
        m_songName = song;
        m_clientID = clientID;
        m_dataIndex = 0;
    }

    public void addToBuffer(byte[] dataToAdd) {
        if(m_songData == null) {
            m_songData = new SongDataWrapper(m_clientID, dataToAdd, false);
        }
        else {
            m_songData.updateSongBuffer(dataToAdd);
        }
    }

    public String getArtist() {
        return m_artist;
    }
    public String getSongName() {
        return m_songName;
    }
    public byte getClientID() { return m_clientID; }
    public SongDataWrapper getSongData() { return m_songData; }
    // setters
    public void setSongData(SongDataWrapper songData) { m_songData = songData; }
}
