package com.mbarca.vete.service;

import com.mbarca.vete.domain.AfipResponse;
import com.mbarca.vete.dto.request.BillRequestDto;

public interface AfipService {
    public String consultarPuntosVenta();
    public String consultarUltimoComprobante(String type);
    public AfipResponse generarComprobante(BillRequestDto billRequestDto);

}
