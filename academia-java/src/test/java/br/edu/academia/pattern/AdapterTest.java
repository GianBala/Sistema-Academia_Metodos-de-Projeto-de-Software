package br.edu.academia.pattern;

import br.edu.academia.domain.logging.Logger;
import br.edu.academia.infrastructure.logging.JavaUtilLoggingAdapter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testa a integridade do padrao Adapter:
 * JavaUtilLoggingAdapter adapta java.util.logging.Logger para a interface Logger do dominio.
 */
class AdapterTest {

    /** Handler customizado para capturar registros de log nos testes. */
    static class CapturingHandler extends Handler {
        private final List<LogRecord> records = new ArrayList<>();

        @Override public void publish(LogRecord record) { records.add(record); }
        @Override public void flush()  {}
        @Override public void close()  {}

        public List<LogRecord> getRecords() { return records; }
        public LogRecord lastRecord() { return records.get(records.size() - 1); }
    }

    private JavaUtilLoggingAdapter adapter;
    private CapturingHandler handler;

    @BeforeEach
    void setUp() {
        adapter = new JavaUtilLoggingAdapter("TestLogger");

        // Anexar nosso handler de captura ao logger JUL subjacente
        var julLogger = java.util.logging.Logger.getLogger("TestLogger");
        julLogger.setUseParentHandlers(false);
        handler = new CapturingHandler();
        handler.setLevel(Level.ALL);
        julLogger.setLevel(Level.ALL);
        julLogger.addHandler(handler);
    }

    @Test
    void adapterImplementaInterfaceLogger() {
        assertInstanceOf(Logger.class, adapter,
                "JavaUtilLoggingAdapter deve implementar a interface Logger do dominio");
    }

    @Test
    void infoMapsToJulInfo() {
        adapter.info("mensagem info");

        assertFalse(handler.getRecords().isEmpty());
        assertEquals(Level.INFO, handler.lastRecord().getLevel());
        assertTrue(handler.lastRecord().getMessage().contains("mensagem info"));
    }

    @Test
    void warnMapsToJulWarning() {
        adapter.warn("mensagem warn");

        assertEquals(Level.WARNING, handler.lastRecord().getLevel());
    }

    @Test
    void errorMapsToJulSevere() {
        adapter.error("mensagem error");

        assertEquals(Level.SEVERE, handler.lastRecord().getLevel());
    }

    @Test
    void debugMapsToJulFine() {
        adapter.debug("mensagem debug");

        assertEquals(Level.FINE, handler.lastRecord().getLevel());
    }

    @Test
    void mensagemContemTextoPassado() {
        adapter.info("texto especifico");

        assertTrue(handler.lastRecord().getMessage().contains("texto especifico"));
    }
}
