const router = require('express').Router();
const { respose, request } = require('express');
const path = require('path');


const search_engine = require('../core/search_engine');

const s_engine = new  search_engine();

router.get('/', function (request, Respose) {
   console.log("i am here");
   Respose.sendFile(path.join(__dirname, '../views', 'home.html'));

});

router.post('/', function (request, Respose) {
   console.log("in post");
   console.log(request.body.searchbar);
   var text = request.body.searchbar;
   s_engine.Search(text).then(function (result) {
      if (result)
         console.log(result);
      else
         console.log("no result");
   });

});

module.exports = router;