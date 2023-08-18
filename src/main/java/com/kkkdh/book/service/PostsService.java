package com.kkkdh.book.service;

import com.kkkdh.book.domain.posts.Posts;
import com.kkkdh.book.domain.posts.PostsRepository;
import com.kkkdh.book.web.dto.PostsResponseDto;
import com.kkkdh.book.web.dto.PostsSaveRequestDto;
import com.kkkdh.book.web.dto.PostsUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class PostsService {

    private final PostsRepository postsRepository;

    @Transactional
    public Long save(PostsSaveRequestDto requestDto){
        return postsRepository.save(requestDto.toEntity()).getId();
    }

    @Transactional
    public Long update(Long id, PostsUpdateRequestDto requestDto){
        Posts posts = postsRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("해당 사용자가 없습니다. id = " + id)
        );
        posts.update(requestDto);

        return posts.getId();
    }

    public PostsResponseDto findById(Long id){
        Posts entity = postsRepository.findById(id).orElseThrow(() ->
            new IllegalArgumentException("해당 사용자가 없습니다. id = " + id)
        );
        return new PostsResponseDto(entity);
    }
}
