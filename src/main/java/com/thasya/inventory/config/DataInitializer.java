package com.thasya.inventory.config;

import com.thasya.inventory.entity.Item;
import com.thasya.inventory.entity.ItemVariant;
import com.thasya.inventory.entity.Stock;
import com.thasya.inventory.entity.VariantStock;
import com.thasya.inventory.repository.ItemRepository;
import com.thasya.inventory.repository.ItemVariantRepository;
import com.thasya.inventory.repository.StockRepository;
import com.thasya.inventory.repository.VariantStockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * Initialize sample data for testing
 */
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final ItemRepository itemRepository;
    private final ItemVariantRepository variantRepository;
    private final StockRepository stockRepository;
    private final VariantStockRepository variantStockRepository;

    @Override
    public void run(String... args) throws Exception {
        initializeData();
    }

    private void initializeData() {
        // Create Item 1: T-Shirt
        Item tshirt = Item.builder()
                .name("Classic T-Shirt")
                .sku("TSHIRT-001")
                .description("A comfortable cotton t-shirt")
                .basePrice(new BigDecimal("19.99"))
                .build();
        tshirt = itemRepository.save(tshirt);

        // Create Stock for T-Shirt
        Stock tshirtStock = Stock.builder()
                .quantity(100)
                .reserved(0)
                .reorderLevel(20)
                .item(tshirt)
                .build();
        stockRepository.save(tshirtStock);

        // Create variants for T-Shirt
        ItemVariant tshirtRed = ItemVariant.builder()
                .name("Red T-Shirt")
                .color("Red")
                .size("M")
                .description("Classic T-Shirt in Red - Medium")
                .price(new BigDecimal("19.99"))
                .item(tshirt)
                .build();
        tshirtRed = variantRepository.save(tshirtRed);

        VariantStock tshirtRedStock = VariantStock.builder()
                .quantity(50)
                .reserved(0)
                .reorderLevel(10)
                .variant(tshirtRed)
                .build();
        variantStockRepository.save(tshirtRedStock);

        ItemVariant tshirtBlue = ItemVariant.builder()
                .name("Blue T-Shirt")
                .color("Blue")
                .size("L")
                .description("Classic T-Shirt in Blue - Large")
                .price(new BigDecimal("19.99"))
                .item(tshirt)
                .build();
        tshirtBlue = variantRepository.save(tshirtBlue);

        VariantStock tshirtBlueStock = VariantStock.builder()
                .quantity(30)
                .reserved(0)
                .reorderLevel(10)
                .variant(tshirtBlue)
                .build();
        variantStockRepository.save(tshirtBlueStock);

        // Create Item 2: Jeans
        Item jeans = Item.builder()
                .name("Premium Jeans")
                .sku("JEANS-001")
                .description("High-quality denim jeans")
                .basePrice(new BigDecimal("59.99"))
                .build();
        jeans = itemRepository.save(jeans);

        // Create Stock for Jeans
        Stock jeansStock = Stock.builder()
                .quantity(50)
                .reserved(0)
                .reorderLevel(15)
                .item(jeans)
                .build();
        stockRepository.save(jeansStock);

        // Create variants for Jeans
        ItemVariant jeansBlack32 = ItemVariant.builder()
                .name("Black Jeans - 32")
                .color("Black")
                .size("32")
                .description("Premium Jeans in Black - Size 32")
                .price(new BigDecimal("59.99"))
                .item(jeans)
                .build();
        jeansBlack32 = variantRepository.save(jeansBlack32);

        VariantStock jeansBlack32Stock = VariantStock.builder()
                .quantity(20)
                .reserved(0)
                .reorderLevel(5)
                .variant(jeansBlack32)
                .build();
        variantStockRepository.save(jeansBlack32Stock);

        ItemVariant jeansBlue34 = ItemVariant.builder()
                .name("Blue Jeans - 34")
                .color("Blue")
                .size("34")
                .description("Premium Jeans in Blue - Size 34")
                .price(new BigDecimal("59.99"))
                .item(jeans)
                .build();
        jeansBlue34 = variantRepository.save(jeansBlue34);

        VariantStock jeansBlue34Stock = VariantStock.builder()
                .quantity(5)
                .reserved(0)
                .reorderLevel(5)
                .variant(jeansBlue34)
                .build();
        variantStockRepository.save(jeansBlue34Stock);

        // Create Item 3: Shoes
        Item shoes = Item.builder()
                .name("Running Shoes")
                .sku("SHOES-001")
                .description("Comfortable running shoes")
                .basePrice(new BigDecimal("89.99"))
                .build();
        shoes = itemRepository.save(shoes);

        // Create Stock for Shoes
        Stock shoesStock = Stock.builder()
                .quantity(75)
                .reserved(0)
                .reorderLevel(25)
                .item(shoes)
                .build();
        stockRepository.save(shoesStock);

        // Create variants for Shoes
        ItemVariant shoesSize9 = ItemVariant.builder()
                .name("Running Shoes - Size 9")
                .color("Black/White")
                .size("9")
                .description("Running Shoes in Black/White - Size 9")
                .price(new BigDecimal("89.99"))
                .item(shoes)
                .build();
        shoesSize9 = variantRepository.save(shoesSize9);

        VariantStock shoesSize9Stock = VariantStock.builder()
                .quantity(40)
                .reserved(0)
                .reorderLevel(10)
                .variant(shoesSize9)
                .build();
        variantStockRepository.save(shoesSize9Stock);

        ItemVariant shoesSize10 = ItemVariant.builder()
                .name("Running Shoes - Size 10")
                .color("Black/White")
                .size("10")
                .description("Running Shoes in Black/White - Size 10")
                .price(new BigDecimal("89.99"))
                .item(shoes)
                .build();
        shoesSize10 = variantRepository.save(shoesSize10);

        VariantStock shoesSize10Stock = VariantStock.builder()
                .quantity(2)
                .reserved(0)
                .reorderLevel(10)
                .variant(shoesSize10)
                .build();
        variantStockRepository.save(shoesSize10Stock);
    }
}

