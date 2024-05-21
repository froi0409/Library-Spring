# Usa la imagen oficial de PostgreSQL
FROM postgres:16.1

# Establece las variables de entorno necesarias para PostgreSQL
ENV POSTGRES_DB=postgres
ENV POSTGRES_USER=postgres
ENV POSTGRES_PASSWORD=postgres

# Copia el script SQL en el contenedor
COPY script.sql /docker-entrypoint-initdb.d/

# El contenedor de PostgreSQL ejecutará automáticamente los scripts en el directorio /docker-entrypoint-initdb.d/

# Exponer el puerto 5432
EXPOSE 5432