public class Accountant {
    int finalSum;
    public String compute (String txt, String res) {
        String num = txt.substring(1);
        int money = Integer.parseInt(num);
        int result = Integer.parseInt(res);
        if ('+' == txt.charAt(0)){
            return Integer.toString(getSum(money, result));
        }else
            return Integer.toString(getSub(result, money));
    }
    //  int finalSum;
    private int getSum (int a, int b){
        finalSum = a + b;
        return (finalSum);
    }
    private int getSub(int a, int b){
        finalSum = a - b;
        return (finalSum);
    }
    public String dropMySum(){
        return ("0");
    }
}
