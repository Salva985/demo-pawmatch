package com.example.demo.dto.chat;

import com.example.demo.model.Mensaje;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ChatRequestDTO {


    @NotNull
    private Long usuario1Id;

    @NotNull
    private Long usuario2Id;

    private Long mensajeId;
}
