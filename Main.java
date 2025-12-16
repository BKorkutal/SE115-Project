// Main.java — Students version
import java.io.*;
import java.util.*;

public class Main {
    static final int MONTHS = 12;
    static final int DAYS = 28;
    static final int COMMS = 5;
    static String[] commodities = {"Gold", "Oil", "Silver", "Wheat", "Copper"};
    static String[] months = {"January","February","March","April","May","June",
            "July","August","September","October","November","December"};


    static int[][][] profits = new int[MONTHS][COMMS][DAYS];


    // ======== REQUIRED METHOD LOAD DATA (Students fill this) ========
    public static void loadData() {

        for (int i = 0; i < MONTHS; i++) {
            for (int j = 0; j < COMMS; j++) {
                for (int k = 0; k < DAYS; k++) {
                    profits[i][j][k] = 0;
                }
            }
        }

        for (int i = 0; i < MONTHS; i++) {

            String path = "Data_Files/" + months[i] + ".txt";

            try {
                BufferedReader reader = new BufferedReader(new FileReader(path));
                String line;

                while ((line = reader.readLine()) != null) {

                    if (line.startsWith("Day")) continue;

                    String[] arr = line.split(",");
                    if (arr.length != 3) continue;

                    int day;
                    int value;

                    try {
                        day = Integer.parseInt(arr[0]);
                        value = Integer.parseInt(arr[2]);
                    } catch (Exception e) {
                        continue;
                    }

                    String name = arr[1];

                    int index = -1;
                    for (int j = 0; j < COMMS; j++) {
                        if (commodities[j].equals(name)) {
                            index = j;
                            break;
                        }
                    }

                    if (day < 1 || day > DAYS) continue;
                    if (index == -1) continue;

                    profits[i][index][day - 1] = value;
                }

                reader.close();

            } catch (Exception e) {
            }
        }
    }



    // ======== 10 REQUIRED METHODS (Students fill these) ========

    public static String mostProfitableCommodityInMonth(int month) {

        if (month < 0 || month >= MONTHS) {
            return "INVALID_MONTH";
        }

        int bestIndex = 0;
        int bestTotal = 0;

        for (int c = 0; c < COMMS; c++) {
            int sum = 0;
            for (int d = 0; d < DAYS; d++) {
                sum += profits[month][c][d];
            }

            if (c == 0 || sum > bestTotal) {
                bestTotal = sum;
                bestIndex = c;
            }
        }
        return commodities[bestIndex] + " " + bestTotal;
    }


    public static int totalProfitOnDay(int month, int day) {

        if (month < 0 || month >= MONTHS) {
            return -99999;
        }
        if (day < 1 || day > DAYS) {
            return -99999;
        }

        int total = 0;
        int d = day - 1;

        for (int i = 0; i < COMMS; i++) {
            total += profits[month][i][d];
        }

        return total;
    }


    public static int commodityProfitInRange(String commodity, int from, int to) {

        if (commodity == null) {
            return -99999;
        }
        if (from < 1 || from > DAYS) {
            return -99999;
        }
        if (to < 1 || to > DAYS) {
            return -99999;
        }
        if (from > to) {
            return -99999;
        }

        int index = -1;


        for (int i = 0; i < COMMS; i++) {
            if (commodities[i].equals(commodity)) {
                index = i;
                break;
            }
        }

        if (index == -1) {
            return -99999;
        }

        int sum = 0;
        for (int m = 0; m < MONTHS; m++) {
            for (int d = from; d <= to; d++) {
                sum += profits[m][index][d - 1];
            }
        }
        return sum;
    }



    public static int bestDayOfMonth(int month) {

        if (month < 0 || month >= MONTHS) {
            return -1;
        }

        int bestDay = 1;
        int bestValue = totalProfitOnDay(month, 1);

        for (int day = 2; day <= DAYS; day++) {
            int value = totalProfitOnDay(month, day);

            if (value > bestValue) {
                bestValue = value;
                bestDay = day;
            }
        }

        return bestDay;
    }


    public static String bestMonthForCommodity(String comm) {
        if (comm == null) {
            return "INVALID_COMMODITY";
        }

        int index = -1;
        for (int i = 0; i < COMMS; i++) {
            if (commodities[i].equals(comm)) {
                index = i;
                break;
            }
        }
        if (index == -1) {
            return "INVALID_COMMODITY";
        }


        int bestMonth = 0;
        int bestTotal = 0;

        for (int m = 0; m < MONTHS; m++) {
            int sum = 0;
            for (int d = 0; d < DAYS; d++) {
                sum += profits[m][index][d];
            }

            if (m == 0 || sum > bestTotal) {
                bestTotal = sum;
                bestMonth = m;
            }
        }


        return months[bestMonth];
    }



    public static int consecutiveLossDays(String comm) {

        if (comm == null) {
            return -1;
        }

        int index = -1;


        for (int i = 0; i < COMMS; i++) {
            if (commodities[i].equals(comm)) {
                index = i;
                break;
            }
        }
        if (index == -1) {
            return -1;
        }

        int best = 0;

        int current = 0;


        for (int m = 0; m< MONTHS; m++) {
            for (int d = 0; d < DAYS; d++) {

                if (profits[m][index][d] < 0) {
                    current++;

                    if (current > best) {
                        best = current;
                    }
                } else {
                    current = 0;
                }
            }
        }
        return best;
    }



    public static int daysAboveThreshold(String comm, int threshold) {

        if (comm == null) {
            return -1 ;
        }

        int index = -1;

        for (int i = 0; i < COMMS; i++) {
            if (commodities[i].equals(comm)) {
                index = i;
                break;
            }
        }

        if (index == -1) {
            return -1;
        }

        int count = 0;

        for (int m = 0; m < MONTHS; m++) {
            for (int d = 0; d < DAYS; d++) {
                if (profits[m][index][d] > threshold) {
                    count++;
                }
            }
        }

        return count;
    }

    public static int biggestDailySwing(int month) {

        if (month < 0 || month >= MONTHS) {
            return -99999;
        }

        int best = 0;


        for (int day = 2; day <= DAYS; day++) {
            int today = totalProfitOnDay(month, day);
            int yesterday = totalProfitOnDay(month, day - 1);

            int diff = today - yesterday;
            if (diff < 0) diff = -diff;

            if (day == 2 || diff > best) {
                best = diff;
            }

        }

        return best;
    }

    public static String compareTwoCommodities(String c1, String c2) {

        if (c1 == null || c2 == null) {
            return "INVALID_COMMODITY";
        }

        int idx1 = -1;
        int idx2 = -1;

        for (int i = 0; i < COMMS; i++) {
            if (commodities[i].equals(c1)) idx1 = i;
            if (commodities[i].equals(c2)) idx2 = i;
        }

        if (idx1 == -1 || idx2 == -1) {
            return "INVALID_COMMODITY";
        }

        int sum1 = 0;
        int sum2 = 0;

        for (int m = 0; m < MONTHS; m++) {
            for (int d = 0; d < DAYS; d++) {
                sum1 += profits[m][idx1][d];
                sum2 += profits[m][idx2][d];
            }
        }

        if (sum1 > sum2) {
            return c1 + " is better by " + (sum1 - sum2);
        } else if (sum2 > sum1) {
            return c2 + " is better by " + (sum2 - sum1);
        } else {
            return "Equal";
        }
    }

    public static String bestWeekOfMonth(int month) {

        if (month < 0 || month >= MONTHS) {
            return "INVALID_MONTH";
        }

        int bestWeek = 1;

        int bestSum = 0;

        for (int week = 1; week <= 4; week++) {
            int startDay = (week - 1) * 7 + 1;
            int endDay = startDay + 6;

            int sum = 0;
            for (int day = startDay; day <= endDay; day++) {
                sum += totalProfitOnDay(month, day);
            }

            if (week == 1 || sum > bestSum) {
                bestSum = sum;
                bestWeek = week;
            }
        }
        return "Week " + bestWeek;
    }


    public static void main(String[] args) {
        loadData();

        System.out.println("Data loaded – ready for queries");

        System.out.println(mostProfitableCommodityInMonth(0));
        System.out.println(totalProfitOnDay(0, 1));
        System.out.println(commodityProfitInRange("Gold", 1, 7));
        System.out.println(bestDayOfMonth(0));
        System.out.println(bestMonthForCommodity("Gold"));
        System.out.println(consecutiveLossDays("Gold"));
        System.out.println(daysAboveThreshold("Gold", 1000));
        System.out.println(biggestDailySwing(0));
        System.out.println(compareTwoCommodities("Gold", "Oil"));
        System.out.println(bestWeekOfMonth(0));
        
    }




}
