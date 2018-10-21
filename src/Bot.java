import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class Bot extends TelegramLongPollingBot{
    Long cid; // anyone's contactID
    long fcid = 345028455; // first contactID
    long scid = 214451524; // second contactID
    long user_id;
    ChatCommands cc = new ChatCommands();
    @Override
    public String getBotUsername() {
        return "mybookkeepingbot";
        //возвращаем юзера
    }
    @Override
    public void onUpdateReceived(Update e) {
        Message msg = e.getMessage(); // Это нам понадобится
        String txt = msg.getText();
        cid = msg.getChatId();
        user_id = msg.getChat().getId();
        if (user_id == fcid || user_id == scid) {
            String returnMsg = cc.firstTextReader(txt, user_id);
            if (returnMsg != null)
                sendMsg(returnMsg);
        }
    //    else if (user_id == scid) { }
        else
            sendMsg("Пошел нахуй, пидор");
    }
   /* private String textReader (String txt){
        if (cid==fcid){
           return cc.firstTextReader(txt, user_id);
        }else {
            return "error";
        }
    } */
    @Override
    public String getBotToken() {
        return "697192904:AAFMN1CGUtzpPoXEnhWUmUUu7fmxsm1OOvg";
        //Токен бота
    }
    @SuppressWarnings("deprecation")
    private void sendMsg(String text) {
        SendMessage s = new SendMessage();
    //    s.setChatId(cid); // id получателя
        s.setChatId(user_id);
        s.setText(text);
        try { //Чтобы не крашнулась программа при вылете Exception
            execute(s);
        } catch (TelegramApiException e){
            e.printStackTrace();
        }
    }
}
