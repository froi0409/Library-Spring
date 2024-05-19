package com.froi.library.services.datafile;

import com.froi.library.dto.datafile.DataFileError;
import com.froi.library.dto.datafile.DataFileResponseDTO;
import com.froi.library.exceptions.UploadDataFileException;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface DataFileService {
    DataFileResponseDTO handleDataFile(MultipartFile file) throws UploadDataFileException;
    List<String> verifySystemData();
    
    boolean insertEntity(List<DataFileError> errorsList, String[] columns, int line);
    
}
