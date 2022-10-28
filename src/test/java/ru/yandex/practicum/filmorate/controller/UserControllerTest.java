package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.AfterEach;
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
//тестирование с помощью MockMvc
//В вебинаре говорись что можно делать и одним тестом, т.к. данные не очищаются
//или я просто не знаю как сделать еще пробовал через WireMock не разбрался, буду позже пробовать

class UserControllerTest {

    private static final String PATH = "/users";

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

    @Test
    void usersTests() throws Exception {
        //создаем пользователя из json
        mockMvc.perform(MockMvcRequestBuilders.post(PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getFile("src/test/java/resources/request/createUser.json"))
                )
                //проверям что вернулось в ответ
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(
                        getFile("src/test/java/resources/request/createUser.json"))
                );

        //проверяем пользователя
        mockMvc.perform(MockMvcRequestBuilders.get(PATH))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(getFile("src/test/java/resources/response/createUser.json"))
                );

        //обновляем пользователя
        mockMvc.perform(MockMvcRequestBuilders.put(PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getFile("src/test/java/resources/request/updateUser.json"))
                )
                //проверям что вернулось в ответ
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(
                        getFile("src/test/java/resources/request/updateUser.json"))
                );

        //проверям какой пользователь вернулся
        mockMvc.perform(MockMvcRequestBuilders.get(PATH))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(getFile("src/test/java/resources/response/updateUser.json"))
                );

        //создаем пользователя из json
        mockMvc.perform(MockMvcRequestBuilders.post(PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(getFile("src/test/java/resources/request/createUserById.json"))
        );

        //проверяем пользователя
        mockMvc.perform(MockMvcRequestBuilders.get(PATH + "/2"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(getFile(
                        "src/test/java/resources/request/createUserById.json"))
                );

        //удаляем пользотелей
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