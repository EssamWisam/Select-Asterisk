# SearchTime
Time to beat Google...

## To run the Crawler
1-You need to import all jar file inside the project ,you will find them insdie 
Code -> Crawler -> jar_file

2-Install MongoDB
```shell
https://www.mongodb.com/try/download/community
```
3-Open Mongo Compass and jump into localhost

4-Open the code inside any editor and run crawler main

5-Wait untill the crawler finishes the 5000 page

## To run the Indexer
1-Create database called indexer on MySQL

2-Open Code->Indexer on DB 

and you will find IndexerDB.sql execute this file in order to create the tables and views

3-Go to code and open file indexer.java 

You will find at the first of the class 
```shell
username=...;
password=...;
```
Set them with your MySQL user and password
4-Run IndexerMain and wait until it finishes



## To open the site
1-First you need to install NodeJS

```shell
Visit https://nodejs.org/en/download/ to download
```

2-Install Express

```shell
npm install express
npm install express --save
```

3-Install MySQL

```shell
npm install mysql
```

3-Install Porter-Stemmer
```shell
npm install porter-stemmer
```
4-Inside Code->Website->Core 
You will find pool.js file open it and edit your MySQL user and password

5-Run the server
Open the terminal inside  Code-> Website  and type
```shell
node app.js
```
6-Open website
```shell
http://localhost:3000/home
```

