package com.spring.redsocial.file;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;

public interface FileUploadService {
    String almacenarArchivo(MultipartFile file);
    Path cargarArchivo(String nombre);
    Resource cargarComoRecurso(String nombreArchivo);
    void eliminarArchivo(String nombre);
}
