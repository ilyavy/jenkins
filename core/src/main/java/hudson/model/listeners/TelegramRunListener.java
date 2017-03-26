package hudson.model.listeners;

import hudson.Extension;
import hudson.model.Result;
import hudson.model.Run;
import hudson.model.TaskListener;
import jenkins.model.Jenkins;
import ru.telegram.bot.PostConnectionBuilder;
import ru.telegram.bot.PostRequestStrategy;
import ru.telegram.bot.ConnectionBuilder;
import ru.telegram.bot.TelegramApi;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * RunListener implementation which allow to send notifications
 * via Telegram about building process
 * @param <R>
 */
@Extension
public class TelegramRunListener<R extends Run> extends RunListener<R> {

    private static final Logger LOGGER = 
            Logger.getLogger(TelegramRunListener.class.getName());

    /**
     * The object, through which messages will be sent
     */
    private TelegramApi tapi;
    
    /**
     * The reference to Jenkins' instance
     */
    private Jenkins j = Jenkins.getInstance();

    /**
     * Default constructor
     */
    public TelegramRunListener() {
        super();
        tapi = new TelegramApi(new PostRequestStrategy());
    }

    
    @Override
    public void onStarted(R r, TaskListener listener) {
        if(j.isTelegramNotify()) {
            String message = "Build " +
                    r.getFullDisplayName() + " has started";

            try {
                tapi.sendMessage(message);
                LOGGER.log(Level.FINEST, 
                        "Telegram notification should have been sent");
                
            } catch (Exception e) {
                LOGGER.log(Level.WARNING, 
                        "Error. Telegram notification wasn't sent", e);
            }
        }
    }
    
    
    @Override
    public void onCompleted(R r, TaskListener listener) {
        if(j.isTelegramNotify()) {
            StringBuilder mesBuilder = new StringBuilder();
            mesBuilder.append("Build ");
            mesBuilder.append(r.getFullDisplayName());
            mesBuilder.append(" has finished for ");
            mesBuilder.append(r.getDurationString().replaceAll("секунд", "s")); // issue #1
            mesBuilder.append(". Finished: ");
            mesBuilder.append(r.getResult());

            // In case of build's failure prepare the report to send
            String failureReport = null;
            if (!r.getResult().equals(Result.SUCCESS)) {
                try {
                    StringWriter wr = new StringWriter();
                    r.getLogText().writeLogTo(0, wr);
                    failureReport = wr.toString();
                    
                } catch (IOException e) {
                    LOGGER.log(Level.WARNING, 
                            "Error preparing telegram notification", e);
                }
            }

            String message = mesBuilder.toString();

            try {
                tapi.sendMessage(message);
                LOGGER.log(Level.FINEST,
                        "Telegram notification should have been sent");
                if (failureReport != null) {
                    tapi.sendMessage(failureReport);
                }

            } catch (Exception e) {
                LOGGER.log(Level.WARNING, 
                        "Error. Telegram notification wasn't sent", e);
            }
        }
    }
}
