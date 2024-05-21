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
import CheckIcon from '@mui/icons-material/Check';

import { useCookies } from 'react-cookie';
import { useState, useEffect } from 'react';
import axios from 'axios';
import ReturnBook from './ReturnBook';

export const API_URL = process.env.REACT_APP_URL_BACKEND;

export default function InactiveStudentsTable() {
  
  const [studentId, setStudentId] = useState('');
  const [booksLoanList, setBooksLoanList] = useState([]);
  const [cookies] = useCookies(['jwt']);
  
  useEffect(() => {
    const timeoutId = setTimeout(() => {
      if (studentId) {
        axios
          .get(`${API_URL}/v1/student/allInactive`, {
            headers: {
              Authorization: cookies.jwt,
            },
          })
          .then((response) => {
            
          })
          .catch((error) => {
            
          });
      }
    }, 1000);

    return () => clearTimeout(timeoutId);
  }, [studentId, date]);

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
              <TableContainer component={Paper}>
                <Table sx={{ minWidth: 650 }} size="small" aria-label="a dense table">
                  <TableHead>
                    <TableRow>
                      <TableCell>Carnet</TableCell>
                      <TableCell align="right">Nombre</TableCell>
                      <TableCell align="right">Apellido</TableCell>
                      <TableCell align="right">email</TableCell>
                      <TableCell align="right"></TableCell>
                    </TableRow>
                  </TableHead>
                  <TableBody>
                    {booksLoanList.map((student) => (
                      <TableRow key={student.id} sx={{ '&:last-child td, &:last-child th': { border: 0 } }}>
                        <TableCell component="th" scope="row">{student.id}</TableCell>
                        <TableCell align="right">{student.firstName}</TableCell>
                        <TableCell align="right">{student.lastName}</TableCell>
                        <TableCell align="right">{student.email}</TableCell>
                        <TableCell align="right">
                          <Button variant="contained" color="secondary" onClick={() => handleReturnBook(student.id)}>
                            <CheckIcon />
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
