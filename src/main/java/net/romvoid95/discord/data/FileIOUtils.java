package net.romvoid95.discord.data;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Objects;

public class FileIOUtils {
    private static final Charset UTF8 = StandardCharsets.UTF_8;

    public static String read(Path path) throws IOException {
    	byte[] ba = Files.readAllBytes(path);
    	return new String(ba, UTF8);
    }

    public static void write(Path path, String contents) throws IOException {
    	byte[] ba = Objects.requireNonNull(contents.getBytes());

        try (OutputStream out = Files.newOutputStream(path, StandardOpenOption.WRITE)) {
            int len = ba.length;
            int rem = len;
            while (rem > 0) {
                int n = Math.min(rem, 8192);
                out.write(ba, (len-rem), n);
                rem -= n;
            }
        }
    }
}
