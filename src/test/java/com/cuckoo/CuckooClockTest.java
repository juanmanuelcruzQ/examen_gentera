package com.cuckoo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

@DisplayName("CuckooClock")
class CuckooClockTest {

    private CuckooClock clock;

    @BeforeEach
    void setUp() {
        clock = new CuckooClock();
    }

    @Nested
    @DisplayName("Validación de entrada")
    class Validacion {

        @ParameterizedTest(name = "entrada [{0}] debe rechazarse")
        @NullAndEmptySource
        @DisplayName("nulo y vacío lanzan excepción")
        void dadoNuloOVacio_deberiaLanzarExcepcion(String entrada) {
            assertThatIllegalArgumentException()
                    .isThrownBy(() -> clock.tick(entrada))
                    .withMessageContaining("nula o vacía");
        }

        @ParameterizedTest(name = "[{0}] no cumple formato HH:mm")
        @ValueSource(strings = { "9:00", "24:00", "12:60", "13.00", "once:00", "13:00:05" })
        @DisplayName("formato inválido lanza excepción")
        void dadoFormatoInvalido_deberiaLanzarExcepcion(String entrada) {
            assertThatIllegalArgumentException()
                    .isThrownBy(() -> clock.tick(entrada))
                    .withMessageContaining("Formato de hora inválido");
        }
    }

    @Nested
    @DisplayName("Hora en punto")
    class HoraEnPunto {

        @Test
        @DisplayName("00:00 emite 12 Cuckoos")
        void medianoche_doceRepiques() {
            assertThat(clock.tick("00:00")).isEqualTo("Cuckoo ".repeat(12).trim());
        }

        @Test
        @DisplayName("12:00 emite 12 Cuckoos")
        void mediodia_doceRepiques() {
            assertThat(clock.tick("12:00")).isEqualTo("Cuckoo ".repeat(12).trim());
        }

        @Test
        @DisplayName("01:00 emite 1 Cuckoo")
        void hora01_unRepique() {
            assertThat(clock.tick("01:00")).isEqualTo("Cuckoo");
        }

        @Test
        @DisplayName("13:00 emite 1 Cuckoo — conversión 24h a 12h")
        void hora13_unRepique() {
            assertThat(clock.tick("13:00")).isEqualTo("Cuckoo");
        }

        @Test
        @DisplayName("23:00 emite 11 Cuckoos")
        void hora23_onceRepiques() {
            assertThat(clock.tick("23:00")).isEqualTo("Cuckoo ".repeat(11).trim());
        }

        @ParameterizedTest(name = "{0} → {1} Cuckoos")
        @CsvSource({
                "02:00,2",  "03:00,3",  "04:00,4",  "05:00,5",
                "06:00,6",  "07:00,7",  "08:00,8",  "09:00,9",
                "10:00,10", "11:00,11", "14:00,2",  "15:00,3",
                "16:00,4",  "17:00,5",  "18:00,6",  "19:00,7",
                "20:00,8",  "21:00,9",  "22:00,10"
        })
        @DisplayName("todas las horas en punto producen los repiques correctos")
        void todasLasHoras_repiquesCorrectos(String hora, int cantidad) {
            assertThat(clock.tick(hora)).isEqualTo("Cuckoo ".repeat(cantidad).trim());
        }
    }

    @Nested
    @DisplayName("Media hora")
    class MediaHora {

        @ParameterizedTest(name = "{0} → Cuckoo")
        @ValueSource(strings = { "00:30", "01:30", "08:30", "12:30", "15:30", "23:30" })
        @DisplayName("siempre emite exactamente un Cuckoo")
        void mediaHora_unSoloRepique(String hora) {
            assertThat(clock.tick(hora)).isEqualTo("Cuckoo");
        }
    }

    @Nested
    @DisplayName("Fizz Buzz")
    class FizzBuzz {

        @ParameterizedTest(name = "{0} → Fizz Buzz")
        @ValueSource(strings = { "08:15", "08:45", "00:45", "01:15", "23:45" })
        @DisplayName("minuto múltiplo de 15 emite Fizz Buzz")
        void multiplo15_fizzBuzz(String hora) {
            assertThat(clock.tick(hora)).isEqualTo("Fizz Buzz");
        }
    }

    @Nested
    @DisplayName("Fizz")
    class Fizz {

        @ParameterizedTest(name = "{0} → Fizz")
        @ValueSource(strings = {
                "08:03", "08:54", "00:03", "01:06", "23:57",
                "08:09", "08:12", "08:18", "08:21", "08:24",
                "08:27", "08:33", "08:36", "08:39", "08:42",
                "08:48", "08:51", "08:57"
        })
        @DisplayName("minuto múltiplo de 3 (no de 5) emite Fizz")
        void multiplo3_fizz(String hora) {
            assertThat(clock.tick(hora)).isEqualTo("Fizz");
        }
    }

    @Nested
    @DisplayName("Buzz")
    class Buzz {

        @ParameterizedTest(name = "{0} → Buzz")
        @ValueSource(strings = {
                "08:05", "08:10", "08:20", "08:25", "08:35",
                "08:40", "08:50", "08:55", "00:05", "23:55"
        })
        @DisplayName("minuto múltiplo de 5 (no de 3) emite Buzz")
        void multiplo5_buzz(String hora) {
            assertThat(clock.tick(hora)).isEqualTo("Buzz");
        }
    }

    @Nested
    @DisplayName("tick")
    class Tick {

        @ParameterizedTest(name = "{0} → tick")
        @ValueSource(strings = {
                "14:07", "23:59", "00:01", "00:02", "00:04",
                "08:01", "08:02", "08:04", "08:08", "08:11",
                "08:13", "08:14", "08:16", "08:17", "08:19",
                "08:22", "08:23", "08:26", "08:28", "08:29",
                "08:31", "08:32", "08:34", "08:37", "08:38",
                "08:41", "08:43", "08:44", "08:46", "08:47",
                "08:49", "08:52", "08:53", "08:56",
                "08:58", "08:59"
        })
        @DisplayName("minuto sin regla activa emite tick")
        void sinRegla_tick(String hora) {
            assertThat(clock.tick(hora)).isEqualTo("tick");
        }
    }

    @Nested
    @DisplayName("Casos de aceptación del enunciado")
    class Aceptacion {

        @ParameterizedTest(name = "{0} → {1}")
        @CsvSource(delimiter = '|', value = {
                "00:00 | Cuckoo Cuckoo Cuckoo Cuckoo Cuckoo Cuckoo Cuckoo Cuckoo Cuckoo Cuckoo Cuckoo Cuckoo",
                "12:00 | Cuckoo Cuckoo Cuckoo Cuckoo Cuckoo Cuckoo Cuckoo Cuckoo Cuckoo Cuckoo Cuckoo Cuckoo",
                "01:00 | Cuckoo",
                "13:00 | Cuckoo",
                "23:00 | Cuckoo Cuckoo Cuckoo Cuckoo Cuckoo Cuckoo Cuckoo Cuckoo Cuckoo Cuckoo Cuckoo",
                "08:30 | Cuckoo",
                "15:30 | Cuckoo",
                "00:30 | Cuckoo",
                "08:15 | Fizz Buzz",
                "08:45 | Fizz Buzz",
                "00:45 | Fizz Buzz",
                "08:03 | Fizz",
                "08:54 | Fizz",
                "08:05 | Buzz",
                "08:10 | Buzz",
                "14:07 | tick",
                "23:59 | tick"
        })
        @DisplayName("todos los casos del enunciado deben pasar")
        void casosDelEnunciado(String entrada, String esperado) {
            assertThat(clock.tick(entrada.trim())).isEqualTo(esperado.trim());
        }
    }

    @Nested
    @DisplayName("Formato de salida")
    class FormatoSalida {

        @Test
        @DisplayName("sin espacios al inicio ni al final")
        void sinEspaciosExternos() {
            assertThat(clock.tick("00:00")).doesNotStartWith(" ").doesNotEndWith(" ");
            assertThat(clock.tick("01:00")).doesNotStartWith(" ").doesNotEndWith(" ");
            assertThat(clock.tick("08:03")).doesNotStartWith(" ").doesNotEndWith(" ");
        }

        @Test
        @DisplayName("palabras separadas por exactamente un espacio")
        void separadorSimple() {
            String resultado = clock.tick("03:00");
            assertThat(resultado.split(" ")).hasSize(3);
            assertThat(resultado).doesNotContain("  ");
        }
    }
}