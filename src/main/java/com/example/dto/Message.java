package com.example.dto;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = CreateTopicMessage.class, name = "create topic"),
        @JsonSubTypes.Type(value = CreateVoteMessage.class, name = "create vote"),
        @JsonSubTypes.Type(value = UsernameMessage.class, name = "username"),
        @JsonSubTypes.Type(value = ReplyMessage.class, name = "response"),
        @JsonSubTypes.Type(value = ViewMessage.class, name = "view"),
        @JsonSubTypes.Type(value = ViewTopicMessage.class, name = "view topic"),
        @JsonSubTypes.Type(value = ViewVoteMessage.class, name = "view vote"),
        @JsonSubTypes.Type(value = DeleteMessage.class, name = "delete"),
})
@AllArgsConstructor
@NoArgsConstructor
@Data
public abstract class Message {
    private String type;
}