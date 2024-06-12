import * as React from 'react';
import { useState, useEffect } from 'react';
import { DataGrid } from '@mui/x-data-grid';
import axios from 'axios';
import { useCookies } from 'react-cookie';
import CalendarMonthOutlinedIcon from '@mui/icons-material/CalendarMonthOutlined';
import { Card, Grid, CardHeader, CardContent, TextField, InputAdornment } from '@mui/material';

const columns = [
  { field: 'id', headerName: 'Código', width: 150 },
  { field: 'book', headerName: 'Libro', width: 200 },
  { field: 'title', headerName: 'Titulo', width: 150 },
  { field: 'loan_date', headerName: 'Fecha de Préstamo', width: 150 },
  { field: 'student', headerName: 'Estudiante', width: 120 },
];

export const API_URL = process.env.REACT_APP_URL_BACKEND;

export default function TodayLoansToDue() {
  const [date, setDate] = useState('');
  const [rows, setRows] = useState([]);
  const [loading, setLoading] = useState(true);
  const [page, setPage] = useState(0);
  const [pageSize, setpageSize] = useState(5)
  const [cookies] = useCookies(['jwt']);

  useEffect(() => {
    const setCurrentDate = () => {
      const curDate = new Date();
      const year = curDate.getFullYear();
      const month = String(curDate.getMonth() + 1).padStart(2, '0');
      const day = String(curDate.getDate()).padStart(2, '0');
      setDate(`${year}-${month}-${day}`);
      console.log(date);
    }
    setCurrentDate();
  }, []);

  useEffect(() => {
    const fetchData = async () => {
      setLoading(true);
      try {
        const response = await axios.get(`${API_URL}/v1/bookloan/today/${date}`, {
          headers: {
            Authorization: cookies.jwt,
          },
        });
        const content = response.data;
        console.log(content);
        const rowsWithId = content.map(item => ({
          ...item,
          id: item.id,
        }));
        setRows(rowsWithId);
      } catch (error) {
        console.error('Error fetching data:', error);
      }
      setLoading(false);
    };
    fetchData();
  }, [date]);

  return (
    <Card>
      <CardHeader title='Listado de Prestamos' titleTypographyProps={{ variant: 'h6' }} />
      <CardContent sx={{ display: 'flex', flexDirection: 'column', alignItems: 'center' }}>
        <Grid container spacing={5}>
          <Grid item xs={12}>
            <TextField
              fullWidth
              label='Fecha de Devolución'
              type='date'
              required
              value={date}
              onChange={(e) => setDate(e.target.value)}
              InputProps={{
                startAdornment: (
                  <InputAdornment position='start'>
                    <CalendarMonthOutlinedIcon />
                  </InputAdornment>
                )
              }}
            />
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
