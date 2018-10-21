import java.sql.*;
import java.util.Date;

public class DBConnector {
    private Connection connect() {
        // SQLite connection string
        String url = "jdbc:sqlite:D://uni//javaP//SQLite//db//accountingBot.db";
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    // Табл. всего. выполняется при вызове команды / +число и /pay число
    public void insertIntoEnrollment(long contact, String target, int amount, String comment) {
        String sql = "INSERT INTO enrollment(contact, target, amount, comment, crDate) VALUES(?, ?, ?, ?, ?)";
        Date date = new Date();
        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, contact);
            pstmt.setString(2, target);
            pstmt.setInt(3, amount);
            pstmt.setString(4, comment);
            pstmt.setString(5, date.toString());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // Табл. выплаты. выполняется при вызове команды /pay число
    public void insertIntoPayments(long payerID, String sum, String status) {
        String sql = "INSERT INTO payments(payerID, sum, status, payDate) VALUES(?, ?, ?, ?)";
        Date date = new Date();
        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, payerID);
            pstmt.setString(2, sum);
            pstmt.setString(3, status);
            pstmt.setString(4, date.toString());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    public String getalldebts(long user_id) {
        String sql = "SELECT IDE, contact, target, amount, comment, crDate FROM enrollment";
        try (Connection conn = this.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            int i = 1;
            String msg = "";
            while (rs.next()) {
                if (user_id == rs.getLong("contact") && rs.getString("target").equals("+")) {
                    msg = msg + "\n" + i++ + ( "\t" +
                            rs.getLong("contact") + "\t" +
                            rs.getString("target") + "\t" +
                            rs.getInt("amount") + "\t" +
                            rs.getString("comment") + "\t" +
                            rs.getString("crDate"));
                }
            }
            return msg;
        } catch (SQLException e) {
            return ("Error in Enrollment");
        }
    }
    public String getcurrentdebt (long user_id){
        String sql = "SELECT contact, target, amount FROM enrollment";
        try (Connection conn = this.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            int sum = 0;
            while (rs.next()) {
                if (user_id == rs.getLong("contact") && rs.getString("target").equals("+")) {
                    sum = sum + rs.getInt("amount");
                }else if (user_id == rs.getLong("contact") && rs.getString("target").equals("-"))
                    sum = sum + (rs.getInt("amount") * (-1));
            }
            return Integer.toString(sum);
        } catch (SQLException e) {
            return ("Error in selectAllFromEnrollment");
        }
    }
    public String dropcurrentsum (long user_id) {
        int currsum = Integer.parseInt(getcurrentdebt(user_id));
        insertIntoEnrollment(user_id, "-", currsum, "Full repayment.");
        insertIntoPayments(user_id, Integer.toString(currsum), "unconfirmed");
        return ("Successfully completed");
    }
    public String getarchivepayments (long user_id){
        String sql = "SELECT IDP, payerID, sum, status, payDate FROM payments";

        try (Connection conn = this.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            String msg = "";
            int i = 1;
            // loop through the result set
            while (rs.next()) {
            //    if (user_id == rs.getLong("payerID")) {
                    msg = msg + "\n" + ("\t" +
                            rs.getInt("idp") + "." + "\t" +
                    rs.getString("sum") + "\t" +
                            rs.getString("status") + "\t" +
                            rs.getString("payDate"));
          //      }
            } return msg;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return ("Error in Payments");
        }
    }
    public String changepaymentstatus (long user_id, int num){
        String sql = "update payments set status = 'confirmed' where payerID <> ? and IDP = ?";
        Date date = new Date();
        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, user_id);
            pstmt.setInt(2, num);
            pstmt.executeUpdate();
            return ("+");
        } catch (SQLException e) {
            return ("Error in changepaymentstatus");
        }
    }
}

