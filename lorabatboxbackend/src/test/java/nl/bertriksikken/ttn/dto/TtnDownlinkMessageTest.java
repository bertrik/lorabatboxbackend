package nl.bertriksikken.ttn.dto;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.time.Instant;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public final class TtnDownlinkMessageTest {

    @Test
    public void test() throws JsonProcessingException {
        int time = (int) Instant.now().getEpochSecond();
        
        ByteBuffer bb = ByteBuffer.allocate(4).order(ByteOrder.BIG_ENDIAN).putInt(time);
        byte[] rawPayload = bb.array();
        TtnDownlinkMessage message = new TtnDownlinkMessage(123, false, rawPayload);
        
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(message);
        System.out.println(json);
    }
    
}
