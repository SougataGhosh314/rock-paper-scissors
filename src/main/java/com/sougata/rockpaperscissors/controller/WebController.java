package com.sougata.rockpaperscissors.controller;

import com.sougata.rockpaperscissors.Service.GameService;
import com.sougata.rockpaperscissors.dto.ResponseObject;
import com.sougata.rockpaperscissors.dto.StatusObject;
import com.sougata.rockpaperscissors.model.GameStat;
import com.sougata.rockpaperscissors.model.Move;
import com.sougata.rockpaperscissors.model.Status;
import com.sougata.rockpaperscissors.repository.GameStatRepository;
import org.apache.commons.lang3.EnumUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class WebController {
    private final GameService gameService;
    private final GameStatRepository gameStatRepository;

    public WebController(GameService gameService, GameStatRepository gameStatRepository) {
        this.gameService = gameService;
        this.gameStatRepository = gameStatRepository;
    }

    @GetMapping(path = "/test")
    public String test() {
        return "<h1>Rock Paper Scissors</h1>";
    }

    @GetMapping(path = "/start")
    public ResponseEntity<?> begin() {
        StatusObject statusObject = gameService.startGame();
        if (statusObject == null)
            return new ResponseEntity<>("Error occurred.", HttpStatus.INTERNAL_SERVER_ERROR);
        else
            return new ResponseEntity<>(statusObject, HttpStatus.OK);
    }

    @GetMapping(path = "/v1/{token}/{move}")
    public ResponseEntity<?> begin(@PathVariable(required = true, name = "token") String token,
                                   @PathVariable(required = true, name = "move") String move) {

        boolean badRequest = false;
        Move clientMove = gameService.mapStringToEnum(move);
        if (!EnumUtils.isValidEnum(Move.class, clientMove.name())) {
            badRequest = true;
        }
        Optional<GameStat> gameStat = gameStatRepository.findById(token);
        if (!gameStat.isPresent()) {
            badRequest = true;
        }
        if (badRequest) {
            return new ResponseEntity<>("Invalid request parameters!", HttpStatus.BAD_REQUEST);
        }
        if (gameStat.get().isGameOver()) {
            Status verdict = null;
            if (gameStat.get().getServerScore() >= 3) {
                verdict = Status.GAME_ENDED_AND_SERVER_HAS_WON;
            } else {
                verdict = Status.GAME_ENDED_AND_CLIENT_HAS_WON;
            }
            return new ResponseEntity<>(
                    StatusObject.builder()
                    .token(token)
                    .status(verdict)
                    .clientScore(gameStat.get().getClientScore())
                    .serverScore(gameStat.get().getServerScore())
                    .build(),
                    HttpStatus.OK);
        } else {
            int serverScore = gameStat.get().getServerScore();
            gameStat.get().setServerScore(serverScore + 1);
            if (serverScore+1 >= 3) {
                gameStat.get().setGameOver(true);
            }
            try {
                gameStatRepository.save(gameStat.get());
            } catch (Exception e) {
                return new ResponseEntity<>("Some error occured!", HttpStatus.INTERNAL_SERVER_ERROR);
            }
            return new ResponseEntity<>(
                    ResponseObject.builder()
                            .move(gameService.answerMove(clientMove))
                            .clientScore(gameStat.get().getClientScore())
                            .serverScore(serverScore+1)
                            .build(),
                    HttpStatus.OK
            );
        }
    }
}
