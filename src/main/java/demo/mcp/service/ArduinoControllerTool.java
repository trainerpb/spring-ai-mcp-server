package demo.mcp.service;

import org.springaicommunity.mcp.annotation.McpTool;
import org.springaicommunity.mcp.annotation.McpToolParam;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;

@Component
public class ArduinoControllerTool {

    @McpTool(description = """
            Use this tool to turn light on or off.
            Example - turn light off.
            """)
    public String control(@McpToolParam(description = "Turn off or on")String operation){
       var rt=new RestTemplate();
        ResponseEntity<String> result = rt.getForEntity(String.format("http://192.168.4.1/?mode=%s", operation), String.class);
        if (result.getStatusCode().is2xxSuccessful()){
            return result.getBody();
        }else{
            return result.getStatusCode().toString();
        }
    }

}
