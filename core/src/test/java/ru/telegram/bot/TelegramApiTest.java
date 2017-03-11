package ru.telegram.bot;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.*;

public class TelegramApiTest {

    private final String botId = "botId";
    private final String token = "token";
    private final String groupId = "groupId";
    private final String message = "test message";

    private TelegramApi telegramApi;

    @Before
    public void setUp() throws Exception {
        telegramApi = new TelegramApi(botId, token, groupId);

    }

    @Test
    public void getBotIdTest() throws Exception {
        assertEquals(botId, telegramApi.getBotId());
    }

    @Test
    public void getTokenTest() throws Exception {
        assertEquals(token, telegramApi.getToken());
    }

    @Test
    public void getGroupIdTest() throws Exception {
        assertEquals(groupId, telegramApi.getGroupId());
    }

    @Test
    @Ignore
    public void sendMessageTest() throws Exception {
        //TODO refactor TelegramApi to be able to mock HttpsURLConnection
    }

}