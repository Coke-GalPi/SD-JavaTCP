/*==============================================================*/
/* DBMS name:      MySQL 5.0                                    */
/* Created on:     24-04-2024 11:26:34                          */
/*==============================================================*/


drop table if exists DOCUMENTOS;

drop table if exists PALABRAS;

/*==============================================================*/
/* Table: DOCUMENTOS                                            */
/*==============================================================*/
create table DOCUMENTOS
(
   ID_DOC               int not null auto_increment,
   NOMBRE               varchar(100),
   TEMA                 varchar(100),
   URL                  varchar(4096),
   primary key (ID_DOC)
);

/*==============================================================*/
/* Table: PALABRAS                                              */
/*==============================================================*/
create table PALABRAS
(
   ID_PALABRA           int not null auto_increment,
   PALABRA              varchar(50),
   SIGNIFICADO          varchar(2048),
   primary key (ID_PALABRA)
);

