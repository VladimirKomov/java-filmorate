package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;


import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class FilmControllerTest {

    private static final String PATH = "/films";

    @Autowired
    MockMvc mockMvc;

    @AfterEach
    void deleteAll() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(PATH));
    }

    @Test
    void getEmpty() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(PATH))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json("[]"));
    }

    @Disabled
    @Test
    void filmsTests() throws Exception {
        //создаем из json
        mockMvc.perform(MockMvcRequestBuilders.post(PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getFile("src/test/java/resources/request/createFilm.json"))
                )
                //проверям что вернулось в ответ
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(
                        getFile("src/test/java/resources/request/createFilm.json"))
                );

        //обновляем
        mockMvc.perform(MockMvcRequestBuilders.put(PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getFile("src/test/java/resources/request/updateFilm.json"))
                )
                //проверям что вернулось в ответ
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(
                        getFile("src/test/java/resources/request/updateFilm.json"))
                );
        //получаем по ID
        mockMvc.perform(MockMvcRequestBuilders.get(PATH + "/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(getFile(
                        "src/test/java/resources/request/updateFilm.json"))
                );

        //создаем другой
        mockMvc.perform(MockMvcRequestBuilders.post(PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(getFile("src/test/java/resources/request/createFilmOtherFilm.json"))
        );

        mockMvc.perform(MockMvcRequestBuilders.get(PATH + "/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(getFile(
                        "src/test/java/resources/request/updateFilm.json"))
                );

        //добавляем пользоватея для лайков
        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(getFile("src/test/java/resources/request/createUserForLike.json"))
        );

        //ставим лайк
        mockMvc.perform(MockMvcRequestBuilders.put(PATH + "/2/like/3"))
                .andExpect(MockMvcResultMatchers.status().isOk()
                );

        //получаем фильмы по порулярности (по умоляанию)
        mockMvc.perform(MockMvcRequestBuilders.get(PATH + "/popular"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(getFile(
                        "src/test/java/resources/response/10popularFilms.json"))
                );

        //получаем первый по популярности
        mockMvc.perform(MockMvcRequestBuilders.get(PATH + "/popular?count=1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(getFile(
                        "src/test/java/resources/response/1popularFilm.json"))
                );


        //удаляем
        mockMvc.perform(MockMvcRequestBuilders.delete(PATH))
                .andExpect(MockMvcResultMatchers.status().isOk()
                );

        //проверяем что пусто
        mockMvc.perform(MockMvcRequestBuilders.get(PATH))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json("[]")
                );


    }

    private String getFile(String path) {
        File file = new File(path);
        try {
            return Files.readString(file.toPath(),
                    StandardCharsets.UTF_8);
        } catch (final IOException e) {
            throw new RuntimeException("Error open file", e);
        }
    }


}