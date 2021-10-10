package com.kdt.post.service;

import com.kdt.domain.post.PostRepository;
import com.kdt.exception.NotFoundException;
import com.kdt.post.dto.PostDto;
import java.text.MessageFormat;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {

    private final PostRepository postRepository;
    private final PostConvertor postConvertor;

    @Transactional
    public Long save(PostDto postDto) {
        return postRepository.save(postConvertor.convertPostDtoToPost(postDto)).getId();
    }

    public Page<PostDto> findAll(Pageable pageable) {
        return postRepository.findAll(pageable).map(postConvertor::convertPostToPostDto);
    }

    public PostDto findOne(Long id) {
        return postRepository.findById(id)
                .map(postConvertor::convertPostToPostDto)
                .orElseThrow(() -> new NotFoundException(MessageFormat.format("not found id : {0}", id)));
    }

    @Transactional
    public Long update(PostDto postDto) {
        return postRepository.findById(postDto.getId())
                .map(post -> postConvertor.convertPostDtoToPost(postDto))
                .orElseThrow(() -> new NotFoundException(MessageFormat.format("not found id : {0}", postDto.getId())))
                .getId();
    }
}
