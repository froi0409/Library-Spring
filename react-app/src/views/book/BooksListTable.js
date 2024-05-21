import * as React from 'react';
import { useState, useEffect } from 'react';
import { DataGrid } from '@mui/x-data-grid';
import axios from 'axios';
import { useCookies } from 'react-cookie';
import { Card, CardHeader, CardContent } from '@mui/material';

const columns = [
  { field: 'code', headerName: 'Código', width: 150 },
  { field: 'title', headerName: 'Titulo', width: 200 },
  { field: 'publisher', headerName: 'Editorial', width: 150 },
  { field: 'author', headerName: 'Autor', width: 150 },
  {
    field: 'loan_count',
    headerName: 'Disponibles',
    type: 'number',
    width: 120,
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

  useEffect(() => {
    const fetchData = async () => {
        setLoading(true);
        try {
          const response = await axios.get(`${API_URL}/v1/book/all`, {
            params: {
              page: page, // la API puede estar basada en 0
              size: pageSize,
            },
            headers: {
              Authorization: cookies.jwt,
            },
          });
          const { content, totalElements } = response.data; // Ajusta esto basado en la estructura de tu respuesta
          console.log(content)
          // Agregar un nuevo campo "id" con el valor del campo "code" a cada elemento
          const rowsWithId = content.map(item => ({
            ...item,
            id: item.code // Aquí asignamos el valor del campo "code" al campo "id"
          }));
      
          setRows(rowsWithId);
          setRowCount(totalElements);
        } catch (error) {
          console.error('Error fetching data:', error);
        }
        setLoading(false);
      };

      fetchData();
  }, [page, pageSize]);

  return (
    <Card>
        <CardHeader title='Listado de Libros' titleTypographyProps={{ variant: 'h6' }} />
        <CardContent sx={{ display: 'flex', flexDirection: 'column', alignItems: 'center' }}>
            <div style={{ flexGrow: 1, width: '100%' }}> {/* Usamos flexGrow: 1 para que el div ocupe todo el espacio vertical disponible */}
            <DataGrid
                rows={rows}
                columns={columns}
                pagination
                pageSize={pageSize}
                rowsPerPageOptions={[25, 50, 100]}
                rowCount={rowCount}
                paginationMode="server"
                onPageChange={(newPage) => setPage(newPage)}
                onPageSizeChange={(newPageSize) => setPageSize(newPageSize)}
                loading={loading}
                checkboxSelection
                sx={{ width: '100%' }}
            />
            </div> 
        </CardContent>
    </Card>
  );
}
