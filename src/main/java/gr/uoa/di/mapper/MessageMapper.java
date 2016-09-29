package gr.uoa.di.mapper;

import gr.uoa.di.dao.MessageEntity;
import gr.uoa.di.dto.message.MessageResponseDto;
import org.springframework.stereotype.Component;

@Component
public class MessageMapper {

    public MessageResponseDto mapMessageEntityToMessageResponseDto(MessageEntity messageEntity) {
        MessageResponseDto dto = new MessageResponseDto();
        dto.setId(messageEntity.getId());
        dto.setFrom(messageEntity.getFrom().getUsername());
        dto.setTo(messageEntity.getTo().getUsername());
        dto.setSubject(messageEntity.getSubject());
        dto.setMessage(messageEntity.getMessage());
        dto.setDate(messageEntity.getSentdate());
        dto.setIsread(messageEntity.getIsread());
        return dto;
    }
}
