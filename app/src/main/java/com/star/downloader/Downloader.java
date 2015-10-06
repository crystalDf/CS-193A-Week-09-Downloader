package com.star.downloader;


import android.os.Environment;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

public class Downloader {

    public static String download(String urlString) {

        File folder = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOWNLOADS);

        if (!folder.exists()) {
            folder.mkdirs();
        }

        byte[] bytes = downloadToByteArray(urlString);

        String fileName = urlString.substring(urlString.lastIndexOf("/") + 1);

        File urlFile = new File(folder, fileName);

        try {
            FileOutputStream fileOutputStream = new FileOutputStream(urlFile);
            fileOutputStream.write(bytes);
            fileOutputStream.close();
            return urlString;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return fileName;
    }

    public static String fakeDownload(String url) {
        return "";
    }

    private static byte[] downloadToByteArray(String urlString) {

        byte[] bytes = null;

        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

            URL url = new URL(urlString);

            InputStream inputStream = url.openStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

            int ch;

            while ((ch = bufferedReader.read()) != -1) {
                byteArrayOutputStream.write(ch);
            }

            bytes = byteArrayOutputStream.toByteArray();

            inputStream.close();
            byteArrayOutputStream.close();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return bytes;

    }
}
