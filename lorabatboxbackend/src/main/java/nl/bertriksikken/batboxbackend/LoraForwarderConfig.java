package nl.bertriksikken.batboxbackend;

import com.fasterxml.jackson.annotation.JsonProperty;

import nl.bertriksikken.ttn.TtnConfig;

/**
 * Configuration class.
 */
public final class LoraForwarderConfig {

    @JsonProperty("ttn")
    private TtnConfig ttnConfig = new TtnConfig();

    public TtnConfig getTtnConfig() {
        return ttnConfig;
    }

}