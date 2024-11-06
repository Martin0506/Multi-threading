package com.example.workshop;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;
import java.util.Random;

public class OnlineOrderProcessor {
    private static final Logger logger = LoggerFactory.getLogger(OnlineOrderProcessor.class);
    private static final int NUM_THREADS = 4;
    private static final int NUM_ORDERS = 20;

    public static void main(String[] args) {
        // Crear un ExecutorService con un número fijo de hilos
        ExecutorService executor = Executors.newFixedThreadPool(NUM_THREADS);

        // Crear y enviar pedidos para procesamiento
        for (int i = 1; i <= NUM_ORDERS; i++) {
            Order order = new Order(i);
            executor.submit(() -> processOrder(order));
        }

        // Apagar el executor de manera ordenada
        executor.shutdown();
        try {
            if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

    private static void processOrder(Order order) {
        logger.info("Iniciando procesamiento del pedido: {}", order.getId());

        try {
            // Paso 1: Validación del pedido
            validateOrder(order);

            // Paso 2: Verificación de inventario
            checkInventory(order);

            // Paso 3: Procesamiento del pago
            processPayment(order);

            // Paso 4: Preparación para el envío
            prepareForShipment(order);

            logger.info("Pedido {} procesado completamente", order.getId());
        } catch (Exception e) {
            logger.error("Error procesando el pedido {}: {}", order.getId(), e.getMessage());
        }
    }

    private static void validateOrder(Order order) throws InterruptedException {
        logger.info("Validando pedido: {}", order.getId());
        simulateWork(100, 300);
    }

    private static void checkInventory(Order order) throws InterruptedException {
        logger.info("Verificando inventario para pedido: {}", order.getId());
        simulateWork(200, 500);
    }

    private static void processPayment(Order order) throws InterruptedException {
        logger.info("Procesando pago para pedido: {}", order.getId());
        simulateWork(300, 700);
    }

    private static void prepareForShipment(Order order) throws InterruptedException {
        logger.info("Preparando envío para pedido: {}", order.getId());
        simulateWork(200, 400);
    }

    private static void simulateWork(int minMs, int maxMs) throws InterruptedException {
        Random random = new Random();
        Thread.sleep(random.nextInt(maxMs - minMs) + minMs);
    }

    static class Order {
        private final int id;

        public Order(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }
    }
}
