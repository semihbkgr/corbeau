package com.semihbkgr.corbeau.repository;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.atomic.AtomicInteger;

@Repository
@Slf4j
public class ImageContentLocalFileRepository implements ImageContentRepository {

    private final Path rootDirectoryPath;
    private final String rootDirectory;
    private final boolean removeIfExists;
    private final String imageNotFoundImagePath;
    private Flux<DataBuffer> imageNotFoundDataBufferFlux;

    @Autowired
    public ImageContentLocalFileRepository(@Value("${image.repository.local.rootDirectory}") String rootDirectory,
                                           @Value("${image.removeIfExists:#{null}}") Boolean removeIfExists,
                                           @Value("${image.path.notFound}") String imageNotFoundImagePath) {
        this.rootDirectory = rootDirectory;
        this.rootDirectoryPath = Path.of(rootDirectory);
        this.removeIfExists = removeIfExists != null ? removeIfExists : false;
        this.imageNotFoundImagePath = imageNotFoundImagePath;
    }

    @PostConstruct
    public void createRootFolderIfNotExist() throws IOException {
        if (!Files.isDirectory(rootDirectoryPath)) {
            Files.createDirectory(rootDirectoryPath);
            log.info("RootFolderPath: Image root directory created successfully, directory: {}", rootDirectory);
        } else {
            log.info("RootFolderPath: Image root directory already exists, directory: {}", rootDirectory);
            if (removeIfExists) {
                var deletedFileCount = new AtomicInteger(0);
                var failedFileCount = new AtomicInteger(0);
                log.info("Image RemoveIfExists property is enabled, scanning for exists files");
                Files.walk(rootDirectoryPath)
                        .filter(path -> !Files.isDirectory(path))
                        .forEach(path -> {
                            try {
                                Files.delete(path);
                                deletedFileCount.incrementAndGet();
                            } catch (IOException ignore) {
                                failedFileCount.incrementAndGet();
                            }
                        });
                log.info("Existing images has been deleted, Deleted file count: {}, Failed file count: {}"
                        , deletedFileCount.get(), failedFileCount.get());
            }
        }
        log.info("ImageNotFoundImagePath: {}", imageNotFoundImagePath);
        this.imageNotFoundDataBufferFlux = DataBufferUtils.read(new ClassPathResource(imageNotFoundImagePath), new DefaultDataBufferFactory(), 4096);
    }


    @Override
    public Mono<Void> save(@NonNull String name, @NonNull FilePart filePart) {
        return filePart.transferTo(rootDirectoryPath.resolve(name));
    }

    @Override
    public Flux<DataBuffer> findByName(@NonNull String name) {
        return DataBufferUtils.read(rootDirectoryPath.resolve(name), new DefaultDataBufferFactory(), 4096);
    }

    @Override
    public Flux<DataBuffer> imageNotFound() {
        return this.imageNotFoundDataBufferFlux;
    }

    @Override
    public Mono<Boolean> exists(@NonNull String name) {
        return Mono.just(Files.exists(rootDirectoryPath.resolve(name)));
    }

    @Override
    public Mono<Void> delete(@NonNull String name) {
        return Mono.defer(() -> {
            try {
                Files.delete(rootDirectoryPath.resolve(name));
                return Mono.empty();
            } catch (IOException e) {
                return Mono.error(e);
            }
        });
    }

}
