import { Card, CardHeader, CardContent, Grid, Button, TextField, InputAdornment } from '@mui/material';
import { BookOutlined, BadgeOutlined } from '@mui/icons-material';
import { useState, useEffect } from 'react';
import axios from 'axios'; // Asegúrate de importar axios
import { useCookies } from 'react-cookie'; // Importar useCookies

export const API_URL = process.env.REACT_APP_URL_BACKEND;

const BookLoan = () => {
    const [validStudent, setValidStudent] = useState(false);
    const [studentId, setStudentId] = useState('');
    const [findBookCode, setFindBookCode] = useState('');
    const [studentFound, setStudentFound] = useState(null); // Nuevo estado para manejar la respuesta de la API
    const [cookies] = useCookies(['jwt']);

    useEffect(() => {
        const timeoutId = setTimeout(() => {
            if (studentId) {
                axios.get(`${API_URL}/v1/student/${studentId}`, {
                    headers: {
                        'Authorization': cookies.jwt,
                    },
                })
                .then(response => {
                    if (response.status === 200) {
                        setStudentFound(true);
                        setValidStudent(true);
                    } else {
                        setStudentFound(false);
                        setValidStudent(false);
                    }
                })
                .catch(error => {
                    setStudentFound(false);
                    setValidStudent(false);
                });
            }
        }, 1000);

        return () => clearTimeout(timeoutId);
    }, [studentId]);

    const handleSubmit = (e) => {
        e.preventDefault();
    }

    const handleSearch = (e) => {
        e.preventDefault();
    }

    return (
        <Card>
            <CardHeader title='Prestamo de Libro' titleTypographyProps={{ variant: 'h6' }} />
            <CardContent sx={{ alignItems: 'center', justifyContent: 'center' }}>
                <form onSubmit={handleSubmit}>
                    <Grid container spacing={5}>
                        <Grid item xs={12}>
                            <TextField
                                fullWidth
                                label='Carnet del Estudiante'
                                placeholder='Ej: 201815100'
                                required
                                value={studentId}
                                onChange={(e) => setStudentId(e.target.value)}
                                InputProps={{
                                    startAdornment: (
                                        <InputAdornment position='start'>
                                            <BadgeOutlined />
                                        </InputAdornment>
                                    ),
                                    style: {
                                        backgroundColor: studentFound === null ? 'inherit' : studentFound ? '#d4edda' : '#f8d7da',
                                    }
                                }}
                            />
                        </Grid>
                        <Grid item xs={12}>
                            <Grid container spacing={5} alignItems='center'>
                                <Grid item xs={10}>
                                    <TextField
                                        fullWidth
                                        label='Código de Libro'
                                        placeholder='ISBN válido'
                                        required
                                        value={findBookCode}
                                        onChange={(e) => setFindBookCode(e.target.value)}
                                        InputProps={{
                                            startAdornment: (
                                                <InputAdornment position='start'>
                                                    <BookOutlined />
                                                </InputAdornment>
                                            )
                                        }}
                                    />
                                </Grid>
                                <Grid item xs={2}>
                                    <Button onClick={handleSearch} variant='contained' size='large' fullWidth>
                                        Buscar Libro
                                    </Button>
                                </Grid>
                            </Grid>
                        </Grid>
                        <Grid item xs={12}>
                            <Grid container spacing={5}>
                                <Grid item xs={2}>
                                    <Button type='submit' variant='contained' size='large' fullWidth>
                                        Crear Prestamo
                                    </Button>
                                </Grid>
                                <Grid item xs={10}>
                                    
                                </Grid>
                            </Grid>
                        </Grid>
                    </Grid>
                </form>
            </CardContent>
        </Card>
    )
}

export default BookLoan;
