package util;

import model.bean.Foto;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class FotoUtils {

    public static Foto load(String pathname) throws IOException {
        File file = new File(pathname);
        byte[] source = Files.readAllBytes(file.toPath());
        return new Foto(source);
    }
}
