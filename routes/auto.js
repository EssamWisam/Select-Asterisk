const router = require('express').Router();
const { respose, request } = require('express');


const search_engine = require('../core/search_engine');

const s_engine = new search_engine();

router.get('/' , function(req , res){
    const {word} = req.query;
    console.log(word);
    if(word !="")
    {
    s_engine.autoComplete(word).then( function (result) {
        if (result) {
           console.log(result);
           res.send(result);
        }
     }).catch(error =>{
         console.error(error);
     });
    }
    
});

module.exports = router;