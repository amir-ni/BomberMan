<p align="center" style="
    font-size: xx-large;
    font-weight: bold;
    padding-top: 2rem;
">BomberMan</p>

###### This repository is my advanced programming course project that I had to create a game similar to BomberMan. 

## About original game

Bomberman is a strategic, maze-based video game franchise originally developed by Hudson Soft and currently owned by Konami. The first game in the series was released in 1983. Today, Bomberman has featured in over 70 different games on numerous platforms (including all Nintendo platforms).

## How to run the game

First, the one who is specified to be server should set database configurations at **Config/ServerConfig.java**.
After that, he should run **ServerRunner.java** and specify a listening port for the game server.

Then players start **ClientRunner.java** and choose _New Game_, enter their names, server's IP and listening port.

One of the players should choose _Create game_ and others choose _Connect to game_ and choose created game from the list.


## Custom features

We had to develop the game with many custom features that I listed some of them below:

- Singleplayer mode
- Multiplayer mode
- 12 power-up types
- 4 type enemies
- Add new enemies types in running game using compiled class files ( JAVA reflection)
- In game chat
- Modifiable panels and object sizes
- Save and load using database

## Required libraries

- Org.Json
- mysql-connector-java

## License

This game is open-source software licensed under the [MIT license](https://opensource.org/licenses/MIT).