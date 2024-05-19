package com.froi.library.dto.datafile;

import com.froi.library.enums.datafile.DataFileErrorType;
import lombok.Value;

@Value
public class DataFileError {
    Integer line;
    String type;
    String description;
}
