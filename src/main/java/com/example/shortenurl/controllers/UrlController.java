package com.example.shortenurl.controllers;

import com.example.shortenurl.dtos.*;
import com.example.shortenurl.exceptions.UrlNotFoundException;
import com.example.shortenurl.exceptions.UserNotFoundException;
import com.example.shortenurl.models.ShortenedUrl;
import com.example.shortenurl.services.UrlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class UrlController {

    private UrlService urlService;
    @Autowired
    public UrlController(UrlService urlService) {
        this.urlService = urlService;
    }

    public ShortenUrlResponseDto shortenUrl(ShortenUrlRequestDto requestDto) {
        ShortenUrlResponseDto responseDto = new ShortenUrlResponseDto();
        try {
            ShortenedUrl shortenedUrl = urlService.shortenUrl(requestDto.getOriginalUrl(), requestDto.getUserId());
            responseDto.setShortUrl(shortenedUrl.getShortUrl());
            responseDto.setExpiresAt(shortenedUrl.getExpiresAt());
            responseDto.setStatus(ResponseStatus.SUCCESS);
        } catch (UserNotFoundException e) {
            responseDto.setStatus(ResponseStatus.FAILURE);
        }
        return responseDto;
    }

    public ResolveShortenUrlResponseDto resolveShortenedUrl(ResolveShortenUrlRequestDto requestDto) {
        ResolveShortenUrlResponseDto responseDto = new ResolveShortenUrlResponseDto();
        try {
            responseDto.setOriginalUrl(urlService.resolveShortenedUrl(requestDto.getShortenUrl()));
            responseDto.setStatus(ResponseStatus.SUCCESS);
        } catch (UrlNotFoundException e) {
            responseDto.setStatus(ResponseStatus.FAILURE);
        }
        return responseDto;
    }
}
