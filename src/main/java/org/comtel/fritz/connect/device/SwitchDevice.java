package org.comtel.fritz.connect.device;

import java.io.Serializable;

public class SwitchDevice implements Serializable {

	private static final long serialVersionUID = -6546912948581699562L;

	public enum State {
		ON, OFF, UNDEFINED
	}

	private final String ain;
	private String name;

	private State state = State.UNDEFINED;

	private int power;
	private int energy;
	private int temperature;

	private boolean present;

	public SwitchDevice(String ain) {
		this.ain = ain;
	}

	public final String getName() {
		return name;
	}

	public final void setName(String name) {
		this.name = name;
	}

	public final State getState() {
		return state;
	}

	public final void setState(State state) {
		this.state = state;
	}

	/**
	 * Ermittelt aktuell über die Steckdose entnommene Leistung Leistung in mW,
	 * "inval" wenn unbekannt
	 */
	public final int getPower() {
		return power;
	}

	public final void setPower(int power) {
		this.power = power;
	}

	/**
	 * Liefert die über die Steckdose entnommene Ernergiemenge seit
	 * Erstinbetriebnahme oder Zurücksetzen der Energiestatistik Energie in Wh,
	 * "inval" wenn unbekannt
	 */
	public final int getEnergy() {
		return energy;
	}

	public final void setEnergy(int energy) {
		this.energy = energy;
	}

	public final String getAin() {
		return ain;
	}

	public void setState(String state) {
		switch (state) {
		case "0":
			setState(State.OFF);
			break;
		case "1":
			setState(State.ON);
			break;
		default:
			setState(State.UNDEFINED);
			break;
		}
	}

	public boolean isPresent() {
		return present;
	}

	public void setPresent(boolean present) {
		this.present = present;
	}

	public int getTemperature() {
		return temperature;
	}

	public void setTemperature(int temperature) {
		this.temperature = temperature;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ain == null) ? 0 : ain.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SwitchDevice other = (SwitchDevice) obj;
		if (ain == null) {
			if (other.ain != null)
				return false;
		} else if (!ain.equals(other.ain))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "SwitchDevice [ain=" + ain + ", name=" + name + ", present=" + present + ", state=" + state + ", power=" + power + ", energy=" + energy + ", temp.=" + temperature + "]";
	}
}
