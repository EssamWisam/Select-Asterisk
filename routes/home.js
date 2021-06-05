const router = require('express').Router();
const { respose, request } = require('express');
let {PythonShell} = require('python-shell')

const search_engine = require('../core/search_engine');

const s_engine = new search_engine();

router.get('/', function (request, Respose) {
   /*PythonShell.run('./Script.py',  {args:['https://www.bbc.com/sport/football/57351188',"xavi"]}, function (err, results) {
      if (err) throw err;
      console.log(results);
    });*/
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
         final_result = result;
         Respose.redirect('results');
      }
   })

   s_engine.insertWord(text);
  
 
});


module.exports = router;