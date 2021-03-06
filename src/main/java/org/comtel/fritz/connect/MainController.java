package org.comtel.fritz.connect;

import java.io.IOException;
import java.net.URL;
import java.net.UnknownHostException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Collection;
import java.util.List;
import java.util.ResourceBundle;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.collections.ListChangeListener;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import org.comtel.fritz.connect.device.SwitchDevice;
import org.comtel.fritz.connect.device.SwitchDevice.State;
import org.comtel.fritz.connect.exception.ServiceNotSupportedException;
import org.controlsfx.dialog.Dialogs;
import org.slf4j.LoggerFactory;

public class MainController implements Initializable {

	private SessionManager sessionManager = SessionManager.getSessionManager();

	private ResourceBundle bundle;
	private final static org.slf4j.Logger logger = LoggerFactory.getLogger(MainController.class);

	private VBox switcherPanel;

	private Accordion settingsPanel;

	private double settingsLastWidth = -1;

	@FXML
	SplitPane splitPane;

	@FXML
	ProgressIndicator progress;

	@FXML
	Button refreshBtn;

	@FXML
	ToggleButton settingsBtn;

	@FXML
	Label status;

	@Override
	public void initialize(URL url, ResourceBundle bundle) {
		this.bundle = bundle;
		try {
			settingsPanel = FXMLLoader.load(MainController.class.getResource("settings.fxml"), bundle);
			ScrollPane scrollPane = new ScrollPane(switcherPanel = new VBox());
			switcherPanel.setId("switcherPane");

			splitPane.getItems().addAll(scrollPane, settingsPanel);
			splitPane.getDividers().get(0).setPosition(1.0);

		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}

		// sessionManager.bind(settingsBtn.selectedProperty(), "settingToggle");
		sessionManager.bind(splitPane.getDividers().get(0).positionProperty(), "splitPanePosition");

		updateDeviceList(sessionManager.getSwitchDeviceList());

		sessionManager.getSwitchDeviceList().addListener((ListChangeListener.Change<? extends SwitchDevice> c) -> {
			while (c.next()) {
				updateDeviceList(c.getAddedSubList());
			}
		});
	}

	private void updateDeviceList(List<? extends SwitchDevice> list) {
		switcherPanel.getChildren().clear();
		list.forEach((dev) -> {
			CheckBox cb = new CheckBox(String.format("%s\t[%s, %s, %s]", dev.getName(), FritzUtils.getEnergy(dev.getEnergy()), FritzUtils.getPower(dev.getPower()),
					FritzUtils.getTemperature(dev.getTemperature())));
			cb.setId(dev.getAin());
			cb.setTooltip(new Tooltip(String.format("ain:\t\t%s\nenergy:\t%s\npower:\t%s\ntemp:\t%s", dev.getAin(), FritzUtils.getEnergy(dev.getEnergy()), FritzUtils.getPower(dev.getPower()),
					FritzUtils.getTemperature(dev.getTemperature()))));
			cb.setSelected(dev.getState() == State.ON);
			cb.setDisable(!dev.isPresent());

			cb.setPrefWidth(350);

			switcherPanel.getChildren().add(cb);

			// switcherPanel.getChildren().add((Node)
			// FXMLLoader.load(DeviceController.class.getResource("device.fxml"),
			// bundle));

			cb.selectedProperty().addListener(
					(observable, oldValue, selected) -> {

						if (cb.isDisable()) {
							return;
						}
						dev.setState(selected ? "1" : "0");

						UpdateDeviceState service = new UpdateDeviceState(dev);
						cb.disableProperty().bind(service.runningProperty());
						service.setOnSucceeded(t -> {
							updateStatus(t.getSource().getValue().toString());
						});
						service.setOnFailed(t -> {
							if (t.getSource().getException() instanceof ServiceNotSupportedException) {
								Dialogs.create().owner(refreshBtn.getScene().getWindow()).lightweight().title("Service not available")
										.masthead("Firmware does not support AHA webservice. Please upgrade firmware to FRITZ!OS 05.55 or later.")
										.showExceptionInNewWindow(t.getSource().getException());
								updateStatus("Service not available");
							} else if (t.getSource().getException() instanceof UnknownHostException) {
								updateStatus(String.format("Synchronized with url: %s failed. Unknown host adress", t.getSource().getException().getMessage()));
							} else {
								updateStatus("Error: " + t.getSource().getException().getMessage());
							}
							logger.error("refresh failed", t.getSource().getException());
						});
						service.start();

					});

		});
	}

	@FXML
	public void toggleSettings(ActionEvent event) {
		final SplitPane.Divider divider = splitPane.getDividers().get(0);
		if (settingsBtn.isSelected()) {
			if (settingsLastWidth == -1) {
				settingsLastWidth = settingsPanel.prefWidth(-1);
			}
			final double divPos = 1 - (settingsLastWidth / splitPane.getWidth());
			new Timeline(new KeyFrame(Duration.seconds(0.3), event1 -> {
				settingsPanel.setMinWidth(Region.USE_PREF_SIZE);
			}, new KeyValue(divider.positionProperty(), divPos, Interpolator.EASE_BOTH))).play();
		} else {
			settingsLastWidth = settingsPanel.getWidth();
			settingsPanel.setMinWidth(0);
			new Timeline(new KeyFrame(Duration.seconds(0.3), new KeyValue(divider.positionProperty(), 1))).play();
		}
	}

	@FXML
	public void refreshAll(ActionEvent event) {

		final long time = System.currentTimeMillis();

		DeviceListRequest service = new DeviceListRequest();
		refreshBtn.disableProperty().bind(service.runningProperty());
		progress.visibleProperty().bind(service.runningProperty());
		switcherPanel.disableProperty().bind(service.runningProperty());

		service.setOnSucceeded(t -> {
			sessionManager.getSwitchDeviceList().clear();
			sessionManager.getSwitchDeviceList().addAll((Collection<SwitchDevice>) t.getSource().getValue());
			updateStatus(String.format("Synchronized with url: %s [%dms]", SwitcherApp.getSwitchService().getURL(), (System.currentTimeMillis() - time)));
		});

		service.setOnFailed(t -> {
			if (t.getSource().getException() instanceof ServiceNotSupportedException) {
				Dialogs.create().owner(refreshBtn.getScene().getWindow()).lightweight().title("Service not available")
						.masthead("Firmware does not support AHA webservice. Please upgrade firmware to FRITZ!OS 05.55 or later.").showExceptionInNewWindow(t.getSource().getException());
				updateStatus("Service not available");
			} else if (t.getSource().getException() instanceof UnknownHostException) {
				updateStatus(String.format("Synchronized with url: %s failed. Unknown host adress", t.getSource().getException().getMessage()));
			} else {
				updateStatus("Error: " + t.getSource().getException().getMessage());
			}
			logger.error("refresh failed", t.getSource().getException());
		});

		service.start();

	}

	private class DeviceListRequest extends Service<Collection<SwitchDevice>> {
		protected Task<Collection<SwitchDevice>> createTask() {
			return new Task<Collection<SwitchDevice>>() {
				protected Collection<SwitchDevice> call() throws ServiceNotSupportedException, Exception {
					return SwitcherApp.getSwitchService().getSwitchDevices();
				}
			};
		}
	}

	private class UpdateDeviceState extends Service<SwitchDevice> {
		private final SwitchDevice dev;

		public UpdateDeviceState(SwitchDevice d) {
			dev = d;
		}

		protected Task<SwitchDevice> createTask() {
			return new Task<SwitchDevice>() {
				protected SwitchDevice call() throws ServiceNotSupportedException, Exception {
					SwitcherApp.getSwitchService().updateDeviceState(dev);
					return dev;
				}
			};
		}
	}

	private void updateStatus(String msg) {
		DateTimeFormatter df = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT);
		status.setText(String.format("[%s] %s", df.format(LocalDateTime.now()), msg));
	}
}
