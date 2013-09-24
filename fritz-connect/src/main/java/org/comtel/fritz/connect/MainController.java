package org.comtel.fritz.connect;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import org.comtel.fritz.connect.device.SwitchDevice;
import org.comtel.fritz.connect.device.SwitchDevice.State;
import org.slf4j.LoggerFactory;

public class MainController implements Initializable {

	private SessionManager sessionManager = SessionManager.getSessionManager();

	private final static org.slf4j.Logger logger = LoggerFactory.getLogger(MainController.class);

	private VBox switcherPanel;

	private Accordion settingsPanel;

	private double settingsLastWidth = -1;

	@FXML
	SplitPane splitPane;

	@FXML
	Button refreshBtn;

	@FXML
	ToggleButton settingsBtn;

	@FXML
	Label status;

	@Override
	public void initialize(URL url, ResourceBundle bundle) {
		try {
			settingsPanel = FXMLLoader.load(MainController.class.getResource("settings.fxml"), bundle);
			switcherPanel = new VBox();
			switcherPanel.setPadding(new Insets(10));

			splitPane.getItems().addAll(switcherPanel, settingsPanel);
			splitPane.getDividers().get(0).setPosition(1);

		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}

		sessionManager.bind(settingsBtn.selectedProperty(), "settingToggle");
		sessionManager.bind(splitPane.getDividers().get(0).positionProperty(), "splitPanePosition");

		updateDeviceList(sessionManager.getSwitchDeviceList());

		sessionManager.getSwitchDeviceList().addListener(new ListChangeListener<SwitchDevice>() {
			public void onChanged(ListChangeListener.Change<? extends SwitchDevice> c) {
				while (c.next()) {
					updateDeviceList(c.getAddedSubList());
				}
			}
		});
	}

	private void updateDeviceList(List<? extends SwitchDevice> list) {
		switcherPanel.getChildren().clear();
		
		for (final SwitchDevice dev : list) {

			final CheckBox cb = new CheckBox(dev.getName());
			cb.setId(dev.getAin());
			cb.setTooltip(new Tooltip(String.format("engery: %s mW\n power: %s mW", dev.getEnergy(), dev.getPower())));
			cb.setSelected(dev.getState() == State.ON);
			cb.setDisable(!dev.isPresent());

			switcherPanel.getChildren().add(cb);

			cb.selectedProperty().addListener(new ChangeListener<Boolean>() {
				@Override
				public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean selected) {

					dev.setState(selected ? "1" : "0");
					cb.setDisable(true);
					Task<?> t = new Task<Object>() {
						@Override
						protected Object call() throws Exception {
							try {
								SwitcherApp.getSwitchService().updateDeviceState(dev);
								status.setText(dev.toString());
							} catch (Exception e) {
								status.setText(e.getMessage());
							} finally {
								cb.setDisable(false);
							}
							return null;
						}

					};
					t.run();

				}
			});

		}
	}

	@FXML
	public void toggleSettings(ActionEvent event) {
		final SplitPane.Divider divider = splitPane.getDividers().get(0);
		if (settingsBtn.isSelected()) {
			if (settingsLastWidth == -1) {
				settingsLastWidth = settingsPanel.prefWidth(-1);
			}
			final double divPos = 1 - (settingsLastWidth / splitPane.getWidth());
			new Timeline(new KeyFrame(Duration.seconds(0.3), new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					settingsPanel.setMinWidth(Region.USE_PREF_SIZE);
				}
			}, new KeyValue(divider.positionProperty(), divPos, Interpolator.EASE_BOTH))).play();
		} else {
			settingsLastWidth = settingsPanel.getWidth();
			settingsPanel.setMinWidth(0);
			new Timeline(new KeyFrame(Duration.seconds(0.3), new KeyValue(divider.positionProperty(), 1))).play();
		}
	}

	@FXML
	public void refreshAll(ActionEvent event) {
		refreshBtn.setDisable(true);
		Task<?> t = new Task<Object>() {
			@Override
			protected Object call() throws Exception {
				try {
					sessionManager.getSwitchDeviceList().addAll(SwitcherApp.getSwitchService().getSwitchDevices());
				} catch (Exception e) {
					status.setText(e.getMessage());
				} finally {
					refreshBtn.setDisable(false);
				}
				return null;
			}

		};
		t.run();

	}
}
