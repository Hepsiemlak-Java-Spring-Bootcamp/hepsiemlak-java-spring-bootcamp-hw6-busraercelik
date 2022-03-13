package com.bsr.emlakburada.service;

import com.bsr.emlakburada.dto.MessageRequestDTO;
import com.bsr.emlakburada.dto.response.MessageResponseDTO;
import com.bsr.emlakburada.model.Message;
import com.bsr.emlakburada.repository.MessageRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.bsr.emlakburada.service.MessageTranformerService.convertToMessage;

@Service
public class MessageService {

    private final MessageRepository messageRepository;

    @Autowired
    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    public List<MessageResponseDTO> getAllMessages() {
       return messageRepository.findAll()
               .stream()
               .map(MessageTranformerService::convertToMessageResponseDTO)
               .collect(Collectors.toList());
    }

    public MessageResponseDTO getMessageById(long id) {
        Optional<Message> messageOptional = messageRepository.findById(id);
        return messageOptional.map(MessageTranformerService::convertToMessageResponseDTO).orElse(null);
    }

    public void saveMessage(MessageRequestDTO messageRequestDTO) {
        messageRepository.save(convertToMessage(messageRequestDTO));
    }

}
