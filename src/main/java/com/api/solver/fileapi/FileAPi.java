package com.api.solver.fileapi;


import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/api/files")
public class FileAPi {


    @PostMapping(value = "/upload",consumes = "multipart/form-data")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) throws  IOException {

           byte[] bytes = file.getBytes();


           Files.write(getDir(file.getOriginalFilename()), bytes);

           return ResponseEntity.ok("File uploaded successfully.");


    }


    @GetMapping("/{filename}")

    public ResponseEntity<Resource> getFile(@PathVariable String filename)   {

        try {
            Path path = getDir(filename);
            Resource resource = new UrlResource(path.toUri());
            if (resource.exists() && resource.isReadable()) {
                    return ResponseEntity.ok()
                            .header(HttpHeaders.CONTENT_DISPOSITION, "attachement; filename=\"" + resource.getFilename() + "\"")
                            .body(resource);
            }
            else{
                return ResponseEntity.notFound().build();
            }
        } catch (IOException e) {
           return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }

    }

    public Path getDir(String filename){
        String absolutePath = Paths.get("").toAbsolutePath().toString();
        String microservicePath ="/Solver-API-for-property-estimation/src/main/resources/excel/";
        String joinPaths = absolutePath+microservicePath+filename;
        return Paths.get(joinPaths);
    }

}
