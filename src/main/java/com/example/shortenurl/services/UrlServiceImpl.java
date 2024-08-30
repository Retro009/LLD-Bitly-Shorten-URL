package com.example.shortenurl.services;

import com.example.shortenurl.exceptions.UrlNotFoundException;
import com.example.shortenurl.exceptions.UserNotFoundException;
import com.example.shortenurl.models.ShortenedUrl;
import com.example.shortenurl.models.UrlAccessLog;
import com.example.shortenurl.models.User;
import com.example.shortenurl.repositories.ShortenedUrlRepository;
import com.example.shortenurl.repositories.UrlAccessLogRepository;
import com.example.shortenurl.repositories.UserRepository;
import com.example.shortenurl.utils.ShortUrlGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UrlServiceImpl implements UrlService{

    @Autowired
    private ShortenedUrlRepository shortenedUrlRepository;
    @Autowired
    private UrlAccessLogRepository urlAccessLogRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ShortUrlGenerator shortUrlGenerator;

    @Override
    public ShortenedUrl shortenUrl(String url, int userId) throws UserNotFoundException {
        User user = userRepository.findById(userId).orElseThrow(()-> new UserNotFoundException("User Not Found"));
        ShortenedUrl shortenedUrl = new ShortenedUrl();
        shortenedUrl.setOriginalUrl(url);
        shortenedUrl.setShortUrl(shortUrlGenerator.generateShortUrl());
        shortenedUrl.setUser(user);
        int day=0;
        switch(user.getUserPlan()){
            case FREE -> day = 1;
            case TEAM -> day = 7;
            case BUSINESS -> day = 30;
            case ENTERPRISE -> day = 365;
        }
        long planTime = day * 24 * 60 * 60 * 1000;
        shortenedUrl.setExpiresAt(System.currentTimeMillis()+planTime);

        return shortenedUrlRepository.save(shortenedUrl);
    }

    @Override
    public String resolveShortenedUrl(String shortUrl) throws UrlNotFoundException {
        Optional<ShortenedUrl> optionalShortenedUrl = shortenedUrlRepository.findByShortUrl(shortUrl);
        if(optionalShortenedUrl.isEmpty())
            throw new UrlNotFoundException("Url Not Found!!");
        ShortenedUrl shortenedUrl = optionalShortenedUrl.get();
        if(shortenedUrl.getExpiresAt()-System.currentTimeMillis()<0)
            throw new UrlNotFoundException("Url is Expired!!");
        if(shortenedUrl.getShortUrl().equals(shortUrl)){
            UrlAccessLog urlAccessLog = new UrlAccessLog();
            urlAccessLog.setShortenedUrl(shortenedUrl);
            urlAccessLog.setAccessedAt(System.currentTimeMillis());
            urlAccessLogRepository.save(urlAccessLog);
        }
        return shortenedUrl.getOriginalUrl();
    }
}
