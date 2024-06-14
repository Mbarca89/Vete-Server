package com.mbarca.vete.service;

import com.mbarca.vete.dto.request.BillRequestDto;

public interface AfipService {
    public String consultarPuntosVenta();
    public String generarComprobante(BillRequestDto billRequestDto);
//    public String consultarPuntosVenta2();
}
