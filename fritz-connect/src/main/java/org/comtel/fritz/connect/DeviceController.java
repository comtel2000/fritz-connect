package org.comtel.fritz.connect;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;

import org.comtel.fritz.connect.device.SwitchDevice;
import org.comtel.fritz.connect.device.SwitchDevice.State;
import org.comtel.fritz.connect.exception.ServiceNotSupportedException;
import org.controlsfx.dialog.Dialogs;
import org.slf4j.LoggerFactory;

public class DeviceController implements Initializable {

	private final static org.slf4j.Logger logger = LoggerFactory.getLogger(DeviceController.class);

	private final SimpleObjectProperty<SwitchDevice> switchDevice = new SimpleObjectProperty<>();
	@FXML
	private Label nameLbl;

	@FXML
	private Label ainLbl;

	@FXML
	private Label energyLbl;

	@FXML
	private Label powerLbl;

	@FXML
	private CheckBox switchCBox;

	@FXML
	private Button refreshBtn;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

		switchDevice.addListener(new ChangeListener<SwitchDevice>() {

			@Override
			public void changed(ObservableValue<? extends SwitchDevice> observable, SwitchDevice olddev, SwitchDevice dev) {
				switchCBox.setId(dev.getAin());
				switchCBox.setSelected(dev.getState() == State.ON);

				nameLbl.setText(dev.getName());
				ainLbl.setText(dev.getAin());
				energyLbl.setText(String.format("%d Wh", dev.getName(), dev.getEnergy()));
				powerLbl.setText(String.format("%.2f W", dev.getPower() > 0 ? (double) dev.getPower() / 1000 : 0d));
			}
		});
		
		switchCBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, final Boolean oldValue, Boolean selected) {

				if (switchCBox.isDisable()){
					return;
				}
				getDevice().setState(selected ? "1" : "0");
				switchCBox.setDisable(true);
				Task<?> t = new Task<Object>() {
					@Override
					protected Object call() throws Exception {
						try {
							SwitcherApp.getSwitchService().updateDeviceState(getDevice());
							updateStatus(getDevice().toString());
						} catch (ServiceNotSupportedException e) {
							Dialogs.create().owner(refreshBtn.getScene().getWindow()).lightweight().title("Service not available").masthead("Firmware does not support AHA webservice. Please upgrade firmware to FRITZ!OS 05.55 or later.").showExceptionInNewWindow(e);
							updateStatus("Service not available");
							switchCBox.setSelected(oldValue);
						} catch (Exception e) {
							updateStatus(e.getMessage());
							switchCBox.setSelected(oldValue);
						} finally {
							switchCBox.setDisable(false);
						}
						return null;
					}

				};
				t.run();

			}
		});
	}

	public void updateDevice(SwitchDevice dev) {
		switchDevice.set(dev);
	}

	public SwitchDevice getDevice() {
		return switchDevice.get();
	}
	
	@FXML
	public void refresh(ActionEvent event) {
		refreshBtn.setDisable(true);
		Task<?> t = new Task<Object>() {
			@Override
			protected Object call() throws Exception {
				try {
					SwitcherApp.getSwitchService().refreshSwitchDevice(getDevice());
					updateStatus("Synchronized with url: " + SwitcherApp.getSwitchService().getURL());
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
	
	private void updateStatus(String msg) {
		logger.info("{}", msg);
		
	}
	
}
