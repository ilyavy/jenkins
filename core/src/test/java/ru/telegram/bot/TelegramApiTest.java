package ru.telegram.bot;

import jenkins.model.Jenkins;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.util.Map;
import java.util.Map.Entry;


@RunWith(MockitoJUnitRunner.class)
public class TelegramApiTest {

    @Mock
    private Jenkins j;
    
    private TestRequestStrategy requestStrategy = 
            new TestRequestStrategy();

    private final String botId = "botId";
    private final String token = "token";
    private final String chatId = "chatId";

    @InjectMocks
    private TelegramApi telegramApi = 
            new TelegramApi(requestStrategy);
    
    @Before
    public void setUp() throws Exception {
        when(j.getTelegramBotID()).thenReturn(botId);
        when(j.getTelegramGroupID()).thenReturn(chatId);
        when(j.getTelegramBotToken()).thenReturn(token);
    }

    @Test
    public void sendMessageTest() throws Exception {
        // Short message test
        String message = "test simple message";
        telegramApi.sendMessage(message);
        assertEquals(requestStrategy.getUrl(),
                "https://api.telegram.org/bot" + botId + ":" + 
                        token + "/sendMessage");
        Map<String, String> params = requestStrategy.getParams();
        assertEquals(2, params.size());
        for (Entry<String, String> entry : params.entrySet()) {
            if ("chat_id".equals(entry.getKey())) {
                assertEquals(chatId, entry.getValue());
            }
            if ("text".equals(entry.getKey())) {
                assertEquals(message, entry.getValue());
            }
        }
        
        // Long message test
        
    }

}