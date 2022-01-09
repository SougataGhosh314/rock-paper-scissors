package com.sougata.rockpaperscissors.dto;

import com.sougata.rockpaperscissors.model.Move;
import com.sougata.rockpaperscissors.model.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StatusObject {
    String token;
    int clientScore;
    int serverScore;
    Status status;
}
