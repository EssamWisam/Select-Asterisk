const router = require('express').Router();
const { respose, request } = require('express');
const search_engine = require('../core/search_engine');

const s_engine = new search_engine();

router.get('/', (request, Respose) => {
   console.log("i am here");
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
   console.log("in post results");

   console.log(request.body.searchbar);
   var text = request.body.searchbar;
   s_engine.Search(text).then(function (result) {
      if (result) {
         
         for (let i of result) {
            var title =i.Content.match(/<title[^>]*>([^<]+)<\/title>/)[1];
            i.Content =i.Content.replace(/(<([^>]+)>)/gi, "");
            var cont = i.Content.toLowerCase();
            i.Content = i.Content.substr(cont.search(text.toLowerCase())-1 , 700);
            i.WORD = title;
         }
         final_result = result;    
      }
      Respose.render('results', {
         results: result
      })
   })

   
});

module.exports = router;