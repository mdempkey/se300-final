package com.se300.store.controller;

import com.se300.store.model.Store;
import com.se300.store.model.StoreException;
import com.se300.store.service.StoreService;
import com.se300.store.servlet.BaseServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Collection;

/**
 * REST API controller for Store operations
 * Implements full CRUD operations
 *
 * @author Sergey L. Sundukovskiy, Ph.D.
 * @version 1.0
 */
public class StoreController extends BaseServlet {

    //TODO: Implement REST CRUD API for Store operations

    private final StoreService storeService;

    public StoreController(StoreService storeService) {
        this.storeService = storeService;
    }

    /**
     * Handle GET requests
     * - GET /api/v1/stores (no parameters) - Get all stores
     * - GET /api/v1/stores/{storeId} - Get store by ID
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    }

    /**
     * Handle POST requests - Create new store
     * POST /api/v1/stores?storeId=xxx&name=xxx&address=xxx
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    }

    /**
     * Handle PUT requests - Update existing store
     * PUT /api/v1/stores/{storeId}?description=xxx&address=xxx
     */
    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {
    }

    /**
     * Handle DELETE requests - Delete store
     * DELETE /api/v1/stores/{storeId}
     */
    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
    }
}