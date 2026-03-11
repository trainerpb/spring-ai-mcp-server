package demo.mcp.service;

import org.springaicommunity.mcp.annotation.McpTool;
import org.springaicommunity.mcp.annotation.McpToolParam;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Component
public class ArduinoControllerTool {

    @McpTool(description = """
            Use this tool to turn light on or off.
            Example - turn light off.
            """)
    public String control(@McpToolParam(description = "Turn off or on")String operation){
       var rt=new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        // 2. Add form parameters to a MultiValueMap
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("topic", "house/kitchen/command");
        map.add("message", operation.toUpperCase());

        // 3. Create the HttpEntity
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);
        var result=rt.postForEntity("http://localhost:8080/send", request, String.class);
        if (result.getStatusCode().is2xxSuccessful()){
            return result.getBody();
        }else{
            return result.getStatusCode().toString();
        }
    }

}
