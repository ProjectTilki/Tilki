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
package com.kasirgalabs.tilki.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5 {
    private final MessageDigest md;

    public MD5() throws NoSuchAlgorithmException {
        md = MessageDigest.getInstance("MD5");

    }

    public void updateHash(byte[] buffer, int len) {
        md.update(buffer, 0, len);
    }

    public String md5Hex() {
        byte[] hash = md.digest();
        StringBuilder md5Hex = new StringBuilder();
        for(int i = 0; i < hash.length; i++) {
            md5Hex.append(Integer.toString((hash[i] & 0xff) + 0x100, 16).substring(1));
        }
        return md5Hex.toString();
    }

    public static String xorHex(String a, String b) {
        if(a.isEmpty()) {
            return b;
        }
        if(b.isEmpty()) {
            return a;
        }
        char[] chars = new char[a.length()];
        for(int i = 0; i < chars.length; i++) {
            chars[i] = toHex(fromHex(a.charAt(i)) ^ fromHex(b.charAt(i)));
        }
        return new String(chars);
    }

    private static int fromHex(char c) {
        if(c >= '0' && c <= '9') {
            return c - '0';
        }
        if(c >= 'A' && c <= 'F') {
            return c - 'A' + 10;
        }
        if(c >= 'a' && c <= 'f') {
            return c - 'a' + 10;
        }
        throw new IllegalArgumentException();
    }

    private static char toHex(int index) {
        if(index < 0 || index > 15) {
            throw new IllegalArgumentException();
        }
        return "0123456789ABCDEF".charAt(index);
    }
}
