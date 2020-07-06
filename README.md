## Project title
Pharaoh game
Computer version of a Slovak card game played with German playing cards for 2-4 players.

## Characterization
Desktop Network GUI Java application. The game can be played on computers in two modes:
 - Player vs. Computer
 - Multiplayer mode over the network

## Motivation
The application was developed as a school project for the PVJ course at Czech Technical University.

**Functionality Requirements / Criteria of Success:**
1. Player vs. Player game mode
1. Player vs. Computer game mode
1. computer player generates valid moves
1. computer player has some ability of evaluate moves and choose the most advantageous one
1. the application enforces the adherence to the game rules of all players
1. use of threads
1. network game
1. possibility of saving the game
1. possibility of loading a saved game and continuing the game

**Evaluation of Criteria of Success**
1. Player vs. Player game mode can be used to play against 1, 2 or 3 other players
1. Player vs. Computer game mode can be used to play against 1, 2 or 3 other players
1. computer player generates valid random moves
1. computer player always plays the move with the most cards played
1. the application check each of the moves played with move validator, if a move is found to be incorrect, such move is not executed
1. threads are used: a) to separate move processing and displaying the playing board, b) in the Multiplayer mode to listen for moves of other players, send move infomation, (client-server communication)
1. the game can be used in a network multiplayer mode, when moves executed by the players are sent using TCP protocol to the other players
1. game can be saved using H2 database
1. saved games can be displayed in a table, sorted by the data saved

**Used Technologies**
* Spring Data JPA
* H2 lightweight Java Database
  * stores:
    * saved games
* Logback Logger
* JUnit Testing Framework
* MVC Architecture

**Project Structure**

The project consists of 7 packages located in the main com.goldasil.pjv package:
* communication – encompasses all the classes handling network communication and network helper classes
* controllers – as the game is run in multiple modes, there is a controller for each game mode (gameController for the basic vs Computer mode, GameControllerServerMultiplayer, GameControllerClientMultiplayer)
* dto – classes for tranferring data
* entity – database object mapping related classes – the entity class inself, service class and JPA repository class
* enums – holds enumeration classes
* models – model object classes for cards, playes, game
* views – GUI related classes

**MVC Architecture**
The project fulfills the principles of the Model-View-Controller Architecture, that is, model is changed only after the invocation from a controller. View only accesses the fields of model through getter methods to display the changes in the game. All logic is handled by the model.

**Testing**
Junit Testing Framework was used to uncover disfunctionalities in code for generating random moves by the computer (RandomPlayerTest class) and for move validation (MoveStateHandlerTest). Classes can be found in src/test/java.

**Logback Logger**
Logback Logger keeps track of the changes in 2 modes: logging to a file (src/main/logs/app.log) and to a console. XML file in target/classess specifies the root level of logger to debug. The logger was used mostly at debug and info level.

**Database**
The application uses Spring Data JPA, a Spring Data interface for generic CRUD operations on a repository.
The current state of the game can be captured in the GameEntity, GameService class is provided to wrap up the CRUD operations on the object.
As for the repository itself, H2 lightweight Java Database was used to store the state of saved games. The database runs only when the application is running, but persists data in a file in-between runs.

**Spring Boot Framework**
For the sake of working with the Spring Data and Spring H2 database, using beans (Repository bean), Spring Boot Framework was used and Spring Application is run.

**JavaFx**
For the GUI development, JavaFx was used to design all of the scenes. A whole variety of its tools has been used, namely EventHandlers, ChangeListener, various layouts, dialog windows. Several JavaFx elemens were used as parent classes (e.g. ButtonCard) to further extend their functionality.

**Threads and Synchronization**
The application uses ComResource object to synchronize across multitude of threads – main thread, JavaFx thread, and in Multiplayer mode: listener thread and sender thread.

**Inheritance**
Inheritance has been used for controller specification.

**JSON**
In order to store data in database or send data about a move to another player, objects, an even list of objects, these objects are being serialized, and deserialized, using GSON. 

**Javadoc Documentation**
available at goldasil/Javadoc/index.html
