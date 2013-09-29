package org.comtel.fritz.connect;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
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
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
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

		//sessionManager.bind(settingsBtn.selectedProperty(), "settingToggle");
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

			final CheckBox cb = new CheckBox(String.format("%s\t[%d Wh, %.2f W]", dev.getName(), dev.getEnergy(), dev.getPower() > 0 ? (double) dev.getPower() / 1000 : 0d));
			cb.setId(dev.getAin());
			cb.setTooltip(new Tooltip(String.format("ain:\t\t%s\nenergy:\t%dWh\npower:\t%.2fW", dev.getAin(), dev.getEnergy(), dev.getPower() > 0 ? (double) dev.getPower() / 1000 : 0d)));
			cb.setSelected(dev.getState() == State.ON);
			cb.setDisable(!dev.isPresent());

			switcherPanel.getChildren().add(cb);

			//switcherPanel.getChildren().add((Node) FXMLLoader.load(DeviceController.class.getResource("device.fxml"), bundle));
			
			cb.selectedProperty().addListener(new ChangeListener<Boolean>() {
				@Override
				public void changed(ObservableValue<? extends Boolean> observable, final Boolean oldValue, Boolean selected) {

					if (cb.isDisable()){
						return;
					}
					dev.setState(selected ? "1" : "0");
					cb.setDisable(true);
					Task<?> t = new Task<Object>() {
						@Override
						protected Object call() throws Exception {
							try {
								SwitcherApp.getSwitchService().updateDeviceState(dev);
								updateStatus(dev.toString());
							} catch (ServiceNotSupportedException e) {
								Dialogs.create().owner(refreshBtn.getScene().getWindow()).lightweight().title("Service not available").masthead("Firmware does not support AHA webservice. Please upgrade firmware to FRITZ!OS 05.55 or later.").showExceptionInNewWindow(e);
								updateStatus("Service not available");
								cb.setSelected(oldValue);
							} catch (Exception e) {
								updateStatus(e.getMessage());
								cb.setSelected(oldValue);
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
					long time = System.currentTimeMillis();
					sessionManager.getSwitchDeviceList().clear();
					sessionManager.getSwitchDeviceList().addAll(SwitcherApp.getSwitchService().getSwitchDevices());
					updateStatus(String.format("Synchronized with url: %s [%dms]", SwitcherApp.getSwitchService().getURL(), (System.currentTimeMillis() - time)));
				} catch (ServiceNotSupportedException e) {
					Dialogs.create().owner(refreshBtn.getScene().getWindow()).lightweight().title("Service not available").masthead("Firmware does not support AHA webservice. Please upgrade firmware to FRITZ!OS 05.55 or later.").showExceptionInNewWindow(e);
					updateStatus("Service not available");
				}  catch (Exception e) {
					updateStatus(e.getMessage());
				} finally {
					refreshBtn.setDisable(false);
				}
				return null;
			}

		};
		t.run();

	}
	
	private void updateStatus(String msg){
		DateTimeFormatter df = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT);
		status.setText(String.format("[%s] %s", df.format(LocalDateTime.now()), msg));
	}
}
