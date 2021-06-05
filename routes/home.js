const router = require('express').Router();
const { respose, request } = require('express');


const search_engine = require('../core/search_engine');

const s_engine = new search_engine();

router.get('/', function (request, Respose) {
   console.log("i am here");
   Respose.render('home');
});

router.post('/',  async (request, Respose) =>{
   console.log("in post");

   console.log(request.body.searchbar);
   var text = request.body.searchbar;
   s_engine.Search(text ).then( function (result) {
      if (result) {
         console.log(result);

         Respose.render('results',{
            results : result 
         });
         // sending data to the front
      }
   })

   s_engine.insertWord(text);
  
 
});


module.exports = router;