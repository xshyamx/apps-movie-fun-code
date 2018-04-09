package org.superbiz.moviefun.blobstore;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.*;
import java.util.Optional;

public class FileStore implements BlobStore {

    private final Path basedir;

    public FileStore(Path basedir) {
        this.basedir = basedir;
    }

    @Override
    public void put(Blob blob) throws IOException {
        Path filePath = basedir.resolve(blob.name);
        Files.copy(blob.inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
        Files.write(basedir.resolve(blob.name + ".contentType"),blob.contentType.getBytes(), StandardOpenOption.TRUNCATE_EXISTING);
    }

    @Override
    public Optional<Blob> get(String name) throws IOException {
        Path filePath = basedir.resolve(name);
        File f = filePath.toFile();
        if ( f.exists() ) {
            String contentType = new String(Files.readAllBytes(basedir.resolve(name + ".contentType")));
            return Optional.of(new Blob(name, new FileInputStream(f), contentType));
        }
        return null;
    }

    @Override
    public void deleteAll() {
        File dir = basedir.toFile();
        for ( File file :  dir.listFiles() ) {
            file.delete();
        }
    }
}
