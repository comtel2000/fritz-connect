package org.comtel.fritz.connect;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;

import org.controlsfx.dialog.Dialogs;
import org.slf4j.LoggerFactory;

public class SettingsController implements Initializable {

	private final static org.slf4j.Logger logger = LoggerFactory.getLogger(SettingsController.class);

	@FXML
	private Accordion settingsPane;

	@FXML
	private TextField ipField;

	@FXML
	private TextField portField;

	@FXML
	private TextField userField;

	@FXML
	private PasswordField pwdField;

	@FXML
	private CheckBox sslCheckBox;

	@FXML
	private Button testBtn;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

		settingsPane.expandedPaneProperty().addListener(new ChangeListener<TitledPane>() {
			@Override
			public void changed(ObservableValue<? extends TitledPane> observable, TitledPane oldValue, TitledPane newValue) {
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						if (settingsPane.getExpandedPane() == null)
							settingsPane.setExpandedPane(settingsPane.getPanes().get(0));
					}
				});
			}
		});

		portField.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String port) {

				if (port == null || port.isEmpty()) {
					SwitcherApp.getSwitchService().getPortProperty().set(-1);
					return;
				}

				if (port.length() > 5) {
					portField.setText(port.substring(0, 5));
				}

				if (!port.matches("[0-9]{0,5}")) {
					if (oldValue.matches("[0-9]{0,5}")) {
						portField.setText(oldValue);
					} else {
						portField.setText("");
					}
					return;
				}

				try {
					SwitcherApp.getSwitchService().getPortProperty().set(Integer.valueOf(port));
				} catch (NumberFormatException e) {
					logger.error(e.getMessage(), e);
				}
			}
		});

		SessionManager sessionManager = SessionManager.getSessionManager();

		sessionManager.bind(ipField.textProperty(), "ipField");
		sessionManager.bind(portField.textProperty(), "portField");
		sessionManager.bind(userField.textProperty(), "userField");
		sessionManager.bindCrypt(pwdField.textProperty(), "pwdField");
		sessionManager.bind(sslCheckBox.selectedProperty(), "useSSL");

		sessionManager.bind(settingsPane, "settingsPane");

		SwitcherApp.getSwitchService().getHostProperty().bind(ipField.textProperty());
		SwitcherApp.getSwitchService().getUserProperty().bind(userField.textProperty());
		SwitcherApp.getSwitchService().getPwdProperty().bind(pwdField.textProperty());
		SwitcherApp.getSwitchService().getSslProperty().bind(sslCheckBox.selectedProperty());
	}

	@FXML
	public void testConnection(ActionEvent event) {

		TestConnectionService service = new TestConnectionService();

		settingsPane.disableProperty().bind(service.runningProperty());
		service.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
			@Override
			public void handle(WorkerStateEvent t) {
				Dialogs.create().owner(testBtn.getScene().getWindow()).lightweight().title("CONNECTION").masthead("Response ok from URL: " + SwitcherApp.getSwitchService().getURL())
						.message("try to refresh smart home devices").showInformation();
			}
		});
		service.setOnFailed(new EventHandler<WorkerStateEvent>() {
			@Override
			public void handle(WorkerStateEvent t) {
				Throwable e = t.getSource().getException();
				String msg = e != null ? e.getMessage() : "general error";;
				Dialogs.create().owner(testBtn.getScene().getWindow()).lightweight().title("CONNECTION ERROR").masthead("Test connection fails! URL: " + SwitcherApp.getSwitchService().getURL())
						.message(msg).showExceptionInNewWindow(e);
				logger.error("test host connection failed", e);
			}
		});
		service.start();
	}

	private class TestConnectionService extends Service<Void> {

		protected Task<Void> createTask() {
			return new Task<Void>() {
				protected Void call() throws IOException, Exception {
					SwitcherApp.getSwitchService().validateConnection();
					return null;
				}
			};
		}
	}

	@FXML
	public void resetFields(ActionEvent event) {
		ipField.setText("fritz.box");
		portField.setText("");
		sslCheckBox.setSelected(false);
		userField.setText("");
		pwdField.setText("");
	}
}
