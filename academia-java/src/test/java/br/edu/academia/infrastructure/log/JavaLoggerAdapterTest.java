package br.edu.academia.infrastructure.log;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;

import static org.junit.jupiter.api.Assertions.*;

class JavaLoggerAdapterTest {

    private static final String LOGGER_NAME = "br.edu.academia.test.adapter";

    private JavaLoggerAdapter adapter;
    private CapturingHandler handler;
    private java.util.logging.Logger julLogger;

    @BeforeEach
    void setUp() {
        adapter = new JavaLoggerAdapter(LOGGER_NAME);
        handler = new CapturingHandler();

        julLogger = java.util.logging.Logger.getLogger(LOGGER_NAME);
        julLogger.addHandler(handler);
        julLogger.setLevel(Level.ALL);
        julLogger.setUseParentHandlers(false);
    }

    @AfterEach
    void tearDown() {
        julLogger.removeHandler(handler);
        julLogger.setUseParentHandlers(true);
    }

    @Test
    void infoDeveMaperarParaLevelInfo() {
        adapter.info("mensagem info");
        assertEquals(1, handler.records.size());
        assertEquals(Level.INFO, handler.records.get(0).getLevel());
        assertEquals("mensagem info", handler.records.get(0).getMessage());
    }

    @Test
    void warnDeveMaperarParaLevelWarning() {
        adapter.warn("mensagem warn");
        assertEquals(1, handler.records.size());
        assertEquals(Level.WARNING, handler.records.get(0).getLevel());
        assertEquals("mensagem warn", handler.records.get(0).getMessage());
    }

    @Test
    void errorDeveMaperarParaLevelSevere() {
        adapter.error("mensagem error");
        assertEquals(1, handler.records.size());
        assertEquals(Level.SEVERE, handler.records.get(0).getLevel());
        assertEquals("mensagem error", handler.records.get(0).getMessage());
    }

    private static class CapturingHandler extends Handler {

        final List<LogRecord> records = new ArrayList<>();

        @Override
        public void publish(LogRecord record) {
            records.add(record);
        }

        @Override
        public void flush() {
        }

        @Override
        public void close() {
        }
    }
}
