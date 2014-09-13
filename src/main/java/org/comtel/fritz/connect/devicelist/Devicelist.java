//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2014.09.13 at 04:45:07 PM MESZ 
//


package org.comtel.fritz.connect.devicelist;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="device" maxOccurs="unbounded" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="present" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *                   &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element ref="{}switch" minOccurs="0"/>
 *                   &lt;element name="powermeter" minOccurs="0">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;sequence>
 *                             &lt;element name="power" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *                             &lt;element name="energy" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *                           &lt;/sequence>
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                   &lt;element name="temperature">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;sequence>
 *                             &lt;element name="celsius" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *                             &lt;element name="offset" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *                           &lt;/sequence>
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                 &lt;/sequence>
 *                 &lt;attribute name="identifier" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                 &lt;attribute name="id" type="{http://www.w3.org/2001/XMLSchema}int" />
 *                 &lt;attribute name="functionbitmask" type="{http://www.w3.org/2001/XMLSchema}int" />
 *                 &lt;attribute name="fwversion" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                 &lt;attribute name="manufacturer" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                 &lt;attribute name="productname" type="{http://www.w3.org/2001/XMLSchema}string" />
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="group" maxOccurs="unbounded" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="present" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *                   &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element ref="{}switch" minOccurs="0"/>
 *                   &lt;element name="groupinfo">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;sequence>
 *                             &lt;element name="masterdeviceid" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *                             &lt;element name="members" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *                           &lt;/sequence>
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                 &lt;/sequence>
 *                 &lt;attribute name="identifier" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                 &lt;attribute name="id" type="{http://www.w3.org/2001/XMLSchema}int" />
 *                 &lt;attribute name="functionbitmask" type="{http://www.w3.org/2001/XMLSchema}int" />
 *                 &lt;attribute name="fwversion" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                 &lt;attribute name="manufacturer" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                 &lt;attribute name="productname" type="{http://www.w3.org/2001/XMLSchema}string" />
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/sequence>
 *       &lt;attribute name="version" type="{http://www.w3.org/2001/XMLSchema}int" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "device",
    "group"
})
@XmlRootElement(name = "devicelist")
public class Devicelist {

    protected List<Devicelist.Device> device;
    protected List<Devicelist.Group> group;
    @XmlAttribute(name = "version")
    protected Integer version;

    /**
     * Gets the value of the device property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the device property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDevice().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Devicelist.Device }
     * 
     * 
     */
    public List<Devicelist.Device> getDevice() {
        if (device == null) {
            device = new ArrayList<Devicelist.Device>();
        }
        return this.device;
    }

    /**
     * Gets the value of the group property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the group property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getGroup().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Devicelist.Group }
     * 
     * 
     */
    public List<Devicelist.Group> getGroup() {
        if (group == null) {
            group = new ArrayList<Devicelist.Group>();
        }
        return this.group;
    }

    /**
     * Gets the value of the version property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getVersion() {
        return version;
    }

    /**
     * Sets the value of the version property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setVersion(Integer value) {
        this.version = value;
    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="present" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
     *         &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element ref="{}switch" minOccurs="0"/>
     *         &lt;element name="powermeter" minOccurs="0">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;sequence>
     *                   &lt;element name="power" type="{http://www.w3.org/2001/XMLSchema}int"/>
     *                   &lt;element name="energy" type="{http://www.w3.org/2001/XMLSchema}int"/>
     *                 &lt;/sequence>
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *         &lt;element name="temperature">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;sequence>
     *                   &lt;element name="celsius" type="{http://www.w3.org/2001/XMLSchema}int"/>
     *                   &lt;element name="offset" type="{http://www.w3.org/2001/XMLSchema}int"/>
     *                 &lt;/sequence>
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *       &lt;/sequence>
     *       &lt;attribute name="identifier" type="{http://www.w3.org/2001/XMLSchema}string" />
     *       &lt;attribute name="id" type="{http://www.w3.org/2001/XMLSchema}int" />
     *       &lt;attribute name="functionbitmask" type="{http://www.w3.org/2001/XMLSchema}int" />
     *       &lt;attribute name="fwversion" type="{http://www.w3.org/2001/XMLSchema}string" />
     *       &lt;attribute name="manufacturer" type="{http://www.w3.org/2001/XMLSchema}string" />
     *       &lt;attribute name="productname" type="{http://www.w3.org/2001/XMLSchema}string" />
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "present",
        "name",
        "_switch",
        "powermeter",
        "temperature"
    })
    public static class Device {

        protected boolean present;
        @XmlElement(required = true)
        protected String name;
        @XmlElement(name = "switch")
        protected Switch _switch;
        protected Devicelist.Device.Powermeter powermeter;
        @XmlElement(required = true)
        protected Devicelist.Device.Temperature temperature;
        @XmlAttribute(name = "identifier")
        protected String identifier;
        @XmlAttribute(name = "id")
        protected Integer id;
        @XmlAttribute(name = "functionbitmask")
        protected Integer functionbitmask;
        @XmlAttribute(name = "fwversion")
        protected String fwversion;
        @XmlAttribute(name = "manufacturer")
        protected String manufacturer;
        @XmlAttribute(name = "productname")
        protected String productname;

        /**
         * Gets the value of the present property.
         * 
         */
        public boolean isPresent() {
            return present;
        }

        /**
         * Sets the value of the present property.
         * 
         */
        public void setPresent(boolean value) {
            this.present = value;
        }

        /**
         * Gets the value of the name property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getName() {
            return name;
        }

        /**
         * Sets the value of the name property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setName(String value) {
            this.name = value;
        }

        /**
         * Gets the value of the switch property.
         * 
         * @return
         *     possible object is
         *     {@link Switch }
         *     
         */
        public Switch getSwitch() {
            return _switch;
        }

        /**
         * Sets the value of the switch property.
         * 
         * @param value
         *     allowed object is
         *     {@link Switch }
         *     
         */
        public void setSwitch(Switch value) {
            this._switch = value;
        }

        /**
         * Gets the value of the powermeter property.
         * 
         * @return
         *     possible object is
         *     {@link Devicelist.Device.Powermeter }
         *     
         */
        public Devicelist.Device.Powermeter getPowermeter() {
            return powermeter;
        }

        /**
         * Sets the value of the powermeter property.
         * 
         * @param value
         *     allowed object is
         *     {@link Devicelist.Device.Powermeter }
         *     
         */
        public void setPowermeter(Devicelist.Device.Powermeter value) {
            this.powermeter = value;
        }

        /**
         * Gets the value of the temperature property.
         * 
         * @return
         *     possible object is
         *     {@link Devicelist.Device.Temperature }
         *     
         */
        public Devicelist.Device.Temperature getTemperature() {
            return temperature;
        }

        /**
         * Sets the value of the temperature property.
         * 
         * @param value
         *     allowed object is
         *     {@link Devicelist.Device.Temperature }
         *     
         */
        public void setTemperature(Devicelist.Device.Temperature value) {
            this.temperature = value;
        }

        /**
         * Gets the value of the identifier property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getIdentifier() {
            return identifier;
        }

        /**
         * Sets the value of the identifier property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setIdentifier(String value) {
            this.identifier = value;
        }

        /**
         * Gets the value of the id property.
         * 
         * @return
         *     possible object is
         *     {@link Integer }
         *     
         */
        public Integer getId() {
            return id;
        }

        /**
         * Sets the value of the id property.
         * 
         * @param value
         *     allowed object is
         *     {@link Integer }
         *     
         */
        public void setId(Integer value) {
            this.id = value;
        }

        /**
         * Gets the value of the functionbitmask property.
         * 
         * @return
         *     possible object is
         *     {@link Integer }
         *     
         */
        public Integer getFunctionbitmask() {
            return functionbitmask;
        }

        /**
         * Sets the value of the functionbitmask property.
         * 
         * @param value
         *     allowed object is
         *     {@link Integer }
         *     
         */
        public void setFunctionbitmask(Integer value) {
            this.functionbitmask = value;
        }

        /**
         * Gets the value of the fwversion property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getFwversion() {
            return fwversion;
        }

        /**
         * Sets the value of the fwversion property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setFwversion(String value) {
            this.fwversion = value;
        }

        /**
         * Gets the value of the manufacturer property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getManufacturer() {
            return manufacturer;
        }

        /**
         * Sets the value of the manufacturer property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setManufacturer(String value) {
            this.manufacturer = value;
        }

        /**
         * Gets the value of the productname property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getProductname() {
            return productname;
        }

        /**
         * Sets the value of the productname property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setProductname(String value) {
            this.productname = value;
        }


        /**
         * <p>Java class for anonymous complex type.
         * 
         * <p>The following schema fragment specifies the expected content contained within this class.
         * 
         * <pre>
         * &lt;complexType>
         *   &lt;complexContent>
         *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *       &lt;sequence>
         *         &lt;element name="power" type="{http://www.w3.org/2001/XMLSchema}int"/>
         *         &lt;element name="energy" type="{http://www.w3.org/2001/XMLSchema}int"/>
         *       &lt;/sequence>
         *     &lt;/restriction>
         *   &lt;/complexContent>
         * &lt;/complexType>
         * </pre>
         * 
         * 
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {
            "power",
            "energy"
        })
        public static class Powermeter {

            protected int power;
            protected int energy;

            /**
             * Gets the value of the power property.
             * 
             */
            public int getPower() {
                return power;
            }

            /**
             * Sets the value of the power property.
             * 
             */
            public void setPower(int value) {
                this.power = value;
            }

            /**
             * Gets the value of the energy property.
             * 
             */
            public int getEnergy() {
                return energy;
            }

            /**
             * Sets the value of the energy property.
             * 
             */
            public void setEnergy(int value) {
                this.energy = value;
            }

        }


        /**
         * <p>Java class for anonymous complex type.
         * 
         * <p>The following schema fragment specifies the expected content contained within this class.
         * 
         * <pre>
         * &lt;complexType>
         *   &lt;complexContent>
         *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *       &lt;sequence>
         *         &lt;element name="celsius" type="{http://www.w3.org/2001/XMLSchema}int"/>
         *         &lt;element name="offset" type="{http://www.w3.org/2001/XMLSchema}int"/>
         *       &lt;/sequence>
         *     &lt;/restriction>
         *   &lt;/complexContent>
         * &lt;/complexType>
         * </pre>
         * 
         * 
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {
            "celsius",
            "offset"
        })
        public static class Temperature {

            protected int celsius;
            protected int offset;

            /**
             * Gets the value of the celsius property.
             * 
             */
            public int getCelsius() {
                return celsius;
            }

            /**
             * Sets the value of the celsius property.
             * 
             */
            public void setCelsius(int value) {
                this.celsius = value;
            }

            /**
             * Gets the value of the offset property.
             * 
             */
            public int getOffset() {
                return offset;
            }

            /**
             * Sets the value of the offset property.
             * 
             */
            public void setOffset(int value) {
                this.offset = value;
            }

        }

    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="present" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
     *         &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element ref="{}switch" minOccurs="0"/>
     *         &lt;element name="groupinfo">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;sequence>
     *                   &lt;element name="masterdeviceid" type="{http://www.w3.org/2001/XMLSchema}int"/>
     *                   &lt;element name="members" type="{http://www.w3.org/2001/XMLSchema}int"/>
     *                 &lt;/sequence>
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *       &lt;/sequence>
     *       &lt;attribute name="identifier" type="{http://www.w3.org/2001/XMLSchema}string" />
     *       &lt;attribute name="id" type="{http://www.w3.org/2001/XMLSchema}int" />
     *       &lt;attribute name="functionbitmask" type="{http://www.w3.org/2001/XMLSchema}int" />
     *       &lt;attribute name="fwversion" type="{http://www.w3.org/2001/XMLSchema}string" />
     *       &lt;attribute name="manufacturer" type="{http://www.w3.org/2001/XMLSchema}string" />
     *       &lt;attribute name="productname" type="{http://www.w3.org/2001/XMLSchema}string" />
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "present",
        "name",
        "_switch",
        "groupinfo"
    })
    public static class Group {

        protected boolean present;
        @XmlElement(required = true)
        protected String name;
        @XmlElement(name = "switch")
        protected Switch _switch;
        @XmlElement(required = true)
        protected Devicelist.Group.Groupinfo groupinfo;
        @XmlAttribute(name = "identifier")
        protected String identifier;
        @XmlAttribute(name = "id")
        protected Integer id;
        @XmlAttribute(name = "functionbitmask")
        protected Integer functionbitmask;
        @XmlAttribute(name = "fwversion")
        protected String fwversion;
        @XmlAttribute(name = "manufacturer")
        protected String manufacturer;
        @XmlAttribute(name = "productname")
        protected String productname;

        /**
         * Gets the value of the present property.
         * 
         */
        public boolean isPresent() {
            return present;
        }

        /**
         * Sets the value of the present property.
         * 
         */
        public void setPresent(boolean value) {
            this.present = value;
        }

        /**
         * Gets the value of the name property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getName() {
            return name;
        }

        /**
         * Sets the value of the name property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setName(String value) {
            this.name = value;
        }

        /**
         * Gets the value of the switch property.
         * 
         * @return
         *     possible object is
         *     {@link Switch }
         *     
         */
        public Switch getSwitch() {
            return _switch;
        }

        /**
         * Sets the value of the switch property.
         * 
         * @param value
         *     allowed object is
         *     {@link Switch }
         *     
         */
        public void setSwitch(Switch value) {
            this._switch = value;
        }

        /**
         * Gets the value of the groupinfo property.
         * 
         * @return
         *     possible object is
         *     {@link Devicelist.Group.Groupinfo }
         *     
         */
        public Devicelist.Group.Groupinfo getGroupinfo() {
            return groupinfo;
        }

        /**
         * Sets the value of the groupinfo property.
         * 
         * @param value
         *     allowed object is
         *     {@link Devicelist.Group.Groupinfo }
         *     
         */
        public void setGroupinfo(Devicelist.Group.Groupinfo value) {
            this.groupinfo = value;
        }

        /**
         * Gets the value of the identifier property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getIdentifier() {
            return identifier;
        }

        /**
         * Sets the value of the identifier property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setIdentifier(String value) {
            this.identifier = value;
        }

        /**
         * Gets the value of the id property.
         * 
         * @return
         *     possible object is
         *     {@link Integer }
         *     
         */
        public Integer getId() {
            return id;
        }

        /**
         * Sets the value of the id property.
         * 
         * @param value
         *     allowed object is
         *     {@link Integer }
         *     
         */
        public void setId(Integer value) {
            this.id = value;
        }

        /**
         * Gets the value of the functionbitmask property.
         * 
         * @return
         *     possible object is
         *     {@link Integer }
         *     
         */
        public Integer getFunctionbitmask() {
            return functionbitmask;
        }

        /**
         * Sets the value of the functionbitmask property.
         * 
         * @param value
         *     allowed object is
         *     {@link Integer }
         *     
         */
        public void setFunctionbitmask(Integer value) {
            this.functionbitmask = value;
        }

        /**
         * Gets the value of the fwversion property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getFwversion() {
            return fwversion;
        }

        /**
         * Sets the value of the fwversion property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setFwversion(String value) {
            this.fwversion = value;
        }

        /**
         * Gets the value of the manufacturer property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getManufacturer() {
            return manufacturer;
        }

        /**
         * Sets the value of the manufacturer property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setManufacturer(String value) {
            this.manufacturer = value;
        }

        /**
         * Gets the value of the productname property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getProductname() {
            return productname;
        }

        /**
         * Sets the value of the productname property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setProductname(String value) {
            this.productname = value;
        }


        /**
         * <p>Java class for anonymous complex type.
         * 
         * <p>The following schema fragment specifies the expected content contained within this class.
         * 
         * <pre>
         * &lt;complexType>
         *   &lt;complexContent>
         *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *       &lt;sequence>
         *         &lt;element name="masterdeviceid" type="{http://www.w3.org/2001/XMLSchema}int"/>
         *         &lt;element name="members" type="{http://www.w3.org/2001/XMLSchema}int"/>
         *       &lt;/sequence>
         *     &lt;/restriction>
         *   &lt;/complexContent>
         * &lt;/complexType>
         * </pre>
         * 
         * 
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {
            "masterdeviceid",
            "members"
        })
        public static class Groupinfo {

            protected int masterdeviceid;
            protected int members;

            /**
             * Gets the value of the masterdeviceid property.
             * 
             */
            public int getMasterdeviceid() {
                return masterdeviceid;
            }

            /**
             * Sets the value of the masterdeviceid property.
             * 
             */
            public void setMasterdeviceid(int value) {
                this.masterdeviceid = value;
            }

            /**
             * Gets the value of the members property.
             * 
             */
            public int getMembers() {
                return members;
            }

            /**
             * Sets the value of the members property.
             * 
             */
            public void setMembers(int value) {
                this.members = value;
            }

        }

    }

}
