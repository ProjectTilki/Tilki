package com.kasirgalabs.tilki.utils;

public class Hex {

    public static String xorHex(String a, String b) {
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
