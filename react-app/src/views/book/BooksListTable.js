import * as React from 'react';
import { useState, useEffect } from 'react';
import { DataGrid } from '@mui/x-data-grid';
import axios from 'axios';
import { useCookies } from 'react-cookie';
import { Card, CardHeader, CardContent, Grid, TextField,InputAdornment } from '@mui/material';
import SearchOutlinedIcon from '@mui/icons-material/SearchOutlined';

const columns = [
  { field: 'code', headerName: 'Código', width: 200 },
  { field: 'title', headerName: 'Titulo', align: 'right', width: 200 },
  { field: 'publisher', headerName: 'Editorial', align: 'right', width: 200 },
  { field: 'author', headerName: 'Autor', align: 'right', width: 200 },
  {
    field: 'loan_count',
    headerName: 'Disponibles',
    align: 'right',
    type: 'number',
    width: 200,
  },
];

export const API_URL = process.env.REACT_APP_URL_BACKEND; // Reemplaza esto con la URL de tu API

export default function BookListTable() {
  const [rows, setRows] = useState([]);
  const [loading, setLoading] = useState(true);
  const [page, setPage] = useState(0);
  const [pageSize, setPageSize] = useState(5);
  const [rowCount, setRowCount] = useState(0);
  const [cookies] = useCookies(['jwt']);
  const [searchTerm, setSearchTerm] = useState('');

  useEffect(() => {
    const fetchData = async () => {
        setLoading(true);
        try {
          const response = await axios.get(`${API_URL}/v1/book/all`, {
            params: {
              searchTerm
            },
            headers: {
              Authorization: cookies.jwt,
            },
          });
          const content = response.data;
          console.log(content)
          // Agregar un nuevo campo "id" con el valor del campo "code" a cada elemento
          const rowsWithId = content.map(item => ({
            ...item,
            id: item.code // Aquí asignamos el valor del campo "code" al campo "id"
          }));
      
          setRows(rowsWithId);
        } catch (error) {
          console.error('Error fetching data:', error);
        }
        setLoading(false);
      };

      fetchData();
  }, [, searchTerm]);

  return (
    <Card>
        <CardHeader title='Listado de Libros' titleTypographyProps={{ variant: 'h6' }} />
        <CardContent sx={{ display: 'flex', flexDirection: 'column', alignItems: 'center' }}>
          <Grid container spacing={5}>
            <Grid item xs={12}>
              <TextField
                fullWidth
                label='Filtro'
                type='text'
                required
                value={searchTerm}
                onChange={(e) => setSearchTerm(e.target.value)}
                InputProps={{
                  startAdornment: (
                    <InputAdornment position='start'>
                      <SearchOutlinedIcon />
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
