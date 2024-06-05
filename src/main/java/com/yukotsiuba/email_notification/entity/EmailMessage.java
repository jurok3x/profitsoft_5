package com.yukotsiuba.email_notification.entity;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.time.Instant;

@Data
@Builder
@Document(indexName="emails")
public class EmailMessage {

    @Id
    private String id;

    @Field(type = FieldType.Text)
    private String subject;

    @Field(type = FieldType.Text)
    private String content;

    @Field(type = FieldType.Keyword)
    private String to;

    @Field(type = FieldType.Date)
    private Instant lastSentTime;

    @Field(type = FieldType.Integer)
    private Integer attempts;

    private String errorMessage;

    @Field(type = FieldType.Keyword)
    private EmailStatus status;
}
