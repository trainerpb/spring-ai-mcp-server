package demo.mcp.service;

import demo.mcp.model.Flavor;
import demo.mcp.model.IceCream;
import demo.mcp.model.Size;
import io.modelcontextprotocol.client.McpSyncClient;
import io.modelcontextprotocol.server.McpSyncServerExchange;
import io.modelcontextprotocol.spec.McpSchema;
import lombok.extern.slf4j.Slf4j;
import org.springaicommunity.mcp.annotation.McpTool;
import org.springaicommunity.mcp.annotation.McpToolParam;
import org.springaicommunity.mcp.context.McpSyncRequestContext;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class IceCreamService {


    @McpTool(description = "This tool is used to create ice cream")
    public IceCream createIceCream(@McpToolParam(description = "Brand of the ice cream. Example- Qwality, Vadilal, Amul, Rollicks, Baskin", required = false) String brandName,
                                   @McpToolParam(description = "Size of icecream. example- small, medium, large",required = false) String strSize,
                                   @McpToolParam(description = "Flavour of the ice cream. E.g. vanilla, chocolate")

                                       String  strFlavor,
                                   @McpToolParam(description="quantity of ice cream in numbers",required = false) Integer quantity,
                                   McpSyncServerExchange exchange
                                   ) {

        Flavor flavor = null;
        try {
            flavor = Flavor.valueOf(strFlavor);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException(e);
        }
        Size size = StringUtils.hasText(strSize)? Size.valueOf(strSize): Size.SMALL;
        brandName=!StringUtils.hasText(brandName)? brandName: "Qwality";



        var resultIceCream= IceCream.builder()
                .brand(brandName)
                .size(size)
                .flavour(flavor)
                .qty(null==quantity?1:quantity)
                .build();

        // Define the schema for the data you want to elicit
        Map<String, Object> schema = Map.of(
                "type", "object",
                "properties", Map.of(
                        "confirmOrder", Map.of(
                                "type", "string",
                                "description", "confirmation should be 'yes' or 'no' otherwise"
                        )
                ),
                "required", List.of("confirmOrder")
        );
        McpSchema.ElicitResult elicitResult = exchange.createElicitation(McpSchema.ElicitRequest.builder()
                .message("Confirm your order " + resultIceCream.toString())
                .requestedSchema(schema)
                .build());
        switch(elicitResult.action()){
            case ACCEPT -> {
                    log.info("Accepted elicitation :: {}" , elicitResult);
                    if("yes".equalsIgnoreCase(elicitResult.content().get("confirmOrder").toString())){
                        log.info("Elicitation accepted :: Order confirmed :: {}",resultIceCream.toString());
                        return resultIceCream;
                    }else{
                        log.warn("User rejected order :: {}" , resultIceCream.toString());
                    }
            }
            case DECLINE -> {
                log.warn("Decline elicitation :: {}" , elicitResult);
            }
        }
        return null;

    }
}
