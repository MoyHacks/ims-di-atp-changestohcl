package com.coppel.dto.logs;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class FactoryJeager {
    private String jeagerAgentHost;
    private boolean flagJaeger;
    private boolean flagJeagerLogsSpan;
}
