# Software engineering final project A.A 2020-2021

<img src="https://craniointernational.com/2021/wp-content/uploads/2021/05/Masters-of-Renaissance_box3D.png" width=250px height=250px align="right" />

**Professor**: Gianpaolo Cugola

**Group:** GC17

**Students**:
- Kunal Sahni (immatriculation number: 932018)
- Alessandro Ponti (immatriculation number: 910336)
- Vittorio Rossi (immatriculation number: 911631)

## Implemented features

| Feature | Implemented |
| ------- | ----------- |
| All basic rules ("Regole complete") | :heavy_check_mark: |
| CLI | :heavy_check_mark: |
| GUI | :heavy_check_mark: |
| Socket | :heavy_check_mark: |
| Advanced functionality 1 (FA 1) | :heavy_check_mark: Multiple matches ("Partite multiple") |
| Advanced functionality 2 (FA 2) | :heavy_check_mark: Persistence ("Persistenza") |
| Advanced functionality 3 (FA 3) | :heavy_check_mark: resilience to disconnections ("Resilienza alle disconnessioni") |

## Project specification
The project consists of a Java version of the board game *Masters of Renaissance*, made by Cranio Creations.

You can find the full game [here](https://craniointernational.com/products/masters-of-renaissance/).

The final version includes:
* source code of implementations
* source code of unit tests
* initial UML diagram
* auto-generated final UML diagram
* working game implementation compliant to project requirements

## Testing

Extensive tesing was performed on each part of the project, specifically with the help of unit testing for server components (Model, Controller) and with manual testing on all parts of the project.
Unit tests cover 100% of classes contained in controller and model, with a 92% line coverage. 

Other unit tests were written for user actions (it.polimi.ingsw.network.clienttoserver.action), which are a key part of our controller, these tests have a class coverage of 100% and a line coverage of 92%.

Testing on client side was done through quality assurance since with unit testing we would've had to mock UI-specific functions.

## Compile

To run the tests and compile the software:

1. Install [Java SE 15](https://docs.oracle.com/en/java/javase/15/)
2. Install [Maven](https://maven.apache.org/install.html)
3. Clone this repo
4. In the cloned repo folder, run:
```bash
mvn package
```
5. The compiled artifact ('MastersOfRenaissance.jar') will be inside the `target` folder.

## Quick start guide

The following command is meant to be run inside the [`deliverables/final/jar`](./deliverables/final/jar) folder.

### Windows/Linux

1. In a terminal window, run:
```bash
java -jar MastersOfRenaissance.jar
```
2. Select the option based on output (0 for server, 1 for CLI, 2 for GUI)

Warning: keep in mind that CLI was implemented to function on Linux, in order to run it on Windows you should use WSL (https://github.com/ingconti/W10JavaCLI)

![Masters of Renaissance CLI running in WSL](.github/assets/cli_personal.png "Masters of Renaissance CLI running in WSL")
![Masters of Renaissance GUI running in Windows](.github/assets/gui_personal.png "Masters of Renaissance GUI running in Windows")


## Development

The software has been written using [Java SE 15](https://docs.oracle.com/en/java/javase/15/).

The IDE used for the development is [IntelliJ Idea](https://www.jetbrains.com/idea/) 2021.1.
