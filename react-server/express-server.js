var express = require('express');
var graphqlHTTP = require('express-graphql');
var { buildSchema } = require('graphql');
var cors = require('cors');

var app = express();
var todos = [];


var schema = buildSchema(`
  type Query {
    todo (input: String) : String
  }
`);

var root = {
    todo: ({ item }) => {
        todos.push(item);
        console.log('todo received, now todo list :', todos);
        return todos;
    } 
}

app.get('/', function (req, res) {
  res.send('Hello this is GraphQL server!')
});

app.use('/graphql',cors(), graphqlHTTP({
  schema: schema,
  rootValue: root,
  graphiql: true,
}));
app.listen(4000);
console.log('Running a GraphQL API server at localhost:4000/graphql');

