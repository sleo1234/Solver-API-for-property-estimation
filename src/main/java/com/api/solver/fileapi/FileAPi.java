package com.api.solver.fileapi;


import com.api.solver.xlutil.ExcelUtil;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;

@RestController
@RequestMapping("/api/files")
public class FileAPi {


    @PostMapping(value = "/upload",consumes = "multipart/form-data")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) throws  IOException {

           byte[] bytes = file.getBytes();


           Files.write(getDir(file.getOriginalFilename()), bytes);

           return ResponseEntity.ok("File uploaded successfully.");


    }

    public void resolveFileName(MultipartFile file){

        String path = getDir(file.getOriginalFilename()).toString();

        //list all files in the above path
        File folder = new File(path);
        File[] listOfFiles = folder.listFiles();

        for (File f : listOfFiles) {
            if (f.getName().equals(file.getOriginalFilename())) {
                System.out.println(file.getName());
            }
        }



    }


    @GetMapping("/{filename}")

    public ResponseEntity<ExcelFileResponseBody> getFileName(@PathVariable String filename)   {

        ExcelFileResponseBody body = new ExcelFileResponseBody();
        HashMap<String,Double> notFound = new HashMap<>();

        try {
            Path path = getDir(filename);
            Resource resource = new UrlResource(path.toUri());
            if (resource.exists() && resource.isReadable()) {
                ExcelUtil excelUtil = new ExcelUtil("/excel/"+filename,2);
                 body.setMapData(excelUtil.getMapData());
                    return new ResponseEntity<>(body,HttpStatus.OK);
            }
            else{
                notFound.put("Resource file not found",-1.0);
                body.setMapData(notFound);
                return new ResponseEntity<>(body,HttpStatus.OK);
            }
        } catch (IOException e) {
            notFound.put("Server error",-1.0);
            body.setMapData(notFound);
           return new ResponseEntity<>(body,HttpStatus.OK);
        }

    }

    public Path getDir(String filename){
        String absolutePath = Paths.get("").toAbsolutePath().toString();
        String microservicePath ="/Solver-API-for-property-estimation/src/main/resources/excel/";
        String joinPaths = absolutePath+microservicePath+filename;
        return Paths.get(joinPaths);
    }

}
