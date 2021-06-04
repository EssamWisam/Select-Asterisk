const router = require('express').Router();
const {respose, request}= require('express');
const path = require('path');
//const Serach_engine = require('../core/search_engine');

router.get('/',function(request,Respose){
   console.log("i am here");
   Respose.sendFile(path.join(__dirname ,'../views', 'home.html'));
 
});

router.post('/', (request,Respose)=>{
 
});

module.exports =router ;