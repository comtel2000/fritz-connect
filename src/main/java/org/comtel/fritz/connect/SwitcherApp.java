package org.comtel.fritz.connect;

import org.comtel.fritz.connect.service.SwitchService;

import javafx.application.Application;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class SwitcherApp extends Application {

	private SessionManager sessionManager;

	private static SwitchService switchService = new SwitchService();

	private Scene scene;

	private final SimpleDoubleProperty sceneWidthProperty = new SimpleDoubleProperty(1024);
	private final SimpleDoubleProperty sceneHeightProperty = new SimpleDoubleProperty(600);

	public static SwitchService getSwitchService() {
		return switchService;
	}

	@Override
	public void start(Stage stage) throws Exception {
		sessionManager = SessionManager.createSessionManager("switcher.app");
		sessionManager.loadSession();

		sessionManager.bind(sceneWidthProperty, "sceneWidth");
		sessionManager.bind(sceneHeightProperty, "sceneHeight");

		stage.setTitle("FRITZ!Box Switcher.App FX");
		scene = new Scene(FXMLLoader.<Parent> load(SwitcherApp.class.getResource("main.fxml")), sceneWidthProperty.get(), sceneHeightProperty.get());

		stage.setScene(scene);
		stage.show();

		stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent event) {
				sceneWidthProperty.set(scene.getWidth());
				sceneHeightProperty.set(scene.getHeight());

				sessionManager.saveSession();
			}
		});

	}

	public static void main(String[] args) {
		launch(args);
	}

}
