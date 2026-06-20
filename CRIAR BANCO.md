CREATE DATABASE pousada_db;
USE pousada_db;

CREATE TABLE clientes (
    codigo INT AUTO_INCREMENT PRIMARY KEY,
    nome_completo VARCHAR(150) NOT NULL,
    cpf VARCHAR(14) UNIQUE NOT NULL,
    telefone VARCHAR(20) NOT NULL,
    email VARCHAR(100) NOT NULL,
    data_nascimento DATE NOT NULL
);

CREATE TABLE quartos (
    codigo INT AUTO_INCREMENT PRIMARY KEY,
    numero VARCHAR(10) UNIQUE NOT NULL,
    tipo VARCHAR(50) NOT NULL,
    capacidade_maxima INT NOT NULL,
    valor_diaria DECIMAL(10,2) NOT NULL,
    status VARCHAR(50) NOT NULL
);
