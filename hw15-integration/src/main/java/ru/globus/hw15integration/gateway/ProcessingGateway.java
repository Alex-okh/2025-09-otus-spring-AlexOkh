package ru.globus.hw15integration.gateway;

import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;

@MessagingGateway(defaultRequestChannel = "processDirectoryChannel")
public interface ProcessingGateway {

    @Gateway(requestChannel = "processDirectoryChannel")
    void process(String directory);
}
