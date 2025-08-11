-- V1__create-initial-tables.sql

-- Creación de la tabla de usuarios
CREATE TABLE usuarios (
    id BIGSERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    contraseña VARCHAR(300) NOT NULL,
    perfil VARCHAR(100) NOT NULL,
    activo BOOLEAN NOT NULL
);

-- Creación de la tabla de tópicos
CREATE TABLE topicos (
    id BIGSERIAL PRIMARY KEY,
    titulo VARCHAR(100) NOT NULL UNIQUE,
    mensaje TEXT NOT NULL,
    fecha_creacion TIMESTAMP NOT NULL,
    status VARCHAR(100) NOT NULL,
    autor_id BIGINT NOT NULL REFERENCES usuarios(id),
    curso VARCHAR(100) NOT NULL,
    categoria VARCHAR(100) NOT NULL
);