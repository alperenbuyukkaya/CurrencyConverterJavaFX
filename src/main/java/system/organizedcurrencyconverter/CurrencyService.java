package system.organizedcurrencyconverter;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public class CurrencyService {
    private JSONObject exchangeRates;
    private ArrayList<JSONObject> pastRates;
    private ScheduledExecutorService scheduler;
    private ArrayList<String> dayList;

    public CurrencyService() {
        new Thread(() -> updatePastRates()).start();
        this.exchangeRates = new JSONObject();
        this.scheduler = Executors.newScheduledThreadPool(1);
        this.pastRates = new ArrayList<>();
        this.dayList = new ArrayList<>();
        rateUpdateStart();
    }
    public ArrayList<String> getPastData(String conversionFrom, String conversionTo) throws JSONException {
        ArrayList<String> pastData = new ArrayList<>();
        for (int i = 0; i < pastRates.size(); i++) {
            double dayRate = pastRates.get(i).getDouble(conversionTo) / pastRates.get(i).getDouble(conversionFrom);
            pastData.add(dayList.get(i) + " " + dayRate);
        }
        /*for (JSONObject i : pastRates) {
            System.out.println(i);
            double dayRate = i.getDouble(conversionTo) / i.getDouble(conversionFrom);
            pastData.add(String.valueOf(dayRate));
        }*/
        return pastData;
    }
    private void updatePastRates(){
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Instant now = Instant.now();
        String actualDate = dateFormat.format(Date.from(now));
        String monthBeforeDate = dateFormat.format(Date.from(now.minus(Duration.ofDays(30))));
        //System.out.println(actualDate + " " + monthBeforeDate);
        String baseString = "https://api.frankfurter.app/" + monthBeforeDate + ".." + actualDate + "?base=USD";
        //System.out.println(baseString);
        try{
            OkHttpClient mainClient = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(baseString)
                    .get()
                    .build();
            Response response = mainClient.newCall(request).execute();
            String html = response.body().string();
            JSONObject jsonObject = new JSONObject(html);
            //System.out.println(jsonObject);
            JSONObject pastData = jsonObject.getJSONObject("rates");
            dayList = sortDays(pastData);
            for (String day : dayList) {
                JSONObject dayObject = pastData.getJSONObject(day);
                dayObject.put("USD", 1);
                pastRates.add(dayObject);
            }
            //System.out.println(pastRates);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private ArrayList<String> sortDays(JSONObject pastData){
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        ArrayList<String> dateList = new ArrayList<>();
        try{
            for (Iterator i = pastData.keys(); i.hasNext(); ) {
                //System.out.println(pastJSON.names());
                Date mainDate = dateFormat.parse(i.next().toString());
                dateList.add(dateFormat.format(mainDate));
            }
            Collections.sort(dateList);
        }catch (Exception e){
            System.out.println(e);
        }
        return dateList;
    }
    private void updateRates() {
        try{
            OkHttpClient mainClient = new OkHttpClient();
            Request request = new Request.Builder()
                    .url("https://api.frankfurter.app/latest?base=USD")
                    .get()
                    .build();
            Response response = mainClient.newCall(request).execute();
            String html = response.body().string();
            JSONObject jsonObject = new JSONObject(html);
            JSONObject exchangeRates = jsonObject.getJSONObject("rates");
            exchangeRates.put("USD",1);
            this.exchangeRates = exchangeRates;
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private void rateUpdateStart(){
        scheduler.scheduleAtFixedRate(() -> {
            try{
                updateRates();
            }catch (Exception e){
                e.printStackTrace();
            }
        },0,10, TimeUnit.MINUTES);
    }
    public double getExchangeRate(String conversionFrom, String conversionTo) {
        /*while (true){
            if (exchangeRates.has(conversionFrom) && exchangeRates.has(conversionTo)){
                break;
            }
        }*/
        try{
            return this.exchangeRates.getDouble(conversionTo) / this.exchangeRates.getDouble(conversionFrom);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }
}
