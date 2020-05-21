package com.goldasil.pjv.entity;

import com.goldasil.pjv.models.Player;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GameService {

    private static final Logger logger = LoggerFactory.getLogger(GameService.class);


    @Autowired(required = true)
    private GameRepository gameRepository;

    public void save() {
        GameEntity entity = new GameEntity("Player#1", 2, "cards player: ", "147" );
        gameRepository.save(entity);

        //List<GameEntity> games = (List<GameEntity>) gameRepository.findAll();
        //System.out.println(games.toString());
    }


    public void save(String mainPLayerName, int currentPlayerToPlay, List<Player> players, String password) {
        Gson gson = new Gson();
        String playersJson = gson.toJson(players);

        GameEntity entity = new GameEntity(mainPLayerName, currentPlayerToPlay, playersJson, password);
        logger.info(entity.toString());

        gameRepository.save(entity);
    }


}
