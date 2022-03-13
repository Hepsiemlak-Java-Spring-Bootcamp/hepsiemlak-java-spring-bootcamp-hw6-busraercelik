package com.bsr.emlakburada.service;

import com.bsr.emlakburada.MockDataUtil;
import com.bsr.emlakburada.dto.response.MessageResponseDTO;
import com.bsr.emlakburada.model.Message;
import com.bsr.emlakburada.repository.MessageRepository;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@SpringBootTest
public class MessageServiceTest {

    @InjectMocks
    private MessageService messageService;

    @Mock
    private MessageRepository messageRepository;

    @Captor
    ArgumentCaptor<Message> argumentCaptor;

    @Test
    void getAllMessagesTest() {
        List<Message> mockMessages = MockDataUtil.getMockMessages();
        Mockito.when(messageRepository.findAll())
                .thenReturn(mockMessages);

        List<MessageResponseDTO> output = messageService.getAllMessages();
        assertNotNull(output);
        assertEquals(mockMessages.get(0).getId(), output.get(0).getId());
        assertEquals(mockMessages.size(), output.size());
        verify(messageRepository.findAll());
    }

    @Test
    void getMessageByIdTest() {
        Message mockMessage = MockDataUtil.getMockMessages().get(0);
        Mockito.when(messageRepository.findById(mockMessage.getId()))
                .thenReturn(Optional.of(mockMessage));
        MessageResponseDTO result = messageService.getMessageById(mockMessage.getId());
        assertNotNull(result);
        assertEquals(result.getId(), mockMessage.getId());
    }

    @Test
    void saveMessage() {
        Message expectedMessage = MockDataUtil.getMockMessages().get(0);
        messageService.saveMessage(MessageTranformerService.convertToMessageRequestDTO(expectedMessage));
        verify(messageRepository).save(argumentCaptor.capture());
        Message actualMessage = argumentCaptor.getValue();
        assertEquals(actualMessage.getText(), expectedMessage.getText());
        assertEquals(actualMessage.getSentFrom(), expectedMessage.getSentFrom());
        assertEquals(actualMessage.getSentTo(), expectedMessage.getSentTo());
    }


}
