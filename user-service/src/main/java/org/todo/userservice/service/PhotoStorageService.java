package org.todo.userservice.service;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.*;
import java.util.UUID;

@Service
public class PhotoStorageService {

    private final Path uploadsDir;

    public PhotoStorageService(
            @Value("${user.uploads-dir:./uploads}") String dir
    ) {
        this.uploadsDir = Paths.get(dir).toAbsolutePath().normalize();
    }

    @PostConstruct
    public void init() throws IOException {
        Files.createDirectories(uploadsDir);
    }

    public String store(MultipartFile file, Long userId) throws IOException {
        String original = StringUtils.cleanPath(file.getOriginalFilename());
        String ext = "";
        int idx = original.lastIndexOf('.');
        if (idx > 0) ext = original.substring(idx);

        String filename = userId + "-" + UUID.randomUUID() + ext;
        Path target = uploadsDir.resolve(filename);

        Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);

        return "/uploads/" + filename;
    }
}
