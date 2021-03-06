package com.liquidcode.jukevox.networking.Messaging;

import java.nio.charset.Charset;

/**
 * MessageBuilder.java
 * Builds different messages and puts the data into formatted byte arrays to be sent over socket
 * Created by mikev on 5/26/2017.
 */

public class MessageBuilder {

    /**
     * Builds the SongData class to be sent over the socket
     * @param artist - the artist name
     * @param song - the song name
     * @return byte[] of our data
     */
    public static byte[] buildSongInfo(byte clientID, String artist, String song) {
        // get the sizes for the data we're sending
        // outgoing song data = 1byte header (SM_SONGINFO) 1byte length + clientidsize + artist length + 1byte delim + song length + 1 byte delim
        short outSize = (short)(BTMessages.SM_MESSAGETYPESIZE + BTMessages.SM_LENGTH + BTMessages.SM_CLIENTIDSIZE
                + artist.length() + BTMessages.SM_DELIMITERSIZE + song.length() + BTMessages.SM_DELIMITERSIZE);
        byte[] outgoing = new byte[outSize];
        // now build our byte data
        int currentIndex = 0;
        // song data header
        outgoing[currentIndex] = BTMessages.SM_SONGINFO;
        ++currentIndex;
        // length
        outgoing[currentIndex] = (byte)outSize;
        ++currentIndex;
        outgoing[currentIndex] = (byte)(outSize >> 8);
        ++currentIndex;
        // client id
        outgoing[currentIndex] = clientID;
        ++currentIndex;
        // copy artist name bytes
        System.arraycopy(artist.getBytes(Charset.forName("UTF-8")), 0, outgoing, currentIndex, artist.length());
        currentIndex += artist.length();
        // put in delimeter
        outgoing[currentIndex] = BTMessages.SM_DELIM;
        ++currentIndex;
        // copy song name
        System.arraycopy(song.getBytes(Charset.forName("UTF-8")), 0, outgoing, currentIndex, song.length());
        currentIndex += song.length();
        // put in delim
        outgoing[currentIndex] = BTMessages.SM_DELIM;
        return outgoing;
    }

    /**
     * Builds the current chunk of this song and sends it to the server.
     * This will repeat until the the whole song is sent.
     * @param clientID - the client sending this message
     * @param songData - the byte data representing the song data
     * @param isDone - is this song done sending the data
     * @return - the byte info
     */
    public static byte[] buildSongData(byte clientID, byte[] songData, boolean isDone) {
        // get the sizes for the data we're sending
        // outgoing song data = 1byte header (SM_SONGDATA) + MESSAGE_LENGTH + CLIENT ID SIZE + 1byte BOOLEAN (finished) + song data length
        short outSize = (short)(BTMessages.SM_MESSAGETYPESIZE + BTMessages.SM_LENGTH + BTMessages.SM_CLIENTIDSIZE + BTMessages.SM_BOOLEAN
                + songData.length);
        byte[] outgoing = new byte[outSize];
        // now build our byte data
        int currentIndex = 0;
        // song data header
        outgoing[currentIndex] = BTMessages.SM_SONGDATA;
        ++currentIndex;
        // length
        int shiftOutsize = outSize;
        outgoing[currentIndex] = (byte)(shiftOutsize & 0xFF);
        ++currentIndex;
        outgoing[currentIndex] = (byte)((shiftOutsize >> 8) & 0xFF);
        ++currentIndex;
        // client id
        outgoing[currentIndex] = clientID;
        ++currentIndex;
        // boolean
        outgoing[currentIndex] = (isDone) ? (byte)1 : (byte)0;
        ++currentIndex;
        // copy the song data into the outgoing array
        System.arraycopy(songData, 0, outgoing, currentIndex, songData.length);
        currentIndex += songData.length;
        return outgoing;
    }

    /**
     * Builds the client count message that gets sent to clients
     * @param clientCount - the current client count
     * @return byte[] representing the message
     */
    public static byte[] buildClientCountData(int clientCount) {
        short outSize = BTMessages.SM_MESSAGETYPESIZE + BTMessages.SM_LENGTH + BTMessages.SM_CLIENTIDSIZE + 1;
        byte[] outgoing = new byte[outSize];
        outgoing[0] = BTMessages.SM_CLIENTCOUNT;
        outgoing[1] = (byte)outSize;
        outgoing[2] = (byte)(outSize >> 8);
        outgoing[3] = (byte)clientCount;
        return outgoing;
    }

    /**
     * Builds info data to be sent by both server and client
     * @param infoToSend - String with the data to be sent
     * @return - the byte array of our data
     */
    public static byte[] buildInfoData(String infoToSend) {
        int currentIndex = 0;
        short outSize = (short)(BTMessages.SM_MESSAGETYPESIZE + BTMessages.SM_LENGTH + infoToSend.length() + BTMessages.SM_DELIMITERSIZE);
        byte[] outgoing = new byte[outSize];
        outgoing[currentIndex] = BTMessages.SM_INFO;
        ++currentIndex;
        // length
        outgoing[currentIndex] = (byte)outSize;
        ++currentIndex;
        outgoing[currentIndex] = (byte)(outSize >> 8);
        ++currentIndex;
        System.arraycopy(infoToSend.getBytes(Charset.forName("UTF-8")), 0, outgoing, currentIndex, infoToSend.length());
        currentIndex += infoToSend.length();
        outgoing[currentIndex] = BTMessages.SM_DELIM; // end message value
        return outgoing;
    }

    /**
     * Builds info data to be sent by both server and client
     * @param infoToSend - String with the data to be sent
     * @return - the byte array of our data
     */
    public static byte[] buildInfoData(byte clientID, String infoToSend) {
        int currentIndex = 0;
        short outSize = (short)(BTMessages.SM_MESSAGETYPESIZE + BTMessages.SM_LENGTH + BTMessages.SM_CLIENTIDSIZE + infoToSend.length() + BTMessages.SM_DELIMITERSIZE);
        byte[] outgoing = new byte[outSize];
        outgoing[currentIndex] = BTMessages.SM_INFO;
        ++currentIndex;
        // length
        outgoing[currentIndex] = (byte)outSize;
        ++currentIndex;
        outgoing[currentIndex] = (byte)(outSize >> 8);
        ++currentIndex;
        outgoing[currentIndex] = clientID;
        ++currentIndex;
        System.arraycopy(infoToSend.getBytes(Charset.forName("UTF-8")), 0, outgoing, currentIndex, infoToSend.length());
        currentIndex += infoToSend.length();
        outgoing[currentIndex] = BTMessages.SM_DELIM; // end message value
        return outgoing;
    }

    /**
     * Builds the message for sending clients their id's
     * @param newID - the client ID
     * @return - byte array
     */
    public static byte[] buildClientIdData(byte newID) {
        short outSize = (short)(BTMessages.SM_MESSAGETYPESIZE + BTMessages.SM_LENGTH + BTMessages.SM_CLIENTIDSIZE + 1);
        byte[] outgoing = new byte[outSize];
        outgoing[0] = BTMessages.SM_CLIENTID;
        outgoing[1] = (byte)outSize;
        outgoing[2] = (byte)(outSize >> 8);
        outgoing[3] = newID;
        return outgoing;
    }

    public static byte[] buildClientNameData(byte clientID, String clientName) {
        short outSize = (short)(BTMessages.SM_MESSAGETYPESIZE + BTMessages.SM_LENGTH + BTMessages.SM_CLIENTIDSIZE + clientName.length() + BTMessages.SM_DELIMITERSIZE);
        byte[] outgoing = new byte[outSize];
        int currentIndex = 0;
        outgoing[currentIndex] = BTMessages.SM_CLIENTDISPLAYNAME;
        ++currentIndex;
        // length
        outgoing[currentIndex] = (byte)outSize;
        ++currentIndex;
        outgoing[currentIndex] = (byte)(outSize >> 8);
        ++currentIndex;
        // put the client id
        outgoing[currentIndex] = clientID;
        ++currentIndex;
        // put the clients name
        System.arraycopy(clientName.getBytes(Charset.forName("UTF-8")), 0, outgoing, currentIndex, clientName.length());
        currentIndex += clientName.length();
        outgoing[currentIndex] = BTMessages.SM_DELIM; // end message value
        return outgoing;
    }

    /** USED BY THE SERVER IT DOESNT NEED AN ID
     * Builds a message response that gets sent to the server
     * Formatting is [SMR_RESPONSE][CLIENT_ID][MESSAGE WE ARE RESPONDING TO]
     * @param respondToMessage - the message (SM_xxx) that we are sending a response for
     * @return the byte array of the message
     */
    public static byte[] buildMessageResponse(byte respondToMessage) {
        short outSize = BTMessages.SM_MESSAGETYPESIZE + BTMessages.SM_LENGTH + 1;
        byte[] outgoing = new byte[outSize];
        // put the response header
        outgoing[0] = BTMessages.SMR_RESPONSE;
        outgoing[1] = (byte)outSize;
        outgoing[2] = (byte)(outSize >> 8);
        // put the message that we are responding to
        outgoing[3] = respondToMessage;
        return outgoing;
    }

    /** USED BY THE CLIENTS
     * Builds a message response that gets sent to the server
     * Formatting is [SMR_RESPONSE][CLIENT_ID][MESSAGE WE ARE RESPONDING TO]
     * @param clientID - the clients ID
     * @param respondToMessage - the message (SM_xxx) that we are sending a response for
     * @return the byte array of the message
     */
    public static byte[] buildMessageResponse(byte clientID, byte respondToMessage) {
        short outSize = BTMessages.SM_MESSAGETYPESIZE + BTMessages.SM_LENGTH + BTMessages.SM_CLIENTIDSIZE + 1;
        byte[] outgoing = new byte[outSize];
        // put the response header
        outgoing[0] = BTMessages.SMR_RESPONSE;
        // length
        outgoing[1] = (byte)outSize;
        outgoing[2] = (byte)(outSize >> 8);
        // put the client ID
        outgoing[3] = clientID;
        // put the message that we are repsonding to
        outgoing[4] = respondToMessage;
        return outgoing;
    }
}
