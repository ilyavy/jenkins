package ru.telegram.bot;

import jenkins.model.Jenkins;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TelegramApiTest {

    @Mock
    private Jenkins jenkins;

    private final String botId = "botId";
    private final String token = "token";
    private final String groupId = "groupId";
    private final String message = "test message";

    @InjectMocks
    private TelegramApi telegramApi;

    @Before
    public void setUp() throws Exception {
        when(jenkins.getTelegramBotID()).thenReturn(botId);
        when(jenkins.getTelegramGroupID()).thenReturn(groupId);
        when(jenkins.getTelegramBotToken()).thenReturn(token);
    }

    @Test
    @Ignore
    public void sendMessageTest() throws Exception {
        //TODO refactor TelegramApi to be able to mock HttpsURLConnection
    }

}