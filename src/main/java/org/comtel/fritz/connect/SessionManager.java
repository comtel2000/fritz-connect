/*
 * Copyright (c) 2010, 2013 Oracle and/or its affiliates.
 * All rights reserved. Use is subject to license terms.
 *
 * This file is available and licensed under the following license:
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  - Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *  - Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the distribution.
 *  - Neither the name of Oracle Corporation nor the names of its
 *    contributors may be used to endorse or promote products derived
 *    from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.comtel.fritz.connect;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Accordion;
import javafx.scene.control.TitledPane;
import javafx.scene.control.ToggleGroup;
import javafx.scene.paint.Color;
import org.comtel.fritz.connect.bookmark.Bookmark;
import org.comtel.fritz.connect.bookmark.Bookmarks;
import org.comtel.fritz.connect.bookmark.ObjectFactory;
import org.comtel.fritz.connect.device.SwitchDevice;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.slf4j.LoggerFactory;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.*;
import java.nio.file.*;
import java.util.Properties;

public class SessionManager {

	private final static org.slf4j.Logger logger = LoggerFactory.getLogger(SessionManager.class);

	private String name;
	private final Properties props = new Properties();

	private final ObservableList<SwitchDevice> switchDevices = FXCollections.observableArrayList();
	private final ObservableList<Bookmark> bookmarkList = FXCollections.observableArrayList();

	private final Path bookmarkPath;
	private final Path streamPath;
	private final Path propPath;

	private SessionManager(String name) {
		this.name = name;
		propPath = FileSystems.getDefault().getPath(System.getProperty("user.home"), "." + name + ".properties");
		streamPath = FileSystems.getDefault().getPath(System.getProperty("user.home"), "." + name + ".stream");
		bookmarkPath = FileSystems.getDefault().getPath(System.getProperty("user.home"), "." + name + "_bookmarks.xml");
	}

	private static SessionManager sessionManager;

	public static SessionManager createSessionManager(String name) {
		return sessionManager = new SessionManager(name);
	}

	public static SessionManager getSessionManager() {
		return sessionManager;
	}

	public Properties getProperties() {
		return props;
	}

	public void loadSession() {

		if (Files.exists(propPath, LinkOption.NOFOLLOW_LINKS)) {
			try (InputStream is = Files.newInputStream(propPath, StandardOpenOption.READ)) {
				props.load(is);
			} catch (IOException ex) {
				logger.error(ex.getMessage(), ex);
			}
		}
		try {
			loadSwitchDeviceList();
		} catch (ClassNotFoundException | IOException ex) {
			logger.error(ex.getMessage(), ex);
		}

		try {
			loadBookmarks();
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
		}
	}

	public void saveSession() {
		logger.debug("save session");
		try (OutputStream outStream = Files.newOutputStream(propPath, StandardOpenOption.CREATE, StandardOpenOption.WRITE)) {
			props.store(outStream, name + " session properties");
		} catch (IOException ex) {
			logger.error(ex.getMessage(), ex);
		}

		try {
			saveSwitchDeviceList();
		} catch (IOException ex) {
			logger.error(ex.getMessage(), ex);
		}

		try {
			saveBookmarks();
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
		}
	}

	public void bind(final BooleanProperty property, final String propertyName) {
		String value = props.getProperty(propertyName);
		if (value != null) {
			property.set(Boolean.valueOf(value));
		}
		property.addListener(o -> {
            props.setProperty(propertyName, property.getValue().toString());
        });
	}

	@SuppressWarnings("unchecked")
	public void bind(final ObjectProperty<?> property, final String propertyName, Class<?> type) {
		String value = props.getProperty(propertyName);
		if (value != null) {
			if (type.getName().equals(Color.class.getName())) {
				((ObjectProperty<Color>) property).set(Color.valueOf(value));
			} else if (type.getName().equals(String.class.getName())) {
				((ObjectProperty<String>) property).set(value);
			} else {
				((ObjectProperty<Object>) property).set(value);
			}
		}
		property.addListener(o -> {
            props.setProperty(propertyName, property.getValue().toString());
        });
	}

	public void bind(final DoubleProperty property, final String propertyName) {
		String value = props.getProperty(propertyName);
		if (value != null) {
			property.set(Double.valueOf(value));
		}
		property.addListener(o -> {
            props.setProperty(propertyName, property.getValue().toString());
        });
	}

	public void bind(final ToggleGroup toggleGroup, final String propertyName) {
		try {
			String value = props.getProperty(propertyName);
			if (value != null) {
				int selectedToggleIndex = Integer.parseInt(value);
				toggleGroup.selectToggle(toggleGroup.getToggles().get(selectedToggleIndex));
			}
		} catch (Exception ignored) {
		}
		toggleGroup.selectedToggleProperty().addListener(o -> {
            if (toggleGroup.getSelectedToggle() == null) {
                props.remove(propertyName);
            } else {
                props.setProperty(propertyName, Integer.toString(toggleGroup.getToggles().indexOf(toggleGroup.getSelectedToggle())));
            }
        });
	}

	public void bind(final Accordion accordion, final String propertyName) {
		Object selectedPane = props.getProperty(propertyName);
		for (TitledPane tp : accordion.getPanes()) {
			if (tp.getText() != null && tp.getText().equals(selectedPane)) {
				accordion.setExpandedPane(tp);
				break;
			}
		}
		accordion.expandedPaneProperty().addListener((ov, t, expandedPane) -> {
            if (expandedPane != null) {
                props.setProperty(propertyName, expandedPane.getText());
            }
        });
	}

	public void bind(final StringProperty property, final String propertyName) {
		String value = props.getProperty(propertyName);
		if (value != null) {
			property.set(value);
		}

		property.addListener(o -> {
            props.setProperty(propertyName, property.getValue());
        });
	}

	/**
	 * poor man's property de/encryption
	 * 
	 * @param property
	 * @param propertyName
	 */
	public void bindCrypt(final StringProperty property, final String propertyName) {
		String value = props.getProperty(propertyName);
		if (value != null && !value.isEmpty()) {
			StandardPBEStringEncryptor enc = new StandardPBEStringEncryptor();
			enc.setPassword("X4E8SS09cw_Qq812");
			try {
				property.set(enc.decrypt(value));
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
				property.set("");
			}
		}

		property.addListener(o -> {
            if (property.getValue() == null || property.getValue().isEmpty()) {
                props.setProperty(propertyName, "");
            } else {
                StandardPBEStringEncryptor enc = new StandardPBEStringEncryptor();
                enc.setPassword("X4E8SS09cw_Qq812");
                props.setProperty(propertyName, enc.encrypt(property.getValue()));
            }
        });
	}

	public final ObservableList<SwitchDevice> getSwitchDeviceList() {
		return switchDevices;
	}

	public final ObservableList<Bookmark> getBookmarks() {
		return bookmarkList;
	}

	private void loadSwitchDeviceList() throws IOException, ClassNotFoundException {
		switchDevices.clear();

		if (!Files.exists(streamPath, LinkOption.NOFOLLOW_LINKS)) {
			logger.debug("no stream exist ({})", streamPath);
			return;
		}
		logger.info("load cached device list ({})", streamPath);

		try (InputStream inStream = Files.newInputStream(streamPath, StandardOpenOption.READ)) {
			try (ObjectInputStream oStream = new ObjectInputStream(inStream)) {
				Object o = null;
				while (inStream.available() > 0 && (o = oStream.readObject()) != null) {
					SwitchDevice dev = (SwitchDevice) o;
					logger.debug("read dev: {}", dev);
					if (!switchDevices.contains(dev)) {
						switchDevices.add(dev);
					} else {
						logger.error("device already exist ({})", dev);
					}
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			Files.deleteIfExists(streamPath);
		}
	}

	private void saveSwitchDeviceList() throws IOException {
		if (switchDevices.isEmpty()) {
			Files.deleteIfExists(streamPath);
			return;
		}

		try (OutputStream outStream = Files.newOutputStream(streamPath, StandardOpenOption.CREATE, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING)) {
			try (ObjectOutputStream switchDeviceStream = new ObjectOutputStream(outStream)) {
				for (SwitchDevice dev : switchDevices) {
					switchDeviceStream.writeObject(dev);
				}
			}
		}

	}

	private void loadBookmarks() throws IOException, JAXBException {
		bookmarkList.clear();

		if (!Files.exists(bookmarkPath, LinkOption.NOFOLLOW_LINKS)) {
			logger.debug("no bookmarks exist ({})", bookmarkPath);
			return;
		}
		logger.info("load bookmarks ({})", bookmarkPath);

		JAXBContext context = JAXBContext.newInstance(Bookmarks.class);
		Unmarshaller unmarshaller = context.createUnmarshaller();
		Bookmarks bookmarksXml = null;
		try (BufferedReader reader = Files.newBufferedReader(bookmarkPath)) {
			bookmarksXml = (Bookmarks) unmarshaller.unmarshal(reader);
		}
		if (bookmarksXml != null && bookmarksXml.getBookmark() != null) {
			bookmarkList.addAll(bookmarksXml.getBookmark());
		}
		decryptBookmarks();
	}

	private void saveBookmarks() throws IOException, JAXBException {
		if (bookmarkList.isEmpty()) {
			Files.deleteIfExists(bookmarkPath);
			return;
		}

		JAXBContext context = JAXBContext.newInstance(Bookmarks.class);
		Marshaller m = context.createMarshaller();
		m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

		ObjectFactory fac = new ObjectFactory();
		Bookmarks bookmarks = fac.createBookmarks();
		encryptBookmarks();
		bookmarks.getBookmark().addAll(bookmarkList);
		try (BufferedWriter writer = Files.newBufferedWriter(bookmarkPath, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)) {
			m.marshal(bookmarks, writer);
		}
		bookmarkList.clear();
	}

	private void encryptBookmarks(){
		for (Bookmark bm : bookmarkList){
			String pwd = bm.getPassword();
			if (pwd != null){
				StandardPBEStringEncryptor enc = new StandardPBEStringEncryptor();
				enc.setPassword("X4E8SS09cw_Qq812");
				bm.setPassword(enc.encrypt(pwd));
			}
		}
	}
	
	private void decryptBookmarks(){
		for (Bookmark bm : bookmarkList){
			String pwd = bm.getPassword();
			if (pwd != null){
				StandardPBEStringEncryptor enc = new StandardPBEStringEncryptor();
				enc.setPassword("X4E8SS09cw_Qq812");
				bm.setPassword(enc.decrypt(pwd));
			}
		}
	}
}
