package com.spring.redsocial.service;

import com.spring.redsocial.dto.PaginationResponse;
import com.spring.redsocial.dto.PublicationResponse;
import com.spring.redsocial.file.FileUploadService;
import com.spring.redsocial.file.FileUploadServiceImpl;
import com.spring.redsocial.model.Follow;
import com.spring.redsocial.model.Publication;
import com.spring.redsocial.model.User;
import com.spring.redsocial.repository.FollowRepository;
import com.spring.redsocial.repository.PublicationRepository;
import com.spring.redsocial.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.apache.coyote.Response;
import org.hibernate.event.spi.PostUpdateEventListener;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.swing.text.html.Option;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PublicationService {

    private final PublicationRepository publicationRepository;
    private final AuthService authService;
    private final UserRepository userRepository;
    private final FollowRepository followRepository;

    public ResponseEntity<?> save(String texto){
        User currentUser = authService.getCurrentUser();
        Publication publication = new Publication();
        publication.setText(texto);
        publication.setUser(currentUser);

        Publication publicationSaved = publicationRepository.save(publication);

        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "Publicacion guardada exitosamenete");
        response.put("data", publicationSaved);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public ResponseEntity<?> getOne(String id){
        User currentUser = authService.getCurrentUser();
        try{
            Optional<Publication> publication = publicationRepository.findById(Integer.parseInt(id));
            if(publication.isEmpty()){
                return new ResponseEntity<>("La publicación no existe", HttpStatus.BAD_REQUEST);
            }

            return new ResponseEntity<>(publication.get(), HttpStatus.OK);
        }catch(NumberFormatException e){
            return new ResponseEntity<>("ID de publicación inválido", HttpStatus.BAD_REQUEST);
        }

    }

    public ResponseEntity<?> remove(String id){
        User currentUser = authService.getCurrentUser();
        try{
            Optional<Publication> publication = publicationRepository.findById(Integer.parseInt(id));
            if(publication.isEmpty()  ){
                return new ResponseEntity<>("La publicación no existe", HttpStatus.BAD_REQUEST);
            }
            if( currentUser.getId() != publication.get().getUser().getId() )            {
                return new ResponseEntity<>("No se ha podido eliminar la publicación", HttpStatus.BAD_REQUEST);
            }
            System.out.println( "Current User: " + currentUser.getId() + " " + " Publicaiont: " + publication.get().getUser().getId() );
            publicationRepository.deleteById(Integer.parseInt(id));

            return ResponseEntity.ok().build();

        }catch(NumberFormatException e){
            return new ResponseEntity<>("ID de publicación inválido", HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<?> getByUser(String id, String page){
        // TODO: COMPLETAR PAGINACION
        Integer defaultPage = 0;
        try{
            Optional<User> user = userRepository.findById(Integer.parseInt(id));
            if(user.isEmpty()) return new ResponseEntity<>("El usuario no existe", HttpStatus.BAD_REQUEST);

            Sort sort = Sort.by("createdAt").descending();

            List<PublicationResponse> listado = publicationRepository.findAllByUserId(Integer.parseInt(id), sort).stream()
                    .map( publication -> map(publication)  )
                    .toList();

            if( page != null && Integer.parseInt(page) > 0 ) defaultPage = Integer.parseInt(page);

            Pageable pageable = PageRequest.of(defaultPage  , 10);

            Page<PublicationResponse> paging = getPage(listado, pageable);

            PaginationResponse paginationResponse = PaginationResponse.builder()
                    .totalElements(paging.getTotalElements())
                    .totalPages(paging.getTotalPages())
                    .currentPage( paging.getNumber() == 0 ? paging.getNumber() + 1 : paging.getNumber() )
                    .itemsPerPage(paging.getSize())
                    .content(paging.getContent())
                    .build();

            return new ResponseEntity<>(paginationResponse, HttpStatus.OK);

        }catch (NumberFormatException e){
            return new ResponseEntity<>("ID de usuario inválido", HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<?> upload(MultipartFile file, String id){
        Integer publicationId = null;
        try{
            publicationId = Integer.parseInt(id);
        }catch(NumberFormatException e){
            return new ResponseEntity<>("ID de publicacion inválido", HttpStatus.BAD_REQUEST);
        }
        Optional<Publication> publication = publicationRepository.findById(publicationId);
        if(publication.isEmpty()){
            return new ResponseEntity<>("La publicación no existe", HttpStatus.BAD_REQUEST);
        }

        User currentUser = authService.getCurrentUser();
        String location = "src/main/resources/static/publications";
        FileUploadService fileUploadService = new FileUploadServiceImpl(location);

        String image = fileUploadService.almacenarArchivo(file);
        String path = fileUploadService.cargarArchivo(image).toString();

        Map<String, String> response = new HashMap<>();
        response.put("filename", image);
        response.put("path", path);
        response.put("message", "Archivo subido correctamente");

        Optional<Publication> existPublication = publicationRepository.findByIdAndUserId(publication.get().getId(),
                currentUser.getId());

        if( existPublication.isEmpty() || image == null ){
            return new ResponseEntity<>("Error al subir archivo", HttpStatus.BAD_REQUEST);
        }

        publication.get().setFile(image);
        publicationRepository.save(publication.get());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public Resource getFile(String file){
        String location = "src/main/resources/static/publications";
        FileUploadService fileService = new FileUploadServiceImpl(location);
        return fileService.cargarComoRecurso(file);
    }

    public ResponseEntity<?> feed(String page){
        Integer defaultPage = 0;
        try{
            if( page != null ) defaultPage = Integer.parseInt(page);
            Pageable pageable = PageRequest.of(defaultPage, 10);
            User currentUser = authService.getCurrentUser();

            List<PublicationResponse> feed = publicationRepository.getFeed(currentUser.getId()).stream()
                    .map( publication -> map(publication) )
                    .toList();

            Page<PublicationResponse> response = getPage(feed, pageable);

            Map<String, Object> result = new HashMap<>();
            result.put("totalElements", response.getTotalElements());
            result.put("itemsPerPage", response.getSize());
            result.put("totalPages", response.getTotalPages());
            result.put("currentPage", response.getNumber());
            result.put("content", response.getContent());

            return new ResponseEntity<>(result, HttpStatus.OK);

        }catch(NumberFormatException e){
            return new ResponseEntity<>("Parámetro page invalido", HttpStatus.BAD_REQUEST);
        }
    }

    private Page<PublicationResponse> getPage(List<PublicationResponse> listado, Pageable pageable) {
        int pageSize = pageable.getPageSize();
        int pageNumber = pageable.getPageNumber();
        int offSet = pageNumber * pageSize;
        int total = listado.size();
        List<PublicationResponse> pagedList = listado.stream()
                .skip(offSet)
                .limit(pageSize)
                .toList();
        return new PageImpl<>(pagedList, pageable, total);
    }

    private PublicationResponse map(Publication publication){
        return PublicationResponse.builder()
                .publicationId(publication.getId())
                .userId(publication.getUser().getId())
                .userNick(publication.getUser().getNick())
                .image(publication.getUser().getImage())
                .text(publication.getText())
                .file(publication.getFile())
                .createdAt(publication.getCreatedAt())
                .build();
    }


}
