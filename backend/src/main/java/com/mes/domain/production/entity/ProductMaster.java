package com.mes.domain.production.entity;

import com.mes.domain.common.BaseEntity;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class ProductMaster extends BaseEntity {

    private UUID productId;
    private String skuCode;
    private String productName;
    private String category;
    private String storageCondition;

    public ProductMaster(UUID productId, String skuCode, String productName, String category,
                         String storageCondition) {
        super(productId, java.time.LocalDateTime.now());
        this.productId = productId;
        this.skuCode = skuCode;
        this.productName = productName;
        this.category = category;
        this.storageCondition = storageCondition;
    }

    @Override
    public ProductMaster copy(UUID newId) {
        return new ProductMaster(
                newId, skuCode, productName, category, storageCondition
        );
    }
}
