const router = require('express').Router();
const { respose, request } = require('express');


const search_engine = require('../core/search_engine');

const s_engine = new search_engine();

router.get('/' , function(req , res,next){
    const {word} = req.query;
    console.log(word);
    s_engine.autoComplete(word).then( function (result) {
        if (result) {
           console.log(result);
           res.render('home',{
               auto_comp : result
           });
        }
        else
        {
            res.render('home',{
                auto_comp : ""
            });
        }
     });
    return next();
});

module.exports = router;