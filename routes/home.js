const router = require('express').Router();
const {respose, request}= require('express');

const Serach_engine = require('../core/search_engine');

router.get('/',(request,Respose)=>{
   // Respose.render('home');
   
});

router.post('/', (request,Respose)=>{
 
});

module.exports =router ;