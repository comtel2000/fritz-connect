package org.comtel.fritz.connect.cmd;

/**
 * @see <a
 *      href="http://avm.de/fileadmin/user_upload/Global/Service/Schnittstellen/AHA-HTTP-Interface.pdf">http://avm.de/fileadmin/user_upload/Global/Service/Schnittstellen/AHA-HTTP-Interface.pdf</a>
 * 
 * @author comtel
 *
 */
public interface SwitchCmd {

	/**
	 * Liefert die kommaseparierte AIN/MAC Liste aller bekannten Steckdosen
	 * kommaseparierte AIN/MAC-Liste, leer wenn keine Steckdose bekannt
	 */
	final String GETSWITCHLIST = "getswitchlist";

	/**
	 * Schaltet Steckdose ein "1"
	 */
	final String SETSWITCHON = "setswitchon";

	/**
	 * Schaltet Steckdose aus "0"
	 */
	final String SETSWITCHOFF = "setswitchoff";

	/**
	 * Ermittelt Schaltzustand der Steckdose "0" oder "1" (Steckdose aus oder
	 * an), "inval" wenn unbekannt
	 */
	final String GETSWITCHSTATE = "getswitchstate";

	/**
	 * Ermittelt Verbindungsstatus des Aktors "0" oder "1" für Gerät nicht
	 * verbunden bzw. verbunden. Bei Verbindungsverlust wechselt der Zustand
	 * erst mit einigen Minuten Verzögerung zu "0".
	 */
	final String GETSWITCHPRESENT = "getswitchpresent";

	/**
	 * Ermittelt aktuell über die Steckdose entnommene Leistung Leistung in mW,
	 * "inval" wenn unbekannt
	 */
	final String GETSWITCHPOWER = "getswitchpower";

	/**
	 * Liefert die über die Steckdose entnommene Ernergiemenge seit
	 * Erstinbetriebnahme oder Zurücksetzen der Energiestatistik Energie in Wh,
	 * "inval" wenn unbekannt
	 */
	final String GETSWITCHENERGY = "getswitchenergy";

	/**
	 * Liefert Bezeichner des Aktors Name
	 */
	final String GETSWITCHNAME = "getswitchname";

	/**
	 * XML device informations
	 */
	final String GETDEVICELISTINFOS = "getdevicelistinfos";

}