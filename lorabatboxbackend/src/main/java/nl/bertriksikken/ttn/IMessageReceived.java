package nl.bertriksikken.ttn;

import nl.bertriksikken.ttn.dto.TtnUplinkMessage;

/**
 * Interface of the callback from the MQTT listener.
 */
public interface IMessageReceived {

    /**
     * Indicates that a message was received.
     * 
     * @param topic   the topic
     * @param uplink the message
     */
    void messageReceived(String topic, TtnUplinkMessage uplink) throws Exception;

}
