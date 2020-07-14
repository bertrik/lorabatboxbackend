package nl.bertriksikken.ttn.dto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Stream;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public final class TtnUplinkMessage {

    @JsonProperty("app_id")
    String appId;

    @JsonProperty("dev_id")
    String devId;

    @JsonProperty("hardware_serial")
    String hardwareSerial;

    @JsonProperty("port")
    int port;

    @JsonProperty("counter")
    int counter;

    @JsonProperty("payload_raw")
    byte[] rawPayload;

    @JsonProperty("payload_fields")
    Map<String, Object> payloadFields;
    
    public String getAppId() {
        return appId;
    }

    public String getDevId() {
        return devId;
    }

    public String getHardwareSerial() {
        return hardwareSerial;
    }

    public int getPort() {
        return port;
    }

    public int getCounter() {
        return counter;
    }

    public byte[] getRawPayload() {
        return (rawPayload != null) ? rawPayload.clone() : null;
    }

    public Map<String, Object> getPayloadFields() {
    	return new HashMap<>(payloadFields);
    }
    
    @Override
    public String toString() {
        if (rawPayload != null) {
            List<String> hex = new ArrayList<>();
            for (byte b : rawPayload) {
                hex.add(String.format(Locale.ROOT, "%02X", b));
            }
            return String.join(" ", hex);
        }
        if (payloadFields != null) {
            return payloadFields.toString();
        }
        return "";
    }
}
