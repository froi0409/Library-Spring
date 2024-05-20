import { Card, CardHeader, CardContent, Grid, Button, TextField, InputAdornment, Alert } from '@mui/material';
import { BookOutlined, BadgeOutlined } from '@mui/icons-material';
import { useState, useEffect } from 'react';
import axios from 'axios';
import { useCookies } from 'react-cookie';
import BooksOrderTable from './BooksOrderTable';
import { set } from 'lodash';

export const API_URL = process.env.REACT_APP_URL_BACKEND;

const BookLoan = () => {
    const [validStudent, setValidStudent] = useState(false);
    const [findSubmitted, setFindSubmitted] = useState(false);
    const [submitted, setSubmitted] = useState(false);
    const [studentId, setStudentId] = useState('');
    const [findBookCode, setFindBookCode] = useState('');
    const [studentFound, setStudentFound] = useState(null);
    const [messageType, setMessageType] = useState('');
    const [findMessageType, setFindMessageType] = useState('OK');
    const [booksList, setBooksList] = useState([]);
    const [response, setResponse] = useState('');
    const [cookies] = useCookies(['jwt']);
    const [maxBooks, setMaxBooks] = useState(0); // Estado para el número máximo de libros permitidos

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
                        axios.get(`${API_URL}/v1/student/loanCount/${studentId}`, {
                            headers: {
                                'Authorization': cookies.jwt,
                            }
                        })
                        .then(responseLoanCount => {
                            if (responseLoanCount.status === 200) {
                                console.log(responseLoanCount.data)
                                if (responseLoanCount.data !== undefined || response.data !== '') {
                                    setMaxBooks(Number(responseLoanCount.data));
                                }
                            }
                        })
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

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            const booksLoanList = booksList.map((book) => book.code);

            const formData = {
                bookCodes: booksLoanList,
                studentId: studentId
            }

            console.log('formdata',formData)

            console.log('prestamo realizado');
            const res = await axios.post(`${API_URL}/v1/bookloan`, formData, {
                headers: {
                    Authorization: cookies.jwt
                }
            })
            setSubmitted(true);
            setFindSubmitted(false);

            if (res.status === 201) {
                setMessageType('OK');
                setResponse('Prestamo creado con éxito');
                
                setStudentId('');
                setBooksList([]);
            } else {
                setMessageType('ERR');
                setResponse('No se pudo realizar el préstamo: ' + res.data.message)
            }
        } catch (error) {
            setSubmitted(true);
            setFindSubmitted(false);
            setMessageType('ERR');
            setResponse('No se pudo realizar el préstamo: ' + error.response.data)
        }
    };

    const updateBookInformation = async () => {
        try {
            const res = await axios.get(`${API_URL}/v1/book/${findBookCode}`, {
                headers: {
                    'Authorization': cookies.jwt,
                }
            });
            if (res.status === 200) {
                setFindMessageType('OK');
                const bookData = {
                    code: res.data.code,
                    title: res.data.title,
                    publisher: res.data.publisher,
                    author: res.data.author,
                };
                setBooksList(prevBooksList => {
                    if (prevBooksList.length < maxBooks) {
                        return [...prevBooksList, bookData];
                    } else {
                        setFindMessageType('ERR');
                        setResponse(`Solo se pueden agregar ${maxBooks} libros.`);
                        return prevBooksList;
                    }
                });
                setFindBookCode('');
            }
        } catch (error) {
            setFindMessageType('ERR');
            setResponse('Error al actualizar la información del libro');
        }
    };

    const handleSearch = async (e) => {
        setFindSubmitted(true);
        try {
            const res = await axios.get(`${API_URL}/v1/bookloan/availability/${findBookCode}`, {
                headers: {
                    'Authorization': cookies.jwt,
                },
            });
            if (res.status === 200) {
                if (res.data === "") {
                    setFindMessageType('ERR');
                    setResponse(`No Existe el libro ${findBookCode}, o no hay copias suficientes`);
                } else if (res.data > 0) {
                    updateBookInformation();
                } else {
                    setFindMessageType('ERR');
                    setResponse(`No hay copias suficientes de ${findBookCode}`);
                }
            } else {
                setFindMessageType('ERR');
                setResponse(`No hay copias suficientes de ${findBookCode}`);
            }
        } catch (error) {
            setFindSubmitted(true);
            setFindMessageType('ERR');
            setResponse('No se encontró el libro');
        } 
    };

    const handleRemoveBook = (code) => {
        setBooksList(prevBooksList => prevBooksList.filter(book => book.code !== code));
    };

    return (
        <Card>
            <Grid>
                {findSubmitted && response !== '' && (
                    <Alert severity={findMessageType === 'ERR' ? "error" : "success"}>{response}</Alert>
                )}
                {submitted && response !== '' && (
                    <Alert severity={messageType === 'OK' ? "success" : "error"}>{response}</Alert>
                )}
            </Grid>
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
                            <BooksOrderTable booksList={booksList} handleRemoveBook={handleRemoveBook} />
                        </Grid>
                        <Grid item xs={12}>
                            <Grid container spacing={5}>
                                <Grid item xs={2}>
                                    <Button type='submit' variant='contained' size='large' fullWidth>
                                        Crear Prestamo
                                    </Button>
                                </Grid>
                                <Grid item xs={10}>
                                    {}
                                </Grid>
                            </Grid>
                        </Grid>
                    </Grid>
                </form>
            </CardContent>
        </Card>
    );
}

export default BookLoan;
