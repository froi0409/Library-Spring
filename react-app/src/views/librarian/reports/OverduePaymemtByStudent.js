import * as React from 'react';
import { useState, useEffect } from 'react';
import { DataGrid } from '@mui/x-data-grid';
import axios from 'axios';
import { useCookies } from 'react-cookie';
import CalendarMonthOutlinedIcon from '@mui/icons-material/CalendarMonthOutlined';
import { Card, Grid, CardHeader, CardContent, TextField, InputAdornment, Alert } from '@mui/material';
import { BadgeOutlined } from '@mui/icons-material';
import Button from '@mui/material/Button';

const columns = [
  { field: 'id', headerName: 'Código', width: 150 },
  { field: 'student', headerName: 'Estudiante', width: 150 },
  { field: 'book', headerName: 'Libro', width: 150 },
  { field: 'loanDate', headerName: 'Fecha de Préstamo', width: 150 },
  { field: 'returnedDate', headerName: 'Fecha de Retorno', width: 150 },
  { field: 'loanTotal', headerName: 'Total Prestamo' },
  { field: 'delayTotal', headerName: 'Total Mora' }
];

export const API_URL = process.env.REACT_APP_URL_BACKEND;

export default function OverduePaymentByStudent() {
  
  const [rows, setRows] = useState([]);
  const [loading, setLoading] = useState(true);
  const [page, setPage] = useState(0);
  const [pageSize, setpageSize] = useState(5)
  const [cookies] = useCookies(['jwt']);

  const [studentId, setStudentId] = useState('');
  const [startDate, setStartDate] = useState('date');
  const [endDate, setEndDate] = useState('date');
  const [responseMessage, setResponseMessage] = useState('')


  const [studentFound, setStudentFound] = useState(null);


  useEffect(() => {
    const timeoutId = setTimeout(() => {
      axios
      .get(`${API_URL}/v1/student/${studentId}`, {
        headers: {
          Authorization: cookies.jwt,
        },
      })
      .then((response) => {
        if (response.status === 200) {
          setStudentFound(true);
          axios
            .get(`${API_URL}/v1/bookloan/overduePaymentByStudent/${studentId}`, {
              headers: {
                Authorization: cookies.jwt,
              }
            })
            .then((responseReport) => {
              if (responseReport.status === 200) {
                setResponseMessage('');
                const content = responseReport.data;
                setRows(content);
              }
            });
        } else {
          setStudentFound(false);
        }
      })
      .catch((error) => {
        setStudentFound(false);
      })
    }, 1000);

    return () => clearTimeout(timeoutId);
  }, [studentId]);

  const handleSubmit = async (e) => {
    setLoading(true);
    try {
      const response = await axios.get(`${API_URL}/v1/bookloan/overduePaymentByStudent/${studentId}/${startDate}/${endDate}`, {
        headers: {
          Authorization: cookies.jwt,
        },
      });
      setResponseMessage('');
      const content = response.data;
      setRows(content);
    } catch (error) {
      console.error('Error fetching data:', error);
      if (error.response) {
        setResponseMessage('Ocurrió un error al generar el reporte: ' + error.response.data);
      } else {
        setResponseMessage('Ocurrió un error al generar el reporte');
      }
    }
    setLoading(false);
  }


  return (
    <Card>
      <CardHeader title='Listado de Moras Pagadas' titleTypographyProps={{ variant: 'h6' }} />
      <CardContent sx={{ display: 'flex', flexDirection: 'column', alignItems: 'center' }}>
        <Grid container spacing={5}>
          <Grid item xs={12}>
            {responseMessage != '' && (
              <Alert severity='error'>
                {responseMessage}
              </Alert>
            )
            }
          </Grid>
          <Grid item xs={12}>
            <Grid container spacing={5}>
              <Grid item xs={12}>
                <TextField
                  fullWidth
                  label="Carnet del Estudiante"
                  placeholder="Ej: 201815100"
                  required
                  value={studentId}
                  onChange={(e) => setStudentId(e.target.value)}
                  InputProps={{
                    startAdornment: (
                      <InputAdornment position="start">
                        <BadgeOutlined />
                      </InputAdornment>
                    ),
                    style: {
                      backgroundColor:
                        studentFound === null ? 'inherit' : studentFound ? '#d4edda' : '#f8d7da',
                    },
                  }}
                />
              </Grid>
              <Grid item xs={4}>
                <TextField
                  fullWidth
                  label='Fecha Inicial'
                  type="date"
                  required
                  value={startDate}
                  onChange={(e) => setStartDate(e.target.value)}
                  InputProps={{
                    startAdornment: (
                      <InputAdornment position='start'>
                        <CalendarMonthOutlinedIcon />
                      </InputAdornment>
                    )
                  }}
                />
              </Grid>
              <Grid item xs={4}>
                <TextField
                  fullWidth
                  label='Fecha Final'
                  type="date"
                  required
                  value={endDate}
                  onChange={(e) => setEndDate(e.target.value)}
                  InputProps={{
                    startAdornment: (
                      <InputAdornment position='start'>
                        <CalendarMonthOutlinedIcon />
                      </InputAdornment>
                    )
                  }}
                />
              </Grid>
              <Grid item xs={4}>
                <Button onClick={handleSubmit} type="submit" variant="contained" size="large" fullWidth>
                  Buscar
                </Button>
              </Grid>
            </Grid>
          </Grid>
          <Grid item xs={12}>
            <div style={{ height: 400, width: '100%' }}>
              <DataGrid
                rows={rows}
                columns={columns}
                initialState={{
                  pagination: {
                    paginationModel: { page: 0, pageSize: 5 },
                  },
                }}
                pageSizeOptions={[5, 10, 25, 50]}
                checkboxSelection
              />
            </div>
          </Grid>
        </Grid>
      </CardContent>
    </Card>
  );
}
