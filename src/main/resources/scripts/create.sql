create table keypair
(
    id         bigint identity (1,1),
    clientId   varchar(150) not null,
    privateKey varchar(max) not null,
    publicKey  varchar(max) not null,

    constraint PK_KeyPair primary key (id),
    constraint UK_clientId unique (clientId)
);

create table desafio(
    clientId varchar(150),
    desafio varchar(50),

    constraint PK_Desafio primary key (clientId)
);
