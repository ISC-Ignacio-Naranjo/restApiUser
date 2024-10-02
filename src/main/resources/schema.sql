     DROP TABLE IF EXISTS phone;
     DROP TABLE IF EXISTS users;
         create table users (
                is_active boolean not null,
                created timestamp(6),
                last_login timestamp(6),
                modified timestamp(6),
				id VARCHAR(36) not null,
                email varchar(255),
                name varchar(255),
                password varchar(255),
                token varchar(255),
                primary key (id)
            );
             create table phone (
                    id VARCHAR(36) not null,
                    user_id VARCHAR(36),
                    citycode varchar(255),
                    countrycode varchar(255),
                    number varchar(255),
                    primary key (id)
                );
            alter table phone
               add constraint FKik7a2etdorybvoolvchfcvgkx
               foreign key (user_id)
               references users;

