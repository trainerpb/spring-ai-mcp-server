package demo.mcp.service;

import demo.mcp.model.*;
import io.modelcontextprotocol.server.McpSyncServerExchange;
import io.modelcontextprotocol.spec.McpSchema;
import lombok.extern.slf4j.Slf4j;
import org.springaicommunity.mcp.annotation.McpTool;
import org.springaicommunity.mcp.annotation.McpToolParam;
import org.springaicommunity.mcp.context.McpSyncRequestContext;
import org.springaicommunity.mcp.context.StructuredElicitResult;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class IceCreamService {


    @McpTool(description = "This tool is used to create ice cream")
    public IceCreamOrderResult createIceCream(@McpToolParam(description = "Brand of the ice cream. Example- Qwality, Vadilal, Amul, Rollicks, Baskin", required = false) String brandName,
                                              @McpToolParam(description = "Size of icecream. example- small, medium, large",required = false) String strSize,
                                              @McpToolParam(description = "Flavour of the ice cream. E.g. vanilla, chocolate")

                                       String  strFlavor,
                                              @McpToolParam(description="quantity of ice cream in numbers",required = false) Integer quantity,
                                              McpSyncRequestContext requestContext
                                   ) {

        System.out.println("IceCreamService.createIceCream :: Received request to create ice cream with brandName: " + brandName + ", size: " + strSize + ", flavor: " + strFlavor);
        log.info("Received request to create ice cream with brandName: {}, size: {}, flavor", brandName,  strFlavor);

        Flavor flavor = null;
        try {
            flavor = Flavor.valueOf(strFlavor.toUpperCase());
            log.info("Parsed flavor: {}", flavor);
        } catch (IllegalArgumentException e) {
            log.error("Invalid flavor provided: {}. Error: {}", strFlavor, e.getMessage());
            throw new RuntimeException(e);
        }
        Size size = StringUtils.hasText(strSize)? Size.valueOf(strSize.toUpperCase()): Size.SMALL;
        log.info("Parsed size: {}", size);
        brandName=StringUtils.hasText(brandName)? brandName: "Qwality";
        log.info("Using brand name: {}", brandName);



        var resultIceCream= IceCream.builder()
                .brand(brandName)
                .size(size)
                .flavour(flavor)
                .qty(null==quantity?1:quantity)
                .build();
        log.info("Created ice cream: {}", resultIceCream);
        // Define the schema for the data you want to elicit

        log.info("Eliciting user confirmation for the order: {}", resultIceCream);
       StructuredElicitResult<OrderConfirmElicitRequest> elicitResult = requestContext
               .elicit(p-> p.message("Please confirm your order (yes/no): "+resultIceCream),OrderConfirmElicitRequest.class);

        switch(elicitResult.action()){
            case ACCEPT -> {
                    log.info("Accepted elicitation :: {}" , elicitResult);
                    if("yes".equalsIgnoreCase(elicitResult.structuredContent().getConfirmOrder())){
                        log.info("Elicitation accepted :: Order confirmed :: {}",resultIceCream.toString());
                        return IceCreamOrderResult.builder()
                                .iceCream(resultIceCream)
                                .success(true)
                                .message("Order confirmed :: "+resultIceCream.toString())
                                .build();
                    }else{
                        log.warn("User rejected order :: {}" , resultIceCream.toString());

                    }
            }
            case DECLINE -> {
                log.warn("Decline elicitation :: {}" , elicitResult);
            }
        }
        return IceCreamOrderResult.builder()
                .success(false)
                .message("User rejected to confirm the order :: "+resultIceCream.toString())
                .iceCream(null)
                .build();

    }
}
