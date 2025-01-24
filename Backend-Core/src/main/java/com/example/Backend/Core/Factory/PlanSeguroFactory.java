package com.example.Backend.Core.Factory;

import com.example.Backend.Core.Models.PlanSeguro;

public class PlanSeguroFactory {
    // Crear una instancia estática única de la clase
    private static PlanSeguroFactory instance;

    // Constructor privado para evitar instanciación directa
    private PlanSeguroFactory() {}

    // Método estático sincronizado para obtener la instancia única
    public static synchronized PlanSeguroFactory getInstance() {
        if (instance == null) {
            instance = new PlanSeguroFactory();
        }
        return instance;
    }

    // Método para crear un PlanSeguro
    public PlanSeguro createPlanSeguro(String nombre, boolean perdidasParciales, double valorPerdidasParciales,
                                       boolean perdidasTotales, double valorPerdidasTotales, boolean auxilioMecanico,
                                       boolean mantenimientoVehicular, double valorPlan) throws Exception {

        // Validaciones (mismas que ya tienes)
        if (!nombre.matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]{1,30}$")) {
            throw new Exception("El nombre contiene caracteres no permitidos: " + nombre);
        }

        if (!perdidasParciales) {
            valorPerdidasParciales = 0.00;
        } else if (valorPerdidasParciales < 0 || (valorPerdidasParciales * 100) % 1 != 0) {
            throw new Exception("El valor de pérdidas parciales " + valorPerdidasParciales + " es incorrecto");
        }

        if (!perdidasTotales) {
            valorPerdidasTotales = 0.00;
        } else if (valorPerdidasTotales < 0 || (valorPerdidasTotales * 100) % 1 != 0) {
            throw new Exception("El valor de pérdidas totales " + valorPerdidasTotales + " es incorrecto");
        }

        if (valorPlan <= 0 || (valorPlan * 100) % 1 != 0) {
            throw new Exception("El valor del plan " + valorPlan + " es incorrecto");
        }

        // Crear y devolver el objeto PlanSeguro
        PlanSeguro planSeguro = new PlanSeguro();
        planSeguro.setNombre(nombre);
        planSeguro.setPerdidasParciales(perdidasParciales);
        planSeguro.setValorPerdidasParciales(valorPerdidasParciales);
        planSeguro.setPerdidasTotales(perdidasTotales);
        planSeguro.setValorPerdidasTotales(valorPerdidasTotales);
        planSeguro.setAuxilioMecanico(auxilioMecanico);
        planSeguro.setMantenimientoVehicular(mantenimientoVehicular);
        planSeguro.setValorPlan(valorPlan);

        return planSeguro;
    }
}