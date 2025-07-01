package com.mbarca.vete.service.impl;

import com.mbarca.vete.domain.AfipResponse;
import com.mbarca.vete.domain.Bill;
import com.mbarca.vete.dto.request.BillRequestDto;
import com.mbarca.vete.repository.BillRepository;
import com.mbarca.vete.service.BillService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

@Service
public class BillServiceImpl implements BillService {
    BillRepository billRepository;

    public BillServiceImpl(BillRepository billRepository) {
        this.billRepository = billRepository;
    }

    @Override
    public String saveBill(BillRequestDto billRequestDto, AfipResponse afipResponse) {
        Integer response = billRepository.saveBill(mapDtoToBill(billRequestDto, afipResponse));
        if (response.equals(0)) {
            return "Error al guardar la factura!";
        }
        return "Factura guardada en la base de datos correctamente!";
    }

    @Override
    public Bill getByllById (Long id) {
        return billRepository.getBillById(id);
    }

    @Override
    public List<Bill> getBillsByDate (Date dateStart, Date dateEnd) {
        return billRepository.getBillsByDate(dateStart, dateEnd);
    }

    private Bill mapDtoToBill (BillRequestDto billRequestDto, AfipResponse afipResponse) {

        Bill bill = new Bill();
        bill.setFecha(new Date());
        bill.setTipo(billRequestDto.getTipo());
        bill.setNumero(billRequestDto.getNumero());
        bill.setTipoDocumento(billRequestDto.getTipoDocumento());
        bill.setDocumento(billRequestDto.getDocumento());
        bill.setNombre(billRequestDto.getNombre());
        bill.setImporteTotal(billRequestDto.getImporteTotal());
        bill.setImporteNoGravado(billRequestDto.getImporteNoGravado());
        bill.setImporteGravado(billRequestDto.getImporteGravado());
        bill.setImporteIva(billRequestDto.getImporteIva());
        bill.setCae(afipResponse.getCae());
        bill.setCaeFchVto(afipResponse.getCaeFchVto());
        bill.setEstado(afipResponse.getStatus());
        bill.setErrors(afipResponse.getErrors());
        bill.setObservations(afipResponse.getObservations());
        bill.setBillProducts(billRequestDto.getBillProducts());
        bill.setCondicionIvaDescripcion(afipResponse.getCondicionIvaDescripcion());
        return bill;
    }
}
