package com.kkkdh.book.service;

import com.kkkdh.book.domain.posts.Posts;
import com.kkkdh.book.domain.posts.PostsRepository;
import com.kkkdh.book.web.dto.PostsListResponseDto;
import com.kkkdh.book.web.dto.PostsResponseDto;
import com.kkkdh.book.web.dto.PostsSaveRequestDto;
import com.kkkdh.book.web.dto.PostsUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

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

    @Transactional
    public void delete(Long id){
        Posts posts = postsRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException("해당 게시글이 없습니다. id = " + id));

        postsRepository.delete(posts);
    }

    public PostsResponseDto findById(Long id){
        Posts entity = postsRepository.findById(id).orElseThrow(() ->
            new IllegalArgumentException("해당 사용자가 없습니다. id = " + id)
        );
        return new PostsResponseDto(entity);
    }

    public List<PostsListResponseDto> findAllDesc(){
        return postsRepository.findAllDesc().stream()
                .map(PostsListResponseDto::new)
                .collect(Collectors.toList());
    }
}
