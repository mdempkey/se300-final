package com.se300.store;

import com.se300.store.data.DataManager;
import com.se300.store.model.*;
import com.se300.store.service.StoreService;
import org.junit.jupiter.api.*;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * End-to-End Integration Tests for the Smart Store Application
 * Coverage: ~70% - focuses on critical workflows and integration points
 *
 * @author Sergey L. Sundukovskiy
 * @version 1.0
 */
@DisplayName("End-to-End Smart Store Integration Tests")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class EndToEndSmartStoreTest {

    private static StoreService storeService;
    private static DataManager dataManager;

    @BeforeAll
    static void setUpAll() {
        dataManager = DataManager.getInstance();
        storeService = new StoreService();
    }

    @BeforeEach
    void setUp() {
        StoreService.clearAllMaps();
        dataManager.clear();
    }

    @Test
    @Order(1)
    @DisplayName("E2E: Complete store provisioning workflow")
    void testCompleteStoreWorkflow() throws StoreException {
        // Provision store
        Store store = storeService.provisionStore("store1", "Main Store", "123 Main St", null);
        assertNotNull(store);
        assertEquals("store1", store.getId());
        assertEquals("Main Store", store.getAddress());

        // Show store
        Store retrieved = storeService.showStore("store1", null);
        assertNotNull(retrieved);
        assertEquals("store1", retrieved.getId());
    }

    @Test
    @Order(2)
    @DisplayName("E2E: Store with aisles and shelves")
    void testStoreWithAislesAndShelves() throws StoreException {
        // Create store
        storeService.provisionStore("store1", "Test Store", "123 St", null);

        // Adding aisles
        Aisle aisle1 = storeService.provisionAisle("store1", "A1", "Groceries", "Food items", 
                AisleLocation.floor, null);
        assertNotNull(aisle1);
        assertEquals("A1", aisle1.getNumber());

        Aisle aisle2 = storeService.provisionAisle("store1", "A2", "Dairy", "Refrigerated", 
                AisleLocation.floor, null);
        assertNotNull(aisle2);

        // Adding shelves
        Shelf shelf1 = storeService.provisionShelf("store1", "A1", "S1", "Top Shelf", 
                ShelfLevel.high, "High shelf", Temperature.ambient, null);
        assertNotNull(shelf1);
        assertEquals("S1", shelf1.getId());

        Shelf shelf2 = storeService.provisionShelf("store1", "A2", "S2", "Cold Shelf", 
                ShelfLevel.medium, "Refrigerated shelf", Temperature.refrigerated, null);
        assertNotNull(shelf2);

        // Verify aisles
        Aisle retrievedAisle = storeService.showAisle("store1", "A1", null);
        assertNotNull(retrievedAisle);
        assertEquals(1, retrievedAisle.getShelfMap().size());
    }

    @Test
    @Order(3)
    @DisplayName("E2E: Product and inventory management")
    void testProductAndInventoryManagement() throws StoreException {
        // Setuping up store structure
        storeService.provisionStore("store1", "Test Store", "123 St", null);
        storeService.provisionAisle("store1", "A1", "Groceries", "Food", AisleLocation.floor, null);
        storeService.provisionShelf("store1", "A1", "S1", "Shelf", ShelfLevel.high, "Desc", 
                Temperature.frozen, null);

        // Creating products
        Product pizza = storeService.provisionProduct("prod1", "Pizza", "Frozen Pizza", "16oz", 
                "Frozen Food", 8.99, Temperature.frozen, null);
        assertNotNull(pizza);
        assertEquals("prod1", pizza.getId());
        assertEquals(8.99, pizza.getPrice());

        Product chips = storeService.provisionProduct("prod2", "Chips", "Potato Chips", "12oz", 
                "Snacks", 3.99, Temperature.ambient, null);
        assertNotNull(chips);

        // Adding inventory
        Inventory inv1 = storeService.provisionInventory("inv1", "store1", "A1", "S1", 
                100, 50, "prod1", InventoryType.standard, null);
        assertNotNull(inv1);
        assertEquals(50, inv1.getCount());
        assertEquals(100, inv1.getCapacity());

        // Updatiing inventory
        Inventory updated = storeService.updateInventory("inv1", 10, null);
        assertEquals(60, updated.getCount());

        // Showing inventory
        Inventory retrieved = storeService.showInventory("inv1", null);
        assertNotNull(retrieved);
        assertEquals(60, retrieved.getCount());
    }

    @Test
    @Order(4)
    @DisplayName("E2E: Customer shopping workflow with basket")
    void testCustomerShoppingWorkflow() throws StoreException {
        // Setup store
        storeService.provisionStore("store1", "Test Store", "123 St", null);
        storeService.provisionAisle("store1", "A1", "Aisle", "Desc", AisleLocation.floor, null);
        storeService.provisionShelf("store1", "A1", "S1", "Shelf", ShelfLevel.medium, "Desc", 
                Temperature.refrigerated, null);
        storeService.provisionProduct("prod1", "Pizza", "Frozen Pizza", "16oz", 
                "Frozen Food", 8.99, Temperature.frozen, null);
        storeService.provisionInventory("inv1", "store1", "A1", "S1", 100, 50, "prod1", 
                InventoryType.standard, null);

        // Create customer!!!
        Customer customer = storeService.provisionCustomer("cust1", "Martha", "Dempkey", 
                CustomerType.registered, "martha.dempkey@example.com", "456 Oak Ave", null);
        assertNotNull(customer);
        assertEquals("cust1", customer.getId());

        // Update the customer location
        Customer updated = storeService.updateCustomer("cust1", "store1", "A1", null);
        assertNotNull(updated.getStoreLocation());
        assertEquals("store1", updated.getStoreLocation().getStoreId());

        // Create & assign basket
        Basket basket = storeService.provisionBasket("basket1", null);
        assertNotNull(basket);

        Basket assigned = storeService.assignCustomerBasket("cust1", "basket1", null);
        assertNotNull(assigned);
        assertEquals("basket1", assigned.getId());

        // Adding items to basket
        Basket withItems = storeService.addBasketProduct("basket1", "prod1", 2, null);
        assertTrue(withItems.toString().contains("prod1"));

        // Verifying the inventory decreased
        Inventory inv = storeService.showInventory("inv1", null);
        assertEquals(48, inv.getCount());
    }

    @Test
    @Order(5)
    @DisplayName("E2E: Device management and events")
    void testDeviceManagementAndEvents() throws StoreException {
        // Setup
        storeService.provisionStore("store1", "Test Store", "123 St", null);
        storeService.provisionAisle("store1", "A1", "Aisle", "Desc", AisleLocation.floor, null);

        // Provision sensor
        Device camera = storeService.provisionDevice("cam1", "Camera1", "camera", "store1", "A1", null);
        assertNotNull(camera);
        assertEquals("cam1", camera.getId());
        assertTrue(camera instanceof Sensor);

        // Provision appliance
        Device robot = storeService.provisionDevice("robot1", "Robot1", "robot", "store1", "A1", null);
        assertNotNull(robot);
        assertTrue(robot instanceof Appliance);

        // Show devices
        Device retrieved = storeService.showDevice("cam1", null);
        assertNotNull(retrieved);

        // Raise event 
        assertDoesNotThrow(() -> storeService.raiseEvent("cam1", "customer_detected", null));

        // Issue command 
        assertDoesNotThrow(() -> storeService.issueCommand("robot1", "CLEAN_FLOOR", null));
    }

    @Test
    @Order(6)
    @DisplayName("E2E: Error handling - duplicate entities")
    void testErrorHandlingDuplicates() throws StoreException {
        storeService.provisionStore("store1", "Store", "Address", null);

        // double store 
        assertThrows(StoreException.class, () -> 
            storeService.provisionStore("store1", "Store2", "Address2", null));

        // double product
        storeService.provisionProduct("prod1", "Soda", "Cola 12-pack", "12pk", "Beverages", 5.99, 
                Temperature.ambient, null);
        assertThrows(StoreException.class, () -> 
            storeService.provisionProduct("prod1", "Product2", "Desc2", "Size2", "Cat2", 2.0, 
                Temperature.ambient, null));
    }

    @Test
    @Order(7)
    @DisplayName("E2E: Error handling - non-existent entities")
    void testErrorHandlingNonExistent() {
        // Non-existent store
        assertThrows(StoreException.class, () -> 
            storeService.showStore("nonexistent", null));

        // Non-existent product
        assertThrows(StoreException.class, () -> 
            storeService.showProduct("nonexistent", null));

        // Non-existent inventory
        assertThrows(StoreException.class, () -> 
            storeService.showInventory("nonexistent", null));
    }

    @Test
    @Order(8)
    @DisplayName("E2E: Temperature consistency validation")
    void testTemperatureConsistency() throws StoreException {
        storeService.provisionStore("store1", "Store", "Address", null);
        storeService.provisionAisle("store1", "A1", "Aisle", "Desc", AisleLocation.floor, null);
        storeService.provisionShelf("store1", "A1", "S1", "Shelf", ShelfLevel.high, "Desc", 
                Temperature.frozen, null);
        storeService.provisionProduct("prod1", "Product", "Desc", "Size", "Cat", 1.0, 
                Temperature.ambient, null);

        // This mismatch should raise
        assertThrows(StoreException.class, () -> 
            storeService.provisionInventory("inv1", "store1", "A1", "S1", 100, 50, "prod1", 
                InventoryType.standard, null));
    }

    @Test
    @Order(9)
    @DisplayName("E2E: Basket operations - remove and clear")
    void testBasketOperations() throws StoreException {
        // Setup store and product
        storeService.provisionStore("store1", "Store", "Address", null);
        storeService.provisionAisle("store1", "A1", "Aisle", "Desc", AisleLocation.floor, null);
        storeService.provisionShelf("store1", "A1", "S1", "Shelf", ShelfLevel.medium, "Desc", 
                Temperature.ambient, null);
        storeService.provisionProduct("prod1", "Product", "Desc", "Size", "Cat", 1.0, 
                Temperature.ambient, null);
        storeService.provisionInventory("inv1", "store1", "A1", "S1", 100, 50, "prod1", 
                InventoryType.standard, null);
        storeService.provisionCustomer("cust1", "Kody", "Wong", CustomerType.registered, 
                "kody.wong@example.com", "789 Pine Rd", null);
        storeService.updateCustomer("cust1", "store1", "A1", null);
        storeService.provisionBasket("basket1", null);
        storeService.assignCustomerBasket("cust1", "basket1", null);

        // Add items
        storeService.addBasketProduct("basket1", "prod1", 5, null);
        Inventory inv = storeService.showInventory("inv1", null);
        assertEquals(45, inv.getCount());

        // Removeing itms
        storeService.removeBasketProduct("basket1", "prod1", 2, null);
        inv = storeService.showInventory("inv1", null);
        assertEquals(47, inv.getCount());

        // Show basket
        Basket basket = storeService.showBasket("basket1", null);
        assertTrue(basket.toString().contains("prod1"));
    }

    @Test
    @Order(10)
    @DisplayName("E2E: Multiple customers in same store")
    void testMultipleCustomers() throws StoreException {
        storeService.provisionStore("store1", "Store", "Address", null);
        storeService.provisionAisle("store1", "A1", "Aisle", "Desc", AisleLocation.floor, null);

        Customer cust1 = storeService.provisionCustomer("cust1", "Kody", "Wong", 
                CustomerType.registered, "kody.wong@example.com", "789 Pine Rd", null);
        Customer cust2 = storeService.provisionCustomer("cust2", "Nathan", "Watkins", 
                CustomerType.registered, "nathan.watkins@example.com", "321 Elm Blvd", null);
        Customer cust3 = storeService.provisionCustomer("cust3", "Martha", "Dempkey", 
                CustomerType.registered, "martha.dempkey@example.com", "654 Maple Dr", null);

        assertNotNull(cust1);
        assertNotNull(cust2);
        assertNotNull(cust3);

        // Updating the  locations
        storeService.updateCustomer("cust1", "store1", "A1", null);
        storeService.updateCustomer("cust2", "store1", "A1", null);
        storeService.updateCustomer("cust3", "store1", "A1", null);

        // Verifying all the costumers 
        Customer retrieved1 = storeService.showCustomer("cust1", null);
        Customer retrieved2 = storeService.showCustomer("cust2", null);
        Customer retrieved3 = storeService.showCustomer("cust3", null);

        assertNotNull(retrieved1.getStoreLocation());
        assertNotNull(retrieved2.getStoreLocation());
        assertNotNull(retrieved3.getStoreLocation());
    }

    @Test
    @Order(11)
    @DisplayName("E2E: Shelf level uniqueness validation")
    void testShelfLevelUniqueness() throws StoreException {
        storeService.provisionStore("store1", "Store", "Address", null);
        storeService.provisionAisle("store1", "A1", "Aisle", "Desc", AisleLocation.floor, null);
        
        // adding the first shelf
        storeService.provisionShelf("store1", "A1", "S1", "Shelf1", ShelfLevel.high, "Desc", 
                Temperature.ambient, null);

        // double leveles 
        assertThrows(StoreException.class, () -> 
            storeService.provisionShelf("store1", "A1", "S2", "Shelf2", ShelfLevel.high, "Desc", 
                Temperature.ambient, null));

        // doing different leveles 
        assertDoesNotThrow(() -> 
            storeService.provisionShelf("store1", "A1", "S3", "Shelf3", ShelfLevel.medium, "Desc", 
                Temperature.ambient, null));
    }

    @Test
    @Order(12)
    @DisplayName("E2E: Guest customer basket restrictions")
    void testGuestCustomerRestrictions() throws StoreException {
        storeService.provisionStore("store1", "Store", "Address", null);
        storeService.provisionAisle("store1", "A1", "Aisle", "Desc", AisleLocation.floor, null);
        storeService.provisionShelf("store1", "A1", "S1", "Shelf", ShelfLevel.medium, "Desc", 
                Temperature.ambient, null);
        storeService.provisionProduct("prod1", "Product", "Desc", "Size", "Cat", 1.0, 
                Temperature.ambient, null);
        storeService.provisionInventory("inv1", "store1", "A1", "S1", 100, 50, "prod1", 
                InventoryType.standard, null);

        // Creating th guest customer
        Customer guest = storeService.provisionCustomer("guest1", "Sergey", "Sundukovskiy", 
                CustomerType.guest, "sergey.sundukovskiy@example.com", "111 Cedar Ln", null);
        storeService.updateCustomer("guest1", "store1", "A1", null);
        storeService.provisionBasket("basket1", null);
        storeService.assignCustomerBasket("guest1", "basket1", null);

        // the fail options 
        assertThrows(StoreException.class, () -> 
            storeService.addBasketProduct("basket1", "prod1", 1, null));
    }

    @AfterAll
    static void tearDownAll() {
        StoreService.clearAllMaps();
        dataManager.clear();
    }
}
