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
  
  const [cookies] = useCookies(['jwt']);
  const [inactiveUsersList, setInactiveUsersList] = useState([]);

  const handleEnableStudent = async (studentId) => {
    try {
      const formData = {  
        studentId: studentId,
        enableDate: '2024-05-22'
      }
      const response = await axios.post(`${API_URL}/v1/student/enableStudent`, formData, {
        headers: {
          Authorization: cookies.jwt
        }
      });

      if (response.status === 200) {
        console.log('se actualizó con éxito')
        
      }
    } catch (error) {
      console.error(error)
    }
  }

  useEffect(() => {
    const fetchData = async () => {
      try {
        const response = await axios.get(`${API_URL}/v1/student/allInactive`, {
          headers: {
            Authorization: cookies.jwt
          }
        });
        if (response.status === 200) {
          setInactiveUsersList(response.data);
        }
      } catch (error) {
        console.error(error);
      }
    }
    fetchData();
  }, []);

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
                    {inactiveUsersList.map((student) => (
                      <TableRow key={student.id} sx={{ '&:last-child td, &:last-child th': { border: 0 } }}>
                        <TableCell component="th" scope="row">{student.id}</TableCell>
                        <TableCell align="right">{student.firstName}</TableCell>
                        <TableCell align="right">{student.lastName}</TableCell>
                        <TableCell align="right">{student.email}</TableCell>
                        <TableCell align="right">
                          <Button type="submit" variant="contained" color="secondary" onClick={() => handleEnableStudent(student.id)}>
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
      
    </>
  );
}
