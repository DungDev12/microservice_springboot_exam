package com.shop.inventoryservice.common.enums;

public enum TransactionType {
    AWAITING_STOCK_ARRIVAL, // Chờ nhập hàng
    IMPORT_STOCK, // Nhập hàng
    EXPORT_SALE, // Xuất hàng để bán
    ADJUSTMENT_PLUS,
    ADJUSTMENT_MINUS,
    CUSTOMER_RETURN,
    CHANGE_PURCHASE,
    CHANGE_SALE,
    CHANGE_INFORMATION
}
