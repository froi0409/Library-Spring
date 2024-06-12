package com.froi.library.dto.datafile;

import lombok.Value;

import java.util.List;

@Value
public class DataFileResponseDTO {
    Integer records;
    List<DataFileError> errors;
}
