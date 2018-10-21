public class ChatCommands {
    String res = "0"; // first result
    String archiveRes = "0";
    String sres = "0"; //second result
    String sarchiveRes = "0";
    DBConnector dbc = new DBConnector();

    public String firstTextReader(String txt, long user_id) {
        Accountant ac = new Accountant();
        String[] subStr;
        subStr = txt.split(" ");

        if (txt.equals("/start")) {
            return "hello";
        } else if (subStr[0].equals("/pay")) {
            try {
                Integer.parseInt(subStr[1]);
                dbc.insertIntoPayments(user_id, subStr[1], "unconfirmed");
                int subsum;
                if (Integer.parseInt(subStr[1]) < 0) {
                    return ("Only positive values.");
                } else
                    subsum = Integer.parseInt(subStr[1]);
                dbc.insertIntoEnrollment(user_id, "-", subsum, "Partial payment");
                return ("Payment was successful");
            } catch (NumberFormatException e) {
                return ("Wrong message format");
            }
        } else if ('+' == txt.charAt(0) || '-' == txt.charAt(0)) {
            int subsum;
            try {
                subsum = Integer.parseInt(subStr[0].substring(1));
                String msg = "";
                for (int i = 1; i < subStr.length; i++) {
                    msg = msg + subStr[i] + " ";
                }
                dbc.insertIntoEnrollment(user_id, String.valueOf(txt.charAt(0)), subsum, msg);
                return (null);
            } catch (NumberFormatException e) {
                return ("Wrong message format");
            }
        } else if (txt.equals("/getalldebts")) {
            return dbc.getalldebts(user_id);
        }else if (txt.equals("/getcurrentdebt")) {
            return ("Your current debt is - " + dbc.getcurrentdebt(user_id));
        }else if (txt.equals("/getarchivesum")) {
            return (archiveRes);
        }else if (txt.equals("/dropcurrentsum")) {
            return dbc.dropcurrentsum(user_id);
        }else if (txt.equals("/getarchivepayments")) {
            return dbc.getarchivepayments(user_id);
        }else if ((subStr[0].equals("/changepaymentstatus"))) {
            try {
                int paymNum = Integer.parseInt(subStr[1]);
                dbc.changepaymentstatus(user_id, paymNum);
                return null;
            }catch (NumberFormatException e) {
                return ("Wrong message format");
            }

        }
        else if (txt.equals("/help")) {
            String msg = "/start - Returns a welcome message. But not for everyone. \n" +
                    "/pay *num* - Partial payment in the amount of the specified number. Returns \"Payment was successful\" " +
                    "in case of success. \n " +
                    "[+*num* *comment*] - Adds a record of debt to the database. " +
                    "If successful, returns nothing. In case of error returns \"Wrong message format\". \n" +
                    "My future abilities: \n" +
                    "/dropcurrentsum - Reset the debt completely.\n" +
                    "/getcurrentdebt - Returns the amount of current debt. \n" +
                    "/getalldebts - Returns all fucked tasks. \n" +
                    "/getarchivepayments - Returns the payment history. \n" +
                    "/changepaymentstatus - Changes the payment confirmation status. Accepts the payment order number from the " +
                    "*/getarchivepayments* command. You can not change the status of your payment, only the payment of your opponent.";
            return msg;
        }else return "unknown message";
    }

 /*   public String secondTextReader (String txt){
        Accountant ac = new Accountant();
        if (txt.equals("/start")) {
            return "hello";
        }else if ('+' == txt.charAt(0) || '-' == txt.charAt(0)) {
            sres = ac.compute(txt, sres);
            return(sres);
        }else if (txt.equals("/dropcurrentsum")){
            sarchiveRes = sres;
            sres = ac.dropMySum();
            return (sres);
        }else if (txt.equals("/getarchivesum")){
            return (sarchiveRes);
        }
        else return "unknown message";
    }*/
}
