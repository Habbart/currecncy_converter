package com.example.currency_converter.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;


@Component
@Slf4j
public class TextFileDownloader {


    @Value("${url}")
    private String url;

    /**
     * Download file from URL in param to TXT file - DownloadedFile_1.txt.
     * If txt file not exist - create such file.
     *
     * @return file which was downloaded
     */

    public File getFile() {
        File file = new File("DownloadedFile_1.txt");
        URL website = null;
        log.debug("this file was downloaded by " + Thread.currentThread().getName());
        try {
            website = new URL(this.url);
            log.debug("url: " + website);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        try {
            assert website != null;
            try (ReadableByteChannel rbc = Channels.newChannel(website.openStream());
                 FileOutputStream fos = new FileOutputStream(file)) {
                fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }
}
