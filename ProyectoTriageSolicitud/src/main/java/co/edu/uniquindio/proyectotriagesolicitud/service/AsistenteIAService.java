package co.edu.uniquindio.proyectotriagesolicitud.service;

import co.edu.uniquindio.proyectotriagesolicitud.dto.request.SugerenciaIARequest;
import co.edu.uniquindio.proyectotriagesolicitud.dto.response.SugerenciaIAResponse;

public interface AsistenteIAService {

    SugerenciaIAResponse sugerir(SugerenciaIARequest request);
}