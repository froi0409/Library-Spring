import * as React from 'react';
import Table from '@mui/material/Table';
import TableBody from '@mui/material/TableBody';
import TableCell from '@mui/material/TableCell';
import TableContainer from '@mui/material/TableContainer';
import TableHead from '@mui/material/TableHead';
import TableRow from '@mui/material/TableRow';
import Paper from '@mui/material/Paper';
import Button from '@mui/material/Button';
import { Card, Grid, CardHeader, CardContent, TextField, InputAdornment } from '@mui/material';
import { BadgeOutlined } from '@mui/icons-material';

import { useCookies } from 'react-cookie';
import { useState, useEffect } from 'react';
import axios from 'axios';

export const API_URL = process.env.REACT_APP_URL_BACKEND;

const ReturnBook = ({ loanId, returnDate }) => {
    
    const [bookId, setBookId] = useState('');
    const [studentId, setStudentId] = useState('');
    const [loanDate, setLoanDate] = useState('');
    const [loanTotal, setLoanTotal] = useState('');
    const [delayTotal, setDelayTotal] = useState('');    
    const [cookies] = useCookies(['jwt']);

    useEffect(() => {
        const fetchData = async () => {
            try {
                const response = await axios.get(`${API_URL}/v1/bookloan/${loanId}/${returnDate}`, {
                    headers: {
                        Authorization: cookies.jwt
                    }
                });
                if (response.status === 200) {
                    console.log(response.data);
                    const { book, student, loanDate, loanTotal, delayTotal } = response.data;
                    setBookId(book);
                    setStudentId(student);
                    setLoanDate(loanDate);
                    setLoanTotal(loanTotal);
                    setDelayTotal(delayTotal);
                } else {
                    console.log(response.data);
                }
            } catch (error) {
                console.error(error);
                console.error(error.response.data)
            }
        }
        
        fetchData();
    }, [loanId, returnDate]);

    return (
        <Card>
            <CardHeader title='Información del Prestamo' titleTypographyProps={{ variant: 'h6' }} />
            <CardContent sx={{ alignItems: 'center', justifyContent: 'center' }}>
                <Grid container spacing={5}>
                    <Grid item xs={12}>
                        <h1>
                            Prestamo - {loanId} - {studentId}                         
                        </h1>
                        <h2>
                            Libro - {bookId}
                        </h2>
                        <h3>
                            Fecha - {loanDate}
                        </h3>
                    </Grid>
                    <Grid item xs={12}>
                        <TableContainer component={Paper}>
                            <Table sx={{ minWidth: 650 }} size="small" aria-label="a dense table">
                                <TableHead>
                                <TableRow>
                                    <TableCell>Descripción</TableCell>
                                    <TableCell align="right">Total a pagar</TableCell>
                                </TableRow>
                                </TableHead>
                                <TableBody>
                                    <TableRow sx={{ '&:last-child td, &:last-child th': { border: 0 } }}>
                                        <TableCell component="th" scope="row">Pago por Préstamo</TableCell>
                                        <TableCell align="right">{loanTotal}</TableCell>
                                    </TableRow>
                                    <TableRow sx={{ '&:last-child td, &:last-child th': { border: 0 } }}>
                                        <TableCell component="th" scope="row">Mora</TableCell>
                                        <TableCell align="right">{delayTotal}</TableCell>
                                    </TableRow>
                                </TableBody>
                            </Table>
                        </TableContainer>
                    </Grid>
                </Grid>
            </CardContent>
        </Card>
    );
}

export default ReturnBook;
