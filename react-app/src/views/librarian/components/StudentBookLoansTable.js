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
import ReturnBook from './ReturnBook';

export const API_URL = process.env.REACT_APP_URL_BACKEND;

export default function StudentBookLoansTable() {
  
  const [studentId, setStudentId] = useState('');
  const [date , setDate] = useState('2024-05-22');
  const [booksLoanList, setBooksLoanList] = useState([]);
  const [cookies] = useCookies(['jwt']);
  const [studentFound, setStudentFound] = useState(null);
  const [validStudent, setValidStudent] = useState(false);  
  const [validLoan, setValidLoan] = useState(false);
  const [loanId, setLoanId] = useState('');

  useEffect(() => {
    const timeoutId = setTimeout(() => {
      if (studentId) {
        axios
          .get(`${API_URL}/v1/student/${studentId}`, {
            headers: {
              Authorization: cookies.jwt,
            },
          })
          .then((response) => {
            if (response.status === 200) {
              setStudentFound(true);
              setValidStudent(true);
              axios
                .get(`${API_URL}/v1/bookloan/byStudent/${studentId}/${date}`, {
                  headers: {
                    Authorization: cookies.jwt,
                  },
                })
                .then((responseLoanCount) => {
                  if (responseLoanCount.status === 200) {
                    console.log(responseLoanCount.data)
                    setBooksLoanList(responseLoanCount.data);
                  }
                });
            } else {
              setStudentFound(false);
              setValidStudent(false);
            }
          })
          .catch((error) => {
            console.log(cookies.jwt, error)
            setStudentFound(false);
            setValidStudent(false);
            setBooksLoanList([]);
          });
      }
    }, 1000);

    return () => clearTimeout(timeoutId);
  }, [studentId]);

  const handleReturnBook = async (loanId) => {
    setValidLoan(true);
    setLoanId(loanId);
  }

  return (
    <>
      <Card>
        <CardHeader title='Prestamos por Estudiante' titleTypographyProps={{ variant: 'h6' }} />
        <CardContent sx={{ alignItems: 'center', justifyContent: 'center' }}>
          <Grid container spacing={5}>
            <Grid item xs={12}>
              <Grid container spacing={5}>
                <Grid item xs={6}>
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
                <Grid item xs={6}>
                  <TextField
                    fullWidth
                    label='Fecha de Devolución'
                    type="date"
                    required
                    value={date}
                    onChange={(e) => setDate(e.target.value)}
                    InputProps={{
                      startAdornment: (
                        <InputAdornment position='start'>
                          <BadgeOutlined />
                        </InputAdornment>
                      )
                    }}
                  />
                </Grid>
              </Grid>
            </Grid>
            <Grid item xs={12}>
              <TableContainer component={Paper}>
                <Table sx={{ minWidth: 650 }} size="small" aria-label="a dense table">
                  <TableHead>
                    <TableRow>
                      <TableCell>Código</TableCell>
                      <TableCell align="right">Libro</TableCell>
                      <TableCell align="right">Fecha de Prestamo</TableCell>
                      <TableCell align="right">Total Prestamo</TableCell>
                      <TableCell align="right">Total Mora</TableCell>
                      <TableCell align="right">Devolver</TableCell>
                    </TableRow>
                  </TableHead>
                  <TableBody>
                    {booksLoanList.map((bookLoan) => (
                      <TableRow key={bookLoan.id} sx={{ '&:last-child td, &:last-child th': { border: 0 } }}>
                        <TableCell component="th" scope="row">{bookLoan.id}</TableCell>
                        <TableCell align="right">{bookLoan.book}</TableCell>
                        <TableCell align="right">{bookLoan.loanDate}</TableCell>
                        <TableCell align="right">{bookLoan.loanTotal}</TableCell>
                        <TableCell align="right">{bookLoan.delayTotal}</TableCell>
                        <TableCell align="right">
                          <Button variant="contained" color="secondary" onClick={() => handleReturnBook(bookLoan.id)}>
                            Devolver
                          </Button>
                        </TableCell>
                      </TableRow>
                    ))}
                  </TableBody>
                </Table>
              </TableContainer>
            </Grid>
          </Grid>
        </CardContent>
      </Card>
      {validLoan && (
        <Grid container spacing={5} mt={2}>
          <Grid item xs={12}>
            <ReturnBook loanId={loanId} returnDate={date} />
          </Grid>
        </Grid>
      )
      }
    </>
  );
}
