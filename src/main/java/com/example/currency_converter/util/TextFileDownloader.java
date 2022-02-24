package com.example.currency_converter.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.time.LocalDateTime;
import java.util.Date;

@Component
public class TextFileDownloader {

    private final Logger serviceLogger = LoggerFactory.getLogger("TextFileDownloader Logger");

    @Value("${url}")
    private String url;

    public File getFile(){
        File file = new File(String.format("DownloadedFile_%s.txt", 1 ));
        try {
            URL website = new URL(this.url);
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
