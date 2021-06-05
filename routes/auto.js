const router = require('express').Router();
const { respose, request } = require('express');


const search_engine = require('../core/search_engine');

const s_engine = new search_engine();

router.get('/' , function(req , resp){
    const {word} = req.query;
    console.log(word);
});

module.exports = router;