package hudson.model.listeners;

import java.io.IOException;
import java.io.StringWriter;

import hudson.Extension;
import hudson.model.Result;
import hudson.model.Run;
import hudson.model.TaskListener;
import ru.telegram.bot.TelegramApi;

/**
 * RunListener implementation which allow to send notifications
 * via Telegram about building process
 * @param <R>
 */
@Extension
public class TelegramRunListener<R extends Run> extends RunListener<R> {
    private TelegramApi tapi;
    
    /**
     * Default constructor for testing purposes only!!!
     */
    public TelegramRunListener() {
        this("309671090", "AAF2bRdghkIE2qTgOaYon2FTQcHlAuwjRJ8",
                "-173759723");
    }

    
    public TelegramRunListener(String tgm_bot_id, 
            String tgm_bot_token, String tgm_group_id) {
        super();
        
        tapi = new TelegramApi(tgm_bot_id,
                tgm_bot_token, tgm_group_id);
        
        System.out.println("INNO > TRL.construct()");
    }
    
    
    @Override
    public void onStarted(R r, TaskListener listener) {
        String message = "Build " +
                r.getFullDisplayName() + " has started";

        try {
            tapi.sendMessage(message);
            listener.getLogger().println(
                    "Telegram notification should have been sent");
            
        } catch (Exception e) {
            listener.getLogger().println(
                    "Error. Telegram notification wasn't sent");
            e.printStackTrace();
        }
    }
    
    @Override
    public void onCompleted(R r, TaskListener listener) {
        StringBuilder mesBuilder = new StringBuilder();
        mesBuilder.append("Build ");
        mesBuilder.append(r.getFullDisplayName());
        mesBuilder.append(" has finished for ");
        mesBuilder.append(r.getDurationString().replaceAll("секунд", "s"));
        mesBuilder.append(". Finished: ");
        mesBuilder.append(r.getResult());
        
        String log = null;
        if (!r.getResult().equals(Result.SUCCESS)) {
            try {
                StringWriter wr = new StringWriter();
                r.getLogText().writeLogTo(0, wr);
                log = wr.toString();
                
            } catch (IOException e) {
                e.printStackTrace();
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
            listener.getLogger().println(
                    "Error. Telegram notification wasn't sent");
            e.printStackTrace();
        }
    }
}
