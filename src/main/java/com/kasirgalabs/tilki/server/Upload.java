package com.kasirgalabs.tilki.server;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.Callable;

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
public class Upload implements Callable<Void> {
    private final Socket socket;
    private final DataInputStream in;
    private final DataOutputStream out;

    public Upload(Socket socket) throws IOException {
        this.socket = socket;
        in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
        out = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
    }

    @Override
    public Void call() throws Exception {
        File file = handshake();
        String md5Hex = readFileFromInput(file);
        writeMD5(md5Hex);
        return null;
    }

    private File handshake() throws IOException {
        String fileName = in.readUTF();
        String examName = in.readUTF();
        return new File(new File(examName), fileName);
    }

    private String readFileFromInput(File file) throws IOException, NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        FileOutputStream fis = new FileOutputStream(file);
        try(BufferedOutputStream bos = new BufferedOutputStream(fis)) {
            int readBytes;
            byte[] buffer = new byte[8192];
            while((readBytes = in.read(buffer)) > 0) {
                bos.write(buffer, 0, readBytes);
                updateHash(md, buffer, readBytes);
            }
        }
        return md5Hex(md);
    }

    private void updateHash(MessageDigest md, byte[] buffer, int len) {
        md.update(buffer, 0, len);
    }

    private String md5Hex(MessageDigest md) {
        byte[] hash = md.digest();
        StringBuilder md5Hex = new StringBuilder();
        for(int i = 0; i < hash.length; i++) {
            md5Hex.append(Integer.toString((hash[i] & 0xff) + 0x100, 16).substring(1));
        }
        return md5Hex.toString();
    }

    private void writeMD5(String md5Hex) throws IOException {
        out.writeUTF(md5Hex);
        out.flush();
        socket.shutdownOutput();
    }
}
