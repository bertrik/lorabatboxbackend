package nl.bertriksikken.ttn;

import com.fasterxml.jackson.annotation.JsonProperty;

public final class TtnAppConfig {

    @JsonProperty("name")
    private String name = "particulatematter";

    @JsonProperty("key")
    private String key = "ttn-account-v2.cNaB2zO-nRiXaCUYmSAugzm-BaG_ZSHbEc5KgHNQFsk";

    public String getName() {
        return name;
    }

    public String getKey() {
        return key;
    }

}
