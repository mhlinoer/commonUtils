package com.linoer.app.utils.string;

import java.io.*;
import java.util.regex.Pattern;

public class StringHelper {
    // inputStream转String
    public String parseString(final InputStream in) throws Exception {
        final ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
        int ch;
        while ((ch = in.read()) != -1) {
            swapStream.write(ch);
        }
        File f = new File("");
        f.lastModified();
        return swapStream.toString();
    }

    // OutputStream 转String
    public String parseString(final OutputStream out) {
        ByteArrayOutputStream baos = (ByteArrayOutputStream) out;
        final ByteArrayInputStream swapStream = new ByteArrayInputStream(baos.toByteArray());
        return swapStream.toString();
    }

    // inputStream转outputStream
    public ByteArrayOutputStream parse(final InputStream in) throws Exception {
        final ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
        int ch;
        while ((ch = in.read()) != -1) {
            swapStream.write(ch);
        }
        return swapStream;
    }

    // outputStream转inputStream
    public ByteArrayInputStream parse(final OutputStream out) {
        ByteArrayOutputStream baos = (ByteArrayOutputStream) out;
        return new ByteArrayInputStream(baos.toByteArray());
    }

    // String转inputStream
    public ByteArrayInputStream parseInputStream(final String in) {
        return new ByteArrayInputStream(in.getBytes());
    }

    // String 转outputStream
    public ByteArrayOutputStream parseOutputStream(final String in) throws Exception {
        return parse(parseInputStream(in));
    }

    public static boolean isNumber(String string) {
        if (string == null) {
            return false;
        }
        Pattern pattern = Pattern.compile("^-?\\d+(\\.\\d+)?$");
        return pattern.matcher(string).matches();
    }

    public static boolean isStartWithNumber(String string){
        if (string == null){
            return false;
        }
        Pattern pattern = Pattern.compile("^(\\d+)(.*)");
        return pattern.matcher(string).matches();
    }

}
