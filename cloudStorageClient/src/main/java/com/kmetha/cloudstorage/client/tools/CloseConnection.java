package com.kmetha.cloudstorage.client.tools;

import io.netty.handler.codec.serialization.ObjectDecoderInputStream;
import io.netty.handler.codec.serialization.ObjectEncoderOutputStream;

import java.io.IOException;
import java.net.Socket;

public class CloseConnection {

    private ObjectDecoderInputStream is;
    private ObjectEncoderOutputStream os;
    private Socket socket;

    public CloseConnection(ObjectDecoderInputStream is, ObjectEncoderOutputStream os, Socket socket) {
        this.is = is;
        this.os = os;
        this.socket = socket;
    }

    public void closeConnection() {
        try {
            is.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        try {
            os.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        try {
            socket.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
}
