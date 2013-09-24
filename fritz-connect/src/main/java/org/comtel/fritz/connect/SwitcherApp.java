package org.comtel.fritz.connect;

import org.comtel.fritz.connect.service.SwitchService;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class SwitcherApp extends Application {

	private SessionManager sessionManager;

	private static SwitchService switchService = new SwitchService();
	

	public static SwitchService getSwitchService() {
		return switchService;
	}

	
	@Override
	public void start(Stage stage) throws Exception {
		sessionManager = SessionManager.createSessionManager("SwitcherApp");
		sessionManager.loadSession();

		Scene scene = new Scene(FXMLLoader.<Parent> load(SwitcherApp.class.getResource("main.fxml")));
		stage.setScene(scene);
		stage.show();

		stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent event) {
				sessionManager.saveSession();
			}
		});


	}
	

    public static void main(String[] args) {
        launch(args);
    }


}
