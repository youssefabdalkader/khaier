package com.example.khaier.controller;

import com.example.khaier.dto.request.BannerRequestDto;
import com.example.khaier.dto.response.BannerResponseDto;
import com.example.khaier.factory.impl.SuccessResponseFactory200;
import com.example.khaier.service.BannerService;
import com.example.khaier.service.Impl.BannerImageServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/${api.version}/banner")
@RequiredArgsConstructor
public class BannerController {
    private final BannerService bannerService;
    private final SuccessResponseFactory200 responseFactory;
    private final BannerImageServiceImpl bannerImageService;

    @GetMapping(value = "/image/{title}", produces = MediaType.ALL_VALUE, consumes = MediaType.ALL_VALUE)
    public ResponseEntity<?> getImage(@PathVariable String title){
        byte[] imageData = bannerImageService.downloadImage(title);
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.valueOf("image/png"))
                .body(imageData);
    }

    @GetMapping
    public ResponseEntity<?> getAllBanners(){
        List<BannerResponseDto> banners = bannerService.findAllBanners();
        return ResponseEntity.status(HttpStatus.OK)
                .body(responseFactory.createResponse(banners, "Banners returned successfully"));
    }

    @PostMapping(value = "/{userId}",consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<?> save(@ModelAttribute BannerRequestDto bannerRequestDto,@PathVariable Long userId){
        BannerResponseDto bannerResponseDto = bannerService.save(bannerRequestDto,userId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(responseFactory.createResponse(bannerResponseDto, "Banner saved successfully"));
    }
}
