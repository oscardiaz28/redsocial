package com.spring.redsocial.file;

import jakarta.annotation.PostConstruct;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileUploadServiceImpl implements FileUploadService{
    private String location;

    public FileUploadServiceImpl(String location){
        this.location = location;
    }

    @PostConstruct
    public void iniciarAlmacenDeArchivos() {
        try {
            Files.createDirectories(Paths.get(location));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String almacenarArchivo(MultipartFile file){
        String nameFile = file.getOriginalFilename();
        return "";
    }

    @Override
    public Path cargarArchivo(String nombre) {
        return null;
    }

    @Override
    public Resource cargarComoRecurso(String nombreArchivo) {
        return null;
    }

    @Override
    public void eliminarArchivo(String nombre) {

    }
}
