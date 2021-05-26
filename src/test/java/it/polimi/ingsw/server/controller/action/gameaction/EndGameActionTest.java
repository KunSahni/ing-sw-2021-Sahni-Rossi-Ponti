//package it.polimi.ingsw.server.controller.action.gameaction;

//import it.polimi.ingsw.server.model.Game;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;

//import java.io.IOException;
//import java.util.List;

//import static org.junit.jupiter.api.Assertions.assertNull;

//public class EndGameActionTest {
//    EndGameAction endGameAction;
//  Game game;
//  String nick1;
//  String nick2;
//  List<String> nicknameList;
//
//  @BeforeEach
//  void init(){
//      nick1 = "qwe";
//      nick2 = "asd";
//      nicknameList = List.of(nick1, nick2);
//      try {
//          game = new Game(1, nicknameList);
//      } catch (IOException e) {
//          e.printStackTrace();
//      }
//      endGameAction = new EndGameAction(game);
//  }
//
//  @Test
//  void executeTest() {
//      assertNull(endGameAction.execute());
//  }
//}
