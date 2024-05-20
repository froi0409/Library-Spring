import * as React from 'react';
import Table from '@mui/material/Table';
import TableBody from '@mui/material/TableBody';
import TableCell from '@mui/material/TableCell';
import TableContainer from '@mui/material/TableContainer';
import TableHead from '@mui/material/TableHead';
import TableRow from '@mui/material/TableRow';
import Paper from '@mui/material/Paper';
import Button from '@mui/material/Button';

export default function BooksOrderTable({ booksList, handleRemoveBook }) {
  return (
    <TableContainer component={Paper}>
      <Table sx={{ minWidth: 650 }} size="small" aria-label="a dense table">
        <TableHead>
          <TableRow>
            <TableCell>Código</TableCell>
            <TableCell align="right">Título</TableCell>
            <TableCell align="right">Editorial</TableCell>
            <TableCell align="right">Autor</TableCell>
            <TableCell align="right">Acciones</TableCell>
          </TableRow>
        </TableHead>
        <TableBody>
          {booksList.map((book) => (
            <TableRow key={book.code} sx={{ '&:last-child td, &:last-child th': { border: 0 } }}>
              <TableCell component="th" scope="row">{book.code}</TableCell>
              <TableCell align="right">{book.title}</TableCell>
              <TableCell align="right">{book.publisher}</TableCell>
              <TableCell align="right">{book.author}</TableCell>
              <TableCell align="right">
                <Button variant="contained" color="secondary" onClick={() => handleRemoveBook(book.code)}>
                  Eliminar
                </Button>
              </TableCell>
            </TableRow>
          ))}
        </TableBody>
      </Table>
    </TableContainer>
  );
}
