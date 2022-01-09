package com.sougata.rockpaperscissors.Service;

import com.sougata.rockpaperscissors.dto.StatusObject;
import com.sougata.rockpaperscissors.model.GameStat;
import com.sougata.rockpaperscissors.model.Move;
import com.sougata.rockpaperscissors.model.Status;
import com.sougata.rockpaperscissors.repository.GameStatRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class GameService {
    private final GameStatRepository gameStatRepository;

    public GameService(GameStatRepository gameStatRepository) {
        this.gameStatRepository = gameStatRepository;
    }

    public StatusObject startGame() {
        GameStat newEntry = GameStat.builder().clientScore(0).serverScore(0).isGameOver(false).build();

        GameStat savedEntry = null;
        try {
            savedEntry = gameStatRepository.save(newEntry);
        } catch (Exception e) {
            return null;
        }

        return StatusObject.builder()
                .token(savedEntry.getToken())
                .status(Status.READY)
                .clientScore(0)
                .serverScore(0)
                .build();
    }

    public Move answerMove(Move move) {
        Move answer = null;
        switch (move) {
            case ROCK:
                answer = Move.PAPER;
                break;
            case PAPER:
                answer = Move.SCISSORS;
                break;
            case SCISSORS:
                answer = Move.ROCK;
                break;
            default:
        }
        return answer;
    }

    public Move mapStringToEnum(String move) {
        if (move.equalsIgnoreCase(Move.ROCK.name())){
            return Move.ROCK;
        } else if (move.equalsIgnoreCase(Move.PAPER.name())){
            return Move.PAPER;
        } else if (move.equalsIgnoreCase(Move.SCISSORS.name())){
            return Move.SCISSORS;
        } else {
            return null;
        }
    }
}
