package org.comtel.fritz.connect;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
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
				
				if (port.length() > 5){
					portField.setText(port.substring(0, 5));
				}
				
				if (!port.matches("[0-9]{0,5}")){
					if (oldValue.matches("[0-9]{0,5}")){
						portField.setText(oldValue);
					}else{
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
		testBtn.setDisable(true);
		try {
			SwitcherApp.getSwitchService().validateConnection();
			Dialogs.create().owner(testBtn.getScene().getWindow()).lightweight().title("Test connection").masthead("Connection seems ok").message(SwitcherApp.getSwitchService().getURL()).showInformation();
		} catch (Exception e) {
			Dialogs.create().owner(testBtn.getScene().getWindow()).lightweight().title("Test connection").masthead("Connection fails").message(SwitcherApp.getSwitchService().getURL()).showExceptionInNewWindow(e);
		} finally {
			testBtn.setDisable(false);
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
