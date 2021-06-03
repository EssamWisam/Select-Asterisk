CREATE TABLE Webpages (URL varchar(255) PRIMARY KEY);
CREATE TABLE Words ( WORD varchar(30) PRIMARY KEY);

CREATE TABLE AppearsIn (WORD varchar(30), URL varchar(255),TF int, IDF float, PRIMARY KEY(WORD, URL),
FOREIGN KEY (WORD) REFERENCES Words(WORD)
ON DELETE CASCADE
,
FOREIGN KEY (URL) REFERENCES Webpages(URL)
ON DELETE CASCADE
 );

 CREATE VIEW ForwardIndex as select URL, Word from appearsin order by URL ASC;
 CREATE VIEW InvertedIndex as select Word, Url, TF from appearsin order by word asc;