package system.organizedcurrencyconverter;

import javafx.application.Platform;
import javafx.fxml.FXML;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ProgressBar;
import org.json.JSONException;


public class HelloController {
    public CurrencyService currencyService = new CurrencyService();
    Random rand = new Random();
    private boolean initialized = false;
    private volatile boolean running = true;
    @FXML
    private ProgressBar loadingbar;
    @FXML
    private LineChart<String, Number> exchangeChart;
    @FXML
    private CategoryAxis dateAxis;
    @FXML
    private NumberAxis rateAxis;
    @FXML
    private HelloApplication application;

    @FXML
    public void initialize() {
        /*dateAxis.setTickLabelRotation(90);

        rateAxis.setAutoRanging(false);
        rateAxis.setTickUnit(0.1);*/
        if (initialized) {
            return;
        }
        initialized = true;

        if (loadingbar == null) {
            return;
        }

        new Thread(() -> {
            double progress = 0;
            while (progress < 100 && running) {
                try {
                    currencyService.getExchangeRate("USD", "USD");

                    while (progress < 100 && running) {
                        progress++;
                        double x = progress;


                        Platform.runLater(() -> {
                            if (loadingbar != null) {
                                loadingbar.setProgress(x / 100.0);
                            }
                        });

                        Thread.sleep(10);
                    }

                    if (progress >= 100 && running) {
                        Platform.runLater(() -> {
                            if (loadingbar != null) {
                                loadingbar.setProgress(1.0);
                            }
                        });
                        Platform.runLater(() -> {
                            try {
                                Parent scene = FXMLLoader.load(getClass().getResource("mainscene.fxml"));
                                application.changeScene(scene);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        });
                        break;
                    }
                } catch (Exception e) {
                    if (progress < 85) {
                        progress += rand.nextDouble(0.2);
                    } else {
                        progress += 0.05;
                    }

                    double x = progress;
                    Platform.runLater(() -> {
                        if (loadingbar != null) {

                            loadingbar.setProgress(x / 100.0);
                        }
                    });

                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException ex) {
                        break;
                    }
                }
            }
        }).start();
    }  // Function for progressbar
    public void setApplication(HelloApplication application) {
        this.application = application;
    }
    @FXML
    protected void onTestButtonClick() throws JSONException {
        exchangeChart.getData().clear();
        ArrayList<String> pastData = currencyService.getPastData("USD", "EUR");
        ArrayList<Double> pastValues = new ArrayList<>();
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        for (String x : pastData) {
            String split[] = x.split(" ");
            pastValues.add(Double.parseDouble(split[1]));
            series.getData().add(new XYChart.Data<>(split[0], Double.parseDouble(split[1])));
        }
        double smallestValue = findSmallest(pastValues);
        double largestValue = findLargest(pastValues);
        System.out.println(rateAxis + " " + dateAxis);
        rateAxis.setUpperBound(largestValue + (largestValue/100.0));
        rateAxis.setLowerBound(smallestValue - (smallestValue/100.0));
        System.out.println(rateAxis.getLayoutBounds());
        exchangeChart.getData().add(series);
    }
    private Double findSmallest(ArrayList<Double> vals) {
        double smallest = vals.get(0);
        for (Double x : vals) {
            if (x < smallest) {
                smallest = x;
            }
        }
        return smallest;
    }
    private Double findLargest(ArrayList<Double> vals) {
        double largest = vals.get(0);
        for (Double x : vals) {
            if (x > largest) {
                largest = x;
            }
        }
        return largest;
    }
}