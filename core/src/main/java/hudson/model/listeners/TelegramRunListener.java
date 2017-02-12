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
    
    public TelegramRunListener() {
        super();
        
        tapi = new TelegramApi("309671090",
                "AAF2bRdghkIE2qTgOaYon2FTQcHlAuwjRJ8", "-173759723");
        
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
