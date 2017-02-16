/*
 * Copyright (C) 2017 Kasirgalabs
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.kasirgalabs.tilki.client;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TilkiConnection {
    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;

    public void connect() throws IOException {
        socket = new Socket("localhost", 50101);
        in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
        out = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
    }

    public DataInputStream getDataInputStream() {
        return in;
    }

    public DataOutputStream getDataOutputStream() {
        return out;
    }

    public void close() {
        try {
            if(out != null) {
                out.close();

            }
            if(in != null) {
                in.close();
            }
            if(socket != null) {
                socket.close();
            }
        } catch(IOException ex) {
            Logger.getLogger(TilkiConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void shutdownOutput() throws IOException {
        socket.shutdownOutput();
    }
}
