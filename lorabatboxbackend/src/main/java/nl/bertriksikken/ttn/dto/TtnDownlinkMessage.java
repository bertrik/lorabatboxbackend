package nl.bertriksikken.ttn.dto;

import java.util.Locale;

import com.fasterxml.jackson.annotation.JsonProperty;

public final class TtnDownlinkMessage {

    @JsonProperty("port")
    int port;

    @JsonProperty("confirmed")
    boolean confirmed;

    @JsonProperty("payload_raw")
    byte[] rawPayload;

    TtnDownlinkMessage(int port, boolean confirmed, byte[] rawPayload) {
        this.port = port;
        this.confirmed = confirmed;
        this.rawPayload = rawPayload;
    }

    @Override
    public String toString() {
        return String.format(Locale.ROOT, "[port=%d,confirmed=%s]", port, confirmed);
    }
    
}
