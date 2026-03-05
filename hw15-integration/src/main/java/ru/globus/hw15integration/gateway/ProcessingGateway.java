package ru.globus.hw15integration.gateway;

import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;
import java.util.List;

@MessagingGateway(defaultRequestChannel = "processDirectoryChannel")
public interface ProcessingGateway {

    @Gateway(requestChannel = "processDirectoryChannel")
    List<String> process(String directory);
}
