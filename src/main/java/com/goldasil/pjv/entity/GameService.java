package com.goldasil.pjv.entity;

import com.goldasil.pjv.dto.MoveDTO;
import com.goldasil.pjv.enums.Suit;
import com.goldasil.pjv.models.Card;
import com.goldasil.pjv.models.Player;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class for the game entity - mediates handling data from and to database.
 */
@Service
public class GameService {

    private static final Logger logger = LoggerFactory.getLogger(GameService.class);

    @Autowired(required = true)
    private GameRepository gameRepository;

    /**
     * Gathers information, creates a new game entity from the passed data and saves it to the database.
     * @param mainPLayerName
     * @param players
     * @param stock
     * @param waste
     * @param upcard
     * @param lastMoveDTO
     * @param currentPlayerToPlay
     * @param password
     */
    public void save(String mainPLayerName, List<Player> players, List<Card> stock, List<Card> waste, Card upcard, MoveDTO lastMoveDTO, int currentPlayerToPlay, String password) {
        Gson gson = new Gson();
        String playersJson = gson.toJson(players);
        String stockStr = gson.toJson(stock);
        String wasteStr = gson.toJson(waste);
        String upcardStr = gson.toJson(upcard);
        String lastMoveDTOStr = gson.toJson(lastMoveDTO);

        GameEntity entity = new GameEntity(mainPLayerName, playersJson, stockStr, wasteStr, upcardStr, lastMoveDTOStr, currentPlayerToPlay, password);
        logger.info(entity.toString());

        gameRepository.save(entity);
    }

    /**
     * Gets a list of saved games
     * @return list of saved games
     */
    public List<GameEntity> getSavedGames() {

        List<GameEntity> savedGames = (List<GameEntity>) gameRepository.findAll();

        return savedGames;
    }


}
