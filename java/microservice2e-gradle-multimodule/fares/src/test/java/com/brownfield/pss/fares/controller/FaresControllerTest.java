package com.brownfield.pss.fares.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class FaresControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void getFare() throws Exception {
        mockMvc.perform(get("/fares/get")
                .param("flightNumber", "BF100")
                .param("flightDate", "22-JAN-18")
        ).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").exists());
    }

}