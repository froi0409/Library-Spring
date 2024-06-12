# Usa una imagen de Node.js como base
FROM node:latest

# Establece el directorio de trabajo en /app
WORKDIR /app

# Copia los archivos de la aplicación
COPY . .

# Instala las dependencias
RUN npm install

# Copia el archivo de variables de entorno al contenedor
COPY .env .env

# Compila la aplicación
RUN npm run build

# Expone el puerto en el que corre la aplicación
EXPOSE 3000

# Comando para correr la aplicación
CMD ["npm", "run", "start"]
