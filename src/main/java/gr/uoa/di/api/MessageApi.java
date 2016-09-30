package gr.uoa.di.api;

import gr.uoa.di.dao.MessageEntity;
import gr.uoa.di.dao.UserEntity;
import gr.uoa.di.dto.message.MessageRequestDto;
import gr.uoa.di.dto.message.MessageResponseDto;
import gr.uoa.di.exception.message.CannotDeleteMessageException;
import gr.uoa.di.exception.message.MessageFieldsException;
import gr.uoa.di.exception.message.MessageNotFoundException;
import gr.uoa.di.exception.user.UserNotFoundException;
import gr.uoa.di.mapper.MessageMapper;
import gr.uoa.di.repo.MessageRepository;
import gr.uoa.di.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.method.P;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/api/messages")
public class MessageApi {

    @Autowired
    MessageRepository messageRepository;

    @Autowired
    UserService userService;

    @Autowired
    MessageMapper messageMapper;

    @RequestMapping(value = "/{messageId}")
    public MessageResponseDto getMessage(@PathVariable int messageId) {
        MessageEntity messageEntity = messageRepository.findOneById(messageId);
        if (messageEntity == null) {
            throw new MessageNotFoundException();
        }

        String currentUser = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!messageEntity.getFrom().getUsername().equals(currentUser) &&
                !messageEntity.getTo().getUsername().equals(currentUser)) {
            throw new MessageNotFoundException();
        }

        if (messageEntity.getFrom().getUsername().equals(currentUser) && messageEntity.getDeletedsender() &&
                !messageEntity.getTo().getUsername().equals(currentUser)) {
            throw new MessageNotFoundException();
        } else if (messageEntity.getTo().getUsername().equals(currentUser) && messageEntity.getDeletedreceiver() &&
                !messageEntity.getFrom().getUsername().equals(currentUser)) {
            throw new MessageNotFoundException();
        }

        if (!messageEntity.getIsread()) {
            messageEntity.setIsread(true);
            messageRepository.save(messageEntity);
        }

        return messageMapper.mapMessageEntityToMessageResponseDto(messageEntity);
    }

    @RequestMapping(value="/new", method = RequestMethod.GET)
    public Long newMessageCount() {
        return messageRepository.countByTo_UsernameAndIsreadFalseAndDeletedreceiverFalse(
                (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
    }

    @RequestMapping(method = RequestMethod.POST)
    public void newMessage(@RequestBody MessageRequestDto dto) {
        Date curDate = new Date();

        if (dto.getTo() == null || dto.getSubject() == null || dto.getMessage() == null ||
                dto.getTo().length() == 0 || dto.getSubject().length() == 0 || dto.getMessage().length() == 0) {
            throw new MessageFieldsException();
        }

        UserEntity from = userService.getUserEntity((String) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        UserEntity to = userService.getUserEntity(dto.getTo());

        if (to == null) {
            throw new UserNotFoundException();
        }

        MessageEntity messageEntity = new MessageEntity();
        messageEntity.setFrom(from);
        messageEntity.setTo(to);
        messageEntity.setSubject(dto.getSubject());
        messageEntity.setMessage(dto.getMessage());
        messageEntity.setSentdate(curDate);
        messageRepository.save(messageEntity);
    }

    @RequestMapping(value = "/received")
    public Page<MessageResponseDto> getReceivedMessages(Pageable pageable) {
        return messageRepository.findByTo_UsernameAndDeletedreceiverFalseOrderBySentdateDesc(
                (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal(), pageable).
                map(messageMapper::mapMessageEntityToMessageResponseDto);
    }

    @RequestMapping(value = "/sent")
    public Page<MessageResponseDto> getSentMessages(Pageable pageable) {
        return messageRepository.findByFrom_UsernameAndDeletedsenderFalseOrderBySentdateDesc(
                (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal(), pageable).
                map(messageMapper::mapMessageEntityToMessageResponseDto);
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/received/{messageId}")
    public void deleteReceivedMessage(@PathVariable int messageId) {
        MessageEntity messageEntity = messageRepository.findOneById(messageId);
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (messageEntity.getTo().getUsername().equals(username)) {
            messageEntity.setDeletedreceiver(true);
        } else {
            throw new CannotDeleteMessageException();
        }

        if (messageEntity.getDeletedreceiver() && messageEntity.getDeletedsender()) {
            messageRepository.delete(messageEntity);
        } else {
            messageRepository.save(messageEntity);
        }
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/sent/{messageId}")
    public void deleteSentMessage(@PathVariable int messageId) {
        MessageEntity messageEntity = messageRepository.findOneById(messageId);
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (messageEntity.getFrom().getUsername().equals(username)) {
            messageEntity.setDeletedsender(true);
        } else {
            throw new CannotDeleteMessageException();
        }

        if (messageEntity.getDeletedreceiver() && messageEntity.getDeletedsender()) {
            messageRepository.delete(messageEntity);
        } else {
            messageRepository.save(messageEntity);
        }
    }
}
