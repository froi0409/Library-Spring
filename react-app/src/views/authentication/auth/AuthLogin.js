import React, { useEffect, useState } from 'react';
import {
    Box,
    Typography,
    Button,
    Stack,
} from '@mui/material';
import { Alert } from '@mui/material';


import { Link } from 'react-router-dom';
import axios from 'axios';

import CustomTextField from '../../../components/forms/theme-elements/CustomTextField'; 
import { useCookies } from 'react-cookie';
import { Route } from 'react-router-dom'

import { useNavigate } from 'react-router-dom';

export const API_URL = process.env.REACT_APP_URL_BACKEND;

const AuthLogin = ({ title, subtitle, subtext }) => {
    
    const [submitted, setSubmitted] = useState(false);
    const [responseMessage, setResponseMessage] = useState('');
    const [formData, setFormData] = useState({
        username: '',
        password: '',
    });

    const [ cookies, setCookie ] = useCookies([ 'jwt' ]);
    const navigate = useNavigate();

    const handleChange = (event) => {
        const { name, value } = event.target;
        setFormData({
            ...formData,
            [name]: value,
        });
    };

    useEffect(() => {
      const verifySystemData = async () => {
            try {
                const response = await axios.get(`${API_URL}/v1/datafile/verifySystemData`);
                if (response.data === false) {
                    setSubmitted(true);
                    setResponseMessage('Hacen falta datos en el sistema, contacta a un adminsitrador');
                }
            } catch (error) {
                console.error(error)
                setSubmitted(true);
                setResponseMessage('Hacen falta datos en el sistema, contacta a un adminsitrador');
            }
        
      }
    
      verifySystemData();
    }, []);
    

    const handleSubmit = async (event) => {
        event.preventDefault();
        try {
            const response = await axios.post(`${API_URL}/v1/auth/login`, formData);
            setSubmitted(true);
            
            if (response.status === 200) {
                const { token, role } = response.data;
                setCookie('jwt', `Bearer ${token}`);
                
                if (role === 'LIBRARIAN') {
                    navigate('/all-books');
                } else if (role === 'STUDENT') {
                    navigate('/student/dashboard');
                } else if (role === 'ADMINISTRATOR') {
                    navigate('/admin/upload-datafile')
                }
                
            } else {
                setResponseMessage('Ocurrió un error al iniciar sesión');
            }

        } catch (error) {
            setSubmitted(true);
            setResponseMessage('Usuario o contraseña incorrecta, verifica tus credenciales');
        }
    };

    return (
        <>
            {title ? (
                <Typography fontWeight="700" variant="h2" mb={1}>
                    {title}
                </Typography>
            ) : null}

            {subtext}

            {submitted && (
                <Alert severity="error" variant='outlined' sx={{ mb: 2 }}>
                    {responseMessage}
                </Alert>
            )}

            <Stack component="form" onSubmit={handleSubmit}>
                <Box>
                    <Typography variant="subtitle1"
                        fontWeight={600} component="label" htmlFor='username' mb="5px">Usuario</Typography>
                    <CustomTextField
                        id="username"
                        name="username"
                        variant="outlined"
                        fullWidth
                        value={formData.username}
                        onChange={handleChange}
                    />
                </Box>
                <Box mt="25px">
                    <Typography variant="subtitle1"
                        fontWeight={600} component="label" htmlFor='password' mb="5px" >Contraseña</Typography>
                    <CustomTextField
                        id="password"
                        name="password"
                        type="password"
                        variant="outlined"
                        fullWidth
                        value={formData.password}
                        onChange={handleChange}
                    />
                </Box>
                <Stack justifyContent="space-between" direction="row" alignItems="center" my={2}>
                    <Typography
                        component={Link}
                        to="/"
                        fontWeight="500"
                        sx={{
                            textDecoration: 'none',
                            color: 'primary.main',
                        }}
                    >
                        ¿Olvidaste tu Contraseña?
                    </Typography>
                </Stack>
                <Box>
                    <Button
                        color="primary"
                        variant="contained"
                        size="large"
                        fullWidth
                        type="submit"
                    >
                        Iniciar Sesión
                    </Button>
                </Box>
            </Stack>
            {subtitle}
        </>
    );
};

export default AuthLogin;
