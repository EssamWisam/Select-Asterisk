const router = require('express').Router();
const { Console } = require('console');
const { respose, request } = require('express');
const search_engine = require('../core/search_engine');

const s_engine = new search_engine();

router.get('/', (request, Respose) => {
   if (typeof final_result == 'undefined') {
      Respose.render('results', {
         results: "",


      });
   } else {
      Respose.render('results', {
         results: final_result,

      });
   }


});
router.post('/', async (request, Respose) => {

   var text = request.body.searchbar;
   s_engine.Search(text).then(function (result) {
      if (result) {
         for (let i of result) {
           
            var cont = i.Content.toLowerCase();
            i.Content = i.Content.substr(cont.search(text.toLowerCase()) - 1, 600);
            //console.log(i.Content);
         
         }
         final_result = result;
      }
      else{
         console.log("saba7");
      }
      s_engine.insertWord(text).catch(
      error =>{
         console.log("7amada");
     });
      Respose.render('results', {
         results: JSON.stringify(result)
      })
   })
    
      

});

module.exports = router;