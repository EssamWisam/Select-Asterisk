const router = require('express').Router();
const {respose, request}= require('express');
const path = require('path');
router.get('/',(request,Respose)=>{
   console.log("i am here");
   Respose.sendFile(path.join(__dirname ,'../views', 'results.html'));
});
router.post('/', (request,response)=>{
  
});

module.exports= router ;