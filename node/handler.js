var express = require('express');
var app = express();
var request = require('request');

app.get('/scrape', function(req, res){
    // The URL we will scrape from - in our example Anchorman 2.

    url = 'https://support.t-mobile.com/api/core/v3/search/contents/?filter=search(' + req.query.query + ')&filter=type(discussion)&fields=question,resolved,resources.html,subject,parentPlace.name,parentPlace.html&count=5';

    // The structure of our request call
    // The first parameter is our URL
    // The callback function takes 3 parameters, an error, response status code and the html

    request(url, function(error, response, html){
        if(!error){
            let body = response.body;

            let n = body.indexOf(";");
            let str = body.substr(n + 1);

            let json = JSON.parse(str);
            let list = json.list;

            var output = [];
            var outputCount = 0;
            for(var i = 0; i < list.length; i++) {
                let questionLink = json.list[i].resources.self.ref;
                let subject = json.list[i].subject;
                let resolved = json.list[i].resolved;

                getQuestion(questionLink, function (questionContent) {
                    if(questionContent.answerUrl != undefined) {
                        getAnswer(questionContent.answerUrl, function (content) {

                            output.push({
                                "subject": subject,
                                "resolved": resolved,
                                "question": questionContent.question,
                                "answer": content
                            });

                            outputCount++;

                            if(outputCount === list.length) {
                                console.log("done");
                                res.send(output);
                            }
                        });
                    } else {
                        outputCount++;
                    }
                });
            }
        }
    })
});

function getQuestion(url, callback) {
    request(url,function (error, response, html) {
        if(!error) {
            let body = response.body;

            let n = body.indexOf(";");
            let str = body.substr(n + 1);

            let json = JSON.parse(str);
            console.log({
                "url" : url,
                "question": json.content,
                "answerUrl": json.answer
            });
            callback({
                "question": json.content,
                "answerUrl": json.answer
            })
        }
    })
}

function getAnswer(url, callback) {
    request(url, function (error, response, html) {
        if(!error) {
            let body = response.body;

            let n = body.indexOf(";");
            let str = body.substr(n + 1);

            let json = JSON.parse(str);

            callback(json.content)
        }
    })
}

app.listen(3000);