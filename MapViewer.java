import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

public class MapViewer extends Application {

    @Override
    public void start(Stage primaryStage) {
        Button viewProvidersButton = new Button("View Nearby Providers");
        WebView webView = new WebView();

        viewProvidersButton.setOnAction(e -> {
            String mapUrl = "https://www.openstreetmap.org/way/35460706#map=16/12.82359/80.04631";
            webView.getEngine().load(mapUrl);
        });

        VBox root = new VBox(viewProvidersButton, webView);
        Scene scene = new Scene(root, 800, 600);
        primaryStage.setTitle("Nearby Providers");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
