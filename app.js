
// frame work
const express = require('express');

//mange path
const path = require('path');

//engine
const ejs = require('ejs');

//to convert response into json format and give us access the form data
const bodyParser = require('body-parser');

// use the framework
const app = express();

// include routes
const homeRouter = require('./routes/home');
const result = require('./routes/results');

//local host
const port = process.env.PORT || 3000;


//server static files 
app.use(express.static(path.join(__dirname, 'views')));
app.set('views', path.join(__dirname, 'views'));

//virtual engine that allow us to write code in html files(views) like for loops
app.set("view engine", 'ejs');

//to convert response into json format
app.use(bodyParser.json());

//to able neasting object in json format this required explicitly we should write this if we need to use body-parser
app.use(bodyParser.urlencoded({ extended: true }));

//Routers of the pages 
app.use('/home', homeRouter);
app.use('/results', result);



//server listen
app.listen(port, (error) => {
    if (error)
        return console.log(error);

    console.log(`listen at port ${port}`);

})