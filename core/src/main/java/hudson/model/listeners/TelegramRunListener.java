package hudson.model.listeners;

import hudson.Extension;
import hudson.model.Result;
import hudson.model.Run;
import hudson.model.TaskListener;
import jenkins.model.Jenkins;
import ru.telegram.bot.TelegramApi;

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

    private static final Logger LOGGER = Logger.getLogger(TelegramRunListener.class.getName());

    private TelegramApi tapi;
    private Jenkins j = Jenkins.getInstance();

//    public TelegramRunListener() {
//        this("309671090", "AAF2bRdghkIE2qTgOaYon2FTQcHlAuwjRJ8",
//                "-173759723");
//    }

    /**
     * Default constructor
     */
    public TelegramRunListener() {
        super();

        tapi = new TelegramApi();
        
        LOGGER.info("INNO > TRL.construct()");
    }

    @Override
    public void onStarted(R r, TaskListener listener) {
        if(j.getTelegramNotify()) {
            String message = "Build " +
                    r.getFullDisplayName() + " has started";

            try {
                tapi.sendMessage(message);
                LOGGER.log(Level.FINEST, "Telegram notification should have been sent");
            } catch (Exception e) {
                LOGGER.log(Level.WARNING, "Error. Telegram notification wasn't sent", e);
            }
        }
    }
    
    @Override
    public void onCompleted(R r, TaskListener listener) {
        if(j.getTelegramNotify()) {
            StringBuilder mesBuilder = new StringBuilder();
            mesBuilder.append("Build ");
            mesBuilder.append(r.getFullDisplayName());
            mesBuilder.append(" has finished for ");
            mesBuilder.append(r.getDurationString().replaceAll("секунд", "s")); // issue #1
            mesBuilder.append(". Finished: ");
            mesBuilder.append(r.getResult());

            String log = null;
            if (!r.getResult().equals(Result.SUCCESS)) {
                try {
                    StringWriter wr = new StringWriter();
                    r.getLogText().writeLogTo(0, wr);
                    log = wr.toString();
                } catch (IOException e) {
                    LOGGER.log(Level.WARNING, "Error preparing telegram notification", e);
                }
            }

            String message = mesBuilder.toString();

            try {
                tapi.sendMessage(message);
                listener.getLogger().println(
                        "Telegram notification should have been sent");
                if (log != null) {
                    tapi.sendMessage(log);
                }

            } catch (Exception e) {
                LOGGER.log(Level.WARNING, "Error. Telegram notification wasn't sent", e);
            }
        }
    }
}
