import * as React from 'react';
import { useState, useEffect } from 'react';
import { DataGrid } from '@mui/x-data-grid';
import axios from 'axios';
import { useCookies } from 'react-cookie';
import CalendarMonthOutlinedIcon from '@mui/icons-material/CalendarMonthOutlined';
import { Card, Grid, CardHeader, CardContent, TextField, InputAdornment, Button, Alert } from '@mui/material';

const columns = [
  { field: 'id', headerName: 'Código', width: 150 },
  { field: 'book', headerName: 'Libro', width: 200 },
  { field: 'loanDate', headerName: 'Fecha de Préstamo', width: 150 },
  { field: 'student', headerName: 'Estudiante', width: 120 },
  { field: 'loanTotal', headerName: 'Total Prestamo', width: 120 },
  { field: 'delayTotal', headerName: 'Total Mora', width: 120 },
];

export const API_URL = process.env.REACT_APP_URL_BACKEND;

export default function RevenueInformation() {
  const [rows, setRows] = useState([]);
  const [loading, setLoading] = useState(true);
  const [page, setPage] = useState(0);
  const [pageSize, setpageSize] = useState(5)
  const [cookies] = useCookies(['jwt']);
  const [totalLoanInfo, setTotalLoanInfo] = useState('');
  const [totalDelayInfo, setTotalDelayInfo] = useState('');
  const [totalRevenueInfor, setTotalRevenueInfor] = useState('');
  const [startDate, setStartDate] = useState('date');
  const [endDate, setEndDate] = useState('date');
  const [errorMessage, setErrorMessage] = useState('');

  useEffect(() => {
    const fetchData = async () => {
      setLoading(true);
      try {
        const response = await axios.get(`${API_URL}/v1/bookloan/revenue`, {
          headers: {
            Authorization: cookies.jwt,
          },
        });
        setErrorMessage('');

        const content = response.data.bookLoanList;
        setRows(content);
        const { totalloan, totaldelay, totalrevenue } = response.data.revenueInformation;
        console.log(response.data.revenueInformation)
        setTotalLoanInfo(totalloan.toFixed(2));
        setTotalDelayInfo(totaldelay.toFixed(2));
        setTotalRevenueInfor(totalrevenue.toFixed(2));
      }
       catch (error) {
        console.error('Error fetching data:', error);
      }
      setLoading(false);
    };
    fetchData();
  }, []);

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const response = await axios.get(`${API_URL}/v1/bookloan/revenue/${startDate}/${endDate}`, {
        headers: {
          Authorization: cookies.jwt
        }
      })
      setErrorMessage('');

      console.log('response', response.data)
      const content = response.data.bookLoanList;
      setRows(content);
      const { totalloan, totaldelay, totalrevenue } = response.data.revenueInformation;
      console.log(response.data.revenueInformation)
      setTotalLoanInfo(totalloan.toFixed(2));
      setTotalDelayInfo(totaldelay.toFixed(2));
      setTotalRevenueInfor(totalrevenue.toFixed(2));
    } catch (error) {
      if (error.response) {
        setErrorMessage(`Error al Generar Reporte: ${error.response.data}`)
      }
    }
  }

  return (
    <Card>
      <CardHeader title='Listado de Ganancias' titleTypographyProps={{ variant: 'h6' }} />
      <CardContent sx={{ display: 'flex', flexDirection: 'column', alignItems: 'center' }}>
        <Grid container spacing={5}>
          <Grid item xs={12}>
            <h1>
              Información
            </h1>
            <h3>
              Ganancias por Prestamo: Q.{totalLoanInfo}
            </h3>
            <h3>
              Ganancias por Moras: Q.{totalDelayInfo}
            </h3>
            <h3>
              Ganancias Totales: Q.{totalRevenueInfor}
            </h3>
          </Grid>
          <Grid item xs={12}>
            {errorMessage != '' && (
              <Alert severity='error'>
                {errorMessage}
              </Alert>
            )
            }
          </Grid>
          <Grid item xs={12}>
            <Grid container spacing={5}>
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
