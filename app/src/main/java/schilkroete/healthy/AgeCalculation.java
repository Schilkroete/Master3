package schilkroete.healthy;

import java.util.Calendar;


public class AgeCalculation {

    private int startYear;
    private int startMonth;
    private int startDay;
    private int endYear;
    private int endMonth;
    private int endDay;
    private int resYear;
    private int resMonth;
    private int resDay;
    private Calendar start;
    private Calendar end;

    public String getCurrentDate(){
        end = Calendar.getInstance();
        endYear = end.get(Calendar.YEAR);
        endMonth = end.get(Calendar.MONTH);
        endMonth++;
        endDay = end.get(Calendar.DAY_OF_MONTH);
        return endDay + "." + endMonth + "." + endYear;
    }

    public void setDateOfBirth (int sYear, int sMonth, int sDay) {
        startYear = sYear;
        startMonth = sMonth;
        startDay = sDay;
    }


    public void calculateYear() {
        resYear = endYear - startYear;
    }

    public void calculateMonth() {
        if (endMonth >= startMonth) {
            resMonth = endMonth - startMonth;
        } else {
            resMonth = endMonth - startMonth;
            resMonth = 12 + resMonth;
            resYear--;
        }
    }

    public void calculateDay() {
        if (endDay >= startDay) {
            resDay = endDay - startDay;
        } else {
            resDay = endDay - startDay;
            resDay = 30 + resDay;
            if (resMonth == 0){
                resMonth = 11;
                resYear--;
            } else {
                resMonth--;
            }
        }
    }

    public String getResult(){
        return resYear + " Jahre";
    }

    // neu hinzugefügt, damit ich ein Integer für die Datenbank erhalte
    public int getResultInt(){
        return resYear;
    }


}
