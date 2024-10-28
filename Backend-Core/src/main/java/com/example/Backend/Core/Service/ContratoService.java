package com.example.Backend.Core.Service;

import com.example.Backend.Core.Models.Automovil;
import com.example.Backend.Core.Models.Cliente;
import com.example.Backend.Core.Models.Contrato;
import com.example.Backend.Core.Models.PlanSeguro;
import com.example.Backend.Core.Repository.ContratoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ContratoService {
    @Autowired
    private final ContratoRepository contratoRepository;
    private final AutomovilService automovilService;
    private final ClienteService clienteService;
    private final PlanService planService;

    public ContratoService(ContratoRepository contratoRepository, AutomovilService automovilService, ClienteService clienteService, PlanService planService) {
        this.contratoRepository = contratoRepository;
        this.automovilService = automovilService;
        this.clienteService = clienteService;
        this.planService = planService;
    }

    public List<Contrato> findAll(){
        return contratoRepository.findAll();
    }

    public Contrato findByIdContrato (Long idContrato){
        return contratoRepository.findByIdContrato(idContrato);
    }

    public Contrato registrarContrato(Contrato contrato) throws Exception{
        //Validacion del automovil
        Automovil automovil = automovilService.findByIdAutomovil(contrato.getAutomovil().getIdAutomovil());
        if (automovil == null){
            throw new Exception("El automóvil no existe");
        }

        //Validacion del cliente
        Cliente cliente = clienteService.findByIdCliente(contrato.getCliente().getIdCliente());
        if (cliente == null){
            throw new Exception("El cliente no existe");
        }

        //Validacion del Plan de Seguro
        PlanSeguro plan = planService.findByIdPlan(contrato.getPlanSeguro().getIdPlan());
        if (plan == null){
            throw new Exception("El plan de seguro no existe");
        }

        //Validacion FechaFinalizacion del contrato
        if (contrato.getFechaInicio().compareTo(contrato.getFechaFinalizacion()) >= 0){
            throw new Exception("La fecha de Finalizacion debe ser menor");
        }


        //Validacion valores agregados
        if (contrato.getValoresAgregados()<=0 || (contrato.getValoresAgregados() * 100) % 1 != 0) {
            throw new Exception("El valor de valores agregados "+contrato.getValoresAgregados()+" es incorrecto");
        }

        //Validacion valor Subtotal
        if (contrato.getValorsubtotal()<=0 || (contrato.getValorsubtotal() * 100) % 1 != 0) {
            throw new Exception("El valor de subtotal "+contrato.getValorsubtotal()+" es incorrecto");
        }

        //Validacion Valor Total
        if (contrato.getValortotal()<=0 || (contrato.getValortotal() * 100) % 1 != 0) {
            throw new Exception("El valor total "+contrato.getValortotal()+" es incorrecto");
        }

        contrato.setAutomovil(automovil);
        contrato.setCliente(cliente);
        contrato.setPlanSeguro(plan);
        return contratoRepository.save(contrato);
    }

    public Contrato updateContrato(Long idContrato, Contrato contratoActualizar) throws Exception{
        Contrato existe = findByIdContrato(idContrato);
        if (existe != null){
            if (existe.getFechaRenovacion().before(existe.getFechaInicio())){
                throw new Exception("La fecha de renovacion debeser posterior a la fecha de inicio "+ existe.getFechaInicio());
            }

            //Validacion FechaFinalizacion del contrato
            if (existe.getFechaInicio().compareTo(existe.getFechaFinalizacion()) >= 0){
                throw new Exception("La fecha de Finalizacion debe ser mayor a"+existe.getFechaInicio());
            }


            //Validacion valores agregados
            if (existe.getValoresAgregados()<=0 || (existe.getValoresAgregados() * 100) % 1 != 0) {
                throw new Exception("El valor de valores agregados "+existe.getValoresAgregados()+" es incorrecto");
            }

            //Validacion valor Subtotal
            if (existe.getValorsubtotal()<=0 || (existe.getValorsubtotal() * 100) % 1 != 0) {
                throw new Exception("El valor de subtotal "+existe.getValorsubtotal()+" es incorrecto");
            }

            //Validacion Valor Total
            if (existe.getValortotal()<=0 || (existe.getValortotal() * 100) % 1 != 0) {
                throw new Exception("El valor total "+existe.getValortotal()+" es incorrecto");
            }

            existe.setRenovacionContrato(contratoActualizar.isRenovacionContrato());
            existe.setFechaRenovacion(contratoActualizar.getFechaRenovacion());
            existe.setValoresAgregados(contratoActualizar.getValoresAgregados());
            existe.setMotivoAgregados(contratoActualizar.getMotivoAgregados());
            existe.setValorsubtotal(contratoActualizar.getValorsubtotal());
            existe.setValortotal(existe.getValorsubtotal()+(existe.getValorsubtotal()*existe.getValoresAgregados()));
            return contratoRepository.save(existe);
        }else {
            throw new Exception("El contrato con ID:"+idContrato+" no existe");
        }
    }

    public boolean deleteContrato (Long idContrato) throws Exception{
        Contrato existe = findByIdContrato(idContrato);
        if (existe != null){
            contratoRepository.delete(existe);
            return true;
        }else {
            throw new Exception("El contrato con ID:"+idContrato+" no existe");
        }
    }

    public List<Contrato> findByPlan(int planSeguro){
        List<Contrato> contratos = findAll();
        List<Contrato> contratosPlan = new ArrayList<>();

        for (Contrato contrato: contratos){
            if (contrato.getPlanSeguro().getIdPlan() == planSeguro){
                contratosPlan.add(contrato);
            }
        }
        return contratosPlan;
    }

    public List<Contrato> findByFechas (Date fechaInicial, Date fechaFinal){
        List<Contrato> contratosEnRango = findAll().stream()
                .filter(contrato -> contrato.getFechaInicio().compareTo(fechaInicial)>= 0 &&
                        contrato.getFechaInicio().compareTo(fechaFinal)<=0)
                .collect(Collectors.toList());
        return contratosEnRango;
    }
}
