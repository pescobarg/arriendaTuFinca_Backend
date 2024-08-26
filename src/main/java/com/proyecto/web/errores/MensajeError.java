package com.proyecto.web.errores;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MensajeError {
    private String mensaje;
    private Date fecha;
    private String error;
}
