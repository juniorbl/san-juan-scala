# San Juan Scala

[![Build Status](https://travis-ci.org/juniorbl/san-juan-scala.svg?branch=master)](https://travis-ci.org/juniorbl/san-juan-scala)

Implementation of the board game San Juan by Andreas Seyfarth using Scala, Play and Akka. This project is for study purposes of functional and reactive programming.

## Getting Started

### Prerequisites

* jdk 8
* sbt 1.0.4
* MongoDB 3.6

### Setup

Setup the database in MongoDB, run `bin/mongo` and:

1. create the database: `use sanJuanScala`
2. create the collection: `db.createCollection("sjsCollection", { size: 10000000, max: 15000000 })`
3. create the user and role: `db.createUser( {user: "sjsUser", pwd: "somepwd", roles: [ {role: "readWrite", db: "sanJuanScala"} ]} )`

### Running the application

Using a terminal, go to the project's root folder:

1. run `sbt run`
2. go to http://localhost:9000/

## Running the tests

Using a terminal, go to the project's root folder and run `sbt test`

## Built With

* [Scala 2.12.3](https://www.scala-lang.org/) - programming language
* [Play Framework 2.6](https://www.playframework.com/) - web framework
* [Akka 2.5.7](https://akka.io/) - concurrent toolkit
* [Bootstrap v4.0.0-beta.3](https://getbootstrap.com/) - toolkit for developing with HTML, CSS, and JS
* [MongoDB 3.6](https://www.mongodb.com/) - document-oriented NoSQL database

## Authors

* **Carlos Luz** -  [GitHub](https://github.com/juniorbl)

## References

* Functional Programming in Scala by Paul Chiusano and Runar Bjarnason
* Reactive Web Applications by Manuel Bernhardt
