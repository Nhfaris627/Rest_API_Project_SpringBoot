package com.devtiro.database.controllers;


import com.devtiro.database.TestDataUtil;
import com.devtiro.database.domain.entities.AuthorEntity;
import com.devtiro.database.services.AuthorService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.assertj.MockMvcTester;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import javax.print.attribute.standard.Media;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public class AuthorControllerIntegrationTests {

        private AuthorService authorService;

        private MockMvc mockMvc;

        private ObjectMapper objectMapper;

        @Autowired
        public AuthorControllerIntegrationTests(MockMvc mockMvc, AuthorService authorService) {
            this.mockMvc = mockMvc;
            this.authorService = authorService;
            this.objectMapper = new ObjectMapper();
        }

        @Test
        public void testThatCreateAuthorSucessfullyReturnsHttp201Created() throws Exception {
            AuthorEntity testAuthorB = TestDataUtil.createTestAuthorB();
            testAuthorB.setId(null);
            String authorJson = objectMapper.writeValueAsString(testAuthorB);

            mockMvc.perform(
                    MockMvcRequestBuilders.post("/authors")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(authorJson)
            ).andExpect(
                    MockMvcResultMatchers.status().isCreated()
            );
        }


    @Test
    public void testThatCreateAuthorSucessfullyReturnsSavedAuthor() throws Exception {
        AuthorEntity testAuthorB = TestDataUtil.createTestAuthorB();
        testAuthorB.setId(null);
        String authorJson = objectMapper.writeValueAsString(testAuthorB);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/authors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(authorJson)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.id").isNumber()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.name").value("Thomas Cronin")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.age").value("44")
        );
    }

    @Test
    public void testThatListAuthorsReturnHttpStatus200() throws Exception {
            mockMvc.perform(
                MockMvcRequestBuilders.get("/authors")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testThatListAuthorsReturnsListOfAuthors() throws Exception {
        AuthorEntity testAuthorEntityA = TestDataUtil.createTestAuthorEntityA();
        authorService.createAuthor(testAuthorEntityA);
        mockMvc.perform(
                MockMvcRequestBuilders.get("/authors")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].id").isNumber()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].name").value("Abigail Rose")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].age").value("80")
        );
    }
}
