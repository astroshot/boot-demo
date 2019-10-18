package com.boot.ut.web.controller;

import com.boot.ut.AbstractControllerTestCase;
import org.junit.Test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class HelloControllerTest extends AbstractControllerTestCase {

    @Test
    public void checkHealth() throws Exception {
        mockMvc.perform(
                get("/check-health")
        ).andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("ok"));
    }
}
