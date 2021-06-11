$/home/csmajs/hshep002/spring2021
$/usr/csshare/pkgs/cs120b-avrtools/createProject.sh
$ avr-gcc -mmcu=atmega1284 -Wall -o build/objects/main.elf source/main.c


initDebugger

//cs166 start
hshep002@bolt.cs.ucr.edu
ssh wch136-24.cs.ucr.edu


$git init
$git remote add origin <URL>
$git pull origin master
//or
git branch --set-upstream-to=origin/master master
git pull


make all
make pytest

//hshep002@bolt.cs.ucr.edu
//ssh wch132-44.cs.ucr.edu for cs166
// CS166 Code
//$pg_ctl status
mkdir /tmp/$LOGNAME
cd /tmp/$LOGNAME
mkdir test
mkdir test/data
mkdir sockets
export PGDATA=/tmp/$LOGNAME/test/data

//Start database server, create database, start interactive environment,
pg_ctl -o "-c unix_socket_directories=/tmp/$LOGNAME/sockets" -D $PGDATA -l /tmp/$LOGNAME/logfile start
createdb -h /tmp/$LOGNAME/sockets $hshep002_db
//something here maybe

source ./startPostgreSQL.sh
source ./createPostgreDB.sh
source ./stopPostegreDB.sh


psql -h localhost -p $PGPORT $USER"_DB"
//lab2
drop table if exists students
create table students (sid numeric (9,0), name text, grade float);
insert into students values (860507041,'John Anderson',3.67);
insert into students values (860309067,'Tom Kamber',3.12);
select sid, name, grade from students where sid = 860507041;
insert into students values (860704039,'George Haggerty',3.67);
select sid, name, grade from students where grade = 3.67;
\q
psql -h localhost -p $PGPORT $USER"_DB" < hshep002_script.sql

pg_ctl -o "-c unix_socket_directories=/tmp/$LOGNAME/sockets" -D /tmp/$LOGNAME/test/data stop

//lab5
cp *.txt /tmp/$USER/myDB/data/
psql -h localhost -p $PGPORT $USER"_DB" < chapter5.sql < queries.sql
$ psql -h localhost -p $PGPORT $USER"_DB" < ./lab5/chapter5.sql < ./lab5/queries.sql

//lab6?--------------------------------------------------66666
//Transfering Files from pc to ssh
//files in This PC/Documents/queries.sql
//in cmd:
        scp lab6.zip hshep002@bolt.cs.ucr.edu:
        ssh hshep002@bolt.cs.ucr.edu
//in ssh:
        scp lab6.zip wch136-24.cs.ucr.edu:/tmp/hshep002
        ssh hshep002@wch136-24.cs.ucr.edu
//in wch ssh

        unzip /tmp/hshep002/lab6.zip

source lab5/start...
source lab5/create...
//then change source sql file
vim lab5/chapter5.sql
        //get pwd 
        //From "suppliers.txt" > "/home/csmajs/hshep002/lab5/suppliers.txt"
source ./lab5/startPostgreSQL.sh
source ./lab5/createPostgreDB.sh
$pg_ctl status
cd lab5
psql -h localhost -p $PGPORT $USER"_DB" < chapter5.sql < queries.sql
source ./lab5/stopPostegreDB.sh

//-----------------cs166 lab7------------------------------------
//cs166 lab7~7~7~7~77~7~7~7~77~7~7~7~77~7~7~7~77~7~7~7~77~7~7~7~7
//Transfering Files from pc to ssh
//files in This PC/Documents/queries.sql
//in cmd:
        scp lab7.zip hshep002@bolt.cs.ucr.edu:
        ssh hshep002@bolt.cs.ucr.edu
//in ssh:
        scp lab7.zip wch136-24.cs.ucr.edu:/tmp/hshep002
        ssh hshep002@wch136-24.cs.ucr.edu
//in wch ssh

        unzip /tmp/hshep002/lab7.zip
        source startPostgreSQL.sh
        source createPostgreDB.sh
        psql -h localhost -p $PGPORT $USER"_DB" < create_tables.sql < queries.sql
        vim measure.sh
        
//------Lab7 table info---------//
drop table if exists part_nyc;
create table part_nyc (part_number integer,
                       supplier integer,
                       color integer,
                       on_hand integer,
                       descr text);
COPY part_nyc
FROM 'part_nyc.dat'
WITH DELIMITER ',';

drop table if exists part_sfo;
create table part_sfo (part_number integer,
                       supplier integer,
                       color integer,
                       on_hand integer,
                       descr text);
COPY part_sfo
FROM 'part_sfo.dat'
WITH DELIMITER ',';

drop table if exists supplier;
create table supplier (supplier_id integer,
                       supplier_name varchar(20));
//----------------------------//

//**//---------------------Lab8 Indexes---------------------//**//
//Transfering Files from pc to ssh
//files in This PC/HarrisShepard?
//in cmd:
        scp lab8.zip hshep002@bolt.cs.ucr.edu:
        ssh hshep002@bolt.cs.ucr.edu
//in ssh:
        scp lab8.zip wch136-24.cs.ucr.edu:/tmp/hshep002
        ssh hshep002@wch136-24.cs.ucr.edu
//in wch ssh

        unzip /tmp/$USER/lab8.zip -d /extra/$USER/
        cd /extra/$USER/lab8
        //change create_tables.sql
        //
        pwd
//in $USER/code/postgresql
        source startPostgreSQL.sh
        source createPostgreDB.sh
//in $USER/code/java
        source ./compile.sh
        source ./run.sh
         vim ./java/src/DBProject2.java

//from code/
        source ./postgresql/startPostgreSQL.sh
        source ./postgresql/createPostgreDB.sh
        psql -h localhost -p $PGPORT $USER"_DB" < create_tables.sql
        psql -h localhost -p $PGPORT $USER"_DB" < create_indexes.sql
        psql -h localhost -p $PGPORT $USER"_DB" < cluster.sql
        vim measure.sh
        chmod +x measure.sh
        source measure.sh

        chmod +x stopPostgreDB.sh
        source stopPostgreDB.sh

//--..----..----..----..----..----..----..----..----..----..----..----..--//
//--..----..----..----..----..----..----..----..----..----..----..----..--//
//**//---------------------Project Phase 3---------------------//**//
//--..----..----..----..----..----..----..----..----..----..----..----..--//
//--..----..----..----..----..----..----..----..----..----..----..----..--//
        scp "cs166 project code.zip" hshep002@bolt.cs.ucr.edu:
        ssh hshep002@bolt.cs.ucr.edu
//in ssh:
        scp "cs166 project code.zip" wch136-24.cs.ucr.edu:/tmp/hshep002
        ssh hshep002@wch136-24.cs.ucr.edu
//in wch ssh

        unzip "/tmp/$USER/cs166 project code.zip" -d /extra/$USER/
        cd /extra/$USER/

      source ./compile.sh
      source ./run.sh
      vim ./sql/queries.sql
      vim ./sql/insertion.sql
      source ./startPostgreSQL.sh
        source ./createPostgreDB.sh
      psql -h localhost -p $PGPORT $USER"_DB" < ./sql/insertion.sql
      psql -h localhost -p $PGPORT $USER"_DB" <./sql/queries.sql

      vim ./src/DBproject.java
      :set paste


//--..----..----..----..----..----..----..----..----..----..----..----..--//
//--..----..----..----.Java---..----..----..----..----..----..--//
//--..----..----..----..----..----..----..----..----..----..----..----..--//
