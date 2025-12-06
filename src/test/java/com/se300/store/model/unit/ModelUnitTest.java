package com.se300.store.model.unit;

import com.se300.store.model.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


/**
 * The ModelUnitTest class contains unit tests for various models used in the Smart Store application.
 * It includes tests for creation, basic operations, and validation of models and enums utilized in the system.
 */

@DisplayName("Model Unit Tests")
public class ModelUnitTest {

    @Test
    @DisplayName("Test User model creation and getters/setters")
    public void testUserModel() {
        // Test constructer
        User user = new User("martha.dempkey@chapman.edu", "password123", "Martha Dempkey");
        
        assertNotNull(user);
        assertEquals("martha.dempkey@chapman.edu", user.getEmail());
        assertEquals("password123", user.getPassword());
        assertEquals("Martha Dempkey", user.getName());
        
        // Test seters
        user.setEmail("kody.wong@chapman.edu");
        user.setPassword("newpass456");
        user.setName("Kody Wong");
        
        assertEquals("kody.wong@chapman.edu", user.getEmail());
        assertEquals("newpass456", user.getPassword());
        assertEquals("Kody Wong", user.getName());
        
        User emptyUser = new User();
        assertNotNull(emptyUser);
 
        user.setEmail(null);
        user.setPassword(null);
        user.setName(null);
        assertNull(user.getEmail());
        assertNull(user.getPassword());
        assertNull(user.getName());
        
        emptyUser.setEmail("test@test.com");
        emptyUser.setPassword("pass");
        emptyUser.setName("Test User");
        assertEquals("test@test.com", emptyUser.getEmail());
        assertEquals("pass", emptyUser.getPassword());
        assertEquals("Test User", emptyUser.getName());
    }

    @Test
    @DisplayName("Test Product model creation and getters/setters")
    public void testProductModel() {
        // Test constructor with all parameters
        Product product = new Product(
            "prod1",
            "Tacos",
            "Beef Tacos",
            "3 pack",
            "Mexican Food",
            12.99,
            Temperature.hot
        );
        
        assertNotNull(product);
        assertEquals("prod1", product.getId());
        assertEquals("Tacos", product.getName());
        assertEquals("Beef Tacos", product.getDescription());
        assertEquals("3 pack", product.getSize());
        assertEquals("Mexican Food", product.getCategory());
        assertEquals(12.99, product.getPrice());
        assertEquals(Temperature.hot, product.getTemperature());
        
        // Test setters
        product.setId("prod2");
        product.setName("Sushi");
        product.setDescription("California Roll");
        product.setSize("8 pieces");
        product.setCategory("Japanese Food");
        product.setPrice(15.99);
        product.setTemperature(Temperature.refrigerated);
        
        assertEquals("prod2", product.getId());
        assertEquals("Sushi", product.getName());
        assertEquals("California Roll", product.getDescription());
        assertEquals("8 pieces", product.getSize());
        assertEquals("Japanese Food", product.getCategory());
        assertEquals(15.99, product.getPrice());
        assertEquals(Temperature.refrigerated, product.getTemperature());
        
        // Test toString
        String productString = product.toString();
        assertTrue(productString.contains("prod2"));
        assertTrue(productString.contains("Sushi"));
        
        product.setTemperature(Temperature.frozen);
        assertEquals(Temperature.frozen, product.getTemperature());
        
        // pricing 
        product.setPrice(0.0);
        assertEquals(0.0, product.getPrice());
        product.setPrice(999.99);
        
        // test all temperatures
        product.setTemperature(Temperature.warm);
        product.setTemperature(Temperature.ambient);
        assertEquals(Temperature.ambient, product.getTemperature());
    }

    @Test
    @DisplayName("Test Customer model creation and getters/setters")
    public void testCustomerModel() {
        // Test constructor
        Customer customer = new Customer(
            "cust1",
            "Nathan",
            "Watkins",
            CustomerType.registered,
            "nathan.watkins@chapman.edu",
            "321 Elm Blvd"
        );
        
        assertNotNull(customer);
        assertEquals("cust1", customer.getId());
        assertEquals("Nathan", customer.getFirstName());
        assertEquals("Watkins", customer.getLastName());
        assertEquals(CustomerType.registered, customer.getType());
        assertEquals("nathan.watkins@chapman.edu", customer.getEmail());
        assertEquals("321 Elm Blvd", customer.getAccountAddress());
        
        // Test setters
        customer.setId("cust2");
        customer.setFirstName("Sergey");
        customer.setLastName("Sundukovskiy");
        customer.setType(CustomerType.guest);
        customer.setEmail("sergey@chapman.edu");
        customer.setAccountAddress("111 Cedar Ln");
        customer.setAgeGroup(CustomerAgeGroup.adult);
        
        assertEquals("cust2", customer.getId());
        assertEquals("Sergey", customer.getFirstName());
        assertEquals("Sundukovskiy", customer.getLastName());
        assertEquals(CustomerType.guest, customer.getType());
        assertEquals("sergey@chapman.edu", customer.getEmail());
        assertEquals("111 Cedar Ln", customer.getAccountAddress());
        assertEquals(CustomerAgeGroup.adult, customer.getAgeGroup());
        
        // Test location and basket
        StoreLocation location = new StoreLocation("store1", "A1");
        customer.setStoreLocation(location);
        assertNotNull(customer.getStoreLocation());
        assertEquals("store1", customer.getStoreLocation().getStoreId());
        
        Basket basket = new Basket("basket1");
        customer.assignBasket(basket);
        assertNotNull(customer.getBasket());
        assertEquals("basket1", customer.getBasket().getId());
        
        String customerString = customer.toString();
        assertTrue(customerString.contains("cust2"));
        assertTrue(customerString.contains("Sergey"));
        
        customer.setAgeGroup(CustomerAgeGroup.child);
        assertEquals(CustomerAgeGroup.child, customer.getAgeGroup());
        
        customer.setLastSeen(new java.util.Date());
        assertNotNull(customer.getLastSeen());
        
        customer.assignBasket(null);
        assertNull(customer.getBasket());
        
        customer.setType(CustomerType.registered);
        assertEquals(CustomerType.registered, customer.getType());
    }

    @Test
    @DisplayName("Test Store model creation and basic operations")
    public void testStoreModel() throws StoreException {
        // Test constructer
        Store store = new Store("store1", "123 Main St", "Chapman Store");
        
        assertNotNull(store);
        assertEquals("store1", store.getId());
        assertEquals("123 Main St", store.getAddress());
        assertEquals("Chapman Store", store.getDescription());
        
        // Test seters
        store.setId("store2");
        store.setAddress("456 Oak Ave");
        store.setDescription("Updated Store");
        
        assertEquals("store2", store.getId());
        assertEquals("456 Oak Ave", store.getAddress());
        assertEquals("Updated Store", store.getDescription());
        
        // Test adding aisle
        Aisle aisle = store.addAisle("A1", "Groceries", "Food items", AisleLocation.floor);
        assertNotNull(aisle);
        assertEquals("A1", aisle.getNumber());
        
        // Test geting aisle
        Aisle retrieved = store.getAisle("A1");
        assertNotNull(retrieved);
        assertEquals("A1", retrieved.getNumber());
        
        // Test duplicate aisle throws exeption
        assertThrows(StoreException.class, () -> 
            store.addAisle("A1", "Duplicate", "Desc", AisleLocation.floor));
        
        // Test non-existant aisle throws exeption
        assertThrows(StoreException.class, () -> store.getAisle("NonExistent"));
        
        // Test adding custmer
        Customer customer = new Customer("cust1", "Martha", "Dempkey", 
            CustomerType.registered, "martha@chapman.edu", "Address");
        store.addCustomer(customer);
        assertEquals(customer, store.getCustomer("cust1"));
        
        // Test duplicate custmer throws exeption
        assertThrows(StoreException.class, () -> store.addCustomer(customer));
        
        Device device = new Sensor("sensor1", "Camera", new StoreLocation("store2", "A1"), "camera");
        store.addDevice(device);
        
        assertThrows(StoreException.class, () -> store.addDevice(device));
      
        Basket basket = new Basket("basket1");
        store.addBasket(basket);
        
        assertThrows(StoreException.class, () -> store.addBasket(basket));
     
        InventoryLocation invLoc = new InventoryLocation("store2", "A1", "S1");
        Inventory inventory = new Inventory("inv1", invLoc, 100, 50, "prod1", InventoryType.standard);
        store.addInventory(inventory);
        
        assertThrows(StoreException.class, () -> store.addInventory(inventory));
    
        String storeString = store.toString();
        assertTrue(storeString.contains("store2"));
      
        store.removeCustomer(customer);
        
        Aisle aisle2 = store.addAisle("B1", "Electronics", "Tech items", AisleLocation.store_room);
        assertNotNull(aisle2);
    }

    @Test
    @DisplayName("Test Basket model operations")
    public void testBasketModel() {
        // Testing constructer
        Basket basket = new Basket("basket1");
        
        assertNotNull(basket);
        assertEquals("basket1", basket.getId());
        
        // Test seter
        basket.setId("basket2");
        assertEquals("basket2", basket.getId());
        
        // Test store and custmer asociations
        Store store = new Store("store1", "Address", "Store");
        Customer customer = new Customer("cust1", "Kody", "Wong", 
            CustomerType.registered, "kody@chapman.edu", "Address");
        
        basket.setStore(store);
        basket.setCustomer(customer);
        
        assertNotNull(basket.getStore());
        assertNotNull(basket.getCustomer());
        assertEquals("store1", basket.getStore().getId());
        assertEquals("cust1", basket.getCustomer().getId());
        
        // Test toString
        String basketString = basket.toString();
        assertTrue(basketString.contains("basket2"));
        
        // test null store and custmer
        basket.setStore(null);
        basket.setCustomer(null);
        assertNull(basket.getStore());
        assertNull(basket.getCustomer());
        
        basket.setStore(store);
        assertNotNull(basket.getStore());
    }

    @Test
    @DisplayName("Test StoreLocation model")
    public void testStoreLocationModel() {
        // Testing constructer
        StoreLocation location = new StoreLocation("store1", "A1");
        
        assertNotNull(location);
        assertEquals("store1", location.getStoreId());
        assertEquals("A1", location.getAisleId());
        
        // Test seters
        location.setStoreId("store2");
        location.setAisleId("B2");
        
        assertEquals("store2", location.getStoreId());
        assertEquals("B2", location.getAisleId());
        
        // Testing toString
        String locationString = location.toString();
        assertTrue(locationString.contains("store2"));
        assertTrue(locationString.contains("B2"));
      
        InventoryLocation invLocation = new InventoryLocation("store1", "A1", "S1");
        assertNotNull(invLocation);
        assertEquals("store1", invLocation.getStoreId());
        assertEquals("A1", invLocation.getAisleId());
        assertEquals("S1", invLocation.getShelfId());
        
        invLocation.setShelfId("S2");
        assertEquals("S2", invLocation.getShelfId());
  
        String invString = invLocation.toString();
        assertTrue(invString.contains("S2"));
        
        invLocation.setStoreId("store3");
        invLocation.setAisleId("C3");
        assertEquals("store3", invLocation.getStoreId());
    }

    @Test
    @DisplayName("Test Temperature enum")
    public void testTemperatureEnum() {
        // Test all enum values exsit
        assertEquals(5, Temperature.values().length);
        
        assertNotNull(Temperature.frozen);
        assertNotNull(Temperature.refrigerated);
        assertNotNull(Temperature.ambient);
        assertNotNull(Temperature.warm);
        assertNotNull(Temperature.hot);
        assertEquals(Temperature.frozen, Temperature.valueOf("frozen"));
        assertEquals(Temperature.refrigerated, Temperature.valueOf("refrigerated"));
        assertEquals(Temperature.ambient, Temperature.valueOf("ambient"));
        assertEquals(Temperature.warm, Temperature.valueOf("warm"));
        assertEquals(Temperature.hot, Temperature.valueOf("hot"));
        
        assertThrows(IllegalArgumentException.class, () -> 
            Temperature.valueOf("invalid"));
            
        // test name method
        assertEquals("frozen", Temperature.frozen.name());
        assertEquals("hot", Temperature.hot.name());
        assertEquals("warm", Temperature.warm.name());
        assertEquals("ambient", Temperature.ambient.name());
    }

    @Test
    @DisplayName("Test CustomerType enum")
    public void testCustomerTypeEnum() {
        // Test all enum values exsit
        assertEquals(2, CustomerType.values().length);
        
        assertNotNull(CustomerType.guest);
        assertNotNull(CustomerType.registered);

        assertEquals(CustomerType.guest, CustomerType.valueOf("guest"));
        assertEquals(CustomerType.registered, CustomerType.valueOf("registered"));
  
        assertThrows(IllegalArgumentException.class, () -> 
            CustomerType.valueOf("invalid"));
            
        // testing ordinal
        assertTrue(CustomerType.guest.ordinal() >= 0);
        assertTrue(CustomerType.registered.ordinal() >= 0);
        
        assertEquals("guest", CustomerType.guest.name());
    }

    @Test
    @DisplayName("Test CustomerAgeGroup enum")
    public void testCustomerAgeGroupEnum() {
        assertEquals(2, CustomerAgeGroup.values().length);
        
        assertNotNull(CustomerAgeGroup.child);
        assertNotNull(CustomerAgeGroup.adult);
        
        assertEquals(CustomerAgeGroup.child, CustomerAgeGroup.valueOf("child"));
        assertEquals(CustomerAgeGroup.adult, CustomerAgeGroup.valueOf("adult"));
        
        // test invalid
        assertThrows(IllegalArgumentException.class, () ->
            CustomerAgeGroup.valueOf("teenager"));
            
        assertEquals("child", CustomerAgeGroup.child.name());
    }

    @Test
    @DisplayName("Test ShelfLevel enum")
    public void testShelfLevelEnum() {
        assertEquals(3, ShelfLevel.values().length);
        
        assertNotNull(ShelfLevel.high);
        assertNotNull(ShelfLevel.medium);
        assertNotNull(ShelfLevel.low);
        
        assertEquals(ShelfLevel.high, ShelfLevel.valueOf("high"));
        assertEquals(ShelfLevel.medium, ShelfLevel.valueOf("medium"));
        assertEquals(ShelfLevel.low, ShelfLevel.valueOf("low"));
        
        // test invalid
        assertThrows(IllegalArgumentException.class, () ->
            ShelfLevel.valueOf("super_high"));
            
        assertTrue(ShelfLevel.high.ordinal() >= 0);
        assertTrue(ShelfLevel.low.ordinal() >= 0);
    }

    @Test
    @DisplayName("Test AisleLocation enum")
    public void testAisleLocationEnum() {
        assertEquals(2, AisleLocation.values().length);
        
        assertNotNull(AisleLocation.floor);
        assertNotNull(AisleLocation.store_room);
        
        assertEquals(AisleLocation.floor, AisleLocation.valueOf("floor"));
        assertEquals(AisleLocation.store_room, AisleLocation.valueOf("store_room"));
        
        // test ordinals
        assertTrue(AisleLocation.floor.ordinal() >= 0);
        assertTrue(AisleLocation.store_room.ordinal() >= 0);
        
        assertEquals("floor", AisleLocation.floor.name());
    }

    @Test
    @DisplayName("Test InventoryType enum")
    public void testInventoryTypeEnum() {
        assertEquals(2, InventoryType.values().length);
        
        assertNotNull(InventoryType.standard);
        assertNotNull(InventoryType.flexible);
        
        assertEquals(InventoryType.standard, InventoryType.valueOf("standard"));
        assertEquals(InventoryType.flexible, InventoryType.valueOf("flexible"));
        
        // test name method
        assertEquals("standard", InventoryType.standard.name());
        
        assertTrue(InventoryType.flexible.ordinal() >= 0);
    }

    @Test
    @DisplayName("Test StoreException")
    public void testStoreException() {
        // Test constructer
        StoreException exception = new StoreException("Add Product", "Product already exists");
        
        assertNotNull(exception);
        assertEquals("Add Product", exception.getAction());
        assertEquals("Product already exists", exception.getReason());
        
        // Test seters
        exception.setAction("Update Product");
        exception.setReason("Product not found");
        
        assertEquals("Update Product", exception.getAction());
        assertEquals("Product not found", exception.getReason());
        
        // test with null values
        exception.setAction(null);
        exception.setReason(null);
        assertNull(exception.getAction());
        assertNull(exception.getReason());
        
        StoreException exception2 = new StoreException("Delete Item", "Item does not exist");
        assertEquals("Delete Item", exception2.getAction());
    }

    @Test
    @DisplayName("Test CommandException")
    public void testCommandException() {
        // Test constructer
        CommandException exception = new CommandException("invalid command", "Unrecognized command");
        
        assertNotNull(exception);
        assertEquals("invalid command", exception.getCommand());
        assertEquals("Unrecognized command", exception.getReason());
        
        // Test seters
        exception.setCommand("define store");
        exception.setReason("Missing parameters");
        exception.setLineNumber(42);
        
        assertEquals("define store", exception.getCommand());
        assertEquals("Missing parameters", exception.getReason());
        assertEquals(42, exception.getLineNumber());
        
        // test edge cases
        exception.setLineNumber(0);
        assertEquals(0, exception.getLineNumber());
        exception.setLineNumber(-1);
        assertEquals(-1, exception.getLineNumber());
        exception.setLineNumber(1000);
        assertEquals(1000, exception.getLineNumber());
        
        exception.setCommand(null);
        assertNull(exception.getCommand());
    }

    @Test
    @DisplayName("Test Aisle model")
    public void testAisleModel() throws StoreException {
        Aisle aisle = new Aisle("A1", "Groceries", "Food items", AisleLocation.floor);
        
        assertNotNull(aisle);
        assertEquals("A1", aisle.getNumber());
        assertEquals("Groceries", aisle.getName());
        assertEquals("Food items", aisle.getDescription());
        assertEquals(AisleLocation.floor, aisle.getAisleLocation());
        
        // Test adding shelf
        Shelf shelf = aisle.addShelf("S1", "Top Shelf", ShelfLevel.high, "Desc", Temperature.ambient);
        assertNotNull(shelf);
        assertEquals("S1", shelf.getId());
        
        Shelf retrieved = aisle.getShelf("S1");
        assertNotNull(retrieved);
        assertEquals("S1", retrieved.getId());
        
        assertThrows(StoreException.class, () -> 
            aisle.addShelf("S2", "Another", ShelfLevel.high, "Desc", Temperature.ambient));
            
        aisle.setNumber("A2");
        aisle.setName("Updated");
        aisle.setDescription("New desc");
        aisle.setAisleLocation(AisleLocation.store_room);
        
        assertEquals("A2", aisle.getNumber());
        assertEquals("Updated", aisle.getName());
        assertEquals("New desc", aisle.getDescription());
        assertEquals(AisleLocation.store_room, aisle.getAisleLocation());
        
        // test getShelfMap
        assertNotNull(aisle.getShelfMap());
        assertEquals(1, aisle.getShelfMap().size());
        
        // test toString
        String aisleString = aisle.toString();
        assertTrue(aisleString.contains("A2"));
        
        Shelf shelf2 = aisle.addShelf("S2", "Bottom Shelf", ShelfLevel.low, "Lower shelf", Temperature.frozen);
        assertEquals(2, aisle.getShelfMap().size());
    }

    @Test
    @DisplayName("Test Shelf model")
    public void testShelfModel() throws StoreException {
        Shelf shelf = new Shelf("S1", "Top Shelf", ShelfLevel.high, "Description", Temperature.frozen);
        
        assertNotNull(shelf);
        assertEquals("S1", shelf.getId());
        assertEquals("Top Shelf", shelf.getName());
        assertEquals(ShelfLevel.high, shelf.getLevel());
        assertEquals("Description", shelf.getDescription());
        assertEquals(Temperature.frozen, shelf.getTemperature());
        
        // Test adding inventory
        Inventory inv = shelf.addInventory("inv1", "store1", "A1", "S1", 
            100, 50, "prod1", InventoryType.standard);
        assertNotNull(inv);
        assertEquals("inv1", inv.getId());
        assertEquals(50, inv.getCount());
        
        // Test inventory map
        assertEquals(1, shelf.getInventoryMap().size());
        assertTrue(shelf.getInventoryMap().containsKey("inv1"));
        
        // test seters
        shelf.setId("S2");
        shelf.setName("Bottom Shelf");
        shelf.setLevel(ShelfLevel.low);
        shelf.setDescription("New desc");
        shelf.setTemperature(Temperature.ambient);
        
        assertEquals("S2", shelf.getId());
        assertEquals("Bottom Shelf", shelf.getName());
        assertEquals(ShelfLevel.low, shelf.getLevel());
        assertEquals("New desc", shelf.getDescription());
        assertEquals(Temperature.ambient, shelf.getTemperature());
        
        assertThrows(StoreException.class, () ->
            shelf.addInventory("inv1", "store1", "A1", "S1", 100, 50, "prod1", InventoryType.standard));
            
        assertThrows(StoreException.class, () ->
            shelf.addInventory("inv2", "store1", "A1", "S1", 100, -1, "prod1", InventoryType.standard));
            
        assertThrows(StoreException.class, () ->
            shelf.addInventory("inv3", "store1", "A1", "S1", 100, 101, "prod1", InventoryType.standard));
            
        String shelfString = shelf.toString();
        assertTrue(shelfString.contains("S2"));
    }

    @Test
    @DisplayName("Test Inventory model")
    public void testInventoryModel() throws StoreException {
        InventoryLocation location = new InventoryLocation("store1", "A1", "S1");
        Inventory inventory = new Inventory("inv1", location, 100, 50, "prod1", InventoryType.standard);
        
        assertNotNull(inventory);
        assertEquals("inv1", inventory.getId());
        assertEquals(100, inventory.getCapacity());
        assertEquals(50, inventory.getCount());
        assertEquals("prod1", inventory.getProductId());
        assertEquals(InventoryType.standard, inventory.getType());
        
        // Test update inventory
        inventory.updateInventory(10);
        assertEquals(60, inventory.getCount());
        
        assertThrows(StoreException.class, () -> inventory.updateInventory(-100));
        assertThrows(StoreException.class, () -> inventory.updateInventory(50));
        
        // test seters
        inventory.setId("inv2");
        inventory.setCapacity(200);
        inventory.setCount(75);
        inventory.setProductId("prod2");
        inventory.setType(InventoryType.flexible);
        
        assertEquals("inv2", inventory.getId());
        assertEquals(200, inventory.getCapacity());
        assertEquals(75, inventory.getCount());
        assertEquals("prod2", inventory.getProductId());
        assertEquals(InventoryType.flexible, inventory.getType());
        
        // test inventory loction
        InventoryLocation newLoc = new InventoryLocation("store2", "A2", "S2");
        inventory.setInventoryLocation(newLoc);
        assertEquals("store2", inventory.getInventoryLocation().getStoreId());
        
        // test toString
        String invString = inventory.toString();
        assertTrue(invString.contains("inv2"));
        
        inventory.updateInventory(20);
        assertEquals(95, inventory.getCount());
        
        inventory.setCount(200);
        assertEquals(200, inventory.getCount());
    }

    @Test
    @DisplayName("Test Device models")
    public void testDeviceModels() {
        StoreLocation location = new StoreLocation("store1", "A1");
        
        // Test Sensor
        Sensor sensor = new Sensor("sensor1", "Camera1", location, "camera");
        assertNotNull(sensor);
        assertEquals("sensor1", sensor.getId());
        assertEquals("Camera1", sensor.getName());
        assertEquals("camera", sensor.getType());
        assertDoesNotThrow(() -> sensor.processEvent("customer_detected"));
        
        // test seters
        sensor.setId("sensor2");
        sensor.setName("Camera2");
        sensor.setType("microphone");
        StoreLocation newLoc = new StoreLocation("store2", "A2");
        sensor.setStoreLocation(newLoc);
        
        assertEquals("sensor2", sensor.getId());
        assertEquals("Camera2", sensor.getName());
        assertEquals("microphone", sensor.getType());
        assertEquals("store2", sensor.getStoreLocation().getStoreId());
        
        // Test Appliance
        Appliance appliance = new Appliance("robot1", "Robot1", location, "robot");
        assertNotNull(appliance);
        assertEquals("robot1", appliance.getId());
        assertEquals("Robot1", appliance.getName());
        assertEquals("robot", appliance.getType());
        assertDoesNotThrow(() -> appliance.processEvent("spill_detected"));
        assertDoesNotThrow(() -> appliance.processCommand("CLEAN_FLOOR"));
        
        appliance.setId("robot2");
        appliance.setName("Robot2");
        appliance.setType("turnstile");
        
        assertEquals("robot2", appliance.getId());
        assertEquals("Robot2", appliance.getName());
        
        // testing toString for devices
        String sensorString = sensor.toString();
        assertTrue(sensorString.contains("sensor2"));
        
        String applianceString = appliance.toString();
        assertTrue(applianceString.contains("robot2"));
        
        assertDoesNotThrow(() -> sensor.processEvent("motion_detected"));
        assertDoesNotThrow(() -> appliance.processCommand("STOCK_SHELF"));
    }
    
    @Test
    @DisplayName("Test SensorType enum")
    public void testSensorTypeEnum() {
        assertEquals(2, SensorType.values().length);
        assertNotNull(SensorType.microphone);
        assertNotNull(SensorType.camera);
        assertEquals(SensorType.microphone, SensorType.valueOf("microphone"));
        assertEquals(SensorType.camera, SensorType.valueOf("camera"));
        
        // test names
        assertEquals("microphone", SensorType.microphone.name());
        assertEquals("camera", SensorType.camera.name());
    }
    
    @Test
    @DisplayName("Test ApplianceType enum")
    public void testApplianceTypeEnum() {
        assertEquals(3, ApplianceType.values().length);
        assertNotNull(ApplianceType.speaker);
        assertNotNull(ApplianceType.robot);
        assertNotNull(ApplianceType.turnstile);
        assertEquals(ApplianceType.robot, ApplianceType.valueOf("robot"));
        assertEquals(ApplianceType.speaker, ApplianceType.valueOf("speaker"));
        assertEquals(ApplianceType.turnstile, ApplianceType.valueOf("turnstile"));
        
        // test ordinals
        assertTrue(ApplianceType.speaker.ordinal() >= 0);
        assertTrue(ApplianceType.robot.ordinal() >= 0);
        assertTrue(ApplianceType.turnstile.ordinal() >= 0);
    }
}
