package hudson.model.listeners;

import hudson.console.AnnotatedLargeText;
import hudson.model.Result;
import hudson.model.Run;
import hudson.model.TaskListener;
import jenkins.model.Jenkins;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import ru.telegram.bot.TelegramApi;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.logging.Level;
import java.util.logging.Logger;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;


@RunWith(MockitoJUnitRunner.class)
public class TelegramRunListenerTest {
    @Mock
    private TelegramApi telegramApi;

    @Mock
    private TaskListener taskListener;

    @Mock
    private Logger logger;

    @Mock
    private Run r;
    
    @Mock
    private Jenkins j;

    @InjectMocks
    private TelegramRunListener<Run> telegramRunListener = new TelegramRunListener<>();

    private final String displayName = "Test build";
    private final String durationStr = "30 sec";

    
    @Before
    public void setUp() throws Exception {
        when(r.getFullDisplayName()).thenReturn(displayName);
        when(r.getDurationStringEnglish()).thenReturn(durationStr);
        when(r.getResult()).thenReturn(Result.SUCCESS);
        when(j.isTelegramNotify()).thenReturn(true);
        
        // Mark the field as public so we can toy with it
        Field field = TelegramRunListener.class.getDeclaredField("LOGGER");
        field.setAccessible(true);
        // Get the Modifiers for the Fields
        Field modifiersField = Field.class.getDeclaredField("modifiers");  
        // Allow us to change the modifiers
        modifiersField.setAccessible(true);
         // Remove final modifier from field by blanking out the bit that says "FINAL" in the Modifiers
        modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
        // Set new value
        field.set(null, logger); 
    }

    
    @Test
    public void onStartedSuccessTest() throws Exception {
        String message = "Build " + r.getFullDisplayName() + " has started";
        String notificationLog = "Telegram notification should have been sent";

        telegramRunListener.onStarted(r, taskListener);

        verify(telegramApi, times(1)).sendMessage(message);
        verify(logger, times(1)).log(Level.FINEST, notificationLog);
    }
    

    @Test
    public void onCompletedSuccessTest1() throws Exception {
        String message = "Build " + r.getFullDisplayName() +
                " has finished for " + r.getDurationStringEnglish() + 
                ". Finished: " + r.getResult();
        String notificationLog = "Telegram notification should have been sent";

        telegramRunListener.onCompleted(r, taskListener);

        verify(telegramApi, times(1)).sendMessage(message);
        verify(logger, times(1)).log(Level.FINEST, notificationLog);
    }
    

    @Test
    public void onStartedFailTest() throws Exception {
        RuntimeException re = mock(RuntimeException.class); 
        doThrow(re).when(telegramApi).sendMessage(any(String.class));

        String message = "Build " + r.getFullDisplayName() + " has started";
        String notificationLog = "Error. Telegram notification wasn't sent";

        telegramRunListener.onStarted(r, taskListener);

        verify(telegramApi, times(1)).sendMessage(message);
        verify(logger, times(1)).log(Level.WARNING, notificationLog, re);
    }


    @Test
    public void onCompletedFailTest() throws Exception {
        RuntimeException re = mock(RuntimeException.class); 
        doThrow(re).when(telegramApi).sendMessage(any(String.class));

        String message = "Build " + r.getFullDisplayName() +
                " has finished for " + r.getDurationStringEnglish() + ". Finished: " + r.getResult();
        String notificationLog = "Error. Telegram notification wasn't sent";

        telegramRunListener.onCompleted(r, taskListener);

        verify(telegramApi, times(1)).sendMessage(message);
        verify(logger, times(1)).log(Level.WARNING, notificationLog, re);
    }
}
