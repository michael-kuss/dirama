/**
 * Copyright (c) 2015-2026 Michael Kuß
 */
package de.miq.dirama.service;

import de.miq.dirama.exception.NotFoundException;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import javax.imageio.ImageIO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@Slf4j
public class ImageService {
  public String storeImage(MultipartFile file) {
    validateImage(file);

    String ext =
        switch (Objects.requireNonNullElse(file.getContentType(), "")) {
          case "image/png" -> "png";
          default -> "jpg";
        };

    String filename = UUID.randomUUID() + "." + ext;

    Path uploadDir = Paths.get("user-avatars");
    Path fullPath = uploadDir.resolve(filename);

    try {
      Files.createDirectories(uploadDir);
      file.transferTo(fullPath);
    } catch (IOException ex) {
      throw new RuntimeException("Could not store avatar", ex);
    }

    return filename;
  }

  public Path getImagePath(String fileName) {
    Path path = Paths.get("user-avatars", fileName);

    if (!Files.exists(path)) {
      log.warn("Missing image file {}", fileName);
      throw new NotFoundException();
      // return ResponseEntity.notFound().build();
    }
    return path;
  }

  public ResponseEntity<byte[]> getImageAsByteArray(Path path) {
    byte[] imageBytes;
    try {
      imageBytes = Files.readAllBytes(path);
    } catch (IOException e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    HttpHeaders headers = new HttpHeaders();
    MediaType type =
        MediaTypeFactory.getMediaType(path.toString()).orElse(MediaType.APPLICATION_OCTET_STREAM);
    headers.setContentType(type);
    headers.setCacheControl(CacheControl.maxAge(30, TimeUnit.DAYS).cachePublic());
    return new ResponseEntity<>(imageBytes, headers, HttpStatus.OK);
  }

  private void validateImage(MultipartFile file) {
    if (file.isEmpty()) {
      throw new IllegalArgumentException("No file received");
    }

    if (file.getSize() > 2 * 1024 * 1024) {
      throw new IllegalArgumentException("Image is too large");
    }

    String contentType = file.getContentType();
    if (!List.of("image/jpeg", "image/png").contains(contentType)) {
      throw new IllegalArgumentException("Unsupported image type");
    }

    try (InputStream in = file.getInputStream()) {
      BufferedImage image = ImageIO.read(in);
      if (image == null) {
        throw new IllegalArgumentException("File is not a valid image");
      }
    } catch (IOException ex) {
      throw new IllegalStateException("Could not read uploaded file");
    }
  }
}
