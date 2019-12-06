CREATE DATABASE trello;
USE trello;

CREATE TABLE usuario(
	apelido VARCHAR(30) NOT NULL PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    senha VARCHAR(255) NOT NULL,
    foto BLOB
);

CREATE TABLE usuario_telefone(
	usuario_apelido VARCHAR(30) NOT NULL,
    telefone VARCHAR(20) NOT NULL,
    FOREIGN KEY (usuario_apelido) REFERENCES usuario(apelido)
);

CREATE TABLE mensagem(
	id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
	remetente VARCHAR(30) NOT NULL,
    destinatario VARCHAR(30) NOT NULL,
    texto VARCHAR(255) NOT NULL,
    estado INT NOT NULL,
    timestamp_envio TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    timestamp_visualizacao TIMESTAMP,
    FOREIGN KEY (remetente) REFERENCES usuario(apelido),
    FOREIGN KEY (destinatario) REFERENCES usuario(apelido)
);

CREATE TABLE projeto(
	id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    nome VARCHAR(255) NOT NULL,
    proprietario VARCHAR(30) NOT NULL,
    FOREIGN KEY (proprietario) REFERENCES usuario(apelido)
);

CREATE TABLE projeto_usuario(
	projeto_id INT NOT NULL,
	usuario_apelido VARCHAR(30) NOT NULL,
    FOREIGN KEY (projeto_id) REFERENCES projeto(id),
    FOREIGN KEY (usuario_apelido) REFERENCES usuario(apelido)
);

CREATE TABLE tarefa(
    id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    titulo VARCHAR(255) NOT NULL,
    descricao VARCHAR(255),
    prioridade INT NOT NULL,
    estado INT NOT NULL,
	ordem INT NOT NULL,
    projeto_id INT NOT NULL,
    proprietario VARCHAR(30) NOT NULL,
    FOREIGN KEY (projeto_id) REFERENCES projeto(id),
    FOREIGN KEY (proprietario) REFERENCES usuario(apelido)
);

