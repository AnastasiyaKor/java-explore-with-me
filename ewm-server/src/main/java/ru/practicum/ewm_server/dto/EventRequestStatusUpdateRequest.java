package ru.practicum.ewm_server.dto;

import lombok.*;
import ru.practicum.ewm_server.enums.RequestStatusEnum;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EventRequestStatusUpdateRequest {
    private List<Integer> requestIds;
    private RequestStatusEnum status;

}
