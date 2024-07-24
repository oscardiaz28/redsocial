package com.spring.redsocial.file;

import com.spring.redsocial.exception.RedSocialExceptionHandler;
import jakarta.annotation.PostConstruct;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Component;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.*;
import java.util.UUID;

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
        String nameFile = UUID.randomUUID().toString() + file.getOriginalFilename();
        try {
            InputStream inputStream = file.getInputStream();
            Files.copy(inputStream, Paths.get(location).resolve(nameFile), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RedSocialExceptionHandler("Error al almacenar el archivo");
        }
        return nameFile;
    }

    @Override
    public Path cargarArchivo(String nombre) {
        return Paths.get(location).resolve(nombre);
    }

    @Override
    public Resource cargarComoRecurso(String nombreArchivo) {
        try {
            Path archivo = cargarArchivo(nombreArchivo);
            Resource resource = new UrlResource(archivo.toUri());
            if(resource.exists() || resource.isReadable() ){
                return resource;
            }else{
                throw new RedSocialExceptionHandler("No se ha encontrado el archivo");
            }
        } catch (MalformedURLException e) {
            throw new RedSocialExceptionHandler("No se ha encontrado el archivo");
        }
    }

    @Override
    public void eliminarArchivo(String nombre) {
        Path archivo = cargarArchivo(nombre);
        try {
            FileSystemUtils.deleteRecursively(archivo);
        } catch (IOException e) {
            throw new RedSocialExceptionHandler("Error el eliminar el archivo");
        }
    }
}
