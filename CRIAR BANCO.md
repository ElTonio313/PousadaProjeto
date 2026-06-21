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

CREATE TABLE reservas (
  codigo int(11) NOT NULL,
  cliente_codigo int(11) NOT NULL,
  quarto_codigo int(11) NOT NULL,
  data_checkin date NOT NULL,
  data_checkout date NOT NULL,
  quantidade_hospedes int(11) NOT NULL,
  status varchar(20) NOT NULL DEFAULT 'ATIVA'
)

CREATE TABLE hospedagens (
  codigo int(11) NOT NULL,
  reserva_codigo int(11) NOT NULL,
  data_hora_checkin datetime NOT NULL,
  status varchar(20) NOT NULL DEFAULT 'EM_ANDAMENTO'
) 
