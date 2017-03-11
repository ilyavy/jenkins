package hudson.model.listeners;

import hudson.Extension;
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
        String message = "Build " + r.getFullDisplayName() + 
                " has finished for " + r.getDurationString();

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
}
