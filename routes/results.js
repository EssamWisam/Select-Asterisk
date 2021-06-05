const router = require('express').Router();
const { respose, request } = require('express');
const search_engine = require('../core/search_engine');

const s_engine = new search_engine();

router.get('/', (request, Respose) => {
   console.log("i am here");
   if (typeof final_result == 'undefined') {
      Respose.render('results', {
         results: ""
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
         console.log(result);
         Respose.render('results', {
            results: result
         })

      }
   })
   s_engine.insertWord(text);

});

module.exports = router;