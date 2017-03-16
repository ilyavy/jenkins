package hudson.model.listeners;

import hudson.model.Result;
import hudson.model.Run;
import hudson.model.TaskListener;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import ru.telegram.bot.TelegramApi;

import java.io.PrintStream;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class TelegramRunListenerTest {
    @Mock
    private TelegramApi telegramApi;

    @Mock
    private TaskListener taskListener;

    @Mock
    private PrintStream printStream;

    @Mock
    private Run r;

    @InjectMocks
    private TelegramRunListener<Run> telegramRunListener = new TelegramRunListener<>();

    private final String displayName = "Test build";
    private final String durationStr = "30 sec";

    @Before
    public void setUp() throws Exception {
        when(taskListener.getLogger()).thenReturn(printStream);
        when(r.getFullDisplayName()).thenReturn(displayName);
        when(r.getDurationString()).thenReturn(durationStr);
        when(r.getResult()).thenReturn(Result.SUCCESS);
    }

    @Test
    public void onStartedSuccessTest() throws Exception {
        String message = "Build " + r.getFullDisplayName() + " has started";
        String notificationLog = "Telegram notification should have been sent";

        telegramRunListener.onStarted(r, taskListener);

        verify(telegramApi, times(1)).sendMessage(message);
        verify(printStream, times(1)).println(notificationLog);
    }

    @Test
    public void onCompletedSuccessTest1() throws Exception {
        String message = "Build " + r.getFullDisplayName() +
                " has finished for " + r.getDurationString() + ". Finished: " + r.getResult();
        String notificationLog = "Telegram notification should have been sent";

        telegramRunListener.onCompleted(r, taskListener);

        verify(telegramApi, times(1)).sendMessage(message);
        verify(printStream, times(1)).println(notificationLog);
    }

    @Test
    public void onStartedFailTest() throws Exception {
        doThrow(mock(RuntimeException.class))
                .when(telegramApi).sendMessage(any(String.class));

        String message = "Build " + r.getFullDisplayName() + " has started";
        String notificationLog = "Error. Telegram notification wasn't sent";

        telegramRunListener.onStarted(r, taskListener);

        verify(telegramApi, times(1)).sendMessage(message);
        verify(printStream, times(1)).println(notificationLog);
    }

    @Test
    public void onCompletedFailTest() throws Exception {
        doThrow(mock(RuntimeException.class))
                .when(telegramApi).sendMessage(any(String.class));

        String message = "Build " + r.getFullDisplayName() +
                " has finished for " + r.getDurationString() + ". Finished: " + r.getResult();
        String notificationLog = "Error. Telegram notification wasn't sent";

        telegramRunListener.onCompleted(r, taskListener);

        verify(telegramApi, times(1)).sendMessage(message);
        verify(printStream, times(1)).println(notificationLog);
    }
}
