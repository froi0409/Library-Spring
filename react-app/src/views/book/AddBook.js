import {
  Card,
  CardHeader,
  CardContent,
  Grid,
  Button,
  TextField,
  InputAdornment,
  Alert,
} from '@mui/material';
import { BookOutlined, BadgeOutlined } from '@mui/icons-material';

import { useState, useEffect } from 'react';
import BookListTable from './BooksListTable';
import CalendarMonthOutlined from '@mui/icons-material/CalendarMonthOutlined';
import PaymentsOutlinedIcon from '@mui/icons-material/PaymentsOutlined';
import Inventory2OutlinedIcon from '@mui/icons-material/Inventory2Outlined';
import axios from 'axios';
import { useCookies } from 'react-cookie';

export const API_URL = process.env.REACT_APP_URL_BACKEND;

const AddBook = () => {
  
  const [code, setCode] = useState('');
  const [title, setTitle] = useState('');
  const [author, setAuthor] = useState('');
  const [publisher, setPublisher] = useState('');
  const [publishDate, setPublishDate] = useState('');
  const [cost, setCost] = useState('');
  const [stock, setStock] = useState('');

  const [severity, setSeverity] = useState('')
  const [responseMessage, setResponseMessage] = useState('');
  const [cookies] = useCookies(['jwt']);

  const handleSubmit = async (e) => {
    e.preventDefault();
    const formData = {
      code,
      title,
      author,
      publisher,
      publishDate,
      cost,
      stock
    }

    try {
      const response = await axios.post(`${API_URL}/v1/book`, formData, {
        headers: {
          Authorization: cookies.jwt
        }
      });

      if (response.status === 201) {
        setSeverity('success')
        setResponseMessage('Libro creado con éxito')
        setCode('');
        setTitle('');
        setAuthor('');
        setPublisher('');
        setPublishDate('');
        setCost('');
        setStock('');
      } else {
        setSeverity('error');
        setResponseMessage('Ourrió un error al crear el libro');
      }
    } catch (error) {
      if (error.response) {
        setSeverity('error');
        setResponseMessage('Ourrió un error al crear el libro: ' + error.response.data);
      } else {
        setSeverity('error');
        setResponseMessage('Ourrió un error al crear el libro');
      }
    }

  }

  return (
    <>
      <Card>
        <Grid>
          {severity != '' && (
            <Alert severity={severity}>
              {responseMessage}
            </Alert>
          )
          }
        </Grid>
        <CardHeader title="Agregar Libro" titleTypographyProps={{ variant: 'h6' }} />
        <CardContent sx={{ alignItems: 'center', justifyContent: 'center' }}>
          <form onSubmit={handleSubmit}>
            <Grid container spacing={5}>
              <Grid item xs={12}>
                <Grid container spacing={5}>
                <Grid item xs={6}>
                  <TextField
                      fullWidth
                      required
                      label="Código de Libro"
                      placeholder="ISBN válido"
                      value={code}
                      onChange={(e) => setCode(e.target.value)}
                      InputProps={{
                        startAdornment: (
                          <InputAdornment position="start">
                            <BookOutlined />
                          </InputAdornment>
                        ),
                      }}
                    />
                  </Grid>
                  <Grid item xs={6}>
                  <TextField
                      fullWidth
                      required
                      label="Título del Libro"
                      placeholder="Nombre del Libro"
                      value={title}
                      onChange={(e) => setTitle(e.target.value)}
                      InputProps={{
                        startAdornment: (
                          <InputAdornment position="start">
                            <BookOutlined />
                          </InputAdornment>
                        ),
                      }}
                    />
                  </Grid>
                </Grid>
              </Grid>
              <Grid item xs={12}>
                <Grid container spacing={5} alignItems="center">
                  <Grid item xs={6}>
                    <TextField
                      fullWidth
                      required
                      label="Autor"
                      placeholder="Nombre del Autor"
                      value={author}
                      onChange={(e) => setAuthor(e.target.value)}
                      InputProps={{
                        startAdornment: (
                          <InputAdornment position="start">
                            <BookOutlined />
                          </InputAdornment>
                        ),
                      }}
                    />
                  </Grid>
                  <Grid item xs={6}>
                  <TextField
                      fullWidth
                      required
                      label="Editorial"
                      placeholder="Nommbre de la Editorial"
                      value={publisher}
                      onChange={(e) => setPublisher(e.target.value)}
                      InputProps={{
                        startAdornment: (
                          <InputAdornment position="start">
                            <BookOutlined />
                          </InputAdornment>
                        ),
                      }}
                    />
                  </Grid>
                </Grid>
              </Grid>
              <Grid item xs={12}>
                <TextField
                  fullWidth
                  required
                  label="Fecha de Publicación"
                  type='date'
                  value={publishDate}
                  onChange={(e) => setPublishDate(e.target.value)}
                  InputProps={{
                    startAdornment: (
                      <InputAdornment position="start">
                        <CalendarMonthOutlined />
                      </InputAdornment>
                    ),
                  }}
                />
              </Grid>
              <Grid item xs={12}>
                <Grid container spacing={5}>
                  <Grid item xs={6}>
                    <TextField
                      fullWidth
                      required
                      label="Costo del Libro"
                      type='number'
                      value={cost}
                      onChange={(e) => setCost(e.target.value)}
                      InputProps={{
                        startAdornment: (
                          <InputAdornment position="start">
                            <PaymentsOutlinedIcon />
                          </InputAdornment>
                        )
                      }}
                    />
                  </Grid>
                  <Grid item xs={6}>
                  <TextField
                      fullWidth
                      required
                      label="Cantidad"
                      type='number'
                      value={stock}
                      onChange={(e) => setStock(e.target.value)}
                      InputProps={{
                        startAdornment: (
                          <InputAdornment position="start">
                            <Inventory2OutlinedIcon />
                          </InputAdornment>
                        )
                      }}
                    />
                  </Grid>
                </Grid>
              </Grid>
              <Grid item xs={12}>
                <Grid container spacing={5}>
                  <Grid item xs={3}>
                    <Button type="submit" variant="contained" size="large" fullWidth>
                      Agregar Libro
                    </Button>
                  </Grid>
                  <Grid item xs={9}>
                    {}
                  </Grid>
                </Grid>
              </Grid>
            </Grid>
          </form>
        </CardContent>
      </Card>
      <Grid container mt={2} spacing={5}>
        <Grid item xs={12}>
          <BookListTable />
        </Grid>
      </Grid>
    </>
  );
}

export default AddBook;
