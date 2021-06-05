const pool = require('./pool');
var stemmer = require('porter-stemmer').stemmer;
const { request, response } = require('express');

function search_engine() { };

search_engine.prototype = {
    // This function will search data in the database. 
    Search: function (text = null) {
        if (text) {
            var word = stemmer(text);
            console.log(word);
            let sql =`SELECT * FROM appearsin where  WORD  = '${word}' `;
          
            return pool.query(sql).then(function (result) {
               return result;
            }).catch(function (err) {
                throw err;
            });
           
        }

    },
    insertWord: function(text=null)
    {
        if(text)
        {
            var word = stemmer(text);
            let sql = `INSERT INTO userwords (WORD) VALUES ('${word}')`;
            return pool.query(sql).then(function (result) {
               return result;
            }).catch(function (err) {
                throw err;
            });
        }
    },
    autoComplete: function(subword=null)
    {
        if(subword)
        {
        let sql = `SELECT * FROM userwords WHERE WORD LIKE '${subword}%'`;
        return pool.query(sql).then(function (result){
            return result;
        } ).catch(function (err){
            throw err;
        });
    }
    }

}
module.exports = search_engine;

