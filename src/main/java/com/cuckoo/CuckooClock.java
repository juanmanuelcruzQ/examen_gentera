package com.cuckoo;

import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class CuckooClock {

    private static final String CUCKOO   = "Cuckoo";
    private static final String FIZZ     = "Fizz";
    private static final String BUZZ     = "Buzz";
    private static final String FIZZ_BUZZ = "Fizz Buzz";
    private static final String TICK     = "tick";

    private static final String TIME_REGEX = "^([01]\\d|2[0-3]):[0-5]\\d$";


    public String tick(String tiempo){

        validacion(tiempo);

        int[] zona = parse(tiempo);
        int   hora   = zona[0];
        int   minuto = zona[1];

        return Optional.of(minuto)
                .map(m -> sonidoReloj(hora, m))
                .orElse(TICK);
    }

    private String sonidoReloj(int hora, Integer minuto) {
        if (minuto == 0) return validaHora(hora);
        if (minuto == 30 ) return CUCKOO;
        if (minuto % 15 == 0) return FIZZ_BUZZ;
        if (minuto % 3  == 0) return FIZZ;
        if (minuto % 5  == 0) return BUZZ;
        return TICK;
    }

    private String validaHora(int hora) {
        int horaExacta = hora % 12 == 0 ? 12 : hora % 12;

        return IntStream.rangeClosed(1, horaExacta)
                .mapToObj(i -> CUCKOO)
                .collect(Collectors.joining(" "));
    }

    private void validacion(String tiempo) {
        if (tiempo == null || tiempo.isBlank()){
            throw new IllegalArgumentException("La hora no puede ser nula o vacía.");
        }

        if (!tiempo.matches(TIME_REGEX)){
            throw new IllegalArgumentException(
                    "Formato de hora inválido: '%s'. Se esperaba HH:mm (00-23 / 00-59).".formatted(tiempo));
        }
    }

    private int[] parse (String tiempo){
        String[] separar = tiempo.split(":");
        return new int[]{ Integer.parseInt(separar[0]), Integer.parseInt(separar[1]) };
    }


    //Prueba
    public static void main(String[] args) {
        CuckooClock clock = new CuckooClock();
        System.out.println(clock.tick("13:00"));
        System.out.println(clock.tick("08:15"));
        System.out.println(clock.tick("14:07"));
    }

}
