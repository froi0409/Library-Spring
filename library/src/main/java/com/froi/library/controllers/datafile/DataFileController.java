package com.froi.library.controllers.datafile;

import com.froi.library.dto.datafile.DataFileResponseDTO;
import com.froi.library.exceptions.UploadDataFileException;
import com.froi.library.services.datafile.DataFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/v1/datafile")
@CrossOrigin
public class DataFileController {
    
    private DataFileService dataFileService;
    
    @Autowired
    public DataFileController(DataFileService dataFileService) {
        this.dataFileService = dataFileService;
    }
    
    @PostMapping("/upload")
    @PreAuthorize("hasRole('LIBRARIAN')")
    public ResponseEntity<DataFileResponseDTO> uploadDataFile(@RequestParam("file") MultipartFile file) throws UploadDataFileException {
        return ResponseEntity
                .ok(dataFileService.handleDataFile(file));
    }
}
