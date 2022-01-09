package com.sougata.rockpaperscissors.dto;

import com.sougata.rockpaperscissors.model.Move;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResponseObject {
    private int clientScore;
    private int serverScore;
    private Move move;
}
