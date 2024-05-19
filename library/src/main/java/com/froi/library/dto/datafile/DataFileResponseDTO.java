package com.froi.library.dto.datafile;

import lombok.Value;

import java.util.List;

@Value
public class DataFileResponseDTO {
    Integer record;
    List<DataFileError> errors;
}
