package nl.bertriksikken.ttn;

import java.nio.charset.Charset;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import nl.bertriksikken.ttn.dto.TtnDownlinkMessage;
import nl.bertriksikken.ttn.dto.TtnUplinkMessage;

/**
 * Listener process for receiving data from MQTT.
 * 
 * Decouples the MQTT callback from listener using a single thread executor.
 */
public final class MqttListener {

    private static final Logger LOG = LoggerFactory.getLogger(MqttListener.class);
    private static final long DISCONNECT_TIMEOUT_MS = 3000;

    private static final ObjectMapper mapper = new ObjectMapper();
    private final MqttClient mqttClient;
    private final MqttConnectOptions options;

    /**
     * Constructor.
     * 
     * @param callback the interface for indicating a received message.
     * @param url      the URL of the MQTT server
     * @param appId    the name of the TTN application
     * @param appKey   the key of the TTN application
     */
    public MqttListener(String url, String appId, String appKey) {
        LOG.info("Creating client for MQTT server '{}' for app '{}'", url, appId);
        try {
            this.mqttClient = new MqttClient(url, MqttClient.generateClientId(), new MemoryPersistence());
        } catch (MqttException e) {
            throw new IllegalArgumentException(e);
        }
        // create connect options
        options = new MqttConnectOptions();
        options.setUserName(appId);
        options.setPassword(appKey.toCharArray());
        options.setAutomaticReconnect(true);
    }

    /**
     * @param callback the uplink callback
     */
    public void setUplinkCallback(IMessageReceived callback) {
        mqttClient.setCallback(new MqttCallbackHandler(mqttClient, "+/devices/+/up", callback));
    }
    
    /**
     * Starts this module.
     * 
     * @throws MqttException in case something went wrong with MQTT
     */
    public void start() throws MqttException {
        LOG.info("Starting MQTT listener '{}'", options.getUserName());

        mqttClient.connect(options);
    }

    public void stop() {
        LOG.info("Stopping MQTT listener '{}'", options.getUserName());
        try {
            mqttClient.disconnect(DISCONNECT_TIMEOUT_MS);
        } catch (MqttException e) {
            // don't care, just log
            LOG.warn("Caught exception on disconnect: {}", e.getMessage());
        }
    }

    public void sendDownlink(String appId, String devId, TtnDownlinkMessage downlink) throws MqttException, JsonProcessingException {
        String message = mapper.writeValueAsString(downlink);
        
        String topic = appId + "/devices/" + devId + "/down";
        MqttMessage mqttMessage = new MqttMessage(message.getBytes(Charset.forName("UTF-8")));
        LOG.info("Sending downlink to {}: {}", topic, message);
        mqttClient.publish(topic, mqttMessage);
    }
    
    /**
     * MQTT callback handler, (re-)subscribes to the topic and forwards incoming
     * messages.
     */
    private static final class MqttCallbackHandler implements MqttCallbackExtended {

        private final MqttClient client;
        private final String topic;
        private final IMessageReceived listener;

        private MqttCallbackHandler(MqttClient client, String topic, IMessageReceived listener) {
            this.client = client;
            this.topic = topic;
            this.listener = listener;
        }

        @Override
        public void connectionLost(Throwable cause) {
            LOG.warn("Connection lost: {}", cause.getMessage());
        }

        @Override
        public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
            LOG.info("Message arrived on topic '{}'", topic);

            // notify our listener, in an exception safe manner
            try {
                TtnUplinkMessage uplink = mapper.readValue(mqttMessage.getPayload(), TtnUplinkMessage.class);
                listener.messageReceived(topic, uplink);
            } catch (Exception e) {
                LOG.trace("Caught exception", e);
                LOG.error("Caught exception in MQTT listener: {}", e.getMessage());
            }
        }

        @Override
        public void deliveryComplete(IMqttDeliveryToken token) {
            // nothing to do
        }

        @Override
        public void connectComplete(boolean reconnect, String serverURI) {
            LOG.info("Connected to '{}', subscribing to MQTT topic '{}'", serverURI, topic);
            try {
                client.subscribe(topic);
            } catch (MqttException e) {
                LOG.error("Caught exception while subscribing!");
            }
        }
    }

}
