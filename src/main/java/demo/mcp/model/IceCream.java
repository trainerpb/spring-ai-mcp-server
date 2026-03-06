package demo.mcp.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IceCream {
    private String brand;
    private Flavor flavour;
    private Size  size;
    private int qty;


}
