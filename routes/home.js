const router = require('express').Router();
const { respose, request } = require('express');


const search_engine = require('../core/search_engine');

const s_engine = new search_engine();

router.get('/', function (request, Respose) {

   Respose.render('home');


});

router.post('/', async (request, Respose) => {

   var text = request.body.searchbar;
   s_engine.Search(text).then(function (result) {
      if (result) {

        
        
         for (let i of result) {
             if(i.Content.length>600)
           {
           var cont = i.Content.toLowerCase();

            i.Content = i.Content.substr(cont.search(text.toLowerCase()) - 1,600);
            console.log(i.Content);
         }
         }
          final_result = JSON.stringify(result);
      }
     

      s_engine.insertWord(text).catch(
      error =>{
         console.log(error);
     });
      Respose.redirect('results');
   })



});


module.exports = router;