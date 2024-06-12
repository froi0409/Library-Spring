package com.froi.library.controllers.datafile;

import com.froi.library.controllers.AbstractMvcTest;
import com.froi.library.controllers.exceptionhandler.GlobalExceptionHandler;
import com.froi.library.dto.datafile.DataFileError;
import com.froi.library.dto.datafile.DataFileResponseDTO;
import com.froi.library.exceptions.UploadDataFileException;
import com.froi.library.services.datafile.DataFileService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ContextConfiguration(classes = {DataFileController.class, DataFileService.class, GlobalExceptionHandler.class})
@AutoConfigureMockMvc
class DataFileControllerTest extends AbstractMvcTest {
    
    private static final Integer RECORDS = 5;
    private static final String FILE_NAME = "file";
    private static final String ORIGINAL_FILE_NAME = "testFile.txt";
    private static final String FILE_CONTENT = "Text Content";
    
    @MockBean
    private DataFileService dataFileService;
    
    @Test
    @WithMockUser(username = "librarian1", roles = {"LIBRARIAN"})
    void testUploadDataFile() throws Exception {
        // Arrange
        MockMultipartFile file = new MockMultipartFile(FILE_NAME, ORIGINAL_FILE_NAME, MediaType.TEXT_PLAIN_VALUE, FILE_CONTENT.getBytes());
        List<DataFileError> errorList = new ArrayList<>();
        DataFileResponseDTO responseDTO = new DataFileResponseDTO(RECORDS, errorList);
        // Configura la respuesta simulada del servicio
        when(dataFileService.handleDataFile(file)).thenReturn(responseDTO);
        
        // Act & Assert
        mockMvc.perform(multipart("/v1/datafile/upload")
                        .file(file)
                        .with(csrf())
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                )
                .andExpect(status().isOk());
    }
    
}
