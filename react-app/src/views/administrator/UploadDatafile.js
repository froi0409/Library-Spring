import * as React from 'react';
import { Button, Card, CardContent, CardHeader, Grid } from '@mui/material';
import CloudUploadIcon from '@mui/icons-material/CloudUpload';
import { styled } from '@mui/material/styles';
import axios from 'axios';
import { useState } from 'react';
import ErrorsDataFileReport from './ErrorsDataFileReport';
import { useCookies } from 'react-cookie';

export const API_URL = process.env.REACT_APP_URL_BACKEND;


const VisuallyHiddenInput = styled('input')({
  clip: 'rect(0 0 0 0)',
  clipPath: 'inset(50%)',
  height: 1,
  overflow: 'hidden',
  position: 'absolute',
  bottom: 0,
  left: 0,
  whiteSpace: 'nowrap',
  width: 1,
});

const UploadDataFile = () => {
  const [selectedFile, setSelectedFile] = useState(null);
  const [errorsReportData, setErrorsReportData] = useState([]);
  const [records, setRecords] = useState(0);
  const [cookies] = useCookies(['jwt']);
  const [data, setData] = useState([]);

  const handleSubmit = async (e) => {
    e.preventDefault();

    if (!selectedFile) return;

    const formData = new FormData();
    formData.append('file', selectedFile);

    try {
      const response = await axios.post(`${API_URL}/v1/datafile/upload`, formData, {
        headers: {
          'Content-Type': 'multipart/form-data',
          Authorization: cookies.jwt,
        },
      });
      
      setErrorsReportData(response.data.errors);
      setRecords(response.data.records);
    } catch (error) {
      console.error('Error uploading file:', error);
    }
  };

  const handleFileChange = (e) => {
    const file = e.target.files && e.target.files[0];
    setSelectedFile(file);
  };

  return (
    <Grid container spacing={5}>
      <Grid item xs={12}>
        <Card>
          <CardHeader title="Subir Archivo de Datos" titleTypographyProps={{ variant: 'h6' }} />
          <CardContent sx={{ minHeight: 100, alignItems: 'center', justifyContent: 'center' }}>
            <form onSubmit={handleSubmit}>
              <Grid container spacing={5}>
                <Grid item xs={12}>
                  <Grid container spacing={5} alignItems="center">
                    <Grid item xs={2}>
                      <Button
                        fullWidth
                        component="label"
                        variant="contained"
                        startIcon={<CloudUploadIcon />}
                      >
                        Subir Archivo
                        <VisuallyHiddenInput type="file" onChange={handleFileChange} />
                      </Button>
                    </Grid>
                    <Grid item xs={9}>
                      {selectedFile && (
                        <p style={{ margin: 0 }}>Archivo Seleccionado: {selectedFile.name}</p>
                      )}
                    </Grid>
                  </Grid>
                </Grid>
                <Grid item xs={12}>
                  <Button type="submit" variant="contained" size="large" fullWidth>
                    Procesar Archivo
                  </Button>
                </Grid>
              </Grid>
            </form>
          </CardContent>
        </Card>
      </Grid>
      <Grid item xs={12}>
        {errorsReportData && errorsReportData.length > 0 && (
          <Grid>
            <ErrorsDataFileReport records={records} rows={errorsReportData} />
          </Grid>
        )}
      </Grid>
    </Grid>
  );
};

export default UploadDataFile;
