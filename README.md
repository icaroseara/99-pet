# 99Pet

99Pet is a RESTful API designed to allow pets to use ride-hailing APP.

## Stack
99Pet uses a number of open source projects to work properly:

* [PlayFramework](https://github.com/playframework) - a high velocity web framework for Java and Scala. This project currently uses version 2.6 with the Scala API.
* [PlayReactiveMongo](https://github.com/ReactiveMongo/Play-ReactiveMongo) - a non-blocking [MongoDB](https://www.mongodb.com) driver. It also provides some useful additions for handling JSON.
* [RediScala](https://github.com/etaty/rediscala) - Non-blocking, Reactive [Redis](https://redis.io) driver for Scala.

And of course 99Pet itself is open source with a [public repository](https://github.com/icaroseara/99-pet) on GitHub.

## Prerequisites
- [sbt](https://github.com/sbt/sbt)
- [Docker](https://docs.docker.com/) and [Docker Compose](https://docs.docker.com/compose/)

## Setup
I strongly recommend to use docker to setup MongoDB and Redis.

To install the dependencies open your favorite terminal and run the follow command:
```
docker-compose up -d
```

## Run specs
To run the specs open your favorite terminal and run the follow command:
```
sbt test
```

