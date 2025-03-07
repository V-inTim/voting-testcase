package com.example.dto;

import lombok.Data;

@Data
public class ViewMessage extends Message{

    public ViewMessage() {
        super("view");
    }
}
