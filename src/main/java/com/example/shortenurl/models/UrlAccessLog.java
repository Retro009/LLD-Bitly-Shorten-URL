package com.example.shortenurl.models;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.Data;

@Data
@Entity
public class UrlAccessLog extends BaseModel{
    @ManyToOne
    private ShortenedUrl shortenedUrl;
    private long accessedAt;
}
