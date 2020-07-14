package nl.bertriksikken.ttn;

import com.fasterxml.jackson.annotation.JsonProperty;

public final class TtnAppConfig {

    @JsonProperty("name")
    private String name = "batbox";

    @JsonProperty("key")
    private String key = "ttn-account-v2.zGpGyRv-mOgzxAAAdn6vR_dBx9uDNecsvrYGyPQIHTo";

    public String getName() {
        return name;
    }

    public String getKey() {
        return key;
    }

}
