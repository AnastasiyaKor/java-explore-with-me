package ru.practicum.ewm_server.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.ewm_server.dto.ParticipationRequestDto;
import ru.practicum.ewm_server.entity.Event;
import ru.practicum.ewm_server.entity.Request;
import ru.practicum.ewm_server.entity.User;

import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class RequestMapper {
    public static Request toRequest(ParticipationRequestDto participationRequestDto) {
        return Request.builder()
                .created(participationRequestDto.getCreated())
                .event(Event.builder().id(participationRequestDto.getId()).build())
                .id(participationRequestDto.getId())
                .requester(User.builder().id(participationRequestDto.getId()).build())
                .status(participationRequestDto.getStatus())
                .build();
    }

    public static ParticipationRequestDto toParticipationRequestDto(Request request) {
        return ParticipationRequestDto.builder()
                .created(request.getCreated())
                .event(request.getEvent().getId())
                .id(request.getId())
                .requester(request.getRequester().getId())
                .status(request.getStatus())
                .build();
    }

    public static List<ParticipationRequestDto> toListParticipationRequestDto(List<Request> requests) {
        return requests.stream()
                .map(RequestMapper::toParticipationRequestDto)
                .collect(Collectors.toList());
    }
}
