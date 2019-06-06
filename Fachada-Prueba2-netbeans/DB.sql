drop table Movimientos;
drop table Titulares;
drop table Cuentas;
drop table oficinas;
drop table Clientes;


create table  Clientes (
codigo_cliente number(3,0)primary key,
nombre varchar2(60),
apellido varchar2(60),
fecha_nacimiento date,
genero char(10));

create table  oficinas
(codigo_oficina number (3,0)primary key,
nombre varchar2(60));


create table  Cuentas
(Numero_cuenta number(3,0)primary key,
tipo char(10),
codigo_oficina number (3,0),
saldo number(12,2),
valor_apertura number(12,2),
 foreign key(codigo_oficina) references oficinas(codigo_oficina)
);


create table  Titulares(
Codigo_cliente  number(3,0), 
numero_cuenta number(3,0),
foreign key(Codigo_cliente) references clientes(Codigo_cliente),
foreign key(numero_cuenta) references cuentas(Numero_cuenta),
primary key (Codigo_cliente,numero_cuenta)
);

create table  Movimientos
(numero_cuenta  number(3,0), 
Numero number(3,0),
tipo char(10),
valor number(10,2),
foreign key(numero_cuenta) references cuentas(Numero_cuenta),
primary key (Numero,numero_cuenta),
check(tipo in('D','C'))
);

